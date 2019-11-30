package edu.stthomas.repo;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Open a file and add item details (replace existing item details)
 */
public class InventoryRepo {
    public static void addItem(int itemId, int qty, double price, int threshold)  {

        String item = itemId + "\t" + qty + "\t" + price +"\t" + threshold +"\n";

        try (FileWriter fw = new FileWriter("./" + "inventory.txt",true);
             BufferedWriter writer = new BufferedWriter(fw)) {
            writer.write(item);
        } catch (IOException e) {
            System.out.println("item addtion failed: "+item);
        }
    }
}
