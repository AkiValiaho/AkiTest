package AkiTest.assertz.jsonPath;


import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;

public class JsonParser {
    private final String json;

    public JsonParser(String json) {
        this.json = json;
    }

    public String findMatchingValue(String matcher) {
        checkArgument(!matcher.isEmpty());
        JsonCharFeed jsonCharFeed = new JsonCharFeed();
        for (int i = 0; i < json.length(); i++) {
            jsonCharFeed.feedCharacter(json.charAt(i));
        }
        return jsonCharFeed.getMatchingComponent(matcher);
    }

    private class JsonCharFeed {

        private Character startingChar = null;
        private Character endingChar = null;

        private Map<JsonKey, JsonValue> hashMap = new HashMap<>();

        private StringBuilder currentKeyBuilder = new StringBuilder();
        private StringBuilder currentValueBuilder = new StringBuilder();
        private JsonKey currentKey;
        private boolean keyFollowingWhiteSpace;
        private boolean keySet;
        private boolean valueBuilt;
        private JsonValue currentValue;

        void feedCharacter(char c) {
            if (startingChar == null) {
                //Check starting character is a bracket
                checkArgument(c == '{', "Missing left bracket '{', not valid JSON");
                startingChar = c;
                return;
            }
            //Starting char already set
            processCharacter(c);
        }

        private void processCharacter(char c) {
            //TODO Refactor to context engine after some time
            if (c == ':') {
                //key found, set it as the currentKey
                this.currentKey = new JsonKey(currentKeyBuilder.toString());
                currentKeyBuilder = new StringBuilder();
                this.keyFollowingWhiteSpace = true;
                return;
            }
            if (checkSkips(c)) return;
            if (c == ' ' && keyFollowingWhiteSpace) {
                keySet = true;
                return;
            }

            keyFollowingWhiteSpace = false;
            if (checkIfAppendToValue(c)) {
                return;
            }
            currentKeyBuilder.append(c);
        }

        private boolean checkIfAppendToValue(char c) {
            if (keySet && !valueBuilt) {
                currentValueBuilder.append(c);
                return true;
            }
            return false;
        }

        private boolean checkSkips(char c) {
            //skip on key following whitespace or "
            if (c == '"') {
                if (currentValueBuilder.length() != 0) {
                    this.currentValue = new JsonValue(currentValueBuilder.toString());
                    this.hashMap.put(currentKey, currentValue);
                    currentKey = null;
                    currentValue = null;
                    valueBuilt = true;
                }
                return true;
            }
            //TODO if value set next char should be , or {
            return false;
        }

        String getMatchingComponent(String matcher) {
            checkArgument(endingChar != null, "Missing closing right bracket '}', not valid JSON");
            return "";
        }
    }
}