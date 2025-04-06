package org.windat.ws.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.windat.domain.entity.Lobby;
import org.windat.rest.dto.LobbyDto;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Mapper(componentModel = "spring")
public interface LobbyMapper {

    @Mapping(source = "created", target = "created", qualifiedByName = "dateToOffsetDateTime")
    @Mapping(source = "updated", target = "updated", qualifiedByName = "dateToOffsetDateTime")
    @Mapping(source = "closed", target = "closed", qualifiedByName = "dateToOffsetDateTime")
    LobbyDto toDto(Lobby lobby);

    @Named("dateToOffsetDateTime")
    default OffsetDateTime map(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant().atOffset(ZoneOffset.UTC);
    }
}