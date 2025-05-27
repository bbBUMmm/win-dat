package org.windat.main;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.stereotype.Component;
import org.windat.domain.repository.UserRepository;
import org.windat.domain.service.CreditFacade;
import org.windat.domain.service.MonthlyRewardService;
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

    @Bean
    public MonthlyRewardService monthlyRewardService(UserFacade userFacade, CreditFacade creditFacade,
                                                     @Value("${rewards.leaderboard.prizes.1st-place:0}") int p1,
                                                     @Value("${rewards.leaderboard.prizes.2nd-place:0}") int p2,
                                                     @Value("${rewards.leaderboard.prizes.3rd-place:0}") int p3,
                                                     @Value("${rewards.leaderboard.prizes.4th-place:0}") int p4,
                                                     @Value("${rewards.leaderboard.prizes.5th-place:0}") int p5,
                                                     @Value("${rewards.leaderboard.prizes.6th-place:0}") int p6,
                                                     @Value("${rewards.leaderboard.prizes.7th-place:0}") int p7,
                                                     @Value("${rewards.leaderboard.prizes.8th-place:0}") int p8,
                                                     @Value("${rewards.leaderboard.prizes.9th-place:0}") int p9,
                                                     @Value("${rewards.leaderboard.prizes.10th-place:0}") int p10,
                                                     @Value("${rewards.leaderboard.top-players-count:10}") int topPlayersCount
    ) {
        return new MonthlyRewardService(userFacade, creditFacade, p1, p2, p3, p4, p5, p6, p7, p8, p9, p10);
    }
}
