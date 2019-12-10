package edu.stthomas.service;

import edu.stthomas.enums.Shift;
import edu.stthomas.model.SalesTransaction;
import edu.stthomas.repo.SalesRepo;

import java.util.Collection;

public class ReportingService {
    /**
     *
     * @param reportDate
     */
    public void reportX(String cashierId, Shift shift, String reportDate) {
        Collection<SalesTransaction> sales = SalesRepo.getSalesForReportX(cashierId, shift, reportDate);
        System.out.println("*****Report X -Shift: " +shift+ "*****");
        System.out.println("Cashier:"+cashierId);

        double totalSalesIncludingTax = sales.stream().mapToDouble(SalesTransaction::getTotalAmtReport).sum();
        System.out.println("Total Sales(including tax):" +totalSalesIncludingTax);
        System.out.println("Total transactions :" +sales.size());
    }

    public void reportZ(Shift shift, String reportDate) {
        Collection<SalesTransaction> sales = SalesRepo.getSalesForReportZ(shift, reportDate);
        System.out.println("*****Report Z -Shift: " +shift+ "*****");

        double reportZSalesIncludingTax = sales.stream().mapToDouble(SalesTransaction::getTotalAmtReport).sum();
        //display all combined
        System.out.println("Total Z Sales(including tax):" +reportZSalesIncludingTax);
        System.out.println("Total transactions:" +sales.size());
        //display each sales transaction report
        for(SalesTransaction salesTransaction: sales) {
            System.out.println("****************");
            System.out.println("Cashier:"+salesTransaction.getCashier().getId());
            double totalSalesIncludingTax = salesTransaction.getTotalAmtReport();
            System.out.println("Sales(including tax):" +totalSalesIncludingTax);
        }
    }
}
