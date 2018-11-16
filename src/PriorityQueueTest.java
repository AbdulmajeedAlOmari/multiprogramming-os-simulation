import java.util.ArrayList;
import java.util.Collection;
import java.util.PriorityQueue;

public class PriorityQueueTest {

    public static void main(String[] args) {
        PriorityQueue<PCB> processes = new PriorityQueue<>();

        // Those 6 processes arrived at the same time.
        PCB process1 = new PCB(1, 0, 30);
        PCB process2 = new PCB(2, 0, 4);
        PCB process3 = new PCB(3, 0, 7);
        PCB process4 = new PCB(4, 0, 43);
        PCB process5 = new PCB(5, 0, 37);
        PCB process6 = new PCB(6, 0, 32);


        // So, we will test this condition in PriorityQueue.
        ArrayList<PCB> collection = new ArrayList<>();
        collection.add(process1);
        collection.add(process2);
        collection.add(process3);
        collection.add(process4);
        collection.add(process5);
        collection.add(process6);

        // NOTE: go check PCB.java:15 , you will find compareTo() method
        // this method will determine which process has more priority
        processes.addAll(collection);

        int i = 1;

        // The output must be: [2] -> [3] -> [1] -> [6] -> [5] -> [4]
        while(processes.size() > 0) {
            PCB process = processes.poll();
            System.out.println(i + ". [" + process.getPid() + "]");
            i++;
        }
        /***
         * Output (CORRECT!!!):
         * 1. [2]
         * 2. [3]
         * 3. [1]
         * 4. [6]
         * 5. [5]
         * 6. [4]
         */
    }
}
