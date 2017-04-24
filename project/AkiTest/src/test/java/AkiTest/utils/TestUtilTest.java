package AkiTest.utils;

import AkiTest.executors.testClasses.BeerService;
import AkiTest.executors.testClasses.BeerServiceHolder;
import AkiTest.mockHook.AkiMockInstance;
import annotations.AkiMockUp;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by vagrant on 4/24/17.
 */
public class TestUtilTest {
    private BeerServiceHolder beerServiceHolder;

    @Before
    public void setUp() throws Exception {
        //Create some class under test
        this.beerServiceHolder = new BeerServiceHolder();
    }

    @Test
    public void setField() throws Exception {
        BeerService mockInstance = new AkiMockInstance<BeerService>() {
            @AkiMockUp(hit = 2)
            public String getNoArgsString() {
                return "test succesfull";
            }
        }.getMockInstance();
        TestUtil.setField(this.beerServiceHolder, BeerServiceHolder.class, "beerService", mockInstance);
        BeerService beerService = this.beerServiceHolder.getBeerService();
        assertEquals(mockInstance.getNoArgsString(), beerServiceHolder.getBeerService().getNoArgsString());
    }

}