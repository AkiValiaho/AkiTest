package AkiTest.mockHook;

import lombok.extern.slf4j.Slf4j;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by vagrant on 3/2/17.
 */
@Slf4j
public class AkiMocker implements MockLibraryHook {
    private final Reflections reflections;

    public AkiMocker() {
        this.reflections = new Reflections();
    }

    @Override
    public void mock(List<Field> annotatedFields, Object instantiatedClass) {
        annotatedFields.stream()
                .forEach(field -> {
                    try {
                        mock(field, instantiatedClass);
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                });
    }

    private void mock(Field field, Object instantiatedClass) throws NoSuchFieldException, IllegalAccessException {
        //TODO Test
        if (!hasNoArgsConstructor(field.getType().getConstructors())) {
            Class<?> dependencyClassType = field.getType();
            Objenesis o = new ObjenesisStd();
            Object o1 = o.newInstance(dependencyClassType);
            //Set field
            Field field1 = instantiatedClass.getClass().getDeclaredField(field.getName());
            field1.setAccessible(true);
            field1.set(instantiatedClass, o1);
        }
        //TODO Add NoArgsConstructor version
    }

    private boolean hasNoArgsConstructor(Constructor<?>[] constructors) {
        for (int i = 0; i < constructors.length; i++) {
            if (constructors[i].getParameterCount() == 0) {
                return true;
            }
        }
        return false;
    }
}
