package greenbuildings.enterprise.dtos;

import greenbuildings.enterprise.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class NumberOfTransactionByType {
    private TransactionType type;
    private long count;
}
