package org.windat.main;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.stereotype.Component;
import org.windat.domain.repository.LobbyRepository;
import org.windat.domain.service.LobbyFacade;
import org.windat.domain.service.LobbyService;
import org.windat.jpa.adapter.JpaLobbyRepositoryAdapter;
import org.windat.jpa.repository.LobbySpringDataRepository;

/*
    Autoconfiguration to create the necessary beans was not working for me
    So I created Beans manually
 */
@Component
public class LobbyBeanConfiguration {


    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public LobbyFacade lobbyFacade(LobbyRepository lobbyRepository) {
        return new LobbyService(lobbyRepository);
    }

    /*
    Other method which worked for me is to comment or remove next two Beans (LobbyRepository and LobbySpringDataRepository)
    And EntityManager and add annotations which I will leave in WinDatApplication.java class
     */
    @Bean
    public LobbyRepository lobbyRepository(LobbySpringDataRepository lobbySpringDataRepository) {
        return new JpaLobbyRepositoryAdapter(lobbySpringDataRepository);
    }

    @Bean
    public LobbySpringDataRepository lobbySpringDataRepository() {
        JpaRepositoryFactory factory = new JpaRepositoryFactory(entityManager);
        return factory.getRepository(LobbySpringDataRepository.class);
    }
}
