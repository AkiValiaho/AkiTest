package AkiTest.executors;

import AkiTest.assertz.Assertion;
import AkiTest.executors.testClasses.BeerService;
import AkiTest.mockHook.AkiMockInstance;
import annotations.AkiMockUp;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.apache.maven.plugin.testing.MojoRule;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;

/**
 * Created by vagrant on 3/26/17.
 */
public class InstanceHitTest {
    private static AbstractMojoTestCase abstractMojoTestCase;
    @Rule
    public MojoRule rule = new MojoRule(abstractMojoTestCase) {
        @Override
        protected void before() throws Throwable {
            super.before();
        }

        @Override
        protected void after() {
            super.after();
        }
    };

    @org.junit.BeforeClass
    public static void initMojoTestCase() {
        abstractMojoTestCase = new AbstractMojoTestCase() {
            @Override
            protected void setUp() throws Exception {
                //Do nothing
            }
        };
    }

    @Test(expected = AssertionError.class)
    public void mockTestShouldFail() throws Exception {
        //Build the mojo first
        File testPom = new File(abstractMojoTestCase.getBasedir() + "/pom.xml");
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
