package com.lorbush.trx.repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lorbush.trx.entities.Account;
import com.lorbush.trx.exceptions.CustomException;

/**
 * Account JPA repository
 * 
 */
@Transactional(rollbackOn = CustomException.class)
public interface AccountRepository extends JpaRepository<Account, Integer> {
	List<Account> findAllByOrderByIdAsc();
	List<Account> findByUserId(Long userId);
}