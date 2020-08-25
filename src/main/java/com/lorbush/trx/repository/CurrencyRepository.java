package com.lorbush.trx.repository;

import com.lorbush.trx.entities.Currency;
import com.lorbush.trx.exceptions.CustomException;
import org.springframework.data.jpa.repository.JpaRepository;
import javax.transaction.Transactional;

/**
 * Currency JPA repository
 *
 */
@Transactional(rollbackOn = CustomException.class)
public interface CurrencyRepository extends JpaRepository<Currency, Integer> {
	Currency findByName(String name);
}