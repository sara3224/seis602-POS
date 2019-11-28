package edu.stthomas.model;

import edu.stthomas.enums.Shift;
import edu.stthomas.helper.Helper;
import edu.stthomas.repo.ReturnsRepo;
import edu.stthomas.repo.SalesRepo;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ReturnRecord {

    private List<ReturnLineItem> returnLineItems;
    private Cashier cashier;
    private Shift shift;
    private Register register;
    private double totalRefundAmt;
    private double totalTaxRefundAmt;
    private Date refundTime;
    private int id;
    private SalesRepo salesRepo;
    private SalesRecord salesRecord;
    private ReturnsRepo returnsRepo;
    private String reason;
    private int salesId;

    public ReturnRecord(Map<Integer, Integer> itemsAndQuantity, int saleId, int cashierId, Shift shift, int registerId, String reason) {
        salesRepo = new SalesRepo();
        returnsRepo = new ReturnsRepo();
        this.salesId = saleId;
        salesRecord = salesRepo.getSalesRecord(salesId);
//        this.salesLineItems = salesRecord.getSalesLineItems();
        this.cashier = new Cashier(cashierId);
        this.shift = shift;
        this.register = new Register(registerId);
        refundTime = Date.from(ZonedDateTime.now().toInstant());
        this.reason = reason;
        this.salesId = saleId;
        List<SalesLineItem> salesLineItems = salesRecord.getSalesLineItems();
        returnLineItems = new ArrayList<>();

        itemsAndQuantity.forEach((key,value) -> {
            SalesLineItem salesLineItem = salesLineItems.stream().filter(it->it.getItemId() == key).findFirst().get();
            returnLineItems.add(new ReturnLineItem(salesLineItem, value, reason));
        });

        for (ReturnLineItem returnLineItem: returnLineItems) {
            totalRefundAmt += returnLineItem.getLineItemRefund();
            totalTaxRefundAmt += returnLineItem.getLineItemTax();
        }
//                (key, value) -> sal
//        itemsAndQuantity.forEach((key,value)-> returnLineItems.add(new ReturnLineItem(key,value)));
//        for (SalesLineItem salesLineItem : salesLineItems) {
//            totalRefundAmt += salesLineItem.getLineItemSale();
//            totalTaxAmt += salesLineItem.getLineItemTax();
//        }
    }

//    public ReturnRecord complete() {
//        SalesRecord salesRecord = salesRepo.getSalesRecord(salesId);
//        List<SalesLineItem> salesLineItems = salesRecord.getSalesLineItems();
//        //create a refund line items
//        List<ReturnLineItem> returnLineItems = new ArrayList<>();
//
//        for(SalesLineItem salesLineItem: salesLineItems) {
//            ReturnLineItem refundLineItem = new ReturnLineItem(salesLineItem, reason);
//            returnLineItems.add(refundLineItem);
//        }
//        returnsRepo.save(this);
//    }


//    public List<ReturnLineItem> refundAll(int salesId, String reason) {
//        //get all items from sales and refund all
//        SalesRecord salesRecord = salesRepo.getSalesRecord(salesId);
//        List<SalesLineItem> salesLineItems = salesRecord.getSalesLineItems();
//        //create a refund line items
//        List<ReturnLineItem> returnLineItems = new ArrayList<>();
//
//        for(SalesLineItem salesLineItem: salesLineItems) {
//            ReturnLineItem refundLineItem = new ReturnLineItem(salesLineItem, reason);
//            returnLineItems.add(refundLineItem);
//        }
//        returnsRepo.save(this);
//        return returnLineItems;
//    }

    public int save(ReturnRecord returnsRecord) {
        id = returnsRepo.save(returnsRecord);
        return id;
    }

    public ReturnRecord getRecord(int id) {
        return returnsRepo.getReturnsRecord(id);
    }

    public int getSalesId() {
        return salesId;
    }

    public int getReturnId() {
        return id;
    }

    /**
     * get the total of sales from each line item (excludes tax amt)
     * @return
     */
    public double getTotalRefundAmt() {
        return Helper.roundUp(totalRefundAmt);
    }
//
    public double getTotalTaxAmt() {
        return Helper.roundUp(totalTaxRefundAmt);
    }

    public double getTotalAmt() {
        return Helper.roundUp(getTotalRefundAmt() + getTotalTaxAmt());
    }

    public List<ReturnLineItem> getReturnLineItems() {
        return returnLineItems;
    }

    public Register getRegister() {
        return register;
    }

    public Date getRefundTime() {
        return refundTime;
    }

    public Cashier getCashier() {
        return cashier;
    }

    public Shift getShift() {
        return shift;
    }
}
