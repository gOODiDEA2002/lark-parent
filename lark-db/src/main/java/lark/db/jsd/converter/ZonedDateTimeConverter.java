package lark.db.jsd.converter;

import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class ZonedDateTimeConverter extends AbstractConverter<ZonedDateTime> {

    public ZonedDateTimeConverter() {
        super(ZonedDateTime.class);
    }

    @Override
    protected Object convertToDbValue(ZonedDateTime javaValue) {
        return Timestamp.from(javaValue.toInstant());
    }

    @Override
    protected ZonedDateTime convertToJavaValue(Object dbValue) {
        Timestamp ts = (Timestamp) dbValue;
        return ZonedDateTime.ofInstant(ts.toInstant(), ZoneOffset.UTC);
    }
}
