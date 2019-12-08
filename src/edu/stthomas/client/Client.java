package edu.stthomas.client;

import edu.stthomas.enums.Shift;
import edu.stthomas.exceptions.POSException;
import edu.stthomas.repo.InventoryRepo;
import edu.stthomas.service.PointOfSale;
import edu.stthomas.service.User;

import java.util.Scanner;

public class Client {
    User user;
    Integer registerId; //TODO: change to string
    Shift shift; //TODO: Change to String

    static Scanner myObj = new Scanner(System.in);

    public void login() {
        System.out.println("Welcome to SEIS 602 POS System!!!. Please enter credentials to get started....");
        String userId = getString("UserId:");
        String password = getString("Enter password");
        if(User.authenticate(userId,password)){
            System.out.println("*****Welcome to POS system...you are authenticated as: " + userId + " Please enter shift and register*****\n");
            this.user = new User(userId);
            register();
        } else {
            System.out.println("Invalid userid and password combination..please re-enter credentials!!!");
            login();
        }
    }

    private void register() {
        try {
            registerId = getInt("Enter register");
            shift();
        } catch (Exception e) {
            System.out.println("Enter number for register id");
//            register();
        }
    }

    private void shift() {
        try {
            shift = Shift.valueOf(getString("Enter shift").toUpperCase());
            start();
        } catch(Exception e){
            System.out.println("Enter either day or night for shift");
//            shift();
        }
    }

    //TODO: report X and report Y by one level 2
    private void start() {
        System.out.println(initialScreen());
        try {
            String choice = myObj.nextLine();  // Read user input
            switch (choice) {
                case "1":
                    System.out.println("Add Item");
                    InventoryRepo.addItem(getInt("enter item Id:"),getString("enter item description"), getInt("enter on hands quantity"),getDouble("enter price"),
                            getDouble("enter tax percentage"), getInt("enter threshold"), getInt("enter supplier id"), getInt("enter replenishment quantity"));
                    break;
                case "2":
                    System.out.println("Delete Item");
                    InventoryRepo.removeItem(getInt("enter item Id:"));
                    break;
                case "11": //POS
                    System.out.println("Enter items and quantity");
//                    PointOfSale pos = new PointOfSale(getInt("enter cashier id"), Shift.valueOf(getString("Enter shift day or night").toUpperCase()),
//                            getInt("Enter register id"));
                    PointOfSale pos = new PointOfSale(user.getId(), shift,registerId);
                    String next = null;
                    while(!"X".equals(next)) {
                        String itemAdded = pos.addItem((getInt("enter item id")), getInt("enter quantity"));
                        if(!itemAdded.equals("")) {
                            System.out.println(itemAdded);
                        } else {
                            System.out.println("press X to finalize POS or C to enter next item id");
                            next = myObj.next();
                            if ("X".equals(next)) {
                                try {
                                    pos.complete();
                                } catch (POSException e) {
                                    System.out.println(e.getMessage());
                                    break;
                                }
                            }
                        }
                    }
                    System.out.println("Thanks..transaction is done..");
                    break;
                case "X":
                    System.out.println("Good bye!");
                    System.exit(0);
                    break;
                default:
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
            throw new NumberFormatException("input should be a string");
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
