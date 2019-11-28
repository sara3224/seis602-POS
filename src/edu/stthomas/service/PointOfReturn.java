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
public class PointOfReturn {
    private int cashierId;
    private Shift shift;
    private int registerId;
    private Map<Integer, Integer> itemsAndQuantity = new HashMap<>();
    private int saleId;
    private String reason;
    private ReturnRecord returnRecord;
    private SalesRecord salesRecord;

    public PointOfReturn(int saledId, int cashierId, Shift shift, int registerId, String  reason) {
        this.cashierId = cashierId;
        this.shift = shift;
        this.registerId = registerId;
        this.saleId = saledId;
        this.reason = reason;
        salesRecord = SalesRepo.getSalesRecord(saledId);
    }

    /**
     * Overrides the quantity for item.
     * @param item_id
     * @param qty
     */
    public void addItem(Integer item_id, int qty) {
        itemsAndQuantity.put(item_id, qty);
    }

    public Map<Integer, Integer> getItemsAndQuantity() {
        return itemsAndQuantity;
    }

    public void removeItem(Integer item_id) {
        getItemsAndQuantity().remove(item_id);
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
//        toString();
//        return returnRecord;
//        salesRecord.save(salesRecord);
    }

    @Override
    public String toString() {
        recordPrint();
        return "";
    }

    private void recordPrint() {
        System.out.println("return id: "+returnRecord.getReturnId() + "cashier id: " +returnRecord.getCashier().getId()+ " shift: "+returnRecord.getShift()+" level: "+returnRecord.getCashier().getLevel()+ " Register: "
                +returnRecord.getRegister().getRegisterId() + " sales amt: " + returnRecord.getTotalRefundAmt() + " sales tax: " +returnRecord.getTotalTaxAmt()
                +" total amt: " +returnRecord.getTotalAmt() +" return time: " +returnRecord.getRefundTime());
    }

}
