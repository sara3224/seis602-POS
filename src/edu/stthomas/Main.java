package edu.stthomas;

import edu.stthomas.enums.Shift;
import edu.stthomas.model.Cashier;
import edu.stthomas.model.Register;
import edu.stthomas.model.SalesLineItem;
import edu.stthomas.service.PointOfSale;
import edu.stthomas.service.ReturnAndCancellationService;

import java.util.List;

public class Main {
    public static void main(String args[]) {
        //1.	Allow the cashier to start a new sale and allow add/remove items to a new sale
        Cashier cashier = new Cashier(1001);
        Register register = new Register(1);
        PointOfSale pos = new PointOfSale(cashier, Shift.DAY, register);
        pos.addItem(1, 2);
        pos.addItem(2, 2);
        pos.removeItem(2);
        pos.addItem(3,5);
        //2.	Once all items are added to the sale the cashier will request for cash to finalize the sale.
        pos.finalizeSale();

        cashier = new Cashier(1002);
        register = new Register(2);
        pos = new PointOfSale(cashier, Shift.NIGHT, register);
        pos.addItem(1, 1);
        pos.addItem(2, 4);
        pos.removeItem(1);
        pos.addItem(3,3);
        pos.finalizeSale();

        //3.	The system will keep track of the amount of sales ($) at each register for each cashier
        salesDetails();

        //5.	For returns - Support cancellation of the entire sale as well as return of an individual item.
        ReturnAndCancellationService returnAndCancellationService = new ReturnAndCancellationService();
        returnAndCancellationService.cancelSale(pos.getSalesId());
        salesDetails();
    }

    private static void salesDetails() {
        List<PointOfSale> sales = PointOfSale.getSales();
        System.out.println("Sales report");
        for(PointOfSale sale: sales) {
            System.out.println("cashier id: " +sale.getCashier().getId()+ " level: "+sale.getCashier().getLevel()+ " Register: "
                    +sale.getRegister().getRegisterId() + " sales amt: " + sale.getSalesAmt() + " sales time: " +sale.getSalesTime()
            +" sales id: "+sale.getSalesId() + "refunded: "+ sale.isRefunded());

            List<SalesLineItem> salesLineItems = sale.getSalesLineItems();
                for(SalesLineItem lineItem: salesLineItems) {
                    //4.	Registers will record the register number, the user (cashier), the dates and times of sale, sale items, and the amount of sales.
                    System.out.println("item id:"+lineItem.getItemId()+" quantity:"+lineItem.getQuantity() + " amt: "+lineItem.getLineItemAmt());
                }
                System.out.println();
        }
    }
}
