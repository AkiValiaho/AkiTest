package AkiTest.mockHook;

import lombok.extern.slf4j.Slf4j;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;


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
                        //Mocks the field given instantiatedClass
                        //in a Java 8 stream-loop
                        mock(field, instantiatedClass);
                    } catch (NoSuchFieldException | IllegalAccessException | InstantiationException e) {
                        e.printStackTrace();
                    }
                });
    }

    private void mock(Field field, Object instantiatedClass) throws NoSuchFieldException, IllegalAccessException, InstantiationException {
        Function<Constructor<?>[], Boolean> hasNoArgsConstructor = constructors -> {
            Optional<Constructor<?>> first = Arrays.stream(constructors)
                    .parallel()
                    .filter(c -> c.getParameterCount() == 0)
                    .findFirst();
            if (first.isPresent()) {
                return true;
            }
            return false;
        };
        if (!hasNoArgsConstructor.apply(field.getType().getConstructors())) {
            Consumer<Field> setField = interField -> {
                Class<?> dependencyClassType = interField.getType();
                Objenesis o = new ObjenesisStd();
                Object nonNoArgsConstructorInvocation = o.newInstance(dependencyClassType);
                try {
                    setField(interField, instantiatedClass, nonNoArgsConstructorInvocation);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            };
            setField.accept(field);
        } else {
            Object noArgsConstructorInvocation = field.getType().newInstance();
            setField(field, instantiatedClass, noArgsConstructorInvocation);
        }
    }

    private void setField(Field field, Object instantiatedClass, Object o1) throws NoSuchFieldException, IllegalAccessException {
        //Set field
        Field field1 = instantiatedClass.getClass().getDeclaredField(field.getName());
        field1.setAccessible(true);
        field1.set(instantiatedClass, o1);
    }
}
