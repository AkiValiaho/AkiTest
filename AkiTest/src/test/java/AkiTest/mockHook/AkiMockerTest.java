package AkiTest.mockHook;

import AkiTest.executors.testClasses.BeerServiceHolder;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Collections;

import static AkiTest.assertz.Assertion.assertTrue;

/**
 * Created by vagrant on 3/3/17.
 */
public class AkiMockerTest {
    private AkiMocker akiMocker;
    private BeerServiceHolder beerServiceHolder;

    @Before
    public void setUp() throws Exception {
        this.beerServiceHolder = new BeerServiceHolder(null);
        this.akiMocker = new AkiMocker();
    }

    @Test
    public void mockNonNoArgsBeerservice() throws Exception {
        Field beerService = beerServiceHolder.getClass().getDeclaredField("beerService");
        assertTrue(beerServiceHolder.getBeerService() == null);
        this.akiMocker.mock(Collections.singletonList(beerService), beerServiceHolder);
        assertTrue(beerServiceHolder.getBeerService() != null);
    }
    @Test
    public void mockNoArgsBeerService() throws Exception {
        Field beerService = beerServiceHolder.getClass().getDeclaredField("noargsBeerService");
        assertTrue(beerServiceHolder.getNoargsBeerService() == null);
        this.akiMocker.mock(Collections.singletonList(beerService), beerServiceHolder);
        assertTrue(beerServiceHolder.getNoargsBeerService() != null);
    }
}