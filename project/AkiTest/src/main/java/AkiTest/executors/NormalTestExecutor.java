package AkiTest.executors;

import AkiTest.mockHook.Mocklibrary;

import java.util.List;


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

	@Override
	public void feedMocker(Mocklibrary mocklibrary) {
		akiTestExecutor.feedMocker(mocklibrary);
	}

}
