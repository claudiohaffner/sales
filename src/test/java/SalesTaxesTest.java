import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.junit.Test;

import it.ch.salestaxes.ClientReceipt;
import it.ch.salestaxes.IReceiptLayout;
import it.ch.salestaxes.Order;
import it.ch.salestaxes.Price;
import it.ch.salestaxes.Product;
import it.ch.salestaxes.ProductSource;
import it.ch.salestaxes.ProductType;
import it.ch.salestaxes.Tax;
import it.ch.salestaxes.TaxPolicy;
import it.ch.salestaxes.TaxProductOnSourceRule;
import it.ch.salestaxes.TaxProductOnTypeRule;
import it.ch.salestaxes.TaxRoundingRule;
import it.ch.salestaxes.TaxType;
import junit.extensions.TestSetup;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class SalesTaxesTest extends TestCase {
	
	private static Logger logger = Logger.getRootLogger();

	private static Currency 	  currencyForTest;	
	private static IReceiptLayout receiptLayout;
	
	public static TestSetup suite() {
		TestSetup setup = new TestSetup(new TestSuite(SalesTaxesTest.class)) {
			protected void setUp() throws Exception {
				logger.info("SetUp configuration for this test suite");
				
				currencyForTest = Currency.getInstance(Locale.US);
				
				// creating and applying taxes on products
				TaxProductOnTypeRule applyOnType = new TaxProductOnTypeRule(ProductType.BASIC);
				Tax basicTax = new Tax(TaxType.BASIC_SALES, new BigDecimal("0.10"), applyOnType);
				TaxProductOnSourceRule applyOnSource = new TaxProductOnSourceRule(ProductSource.IMPORTED);
				Tax importTax = new Tax(TaxType.IMPORT_DUTY, new BigDecimal("0.05"), applyOnSource);

				// creating taxing policy and applying taxes in this test suite
				TaxPolicy.getInstance().setRoundingScale(new BigDecimal("0.05"));
				TaxPolicy.getInstance().setTaxRoundingRule(TaxRoundingRule.ON_PRODUCT);
				TaxPolicy.getInstance().addTaxToPolicy(basicTax);
				TaxPolicy.getInstance().addTaxToPolicy(importTax);
				
				// defining the layout of the receipt for orders
				receiptLayout = new ClientReceipt();
			}
		};
		return setup;
	}
    
	@Test
	public void testOrder1() throws Exception {
		logger.info("Testing shopping basket 1");
		
		// creating products
		Product book = new Product("book", ProductType.BOOK, ProductSource.LOCAL, Price.instance("12.49", currencyForTest));
		Product cd = new Product("music CD", ProductType.BASIC, ProductSource.LOCAL, Price.instance("14.99", currencyForTest));
		Product chocBar = new Product("chocolate bar", ProductType.FOOD, ProductSource.LOCAL, Price.instance("0.85", currencyForTest));

		// creating order on products
		Order order = new Order(TaxPolicy.getInstance(), currencyForTest);
		order.addProductOrder(book, 1);
		order.addProductOrder(cd, 1);
		order.addProductOrder(chocBar, 1);

		// print the receipt
		order.print(receiptLayout);

		assertTrue(order.getTotal().equals(Price.instance("29.83", currencyForTest)));
	}

	@Test
	public void testOrder2() throws Exception {
		logger.info("Testing shopping basket 2");

		// creating products
		Product box = new Product("imported box of chocolates", ProductType.FOOD, ProductSource.IMPORTED, Price.instance("10.00", currencyForTest));
		Product bottle = new Product("imported bottle of perfume", ProductType.BASIC, ProductSource.IMPORTED, Price.instance("47.50", currencyForTest));

		// creating order
		Order order = new Order(TaxPolicy.getInstance(), currencyForTest);
		order.addProductOrder(box, 1);
		order.addProductOrder(bottle, 1);

		// print the receipt
		order.print(receiptLayout);

		assertTrue(order.getTotal().equals(Price.instance("65.15", currencyForTest)));
	}

	@Test
	public void testOrder3() throws Exception {
		logger.info("Testing shopping basket 3");

		// creating products
		Product impBottle = new Product("imported bottle of perfume", ProductType.BASIC, ProductSource.IMPORTED, Price.instance("27.99", currencyForTest));
		Product bottle = new Product("bottle of perfume", ProductType.BASIC, ProductSource.LOCAL, Price.instance("18.99", currencyForTest));
		Product packetPills = new Product("packet of headache pills", ProductType.MEDICAL, ProductSource.LOCAL, Price.instance("9.75", currencyForTest));
		Product box = new Product("imported box of chocolates", ProductType.FOOD, ProductSource.IMPORTED, Price.instance("11.25", currencyForTest));
		
		// creating order
		Order order = new Order(TaxPolicy.getInstance(), currencyForTest);
		order.addProductOrder(impBottle, 1);
		order.addProductOrder(bottle, 1);
		order.addProductOrder(packetPills, 1);
		order.addProductOrder(box, 1);

		// print the receipt
		order.print(receiptLayout);

		assertTrue(order.getTotal().equals(Price.instance("74.68", currencyForTest)));
	}

	@Test
	public void testOrder4() throws Exception {
		logger.info("Testing shopping basket 4");

		// creating products
		Product localPills = new Product("local pills", ProductType.MEDICAL, ProductSource.LOCAL, Price.instance("19.99", currencyForTest));
		Product impPills = new Product("imported pills", ProductType.MEDICAL, ProductSource.IMPORTED, Price.instance("19.99", currencyForTest));
		
		// creating order
		Order order = new Order(TaxPolicy.getInstance(), currencyForTest);
		order.addProductOrder(localPills, 1);
		order.addProductOrder(impPills, 1);

		// print the receipt
		order.print(receiptLayout);

		assertTrue(order.getTotal().equals(Price.instance("40.98", currencyForTest)));
	}

	@Test
	public void testQuantity() throws Exception {
		logger.info("Testing shopping basket on quantity >1");

		// creating products
		Product impJewel = new Product("imported jewel", ProductType.BASIC, ProductSource.IMPORTED, Price.instance("27.99", currencyForTest));
		
		// creating order
		Order order = new Order(TaxPolicy.getInstance(), currencyForTest);
		order.addProductOrder(impJewel, 3);

		// print the receipt
		order.print(receiptLayout);

		assertTrue(order.getTotal().equals(Price.instance("96.57", currencyForTest)));
	}

	@Test
	public void testDifferentRoundingPolicies() throws Exception {
		logger.info("Testing differences between taxing rounding policy on the total order quantity or on the single product");

		// creating products
		Product impJewel = new Product("imported jewel", ProductType.BASIC, ProductSource.IMPORTED, Price.instance("27.99", currencyForTest));

		// creating order1 with tax rounding rule on product
		TaxPolicy.getInstance().setTaxRoundingRule(TaxRoundingRule.ON_PRODUCT);
		Order order1 = new Order(TaxPolicy.getInstance(), currencyForTest);
		order1.addProductOrder(impJewel, 3000);

		// print the receipt
		order1.print(receiptLayout);
		
		// creating order2 with tax rounding rule on order
		TaxPolicy.getInstance().setTaxRoundingRule(TaxRoundingRule.ON_ORDER);
		Order order2 = new Order(TaxPolicy.getInstance(), currencyForTest);
		order2.addProductOrder(impJewel, 3000);

		// print the receipt
		order2.print(receiptLayout);

		assertFalse(order1.getTotal().equals(order2.getTotal()));
	}
}