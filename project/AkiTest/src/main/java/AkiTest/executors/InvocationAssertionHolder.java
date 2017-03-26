package AkiTest.executors;

import AkiTest.mockHook.MockMethod;
import annotations.AkiMockUp;
import annotations.MockProperty;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vagrant on 3/19/17.
 */
@Slf4j
public class InvocationAssertionHolder {
    private Map<Method, Integer> invocationholder;
    private Map<String, List<MockMethod>> mockMethodsByTestMethod;
    private InvocationAssertionHolder() {
        this.invocationholder = new HashMap<>();
        this.mockMethodsByTestMethod = new HashMap<>();
    }
    public synchronized void assertInvocationHitsMatch(Method testMethod) {
        assert testMethod != null;
        log.debug("Trying to assert invocations post test method: {}", testMethod.getName());
        List<MockMethod> mockMethods = mockMethodsByTestMethod.get(testMethod.getName());
        if (mockMethods == null) {
            return;
        }
        mockMethods.stream()
                .forEach(method -> {
                    assert invocationholder.get(method.getMethod()) != null;
                    Integer actualInvocations = invocationholder.get(method.getMethod());
                    int hit = method.getMethod().getAnnotation(AkiMockUp.class).hit();
                    if (actualInvocations != hit) {
                        log.debug("Expected annotations differ from actual invocations");
                        throw new AssertionError("Wrong number of invocations detected, actual: " + actualInvocations + " expected invocations: " + hit);
                    }
                });
    }

    public synchronized void putMockMethod(MockMethod mockMethod, String testMethodName) {
        if (mockMethodsByTestMethod.get(testMethodName) != null) {
            List<MockMethod> mockMethods = mockMethodsByTestMethod.get(testMethodName);
            mockMethods.add(mockMethod);
            mockMethodsByTestMethod.put(testMethodName, mockMethods);
        } else {
            List<MockMethod> mockMethods = new ArrayList<>();
            mockMethods.add(mockMethod);
            mockMethodsByTestMethod.put(testMethodName,mockMethods);
        }
    }

    public void checkMockInvocation(String name, MockMethod mockMethod) {
        AkiMockUp annotation = mockMethod.getMethod().getAnnotation(AkiMockUp.class);
        int hit = annotation.hit();
        if (invocationholder.containsKey(mockMethod.getMethod())) {
            Integer numberOfTimesInvoked = invocationholder.get(mockMethod.getMethod());
            numberOfTimesInvoked++;
            //Verify allowed to invoke additional times
            if (numberOfTimesInvoked > hit) {
                throw new AssertionError("Method " + name + " invoked " + numberOfTimesInvoked + " times. Should have " +
                        "been invoked " + hit + " times");
            }
            invocationholder.put(mockMethod.getMethod(), numberOfTimesInvoked);
        } else {
            //Should the method even be in the invocation map?
            if (!(hit == MockProperty.ANYTIME)) {
                putMockMethod(mockMethod, name);
                invocationholder.put(mockMethod.getMethod(), 1);
            }
        }
    }

    private static class LazyLoadedInvocationHandler {
        private static final InvocationAssertionHolder holder = new InvocationAssertionHolder();
    }
    public static InvocationAssertionHolder getInstance() {
        //Loads the inner static class lazily
        return LazyLoadedInvocationHandler.holder;
    }
}
