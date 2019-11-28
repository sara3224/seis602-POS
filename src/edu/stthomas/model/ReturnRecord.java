package edu.stthomas.model;

import edu.stthomas.enums.Shift;
import edu.stthomas.helper.Helper;
import edu.stthomas.repo.ReturnsRepo;
import edu.stthomas.repo.SalesRepo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ReturnRecord extends AbstractRecord {

    private List<ReturnLineItem> returnLineItems;
    private SalesRecord salesRecord;
    private ReturnsRepo returnsRepo;
    private String reason;
    private int salesId;

    public ReturnRecord(Map<Integer, Integer> itemsAndQuantity, int saleId, int cashierId, Shift shift, int registerId, String reason) {
        super(cashierId,shift,registerId);
        salesRepo = new SalesRepo();
        returnsRepo = new ReturnsRepo();
        this.salesId = saleId;
        salesRecord = SalesRepo.getSalesRecord(salesId);
        this.reason = reason;
        List<SalesLineItem> salesLineItems = salesRecord.getSalesLineItems();
        returnLineItems = new ArrayList<>();

        itemsAndQuantity.forEach((key,value) -> {
            SalesLineItem salesLineItem = salesLineItems.stream().filter(it->it.getItemId() == key).findFirst().get();
            returnLineItems.add(new ReturnLineItem(salesLineItem, value, this.reason));
        });

        for (ReturnLineItem returnLineItem: returnLineItems) {
            totalAmtBeforeTax += returnLineItem.getLineItemRefund();
            totalTaxAmt += returnLineItem.getLineItemTax();
        }
    }

    public int save(ReturnRecord returnsRecord) {
        setId(returnsRepo.save(returnsRecord));
        return getId();
    }

    public ReturnRecord getRecord(int id) {
        return returnsRepo.getReturnsRecord(id);
    }

    public int getSalesId() {
        return salesId;
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

    public List<ReturnLineItem> getReturnLineItems() {
        return returnLineItems;
    }

    public Register getRegister() {
        return register;
    }

    public Date getTransactionTime() {
        return transactionTime;
    }

    public Cashier getCashier() {
        return cashier;
    }

    public Shift getShift() {
        return shift;
    }
}
