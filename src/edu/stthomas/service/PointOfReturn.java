package edu.stthomas.service;

import edu.stthomas.enums.Shift;
import edu.stthomas.model.ReturnTransaction;
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
        salesRecord = SalesRepo.getSalesRecord(saledId);
        this.reason = reason;
    }

    /*
    populate all sales items for returns
     */
    public void cancelAll(){
        salesRecord.getSalesLineItems().stream().forEach(it-> itemsAndQuantity.put(it.getItemId(), it.getQuantity()));
    }

    //call the pricing service to get cost of each item and calculate total
    //return details of refund with item, qty for line item and total refund amt
    public ReturnTransaction complete() {
          returnRecord = new ReturnTransaction(itemsAndQuantity, saleId, cashierId, shift, registerId, reason);
            String id = returnRecord.save(returnRecord);
            returnRecord =  returnRecord.getRecord(id);
            return returnRecord;
    }

    @Override
    public void recordPrint() {
        System.out.println("return id: "+returnRecord.getId() + "cashier id: " +returnRecord.getCashier().getId()+ " shift: "+returnRecord.getShift()+" level: "+returnRecord.getCashier().getLevel()+ " Register: "
                +returnRecord.getRegister().getRegisterId() + " sales amt: " + returnRecord.getTotalAmtBeforeTax() + " sales tax: " +returnRecord.getTotalTaxAmt()
                +" total amt: " +returnRecord.getTotalAmt() +" return time: " +returnRecord.getTransactionTime());
    }
}
