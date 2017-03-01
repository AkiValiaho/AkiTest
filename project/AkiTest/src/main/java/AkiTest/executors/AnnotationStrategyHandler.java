package AkiTest.executors;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by vagrant on 3/1/17.
 */
public interface AnnotationStrategyHandler {
    void handleOncePerTestClassAnnotations();

    void handleOncePerTestAnnotations(Class<?> declaringClass) throws IllegalAccessException, InvocationTargetException, InstantiationException;
}
