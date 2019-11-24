package edu.stthomas;

import edu.stthomas.enums.Shift;
import edu.stthomas.model.SalesLineItem;
import edu.stthomas.model.SalesRecord;
import edu.stthomas.repo.SalesRepo;
import edu.stthomas.service.PointOfSale;

import java.util.Collection;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        //1.	Allow the cashier to start a new sale and allow add/remove items to a new sale
        PointOfSale pos = new PointOfSale(1001, Shift.DAY, 1);
        pos.addItem(1, 2);
        pos.addItem(2, 2);
        pos.removeItem(2);
        pos.addItem(3,5);
        pos.addItem(3,1);//correction in the sale quantity
        //2.	Once all items are added to the sale the cashier will request for cash to finalize the sale.
        pos.finalizeSale();

        pos = new PointOfSale(1002, Shift.NIGHT, 2);
        pos.addItem(1, 1);
        pos.addItem(2, 4);
        pos.removeItem(1);
        pos.addItem(3,3);
        pos.finalizeSale();

        //3.	The system will keep track of the amount of sales ($) at each register for each cashier
        salesDetails();
    }

    private static void salesDetails() {
        Collection<SalesRecord> sales = SalesRepo.getSales();
        System.out.println("Sales report");
        for(SalesRecord sale: sales) {

            System.out.println("cashier id: " +sale.getCashier().getId()+ " shift: "+sale.getShift()+" level: "+sale.getCashier().getLevel()+ " Register: "
                    +sale.getRegister().getRegisterId() + " sales amt: " + sale.getTotalSalesAmt() + " sales tax: " +sale.getTotalTaxAmt()
                    +" total amt: " +sale.getTotalAmt()
                    +" sales time: " +sale.getSalesTime() +" sales id: "+sale.getSalesId());

            List<SalesLineItem> salesLineItems = sale.getSalesLineItems();
                for(SalesLineItem lineItem: salesLineItems) {
                    //4.	Registers will record the register number, the user (cashier), the dates and times of sale, sale items, and the amount of sales.
                    System.out.println("item id:"+lineItem.getItemId()+" quantity:"+lineItem.getQuantity() + " price: "+lineItem.getPrice()
                    +" tax:" +lineItem.getTax() +" sale amt: "+lineItem.getLineItemSale() +" sale tax: "+lineItem.getLineItemTax()
                    +" total amt: "+lineItem.getLineItemAmt());
                }
                System.out.println();
        }
    }
}
