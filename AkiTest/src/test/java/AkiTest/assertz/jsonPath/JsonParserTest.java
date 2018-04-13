package AkiTest.assertz.jsonPath;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JsonParserTest {


    private JsonParser jsonParser;

    @Test(expected = IllegalArgumentException.class)
    public void findMatchingValue_emptyJson_shouldThrowException() {
        this.jsonParser = new JsonParser("");
        this.jsonParser.findMatchingValue("");
    }

    @Test
    public void findMatchingValue_validJson_shouldReturnValue() {
        this.jsonParser = new JsonParser("{\"aki\": \"hello\"}");
        String aki = this.jsonParser.findMatchingValue("aki");
        assertEquals(aki, "hello");
    }

    @Test(expected = IllegalArgumentException.class)
    public void findMatchingValue_missingStartingBracket_shouldThrowException() {
        this.jsonParser = new JsonParser("\"aki\": \"hello\"}");
        this.jsonParser.findMatchingValue("aki");
    }
    @Test(expected = IllegalArgumentException.class)
    public void findMatchingValue_missingClosingBracket_shouldThrowExecption() {
        this.jsonParser = new JsonParser("{\"aki\": \"hello\"");
        this.jsonParser.findMatchingValue("aki");
    }
    @Test
    public void findMatchingValue_validMultiJson_shouldReturnCorrectValue() {
        this.jsonParser = new JsonParser("{\"aki\": \"hello\", \"asdf\": \"asdf\"}");
        String asdf = this.jsonParser.findMatchingValue("asdf");
        assertEquals(asdf, "asdf");
    }


}