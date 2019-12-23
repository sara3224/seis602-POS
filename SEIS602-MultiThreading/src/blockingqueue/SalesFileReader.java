package blockingqueue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;

public class SalesFileReader implements Runnable {
    private final BlockingQueue<String> queue;
    private static File salesRecord = new File("./data/" + "1000000 Sales Records.csv");
    private String threadName;
    private Thread t;
    private BufferedReader reader;

    public SalesFileReader(String name, BlockingQueue<String> q) {
        threadName = name;
        queue = q;
        try {
            reader = new BufferedReader(new FileReader(salesRecord));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        List<String> allLines = reader.lines().collect(Collectors.toList());
            for(String line: allLines.subList(1, allLines.size())) {
                queue.add(line);
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
