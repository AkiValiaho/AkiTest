package AkiTest.executors;

import org.apache.log4j.spi.LoggingEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by vagrant on 2/25/17.
 */
public class StaticTestAppenderHistory {
    private static List<AkiTestingAppender> staticAppenders = new ArrayList<>();

    public static void addAppenderInstance(AkiTestingAppender akiTestingAppender) {
        staticAppenders.add(akiTestingAppender);
    }

    public static Stream<LoggingEvent> staticLoggingEvents() {
        return staticAppenders.stream()
                .flatMap(akiTestingAppender -> akiTestingAppender.getAppendedMessages().stream());
    }
}
