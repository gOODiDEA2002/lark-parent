package lark.msg;

import java.lang.annotation.*;

/**
 * @author cuigh
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface MsgHandler {
    String topic() default "";
    String channel() default "";
    int threads() default 4;
    Class<? extends Subscriber> provider();
}
