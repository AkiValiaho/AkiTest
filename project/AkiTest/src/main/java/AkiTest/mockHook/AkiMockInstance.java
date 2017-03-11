package AkiTest.mockHook;

import annotations.AkiMockUp;
import annotations.MockProperty;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Created by vagrant on 3/3/17.
 */
public class AkiMockInstance<T> {
    private final MockMethodParser akiMockParser;
    private final Type[] actualTypeArguments;
    private final MethodInterceptor methodInterceptor;
    private List<MockMethod> mockMethods = new ArrayList<>();
    private Map<Method, Integer> invocationMap;

    public AkiMockInstance() {
        //TODO Test with interfaces and abstract classes
        this.akiMockParser = new MockMethodParser();
        this.invocationMap = new HashMap<>();
        Type enclosingClass = this.getClass().getGenericSuperclass();
        mockMethods = akiMockParser.parseMockMethods(this);
        Class<?> rawType = ((ParameterizedTypeImpl) enclosingClass).getRawType();
        this.actualTypeArguments = ((ParameterizedTypeImpl) enclosingClass).getActualTypeArguments();
        this.methodInterceptor = (o, method, objects, methodProxy) -> {
                    //Invoke method
                    Optional<MockMethod> first = mockMethods.stream()
                            .filter(streamMethod -> streamMethod.getMethod().getName().equals(method.getName()))
                            .findFirst();
                    if (first.isPresent()) {
                        checkInvocationNumberFromStateMap(first.get());
                        //Check if allowed to invoke first
                        Method method1 = first.get().getMethod();
                        method1.setAccessible(true);
                        return method1.invoke(this, objects);
                    }
                return null;
            };
    }

    private void checkInvocationNumberFromStateMap(MockMethod mockMethod) {
        Method method = mockMethod.getMethod();
        AkiMockUp annotation = method.getAnnotation(AkiMockUp.class);
        int hit = annotation.hit();
        if (invocationMap.containsKey(method)) {
            Integer numberOfTimesInvoked = invocationMap.get(method);
            numberOfTimesInvoked++;
            //Verify allowed to invoke additional times
            if (numberOfTimesInvoked > hit) {
                throw new AssertionError("Method " + method.getName() + " invoked " + numberOfTimesInvoked + " times. Should have " +
                        "been invoked " + hit + " times");
            }
            invocationMap.put(method, numberOfTimesInvoked);
        } else {
            //Should the method even be in the invocation map?
            if (!(hit == MockProperty.ANYTIME)) {
                invocationMap.put(method, 1);
            }
        }
    }

    public T getMockInstance() {
        //Create an instance from the actual type arguments
        Type actualTypeArgument = actualTypeArguments[0];
        try {
            Objenesis objenesis = new ObjenesisStd();
            Class<?> aClass = ((Class) actualTypeArgument).getClassLoader().loadClass(((Class) actualTypeArgument).getCanonicalName());
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(aClass);
            enhancer.setCallbackType(methodInterceptor.getClass());
            Class<T> aClass1 = enhancer.createClass();
            Enhancer.registerCallbacks(aClass1, new Callback[] {methodInterceptor});
            //Use objenesis to bypass constructor instance creation
            return objenesis.newInstance(aClass1);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


}
