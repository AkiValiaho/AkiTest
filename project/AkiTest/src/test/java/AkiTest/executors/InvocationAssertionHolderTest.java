package AkiTest.executors;

import AkiTest.mockHook.MockMethod;
import annotations.AkiMockUp;
import lombok.extern.slf4j.Slf4j;
import mockit.Deencapsulation;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vagrant on 3/26/17.
 */
@Slf4j
public class InvocationAssertionHolderTest {
    private InvocationAssertionHolder invocationAssertionHolder;

    @Before
    public void setUp() throws Exception {
        this.invocationAssertionHolder = InvocationAssertionHolder.getInstance();
        Map<String, List<MockMethod>> mockMethodsByTestMethod = new HashMap<>();
        //Create one mocked annotation
        List<MockMethod> mockedMethod = createMockedMethod();
        mockMethodsByTestMethod.put("tmpAnnotated", mockedMethod);
        Deencapsulation.setField(this.invocationAssertionHolder, "mockMethodsByTestMethod",mockMethodsByTestMethod);
        Map<Method, Integer> invocationHolder = new HashMap<>();
        invocationHolder.put(mockedMethod.get(0).getMethod(), 2);
        Deencapsulation.setField(this.invocationAssertionHolder, "invocationholder", invocationHolder);
    }

    private List<MockMethod> createMockedMethod() throws NoSuchMethodException, IOException {
        List<MockMethod> arrayList = new ArrayList<>();
        Method tmpAnnotated = this.getClass().getMethod("tmpAnnotated", null);
        arrayList.add(new MockMethod(tmpAnnotated));
        return arrayList;
    }


    @Test
    public void assertInvocationHitsMatch() throws Exception {
        PerformanceTestUtil.startTimer();
        invocationAssertionHolder.assertInvocationHitsMatch(this.getClass().getMethod("tmpAnnotated", null));
        PerformanceTestUtil.stopAndLog();
    }

    @AkiMockUp(hit = 2)
    public void tmpAnnotated() {

    }


}