package edu.stthomas.model;

import edu.stthomas.enums.Shift;
import edu.stthomas.repo.SalesRepo;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;

public class SalesRecord extends AbstractRecord {

    public SalesRecord(Map<Integer, Integer> itemsAndQuantity, int cashierId, Shift shift, int registerId) {
        super(cashierId, shift, registerId);
        salesRepo = new SalesRepo();
        transactionTime = Date.from(ZonedDateTime.now().toInstant());

        itemsAndQuantity.forEach((key,value)-> salesLineItems.add(new SalesLineItem(key,value)));
    }
    public SalesRecord getRecord(int id) {
        return SalesRepo.getSalesRecord(id);
    }

    public int save(SalesRecord salesRecord) {
        setId(salesRepo.save(salesRecord));
        return getId();
    }
}
