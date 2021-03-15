package lark.db.jsd.annotation;

import lark.db.jsd.converter.Converter;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author hongmiao.yu
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface JsdConverter {
    Class<? extends Converter> value() default Converter.NoConversion.class;
}
