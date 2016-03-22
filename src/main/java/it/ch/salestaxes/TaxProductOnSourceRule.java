package it.ch.salestaxes;

import java.util.*;

public class TaxProductOnSourceRule implements TaxLevyRule {

	private List<ProductSource> appliableSources = Collections.emptyList();

	public TaxProductOnSourceRule(ProductSource... sources) {
		this.appliableSources = Arrays.asList(sources);
	}

	@Override
	public boolean applicable(Product product) {
		return appliableSources.contains(product.getSource());
	}

}
