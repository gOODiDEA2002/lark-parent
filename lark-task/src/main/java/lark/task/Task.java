package lark.task;

import java.lang.annotation.*;

/**
 *
 * @author noname
 * @date 15/11/27
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Task {
    String name() default "";

}
