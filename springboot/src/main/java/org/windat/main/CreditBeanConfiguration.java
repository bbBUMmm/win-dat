package org.windat.main;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.stereotype.Component;
import org.windat.domain.repository.CreditRepository;
import org.windat.domain.repository.UserRepository;
import org.windat.domain.service.CreditFacade;
import org.windat.domain.service.CreditService;
import org.windat.jpa.adapter.JpaCreditRepositoryAdapter;
import org.windat.jpa.repository.CreditSpringDataRepository;

/*
Manual creation and configuration of Credit beans
 */
@Component
public class CreditBeanConfiguration {

    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public CreditFacade creditFacade(CreditRepository creditRepository, UserRepository userRepository) {
        return new CreditService(userRepository, creditRepository);
    }

    @Bean
    public CreditRepository creditRepository(CreditSpringDataRepository creditSpringDataRepository) {
        return new JpaCreditRepositoryAdapter(creditSpringDataRepository);
    }

    @Bean
    public CreditSpringDataRepository creditSpringDataRepository() {
        JpaRepositoryFactory jpaRepositoryFactory = new JpaRepositoryFactory(entityManager);
        return jpaRepositoryFactory.getRepository(CreditSpringDataRepository.class);
    }

}
