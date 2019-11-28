package edu.stthomas.service;

import edu.stthomas.enums.Shift;
import edu.stthomas.model.ReturnRecord;
import edu.stthomas.model.SalesRecord;
import edu.stthomas.repo.SalesRepo;

import java.util.HashMap;
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
public class PointOfReturn extends AbstractPointOfAction {
    private String reason;
    private ReturnRecord returnRecord;

    public PointOfReturn(int saledId, int cashierId, Shift shift, int registerId, String  reason) {
        super(cashierId, shift, registerId, saledId);
        this.reason = reason;
    }

    public void cancelAll(){
        salesRecord.getSalesLineItems().stream().forEach(it-> itemsAndQuantity.put(it.getItemId(), it.getQuantity()));
    }

    //call the pricing service to get cost of each item and calculate total
    //return details of refund with item, qty for line item and total refund amt
    public ReturnRecord complete() {
          returnRecord = new ReturnRecord(itemsAndQuantity, saleId, cashierId, shift, registerId, reason);
//        try {
            int id = returnRecord.save(returnRecord);
            returnRecord =  returnRecord.getRecord(id);
            return returnRecord;
//        } catch (Exception e) { //validation refund qty more than sales qty or item id not in sales
//
//        }
    }

    @Override
    public void recordPrint() {
        System.out.println("return id: "+returnRecord.getReturnId() + "cashier id: " +returnRecord.getCashier().getId()+ " shift: "+returnRecord.getShift()+" level: "+returnRecord.getCashier().getLevel()+ " Register: "
                +returnRecord.getRegister().getRegisterId() + " sales amt: " + returnRecord.getTotalRefundAmt() + " sales tax: " +returnRecord.getTotalTaxAmt()
                +" total amt: " +returnRecord.getTotalAmt() +" return time: " +returnRecord.getRefundTime());
    }

}
