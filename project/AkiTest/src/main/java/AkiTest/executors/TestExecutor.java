package AkiTest.executors;

import AkiTest.mockHook.Mocklibrary;

import java.util.List;

public interface TestExecutor {
	public void execute();

	public void scanClassPathForTests(List<String> packages);

	public void feedMocker(Mocklibrary mocklibrary);
}
