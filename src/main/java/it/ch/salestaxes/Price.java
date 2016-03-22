package it.ch.salestaxes;

import java.math.BigDecimal;
import java.util.Currency;

public class Price {

	private BigDecimal amount;
	private Currency currency;

	public Price(String amountStr, Currency currency) {
		this.amount = new BigDecimal(amountStr);
		this.currency = currency;
	}

	public Price(BigDecimal amount, Currency currency) {
		this.amount = amount;
		this.currency = currency;
	}

	public static Price zero(Currency currency) {
		return Price.instance("0", currency);
	}

	public static Price instance(String amountStr, Currency currency) {
		return new Price(amountStr, currency);
	}

	public static Price instance(BigDecimal amount, Currency currency) {
		return new Price(amount, currency);
	}

	public Price add(Price price) throws CurrencyException {
		if (!this.getCurrency().equals(price.getCurrency()))
			throw new CurrencyException(this.getCurrency()+" "+price.getCurrency()+" are not comparable");
		return Price.instance(this.amount.add(price.getAmount()), this.currency);
	}

	public Price multiply(int quantity) {
		return Price.instance(this.amount.multiply(BigDecimal.valueOf(quantity)), this.currency);
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	@Override
	public String toString() {
		return amount + " " + currency;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Price other = (Price) obj;
		if (amount == null) {
			if (other.amount != null)
				return false;
		} else if (!amount.equals(other.amount))
			return false;
		if (currency == null) {
			if (other.currency != null)
				return false;
		} else if (currency.getNumericCode() != other.currency.getNumericCode())
			return false;
		return true;
	}

}
