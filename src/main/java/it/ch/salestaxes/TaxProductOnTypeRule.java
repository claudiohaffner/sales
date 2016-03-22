package it.ch.salestaxes;

import java.util.*;

public class TaxProductOnTypeRule implements TaxLevyRule {

	private List<ProductType> applicableTypes = Collections.emptyList();

	public TaxProductOnTypeRule(ProductType... types) {
		this.applicableTypes = Arrays.asList(types);
	}

	@Override
	public boolean applicable(Product product) {
		return applicableTypes.contains(product.getType());
	}

}
