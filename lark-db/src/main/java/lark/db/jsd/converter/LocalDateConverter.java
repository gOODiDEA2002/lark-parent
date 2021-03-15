package lark.db.jsd.converter;

import java.sql.Date;
import java.time.LocalDate;

public class LocalDateConverter extends AbstractConverter<LocalDate> {

    public LocalDateConverter() {
        super(LocalDate.class);
    }

    @Override
    protected Object convertToDbValue(LocalDate javaValue) {
        return Date.valueOf(javaValue);
    }

    @Override
    protected LocalDate convertToJavaValue(Object dbValue) {
        return ((Date)dbValue).toLocalDate();
    }
}
