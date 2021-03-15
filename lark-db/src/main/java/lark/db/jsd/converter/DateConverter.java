package lark.db.jsd.converter;

import java.sql.Timestamp;
import java.util.Date;

public class DateConverter extends AbstractConverter<Date> {

    public DateConverter() {
        super(Date.class);
    }

    @Override
    protected Object convertToDbValue(Date javaValue) {
        return new Timestamp(javaValue.getTime());
    }

    @Override
    protected Date convertToJavaValue(Object dbValue) {
        return (Date) dbValue;
    }
}
