package edu.stthomas.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class ItemTest {
	Item item = new Item(1,"apple",10,1.00,0.05,20,100,50,0);
	

	@Test
	public void testGetItemId() {
		int a=item.getItemId();
		assertEquals(a,1);
	}

	@Test
	public void testGetName() {
		String a=item.getName();
		assertEquals(a,"apple");
	}

	@Test
	public void testGetOnhands() {
		int a=item.getOnhands();
		assertEquals(a,10);
	}

	@Test
	public void testGetPrice() {
		double a=item.getPrice();
		assertEquals(a,1.00,0.01);
	}

	@Test
	public void testGetTax() {
		double a=item.getTax();
		assertEquals(a,0.05,0.01);
	}

	@Test
	public void testGetThreshold() {
		int a=item.getThreshold();
		assertEquals(a,20);
	}

	@Test
	public void testGetSupplierId() {
		int a=item.getSupplierId();
		assertEquals(a,100);
	}

	@Test
	public void testGetReorderQty() {
		int a=item.getReorderQty();
		assertEquals(a,50);
	}

	@Test
	public void testGetPending() {
		int a=item.getPending();
		assertEquals(a,0);
	}

}
