package AkiTest.mockHook;

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Method;

/**
 * Created by vagrant on 3/5/17.
 */
public class MockMethod {
 @Getter @Setter
 private final Method method;

    public MockMethod(Method method) {
        this.method = method;
    }
}
