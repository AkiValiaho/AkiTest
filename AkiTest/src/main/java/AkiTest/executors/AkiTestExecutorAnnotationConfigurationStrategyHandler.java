package AkiTest.executors;

import AkiTest.mockHook.MockLibraryHook;
import annotations.AkiMock;
import annotations.Before;
import annotations.BeforeClass;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by vagrant on 3/1/17.
 */
@Slf4j
public class AkiTestExecutorAnnotationConfigurationStrategyHandler implements AnnotationStrategyHandler {
 @Getter @Setter
 private MockLibraryHook mockLibraryHook;

    public AkiTestExecutorAnnotationConfigurationStrategyHandler(MockLibraryHook mockLibraryHook) {
        this.mockLibraryHook = mockLibraryHook;
    }
    @Override
    public Object handleOncePerTestClassAnnotations(Class<?> declaringClass) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        Object instantiatedClass = handleBeforeClass(declaringClass);
        handleMocks(declaringClass, instantiatedClass);
        return instantiatedClass;
    }

    @Override
    public void handleOncePerTestAnnotations(Class<?> declaringClass, Object declaredClassInstance) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        invokeAnnotatedMethods(declaringClass, declaredClassInstance, Before.class);
    }

    private Object handleBeforeClass(Class<?> declaringClass) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        log.debug("Handling @BeforeClass");
        Object createdInstance = declaringClass.newInstance();
        invokeAnnotatedMethods(declaringClass, createdInstance, BeforeClass.class);
        return createdInstance;
    }

    private void handleMocks(Class<?> declaringClass, Object instantiatedClass) {
        List<Field> annotatedFields = (List<Field>) getAnnotatedMembers(declaringClass, AkiMock.class, Field.class);
        mockLibraryHook.mock(annotatedFields, instantiatedClass);
        System.out.println(annotatedFields);
    }


    private void invokeAnnotatedMethods(Class<?> declaringClass, Object declaredClassInstance, Class<? extends Annotation> annotationType) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        List<Method> methodsToInvoke = (List<Method>) getAnnotatedMembers(declaringClass, annotationType, Method.class);
        if (methodsToInvoke.size() > 0) {
            //Invoke before method
            for (Method method : methodsToInvoke) {
                method.invoke(declaredClassInstance, new Object[0]);
            }
        }
    }

    private List<? extends Member> getAnnotatedMembers(Class<?> declaringClass, Class<? extends Annotation> annotationType, Class<? extends Member> memberClass) {
        if (memberClass.equals(Method.class)) {
            return Arrays.stream(declaringClass.getMethods())
                    .filter(method -> method.getAnnotation(annotationType) != null)
                    .collect(Collectors.toList());
        } else if (memberClass.equals(Field.class)) {
            return Arrays.stream(declaringClass.getDeclaredFields())
                    .filter(field -> {
                        Annotation[] annotations = field.getAnnotations();
                        Boolean contains = false;
                        for (int i = 0; i < annotations.length; i++) {
                            if (annotations[i].annotationType().equals(annotationType)) {
                                contains = true;
                            }
                        }
                        return contains;
                    })
                    .collect(Collectors.toList());
        }
        return null;
    }

}
