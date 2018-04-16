package com.akivaliaho.AkiTestingClientCode;

import AkiTest.mockHook.AkiMockInstance;
import annotations.AkiMock;
import annotations.AkiMockUp;
import annotations.Before;
import annotations.Test;
import org.slf4j.LoggerFactory;

import static AkiTest.assertz.Matchers.jsonPath;
import static junit.framework.Assert.assertEquals;

public class JsonPathClientTest {
    @AkiMock
    BeerService beerService;
    private org.slf4j.Logger LOG = LoggerFactory.getLogger(TestClass.class);

    @Before
    public void init() {
        LOG.debug("Test initialization begin");
    }

    @Test
    public void jsonPathParserTest() {
        BeerService mockInstance = new AkiMockInstance<BeerService>() {
            @AkiMockUp(hit = 1)
            public String getNoArgsString() {
                return "{\"aki\": \"hello\", \"testing\" : \"testbed\"}";
            }
        }.getMockInstance();
        String noArgsString = mockInstance.getNoArgsString();
        String testing = jsonPath(noArgsString, "testing");
        LOG.info("JsonPath found string: {}", testing);
        assertEquals(testing, "testbed");
    }

}
