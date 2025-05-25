package org.windat.main;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.stereotype.Component;
import org.windat.domain.repository.UserRepository;
import org.windat.domain.service.UserFacade;
import org.windat.domain.service.UserService;
import org.windat.jpa.adapter.JpaUserRepositoryAdapter;
import org.windat.jpa.repository.UserSpringDataRepository;

/*
Manual creation and configuration of Users beans
 */
@Component
public class UserBeanConfiguration {

    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public UserFacade userFacade(UserRepository userRepository) {
        return new UserService(userRepository);
    }

    @Bean
    public UserRepository userRepository(UserSpringDataRepository userSpringDataRepository) {
        return new JpaUserRepositoryAdapter(userSpringDataRepository);
    }

    @Bean
    public UserSpringDataRepository userSpringDataRepository(){
        JpaRepositoryFactory jpaRepositoryFactory = new JpaRepositoryFactory(entityManager);
        return jpaRepositoryFactory.getRepository(UserSpringDataRepository.class);
    }
}
