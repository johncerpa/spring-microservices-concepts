package com.johncerpa.currencyexchangemicroservice;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CurrencyExchangeRepository extends JpaRepository<CurrencyExchange, Long> {

    Optional<CurrencyExchange> findByFromAndTo(String from, String to);

}
