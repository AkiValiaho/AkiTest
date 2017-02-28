package AkiTest.assertz;

/**
 * Created by vagrant on 2/27/17.
 */
public class Assertion {
    public static void assertTrue(boolean assertValue) throws AssertionError {
        if (!assertValue) {
            throw new AssertionError(generateMessage());
        }
    }

    private static String generateMessage() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StackTraceElement callingMethod = stackTrace[3];
        StringBuilder logBuilder = new StringBuilder();
        return logBuilder.append("Assertion error happened at method: ")
                .append(callingMethod.getMethodName()).toString();
    }
}
