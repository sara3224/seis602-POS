package edu.stthomas.service;

import edu.stthomas.enums.Shift;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReportingServiceTest {

    @Test
    void reportX() {
        ReportingService reportingService = new ReportingService();
        reportingService.reportX("sara3224", Shift.DAY,"2019-12-11");
    }

    @Test
    void reportZ() {
    }
}
