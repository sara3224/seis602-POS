package blockingqueue;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

/**
 * https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/BlockingQueue.html
 */
public class SalesTaxWriter implements  Runnable {
    private final BlockingQueue<String> queue;
    private static File salesRecordwithTax = new File("./data/" + "tax total using blocking queue.csv");
    private String threadName;
    private Thread t;
    BufferedWriter writer;
    FileWriter fw;
    static int count = 0;

    public SalesTaxWriter(String name, BlockingQueue blockingQueue) {
        threadName = name;
        queue = blockingQueue;
        System.out.print("Creating thread:" + threadName);
        try {
            fw = new FileWriter(salesRecordwithTax, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        System.out.println("Starting " + threadName);
        if (t == null) {
            t = new Thread(this, threadName);
            t.start();
        }
    }

    @Override
    public void run() {
        try {
            writer = new BufferedWriter(new FileWriter(salesRecordwithTax, true));
            //write first header row
            writer.write("Region,Country,Item Type,Sales Channel,Order Priority,Order Date,Order ID,Ship Date,Units Sold,Unit Price,Unit Cost,Total Revenue,Total Cost,Total Profit,Tax Rate,Total Tax Paid");
            while(true) { //keep listening for messages read from file.
                writeLineToFile(queue.take());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void writeLineToFile(String line) throws IOException {
        String[] attributes;
        String newLine2;
        attributes = line.split(",");
        newLine2 = "\n" + line.substring(0, line.length() - 1) + "6.90%";
        String totalTax = Helper.digit2Doubles(6.90 * Double.valueOf(attributes[8]) * Double.valueOf(
                attributes[9]) / 100);
        newLine2 += "," + totalTax;
        synchronized (this) {
            System.out.println(threadName+":"+ ++count);
            writer.write(newLine2);
        }
    }
}
