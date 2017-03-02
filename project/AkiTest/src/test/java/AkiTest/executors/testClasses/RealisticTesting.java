package AkiTest.executors.testClasses;

import AkiTest.assertz.Assertion;
import com.akivaliaho.AkiTest.AkiMock;
import com.akivaliaho.AkiTest.Before;
import com.akivaliaho.AkiTest.BeforeClass;
import com.akivaliaho.AkiTest.Test;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by vagrant on 2/25/17.
 */
@Slf4j
public class RealisticTesting {

    @AkiMock
    @Getter
    @Setter
    BeerService beerService;

    @BeforeClass
    public void staticInitialization() {
        log.debug("Static initialization");
    }

    @Before
    public void init() {
        log.debug("Test init is called");
    }

    @Test(expected = AssertionError.class)
    public void somethingWild() {
        log.debug("Something wild happened");
        Assertion.assertTrue(false);
    }

    @Test
    public void arealtest() {
        log.debug("This test is more real than you are");
    }
}
