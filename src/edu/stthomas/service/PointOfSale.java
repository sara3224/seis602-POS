package edu.stthomas.service;

import edu.stthomas.enums.Shift;
import edu.stthomas.model.SalesLineItem;
import edu.stthomas.model.SalesTransaction;

import java.util.List;
import java.util.Map;

/**
 * Responsible for activities for the actual sale at the register:
 * Input: Cashier Id
 * captures: itemsAndQuantity and quantity
 * processes itemsAndQuantity and quantity to calculate total payment amount
 * updates inventory of itemsAndQuantity on sale
 * trigger replenishment on each sale to order new itemsAndQuantity
 *
 */
public class PointOfSale extends AbstractPointOfAction {
    public PointOfSale(int cashierId, Shift shift, int registerId) {
        super(cashierId, shift, registerId);
    }

    @Override
    public Map<Integer, Integer> getItemsAndQuantity() {
        return itemsAndQuantity;
    }

    //call the pricing service to get cost of each item and calculate total
    public SalesTransaction complete() {
        salesRecord = new SalesTransaction(itemsAndQuantity, cashierId, shift, registerId);
        int saleId = salesRecord.save(salesRecord);
        salesRecord =  salesRecord.getRecord(saleId);
        recordPrint();
        return salesRecord;
    }

    @Override
    public String toString() {
        recordPrint();
        return "";
    }

    public void recordPrint() {
        System.out.println("sales id: "+salesRecord.getId() + " cashier id: " +salesRecord.getCashier().getId()+ " shift: "+salesRecord.getShift()+" level: "+salesRecord.getCashier().getLevel()+ " Register: "
                +salesRecord.getRegister().getRegisterId() + " sales amt: " + salesRecord.getTotalAmtBeforeTax() + " sales tax: " +salesRecord.getTotalTaxAmt()
                +" total amt: " +salesRecord.getTotalAmt() +" sales time: " +salesRecord.getTransactionTime());
        List<SalesLineItem> salesLineItems = salesRecord.getSalesLineItems();
        for(SalesLineItem lineItem: salesLineItems) {
            //4.	Registers will record the register number, the user (cashier), the dates and times of sale, sale items, and the amount of sales.
            System.out.println("item id:"+lineItem.getItemId()+" quantity:"+lineItem.getQuantity() + " price: "+lineItem.getPrice()
                    +" tax:" +lineItem.getTax() +" sale amt: "+lineItem.getLineItemAntBeforeTax() +" sale tax: "+lineItem.getLineItemTax()
                    +" total amt: "+lineItem.getLineItemAmt());
        }
        System.out.println();
    }
}
