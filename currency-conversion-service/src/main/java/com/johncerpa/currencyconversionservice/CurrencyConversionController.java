package com.johncerpa.currencyconversionservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;

@RestController
public class CurrencyConversionController {

    @Autowired
    private CurrencyExchangeProxy proxy;

    @GetMapping("currency-conversion/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversion currencyConversion(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity) {
        final String url = "http://localhost:8000/currency-exchange/from/{from}/to/{to}";

        final HashMap<String, String> uriVariables = new HashMap<String, String>() {
            {
                put("from", from);
                put("to", to);
            }
        };

        ResponseEntity<CurrencyConversion> response = new RestTemplate().getForEntity(url, CurrencyConversion.class, uriVariables);

        CurrencyConversion currencyConversion = response.getBody();

        return new CurrencyConversion(
            currencyConversion.getId(),
            from, to, quantity,
            currencyConversion.getConversionMultiple(),
            quantity.multiply(currencyConversion.getConversionMultiple()),
            currencyConversion.getEnvironment()
        );
    }

    @GetMapping("currency-conversion-feign/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversion currencyConversionFeign(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity) {
        CurrencyConversion currencyConversion = proxy.getExchangeValue(from, to);

        return new CurrencyConversion(
            currencyConversion.getId(),
            from, to, quantity,
            currencyConversion.getConversionMultiple(),
            quantity.multiply(currencyConversion.getConversionMultiple()),
            currencyConversion.getEnvironment()
        );
    }

}
