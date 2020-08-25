package com.lorbush.trx.view.model;

import com.lorbush.trx.entities.Transaction;

import java.math.BigDecimal;
import java.util.List;

public class TransactionListModel {

	private List<Transaction> transactions;

	private BigDecimal poundTotalAmount;

	public TransactionListModel() {
	}

	public TransactionListModel(List<Transaction> transactions, BigDecimal poundTotalAmount) {
		this.transactions = transactions;
		this.poundTotalAmount = poundTotalAmount;
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}

	public BigDecimal getPoundTotalAmount() {
		return poundTotalAmount;
	}

	public void setPoundTotalAmount(BigDecimal poundTotalAmount) {
		this.poundTotalAmount = poundTotalAmount;
	}
}
