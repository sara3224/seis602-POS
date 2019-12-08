package edu.stthomas.repo;

import edu.stthomas.model.Item;
import edu.stthomas.model.SalesLineItem;
import edu.stthomas.model.SalesTransaction;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class SalesRepo {
    //TODO: stores sales in in file
    //TODO: report x
    //TODO report y
//    public static final Map<Integer, SalesTransaction> sales = new HashMap<>();
    public static final Map<String, SalesTransaction> sales = new HashMap<>();
    private static File salesFile = new File("./data/" + "sales.tsv");
    private static File salesItemsFile = new File("./data/" + "salesLineItems.tsv");

//    private static String salesId = UUID.randomUUID().toString().substring(0,8);

//    public int save(SalesTransaction salesRecord) {
    public String save(SalesTransaction salesRecord) {
        String salesId =  UUID.randomUUID().toString().substring(0,8);
//        sales.put(++salesId, salesRecord);
        sales.put(salesId, salesRecord);
        salesRecord.setId(salesId);
        record(salesRecord);
        updateInventory(salesRecord);
        return salesId;
    }

    private void record(SalesTransaction salesRecord) {
//        String salesSave = salesRecord.getId()+ "\t" + salesRecord.getTotalAmtBeforeTax() + "\t" + salesRecord.getTotalTaxAmt()
//                + "\t" + salesRecord.getTotalAmt();

        String salesSave = salesRecord.getId() + "\t" +salesRecord.getCashier().getId() + "\t" +salesRecord.getShift() + "\t" +salesRecord.getCashier().getLevel()+ "\t"
                +salesRecord.getRegister().getRegisterId() + "\t" + salesRecord.getTotalAmtBeforeTax() + "\t" +salesRecord.getTotalTaxAmt()
                + "\t" +salesRecord.getTotalAmt() + "\t" +salesRecord.getTransactionTime() + "\n";

        try (FileWriter fw = new FileWriter(salesFile,true);
             BufferedWriter writer = new BufferedWriter(fw)) {
            writer.write(salesSave);
        } catch (IOException e) {
            System.out.println("item addition failed: "+salesSave);
        }

        StringBuilder lineItemsDetails = new StringBuilder();
        List<SalesLineItem> salesLineItems = salesRecord.getSalesLineItems();
        boolean first = true;
        for(SalesLineItem lineItem: salesLineItems) {
            //4.	Registers will record the register number, the user (cashier), the dates and times of sale, sale items, and the amount of sales.

//            System.out.println("item id:"+lineItem.getItemId() + " quantity:"+lineItem.getQuantity() + " price: " +lineItem.getPrice()
//                    +" tax:" +lineItem.getTax() +" sale amt: " +lineItem.getLineItemAmtBeforeTax() + " sale tax: " +lineItem.getLineItemTax()
//                    +" total amt: "+lineItem.getLineItemAmt());
//            if(first)
//                lineItemsDetails.append("\n");

            lineItemsDetails.append(salesRecord.getId() + "\t" + lineItem.getItemId() + "\t" + lineItem.getQuantity() + "\t" +lineItem.getPrice()
                    +"\t" +lineItem.getTax() + "\t" +lineItem.getLineItemAmtBeforeTax() + "\t" +lineItem.getLineItemTax()
                    + "\t" + lineItem.getLineItemAmt());
            first = false;
        }

        try (FileWriter fw = new FileWriter(salesItemsFile,true);
             BufferedWriter writer = new BufferedWriter(fw)) {
            writer.write(lineItemsDetails.toString());
        } catch (IOException e) {
            System.out.println("writing sales line item failed: "+lineItemsDetails);
        }

//                getName() + "\t" + updatedItem.getOnhands() + "\t" + updatedItem.getPrice() +"\t"
//                + updatedItem.getTax() + "\t" + updatedItem.getThreshold() + "\t" + updatedItem.getSupplierId() + "\t"
//                + updatedItem.getReorderQty() + "\t" +updatedItem.getPending() +"\n";
    }

    /**
     * Only valid transaction update inventory
     * @param salesRecord
     */
    private void updateInventory(SalesTransaction salesRecord) {
        for (SalesLineItem salesLineItem: salesRecord.getSalesLineItems()) {
            Item item = InventoryRepo.getItem(salesLineItem.getItemId());
            int newOH =  (item.getOnhands() - salesLineItem.getQuantity());
            int pendingQty = orderToSupplier(item, newOH);
            Item updatedItem = new Item(item.getItemId(),item.getName(), newOH, item.getPrice(), item.getTax(),
                    item.getThreshold(), item.getSupplierId(),item.getReorderQty(), pendingQty);
            InventoryRepo.updateItem(updatedItem);
        }
    }

    /**
     * Check for threshold and onhands. if threshold>onhands order with replenishment quantity and update pending order.
     * @param item
     * @param newOH
     * @return
     */
    private int orderToSupplier(Item item, int newOH) {
        return (item.getThreshold() >= newOH)? item.getReorderQty():item.getPending();
    }

    public static Collection<SalesTransaction> getSales() {
        return sales.values();
    }

//    public static SalesTransaction getSalesRecord(int salesId) {
    public static SalesTransaction getSalesRecord(String salesId) {
        return sales.get(salesId);
    }
}
