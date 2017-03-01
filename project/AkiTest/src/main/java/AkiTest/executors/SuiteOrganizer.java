package AkiTest.executors;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by vagrant on 3/1/17.
 */
public class SuiteOrganizer {
    private final Map<Class, List<Method>> suiteOrganizer;

    public SuiteOrganizer() {
        this.suiteOrganizer = new HashMap<>();
    }

    public Map<Class, List<Method>> organizeTestMethods(Set<Method> testMethods) {
        testMethods.parallelStream()
                .forEach(testMethod -> putToMap(testMethod));
        return suiteOrganizer;
    }

    private void putToMap(Method testMethod) {
        if (suiteOrganizer.containsKey(testMethod.getDeclaringClass())) {
            List<Method> methods = suiteOrganizer.get(testMethod.getDeclaringClass());
            methods.add(testMethod);
        } else {
            List<Method> methodList = new ArrayList<>();
            methodList.add(testMethod);
            suiteOrganizer.put(testMethod.getDeclaringClass(), methodList);
        }
    }
}
