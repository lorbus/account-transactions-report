package com.lorbush.trx.repository;

import com.lorbush.trx.entities.Transaction;
import com.lorbush.trx.entities.Account;
import com.lorbush.trx.exceptions.CustomException;
import org.springframework.data.jpa.repository.JpaRepository;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Transaction JPA repository
 * 
 */
@Transactional(rollbackOn = CustomException.class)
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
	List<Transaction> findByAccount(Account account);

}
