package edu.stthomas.service;

import edu.stthomas.model.SalesLineItem;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * For a given sales id:
 * 1. cancel all returns total sale amount
 * 2. return items and quantity
 */
public class ReturnAndCancellationService {

    public double cancelSale(UUID saleId) {
//        PointOfSale refundPos = null;
        double refundAmt = 0.0;
//        for(PointOfSale pos: PointOfSale.getSales()) {
//            if (pos.getSalesId().equals(saleId)) {
//                refundAmt = pos.getSalesAmt();
//                pos.setRefunded(true);
//            }
//        }
        return refundAmt;
    }


    public double refundAmt(UUID saleId, List<SalesLineItem> salesLineItems) {
//        Optional<PointOfSale> optionalPos = PointOfSale.getSales().stream().filter(sale -> sale.getSalesId().equals(saleId)).findFirst();
//        PointOfSale pos = optionalPos.get();
////        List<SalesLineItem> salesLineItems = pos.getSalesLineItems();
        return 0;
    }
}
