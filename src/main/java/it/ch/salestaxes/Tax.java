package it.ch.salestaxes;

import java.math.BigDecimal;

public class Tax {

	private TaxType type;
	private BigDecimal rate;
	private ITaxLevyRule iTaxLevyRule;

	public Tax(TaxType type, BigDecimal rate, ITaxLevyRule iTaxLevyRule) {
		this.type = type;
		this.rate = rate;
		this.iTaxLevyRule = iTaxLevyRule;
	}
	
	public boolean applicable(Product product) {
		return (this.iTaxLevyRule.applicable(product));
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
