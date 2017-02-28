package AkiTest.executors;

import java.util.List;

public interface TestExecutor {
	public void execute();

	public void scanClassPathForTests(List<String> packages);
}
