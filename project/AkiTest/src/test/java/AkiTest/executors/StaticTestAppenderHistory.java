package AkiTest.executors;

import org.apache.log4j.spi.LoggingEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by vagrant on 2/25/17.
 */
public class StaticTestAppenderHistory {
    private static List<TestAppender> staticAppenders = new ArrayList<>();

    public static void addAppenderInstance(TestAppender testAppender) {
        staticAppenders.add(testAppender);
    }

    public static Stream<LoggingEvent> staticLoggingEvents() {
        return staticAppenders.stream()
                .flatMap(testAppender -> testAppender.getAppendedMessages().stream());
    }
}
