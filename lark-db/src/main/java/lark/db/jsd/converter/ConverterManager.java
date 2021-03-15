package lark.db.jsd.converter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hongmiao.yu
 */
@SuppressWarnings("unchecked")
public class ConverterManager {

    private static Map<Class<?>, Converter<?>> converters = new ConcurrentHashMap<>();

    static {
        register(Date.class, DateConverter.class);
        register(LocalDate.class, LocalDateConverter.class);
        register(LocalDateTime.class, LocalDateTimeConverter.class);
        register(ZonedDateTime.class, ZonedDateTimeConverter.class);
    }

    public static <T> Converter<T> get(Class<T> type) {
        return (Converter<T>) converters.computeIfAbsent(type, t -> {
            if (t.isEnum()) {
                return new EnumConverter(type);
            }
            return new Converter.NoConversion<T>();
        });
    }

    public static <T> void register(Class<T> type, Converter<T> converter) {
        converters.put(type, converter);
    }

    public static void register(Class<?> type, Class<? extends Converter> converterClass) {
        Converter<?> converter;
        try {
            converter = converterClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        converters.put(type, converter);
    }

    public static Object toDbValue(Object javaValue) {
        if (javaValue == null)
            return null;

        Converter converter = get(javaValue.getClass());
        return converter.toDbValue(javaValue);
    }
}
