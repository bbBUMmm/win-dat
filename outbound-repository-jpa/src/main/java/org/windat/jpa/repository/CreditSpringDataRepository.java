package org.windat.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.windat.domain.entity.CreditTransaction;

import java.util.Collection;
import java.util.Optional;

public interface CreditSpringDataRepository extends JpaRepository<CreditTransaction, Integer> {
    @Query("SELECT ct FROM CreditTransaction ct WHERE ct.user.id = :userId OR ct.relatedUser.id = :userId")
    Collection<CreditTransaction> findByUserId(@Param("userId") Integer userId);
}
