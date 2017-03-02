package AkiTest.mockHook;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by vagrant on 3/1/17.
 */
public interface MockLibraryHook {
    void mock(List<Field> annotatedFields, Object instantiatedClass);
}
