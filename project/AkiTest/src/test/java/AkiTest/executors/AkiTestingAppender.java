package AkiTest.executors;

import lombok.Getter;
import lombok.Setter;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.spi.LoggingEvent;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by vagrant on 2/25/17.
 */
public class AkiTestingAppender extends ConsoleAppender {
 @Getter
 @Setter
 private final ArrayList<LoggingEvent> appendedMessages;

    public AkiTestingAppender() {
        StaticTestAppenderHistory.addAppenderInstance(this);
        this.appendedMessages = new ArrayList<>();
    }
    @Override
    public void append(LoggingEvent event) {
        appendedMessages.add(event);
        super.append(event);
    }

    public boolean containsString(String contains) {
        //Get all the static appender instances
        Set<LoggingEvent> collect = StaticTestAppenderHistory.staticLoggingEvents()
                .filter(event -> {
                    if (event.getRenderedMessage().contains(contains)) {
                        return true;
                    }
                    return false;
                })
                .collect(Collectors.toSet());
        return collect.size() > 0 ? true : false;
    }
}
