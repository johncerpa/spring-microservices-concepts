package com.johncerpa.currencyexchangemicroservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import java.math.BigDecimal;
import java.util.Optional;

@RestController
public class CurrencyExchangeController {

    @Autowired
    private CurrencyExchangeRepository exchangeRepository;

    @Autowired
    private Environment environment;

    @GetMapping("/currency-exchange/from/{from}/to/{to}")
    public CurrencyExchange receiveExchangeValue(@PathVariable String from, @PathVariable String to) {
        Optional<CurrencyExchange> currExchangeOpt = exchangeRepository.findByFromAndTo(from, to);

        if (!currExchangeOpt.isPresent()) {
            throw new RuntimeException("Not found");
        }

        CurrencyExchange currExchange = currExchangeOpt.get();

        final String port = environment.getProperty("local.server.port");
        currExchange.setEnvironment(port);

        return currExchange;
    }

}
