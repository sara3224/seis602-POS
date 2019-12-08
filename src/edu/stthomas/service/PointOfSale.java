package edu.stthomas.service;

import edu.stthomas.enums.Shift;
import edu.stthomas.exceptions.POSException;
import edu.stthomas.model.Item;
import edu.stthomas.model.SalesLineItem;
import edu.stthomas.model.SalesTransaction;
import edu.stthomas.repo.InventoryRepo;

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
//    public PointOfSale(int cashierId, Shift shift, int registerId) {
//        super(cashierId, shift, registerId);
//    }

    public PointOfSale(String cashierId, Shift shift, int registerId) {
        super(cashierId, shift, registerId);
    }

    @Override
    public Map<Integer, Integer> getItemsAndQuantity() {
        return itemsAndQuantity;
    }

    //call the pricing service to get cost of each item and calculate total
    public SalesTransaction complete() throws POSException {
        //validate if items are still in inventory
        for(Map.Entry<Integer,Integer> itemQty: getItemsAndQuantity().entrySet()) {
            int itemId = itemQty.getKey();
            int qty = itemQty.getValue();
            Item item = InventoryRepo.getItem(itemId);
            if(item == null) {
                throw new POSException("Item is no longer avaliable. Item: " +item+ " does not exist...Please re-start trasnaction.");
            } else if(qty > item.getOnhands()) {
                throw new POSException("item" +item.getItemId() + " " + item.getName() + " max quantity available is: "+item.getOnhands() +" Please re-start trasnaction.");
            }
        }
        salesRecord = new SalesTransaction(itemsAndQuantity, cashierId, shift, registerId);
//        int saleId = salesRecord.save(salesRecord);
        String saleId = salesRecord.save(salesRecord);
//        salesRecord =  salesRecord.getRecord(saleId);
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
                    +" tax:" +lineItem.getTax() +" sale amt: "+lineItem.getLineItemAmtBeforeTax() +" sale tax: "+lineItem.getLineItemTax()
                    +" total amt: "+lineItem.getLineItemAmt());
        }
        System.out.println();
    }
}
