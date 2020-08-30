package com.lorbush.trx.entities;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * Transaction entity.
 *
 */
@Entity
@Table(name = "transaction")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Transaction {

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotNull(message = "Transaction typeId must be provided")
	@ManyToOne
	@JoinColumn(name = "type_id")
	private TransactionType type;

	@NotNull(message = "Transaction amount must be provided")
	@Column(name = "amount", nullable = false)
	private BigDecimal amount;

	@NotNull(message = "Transaction account must be provided")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "account_id")
	private Account account;

	@NotNull(message = "Transaction currency must be provided")
	@ManyToOne
	@JoinColumn(name = "currency_id")
	private Currency currency;

	@Column(name = "description")
	String description;

	@Column(name = "updated_on")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedOn;

	@Column(name = "updated_by")
	private String updatedBy;

	public Transaction() {
	}

	public Transaction(TransactionType type, BigDecimal amount, Account account, Currency currency,
			String description) {
		this.type = type;
		this.amount = amount;
		this.account = account;
		this.currency = currency;
		this.description = description;
		this.updatedOn = new Date();
	}

	public Transaction(TransactionType type, BigDecimal amount, Account account, Currency currency, String description,
			String updatedBy) {
		this(type, amount, account, currency, description);
		this.updatedBy = updatedBy;
	}


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public TransactionType getType() {
		return type;
	}

	public void setType(TransactionType type) {
		this.type = type;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}


}
