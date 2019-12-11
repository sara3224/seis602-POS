package edu.stthomas.service;

import edu.stthomas.enums.Shift;
import edu.stthomas.exceptions.POSException;
import edu.stthomas.model.ReturnLineItem;
import edu.stthomas.model.ReturnTransaction;
import edu.stthomas.model.SalesTransaction;
import edu.stthomas.repo.ReturnsRepo;
import edu.stthomas.repo.SalesRepo;

import java.util.Map;

/**
 * Responsible for activities for the actual sale at the register:
 * Input: Cashier Id
 * captures: itemsAndQuantity and quantity
 * processes itemsAndQuantity and quantity to calculate total payment amount
 * updates inventory of itemsAndQuantity on sale
 * trigger replenishment on each sale to order new itemsAndQuantity
 *
 */
public class PointOfReturn extends AbstractPointOfAction {
    private String reason;
    private ReturnTransaction returnRecord;

    public PointOfReturn(String saledId, String cashierId, Shift shift, int registerId, String  reason) {
        super(cashierId, shift, registerId) ;
        this.saleId = saledId;
        salesRecord = SalesRepo.getSalesRecordForReturns(saledId);
        this.reason = reason;
    }

    /*
    populate all sales items for returns
     */
    public void cancelAll() {
        salesRecord.getSalesLineItems().forEach(it-> itemsAndQuantity.put(it.getItemId(), it.getQuantity()));
    }

    //call the pricing service to get cost of each item and calculate total
    //return details of refund with item, qty for line item and total refund amt
    public ReturnTransaction complete() throws POSException  {
        returnRecord = new ReturnTransaction(getItemsAndQuantity(), saleId, cashierId, shift, registerId, reason);
          //validate if returns are less than sales items count.
        SalesTransaction salesRecord = SalesRepo.getSalesRecordForReturns(saleId);
        Map<Integer,Integer> existingReturnItemQty = ReturnsRepo.getReturnRecordForReturns(saleId);

        for (ReturnLineItem returnLineItem:  returnRecord.getReturnLineItems()) {
            //get the corresponding sales line item
            //TODO: validate this scenario where return item is not in sales record.
            boolean salesHappenedForReturningItem = salesRecord.getSalesLineItems().stream().filter(it->it.getItemId() == returnLineItem.getItemId()).count() >= 1;
            if(!salesHappenedForReturningItem ) {
                throw new POSException("item-"+returnLineItem.getItemId() +" is not item of sales id: "+saleId);
            }

            int salesQty = salesRecord.getSalesLineItems().stream().filter(it->it.getItemId() == returnLineItem.getItemId()).map(it->it.getQuantity()).findFirst().get();
            //TODO: total return quantity should be aggregated by sales id for each returned item...validate
            int existingReturnQty = 0;
            if(existingReturnItemQty.containsKey(returnLineItem.getItemId())) {
                existingReturnQty = existingReturnItemQty.get(returnLineItem.getItemId());
            }
            int aggregatedReturnQty = existingReturnQty+returnLineItem.getQuantity();
            if(aggregatedReturnQty >= salesQty ) {//TODO: finish on this statement
                throw new POSException("item-"+returnLineItem.getItemId()+" aggregated return quantity "+aggregatedReturnQty+
                        " is higher than sales quantity "+salesQty + " please re-enter transaction.");
            }
        }

        String id = returnRecord.save(returnRecord);
        returnRecord =  returnRecord.getRecord(id);
        recordPrint();
        return returnRecord;
    }


    @Override
    public String addItem(Integer item_id, int qty) {
        itemsAndQuantity.put(item_id, qty);
        return "";
    }

    //TODO POS and returns print should be similar to actual receipts i.e headers and than line items
    @Override
    public void recordPrint() {
        System.out.println("return id: " +returnRecord.getId() + " sales id: " + returnRecord.getSalesId() + " cashier id: "
                +returnRecord.getCashier().getId()+ " shift: "+returnRecord.getShift()+" level: "+returnRecord.getCashier().getLevel()+ " Register: "
                +returnRecord.getRegister().getRegisterId() + " refund amt: " + returnRecord.getTotalAmtBeforeTax() + " refund tax: " +returnRecord.getTotalTaxAmt()
                +" total refund amt: " +returnRecord.getTotalAmt() +" return time: " +returnRecord.getTransactionTime());
    }
}
