package edu.stthomas.client;

import edu.stthomas.enums.Shift;
import edu.stthomas.repo.InventoryRepo;
import edu.stthomas.service.PointOfSale;

import java.util.Scanner;

public class Client {
    static Scanner myObj = new Scanner(System.in);
    public static void start() {
        System.out.println(initialScreen());
        try {
            String choice = myObj.nextLine();  // Read user input
            switch (choice) {
                case "1":
                    System.out.println("Add Inventory");
                    InventoryRepo.addItem(getInt("enter item Id:"),getInt("enter quantity"),getDouble("enter price"),
                            getDouble("enter tax percentage"), getInt("enter threshold"));
                    break;
                case "2":
                    System.out.println("Delete Inventory");
                    InventoryRepo.removeItem(getInt("enter item Id:"));
                    break;
                case "11": //POS
                    System.out.println("Enter cashier and register details");
                    PointOfSale pos = new PointOfSale(getInt("enter cashier id"), Shift.valueOf(getString("Enter shift day or night").toUpperCase()),
                            getInt("Enter register id"));
                    String next = null;
                    while(!"X".equals(next)) {
                        pos.addItem((getInt("enter item id")), getInt("enter quantity"));
                        System.out.println("press X to finalize POS or C to enter next item id");
                        next = myObj.next();
                        if("X".equals(next)) {
                            pos.complete();
                        }
                    }
                    System.out.println("Thanks..transaction is done..");
                    break;
                case "X":
                    System.out.println("Good bye!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Please enter one of the given options");
                    start();
                    break;
            }
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println("");
        //repeat until option X is selected
        start();
    }

    /**
     * @return
     */
    private static String getString(String desc) {
        System.out.println(desc);
        String input = "";
        try {
            input = myObj.next();
        }catch (Exception e) {
            throw new NumberFormatException("input should be a integer");
        }
        return input;
    }

    /**
     * @return
     */
    private static int getInt(String desc) {
        System.out.println(desc);
        int input = 0;
        try {
            input = myObj.nextInt();
        }catch (Exception e) {
            throw new NumberFormatException("input should be a integer");
        }
        return input;
    }

    /**
     * @return
     */
    private static double getDouble(String desc) {
        System.out.println(desc);
        double dbl = 0;
        try {
            dbl = myObj.nextDouble();
        }catch (Exception e) {
            throw new NumberFormatException("inpout should be a double");
        }
        return dbl;
    }

    private static String initialScreen() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Please select one of the following options\n");
        stringBuilder.append("1\tInventory -- add item \n");
        stringBuilder.append("2\tInventory -- delete item \n");
        stringBuilder.append("11\tPOS -- new sale\n");
        stringBuilder.append("X\tTo Exit the Application\n");

        return stringBuilder.toString();
    }
}
