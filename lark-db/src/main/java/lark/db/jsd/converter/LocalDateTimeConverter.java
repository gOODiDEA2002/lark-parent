package lark.db.jsd.converter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class LocalDateTimeConverter extends AbstractConverter<LocalDateTime> {

    public LocalDateTimeConverter() {
        super(LocalDateTime.class);
    }

    @Override
    protected Object convertToDbValue(LocalDateTime javaValue) {
        return Timestamp.valueOf(javaValue);
    }

    @Override
    protected LocalDateTime convertToJavaValue(Object dbValue) {
        return ((Timestamp)dbValue).toLocalDateTime();
    }
}
