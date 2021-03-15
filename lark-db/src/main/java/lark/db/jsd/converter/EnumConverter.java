package lark.db.jsd.converter;

import lark.core.lang.EnumValuable;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hongmiao.yu
 */
public class EnumConverter<T extends Enum<T>> extends AbstractConverter<T> {
    private final Map<Integer, T> valueToConstants = new HashMap<>();
    private final Map<String, T> nameToConstants = new HashMap<>();

    private final boolean isEnumValueSupport;

    public EnumConverter(Class<T> javaType) {
        super(javaType);
        this.isEnumValueSupport = EnumValuable.class.isAssignableFrom(javaType);
        if (this.isEnumValueSupport) {
            for (T v : javaType.getEnumConstants()) {
                valueToConstants.put(((EnumValuable)v).value(), v);
            }
        } else {
            for (T v : javaType.getEnumConstants()) {
                nameToConstants.put(v.name(), v);
            }
        }
    }

    @Override
    protected Object convertToDbValue(T value) {
        if (isEnumValueSupport) {
            return ((EnumValuable) value).value();
        }
        return value.name();
    }

    @Override
    protected T convertToJavaValue(Object dbValue) {
        if (isEnumValueSupport) {
            Integer value = (Integer)dbValue;
            return valueToConstants.get(value);
        } else {
            String name = (String)dbValue;
            return nameToConstants.get(name);
        }
    }
}
