package edu.stthomas.model;

import edu.stthomas.enums.Shift;
import edu.stthomas.exceptions.POSException;
import edu.stthomas.repo.ReturnsRepo;
import edu.stthomas.repo.SalesRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ReturnTransaction extends AbstractTransaction {

    private List<ReturnLineItem> returnLineItems;
    private ReturnsRepo returnsRepo;
    private String reason;
    private String salesId;

    public ReturnTransaction( String saleId, String cashierId, Shift shift, String registerId, String reason) {
        super(cashierId,shift,registerId);
        returnsRepo = new ReturnsRepo();
        this.salesId = saleId;
        this.reason = reason;
        returnLineItems = new ArrayList<>();
    }

    public void calculateReturnDetails(Map<String, Integer> itemsAndQuantity) throws POSException{
        SalesTransaction salesRecord = SalesRepo.getSalesRecordForReturns(salesId);
        List<SalesLineItem> salesLineItems = salesRecord.getSalesLineItems();

        for (Map.Entry<String, Integer> entry : itemsAndQuantity.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            Optional<SalesLineItem> salesLineItemOption = salesLineItems.stream()
                    .filter(it -> it.getItemId().equals(key))
                    .findFirst();
            if (salesLineItemOption.isPresent()) {
                SalesLineItem salesLineItem = salesLineItemOption.get();
                returnLineItems.add(new ReturnLineItem(salesLineItem, value, this.reason));
            } else {
                    throw new POSException("Item:"+key+ " is not associated with sales: "+salesId + " Please re-enter transaction");
            }
        }

        for (ReturnLineItem returnLineItem: returnLineItems) {
            totalAmtBeforeTax += returnLineItem.getLineItemAmtBeforeTax();
            totalTaxAmt += returnLineItem.getLineItemTax();
        }
    }

    public String save(ReturnTransaction returnsRecord) {
        setId(returnsRepo.save(returnsRecord));
        return getId();
    }

    public ReturnTransaction getRecord(String id) {
        return returnsRepo.getReturnsRecord(id);
    }

    public String getSalesId() {
        return salesId;
    }

    public List<ReturnLineItem> getReturnLineItems() {
        return returnLineItems;
    }

}
