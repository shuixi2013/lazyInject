package com.trend.lazyinject.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by ganyao on 2017/12/5.
 */
@Target({FIELD,PARAMETER})
@Retention(RUNTIME)
public @interface Inject {

    Class<?> component() default None.class;
    boolean alwaysRefresh() default false;
    String[] args() default {};
    boolean nullProtect() default false;

    class None {}

}
