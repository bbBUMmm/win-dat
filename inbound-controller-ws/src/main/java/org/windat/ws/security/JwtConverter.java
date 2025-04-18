package org.windat.ws.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.windat.rest.dto.UserDto;
import org.windat.rest.dto.UserRoleDto;

import java.util.*;

class JwtConverter extends AbstractAuthenticationToken {
    private final Jwt source;

    public JwtConverter(Jwt source) {
        super(Collections.emptyList());
        this.source = Objects.requireNonNull(source);
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return Collections.emptyList();
    }

    /*
    There could be problems with this because user is not setup correctly yet
     */
    @Override
    public Object getPrincipal() {
        UserDto userDto = new UserDto();
//        userDto.setEmail(source.getClaimAsString("email"));
//        userDto.setName(source.getClaimAsString("given_name"));
//        userDto.setRola(getRole());
        userDto.setLoginName(source.getClaimAsString("login_name"));
        return userDto;
    }

    private UserRoleDto getRole() {
        Map<String, Object> realmAccess = source.getClaimAsMap("realm_access");
        if (realmAccess == null || realmAccess.get("roles") == null) return null;

        @SuppressWarnings("unchecked")
        List<String> roles = (List<String>) realmAccess.get("roles");
        Optional<UserRoleDto> userRoleDto = findRole(roles);

        return userRoleDto.orElse(null);
    }

    private Optional<UserRoleDto> findRole(List<String> roles) {
        return roles.stream()
                .filter(role -> Arrays.stream(UserRoleDto.values())
                        .anyMatch(enumRole -> enumRole.name().equals(role)))
                .map(UserRoleDto::fromValue)
                .findFirst();
    }
}
