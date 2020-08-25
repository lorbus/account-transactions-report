package com.lorbush.trx.repository;

import com.lorbush.trx.entities.TransactionType;
import com.lorbush.trx.exceptions.CustomException;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

/**
 * Transaction type JPA repository
 *
 */
@Transactional(rollbackOn = CustomException.class)
public interface TransactionTypeRepository extends JpaRepository<TransactionType, String> {
}
