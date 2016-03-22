package it.ch.salestaxes;

public interface IReceiptLayout {
	void header();

	void printProduct(ProductOrder entry);

	void printTotProducts(Price subTotal);

	void printTotTaxes(Price taxTotal);

	void printTotal(Price total);

	void footer();
}
