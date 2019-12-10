package edu.stthomas.model;

import edu.stthomas.enums.Shift;
import edu.stthomas.helper.Helper;
import edu.stthomas.repo.SalesRepo;
import edu.stthomas.service.User;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class AbstractTransaction implements Transaction {
    protected List<SalesLineItem> salesLineItems;
    protected User cashier;
    protected Shift shift;
    protected Register register;
    protected double totalAmtBeforeTax;
    protected double totalTaxAmt;
    protected String transactionTime;
    protected SalesRepo salesRepo;
    private String id;

    public AbstractTransaction(){};

    public AbstractTransaction(String cashierId, Shift shift, int registerId) {
        this.salesLineItems = new ArrayList<>();
        this.cashier = new User(cashierId);
        this.shift = shift;
        this.register = new Register(registerId);
        transactionTime = ZonedDateTime.now().toString();
    }

    public void setId(String id) {
        this.id = id;
    }

    public abstract AbstractTransaction getRecord(String id);

    public String getId() {
        return id;
    }

    public String getTransactionTime() {
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

    public User getCashier() {
        return cashier;
    }

    public Shift getShift() {
        return shift;
    }
}
