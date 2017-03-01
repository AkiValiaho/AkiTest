package AkiTest.executors;

import AkiTest.executors.testClasses.SecondTestFile;
import AkiTest.executors.testClasses.RealisticTesting;
import mockit.Injectable;
import mockit.StrictExpectations;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by vagrant on 2/25/17.
 */
public class AkiTestExecutorTest {
    @Injectable
    SuiteOrganizer suiteOrganizer;
    @Injectable
    AnnotationStrategyHandler annotationStrategyHandler;
    private Logger logger = Logger.getLogger(AkiTestExecutorTest.class);
    private AkiTestExecutor akiTesting;
    private AkiTestingAppender testLogAppender;

    @Before
    public void setUp() throws Exception {
        //Dynamically readd a custom log appender to this test
        testLogAppender = new AkiTestingAppender();
        this.logger.addAppender(testLogAppender);
        this.akiTesting = new AkiTestExecutor<>(suiteOrganizer, annotationStrategyHandler);
    }

    private Map<Class,List<Method>> createDefaultOrderedTestExecutionMap() {
        Map<Class, List<Method>> orderedExecutionMap = new HashMap<>();

        //Second test file
        List<Method> secondTestFileWithTestMethodsLists = new ArrayList<>();
        Method[] declaredMethods = SecondTestFile.class.getDeclaredMethods();
        secondTestFileWithTestMethodsLists.addAll(Arrays.stream(declaredMethods)
                                                    .filter(method -> method.getAnnotation(com.akivaliaho.AkiTest.Test.class) != null)
                                                    .collect(Collectors.toList()));
        //Test file with real tests
        List<Method> testFileWithRealTestsList = new ArrayList<>();
        Method[] testFileWithRealTestsMethods = RealisticTesting.class.getDeclaredMethods();
        testFileWithRealTestsList.addAll(Arrays.stream(testFileWithRealTestsMethods)
                .filter(method -> method.getAnnotation(com.akivaliaho.AkiTest.Test.class) != null)
                .collect(Collectors.toList()));

        //Add picked-up methods to executionmap
        orderedExecutionMap.put(testFileWithRealTestsMethods[0].getDeclaringClass(), testFileWithRealTestsList);
        orderedExecutionMap.put(declaredMethods[0].getDeclaringClass(), secondTestFileWithTestMethodsLists);
        return orderedExecutionMap;
    }


    @Test
    public void execute() throws Exception {
        Map<Class, List<Method>> orderedExecutionMap = createDefaultOrderedTestExecutionMap();
        new StrictExpectations() {{
            suiteOrganizer.organizeTestMethods(withAny(null)); result = orderedExecutionMap; times = 1;
            annotationStrategyHandler.handleOncePerTestAnnotations(withAny(null)); maxTimes = 3;
        }};
        defaultClassPathScan();
        this.akiTesting.execute();
        //Assert that the test appender contains the test string
        assertTrue(this.testLogAppender.containsString("Something wild happened"));
    }

    @Test
    public void scanClassPathForTests() throws Exception {
        defaultClassPathScan();
    }

    private void defaultClassPathScan() {
        logger.debug("Testing classpath scanner");
        String[] arrayOfStrings = {"AkiTest.executors.testClasses"};
        this.akiTesting.scanClassPathForTests(Arrays.asList(arrayOfStrings));
        //Check that three tests were found
        assertEquals(this.akiTesting.getTestMethods().size(), 3);
    }

    @Test
    public void scanClassPathFromRootPackage() throws Exception {
        logger.debug("Testing classpath scanner from root package");
        String[] arrayOfStrings = {"AkiTest"};
        this.akiTesting.scanClassPathForTests(Arrays.asList(arrayOfStrings));
        //Still should find 3 methods
        assertEquals(this.akiTesting.getTestMethods().size(), 3);
    }

    @Test
    public void scanClassPathRecursivelyFromUpperPackage() throws Exception {
        logger.debug("Testing classpath from upper package");
        String[] arrayOfStrings = {"AkiTest.executors"};
        this.akiTesting.scanClassPathForTests(Arrays.asList(arrayOfStrings));
        //Still should find 3 methods
        assertEquals(this.akiTesting.getTestMethods().size(), 3);
    }

    @Test
    public void scanClassPathWithTwoDifferentPackages() throws Exception {
        logger.debug("Testing classpath scanner with root package and an another package");
        String[] arrayOfStrings = {"AkiTest", "AkiTestSecond"};
        this.akiTesting.scanClassPathForTests(Arrays.asList(arrayOfStrings));
        //Should find 3 methods
        assertEquals(this.akiTesting.getTestMethods().size(), 4);
    }

}