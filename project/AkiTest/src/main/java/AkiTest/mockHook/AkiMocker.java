package AkiTest.mockHook;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by vagrant on 3/2/17.
 */
@Slf4j
public class AkiMocker implements MockLibraryHook {
    @Override
    public void mock(List<Field> annotatedFields, Object instantiatedClass) {
        log.debug("Mocking fields in class");
    }
}
