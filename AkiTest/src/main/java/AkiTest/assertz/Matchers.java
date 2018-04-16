package AkiTest.assertz;


import AkiTest.assertz.jsonPath.JsonParser;

import static com.google.common.base.Preconditions.checkNotNull;

public class Matchers {
    public static String jsonPath(String json, String matcher) {
        checkNotNull(json);
        checkNotNull(matcher);
        return new JsonParser(json).findMatchingValue(matcher);
    }
}