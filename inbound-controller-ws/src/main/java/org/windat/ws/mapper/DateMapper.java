package org.windat.ws.mapper;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;

public interface DateMapper {

    default OffsetDateTime map(Date date) {
        return date == null ? null : date.toInstant().atOffset(ZoneOffset.UTC);
    }

    default Date map(OffsetDateTime offsetDateTime) {
        return offsetDateTime == null ? null : Date.from(offsetDateTime.toInstant());
    }
}
