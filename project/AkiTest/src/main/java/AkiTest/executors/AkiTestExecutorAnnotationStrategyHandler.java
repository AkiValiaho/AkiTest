package AkiTest.executors;

import com.akivaliaho.AkiTest.Before;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

/**
 * Created by vagrant on 3/1/17.
 */
public class AkiTestExecutorAnnotationStrategyHandler implements AnnotationStrategyHandler {
    @Override
    public void handleOncePerTestClassAnnotations() {

    }
    @Override
    public void handleOncePerTestAnnotations(Class<?> declaringClass) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        //Check if @Before is declared
        handleBefore(declaringClass);
    }

    private void handleBefore(Class<?> declaringClass) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        Optional<Method> first = Arrays.stream(declaringClass.getMethods())
                .filter(method -> method.getAnnotation(Before.class) != null)
                .findFirst();
        if (first.isPresent()) {
            //Invoke before method
            first.get().invoke(declaringClass.newInstance(), new Object[0]);
        }
    }

}
