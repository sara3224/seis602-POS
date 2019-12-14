package edu.stthomas.service;

import edu.stthomas.enums.Shift;
import edu.stthomas.repo.InventoryRepo;
import org.junit.Assert;
import org.junit.Test;

public class PointOfSaleTest {

	@Test
	public void test1() {
		PointOfSale pos = new PointOfSale("1001", Shift.DAY, "1");

		InventoryRepo.removeItem("foo");
		Assert.assertEquals("Item: foo does not exist...Please enter valid item",pos.addItem("foo", 3));
		InventoryRepo.addItem("foo","",10,1,0,1,"1",10);
		Assert.assertEquals("",pos.addItem("foo", 3));

		InventoryRepo.removeItem("bar");
		Assert.assertEquals("Item: bar does not exist...Please enter valid item",pos.addItem("bar", 3));
		InventoryRepo.addItem("bar","",10,1,0,1,"1",10);
		Assert.assertNotEquals("Item: bar does not exist...Please enter valid item",pos.addItem("bar", 3));

		Assert.assertEquals(2, pos.getItemsAndQuantity().size());

		InventoryRepo.removeItem("foo");
		InventoryRepo.removeItem("bar");
	}
}
