package edu.stthomas;

import edu.stthomas.enums.Shift;
import edu.stthomas.model.Cashier;
import edu.stthomas.model.Register;
import edu.stthomas.service.PointOfSale;

import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String args[]) {
        Cashier cashier = new Cashier(1001);
        Register register = new Register(1);
        PointOfSale pos = new PointOfSale(cashier, Shift.DAY, register);
        pos.addItem(1, 2);
        pos.addItem(2, 2);
        pos.removeItem(2);
        pos.addItem(3,5);
        pos.finalizeSale();

        cashier = new Cashier(1002);
        register = new Register(2);
        pos = new PointOfSale(cashier, Shift.NIGHT, register);
        pos.addItem(1, 1);
        pos.addItem(2, 4);
        pos.removeItem(1);
        pos.addItem(3,5);
        pos.finalizeSale();


        List<PointOfSale> sales = PointOfSale.getSales();
        for(PointOfSale sale: sales) {
            System.out.println("cashier id: " +sale.getCashier().getId()+ " level: "+sale.getCashier().getLevel()+ " Register: "
                    +sale.getRegister().getRegisterId() + " sales amt: " + sale.getSalesAmt() + " sales time: " +sale.getSalesTime()
            +" sales id: "+sale.getSalesId());
            Map<Integer, Integer> salesAndItems = sale.getItemsAndQuantity();
                for(Integer item: salesAndItems.keySet()) {
                    System.out.println("item id:"+item+" quantity:"+salesAndItems.get(item));
                }
                System.out.println();
        }

    }
}
