package edu.stthomas.service;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.stthomas.enums.Shift;

public class PointOfSaleTest {

	@Test
	public void test1() {
		PointOfSale pos = new PointOfSale("1001", Shift.DAY, "1");
		pos.addItem("1", 2);
	    pos.addItem("2", 3);
		int a=pos.getItemsAndQuantity().get(1);
		int b=pos.getItemsAndQuantity().get(2);
	    assertEquals(a,2);
	    assertEquals(b,3);
	}

	
	@Test
	public void test2() {
		PointOfSale pos = new PointOfSale("1001", Shift.DAY, "1");
		pos.addItem("1", 2);
	    pos.addItem("2", 3);
	    pos.removeItem("2");
		int a=pos.getItemsAndQuantity().get(1);
	    assertEquals(a,2);
	}
	
	
	
}
