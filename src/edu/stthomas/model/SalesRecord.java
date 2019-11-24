package edu.stthomas.model;

import edu.stthomas.enums.Shift;
import edu.stthomas.helper.Helper;
import edu.stthomas.repo.SalesRepo;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class SalesRecord {

    private List<SalesLineItem> salesLineItems;
    private Cashier cashier;
    private Shift shift;
    private Register register;
    private double totalSalesAmt;
    private double totalTaxAmt;
    private Date salesTime;
    private int id;
    private SalesRepo salesRepo;

    public SalesRecord(Map<Integer, Integer> itemsAndQuantity, int cashierId, Shift shift, int registerId) {
        salesRepo = new SalesRepo();
        this.salesLineItems = new ArrayList<>();
        this.cashier = new Cashier(cashierId);
        this.shift = shift;
        this.register = new Register(registerId);
        salesTime = Date.from(ZonedDateTime.now().toInstant());
        itemsAndQuantity.forEach((key,value)-> salesLineItems.add(new SalesLineItem(key,value)));
        for (SalesLineItem salesLineItem : salesLineItems) {
            totalSalesAmt += salesLineItem.getLineItemSale();
            totalTaxAmt += salesLineItem.getLineItemTax();
        }
    }

    public void save(SalesRecord salesRecord) {
        id = salesRepo.save(salesRecord);
    }

    public int getSalesId() {
        return id;
    }


    public Date getSalesTime() {
        return salesTime;
    }

    /**
     * get the total of sales from each line item (excludes tax amt)
     * @return
     */
    public double getTotalSalesAmt() {
        return Helper.roundUp(totalSalesAmt);
    }

    public double getTotalTaxAmt() {
        return Helper.roundUp(totalTaxAmt);
    }

    public double getTotalAmt() {
        return Helper.roundUp(getTotalSalesAmt() + getTotalTaxAmt());
    }

    public List<SalesLineItem> getSalesLineItems() {
        return salesLineItems;
    }

    public Register getRegister() {
        return register;
    }

    public Cashier getCashier() {
        return cashier;
    }

    public Shift getShift() {
        return shift;
    }
}
