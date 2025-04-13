package greenbuildings.enterprise.rest;

import greenbuildings.commons.api.adapters.CurrencyConverter;
import greenbuildings.commons.api.enums.Currency;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/currency-converter")
@RequiredArgsConstructor
public class CurrencyConverterResource {
    
    private final CurrencyConverter currencyConverter;
    
    @GetMapping("/VND/{amount}/{toCurrency}")
    public ResponseEntity<BigDecimal> convertCurrency(@PathVariable Currency toCurrency,
                                                      @PathVariable BigDecimal amount) {
        return ResponseEntity.ok(currencyConverter.convert(amount, Currency.VND, toCurrency));
    }
    
}
