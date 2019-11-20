package eu.stthomas.service;

import eu.stthomas.model.Cashier;
import eu.stthomas.model.Register;
import eu.stthomas.enums.Shift;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

/**
 * Responsible for activities for the actual sale at the register:
 * Input: Cashier Id
 * captures: itemsAndQuantity and quantity
 * processes itemsAndQuantity and quantity to calculate total payment amount
 * updates inventory of itemsAndQuantity on sale
 * trigger replenishment on each sale to order new itemsAndQuantity
 *
 */
public class PointOfSale {
    UUID salesId;
    Cashier cashier;
    Shift shift;
    Register register;
    PricingService pricingService = new PricingService();
    private Map<Integer, Integer> itemsAndQuantity = new HashMap<>();
    private static List<PointOfSale> sales = new ArrayList<>();
    private double salesAmt;
    private Date salesTime;

    public PointOfSale(Cashier cashier, Shift shift, Register register) {
        this.cashier = cashier;
        this.shift = shift;
        this.register = register;
        salesId = UUID.randomUUID();
    }

    /**
     * Overrides the quantity for item.
     * @param item_id
     * @param qty
     */
    public void addItem(Integer item_id, int qty) {
        itemsAndQuantity.put(item_id, qty);
    }

    public UUID getSalesId() {
        return salesId;
    }

    public Map<Integer, Integer> getItemsAndQuantity() {
        return itemsAndQuantity;
    }

    public void removeItem(Integer item_id) {
        itemsAndQuantity.remove(item_id);
    }

    //call the pricing service to get cost of each item and calculate total
    public void finalizeSale() {
        salesAmt = itemsAndQuantity.keySet().stream().mapToDouble(item_id -> pricingService.getPrice(item_id)* itemsAndQuantity
                .get(item_id)).sum();
        salesTime = new Date();
        sales.add(this);
    }

    public String getSalesTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("America/Chicago"));
        return sdf.format(salesTime);
    }

    public double getSalesAmt() {
        return salesAmt;
    }

    public static List<PointOfSale> getSales(){
        return sales;
    }

    public Register getRegister() {
        return register;
    }

    public Cashier getCashier() {
        return cashier;
    }
}
