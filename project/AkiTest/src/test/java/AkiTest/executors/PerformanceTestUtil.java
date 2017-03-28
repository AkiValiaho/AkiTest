package AkiTest.executors;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * Created by vagrant on 3/28/17.
 */
@Slf4j
public class PerformanceTestUtil {
    private static long l;

    public static void startTimer() {
        PerformanceTestUtil.l = System.nanoTime();
    }

    public static void stopAndLog() {
        log.debug("Code block execution took " + TimeUnit.MILLISECONDS.convert(System.nanoTime() - l, TimeUnit.NANOSECONDS) + " ms");
    }
}

