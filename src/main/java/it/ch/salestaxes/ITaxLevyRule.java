package it.ch.salestaxes;

public interface ITaxLevyRule {
	
	boolean applicable(Product product);
	
}
