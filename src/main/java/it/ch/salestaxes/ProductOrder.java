package it.ch.salestaxes;

public class ProductOrder {

	private Product product;
	private int quantity;
	private Price taxPrice;

	public ProductOrder(Product product, int quantity) {
		this.product = product;
		this.quantity = quantity;
	}

	public Product getProduct() {
		return product;
	}

	public int getQuantity() {
		return quantity;
	}

	public Price getTaxPrice() {
		return taxPrice;
	}

	public void setTaxPrice(Price taxPrice) {
		this.taxPrice = taxPrice;
	}

	public Price getOrderPrice() {
		return this.product.getPrice().multiply(quantity);
	}
}
