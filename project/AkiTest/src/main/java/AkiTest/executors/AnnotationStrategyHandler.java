package AkiTest.executors;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by vagrant on 3/1/17.
 */
public interface AnnotationStrategyHandler {
    Object handleOncePerTestClassAnnotations(Class<?> declaringClass) throws InstantiationException, IllegalAccessException, InvocationTargetException;

    void handleOncePerTestAnnotations(Class<?> declaringClass, Object declaredClassInstance) throws IllegalAccessException, InvocationTargetException, InstantiationException;
}
