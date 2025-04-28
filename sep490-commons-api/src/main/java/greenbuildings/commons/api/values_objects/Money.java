package greenbuildings.commons.api.values_objects;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;

@Getter
public class Money {
    private final BigDecimal amount;
    private final Currency currency;
    
    public Money(@NotNull @Min(0) BigDecimal amount, @NotNull Currency currency) {
        this.amount = amount;
        this.currency = currency;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        Money money = (Money) o;
        return amount.compareTo(money.amount) == 0 && currency == money.currency;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }
}
