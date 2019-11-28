package edu.stthomas.service;

import edu.stthomas.enums.Shift;
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
public class PointOfSale {
    private int cashierId;
    private Shift shift;
    private int registerId;
    private Map<Integer, Integer> itemsAndQuantity = new HashMap<>();
    SalesRecord sale;

    public PointOfSale(int cashierId, Shift shift, int registerId) {
        this.cashierId = cashierId;
        this.shift = shift;
        this.registerId = registerId;
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
    public SalesRecord complete() {
        SalesRecord salesRecord = new SalesRecord(itemsAndQuantity, cashierId, shift, registerId);
        int saleId = salesRecord.save(salesRecord);
        sale =  salesRecord.getRecord(saleId);
        return sale;
    }

    @Override
    public String toString() {
        recordPrint();
        return "";
    }

    private void recordPrint() {
        System.out.println("sales id: "+sale.getId() + "cashier id: " +sale.getCashier().getId()+ " shift: "+sale.getShift()+" level: "+sale.getCashier().getLevel()+ " Register: "
                +sale.getRegister().getRegisterId() + " sales amt: " + sale.getTotalSalesAmt() + " sales tax: " +sale.getTotalTaxAmt()
                +" total amt: " +sale.getTotalAmt() +" sales time: " +sale.getSalesTime());
    }
}
