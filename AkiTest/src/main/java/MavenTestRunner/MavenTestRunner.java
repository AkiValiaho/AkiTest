package MavenTestRunner;

import AkiTest.executors.*;
import AkiTest.mockHook.AkiMocker;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Mojo(name = "test", requiresDependencyResolution = ResolutionScope.TEST)
public class MavenTestRunner extends AbstractMojo {

    @Component
    private MavenProject project;
    @Parameter
    private String[] defaultPackage;
    @Parameter
    private String[] parallel;

    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            //Load all the test phase generated .class-files
            //with custom classloader
            Thread.currentThread().setContextClassLoader(getClassLoader());
            getLog().info("Executing Aki Tests");
            TestExecutor akiTestExecutor;
            if (parallel.length > 0 && parallel[0].equals("true")) {
                //Use the parallel test executor
                akiTestExecutor = new ParallelTestExecutor(new AkiTestExecutorAnnotationConfigurationStrategyHandler(new AkiMocker()),
                        new SuiteOrganizer());
            } else {
                akiTestExecutor = new NormalTestExecutor(new SuiteOrganizer(),
                        new AkiTestExecutorAnnotationConfigurationStrategyHandler(new AkiMocker()));
            }
            akiTestExecutor.scanClassPathForTests(Arrays.stream(defaultPackage).collect(Collectors.toList()));
            akiTestExecutor.execute();
        } catch (DependencyResolutionRequiredException e) {
            e.printStackTrace();
        }
    }

    public ClassLoader getClassLoader() throws DependencyResolutionRequiredException {
        List<String> classpathElements = project.getTestClasspathElements();
        List<URL> classPathURLs = classpathElements.stream()
                .map(element -> {
                    try {
                        return new File(element).toURI().toURL();
                    } catch (MalformedURLException e) {
                    }
                    return null;
                })
                .collect(Collectors.toList());
        return new URLClassLoader(classPathURLs.toArray(new URL[classPathURLs.size()]), getClass().getClassLoader());
    }
}
