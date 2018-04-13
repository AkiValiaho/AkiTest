package AkiTest.assertz;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MatchersTest {

    @Test(expected = NullPointerException.class)
    public void jsonPath_emptyJson_shouldThrowException() {
        Matchers.jsonPath("", null);
    }

    @Test(expected = NullPointerException.class)
    public void jsonPath_jsonMatcherNull_shouldThrowException() {
        Matchers.jsonPath("asdf", null);
    }

    @Test
    public void jsonPath_simpleJson_shouldReturnValue() {
        String hello = Matchers.jsonPath("{\"aki\": \"hello\"}", "aki");
        assertEquals(hello, "hello");
    }

    @Test
    public void jsonPath_doubleJson_shouldReturnProperValue() {
        String another = Matchers.jsonPath("{\"aki\": \"hello\",\"another\": \"another\" }", "another");
        assertEquals(another, "another");
    }
}