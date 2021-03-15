package lark.db.jsd.converter;

/**
 * @author hongmiao.yu
 */
public interface Converter<T> {

    Object toDbValue(T javaValue);

    T toJavaValue(Object dbValue);

    class NoConversion<J> implements Converter<J> {

        @Override
        public Object toDbValue(J javaValue) {
            return javaValue;
        }

        @SuppressWarnings("unchecked")
        @Override
        public J toJavaValue(Object dbValue) {
            return (J)dbValue;
        }
    }
}
