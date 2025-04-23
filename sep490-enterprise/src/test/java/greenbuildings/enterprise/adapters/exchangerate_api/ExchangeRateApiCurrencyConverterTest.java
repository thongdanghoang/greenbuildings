package greenbuildings.enterprise.adapters.exchangerate_api;

import greenbuildings.commons.api.enums.Currency;
import greenbuildings.enterprise.TestcontainersConfigs;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExchangeRateApiCurrencyConverterTest extends TestcontainersConfigs {
    
    @Mock
    RestTemplate restTemplate;
    
    @InjectMocks
    private ExchangeRateApiCurrencyConverter currencyConverter;
    
    @BeforeEach
    void setUp() {
        ExchangeRateApiResponse mockResponse = new ExchangeRateApiResponse(
                "success",
                "https://example.com/docs",
                "https://example.com/terms",
                1638316800,
                "2021-12-01T00:00:00Z",
                1638403200,
                "2021-12-02T00:00:00Z",
                "USD",
                new ConversionRates(
                        BigDecimal.ONE, // USD to USD rate
                        new BigDecimal("7.2941"), // USD to CNY rate
                        new BigDecimal("25289.3072") // USD to VND rate
                )
        );
        Mockito
                .when(currencyConverter.getExchangeRates())
                .thenReturn(mockResponse);
    }
    
    @Test
    void testConvert_SameCurrency() {
        BigDecimal amount = new BigDecimal("100.00");
        BigDecimal result = currencyConverter.convert(amount, Currency.USD, Currency.USD);
        
        assertEquals(amount, result, "Conversion between the same currency should return the same amount.");
    }
    
    @Test
    void testConvert_USD_to_CNY() {
        BigDecimal amount = new BigDecimal("100.00");
        BigDecimal expected = new BigDecimal("729.41");
        BigDecimal result = currencyConverter.convert(amount, Currency.USD, Currency.CNY);
        
        assertEquals(expected, result, "Conversion from USD to CNY should be correct.");
    }
    
    @Test
    void testConvert_CNY_to_VND() {
        BigDecimal amount = new BigDecimal("100.00");
        BigDecimal expected = new BigDecimal("346709.08");
        BigDecimal result = currencyConverter.convert(amount, Currency.CNY, Currency.VND);
        
        assertEquals(expected, result, "Conversion from CNY to VND should be correct.");
    }
    
    @Test
    void testConvert_DefaultCurrency_to_CNY() {
        long amount = 10000; // Represents 100.00 USD in cents
        BigDecimal expected = new BigDecimal("729.41");
        BigDecimal result = currencyConverter.convert(amount, Currency.CNY);
        
        assertEquals(expected, result, "Conversion from default currency (USD) to CNY should be correct.");
    }
    
    @Test
    void testDefaultCurrencyConvert_USD_to_Default() {
        BigDecimal amount = new BigDecimal("100.00");
        long expected = 10000; // 100.00 USD in cents
        long result = currencyConverter.defaultCurrencyConvert(amount, Currency.USD);
        
        assertEquals(expected, result, "Conversion from USD to default currency (cents) should be correct.");
    }
    
    @Test
    void testDefaultCurrencyConvert_CNY_to_Default() {
        BigDecimal amount = new BigDecimal("729.41");
        long expected = 10000;
        long result = currencyConverter.defaultCurrencyConvert(amount, Currency.CNY);
        
        assertEquals(expected, result, "Conversion from CNY to default currency (cents) should be correct.");
    }
}
