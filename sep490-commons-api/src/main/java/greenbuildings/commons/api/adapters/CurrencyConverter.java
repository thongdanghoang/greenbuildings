package greenbuildings.commons.api.adapters;

import greenbuildings.commons.api.enums.Currency;

import java.math.BigDecimal;

public interface CurrencyConverter {
    
    /**
     * Converts an amount from one currency to another.
     *
     * @param amount the amount to be converted
     * @param fromCurrency the currency of the amount to be converted
     * @param toCurrency the currency to which the amount should be converted
     * @return the converted amount in the target currency
     */
    BigDecimal convert(BigDecimal amount, Currency fromCurrency, Currency toCurrency);
    
    /**
     * Converts an amount from the default currency to another currency.
     *
     * @param amount the default (cents) stored in the database
     * @param toCurrency the currency to which the amount should be converted
     * @return the converted amount in the target currency
     */
    BigDecimal convert(long amount, Currency toCurrency);
    
    /**
     * Converts an amount from a specified currency to the default currency.
     *
     * @param amount the amount to be converted
     * @param fromCurrency the currency of the amount to be converted
     * @return the converted amount in the default currency (cents)
     */
    long defaultCurrencyConvert(BigDecimal amount, Currency fromCurrency);
    
}
