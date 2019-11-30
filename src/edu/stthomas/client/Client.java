package edu.stthomas.client;

import edu.stthomas.repo.InventoryRepo;

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
                    InventoryRepo.addItem(getInt("enter item Id:"),getInt("enter quantity"),getDouble("enter price"), getInt("enter threshold"));
                    break;
                case "2":
                    System.out.println("Delete Inventory");
                    InventoryRepo.removeItem(getInt("enter item Id:"));
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
        stringBuilder.append("X\tTo Exit the Application\n");
        stringBuilder.append(" \tSelection:\n");

        return stringBuilder.toString();
    }
}
