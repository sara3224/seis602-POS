package edu.stthomas.service;

import edu.stthomas.enums.Shift;
import edu.stthomas.exceptions.POSException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PointOfReturnTest {
    @Test
    void cancelAll() throws  POSException{
        PointOfReturn pointOfReturn = new PointOfReturn("c5931582","1001", Shift.DAY,1,"");
        pointOfReturn.cancelAll();
        pointOfReturn.complete();
//        Assertions.assertThrows(POSException.class, ()->pointOfReturn.complete());
    }

    @Test
    void complete() {
    }
}
