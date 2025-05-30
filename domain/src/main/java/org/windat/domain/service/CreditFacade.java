package org.windat.domain.service;

import org.windat.domain.entity.CreditTransaction;
import org.windat.domain.entity.User;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface CreditFacade {

    User transferCredits(UUID senderId, Integer receiverId, Integer amount, String description);

    Collection<CreditTransaction> readAll();

    CreditTransaction create(CreditTransaction creditTransaction);

    Optional<CreditTransaction> readOne(Integer id);

    CreditTransaction update(CreditTransaction creditTransaction);

    Collection<CreditTransaction> readAllTransactionsForSpecificUser(Integer userId);
}
