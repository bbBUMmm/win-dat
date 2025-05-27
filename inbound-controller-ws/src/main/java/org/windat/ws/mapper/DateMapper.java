package org.windat.ws.mapper;

import org.mapstruct.Named;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;

public interface DateMapper {

    @Named("dateToOffsetDateTime")
    default OffsetDateTime map(Date date) {
        return date == null ? null : date.toInstant().atOffset(ZoneOffset.UTC);
    }

    @Named("offsetDateTimeToDate")
    default Date map(OffsetDateTime offsetDateTime) {
        return offsetDateTime == null ? null : Date.from(offsetDateTime.toInstant());
    }
}
