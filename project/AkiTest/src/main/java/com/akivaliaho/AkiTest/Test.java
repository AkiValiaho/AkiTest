package com.akivaliaho.AkiTest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Test {

    Class<? extends Throwable> expected() default DEFAULT.class;

    static final class DEFAULT extends Throwable {
    }

    ;
}
