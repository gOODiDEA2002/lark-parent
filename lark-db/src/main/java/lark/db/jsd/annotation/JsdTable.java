package lark.db.jsd.annotation;

import lark.db.jsd.NameStyle;

import java.lang.annotation.*;

/**
 * Created by noname on 15/11/19.
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface JsdTable {
    /**
     * 表字段命名风格
     * @return
     */
    NameStyle nameStyle() default NameStyle.PASCAL;

    /**
     * 表分片字段
     * @return
     */
    String[] shardKeys() default {};
}
