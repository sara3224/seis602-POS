package edu.stthomas.repo;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;

/**
 * Open a file and add item details (replace existing item details)
 */
public class InventoryRepo {
    private static File inventory = new File("./data/" + "inventory.txt");
    public static void addItem(int itemId, int qty, double price, int threshold)  {
        if(itemExist(itemId)) {
            System.out.println("Item exists");//TODO: prevent from adding new entry
        }

        String item = itemId + "\t" + qty + "\t" + price +"\t" + threshold +"\n";

        try (FileWriter fw = new FileWriter(inventory,true);
             BufferedWriter writer = new BufferedWriter(fw)) {
            writer.write(item);
        } catch (IOException e) {
            System.out.println("item addition failed: "+item);
        }
    }

    private static boolean removeLine(String lineContent) throws IOException
    {
        File temp = new File(inventory.getAbsoluteFile() + ".temp");
        try (PrintWriter out = new PrintWriter(new FileWriter(temp))) {
            Files.lines(inventory.toPath())
                    .filter(line -> !line.contains(lineContent))
                    .forEach(out::println);
            out.flush();
        }
        return temp.renameTo(inventory);
    }


    static boolean itemExist(int itemId) {
        boolean exist = false;
        try (BufferedReader br = new BufferedReader(new FileReader(inventory))) {
            String st;
            String attributes[] = new String[5];
            while ((st = br.readLine()) != null) {
                attributes = st.split("\t");
                if (attributes[0].equals(Integer.toString(itemId))) {
                    exist = true;
                }
            }
        } catch (FileNotFoundException fnf) {
            System.out.println("item deletion failed: "+itemId);
        } catch(IOException io) {
            System.out.println("item deletion failed: "+itemId);
        }
        return exist;
    }

    public static void removeItem(int itemId) {
        try (BufferedReader br = new BufferedReader(new FileReader(inventory))) {
            String st;
            String attributes[] = new String[5];
            while ((st = br.readLine()) != null) {
                attributes = st.split("\t");
                if (attributes[0].equals(Integer.toString(itemId))) {
                    removeLine(st);
                }
            }
    } catch (FileNotFoundException fnf) {
            System.out.println("item deletion failed: "+itemId);
        } catch(IOException io) {
            System.out.println("item deletion failed: "+itemId);
        }
    }
}
