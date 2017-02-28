package AkiTest.executors;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by vagrant on 2/25/17.
 */
public class AkiTestExecutorTest {
    private Logger logger = Logger.getLogger(AkiTestExecutorTest.class);
    private AkiTestExecutor akiTesting;
    private TestAppender newAppender;

    @Before
    public void setUp() throws Exception {
        //Dynamically readd a custom log appender to this test
        newAppender = new TestAppender();
        this.logger.addAppender(newAppender);
        this.akiTesting = new AkiTestExecutor<>();
    }

    @Test
    public void executeTestAssertError() throws Exception {
        defaultClassPathScan();
        this.akiTesting.execute();
    }


    @Test
    public void execute() throws Exception {
        defaultClassPathScan();
        this.akiTesting.execute();
        //Assert that the test appender contains the test string
        assertTrue(this.newAppender.containsString("Something wild happened"));
    }

    @Test
    public void scanClassPathForTests() throws Exception {
        defaultClassPathScan();
    }

    private void defaultClassPathScan() {
        logger.debug("Testing classpath scanner");
        String[] arrayOfStrings = {"AkiTest.executors.testClasses"};
        this.akiTesting.scanClassPathForTests(Arrays.asList(arrayOfStrings));
        //Check that two tests were found
        assertEquals(this.akiTesting.getTestMethods().size(), 2);
    }

    @Test
    public void scanClassPathFromRootPackage() throws Exception {
        logger.debug("Testing classpath scanner from root package");
        String[] arrayOfStrings = {"AkiTest"};
        this.akiTesting.scanClassPathForTests(Arrays.asList(arrayOfStrings));
        //Still should find 2 methods
        assertEquals(this.akiTesting.getTestMethods().size(), 2);
    }

    @Test
    public void scanClassPathRecursivelyFromUpperPackage() throws Exception {
        logger.debug("Testing classpath from upper package");
        String[] arrayOfStrings = {"AkiTest.executors"};
        this.akiTesting.scanClassPathForTests(Arrays.asList(arrayOfStrings));
        //Still should find 2 methods
        assertEquals(this.akiTesting.getTestMethods().size(), 2); }

    @Test
    public void scanClassPathWithTwoDifferentPackages() throws Exception {
        logger.debug("Testing classpath scanner with root package and an another package");
        String[] arrayOfStrings = {"AkiTest", "AkiTestSecond"};
        this.akiTesting.scanClassPathForTests(Arrays.asList(arrayOfStrings));
        //Should find 3 methods
        assertEquals(this.akiTesting.getTestMethods().size(), 3);
    }

}