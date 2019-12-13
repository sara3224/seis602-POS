package edu.stthomas.repo;

import edu.stthomas.enums.Shift;
import edu.stthomas.helper.Helper;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class ReturnsRepo {
    //this would be in file or database
    public static final Map<String, ReturnTransaction> returns = new HashMap<>();
    private static File returnsFile = new File("./data/" + "returns.tsv");
    private static File returnsItemsFile = new File("./data/" + "returnsLineItems.tsv");

    public String save(ReturnTransaction returnRecord) {
        String returnId =  UUID.randomUUID().toString().substring(0,8);
        returnRecord.setId(returnId);
        returns.put(returnId, returnRecord);
        record(returnRecord);
        updateInventory(returnRecord);
        return returnId;
    }

    public static Map<String,Integer> getReturnRecordForReturns(String salesId) {
        Map<String, Integer> returnItemQty = new HashMap<>();
        try {
            try (BufferedReader br = new BufferedReader(new FileReader(returnsItemsFile))) {
                String str;
                while ((str = br.readLine()) != null) { //loop until end of file.
                    String[] line = str.split("\t");
                    if (Objects.equals(line[0], salesId)) {
                        String itemId = line[2];
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
                +returnRecord.getRegister().getRegisterId() + "\t" + Helper.digit2Doubles(returnRecord.getTotalAmtBeforeTax()) + "\t" +Helper.digit2Doubles(returnRecord.getTotalTaxAmt())
                + "\t" +Helper.digit2Doubles(returnRecord.getTotalAmt()) + "\t" +returnRecord.getTransactionTime() + "\n";

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
                    +"\t" +lineItem.getTax() + "\t" +Helper.digit2Doubles(lineItem.getLineItemAmtBeforeTax()) + "\t" +Helper.digit2Doubles(lineItem.getLineItemTax())
                    + "\t" + Helper.digit2Doubles(lineItem.getLineItemAmt()) + "\n");
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

//salesid	returnId	cashier	shift	level	register	totalAmtBeforeTax	totalTaxAmt	totalAmt
    public static Collection<ReturnTransaction> getReturnsForReportZ(Shift shift, String reportDate) {
        List<ReturnTransaction> returnTransactions = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(returnsFile))) {
            String str;
            ReturnTransaction returnTransaction;
            while ((str = br.readLine()) != null) { //loop until end of file.
                String[] line = str.split("\t");
                //salesid0	returnId1	cashier2	shift3	level4	register5	totalAmtBeforeTax6	totalTaxAmt7	totalAmt8 transactionTime9
                if (line.length>4 && Objects.equals(shift.name(), line[3])) {
                    ZonedDateTime transactionDate = ZonedDateTime.parse(line[9]);
                    if(Helper.isSameDay(new SimpleDateFormat("yyy-MM-dd").parse(reportDate), Date.from(transactionDate.toInstant()))) {
                        returnTransaction = new ReturnTransaction();
                        returnTransaction.setCashier(line[2]);
                        returnTransaction.setRegisterId(line[5]);
                        returnTransaction.setTotalAmtReport(Double.valueOf(line[8]));
                        returnTransaction.setReportDate(line[9]);
                        returnTransactions.add(returnTransaction);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Please enter date in 'YYYY-MM-DD' format only, please retry.");
        } catch (ParseException e) {
            System.out.println("Please enter date in 'YYYY-MM-DD' format only, please retry.");
        }
        return returnTransactions;
    }

    //salesid	returnId	cashier	shift	level	register	totalAmtBeforeTax	totalTaxAmt	totalAmt	transactionTime
    public static Collection<ReturnTransaction> getReturnForReportX(String cashierId, Shift shift, String reportDate) {
        List<ReturnTransaction> returnTransactions = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(returnsFile))) {
            String str;
            ReturnTransaction returnTransaction;
            while ((str = br.readLine()) != null) { //loop until end of file.
                String[] line = str.split("\t");
                //salesid0	returnId1	cashier2	shift3	level4	register5	totalAmtBeforeTax6	totalTaxAmt7	totalAmt8	transactionTime9
                if (line.length>4 && Objects.equals(line[2], cashierId) && Objects.equals(shift.name(), line[3])) {
                    ZonedDateTime returnDate = ZonedDateTime.parse(line[9]);
                    if(Helper.isSameDay(new SimpleDateFormat("yyy-MM-dd").parse(reportDate), Date.from(returnDate.toInstant()))) {
                        returnTransaction = new ReturnTransaction();
                        returnTransaction.setRegisterId(line[5]);
                        returnTransaction.setTotalAmtReport(Double.valueOf(line[8]));
                        returnTransaction.setReportDate(line[9]);
                        returnTransactions.add(returnTransaction);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Please enter date in 'YYYY-MM-DD' format only, please retry.");
        } catch (ParseException e) {
            System.out.println("Please enter date in 'YYYY-MM-DD' format only, please retry.");
        }
        return returnTransactions;
    }

    public ReturnTransaction getReturnsRecord(String returnId) {
        return returns.get(returnId);
    }
}
