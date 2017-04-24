package AkiTest.mockHook;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by vagrant on 3/5/17.
 */
public class MockMethodParser {

    public <T> List<MockMethod> parseMockMethods(AkiMockInstance<T> tAkiMockInstance) {
        Method[] declaredMethods = tAkiMockInstance.getClass().getDeclaredMethods();
        //TODO Check every method has @Mock annotation
        return Arrays.stream(declaredMethods)
                .map(method -> {
                    Annotation[] annotations = method.getAnnotations();
                    List<Annotation> collect = Arrays.stream(annotations)
                            .filter(annotation -> isMockUpAnnotation(annotation))
                            .collect(Collectors.toList());
                    if (collect.size() != 1) {
                        throw new IllegalArgumentException("Please annotate your mock method with ONE " +
                                "@AkiMockUp-annotation");
                    }
                    return new MockMethod(method);
                })
                .collect(Collectors.toList());
    }

    private Boolean isMockUpAnnotation(Annotation annotation) {
        Class<? extends Annotation> aClass = annotation.annotationType();
        return aClass.getName().equals("annotations.AkiMockUp");
    }
}
