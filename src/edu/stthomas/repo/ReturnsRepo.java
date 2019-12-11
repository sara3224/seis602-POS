package edu.stthomas.repo;

import edu.stthomas.model.Item;
import edu.stthomas.model.ReturnLineItem;
import edu.stthomas.model.ReturnTransaction;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

//TODO; write to returns and returns line items file..cannot have more items returned than purchased.
public class ReturnsRepo {
    //this would be in file or database
    public static final Map<String, ReturnTransaction> returns = new HashMap<>();
    private static File returnsFile = new File("./data/" + "returns.tsv");
    private static File returnsItemsFile = new File("./data/" + "returnsLineItems.tsv");

    //TODO: check if inventory is added back and also not over returns
    public String save(ReturnTransaction returnRecord) {
        String returnId =  UUID.randomUUID().toString().substring(0,8);
        returnRecord.setId(returnId);
        returns.put(returnId, returnRecord);
        record(returnRecord);
        updateInventory(returnRecord);
        return returnId;
    }

//    public static List<ReturnLineItem> getReturnRecordForReturns(String salesId) {
//            List<ReturnLineItem> returnLineItems = new ArrayList<>();
//            try {
//                try (BufferedReader br = new BufferedReader(new FileReader(returnsItemsFile))) {
//                    String str;
//                    while ((str = br.readLine()) != null) { //loop until end of file.
//                        String[] line = str.split("\t");
//                        if (Objects.equals(line[0], salesId)) {
//                            returnLineItems.add(new ReturnLineItem(salesId, Integer.valueOf(line[2]), Integer.valueOf(line[3])));
//                        }
//                    }
//                }
//            } catch (FileNotFoundException e) {
//                System.out.println("File not found" + e);
//            } catch (IOException e) {
//                System.out.println("File not found" + e);
//            }
//        return returnLineItems;
//    }


    public static Map<Integer,Integer> getReturnRecordForReturns(String salesId) {
        Map<Integer, Integer> returnItemQty = new HashMap<>();
        try {
            try (BufferedReader br = new BufferedReader(new FileReader(returnsItemsFile))) {
                String str;
                while ((str = br.readLine()) != null) { //loop until end of file.
                    String[] line = str.split("\t");
                    if (Objects.equals(line[0], salesId)) {
                        int itemId = Integer.parseInt(line[2]);
                        int qty = Integer.parseInt(line[3]);
                        if(returnItemQty.containsKey(itemId)) {
                            qty += returnItemQty.get(itemId);
                        }
                        returnItemQty.put( itemId, qty);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found" + e);
        } catch (IOException e) {
            System.out.println("File not found" + e);
        }
        return returnItemQty;
    }

    private void record(ReturnTransaction returnRecord) {
            String returnsSave = returnRecord.getSalesId() + "\t" + returnRecord.getId() + "\t" +returnRecord.getCashier().getId() + "\t"
                    +returnRecord.getShift() + "\t" +returnRecord.getCashier().getLevel()+ "\t"
                +returnRecord.getRegister().getRegisterId() + "\t" + returnRecord.getTotalAmtBeforeTax() + "\t" +returnRecord.getTotalTaxAmt()
                + "\t" +returnRecord.getTotalAmt() + "\t" +returnRecord.getTransactionTime() + "\n";

        try (FileWriter fw = new FileWriter(returnsFile,true);
             BufferedWriter writer = new BufferedWriter(fw)) {
            writer.write(returnsSave);
        } catch (IOException e) {
            System.out.println("item returns failed: "+returnsSave);
        }

        StringBuilder lineItemsDetails = new StringBuilder();
        List<ReturnLineItem> returnsLineItems = returnRecord.getReturnLineItems();
        for(ReturnLineItem lineItem: returnsLineItems) {
            lineItemsDetails.append(returnRecord.getSalesId() + "\t" +returnRecord.getId() + "\t" + lineItem.getItemId() + "\t" + lineItem.getQuantity() + "\t" +lineItem.getPrice()
                    +"\t" +lineItem.getTax() + "\t" +lineItem.getLineItemAmtBeforeTax() + "\t" +lineItem.getLineItemTax()
                    + "\t" + lineItem.getLineItemAmt() + "\n");
        }

        try (FileWriter fw = new FileWriter(returnsItemsFile,true);
             BufferedWriter writer = new BufferedWriter(fw)) {
            writer.write(lineItemsDetails.toString());
        } catch (IOException e) {
            System.out.println("writing sales line item failed: "+lineItemsDetails);
        }
    }

    /**
     * Only valid transaction update inventory
     * @param returnRecord
     */
    private void updateInventory(ReturnTransaction returnRecord) {
        for (ReturnLineItem returnsLineItem: returnRecord.getReturnLineItems()) {
            Item item = InventoryRepo.getItem(returnsLineItem.getItemId());
            int newOH =  (item.getOnhands() + returnsLineItem.getQuantity());
            Item updatedItem = new Item(item.getItemId(),item.getName(), newOH, item.getPrice(), item.getTax(),
                    item.getThreshold(), item.getSupplierId(),item.getReorderQty(), item.getPending());
            InventoryRepo.updateItem(updatedItem);
        }
    }

    public static Map<String, ReturnTransaction> getAllReturns() {
        return returns;
    }

    public ReturnTransaction getReturnsRecord(String returnId) {
        return returns.get(returnId);
    }
}
