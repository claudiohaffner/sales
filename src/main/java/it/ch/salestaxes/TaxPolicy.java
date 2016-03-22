package it.ch.salestaxes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

public class TaxPolicy {

	private List<Tax> applicableTaxes;
	private BigDecimal roundingScale;
	private TaxRoundingRule taxRoundingRule;

	public TaxPolicy(List<Tax> applicableTaxes, BigDecimal roundingScale, TaxRoundingRule taxRoundingRule) {
		this.applicableTaxes = applicableTaxes;
		this.roundingScale = roundingScale;
		this.taxRoundingRule = taxRoundingRule;
	}

	public TaxPolicy(BigDecimal roundingScale, TaxRoundingRule taxRoundingRule) {
		this.applicableTaxes = new ArrayList<Tax>();
		this.roundingScale = roundingScale;
		this.taxRoundingRule = taxRoundingRule;
	}

	public void addTaxToPolicy(Tax tax) {
		applicableTaxes.add(tax);
	}

	public Price getTaxPrice(ProductOrder productOrder) {
		Price productPrice = productOrder.getProduct().getPrice();
		Currency productCurrency = productPrice.getCurrency();
		Price totalTax = Price.zero(productCurrency);

		for (Tax tax : this.applicableTaxes) {
			Price taxPrice = Price.zero(productCurrency);
			if (tax.applicable(productOrder.getProduct())) {

				if (taxRoundingRule.equals(TaxRoundingRule.ON_ORDER)) {
					taxPrice = applyTax(productPrice.multiply(productOrder.getQuantity()), tax);
				}
				if (taxRoundingRule.equals(TaxRoundingRule.ON_PRODUCT)) {
					taxPrice = applyTax(productPrice, tax).multiply(productOrder.getQuantity());
				}
			}
			totalTax = totalTax.add(taxPrice);
		}
		return totalTax;
	}

	public Price applyTax(Price price, Tax tax) {
		return roundByPolicy(Price.instance(price.getAmount().multiply(tax.getRate()), price.getCurrency()));
	}

	public Price roundByPolicy(Price price) {
		price.setAmount(round(price.getAmount(), this.roundingScale));
		return price;
	}

	private static BigDecimal round(BigDecimal number, BigDecimal scale) {
		return number.divide(scale).setScale(0, BigDecimal.ROUND_UP).multiply(scale);
	}

	public BigDecimal getRoundingScale() {
		return roundingScale;
	}

	public void setRoundingScale(BigDecimal roundingScale) {
		this.roundingScale = roundingScale;
	}

	public TaxRoundingRule getTaxRoundingRule() {
		return taxRoundingRule;
	}

	public void setTaxRoundingRule(TaxRoundingRule taxRoundingRule) {
		this.taxRoundingRule = taxRoundingRule;
	}

}
