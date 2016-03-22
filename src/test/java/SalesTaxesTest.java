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
import junit.framework.TestCase;

public class SalesTaxesTest extends TestCase {
	
	private static Logger logger = Logger.getRootLogger();

	private static final Currency 			CURRENCY 			= Currency.getInstance(Locale.ITALY);
	private static final BigDecimal 		TAX_ROUNDING_SCALE	= new BigDecimal("0.05");
	private static final TaxRoundingRule	TAX_ROUNDING_RULE	= TaxRoundingRule.ON_PRODUCT;
	
	private TaxPolicy 		taxPolicy;
	private IReceiptLayout 	receiptLayout;

	@Override
	protected void setUp() throws Exception {
		
		// creating and applying taxes on products
		TaxProductOnTypeRule applyOnType = new TaxProductOnTypeRule(ProductType.BASIC);
		Tax basicTax = new Tax(TaxType.BASIC_SALES, new BigDecimal("0.10"), applyOnType);
		TaxProductOnSourceRule applyOnSource = new TaxProductOnSourceRule(ProductSource.IMPORTED);
		Tax importTax = new Tax(TaxType.IMPORT_DUTY, new BigDecimal("0.05"), applyOnSource);
		
		// creating taxing policy and applying taxes
		taxPolicy = new TaxPolicy(TAX_ROUNDING_SCALE, TAX_ROUNDING_RULE);
		taxPolicy.addTaxToPolicy(basicTax);
		taxPolicy.addTaxToPolicy(importTax);
		
		// define a layout for receipt
		receiptLayout = new ClientReceipt();
		//receiptLayout = new DetailedReceipt();
	}

	@Test
	public void testOrder1() throws Exception {
		logger.info("Testing shopping basket 1");
		
		// creating products
		Product book = new Product("book", ProductType.BOOK, ProductSource.LOCAL, Price.instance("12.49", CURRENCY));
		Product cd = new Product("music CD", ProductType.BASIC, ProductSource.LOCAL, Price.instance("14.99", CURRENCY));
		Product chocBar = new Product("chocolate bar", ProductType.FOOD, ProductSource.LOCAL, Price.instance("0.85", CURRENCY));

		// creating order on products
		Order order = new Order(taxPolicy, CURRENCY);
		order.addProductOrder(book, 1);
		order.addProductOrder(cd, 1);
		order.addProductOrder(chocBar, 1);

		// print the receipt
		order.print(receiptLayout);

		assertTrue(order.getTotal().equals(Price.instance("29.83", CURRENCY)));
	}

	@Test
	public void testOrder2() throws Exception {
		logger.info("Testing shopping basket 2");

		// creating products
		Product box = new Product("imported box of chocolates", ProductType.FOOD, ProductSource.IMPORTED, Price.instance("10.00", CURRENCY));
		Product bottle = new Product("imported bottle of perfume", ProductType.BASIC, ProductSource.IMPORTED, Price.instance("47.50", CURRENCY));

		// creating order
		Order order = new Order(taxPolicy, CURRENCY);
		order.addProductOrder(box, 1);
		order.addProductOrder(bottle, 1);

		// print the receipt
		order.print(receiptLayout);

		assertTrue(order.getTotal().equals(Price.instance("65.15", CURRENCY)));
	}

	@Test
	public void testOrder3() throws Exception {
		logger.info("Testing shopping basket 3");

		// creating products
		Product impBottle = new Product("imported bottle of perfume", ProductType.BASIC, ProductSource.IMPORTED, Price.instance("27.99", CURRENCY));
		Product bottle = new Product("bottle of perfume", ProductType.BASIC, ProductSource.LOCAL, Price.instance("18.99", CURRENCY));
		Product packetPills = new Product("packet of headache pills", ProductType.MEDICAL, ProductSource.LOCAL, Price.instance("9.75", CURRENCY));
		Product box = new Product("imported box of chocolates", ProductType.FOOD, ProductSource.IMPORTED, Price.instance("11.25", CURRENCY));
		
		// creating order
		Order order = new Order(taxPolicy, CURRENCY);
		order.addProductOrder(impBottle, 1);
		order.addProductOrder(bottle, 1);
		order.addProductOrder(packetPills, 1);
		order.addProductOrder(box, 1);

		// print the receipt
		order.print(receiptLayout);

		assertTrue(order.getTotal().equals(Price.instance("74.68", CURRENCY)));
	}

	@Test
	public void testOrder4() throws Exception {
		logger.info("Testing shopping basket 4");

		// creating products
		Product localPills = new Product("local pills", ProductType.MEDICAL, ProductSource.LOCAL, Price.instance("19.99", CURRENCY));
		Product impPills = new Product("imported pills", ProductType.MEDICAL, ProductSource.IMPORTED, Price.instance("19.99", CURRENCY));
		
		// creating order
		Order order = new Order(taxPolicy, CURRENCY);
		order.addProductOrder(localPills, 1);
		order.addProductOrder(impPills, 1);

		// print the receipt
		order.print(receiptLayout);

		assertTrue(order.getTotal().equals(Price.instance("40.98", CURRENCY)));
	}

	@Test
	public void testQuantity() throws Exception {
		logger.info("Testing shopping basket on quantity >1");

		// creating products
		Product impJewel = new Product("imported jewel", ProductType.BASIC, ProductSource.IMPORTED, Price.instance("27.99", CURRENCY));
		
		// creating order
		Order order = new Order(taxPolicy, CURRENCY);
		order.addProductOrder(impJewel, 3);

		// print the receipt
		order.print(receiptLayout);

		assertTrue(order.getTotal().equals(Price.instance("96.57", CURRENCY)));
	}

	@Test
	public void testDifferentRoundingPolicies() throws Exception {
		logger.info("Testing differences between taxing rounding policy on the total order quantity or on the single product");

		// creating products
		Product impJewel = new Product("imported jewel", ProductType.BASIC, ProductSource.IMPORTED, Price.instance("27.99", CURRENCY));

		// creating order1 with tax rounding rule on order
		taxPolicy.setTaxRoundingRule(TaxRoundingRule.ON_ORDER);
		Order order1 = new Order(taxPolicy, CURRENCY);
		order1.addProductOrder(impJewel, 3000);

		// print the receipt
		order1.print(receiptLayout);

		// creating order2 with tax rounding rule on product
		taxPolicy.setTaxRoundingRule(TaxRoundingRule.ON_PRODUCT);
		Order order2 = new Order(taxPolicy, CURRENCY);
		order2.addProductOrder(impJewel, 3000);

		// print the receipt
		order2.print(receiptLayout);

		assertFalse(order1.getTotal().equals(order2.getTotal()));
	}

	@Test
	public void testRoundByPolicy() throws Exception {
		assertTrue(taxPolicy.roundByPolicy(Price.instance("13.50", CURRENCY)).equals(Price.instance("13.50", CURRENCY)));
		assertTrue(taxPolicy.roundByPolicy(Price.instance("13.51", CURRENCY)).equals(Price.instance("13.55", CURRENCY)));
		assertTrue(taxPolicy.roundByPolicy(Price.instance("12.4943", CURRENCY)).equals(Price.instance("12.50", CURRENCY)));
	}
}