package it.ch.salestaxes;

import java.util.*;

public class TaxProductOnTypeRule implements ITaxLevyRule {

	private List<ProductType> listApplicableTypes = Collections.emptyList();

	public TaxProductOnTypeRule(ProductType... types) {
		this.listApplicableTypes = Arrays.asList(types);
	}

	@Override
	public boolean applicable(Product product) {
		return listApplicableTypes.contains(product.getType());
	}

}
