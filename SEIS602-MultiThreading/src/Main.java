import blockingqueue.SalesFileReader;
import blockingqueue.SalesTaxWriter;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;

public class Main {
    public static void main(String[] args) {


//        TaxCalculate taxCalculate = new TaxCalculate("StartFromTop");
//        taxCalculate.start();
//
//        TaxCalculate taxCalculate2 = new TaxCalculate("StartFromBottom");
//        taxCalculate2.start();


        BlockingQueue<String> q = new ArrayBlockingQueue(1000000*2);

        SalesFileReader reader = new SalesFileReader("reader", q);
        reader.start();

        SalesTaxWriter writer = new SalesTaxWriter("first ",q);
        writer.start();
        SalesTaxWriter writer2 = new SalesTaxWriter("second ",q);
        writer2.start();


    }
}
