package AkiTest.executors;

import AkiTest.mockHook.MockLibraryHook;

import java.util.List;

/**
 * Created by vagrant on 4/24/17.
 */
public class ParallelTestExecutor implements TestExecutor {

    private final AkiTestExecutor akiTestExecutor;

    public ParallelTestExecutor(AnnotationStrategyHandler annotationStrategyHandler, SuiteOrganizer suiteOrganizer) {
        this.akiTestExecutor = new AkiTestExecutor<>(suiteOrganizer, annotationStrategyHandler);
    }

    @Override
    public void execute() {
        this.akiTestExecutor.executeInParallel();
    }

    @Override
    public void scanClassPathForTests(List<String> packages) {
        this.akiTestExecutor.scanClassPathForTests(packages);
    }

    @Override
    public void feedMocker(MockLibraryHook mockLibraryHook) {
        akiTestExecutor.feedMocker(mockLibraryHook);
    }
}
