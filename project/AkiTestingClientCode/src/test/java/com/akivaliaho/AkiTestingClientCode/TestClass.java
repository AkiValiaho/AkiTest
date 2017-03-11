package com.akivaliaho.AkiTestingClientCode;

import AkiTest.assertz.Assertion;
import AkiTest.mockHook.AkiMockInstance;
import annotations.AkiMock;
import annotations.AkiMockUp;
import annotations.Before;
import annotations.Test;
import org.slf4j.LoggerFactory;

public class TestClass {
    @AkiMock
    BeerService beerService;
    private org.slf4j.Logger LOG = LoggerFactory.getLogger(TestClass.class);

    @Before
    public void init() {
        LOG.debug("Test initialization begin");
    }

    @Test(expected = AssertionError.class)
    public void initializationTest() {
        BeerService mockInstance = new AkiMockInstance<BeerService>() {
            @AkiMockUp(hit = 1)
            public String getNoArgsString() {
                return "Hello world";
            }
        }.getMockInstance();
        String noArgsString = mockInstance.getNoArgsString();
        Assertion.assertTrue(noArgsString.equals("Hello world"));
        LOG.info("Debug string: {}", noArgsString);
        LOG.info("Found the test method!");
        Assertion.assertTrue(beerService != null);
        Assertion.assertTrue(false);
    }
}
