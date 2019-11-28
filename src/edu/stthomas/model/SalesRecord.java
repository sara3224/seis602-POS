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
    private double totalAmtBeforeTax;
    private double totalTaxAmt;
    private Date transactionTime;
    private int id;
    private SalesRepo salesRepo;

    public SalesRecord(Map<Integer, Integer> itemsAndQuantity, int cashierId, Shift shift, int registerId) {
        salesRepo = new SalesRepo();
        this.salesLineItems = new ArrayList<>();
        this.cashier = new Cashier(cashierId);
        this.shift = shift;
        this.register = new Register(registerId);
        transactionTime = Date.from(ZonedDateTime.now().toInstant());

        itemsAndQuantity.forEach((key,value)-> salesLineItems.add(new SalesLineItem(key,value)));
        for (SalesLineItem salesLineItem : salesLineItems) {
            totalAmtBeforeTax += salesLineItem.getLineItemSale();
            totalTaxAmt += salesLineItem.getLineItemTax();
        }
    }

    public int save(SalesRecord salesRecord) {
        id = salesRepo.save(salesRecord);
        return id;
    }

    public SalesRecord getRecord(int id) {
        return SalesRepo.getSalesRecord(id);
    }

    public int getId() {
        return id;
    }

    public Date getTransactionTime() {
        return transactionTime;
    }

    /**
     * get the total of sales from each line item (excludes tax amt)
     * @return
     */
    public double getTotalAmtBeforeTax() {
        return Helper.roundUp(totalAmtBeforeTax);
    }

    public double getTotalTaxAmt() {
        return Helper.roundUp(totalTaxAmt);
    }

    public double getTotalAmt() {
        return Helper.roundUp(getTotalAmtBeforeTax() + getTotalTaxAmt());
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
