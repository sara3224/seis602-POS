package edu.stthomas.repo;

import edu.stthomas.enums.Shift;
import edu.stthomas.model.Item;
import edu.stthomas.model.SalesLineItem;
import edu.stthomas.model.SalesTransaction;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * manage sales transactions
 */
public class SalesRepo {
    //TODO: report x
    //TODO report y
    public static final Map<String, SalesTransaction> sales = new HashMap<>();
    private static File salesFile = new File("./data/" + "sales.tsv");
    private static File salesItemsFile = new File("./data/" + "salesLineItems.tsv");

    public String save(SalesTransaction salesRecord) {
        String salesId =  UUID.randomUUID().toString().substring(0,8);
        sales.put(salesId, salesRecord);
        salesRecord.setId(salesId);
        record(salesRecord);
        updateInventory(salesRecord);
        return salesId;
    }

    private void record(SalesTransaction salesRecord) {
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
        for(SalesLineItem lineItem: salesLineItems) {
            lineItemsDetails.append(salesRecord.getId() + "\t" + lineItem.getItemId() + "\t" + lineItem.getQuantity() + "\t" +lineItem.getPrice()
                    +"\t" +lineItem.getTax() + "\t" +lineItem.getLineItemAmtBeforeTax() + "\t" +lineItem.getLineItemTax()
                    + "\t" + lineItem.getLineItemAmt() + "\n");
        }

        try (FileWriter fw = new FileWriter(salesItemsFile,true);
             BufferedWriter writer = new BufferedWriter(fw)) {
            writer.write(lineItemsDetails.toString());
        } catch (IOException e) {
            System.out.println("writing sales line item failed: "+lineItemsDetails);
        }
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

    public static Collection<SalesTransaction> getSalesForReportX(String cashierId, Shift shift, String reportDate) {
        List<SalesTransaction> salesTransactions = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(salesFile))) {
            String str;
            SalesTransaction salesTransaction = new SalesTransaction();
            while ((str = br.readLine()) != null) { //loop until end of file.
                String[] line = str.split("\t");
                //id0	cashier1	shift2	level3	register4	totalAmtBeforeTax5	totalTaxAmt6	totalAmt7	transactionTime8
                if (line.length>3 && Objects.equals(line[1], cashierId) && Objects.equals(shift.name(), line[2])) {
                    ZonedDateTime saleDate = ZonedDateTime.parse(line[8]);
                    if(isSameDay(new SimpleDateFormat("yyy-MM-dd").parse(reportDate), Date.from(saleDate.toInstant()))) {
                        salesTransaction = new SalesTransaction();
                        salesTransaction.setRegisterId(line[4]);
                        salesTransaction.setTotalAmtReport(Double.valueOf(line[7]));
                        salesTransaction.setReportDate(line[8]);
                        salesTransactions.add(salesTransaction);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return salesTransactions;
    }

    public static Collection<SalesTransaction> getSalesForReportZ(Shift shift, String reportDate) {
        List<SalesTransaction> salesTransactions = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(salesFile))) {
            String str;
            SalesTransaction salesTransaction = new SalesTransaction();
            while ((str = br.readLine()) != null) { //loop until end of file.
                String[] line = str.split("\t");
                //id0	cashier1	shift2	level3	register4	totalAmtBeforeTax5	totalTaxAmt6	totalAmt7	transactionTime8
                if (line.length>3 && Objects.equals(shift.name(), line[2])) {
                    ZonedDateTime saleDate = ZonedDateTime.parse(line[8]);
                    if(isSameDay(new SimpleDateFormat("yyy-MM-dd").parse(reportDate), Date.from(saleDate.toInstant()))) {
                        salesTransaction = new SalesTransaction();
                        salesTransaction.setCashier(line[1]);
                        salesTransaction.setRegisterId(line[4]);
                        salesTransaction.setTotalAmtReport(Double.valueOf(line[7]));
                        salesTransaction.setReportDate(line[8]);
                        salesTransactions.add(salesTransaction);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return salesTransactions;
    }


    private static boolean isSameDay(Date reportDate2, Date salesDate) {
        Calendar requestedDate = Calendar.getInstance();
        requestedDate.setTime(reportDate2);

        Calendar salesRecordCal = Calendar.getInstance();
        salesRecordCal.setTime(salesDate);

        return requestedDate.get(Calendar.DAY_OF_YEAR) == salesRecordCal.get(Calendar.DAY_OF_YEAR) &&
                requestedDate.get(Calendar.YEAR) == salesRecordCal.get(Calendar.YEAR);
    }

    /**
     * get sales record from the sales and salesLineitems tsv file
     * @param salesId
     * @return
     */
    public static SalesTransaction getSalesRecord(String salesId) {
        return sales.get(salesId);
    }

    /**
     * get sales record from the sales and salesLineitems tsv file
     * @param salesId
     * @return
     */
    public static SalesTransaction getSalesRecordForReturns(String salesId) {
        SalesTransaction salesTransaction = sales.get(salesId);
        if(salesTransaction == null) {
            salesTransaction = new SalesTransaction();
            List<SalesLineItem> salesLineItems = new ArrayList<>();
            try {
                try (BufferedReader br = new BufferedReader(new FileReader(salesItemsFile))) {
                    String str;
                    while ((str = br.readLine()) != null) { //loop until end of file.
                        String[] line = str.split("\t");
                        if (Objects.equals(line[0], salesId)) {
                            salesLineItems.add(new SalesLineItem(Integer.valueOf(line[1]), Integer.valueOf(line[2]),Double.valueOf(line[3]), Double.valueOf(line[4])));
                        }
                    }
                }
                salesTransaction = new SalesTransaction(salesLineItems);
            } catch (FileNotFoundException e) {
                System.out.println("File not found" + e);
            } catch (IOException e) {
                System.out.println("File not found" + e);
            }
        }
        return salesTransaction;
    }
}
