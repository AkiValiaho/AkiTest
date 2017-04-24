package AkiTest.mockHook;

import AkiTest.executors.testClasses.BeerService;
import annotations.AkiMockUp;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by vagrant on 4/24/17.
 */
public class MockMethodParserTest {
    private MockMethodParser mockmethodParser;

    @Before
    public void setUp() throws Exception {
        this.mockmethodParser = new MockMethodParser();
    }

    @Test
    public void parseMockMethodsAllWell() throws Exception {
        AkiMockInstance<BeerService> mockedInstance = new AkiMockInstance<BeerService>() {
            @AkiMockUp(hit = 3)
            public String noArgsString() {
                return "hello";
            }
        };
        List<MockMethod> mockMethods = this.mockmethodParser.parseMockMethods(mockedInstance);
        assertEquals(mockMethods.size(), 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseMockMethodsAFewNonMocked() throws Exception {
        AkiMockInstance<BeerService> mockedInstance = new AkiMockInstance<BeerService>() {
            @AkiMockUp(hit = 3)
            public String noArgsString() {
                return "hello";
            }

            public String getArgs() {
                return "emptyStuff";
            }
        };
        //This should fail
        this.mockmethodParser.parseMockMethods(mockedInstance);
    }
}