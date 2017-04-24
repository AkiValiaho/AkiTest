package AkiTest.executors;

import AkiTest.executors.testClasses.RealisticTesting;
import AkiTest.executors.testClasses.SecondTestFile;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.*;

import static java.util.Arrays.stream;

/**
 * Created by vagrant on 3/1/17.
 */
public class SuiteOrganizerTest {
    private SuiteOrganizer suiteOrganizer;
    private Set<Method> listOfMethods;

    @Before
    public void setUp() throws Exception {
        this.suiteOrganizer = new SuiteOrganizer();
        this.listOfMethods = new HashSet<>();
    }

    @Test
    public void organizeTestMethods() throws Exception {
        Method[] methods = SecondTestFile.class.getDeclaredMethods();
        Method[] methods1 = RealisticTesting.class.getDeclaredMethods();
        Map<Class, List<Method>> classListMap = suiteOrganizer.organizeTestMethods(concatenateToSet(methods, methods1));
        //Check that the map contains all tests
        Assert.assertTrue(classListMap.containsKey(SecondTestFile.class));
        Assert.assertTrue(classListMap.containsKey(RealisticTesting.class));
        //Check that the tests are in proper order
        List<Method> secondTestFileMethods = classListMap.get(SecondTestFile.class);
        for (int i = 0; i < methods.length; i++) {
            Assert.assertTrue(secondTestFileMethods.contains(methods[i]));
        }
        List<Method> testFileWithRealTestsMethods = classListMap.get(RealisticTesting.class);
        Arrays.stream(methods1)
                .forEach(m -> Assert.assertTrue(testFileWithRealTestsMethods.contains(m)));
    }

    public Set<Method> concatenateToSet(Method[] firstArray, Method[] secondArray) {
        Method[] concatenatedArray = (Method[]) Array.newInstance(firstArray.getClass().getComponentType(), firstArray.length + secondArray.length);
        System.arraycopy(firstArray, 0, concatenatedArray, 0, firstArray.length);
        System.arraycopy(secondArray, 0, concatenatedArray, firstArray.length, secondArray.length);
        return setFromArray(concatenatedArray);
    }

    private Set<Method> setFromArray(Method[] concatenatedArray) {
        Set<Method> methods = new HashSet<>();
        stream(concatenatedArray)
                .forEach(ca -> methods.add(ca));
        return methods;
    }
}