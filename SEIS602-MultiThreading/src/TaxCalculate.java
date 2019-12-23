import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * program to read 1000000 Sales Records.csv and append total tax with a new file 1000000 Sales Records with tax total.csv
 */
public class TaxCalculate implements Runnable {
    private static File salesRecord = new File("./data/" + "1000000 Sales Records.csv");
    //output file
    private static File salesRecordwithTax = new File("./data/" + "1000000 Sales Records with tax total.csv");
    int splitNumber = 500000;
    private String threadName;
    private Thread t;
    private BufferedReader reader;
    BufferedWriter writer;
    FileWriter fw;
    List<String> split1;
    List<String> split2;

    public TaxCalculate(String name) {
        threadName = name;
        System.out.print("Creating thread:" + threadName);
    }

    /**
     * 0Region,1Country,2Item Type,3Sales Channel,4Order Priority,5Order Date,6Order ID,7Ship Date,8Units Sold,9Unit Price,
     * 10Unit Cost,11Total Revenue,12Total Cost,13Total Profit,14Tax Rate,15Total Tax Paid
     */
    @Override
    public void run() {
            if (reader == null) {
                try {
                    reader = new BufferedReader(new FileReader(salesRecord));
                    split1 = reader.lines().collect(Collectors.toList());
                    split2 = split1.subList(splitNumber + 1, split1.size());
                    split1 = split1.subList(1, splitNumber + 1);
                    System.out.println(threadName +": read rows");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

        if (fw == null) {
            try {
                fw = new FileWriter(salesRecordwithTax, true);
                System.out.println(threadName+": file writre created");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            if (writer == null) {
                writer = new BufferedWriter(fw);
                writer.write(
                        "Region,Country,Item Type,Sales Channel,Order Priority,Order Date,Order ID,Ship Date,Units Sold,Unit Price,Unit Cost,Total Revenue,Total Cost,Total Profit,Tax Rate,Total Tax Paid");
                System.out.println(threadName +": first row written");
            }

            if ("StartFromTop".equals(threadName)) {
                for (String line : split1) {
                    writeLineToFile(line);
                }
            } else if ("StartFromBottom".equals(threadName)) {
                for (String line : split2) {
                    writeLineToFile(line);
                }
            }
            writer.close();
            fw.close();
        } catch (FileNotFoundException fnf) {
            System.out.println("updates failed: " + fnf.getMessage());
        } catch (IOException io) {
            System.out.println("updates failed: " + io.getMessage());
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
            writer.write(newLine2);
        }
    }

    public void start() {
        System.out.println("Starting " + threadName);
        if (t == null) {
            t = new Thread(this, threadName);
            t.start();
        }
    }
}
