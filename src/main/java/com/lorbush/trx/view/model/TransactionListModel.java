package com.lorbush.trx.view.model;

import com.lorbush.trx.entities.Transaction;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TransactionListModel {

	private List<Transaction> transactions;
	private BigDecimal poundTotalAmount;

}
