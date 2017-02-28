package AkiTest.executors;

import com.akivaliaho.AkiTest.Test;
import lombok.Getter;
import lombok.Setter;
import org.reflections.Configuration;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static AkiTest.preconditions.Preconditions.checkCollectionNotNullOrEmpty;

public class AkiTestExecutor<T> implements TestExecutor {
    private org.slf4j.Logger LOG = LoggerFactory.getLogger(AkiTestExecutor.class);
    @Getter
    @Setter
    private Set<Method> testMethods = new HashSet<>();

    public void execute() {
        //Number of tests to execute logged here
        LOG.info("Executing {} tests", testMethods.size());
        checkCollectionNotNullOrEmpty(testMethods);
        testMethods.stream()
                .forEach(method -> {
                    try {
                        Class<?> declaringClass = method.getDeclaringClass();
                        //Invoker in try/catch-block to catch AssertExceptions
                        method.invoke(declaringClass.newInstance(), new Object[0]);
                    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                        if (e instanceof InvocationTargetException) {
                            //Check if exception is allowed to happen
                            Class<? extends Throwable> allowedException = getAllowedException(method);
                            if (((InvocationTargetException) e)
                                    .getTargetException()
                                    .getClass()
                                    .getCanonicalName()
                                    .equals(allowedException.getCanonicalName())) {
                                LOG.debug("Got exception: {} ", e);
                            } else {
                                LOG.debug(e.getMessage());
                                e.printStackTrace();
                                throw new RuntimeException(e);
                            }
                        }
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    }
                });
    }

    private Class<? extends Throwable> getAllowedException(Method method) {
        Test annotation = method.getAnnotation(Test.class);
        Class<? extends Throwable> expected = annotation.expected();
        return expected;
    }

    /**
     * @param packages
     */
    @Override
    public void scanClassPathForTests(List<String> packages) {
        LOG.debug("Found packages {}", packages);
        Configuration configuration = new ConfigurationBuilder().addUrls(
                packages.stream().flatMap(string -> getURLsForPackage(string).stream()).collect(Collectors.toList()))
                .setScanners(new SubTypesScanner(), new TypeAnnotationsScanner(), new MethodAnnotationsScanner(),
                        new FieldAnnotationsScanner());
        Reflections reflections = new Reflections(configuration);
        //Scan classes with tests
        this.testMethods = reflections.getMethodsAnnotatedWith(Test.class);
        LOG.debug("Found {} methods annotated with @Test", testMethods.size());
        testMethods.stream()
                .forEach(clazz -> testMethods.add(clazz));
    }

    private Collection<URL> getURLsForPackage(String string) {
        Collection<URL> forPackage = ClasspathHelper.forPackage(string, null);
        //Add the package ending to the url
        return forPackage.stream()
                .peek(msg -> LOG.info("Found URL: {}", msg.getPath()))
                .map(msg -> {
                    try {
                        return appendPackageName(string, msg);
                    } catch (MalformedURLException e) {
                        LOG.error("Couldn't create an URL", e);
                    }
                    return null;
                })
                .collect(Collectors.toList());
    }

    private URL appendPackageName(String string, URL msg) throws MalformedURLException {
        String changedPackageName = string.replaceAll("\\.", "/");
        String urlAsString = msg.toString();
        return new URL(urlAsString + changedPackageName);
    }

}
