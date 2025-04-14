package greenbuildings.enterprise.services;

import greenbuildings.enterprise.entities.TransactionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface TransactionService {
    List<TransactionEntity> getTransactions(UUID buildingId);
    Page<TransactionEntity> search(UUID buildingId, Pageable page);
}
