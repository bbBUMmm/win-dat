package org.windat.ws.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.windat.domain.entity.CreditTransaction;
import org.windat.domain.enums.TransactionType;
import org.windat.rest.dto.CreditTransactionDto;
import org.windat.rest.dto.TransactionTypeDto;

import java.util.Collection;
import java.util.List;


@Mapper(componentModel = "spring", uses = {DateMapper.class, UserMapper.class})
public interface CreditTransactionMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "relatedUser.id", target = "relatedUserId")
    @Mapping(source = "transactionTime", target = "transactionTime", qualifiedByName = "dateToOffsetDateTime")
    @Mapping(source = "type", target = "type")
    CreditTransactionDto toDto(CreditTransaction entity);


    List<CreditTransactionDto> toDtoList(Collection<CreditTransaction> entities);

    default TransactionTypeDto mapTransactionType(TransactionType domainType) {
        if (domainType == null) {
            return null;
        }
        return TransactionTypeDto.valueOf(domainType.name());
    }

    default TransactionType mapTransactionType(TransactionTypeDto dtoType) {
        if (dtoType == null) {
            return null;
        }
        return TransactionType.valueOf(dtoType.name());
    }
}
