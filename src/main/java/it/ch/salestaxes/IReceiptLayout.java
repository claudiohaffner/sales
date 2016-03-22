package it.ch.salestaxes;

public interface IReceiptLayout {
	
	void printHeader();

	void printProduct(ProductOrder entry);

	void printTotProducts(Price subTotal);

	void printTotTaxes(Price taxTotal);

	void printTotal(Price total);

	void printFooter();
}
