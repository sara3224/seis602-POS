package edu.stthomas;

import edu.stthomas.enums.Shift;
import edu.stthomas.model.SalesLineItem;
import edu.stthomas.model.SalesRecord;
import edu.stthomas.repo.SalesRepo;
import edu.stthomas.service.PointOfSale;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
        pos.finalizeSale();

        pos = new PointOfSale(1002, Shift.NIGHT, 2);
        pos.addItem(1, 1);
        pos.addItem(2, 4);
        pos.removeItem(1);
        pos.addItem(3,3);
        pos.finalizeSale();

        //3.	The system will keep track of the amount of sales ($) at each register for each cashier
        salesDetails();
        reportZ("2019-11-25");
        reportX(1001,Shift.DAY,"2019-11-24");
    }

    private static void salesDetails() {
        Collection<SalesRecord> sales = SalesRepo.getSales();
        System.out.println("Sales report");
        generateReport(sales);
    }

    private static void generateReport(Collection<SalesRecord> sales) {
        for(SalesRecord sale: sales) {
            System.out.println("cashier id: " +sale.getCashier().getId()+ " shift: "+sale.getShift()+" level: "+sale.getCashier().getLevel()+ " Register: "
                    +sale.getRegister().getRegisterId() + " sales amt: " + sale.getTotalSalesAmt() + " sales tax: " +sale.getTotalTaxAmt()
                    +" total amt: " +sale.getTotalAmt()
                    +" sales time: " +sale.getSalesTime() +" sales id: "+sale.getSalesId());

            List<SalesLineItem> salesLineItems = sale.getSalesLineItems();
                for(SalesLineItem lineItem: salesLineItems) {
                    //4.	Registers will record the register number, the user (cashier), the dates and times of sale, sale items, and the amount of sales.
                    System.out.println("item id:"+lineItem.getItemId()+" quantity:"+lineItem.getQuantity() + " price: "+lineItem.getPrice()
                    +" tax:" +lineItem.getTax() +" sale amt: "+lineItem.getLineItemSale() +" sale tax: "+lineItem.getLineItemTax()
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
        Collection<SalesRecord> sales = SalesRepo.getSales();
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
    private static void reportZ(String reportDate) {
            Collection<SalesRecord> sales = SalesRepo.getSales();
            System.out.println("Sales report");
            sales = sales.stream()
                    .filter(salesRecord -> {
                        try {
                            return isSameDay(new SimpleDateFormat("yyy-MM-dd").parse(reportDate), salesRecord);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        return false;
                    })
                    .collect(
                            Collectors.toList());
        generateReport(sales);
    }

    private static boolean isSameDay(Date reportDate2, SalesRecord salesRecord) {
        Calendar requestedDate = Calendar.getInstance();
        requestedDate.setTime(reportDate2);

        Calendar salesRecordCal = Calendar.getInstance();
        salesRecordCal.setTime(salesRecord.getSalesTime());

        return requestedDate.get(Calendar.DAY_OF_YEAR) == salesRecordCal.get(Calendar.DAY_OF_YEAR) &&
                requestedDate.get(Calendar.YEAR) == salesRecordCal.get(Calendar.YEAR);
    }
}
