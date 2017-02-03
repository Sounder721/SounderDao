package com.sounder.sounder.annotation;
/**
 * Created by Sounder on 2017/1/21.
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据表字段约束注解
 * Createed by Sounder on 2017/1/21
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Constraint {
    /**
     * 字段名称
     */
    String name();

    /**
     * 字段类型，默认为TEXT
     */
    FieldType type() default FieldType.TEXT;

    /**
     * 是否为主键
     */
    boolean primaryKey() default false;

    /**
     * 是否自动增长，为了方便，当自增时就默认该字段为唯一的一个主键
     */
    boolean autoInCrement() default false;
}
