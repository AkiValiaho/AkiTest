package AkiTest.utils;

import java.lang.reflect.Field;

/**
 * Created by vagrant on 4/24/17.
 */
public class TestUtil {
    public static void setField(Object instance, Class<?> instanceClass, String classField, Object mockInstance) throws NoSuchFieldException, IllegalAccessException {
        Field field = instanceClass.getDeclaredField(classField);
        field.setAccessible(true);
        field.set(instance, mockInstance);
    }

}
