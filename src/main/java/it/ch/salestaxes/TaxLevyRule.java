package it.ch.salestaxes;

public interface TaxLevyRule {
	
	boolean applicable(Product product);
	
}
