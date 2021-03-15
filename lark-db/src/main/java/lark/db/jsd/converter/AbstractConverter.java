package lark.db.jsd.converter;

/**
 * @author hongmiao.yu
 */
public abstract class AbstractConverter<T> implements Converter<T> {

    protected final Class<T> javaType;

    AbstractConverter(Class<T> javaType) {
        this.javaType = javaType;
    }

    @Override
    public Object toDbValue(T javaValue) {
        if (javaValue == null) return null;
        return convertToDbValue(javaValue);
    }

    protected abstract Object convertToDbValue(T javaValue);

    @Override
    public T toJavaValue(Object dbValue) {
        if (dbValue == null) return null;
        return convertToJavaValue(dbValue);
    }

    protected abstract T convertToJavaValue(Object dbValue);
}
