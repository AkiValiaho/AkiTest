package AkiTest.executors;

import AkiTest.assertz.Assertion;
import AkiTest.executors.testClasses.BeerService;
import AkiTest.mockHook.AkiMockInstance;
import annotations.AkiMockUp;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.apache.maven.plugin.testing.MojoRule;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;

/**
 * Still not working, ignored
 * Created by vagrant on 3/26/17.
 */
@Ignore
public class InstanceHitTest {
    private static AbstractMojoTestCase abstractMojoTestCase;
    private static File testPom;
    @Rule
    public MojoRule rule = new MojoRule(abstractMojoTestCase) {
        @Override
        protected void before() throws Throwable {
            //Block abstract test case from running it's setup method
        }

        @Override
        protected void after() {
            //Block the abstract test case from running it's after method
        }
    };

    @org.junit.BeforeClass
    public static void initMojoTestCase() {
        abstractMojoTestCase = new AbstractMojoTestCase() {
            @Override
            protected void setUp() throws Exception {
                //Do nothing to avoid exceptions with RepositorySystem initialization
            }
        };
        testPom = new File(abstractMojoTestCase.getBasedir() + "/pom.xml");
    }

    @Test(expected = AssertionError.class)
    public void mockTestShouldFail() throws Exception {
        //Build the mojo first
        AkiTestExecutor test = (AkiTestExecutor) rule.lookupMojo("test", testPom);
        BeerService mockInstance = new AkiMockInstance<BeerService>() {
            @AkiMockUp(hit = 2)
            public String getNoArgsString() {
                return "Hello world";
            }
        }.getMockInstance();
        String noArgsString = mockInstance.getNoArgsString();
        String sometingCool = mockInstance.getNoArgsString();
        Assertion.assertTrue(noArgsString.equals("Hello world"));
        Assertion.assertTrue(false);
    }
}
