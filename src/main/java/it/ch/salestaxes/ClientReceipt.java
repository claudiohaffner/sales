package it.ch.salestaxes;

public class ClientReceipt implements IReceiptLayout {

	private static final String LINE = "---------------------------------------------------------";
	private static final String FORMAT = "n.%3s %29s  =   %9s";
	private static final String FORMAT_TOT = "%35s ---> %8s";
	
	@Override
	public void header() {
		System.out.println(LINE);
		System.out.println(String.format(FORMAT, 
				"QTY",
				"PRODUCT_NAME",
				"PRICE"));
	}

	@Override
	public void printProduct(ProductOrder entry) {
		System.out.println(String.format(FORMAT,
				entry.getQuantity(),
				entry.getProduct().getName(),
				entry.getOrderPrice().add(entry.getTaxPrice())
				));
	}

	@Override
	public void printTotProducts(Price subTotal) {
		System.out.println(String.format(FORMAT_TOT, "SUB-TOTAL", subTotal));
	}

	@Override
	public void printTotTaxes(Price taxTotal) {
		System.out.println(String.format(FORMAT_TOT, "TAX-TOTAL", taxTotal));

	}

	@Override
	public void printTotal(Price total) {
		System.out.println(String.format(FORMAT_TOT, "TOTAL", total));
	}

	@Override
	public void footer() {
		System.out.println(LINE);
	}

}
