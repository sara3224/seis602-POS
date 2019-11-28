package edu.stthomas.model;

import edu.stthomas.enums.Shift;
import edu.stthomas.repo.ReturnsRepo;
import edu.stthomas.repo.SalesRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ReturnRecord extends AbstractRecord {

    private List<ReturnLineItem> returnLineItems;
    private ReturnsRepo returnsRepo;
    private String reason;
    private int salesId;

    public ReturnRecord(Map<Integer, Integer> itemsAndQuantity, int saleId, int cashierId, Shift shift, int registerId, String reason) {
        super(cashierId,shift,registerId);
        returnsRepo = new ReturnsRepo();
        this.salesId = saleId;
        SalesRecord salesRecord = SalesRepo.getSalesRecord(salesId);
        this.reason = reason;
        List<SalesLineItem> salesLineItems = salesRecord.getSalesLineItems();
        returnLineItems = new ArrayList<>();

        itemsAndQuantity.forEach((key,value) -> {
            Optional<SalesLineItem> salesLineItemOption = salesLineItems.stream().filter(it->it.getItemId() == key).findFirst();
            if(salesLineItemOption.isPresent()) {
                SalesLineItem salesLineItem = salesLineItemOption.get();
                returnLineItems.add(new ReturnLineItem(salesLineItem, value, this.reason));
            }
        });

        for (ReturnLineItem returnLineItem: returnLineItems) {
            totalAmtBeforeTax += returnLineItem.getLineItemAntBeforeTax();
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

    public List<ReturnLineItem> getReturnLineItems() {
        return returnLineItems;
    }

}
