package it.ch.salestaxes;

import java.util.*;

public class TaxProductOnSourceRule implements ITaxLevyRule {

	private List<ProductSource> listApplicableSources = Collections.emptyList();

	public TaxProductOnSourceRule(ProductSource... sources) {
		this.listApplicableSources = Arrays.asList(sources);
	}

	@Override
	public boolean applicable(Product product) {
		return listApplicableSources.contains(product.getSource());
	}

}
