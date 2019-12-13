package edu.stthomas.client;

import edu.stthomas.enums.Shift;
import edu.stthomas.exceptions.POSException;
import edu.stthomas.model.Register;
import edu.stthomas.model.SalesTransaction;
import edu.stthomas.repo.InventoryRepo;
import edu.stthomas.repo.SalesRepo;
import edu.stthomas.service.PointOfReturn;
import edu.stthomas.service.PointOfSale;
import edu.stthomas.service.ReportingService;
import edu.stthomas.service.User;

import java.util.Objects;
import java.util.Scanner;

//TODO: remove item and then return happens
//TODO: tax should be multiplied by 100 when displayed
public class Client {
    User user; //TODO level 1 can only do 11 i.e POS and returns. rest all i.e add inventory, remove and report level 2
    String registerId; //TODO: change to string
    Shift shift; //TODO: Change to String

    static Scanner myObj = new Scanner(System.in);
    public void login() {
        System.out.println("Welcome to SEIS 602 POS System!!!. Please enter credentials to get started....");
        String userId = getString("UserId:");
        String password = getString("Enter password");
        if(User.authenticate(userId,password)){
            System.out.println("*****Welcome to SEIS 602 POS system...you are authenticated as: " + userId + " Please enter shift and register*****\n");
            this.user = new User(userId);
            register();
        } else {
            System.out.println("Invalid userid and password combination..please re-enter credentials!!!");
            login();
        }
    }

    private void register() {
        registerId = getString("Please enter register in range of 1 to 10");
        if(!Register.registers.contains(registerId)) {
            register();
        }
        shift();
    }

    private void shift() {
        String inputShift = getString("Enter shift....day or night");
        if(inputShift.equalsIgnoreCase("day") || inputShift.equalsIgnoreCase("night")) {
            shift = Shift.valueOf(inputShift.toUpperCase());
        } else {
            shift();
        }
        start();
    }

    private int getInt(String desc) {
        System.out.println(desc);
        int input = 0;
        try {
            input = myObj.nextInt();
        }catch (Exception e) {
            throw new NumberFormatException("input should be a number");
        }
        return input;
    }

    public void start() {
        System.out.println(initialScreen());
        try {
            String choice = myObj.next();  // Read user input
            switch (choice) {
                case "1":
                    System.out.println("Add Item");
                    InventoryRepo.addItem(getString("enter item Id"), getString("enter item description"), getPositiveInt("enter on hands quantity"),getPositiveDouble("enter price"),
                            getPositiveDouble("enter tax percentage"), getPositiveInt("enter threshold"), getString("enter supplier id"), getPositiveInt("enter replenishment quantity"));
                    break;
                case "2":
                    System.out.println("Delete Item");
                    InventoryRepo.removeItem(getString("enter item Id:"));
                    break;
                case "11": //POS
                    System.out.println("Enter items and quantity");
                    PointOfSale pos = new PointOfSale(user.getId(), shift, registerId);
                    String next = null;
                    while(!Objects.equals("X", next)) {
                        String itemAdded = pos.addItem((getString("enter item id")), getInt("enter quantity"));
                        if(!itemAdded.equals("")) {
                            System.out.println(itemAdded);
                        } else {
                            System.out.println("press X to finalize POS or C to enter next item id");
                            next = myObj.next();
                            if ("X".equals(next)) {
                                try {
                                    pos.complete();
                                } catch (POSException e) {
                                    e.printStackTrace();
                                    break;
                                }
                            }
                        }
                    }
                    System.out.println("Thanks..transaction is done..");
                    break;
                case "21": //POS returns all sales
                    String salesId = getString("Enter sales Id:");
                    SalesTransaction salesTransaction = SalesRepo.getSalesRecordForReturns(salesId);
                    if(salesTransaction.getSalesLineItems().size() == 0) {
                        System.out.println("salesId: "+salesId +" does not exists, please enter a valid sales id for returning items");
                        break;
                    }
                    try {
                        PointOfReturn pointOfReturn = new PointOfReturn(salesId, user.getId(), shift, registerId,"");//TODO uncomment
                        pointOfReturn.cancelAll();
                        pointOfReturn.complete();
                    } catch (POSException e) {
                        System.out.println(e.getMessage());
                        break;
                    }
                    break;
                case "22":
                    salesId = getString("Enter sales Id:");
                    salesTransaction = SalesRepo.getSalesRecordForReturns(salesId);
                    if(salesTransaction.getSalesLineItems().size() == 0) {
                        System.out.println("salesId: "+salesId +" does not exists, please enter a valid sales id for returning items");
                        break;
                    }
                    PointOfReturn pointOfReturn = new PointOfReturn(salesId, user.getId(), shift, registerId,"");
                    String nextR = null;
                    while(!Objects.equals("X", nextR)) {
                        pointOfReturn.addItem((getString("enter item id")), getInt("enter quantity"));
                            System.out.println("press X to finalize return or C to enter next return item id");
                            nextR = myObj.next();
                            if (Objects.equals("X", nextR)) {
                                try {
                                    pointOfReturn.complete();
                                } catch (POSException e) {
                                    System.out.println(e.getMessage());
                                    break;
                                }
                        }
                    }
                    System.out.println("Thanks..return transaction is done..");
                    break;
                case "31":
                    System.out.println("Welcome to report X...");
                    String cashierid = getString("Enter Cashier id");
                    String shift = getString("Enter shift day or night");
                    String reportDate = getString("Enter date in 'YYYY-MM-DD' format");
                    ReportingService reportingService = new ReportingService();
                    reportingService.reportX(cashierid,Shift.valueOf(shift.toUpperCase()),reportDate);
                    break;
                case "32":
                    System.out.println("Welcome to report Z...");
                    shift = getString("Enter shift day or night");
                    reportDate = getString("Enter date in 'YYYY-MM-DD' format");
                    reportingService = new ReportingService();
                    reportingService.reportZ(Shift.valueOf(shift.toUpperCase()),reportDate);
                    break;
                case "X":
                    System.out.println("Good bye!");
                    System.exit(0);
                    break;
                default:
                    break;
            }
        }catch (Exception e) {
            e.printStackTrace();
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
    private static int getPositiveInt(String desc) {
        System.out.println(desc);
        int input = 0;
        try {
            input = myObj.nextInt();
            if (input <= 0) {
                throw new NumberFormatException();
            }
        }catch (Exception e) {
            throw new NumberFormatException("input should be a positive integer");
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

    /**
     * @return
     */
    private static double getPositiveDouble(String desc) {
        System.out.println(desc);
        double dbl = 0;
        try {
            dbl = myObj.nextDouble();
            if(dbl<0) {
                throw new NumberFormatException();
            }
        }catch (Exception e) {
            throw new NumberFormatException("input should be positive a double");
        }
        return dbl;
    }

    private String initialScreen() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Please select one of the following options\n");
        if(user.getLevel() == 1) {
            stringBuilder.append("1\tInventory -- add item \n");
            stringBuilder.append("2\tInventory -- delete item \n");
            stringBuilder.append("11\tPOS -- new sale\n");
            stringBuilder.append("21\tPOS -- returns..cancel all sales\n");
            stringBuilder.append("22\tPOS -- return..individual items\n");
        } else if (user.getLevel() == 2) {
            stringBuilder.append("31\tPOS -- report X. sales for a cashier with a shift and day\n");
            stringBuilder.append("32\tPOS -- report Z. sales for a shift and day\n");
        }
        stringBuilder.append("X\tTo Exit the Application\n");

        return stringBuilder.toString();
    }
}
