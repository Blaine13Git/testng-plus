package com.qa.testng_plus.data_provider;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DataModel {
    public static final int File = 1;
    public static final int Method = 2;

    int value();
}
