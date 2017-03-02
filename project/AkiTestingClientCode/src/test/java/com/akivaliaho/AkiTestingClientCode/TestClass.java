package com.akivaliaho.AkiTestingClientCode;

import AkiTest.assertz.Assertion;
import annotations.AkiMock;
import annotations.Before;
import annotations.Test;
import org.slf4j.LoggerFactory;

public class TestClass {
    private org.slf4j.Logger LOG = LoggerFactory.getLogger(TestClass.class);
    @AkiMock
    BeerService beerService;

    @Before
    public void init() {
        LOG.debug("Test initialization begin");
    }
    @Test(expected = AssertionError.class)
    public void initializationTest() {
        LOG.info("Found the test method!");
        Assertion.assertTrue(false);
    }
}
