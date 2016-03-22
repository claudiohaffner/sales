package it.ch.salestaxes;

import java.math.BigDecimal;

public class Tax {

	private TaxType type;
	private BigDecimal rate;
	private TaxLevyRule taxLevyRule;

	public Tax(TaxType type, BigDecimal rate, TaxLevyRule taxLevyRule) {
		super();
		this.type = type;
		this.rate = rate;
		this.taxLevyRule = taxLevyRule;
	}
	
	public boolean applicable(Product product) {
		return (this.taxLevyRule.applicable(product));
	}

	public TaxType getType() {
		return type;
	}

	public void setType(TaxType type) {
		this.type = type;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}
}
