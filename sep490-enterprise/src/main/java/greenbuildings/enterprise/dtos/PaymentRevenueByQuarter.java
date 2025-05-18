package greenbuildings.enterprise.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class PaymentRevenueByQuarter {
    private int quarter;
    private long revenue;
}
