package AkiTest.assertz.jsonPath;


import static com.google.common.base.Preconditions.checkArgument;

public class JsonParser {
    private final String json;

    public JsonParser(String json) {
        this.json = json;
    }

    public String findMatchingValue(String matcher) {
        checkArgument(!matcher.isEmpty());
        JsonCharFeed jsonCharFeed = new JsonCharFeed();
        for (int i = 0; i < matcher.length(); i++) {
            jsonCharFeed.feedCharacter(matcher.charAt(i));
        }
        return jsonCharFeed.getMatchingComponent(matcher);
    }

    private class JsonCharFeed {

        private Character startingChar = null;
        private Character endingChar = null;

        void feedCharacter(char c) {
            if (startingChar == null) {
                //Check starting character is a bracket
                checkArgument(c == '{', "Missing left bracket '{', not valid JSON");
            }

        }

        String getMatchingComponent(String matcher) {
            checkArgument(endingChar != null, "Missing closing right bracket '}', not valid JSON");
            return "";
        }
    }
}
