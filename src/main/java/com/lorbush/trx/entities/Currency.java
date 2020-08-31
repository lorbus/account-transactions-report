package com.lorbush.trx.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;
import java.util.Date;

/**
 * Currency entity.
 *
 */
@Entity
@Table(name = "currency")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Currency {

	@Id
	@Column(name = "id", nullable = false)
	private String id;

	@Column(name = "name")
	private String name;

	@Column(name = "updated_on")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedOn;

	@Column(name = "updated_by")
	private String updatedBy;

	public Currency() {
	}

	public Currency(String id, String name, String updatedBy) {
		this.id = id;
		this.name = name;
		this.updatedOn = new Date();
		this.updatedBy = updatedBy;
	}

}
