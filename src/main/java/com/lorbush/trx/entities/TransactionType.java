package com.lorbush.trx.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;
import java.util.Date;

/**
 * Transaction type entity.
 * <p>
 * Type can be C - Credit or D - Debit
 * </p>
 *
 */
@Entity
@Table(name = "transaction_type")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class TransactionType {

	@Id
	@Column(name = "id", nullable = false, unique = true)
	private String id;

	@Column(name = "description")
	private String description;

	@Column(name = "updated_on")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedOn;

	@Column(name = "updated_by")
	private String updatedBy;

	public TransactionType() {
	}

	public TransactionType(String id, String description, String updatedBy) {
		this.id = id;
		this.description = description;
		this.updatedOn = new Date();
		this.updatedBy = updatedBy;
	}


}
