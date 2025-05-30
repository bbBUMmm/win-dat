package org.windat.ws.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.windat.domain.entity.Lobby;
import org.windat.domain.entity.User;
import org.windat.rest.dto.LobbyCreateRequestDTODto;
import org.windat.rest.dto.LobbyDto;
import org.windat.rest.dto.UserDto;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {DateMapper.class, UserMapper.class})
public interface LobbyMapper {

    @Mapping(source = "created", target = "created", qualifiedByName = "dateToOffsetDateTime")
    @Mapping(source = "updated", target = "updated", qualifiedByName = "dateToOffsetDateTime")
    @Mapping(source = "closed", target = "closed", qualifiedByName = "dateToOffsetDateTime")
    @Mapping(target = "userList", qualifiedByName = "toDtoShallowList") // Це викликає toDtoShallow, який тепер поверне lobbyId
    LobbyDto toDto(Lobby lobby);

    @Named("toDtoShallowList")
    static List<UserDto> toDtoShallowList(List<User> users) {
        if (users == null) return null;
        return users.stream()
                .map(UserMapper::toDtoShallow)
                .collect(Collectors.toList());
    }

    @Mapping(source = "name", target = "name")
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "updated", ignore = true)
    @Mapping(target = "closed", ignore = true)
    Lobby dtoToEntity(LobbyCreateRequestDTODto dto);
}