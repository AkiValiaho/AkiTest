package AkiTest.mockHook;

import AkiTest.executors.testClasses.BeerService;
import annotations.AkiMockUp;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

/**
 * Created by vagrant on 3/11/17.
 */
public class AkiMockInstanceTest {
    private BeerService mockInstanceBeerService;

    @Test
    public void testHitOnce() {
        this.mockInstanceBeerService = new AkiMockInstance<BeerService>() {
            @AkiMockUp(hit = 1)
            public String getNoArgsString() {
                return null;
            }
        }.getMockInstance();
        //Lets invoke the instance once
        String noArgsString = this.mockInstanceBeerService.getNoArgsString();
        Assert.assertNull(noArgsString);
    }

    @Test(expected = AssertionError.class)
    public void testAssertionThrownAfterWrongInvocation() {
        this.mockInstanceBeerService = new AkiMockInstance<BeerService>() {
            @AkiMockUp(hit =  1)
            public String getNoArgsString() {
                return null;
            }
        }.getMockInstance();
        this.mockInstanceBeerService.getNoArgsString();
        this.mockInstanceBeerService.getNoArgsString();
    }

    @Test
    public void testInvocedTwice() {
        this.mockInstanceBeerService = new AkiMockInstance<BeerService>() {
            @AkiMockUp(hit =  2)
            public String getNoArgsString() {
                return null;
            }
            @AkiMockUp(hit = 1)
            public void setNoArgsString(String noArgsString) {

            }
        }.getMockInstance();
        this.mockInstanceBeerService.getNoArgsString();
        this.mockInstanceBeerService.setNoArgsString("Hello");
        this.mockInstanceBeerService.getNoArgsString();
    }


    //TODO Find a way to verify after test invocation numbers
    @Test
    public void testHitIndefiniteTimes() {
        this.mockInstanceBeerService = new AkiMockInstance<BeerService>() {
            @AkiMockUp
            public String getNoArgsString() {
                return null;
            }
        }.getMockInstance();
        Random rand = new Random();
        int i = rand.nextInt(10);
        for (int j = 0; j < i; j++) {
            mockInstanceBeerService.getNoArgsString();
        }
    }

}