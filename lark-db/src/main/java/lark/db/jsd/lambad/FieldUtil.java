package lark.db.jsd.lambad;

import lark.db.jsd.NameStyle;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.Locale;

public class FieldUtil {

    public static <T> String getFieldName(FieldFunction<T, ?> lambda) {
        try {
            Method method = lambda.getClass().getDeclaredMethod("writeReplace");
            method.setAccessible(true);

            SerializedLambda serializedLambda = (SerializedLambda) method.invoke(lambda);
            String implMethodName = serializedLambda.getImplMethodName();
            return methodToProperty(implMethodName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> String getColumnName(FieldFunction<T, ?> lambda) {
        return NameStyle.transform(getFieldName(lambda), NameStyle.LOWER);
    }

    private static String methodToProperty(String name) {
        if (name.startsWith("is")) {
            name = name.substring(2);
        } else {
            if (!name.startsWith("get") && !name.startsWith("set")) {
                throw new RuntimeException("Error parsing property name '" + name + "'.  Didn't start with 'is', 'get' or 'set'.");
            }

            name = name.substring(3);
        }

        if (name.length() == 1 || name.length() > 1 && !Character.isUpperCase(name.charAt(1))) {
            name = name.substring(0, 1).toLowerCase(Locale.ENGLISH) + name.substring(1);
        }

        return name;
    }
}