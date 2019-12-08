package edu.stthomas.model;

import edu.stthomas.enums.Shift;
import edu.stthomas.repo.SalesRepo;

import java.util.Map;

public class SalesTransaction extends AbstractTransaction {

    public SalesTransaction(Map<Integer, Integer> itemsAndQuantity, String cashierId, Shift shift, int registerId) {
        super(cashierId, shift, registerId);
        salesRepo = new SalesRepo();
        itemsAndQuantity.forEach((key,value)-> salesLineItems.add(new SalesLineItem(key,value)));

        for (SalesLineItem salesLineItem : salesLineItems) {
            totalAmtBeforeTax += salesLineItem.getLineItemAmtBeforeTax();
            totalTaxAmt += salesLineItem.getLineItemTax();
        }
    }
    public SalesTransaction getRecord(String id) {
        return SalesRepo.getSalesRecord(id);
    }

    public String save(SalesTransaction salesRecord) {
        setId(salesRepo.save(salesRecord));
        return getId();
    }
}
