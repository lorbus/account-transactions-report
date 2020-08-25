package com.lorbush.trx.entities;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Account entity.
 *
 */
@Entity
@Table(name = "account")
@EntityListeners(AuditingEntityListener.class)
public class Account {

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotNull(message = "iban must be provided")
	@Column(name = "iban")
	private String iban;

	@NotNull(message = "User Id must be provided")
	@Column(name = "user_id")
	private String userId;

	@Min(0)
	@Column(name = "balance", nullable = false)
	@NotNull(message = "Account balance must be provided")
	private BigDecimal balance;

	@NotNull(message = "Account currency must be provided")
	@ManyToOne
	@JoinColumn(name = "currency_id")
	private Currency currency;

	@Column(name = "updated_on")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedOn;

	@Column(name = "updated_by")
	private String updatedBy;

	@OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
	private List<Transaction> transactions;

	public Account() {
	}

	public Account(String iban, String userId, Currency currency, BigDecimal balance) {
		this.iban = iban;
		this.userId = userId;
		this.balance = balance;
		this.currency = currency;
		this.updatedOn = new Date();
	}

	public Account(String iban, String userId, Currency currency, BigDecimal balance, String updatedBy) {
		this(iban, userId, currency, balance);
		this.updatedBy = updatedBy;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getIBan() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
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

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
