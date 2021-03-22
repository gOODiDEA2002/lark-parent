package lark.db.jsd.util;

import java.io.Serializable;
import java.util.function.Function;

@FunctionalInterface
public interface FieldFunction<T, F> extends Function<T, F>, Serializable {
}