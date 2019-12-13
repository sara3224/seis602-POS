package edu.stthomas.service;

import edu.stthomas.enums.Shift;
import edu.stthomas.helper.Helper;
import edu.stthomas.model.ReturnTransaction;
import edu.stthomas.model.SalesTransaction;
import edu.stthomas.repo.ReturnsRepo;
import edu.stthomas.repo.SalesRepo;

import java.util.Collection;

public class ReportingService {
    /**
     *
     * @param reportDate
     */
    public void reportX(String cashierId, Shift shift, String reportDate) {

        System.out.println("*****Report X -Shift: " +shift+ "*****");
        System.out.println("Cashier:"+cashierId);

        Collection<SalesTransaction> sales = SalesRepo.getSalesForReportX(cashierId, shift, reportDate);
        double totalSalesIncludingTax = sales.stream().mapToDouble(SalesTransaction::getTotalAmtReport).sum();
        System.out.println("Total Sales (including tax):" + Helper.digit2Doubles(totalSalesIncludingTax));
        System.out.println("Total transactions :" +sales.size());


        Collection<ReturnTransaction> returnTransactions = ReturnsRepo.getReturnForReportX(cashierId, shift, reportDate);
        double totalReturnIncludingTax = returnTransactions.stream().mapToDouble(ReturnTransaction::getTotalAmtReport).sum();
        System.out.println("\nTotal returns (including tax):" + Helper.digit2Doubles(totalReturnIncludingTax));
        System.out.println("Total transactions :" +returnTransactions.size());
    }

    public void reportZ(Shift shift, String reportDate) {
        System.out.println("*****Report Z -Shift: " +shift+ "*****");

        Collection<SalesTransaction> sales = SalesRepo.getSalesForReportZ(shift, reportDate);
        double reportZSalesIncludingTax = sales.stream().mapToDouble(SalesTransaction::getTotalAmtReport).sum();
        //display all combined
        System.out.println("Total Z Sales(including tax):" +Helper.twoDigitNoRound(reportZSalesIncludingTax));
        System.out.println("Total transactions:" +sales.size());
        //display each sales transaction report
        for(SalesTransaction salesTransaction: sales) {
            System.out.println("****************");
            System.out.println("Cashier:"+salesTransaction.getCashier().getId());
            double totalSalesIncludingTax = salesTransaction.getTotalAmtReport();
            System.out.println("Sales(including tax):" + Helper.digit2Doubles(totalSalesIncludingTax));
        }

        Collection<ReturnTransaction> returnTransactions = ReturnsRepo.getReturnsForReportZ(shift, reportDate);
        double reportZReturnsIncludingTax = returnTransactions.stream().mapToDouble(ReturnTransaction::getTotalAmtReport).sum();
        //display all combined
        System.out.println("\nTotal Z Returns(including tax):" +Helper.twoDigitNoRound(reportZReturnsIncludingTax));
        System.out.println("Total transactions:" +returnTransactions.size());
        //display each return transaction report
        for(ReturnTransaction returnTransaction: returnTransactions) {
            System.out.println("****************");
            System.out.println("Cashier:"+returnTransaction.getCashier().getId());
            double totalReturnIncludingTax = returnTransaction.getTotalAmtReport();
            System.out.println("Returns (including tax):" + Helper.digit2Doubles(totalReturnIncludingTax));
        }
    }
}
