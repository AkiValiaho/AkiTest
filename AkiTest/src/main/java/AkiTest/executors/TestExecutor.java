package AkiTest.executors;

import AkiTest.mockHook.MockLibraryHook;

import java.util.List;

public interface TestExecutor {
	public void execute();

	public void scanClassPathForTests(List<String> packages);

	public void feedMocker(MockLibraryHook mockLibraryHook);
}
