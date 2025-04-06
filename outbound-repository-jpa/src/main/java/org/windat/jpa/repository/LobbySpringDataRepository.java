package org.windat.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.windat.domain.entity.Lobby;

public interface LobbySpringDataRepository extends JpaRepository<Lobby, Integer> {
}
