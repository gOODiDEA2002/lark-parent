package lark.core.lang;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * API的响应结果为原始内容
 */
@Target( ElementType.METHOD )
@Retention(RetentionPolicy.RUNTIME)
public @interface RawResult {
    String value() default "";
}
