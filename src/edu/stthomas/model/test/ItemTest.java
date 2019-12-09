package edu.stthomas.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class ItemTest {
	Item item = new Item(1,"apple",10,0.01,0.5,20,100,50,0);
	

	@Test
	public void testGetItemId() {
		int a=item.getItemId();
		assertEquals(a,1);
	}

	@Test
	public void testGetName() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetOnhands() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetPrice() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetTax() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetThreshold() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetSupplierId() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetReorderQty() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetPending() {
		fail("Not yet implemented");
	}

}
