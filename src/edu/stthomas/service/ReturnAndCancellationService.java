package edu.stthomas.service;

import java.util.List;
import java.util.UUID;

/**
 * For a given sales id:
 * 1. cancel all returns total sale amount
 * 2. return items and quantity
 */
public class ReturnAndCancellationService {



    public double cancelSale(UUID saleId) {
        PointOfSale refundPos = null;
        double refundAmt = 0.0;
        for(PointOfSale pos: PointOfSale.getSales()) {
            if (pos.getSalesId().equals(saleId)) {
                refundAmt = pos.getSalesAmt();
                pos.setRefunded(true);
            }
        }
        return refundAmt;
    }
}
