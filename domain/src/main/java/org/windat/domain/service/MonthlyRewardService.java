package org.windat.domain.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.windat.domain.entity.CreditTransaction;
import org.windat.domain.entity.User;
import org.windat.domain.enums.TransactionType;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MonthlyRewardService {

    private final UserFacade userFacade;
    private final CreditFacade creditFacade;

//    Get values from the properties
//    Set default to 10 if not found
    @Value("${rewards.leaderboard.top-players-count:10}")
    private int topPlayersCount;

//    Use a map to store prizes for different ranks
    private final Map<Integer, Integer> prizes = new HashMap<>();

    public MonthlyRewardService(UserFacade userFacade, CreditFacade creditFacade,
                                @Value("${rewards.leaderboard.prizes.1st-place:0}") int p1,
                                @Value("${rewards.leaderboard.prizes.2nd-place:0}") int p2,
                                @Value("${rewards.leaderboard.prizes.3rd-place:0}") int p3,
                                @Value("${rewards.leaderboard.prizes.4th-place:0}") int p4,
                                @Value("${rewards.leaderboard.prizes.5th-place:0}") int p5,
                                @Value("${rewards.leaderboard.prizes.6th-place:0}") int p6,
                                @Value("${rewards.leaderboard.prizes.7th-place:0}") int p7,
                                @Value("${rewards.leaderboard.prizes.8th-place:0}") int p8,
                                @Value("${rewards.leaderboard.prizes.9th-place:0}") int p9,
                                @Value("${rewards.leaderboard.prizes.10th-place:0}") int p10
    ) {
        this.userFacade = userFacade;
        this.creditFacade = creditFacade;

//        Populate the prizes map
        prizes.put(1, p1);
        prizes.put(2, p2);
        prizes.put(3, p3);
        prizes.put(4, p4);
        prizes.put(5, p5);
        prizes.put(6, p6);
        prizes.put(7, p7);
        prizes.put(8, p8);
        prizes.put(9, p9);
        prizes.put(10, p10);
    }

    /**
     * This method is scheduled to run at 23:59:00 on the last day of every month.
     * It identifies the top players and distributes monthly rewards.
     */
//    cron = "0 59 23 L * ?" means: at 23:59:00, on the last day of the month, every month, any day of the week.
//    L means "last day of the month"
    @Scheduled(cron = "0 59 23 L * ?")
    @Transactional // Ensure the entire reward distribution is atomic
    public void distributeMonthlyRewards() {
        System.out.println("Starting monthly reward distribution at " + LocalDateTime.now());


        List<User> topUsers = userFacade.readBest10UsersByWins()
                .stream()
                .limit(topPlayersCount)
                .toList();

        if (topUsers.isEmpty()) {
            System.out.println("No users on the leaderboard to distribute rewards.");
            return;
        }

        for (int i = 0; i < topUsers.size(); i++) {
            User user = topUsers.get(i);
            int rank = i + 1;
            Integer rewardAmount = prizes.getOrDefault(rank, 0); // Get reward for rank, default to 0

            if (rewardAmount > 0) {
//                Add credits to the user's balance
                user.addCredits(rewardAmount);
                userFacade.update(user);

//                Record the transaction
                CreditTransaction rewardTransaction = new CreditTransaction();
                rewardTransaction.setAmount(rewardAmount);
                rewardTransaction.setUser(user);
                rewardTransaction.setTransactionTime(LocalDateTime.now());
                rewardTransaction.setDescription("Monthly leaderboard reward for rank " + rank);
                rewardTransaction.setType(TransactionType.MONTHLY_BONUS);

                creditFacade.create(rewardTransaction);

//                TODO: change this form sout to use some logger
                System.out.println("Distributed " + rewardAmount + " credits to " + user.getLoginName() + " (Rank " + rank + ")");
            } else {
                System.out.println("No reward defined for rank " + rank + " or beyond for " + user.getLoginName());
            }
        }
        System.out.println("Finished monthly reward distribution.");
    }
}
