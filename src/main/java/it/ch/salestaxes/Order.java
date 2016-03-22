package it.ch.salestaxes;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

public class Order {

	private final TaxPolicy taxPolicy;
	private final Currency currency;
	private final List<ProductOrder> listProductOrder = new ArrayList<ProductOrder>();

	public Order(TaxPolicy taxPolicy, Currency currency) {
		this.taxPolicy = taxPolicy;
		this.currency = currency;
	}

	public void addProductOrder(Product product, int quantity) {
		ProductOrder productOrder = new ProductOrder(product, quantity);
		Price taxPrice = this.taxPolicy.getTaxPrice(productOrder);
		productOrder.setTaxPrice(taxPrice);
		listProductOrder.add(productOrder);
	}

	public Price getTaxTotal() {
		Price total = Price.zero(this.currency);
		for (ProductOrder entry: listProductOrder) {
			total = total.add(entry.getTaxPrice());
		}
		return total;
	}

	private Price getSubTotal() {
		Price total = Price.zero(this.currency);
		for (ProductOrder entry: listProductOrder) {
			total = total.add(entry.getOrderPrice());
		}
		return total;
	}

	public Price getTotal() {
		return getSubTotal().add(getTaxTotal());
	}

	public void print(IReceiptLayout iSalesReceipt) {
		iSalesReceipt.header();

		for (ProductOrder entry : listProductOrder)
			iSalesReceipt.printProduct(entry);
		
		iSalesReceipt.printTotProducts(getSubTotal());
		iSalesReceipt.printTotTaxes(getTaxTotal());
		iSalesReceipt.printTotal(getTotal());
		iSalesReceipt.footer();
	}
}
