package edu.stthomas.service;

import edu.stthomas.enums.Shift;
import edu.stthomas.exceptions.POSException;
import edu.stthomas.helper.Helper;
import edu.stthomas.model.AbstractLineItem;
import edu.stthomas.model.ReturnLineItem;
import edu.stthomas.model.ReturnTransaction;
import edu.stthomas.model.SalesTransaction;
import edu.stthomas.repo.ReturnsRepo;
import edu.stthomas.repo.SalesRepo;

import java.util.Map;
import java.util.Optional;

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

    public PointOfReturn(String saledId, String cashierId, Shift shift, String registerId, String  reason) {
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
        returnRecord = new ReturnTransaction(saleId, cashierId, shift, registerId, reason);
        returnRecord.calculateReturnDetails(getItemsAndQuantity());
          //validate if returns are less than sales items count.
        SalesTransaction salesRecord = SalesRepo.getSalesRecordForReturns(saleId);
        Map<String,Integer> existingReturnItemQty = ReturnsRepo.getReturnRecordForReturns(saleId);

        for (ReturnLineItem returnLineItem:  returnRecord.getReturnLineItems()) {
            //get the corresponding sales line item
            boolean salesHappenedForReturningItem = salesRecord.getSalesLineItems().stream().filter(it->it.getItemId().equals(returnLineItem.getItemId())).count() >= 1;
            if(!salesHappenedForReturningItem ) {
                throw new POSException("item-"+returnLineItem.getItemId() +" is not item of sales id: "+saleId);
            }

            Optional<Integer> salesLineItemQty = salesRecord.getSalesLineItems().stream().filter(it->it.getItemId().equals(returnLineItem.getItemId())).map(
                    AbstractLineItem::getQuantity).findFirst();
            boolean salesLineItemPresent  = salesLineItemQty.isPresent();
            int salesQty = 0;
            if(salesLineItemPresent) {
                salesQty = salesLineItemQty.get();
            }
            //TODO: total return quantity should be aggregated by sales id for each returned item...validate
            int existingReturnQty = 0;
            if(existingReturnItemQty.containsKey(returnLineItem.getItemId())) {
                existingReturnQty = existingReturnItemQty.get(returnLineItem.getItemId());
            }
            int aggregatedReturnQty = existingReturnQty+returnLineItem.getQuantity();
            if(aggregatedReturnQty > salesQty ) {//TODO: finish on this statement
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
    public String addItem(String item_id, int qty) {
        itemsAndQuantity.put(item_id, qty);
        return "";
    }

    @Override
    public void recordPrint() {
        System.out.println("return for sales id: " + returnRecord.getSalesId()
                + "\ncashier id: " +returnRecord.getCashier().getId()
                + "\nshift: "+returnRecord.getShift()
                + "\nRegister: " +returnRecord.getRegister().getRegisterId()
                + "\nrefund amt: " + Helper.digit2Doubles(returnRecord.getTotalAmtBeforeTax())
                + "\nrefund tax: " +Helper.digit2Doubles(returnRecord.getTotalTaxAmt())
                +"\ntotal refund amt: " +Helper.digit2Doubles(returnRecord.getTotalAmt())
                +"\nreturn time: " +returnRecord.getTransactionTime());
        for(ReturnLineItem returnLineItem : returnRecord.getReturnLineItems()) {
            System.out.println("Item:" +returnLineItem.getItemId() + " price: "+Helper.digit2Doubles(returnLineItem.getPrice())+ " tax: "+ returnLineItem.getTax()
            +" qty:"+returnLineItem.getQuantity() + " sale refund: "+Helper.digit2Doubles(returnLineItem.getLineItemAmtBeforeTax()) + " tax refund:"+
                    Helper.digit2Doubles(returnLineItem.getLineItemTax()) + " total refund: "+ Helper.digit2Doubles(returnLineItem.getLineItemAmt()));
        }
    }
}
