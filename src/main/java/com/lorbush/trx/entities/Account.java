package com.lorbush.trx.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
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
@Getter
@Setter
public class Account {

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotNull(message = "iban must be provided")
	@Column(name = "iban")
	private String iban;

	@NotNull(message = "Account user must be provided")
	@ManyToOne(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
	@JoinColumn(name = "user_id")
	private User user;

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

	@OneToMany(mappedBy = "account")
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Transaction> transactions;

	public Account() {
	}

	public Account(String iban, User user, Currency currency, BigDecimal balance) {
		this.iban = iban;
		this.user = user;
		this.balance = balance;
		this.currency = currency;
		this.updatedOn = new Date();
	}

	public Account(String iban, User user, Currency currency, BigDecimal balance, String updatedBy) {
		this(iban, user, currency, balance);
		this.updatedBy = updatedBy;
	}

}
