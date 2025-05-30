package org.windat.jpa.adapter;

import org.springframework.stereotype.Repository;
import org.windat.domain.entity.CreditTransaction;
import org.windat.domain.repository.CreditRepository;
import org.windat.jpa.repository.CreditSpringDataRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public class JpaCreditRepositoryAdapter implements CreditRepository {

    private final CreditSpringDataRepository creditSpringDataRepository;

    public JpaCreditRepositoryAdapter(CreditSpringDataRepository creditSpringDataRepository) {
        this.creditSpringDataRepository = creditSpringDataRepository;
    }
    @Override
    public Collection<CreditTransaction> readAll() {
        return creditSpringDataRepository.findAll();
    }

    @Override
    public CreditTransaction create(CreditTransaction creditTransaction) {
        return creditSpringDataRepository.save(creditTransaction);
    }

    @Override
    public Optional<CreditTransaction> readOne(Integer id) {
        return creditSpringDataRepository.findById(id);
    }

    @Override
    public CreditTransaction update(CreditTransaction creditTransaction) {
        return creditSpringDataRepository.save(creditTransaction);
    }

    @Override
    public Collection<CreditTransaction> readByUserId(Integer userId) {
        return creditSpringDataRepository.findByUserId(userId);
    }
}
