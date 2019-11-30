package edu.stthomas;

import edu.stthomas.client.Client;
import edu.stthomas.enums.Shift;
import edu.stthomas.model.ReturnLineItem;
import edu.stthomas.model.ReturnTransaction;
import edu.stthomas.model.SalesLineItem;
import edu.stthomas.model.SalesTransaction;
import edu.stthomas.repo.ReturnsRepo;
import edu.stthomas.repo.SalesRepo;
import edu.stthomas.service.PointOfReturn;
import edu.stthomas.service.PointOfSale;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 TODO:during sale: Invalid item id on POS system., invalid register
 TODO:during return: validate if sales id is valid, qty is less or equal to sold item qty, avoid second return, avoid multiple returns, support return reason at item level.
TODO:extract POS and POR in abstract class and/or interface.
TODO:Check for inventory before allowing to enter quantity.
TODO:No item id found during sale. Enter 9999 item id or do not allow to add?
TODO:Default check for item exists.
TODO:Search inventory(Optional)
TODO:Refactoring of class names and repackaging
TODO:Add some welcome header, store name, etc
 */
public class Main {
    public static void main(String[] args) {

        //1.	Allow the cashier to start a new sale and allow add/remove items to a new sale
        PointOfSale pos = new PointOfSale(1001, Shift.DAY, 1);
        pos.addItem(1, 2);
        pos.addItem(2, 2);
        pos.removeItem(2);
        pos.addItem(3,5);
        pos.addItem(3,1);//correction in the sale quantity
        //2.	Once all items are added to the sale the cashier will request for cash to finalize the sale.
        pos.complete();

        pos = new PointOfSale(1002, Shift.NIGHT, 2);
        pos.addItem(1, 1);
        pos.addItem(2, 4);
        pos.removeItem(1);
        pos.addItem(3,3);
        pos.complete();

        //3.	The system will keep track of the amount of sales ($) at each register for each cashier
        salesDetails();
        reportZ(Shift.NIGHT, "2019-11-28");
        reportX(1001,Shift.DAY,"2019-11-26");

        //refund
        PointOfReturn por = new PointOfReturn(1, 1,Shift.DAY,1, "extra purchased");
        por.addItem(1,1);
        por.complete();

        //refund all
        PointOfReturn por2 = new PointOfReturn(2, 2,Shift.NIGHT,2, "short of cash");
        por2.cancelAll();
        por2.complete();
        generateReturnReport();
        Client.start();
    }

    private static void generateReturnReport() {
        Map<Integer, ReturnTransaction> returns = ReturnsRepo.getAllReturns();
        System.out.println("Return report:");
        returns.forEach((key,record) -> {
            System.out.print(key);
            System.out.println(" sales id: "+ record.getSalesId()+ " return id:"+record.getId() +" cashier id: " +record.getCashier().getId()+ " shift: "
                    +record.getShift()+" level: "+record.getCashier().getLevel()+ " Register: "
                    +record.getRegister().getRegisterId() + " sales amt: " + record.getTotalAmtBeforeTax() + " sales tax: " +record.getTotalTaxAmt()
                    +" total amt: " +record.getTotalAmt() +" sales time: " +record.getTransactionTime());

            List<ReturnLineItem> returnLineItems = record.getReturnLineItems();
            for(ReturnLineItem lineItem: returnLineItems) {
                System.out.println("item id:"+lineItem.getItemId()+" quantity:"+lineItem.getQuantity() + " price: "+lineItem.getPrice()
                        +" tax:" +lineItem.getTax() +" sale amt: "+lineItem.getLineItemAntBeforeTax() +" sale tax: "+lineItem.getLineItemTax()
                        +" total amt: "+lineItem.getLineItemAmt()+ " reason: "+lineItem.getReason());
            }
            System.out.println();
        });
    }

    private static void salesDetails() {
        Collection<SalesTransaction> sales = SalesRepo.getSales();
        System.out.println("Sales report...excludes returns");
        generateReport(sales);
    }

    private static void generateReport(Collection<SalesTransaction> sales) {
        for(SalesTransaction sale: sales) {
            System.out.println(" sales id: "+sale.getId()+"cashier id: " +sale.getCashier().getId()+ " shift: "+sale.getShift()+" level: "+sale.getCashier().getLevel()+ " Register: "
                    +sale.getRegister().getRegisterId() + " sales amt: " + sale.getTotalAmtBeforeTax() + " sales tax: " +sale.getTotalTaxAmt()
                    +" total amt: " +sale.getTotalAmt()
                    +" sales time: " +sale.getTransactionTime());

            List<SalesLineItem> salesLineItems = sale.getSalesLineItems();
                for(SalesLineItem lineItem: salesLineItems) {
                    //4.	Registers will record the register number, the user (cashier), the dates and times of sale, sale items, and the amount of sales.
                    System.out.println("item id:"+lineItem.getItemId()+" quantity:"+lineItem.getQuantity() + " price: "+lineItem.getPrice()
                    +" tax:" +lineItem.getTax() +" sale amt: "+lineItem.getLineItemAntBeforeTax() +" sale tax: "+lineItem.getLineItemTax()
                    +" total amt: "+lineItem.getLineItemAmt());
                }
                System.out.println();
        }
    }

    /**
     *
     * @param reportDate
     */
    private static void reportX(int cashierId, Shift shift, String reportDate) {
        Collection<SalesTransaction> sales = SalesRepo.getSales();
        System.out.println("Sales report");
        sales = sales.stream()
                .filter(salesRecord -> {
                    try {
                        boolean isSameCashier = salesRecord.getCashier().getId() == cashierId;
                        boolean shiftMatch = salesRecord.getShift().equals(shift);
                        return isSameDay(new SimpleDateFormat("yyy-MM-dd").parse(reportDate), salesRecord) && isSameCashier && shiftMatch;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return false;
                }).collect(Collectors.toList());
        generateReport(sales);
    }

    /**
     *
     * @param reportDate
     */
    private static void reportZ(Shift shift, String reportDate) {
        Collection<SalesTransaction> sales = SalesRepo.getSales();
        System.out.println("Sales Z report ");
        sales = sales.stream()
                .filter(salesRecord -> {
                    try {
                        boolean shiftMatch = salesRecord.getShift().equals(shift);
                        return isSameDay(new SimpleDateFormat("yyy-MM-dd").parse(reportDate), salesRecord) && shiftMatch;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return false;
                })
                .collect(Collectors.toList());
        generateReport(sales);
    }

    private static boolean isSameDay(Date reportDate2, SalesTransaction salesRecord) {
        Calendar requestedDate = Calendar.getInstance();
        requestedDate.setTime(reportDate2);

        Calendar salesRecordCal = Calendar.getInstance();
        salesRecordCal.setTime(salesRecord.getTransactionTime());

        return requestedDate.get(Calendar.DAY_OF_YEAR) == salesRecordCal.get(Calendar.DAY_OF_YEAR) &&
                requestedDate.get(Calendar.YEAR) == salesRecordCal.get(Calendar.YEAR);
    }
}
