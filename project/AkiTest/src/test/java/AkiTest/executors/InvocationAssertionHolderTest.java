package AkiTest.executors;

import mockit.Deencapsulation;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vagrant on 3/26/17.
 */
public class InvocationAssertionHolderTest {
    private InvocationAssertionHolder invocationAssertionHolder;

    @Before
    public void setUp() throws Exception {
        this.invocationAssertionHolder = InvocationAssertionHolder.getInstance();
        Map<String, List<Method>> mockMethodsByTestMethod = new HashMap<>();
        //TODO Find a way to mock the methods to this list
        Deencapsulation.setField(this.invocationAssertionHolder, "mockMethodsByTestMethod",mockMethodsByTestMethod);
    }

    @Test
    public void assertInvocationHitsMatch() throws Exception {
        invocationAssertionHolder.assertInvocationHitsMatch();
    }

    @Test
    public void putMockMethod() throws Exception {
    }

    @Test
    public void checkMockInvocation() throws Exception {
    }

    @Test
    public void getInstance() throws Exception {
    }

}