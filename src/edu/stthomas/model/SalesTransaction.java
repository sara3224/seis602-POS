package edu.stthomas.model;

import edu.stthomas.enums.Shift;
import edu.stthomas.repo.SalesRepo;

import java.util.List;
import java.util.Map;

public class SalesTransaction extends AbstractTransaction {
    private String registerId;
    private double totalAmt;
    private String reportDate;

    public SalesTransaction() {
    }

    public SalesTransaction(Map<Integer, Integer> itemsAndQuantity, String cashierId, Shift shift, int registerId) {
        super(cashierId, shift, registerId);
        salesRepo = new SalesRepo();
        itemsAndQuantity.forEach((key,value)-> salesLineItems.add(new SalesLineItem(key,value)));

        for (SalesLineItem salesLineItem : salesLineItems) {
            totalAmtBeforeTax += salesLineItem.getLineItemAmtBeforeTax();
            totalTaxAmt += salesLineItem.getLineItemTax();
        }
    }

    public SalesTransaction(List<SalesLineItem> salesItems ) {
        this.salesLineItems = salesItems;
        for (SalesLineItem salesLineItem : salesItems) {
            totalAmtBeforeTax += salesLineItem.getLineItemAmtBeforeTax();
            totalTaxAmt += salesLineItem.getLineItemTax();
        }
    }

    public SalesTransaction(List<SalesLineItem> salesItems, String cashierId, Shift shift, int registerId ) {
        super(cashierId, shift, registerId);
        this.salesLineItems = salesItems;
        for (SalesLineItem salesLineItem : salesItems) {
            totalAmtBeforeTax += salesLineItem.getLineItemAmtBeforeTax();
            totalTaxAmt += salesLineItem.getLineItemTax();
        }
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }

    public String getReportDate() {
        return reportDate;
    }

    public double getTotalAmtReport() {
        return totalAmt;
    }

    public void setTotalAmtReport(double totalAmt) {
        this.totalAmt = totalAmt;
    }


    public String getRegisterId() {
        return registerId;
    }

    public void setRegisterId(String registerId) {
        this.registerId = registerId;
    }

    public SalesTransaction getRecord(String id) {
        return SalesRepo.getSalesRecord(id);
    }

    public String save(SalesTransaction salesRecord) {
        setId(salesRepo.save(salesRecord));
        return getId();
    }
}
