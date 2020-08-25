package com.lorbush.trx.service;

import com.lorbush.trx.entities.Currency;
import com.lorbush.trx.entities.Account;
import com.lorbush.trx.exceptions.ErrorMessages;
import com.lorbush.trx.exceptions.CustomException;
import com.lorbush.trx.repository.CurrencyRepository;
import com.lorbush.trx.repository.TransactionRepository;
import com.lorbush.trx.repository.AccountRepository;
import com.lorbush.trx.helper.Helper;
import org.hibernate.ObjectNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Service for managing accounts
 *
 */
@Validated
@PropertySource("classpath:application.properties")
@Service
class AccountServiceImpl implements AccountService {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private Helper inputParametersValidator;

    @Value("${db.updated_by}")
    private String updatedBy;

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    /**
     *
     * @return the list of all accounts
     * @throws CustomException when not possible to obtain the list of accounts
     */
    @Transactional(rollbackFor = CustomException.class)
    @Override
    public List<Account> findAll() throws CustomException {
        logger.debug("Call AccountServiceImpl.findAll");
        return accountRepository.findAllByOrderByIdAsc();
    }

    /**
     *
     * @param id
     * @return the account found by Id
     * @throws CustomException when not possible to obtain the account
     */
    @Transactional(rollbackFor = CustomException.class)
    @Override
    public Account findById(@NotNull Integer id) throws CustomException {
        logger.debug("Call AccountServiceImpl.findById");
        Optional<Account> optionalAccount = accountRepository.findById(id);
        inputParametersValidator.conditionIsTrue(optionalAccount.isPresent(),
                String.format(ErrorMessages.NO_ACCOUNT_FOUND, id), HttpStatus.BAD_REQUEST.value());
        return optionalAccount.get();
    }

    /**
     *
     * @param userId
     * @return the list af accounts found by user Id
     * @throws CustomException when not possible to obtain the list of accounts
     */
    @Transactional(rollbackFor = CustomException.class)
    @Override
    public List<Account> findByUserId(@NotBlank String userId) throws CustomException {
        logger.debug("Call AccountServiceImpl.findByUserId");
        return accountRepository.findByUserId(userId);
    }

    /**
     * Creates account based on currency.
     * @param userId valid currency id
     * @param currencyName valid currency name
     * @return created account
     * @throws CustomException when not possible to create the account
     */
    @Transactional(rollbackFor = CustomException.class)
    @Override
    public Account createAccount(@NotBlank String id, @NotBlank String userId, @NotBlank String currencyName) throws CustomException {
        logger.debug("Call AccountServiceImpl.createAccount");
        try {
            Currency currency = currencyRepository.findByName(currencyName);
            String error = String.format(ErrorMessages.NO_CURRENCY_PRESENT,currencyName);
            inputParametersValidator.conditionIsTrue(currency != null,error,HttpStatus.BAD_REQUEST.value());
            return accountRepository.save(new Account(id, userId, currency, new BigDecimal(0), updatedBy));
        } catch (ObjectNotFoundException e){
            throw new CustomException(String.format(ErrorMessages.NO_CURRENCY_PRESENT,currencyName),HttpStatus.BAD_REQUEST.value());
        }
    }

    /**
     * Updates account balance, after checking credits in the account balance.
     * When not enough credits throws AccountException
     * When isCredit true, takes amount and adds it to account balance.
     * When isCredit false, takes amount and subtracts it from account balance.
     *
     * @param account
     * @param amount
     * @param isCredit
     * @return updated account
     * @throws CustomException when not possible to update the account balance.
     */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE, rollbackFor = CustomException.class)
    @Override
    public Account updateAccountAmount(@NotNull Account account, @NotBlank String amount, @NotNull Boolean isCredit) throws CustomException {
        logger.debug("Call AccountServiceImpl.updateAccountAmount");
        try {
            BigDecimal transactionAmount = (isCredit) ? new BigDecimal(amount).abs() : new BigDecimal(amount).abs().negate();

            //Check if there are enough credits on account in order to perform a debit transaction
            Boolean condition = (isCredit || (account.getBalance().compareTo(transactionAmount.abs()) >= 0) );
            inputParametersValidator.conditionIsTrue(condition, String.format(ErrorMessages.NO_ENOUGH_FUNDS,account.getId(),amount),HttpStatus.BAD_REQUEST.value());

            account.setBalance(account.getBalance().add(transactionAmount));
            account.setUpdatedBy(updatedBy);
            account.setUpdatedOn(new Date());

            return accountRepository.save(account);
        }catch (NumberFormatException e){
            String error = String.format(ErrorMessages.NUMBER_FORMAT_MISMATCH,amount);
            throw new CustomException(error, HttpStatus.BAD_REQUEST.value());
        }
    }

}
