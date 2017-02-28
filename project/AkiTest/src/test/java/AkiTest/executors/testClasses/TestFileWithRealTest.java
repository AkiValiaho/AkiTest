package AkiTest.executors.testClasses;

import AkiTest.assertz.Assertion;
import com.akivaliaho.AkiTest.Test;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by vagrant on 2/25/17.
 */
@Slf4j
public class TestFileWithRealTest {
    @Test(expected = AssertionError.class)
    public void somethingWild() {
        log.debug("Something wild happened");
        Assertion.assertTrue(false);
    }
}
