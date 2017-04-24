package AkiTest.executors;

import AkiTest.mockHook.MockLibraryHook;
import annotations.Test;
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
import java.util.*;
import java.util.stream.Collectors;

import static AkiTest.preconditions.Preconditions.checkCollectionNotNullOrEmpty;

public class AkiTestExecutor<T> implements TestExecutor {
    private final SuiteOrganizer suiteOrganizer;
    private final AnnotationStrategyHandler annotationStrategyHandler;
    private final InvocationAssertionHolder invocationNumberHolder;
    private org.slf4j.Logger LOG = LoggerFactory.getLogger(AkiTestExecutor.class);
    @Getter
    @Setter
    private Set<Method> testMethods = new HashSet<>();
    private MockLibraryHook mockLibrary;

    public AkiTestExecutor(SuiteOrganizer suiteOrganizer, AnnotationStrategyHandler annotationStrategyHandler) {
        this.suiteOrganizer = suiteOrganizer;
        this.annotationStrategyHandler = annotationStrategyHandler;
        this.invocationNumberHolder = InvocationAssertionHolder.getInstance();
    }

    public void execute() {
        //Number of tests to execute logged here
        Map<Class, List<Method>> testMethodsPerClass = organizeByClass();
        testMethodsPerClass.values()
                .forEach(methodList -> {
                    try {
                        executeTestsInList(methodList);
                    } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                });
    }

    public void executeInParallel() {
        Map<Class, List<Method>> testMethodsPerClass = organizeByClass();
        //Spawn a runner per test method class
        testMethodsPerClass.values()
                .parallelStream()
                .forEach(methodList -> {
                    try {
                        executeTestsInList(methodList, true);
                    } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                });
    }

    private Map<Class, List<Method>> organizeByClass() {
        //Number of tests to execute logged here
        LOG.info("Executing {} tests", testMethods.size());
        checkCollectionNotNullOrEmpty(testMethods);
        return suiteOrganizer.organizeTestMethods(testMethods);
    }

    private void executeTestsInList(List<Method> methodList) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        Object declaredClassInstance = this.annotationStrategyHandler.handleOncePerTestClassAnnotations(methodList.get(0).getDeclaringClass());
        //TODO Provide flag to allow parallelization
        methodList.stream()
                .forEach(method -> {
                    runTest(declaredClassInstance, method);
                });
    }

    private void executeTestsInList(List<Method> methodList, Boolean parallel) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        //TODO USe somewhere
        Object declaredClassInstance = this.annotationStrategyHandler.handleOncePerTestClassAnnotations(methodList.get(0).getDeclaringClass());
        methodList
                .parallelStream()
                .forEach(method -> runTest(declaredClassInstance, method));
    }

    private void runTest(Object declaredClassInstance, Method method) {
        try {
            //Invoker in try/catch-block to catch AssertExceptions
            this.annotationStrategyHandler.handleOncePerTestAnnotations(declaredClassInstance.getClass(),
                    declaredClassInstance);
            method.invoke(declaredClassInstance, new Object[0]);
            //Assert hits
            invocationNumberHolder.assertInvocationHitsMatch(method);
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
                    invocationNumberHolder.assertInvocationHitsMatch(method);
                } else {
                    LOG.debug(e.getMessage());
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
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

    @Override
    public void feedMocker(MockLibraryHook mockLibraryHook) {
        this.mockLibrary = mockLibraryHook;
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
