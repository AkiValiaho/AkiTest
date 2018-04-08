package AkiTest.executors;

import AkiTest.executors.testClasses.BeerService;
import AkiTest.executors.testClasses.RealisticTesting;
import AkiTest.mockHook.MockLibraryHook;
import mockit.Mock;
import mockit.MockUp;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.List;

import static AkiTest.assertz.Assertion.assertTrue;

/**
 * Created by vagrant on 3/2/17.
 */
public class AkiTestExecutorAnnotationStrategyConfigurationHandlerTest {

    private AkiTestingAppender testLogAppender;
    private Logger logger = Logger.getLogger(AkiTestExecutorAnnotationStrategyConfigurationHandlerTest.class);
    private AkiTestExecutorAnnotationConfigurationStrategyHandler akiTestExecutorAnnotationConfigurationStrategyHandler;

    @Before
    public void setUp() throws Exception {
        //Dynamically readd a custom log appender to this test
        testLogAppender = new AkiTestingAppender();
        this.logger.addAppender(testLogAppender);
        MockLibraryHook mockLibraryHook1 = new MockUp<MockLibraryHook>() {
            @Mock
            public void mock(List<Field> annotatedFields, Object instantiatedClass) {
                ((RealisticTesting) instantiatedClass).setBeerService(new BeerService("hello world"));
            }
        }.getMockInstance();
        this.akiTestExecutorAnnotationConfigurationStrategyHandler = new AkiTestExecutorAnnotationConfigurationStrategyHandler(mockLibraryHook1);
    }

    @Test
    public void handleOncePerTestClassAnnotations() throws Exception {
        RealisticTesting testing = (RealisticTesting) akiTestExecutorAnnotationConfigurationStrategyHandler
                .handleOncePerTestClassAnnotations(RealisticTesting.class);
        assertTrue(testLogAppender.containsString("Handling @BeforeClass"));
        assertTrue(testing.getBeerService() != null);
    }

    @Test
    public void handleOncePerTestAnnotations() throws Exception {

    }

}