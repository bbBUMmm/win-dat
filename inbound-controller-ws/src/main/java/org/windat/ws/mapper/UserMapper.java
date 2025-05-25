package org.windat.ws.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.windat.domain.UserRole;
import org.windat.domain.entity.User;
import org.windat.rest.dto.UserDto;
import org.windat.rest.dto.UserRoleDto;


@Mapper(componentModel = "spring", uses = {DateMapper.class})
public interface UserMapper {

    @Mapping(source = "lobby.id", target = "lobbyId")
    UserDto toDto(User user);

    User toUser(UserDto userDto);

    @Named("toDtoShallow")
    static UserDto toDtoShallow(User user) {
        if (user == null) return null;

        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setKeycloakId(user.getKeycloakId());
        dto.setLoginName(user.getLoginName());
        dto.setUserRoleEnum(toUserRoleDto(user.getUserRoleEnum()));
        dto.setLobbyId(user.getCurrentLobbyId());
        return dto;
    }

    // Enum mapping from domain to DTO
    static UserRoleDto toUserRoleDto(UserRole role) {
        if (role == null) {
            return null;
        }
        return switch (role) {
            case USER_ROLE -> UserRoleDto.USER_ROLE;
            case ADMIN_ROLE -> UserRoleDto.ADMIN_ROLE;
        };
    }

    // Enum mapping from DTO to domain
    default UserRole toUserRole(UserRoleDto dtoRole) {
        if (dtoRole == null) {
            return null;
        }
        return switch (dtoRole) {
            case USER_ROLE -> UserRole.USER_ROLE;
            case ADMIN_ROLE -> UserRole.ADMIN_ROLE;
        };
    }
}