package edu.stthomas.service;

import edu.stthomas.enums.Shift;
import edu.stthomas.model.SalesRecord;

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

    public Map<Integer, Integer> getItemsAndQuantity() {
        return itemsAndQuantity;
    }

    //call the pricing service to get cost of each item and calculate total
    public SalesRecord complete() {
        SalesRecord salesRecord = new SalesRecord(itemsAndQuantity, cashierId, shift, registerId);
        int saleId = salesRecord.save(salesRecord);
        salesRecord =  salesRecord.getRecord(saleId);
        return salesRecord;
    }

    @Override
    public String toString() {
        recordPrint();
        return "";
    }

    public void recordPrint() {
        System.out.println("sales id: "+salesRecord.getId() + "cashier id: " +salesRecord.getCashier().getId()+ " shift: "+salesRecord.getShift()+" level: "+salesRecord.getCashier().getLevel()+ " Register: "
                +salesRecord.getRegister().getRegisterId() + " sales amt: " + salesRecord.getTotalAmtBeforeTax() + " sales tax: " +salesRecord.getTotalTaxAmt()
                +" total amt: " +salesRecord.getTotalAmt() +" sales time: " +salesRecord.getTransactionTime());
    }
}
