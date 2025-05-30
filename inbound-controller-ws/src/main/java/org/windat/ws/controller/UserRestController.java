package org.windat.ws.controller;

import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;
import org.windat.domain.enums.UserRole;
import org.windat.domain.entity.User;
import org.windat.domain.exceptions.UserNotFoundException;
import org.windat.domain.service.CreditFacade;
import org.windat.domain.service.UserFacade;
import org.windat.rest.api.UsersApi;
import org.windat.rest.dto.CreditTransferRequestDto;
import org.windat.rest.dto.UserCreateRequestDto;
import org.windat.rest.dto.UserDto;
import org.windat.ws.mapper.UserMapper;


import java.util.*;
import java.util.stream.Collectors;

@RestController
public class UserRestController implements UsersApi {

    private final UserFacade userFacade;
    private final UserMapper userMapper;
    private final Keycloak keycloakAdminClient;
    private final CreditFacade creditFacade;

    public UserRestController(
            UserFacade userFacade,
            UserMapper userMapper,
            Keycloak keycloakAdminClient,
            CreditFacade creditFacade
    ) {
        this.userFacade = userFacade;
        this.userMapper = userMapper;
        this.keycloakAdminClient = keycloakAdminClient;
        this.creditFacade = creditFacade;
    }

    @Override
//    TODO: REFACTOR THIS CODE. ENCAPSULATE THIS INTO SERVICE SO IT SUITS HEXAGONAL ARCHITECTURE
    public ResponseEntity<UserDto> createUser(UserCreateRequestDto userCreateRequestDto) {
//        Creating representtion of user for the Keycloak API
        UserRepresentation userRepresentation = new UserRepresentation();

        userRepresentation.setUsername(userCreateRequestDto.getUsername());
        userRepresentation.setEmail(userCreateRequestDto.getEmail());
        userRepresentation.setFirstName(userCreateRequestDto.getFirstName());
        userRepresentation.setLastName(userCreateRequestDto.getLastName());
        userRepresentation.setEmailVerified(false);
        userRepresentation.setEnabled(true);

//        Adding credential
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();

        credentialRepresentation.setTemporary(false);
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setValue(userCreateRequestDto.getPassword());
        userRepresentation.setCredentials(Collections.singletonList(credentialRepresentation));

//        Idk about attributes. For now I will leave it blank
        userRepresentation.setAttributes(Collections.emptyMap());

//        Call KeycloakAPI to create user
        RealmResource realmsResource = keycloakAdminClient.realm("WinDat");
        UsersResource usersResource = realmsResource.users();

//        I think error occures here
        Response response = usersResource.create(userRepresentation);
        if (response.getStatus() == 401 ){
            System.out.println("User creation failed");
        }
//        Successful creation of user
        if (response.getStatus() == 201) {

//            Get keycloak id of user
            String path = response.getLocation().getPath();
            String keycloakUserId = path.substring(path.lastIndexOf('/') + 1);
            UUID keycloakUuid = UUID.fromString(keycloakUserId);

//            Save user in application database
            User user = new User();
            user.setKeycloakId(keycloakUuid);
            user.setLoginName(userCreateRequestDto.getUsername());
            user.setUserRoleEnum(UserRole.USER_ROLE);
            user.setLobby(null);
            user.setCs2Username(userCreateRequestDto.getCs2Username());

            User persistedUser = userFacade.create(user);

//            Return successful response
            UserDto persistedUserDto = userMapper.toDto(persistedUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(persistedUserDto);

        } else if (response.getStatus() == 409) {
//            409 conflict
            throw new IllegalArgumentException("User with this username or email already exists in Keycloak.");
        } else {
            String errorMessage = "Failed to create user in Keycloak. Status: " + response.getStatus();
            try {
                errorMessage += ", Error: " + response.readEntity(String.class);
            } catch (Exception e) {
//                pass
            }
            throw new RuntimeException(errorMessage);
        }
    }

    @Override
    public ResponseEntity<List<UserDto>> getLeaderboard() {
        Collection<User> topUsers = userFacade.readBest10UsersByWins();

        List<UserDto> userDtos = topUsers.stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(userDtos);
    }

    @Override
    public ResponseEntity<UserDto> getOneAuthenticatedApplicationUser() {
        //        Get user from jwt token
        UserDto userDto = (UserDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UUID keycloakId = userDto.getKeycloakId();

        User user = userFacade.readOne(keycloakId).orElseThrow(
                () -> new UserNotFoundException("User with keycloakId " + keycloakId + " not found")
        );

        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @Override
    public ResponseEntity<UserDto> transferCredits(CreditTransferRequestDto creditTransferRequestDto) {
//        Get user from jwt token
        UserDto userDto = (UserDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UUID senderKeycloakId = userDto.getKeycloakId();

        User updatedSenderDomainUser = creditFacade.transferCredits(
                senderKeycloakId,
                creditTransferRequestDto.getReceiverId(),
                creditTransferRequestDto.getAmount(),
                creditTransferRequestDto.getDescription()
        );

        return ResponseEntity.ok(userMapper.toDto(updatedSenderDomainUser));
    }
}
