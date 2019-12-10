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
        System.out.println("Report X -Shift: " +shift);
        System.out.println("Cashier:"+cashierId);

        double totalSalesIncludingTax = sales.stream().mapToDouble(SalesTransaction::getTotalAmtReport).sum();
        System.out.println("Total Sales(including tax):" +totalSalesIncludingTax);
        System.out.println("Total transactions :" +sales.size());
    }

    private void generateReport(Collection<SalesTransaction> sales) {
        for(SalesTransaction sale: sales) {
            System.out.println(" sales id: "+sale.getId()+"cashier id: " +sale.getCashier().getId()+ " shift: "+sale.getShift()+" level: "+sale.getCashier().getLevel()+ " Register: "
                    +sale.getRegister().getRegisterId() + " sales amt: " + sale.getTotalAmtBeforeTax() + " sales tax: " +sale.getTotalTaxAmt()
                    +" total amt: " +sale.getTotalAmt()
                    +" sales time: " +sale.getTransactionTime());
            System.out.println();
        }
    }
}
