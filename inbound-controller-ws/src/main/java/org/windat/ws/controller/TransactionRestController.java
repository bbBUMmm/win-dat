package org.windat.ws.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.windat.domain.entity.CreditTransaction;
import org.windat.domain.entity.User;
import org.windat.domain.service.CreditFacade;
import org.windat.domain.service.UserFacade;
import org.windat.rest.api.TransactionsApi;
import org.windat.rest.dto.CreditTransactionDto;
import org.windat.rest.dto.CreditTransactionRequestDto;
import org.windat.rest.dto.UserDto;
import org.windat.ws.mapper.CreditTransactionMapper;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
public class TransactionRestController implements TransactionsApi {

    private final CreditFacade creditFacade;
    private final UserFacade userFacade;
    private final CreditTransactionMapper creditTransactionMapper;

    public TransactionRestController(
            CreditFacade creditFacade,
            UserFacade userFacade,
            CreditTransactionMapper creditTransactionMapper
    ) {
        this.creditFacade = creditFacade;
        this.userFacade = userFacade;
        this.creditTransactionMapper = creditTransactionMapper;
    }

//    This method is not implemented yet
    @Override
    public ResponseEntity<CreditTransactionDto> createCreditTransaction(CreditTransactionRequestDto creditTransactionRequestDto) {
        return ResponseEntity.badRequest().build();
    }

//    You need admin right for this endpoint to see all transactions
//    Or you can see your own transactions
    @Override
    public ResponseEntity<List<CreditTransactionDto>> getCreditTransactions(Integer userId) {

//        Get authentication details
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDto principalUserDto = (UserDto) authentication.getPrincipal();
        UUID authenticatedUserKeycloakId = principalUserDto.getKeycloakId();

        User applicationUser = userFacade.readOne(authenticatedUserKeycloakId).orElseThrow(
                () -> new IllegalStateException("Authenticated user not found in application database.")
        );

        Integer authenticatedUserId = applicationUser.getId(); // Assuming UserDto has getId()


        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN_ROLE"));

        Collection<CreditTransaction> transactions;

        if (userId != null) {
            // User explicitly requested transactions for a specific userId
            if (userId.equals(authenticatedUserId)) {
                // User requesting their own transactions (userId matches authenticated user's ID)
                transactions = creditFacade.readAllTransactionsForSpecificUser(userId);
            } else {
                // User requesting another user's transactions. Check for admin rights.
                if (isAdmin) {
                    transactions = creditFacade.readAllTransactionsForSpecificUser(userId);
                } else {
                    // Not admin and requesting someone else's transactions
                    return ResponseEntity.status(403).build(); // Forbidden
                }
            }
        } else {
            // userId is null:
            // If admin, return ALL transactions.
            // If not admin, return transactions for the currently authenticated user.
            if (isAdmin) {
                transactions = creditFacade.readAll(); // Admin gets ALL transactions
            } else {
                transactions = creditFacade.readAllTransactionsForSpecificUser(applicationUser.getId());
            }
        }

        // Convert domain entities to DTOs
        List<CreditTransactionDto> transactionDtos = transactions.stream()
                .map(creditTransactionMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(transactionDtos);
    }
}
