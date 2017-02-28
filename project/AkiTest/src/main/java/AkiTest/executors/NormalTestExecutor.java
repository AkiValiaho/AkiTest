package AkiTest.executors;

import java.util.List;

import AkiTest.executors.TestExecutor;;

public class NormalTestExecutor implements TestExecutor {
	private AkiTestExecutor akiTestExecutor;

	public NormalTestExecutor() {
		this.akiTestExecutor = new AkiTestExecutor<>();
	}

	@Override
	public void execute() {
		akiTestExecutor.execute();
	}

	@Override
	public void scanClassPathForTests(List<String> packages) {
		akiTestExecutor.scanClassPathForTests(packages);
	}

}
