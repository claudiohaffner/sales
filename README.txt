-------------------
SALES TAXES PROBLEM
-------------------

-----------
Description
-----------
This problem is about taxing product orders and produce a detailed receipt.

A Product is described by a name, a type, a source (local or imported) and a shelf price.
Price is generic and can be applied to every currency (USD, EUR, ...).

A Tax is described by a type, a rate (%) and a generic ITaxLevyRule that simply defines whether a tax is applicable on a product or not. 
In this exercise we have only two kind of rules: TaxProductOnTypeRule for taxes that are applicable on product types and TaxProductOnSourceRule 
for taxes that are applicable on product sources.
A TaxPolicy holds all the defined taxes and the general rules of rounding, in order to calculate the total tax price; TaxPolicy is defined once
we use a singleton to share the rules between the different orders.

The Order defines a collection of ProductOrder which are described by the quantity of a Product that can be ordered by a customer in a shopping 
basket; the user must specify the TaxPolicy and the currency to complete an Order.

The receipt of an Order contains the prices of every ProductOrder, the amount of taxes and the total price of the Order (including taxes).
This implementation provides two examples of receipts: the ClientReceipt (which shows the simple output required by the exercise) and the 
DetailedReceipt (which shows all the details about the taxes applied on every single ProductOrder). In this simple implementation all the types 
of receipt print on System.out, but it's easy to implement other wrappers that write on file or on other output streams.

We also assumed that the user can decide the rounding rule for applying taxes: 

- ON_PRODUCT : taxes amount are rounded up on every single product, so rounding amount increases with quantity.
- ON_ORDER: taxes amount are rounded up on the whole order of a product, so rounding amount is quantity-independent.

All the order examples provided by the exercise have quantity=1, so they give the same result using one rounding rule or the other, but according to 
the requirements ("rounding rules apply to the shelf price") we decided to put ON_PRODUCT rounding by default.


--------------------
Testing the solution
--------------------
The SalesTaxesTest is a JUnit class that includes:

- testOrder 1,2,3: the receipt of the three shopping baskets provided by the exercise
- testOrder4: a test about the difference between local and imported products taxation
- testQuantity: testing a shopping basket with product quantity >1
- testDifferentRoundingPolicies: testing the difference between ON_PRODUCT/ON_ORDER rounding rules on big quantity orders


----------------------
Improving the solution
----------------------
User is free to add further tests, changing for example the tax rounding scale or the taxing rate.
It's also possible to create new taxes or taxing rules simply by implementing the TaxLevyRule interface.
A better solution is to delete hard coded products and to get the input products and taxes from a persistent source (database, xml, ...)