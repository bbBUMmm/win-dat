package org.windat.domain.repository;

import org.windat.domain.entity.CreditTransaction;

import java.util.Collection;
import java.util.Optional;

public interface CreditRepository {

    Collection<CreditTransaction> readAll();

    CreditTransaction create(CreditTransaction creditTransaction);

    Optional<CreditTransaction> readOne(Integer id);

    CreditTransaction update(CreditTransaction creditTransaction);

    Collection<CreditTransaction> readByUserId(Integer userId);
}
