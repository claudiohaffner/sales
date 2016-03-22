package it.ch.salestaxes;

public class ClientReceipt implements IReceiptLayout {

	private static final String LINE = "---------------------------------------------------------";
	private static final String FORMAT = "n.%3s %29s  =   %9s";
	private static final String FORMAT_TOT = "%35s ---> %8s";
	
	@Override
	public void printHeader() {
		System.out.println(LINE);
		System.out.println(String.format(FORMAT, 
				"QTY",
				"PRODUCT_NAME",
				"PRICE"));
	}

	@Override
	public void printProduct(ProductOrder entry) {
		String totOrderPrice;
		try {
			totOrderPrice = entry.getOrderPrice().add(entry.getTaxPrice()).toString();
		} catch (Exception e) {
			totOrderPrice = "ERR";
		}
		System.out.println(String.format(FORMAT,
				entry.getQuantity(),
				entry.getProduct().getName(),
				totOrderPrice
				));
	}

	@Override
	public void printTotProducts(Price subTotal) {
		System.out.println(String.format(FORMAT_TOT, "SUB_TOTAL", subTotal));
	}

	@Override
	public void printTotTaxes(Price taxTotal) {
		System.out.println(String.format(FORMAT_TOT, "TAX_TOTAL", taxTotal));

	}

	@Override
	public void printTotal(Price total) {
		System.out.println(String.format(FORMAT_TOT, "TOTAL", total));
	}

	@Override
	public void printFooter() {
		System.out.println(LINE);
	}

}
