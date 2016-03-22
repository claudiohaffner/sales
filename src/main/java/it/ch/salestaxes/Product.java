package it.ch.salestaxes;
public class Product {
	
	private ProductType type;
	private ProductSource source; // imported, local
	private String name; // music cd, chocolate bar, ...
	private Price price; // amount + currency

	public Product(String name, ProductType type, ProductSource source, Price price) {
		this.type = type;
		this.source = source;
		this.name = name;
		this.price = price;
	}

	public ProductType getType() {
		return type;
	}

	public void setType(ProductType type) {
		this.type = type;
	}

	public ProductSource getSource() {
		return source;
	}

	public void setSource(ProductSource source) {
		this.source = source;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Price getPrice() {
		return price;
	}

	public void setPrice(Price price) {
		this.price = price;
	}
	
}
