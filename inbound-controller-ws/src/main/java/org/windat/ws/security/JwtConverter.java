package org.windat.ws.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
        userDto.setKeycloakId(UUID.fromString(source.getSubject()));
        userDto.setLoginName(source.getClaim("preferred_username"));
//        I will comment out this for now
//        Letter need to implement role checking and do thing based on that
//        userDto.setUserRoleEnum(UserRoleDto.USER_ROLE);
        return userDto;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new HashSet<>(); // Використовуємо HashSet для уникнення дублікатів

//        Get real roles from jwt
        if (source.hasClaim("realm_access")) {
            Map<String, Object> realmAccess = source.getClaimAsMap("realm_access");
            if (realmAccess != null && realmAccess.containsKey("roles")) {
                Object rolesObject = realmAccess.get("roles");
                if (rolesObject instanceof List) {
                    ((List<?>) rolesObject).stream()
                            .filter(String.class::isInstance)
                            .map(String.class::cast)
//                            Add prefix role
                            .map(roleName -> new SimpleGrantedAuthority("ROLE_" + roleName))
                            .forEach(authorities::add);
                }
            }
        }

//        Get client roles from token
        if (source.hasClaim("resource_access")) {
            Map<String, Object> resourceAccess = source.getClaimAsMap("resource_access");
//            Here it is hardcoded for the fsa-client client
            if (resourceAccess != null && resourceAccess.containsKey("fsa-client")) {
                Map<String, Object> accountClient = (Map<String, Object>) resourceAccess.get("account");
                if (accountClient.containsKey("roles")) {
                    Object rolesObject = accountClient.get("roles");
                    if (rolesObject instanceof List) {
                        ((List<?>) rolesObject).stream()
                                .filter(String.class::isInstance)
                                .map(String.class::cast)
                                .map(roleName -> new SimpleGrantedAuthority("ROLE_" + roleName))
                                .forEach(authorities::add);
                    }
                }
            }
        }

        return authorities;
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
