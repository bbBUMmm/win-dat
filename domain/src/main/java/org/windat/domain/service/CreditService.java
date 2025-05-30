package org.windat.domain.service;

import jakarta.transaction.Transactional;
import org.windat.domain.entity.CreditTransaction;
import org.windat.domain.entity.User;
import org.windat.domain.enums.TransactionType;
import org.windat.domain.exceptions.UserNotFoundException;
import org.windat.domain.repository.CreditRepository;
import org.windat.domain.repository.UserRepository;

import java.util.*;

public class CreditService implements CreditFacade {

    private final UserRepository userRepository;
    private final CreditRepository creditRepository;

    public CreditService(UserRepository userRepository, CreditRepository creditRepository) {
        this.userRepository = userRepository;
        this.creditRepository = creditRepository;
    }

    @Override
    @Transactional
    public User transferCredits(UUID senderId, Integer receiverId, Integer amount, String description) {
        Objects.requireNonNull(senderId, "Sender Keycloak ID cannot be null.");
        Objects.requireNonNull(receiverId, "Receiver ID cannot be null.");
        Objects.requireNonNull(amount, "Amount cannot be null.");

        if (amount <= 0) {
            throw new IllegalArgumentException("Transfer amount must be positive.");
        }


        User sender = userRepository.readOne(senderId)
                .orElseThrow(() -> new UserNotFoundException("Sender user not found with Keycloak ID: " + senderId));

//        Get receiver using application id, not keycloakId
        User receiver = userRepository.readOne(receiverId)
                .orElseThrow(() -> new UserNotFoundException("Receiver user not found with ID: " + receiverId));

        if (sender.getId() == receiver.getId()) {
            throw new IllegalArgumentException("Cannot transfer credits to self.");
        }

        sender.deductCredits(amount);
        receiver.addCredits(amount);

        userRepository.update(sender);
        userRepository.update(receiver);

        CreditTransaction senderTransaction = new CreditTransaction(
                sender,
//                Negative value to be written off
                -amount,
                description != null ? description : "Transfer to " + receiver.getLoginName(),
                receiver,
                TransactionType.USER_TRANSFER_OUT
        );

        creditRepository.create(senderTransaction);

        CreditTransaction receiverTx = new CreditTransaction(
                receiver,
//                Positive value for accrual
                amount,
                description != null ? description : "Transfer from " + sender.getLoginName(),
                sender,
                TransactionType.USER_TRANSFER_IN
        );

        creditRepository.create(receiverTx);

//        Return updated sender
        return sender;
    }

    @Override
    public Collection<CreditTransaction> readAll() {
        return creditRepository.readAll();
    }

    @Override
    @Transactional
    public CreditTransaction create(CreditTransaction creditTransaction) {
        return creditRepository.create(creditTransaction);
    }

    @Override
    public Optional<CreditTransaction> readOne(Integer id) {
        return creditRepository.readOne(id);
    }

    @Override
    @Transactional
    public CreditTransaction update(CreditTransaction creditTransaction) {
        return creditRepository.update(creditTransaction);
    }

    @Override
    public Collection<CreditTransaction> readAllTransactionsForSpecificUser(Integer userId) {
        return creditRepository.readByUserId(userId);
    }
}
