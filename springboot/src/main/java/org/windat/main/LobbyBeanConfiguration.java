package org.windat.main;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.windat.domain.repository.LobbyRepository;
import org.windat.domain.service.LobbyFacade;
import org.windat.domain.service.LobbyService;

@Component
public class LobbyBeanConfiguration {

    @Bean
    public LobbyFacade lobbyFacade(LobbyRepository lobbyRepository) {
        return new LobbyService(lobbyRepository);
    }
}
