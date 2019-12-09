package edu.stthomas.service;

import edu.stthomas.enums.Shift;
import edu.stthomas.exceptions.POSException;
import edu.stthomas.model.ReturnLineItem;
import edu.stthomas.model.ReturnTransaction;
import edu.stthomas.model.SalesLineItem;
import edu.stthomas.model.SalesTransaction;
import edu.stthomas.repo.SalesRepo;

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
        salesRecord.getSalesLineItems().stream().forEach(it-> itemsAndQuantity.put(it.getItemId(), it.getQuantity()));
    }

    //call the pricing service to get cost of each item and calculate total
    //return details of refund with item, qty for line item and total refund amt
    public ReturnTransaction complete() throws POSException  {
          returnRecord = new ReturnTransaction(itemsAndQuantity, saleId, cashierId, shift, registerId, reason);
          //validate if returns are less than sales items count.
            SalesTransaction salesRecord = SalesRepo.getSalesRecordForReturns(saleId);

        for (ReturnLineItem returnLineItem: returnRecord.getReturnLineItems()) {
            SalesLineItem salesLineItem = salesRecord.getSalesLineItems().stream().filter(it->it.getItemId() == returnLineItem.getItemId()).findFirst().get();
            if(salesLineItem == null) {
                throw new POSException("item-"+returnLineItem.getItemId() +" is not item of sales id: "+saleId);
            }
            //TODO: total return quanity should be aggregated by sales id for each returned item
            if(returnLineItem.getQuantity() > salesLineItem.getQuantity()) {
                throw new POSException("item-"+returnLineItem.getItemId()+" quantity "+returnLineItem.getQuantity()+
                        " is higher than sales quantity "+salesLineItem.getQuantity()+ " please re-enter transaction.");
            }
        }
            String id = returnRecord.save(returnRecord);
            returnRecord =  returnRecord.getRecord(id);
            recordPrint();
            return returnRecord;
    }

    //TODO POS and returns print should be similar to actual receipts i.e headers and than line items
    @Override
    public void recordPrint() {
        System.out.println("return id: " +returnRecord.getId() + " sales id: " + returnRecord.getSalesId() + "cashier id: "
                +returnRecord.getCashier().getId()+ " shift: "+returnRecord.getShift()+" level: "+returnRecord.getCashier().getLevel()+ " Register: "
                +returnRecord.getRegister().getRegisterId() + " refund amt: " + returnRecord.getTotalAmtBeforeTax() + " refund tax: " +returnRecord.getTotalTaxAmt()
                +" total refund amt: " +returnRecord.getTotalAmt() +" return time: " +returnRecord.getTransactionTime());
    }
}
