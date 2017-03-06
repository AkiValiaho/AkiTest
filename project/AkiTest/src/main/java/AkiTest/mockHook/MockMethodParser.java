package AkiTest.mockHook;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by vagrant on 3/5/17.
 */
public class MockMethodParser {

    public <T> List<MockMethod> parseMockMethods(AkiMockInstance<T> tAkiMockInstance) {
        Method[] declaredMethods = tAkiMockInstance.getClass().getDeclaredMethods();
        //TODO Check every method has @Mock annotation
        return Arrays.stream(declaredMethods)
                .map(method -> new MockMethod(method))
                .collect(Collectors.toList());
    }
}
