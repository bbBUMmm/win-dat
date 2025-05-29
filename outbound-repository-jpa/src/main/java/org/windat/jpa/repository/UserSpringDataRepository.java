package org.windat.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.windat.domain.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserSpringDataRepository extends JpaRepository<User, Integer> {
    Optional<User> findByKeycloakId(UUID keycloakId);

    List<User> findTop10ByOrderByGamesWonDesc();

    Optional<User> findByCs2Username(String username);
}
