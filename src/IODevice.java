import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import Bursts.Burst;

/***
 * This class represents an IO device that handles any IO requests<br>
 * We chose to set this as a Thread class because in reality, the<br>
 * CPU does not wait for IO bursts to finish before going to the <br>
 * next process.
 */
public class IODevice extends Thread {
    private static PCB currentProcess;
    private static Queue<PCB> waitingList; //PCB in waiting state

    public IODevice() {
        currentProcess = null;
        waitingList = new ConcurrentLinkedQueue<>();
    }

    @Override
    public void run() {
        //While OS is running, keep handling IO requests if available
        while(true) {
        	currentProcess = waitingList.poll();

            if(currentProcess != null) {
                handleIORequest();
            } else {
                //sleep for 1 millisecond
                try {
                    sleep(1);
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void handleIORequest() {
        // Increment the process IO counter
        currentProcess.incrementIoCounter();

//        System.out.print("["+currentProcess.getPid()+"] (" + currentProcess.getCurrentBurst().getRemainingTime()+ ") -->\t");
        
        while(currentProcess.getCurrentBurst().getRemainingTime() > 0) {
            // Increment total IO time of process
            currentProcess.incrementIoTotalTime();

            // Decrement the process IO burst
            currentProcess.getCurrentBurst().decrementRemainingTime();

//            System.out.print(currentProcess.getCurrentBurst().getRemainingTime()+"\t");
            try {
                //Wait for 1 millisecond
                sleep(1);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
//        System.out.println();

        Burst nextBurst = currentProcess.nextBurst();

        if(nextBurst == null) {
            currentProcess.terminateProcess();
            return;
        }

        currentProcess.letProcessReady();
    }

    public void addProcessToDevice(PCB process) {
    	waitingList.add(process);
    }

    void killProcessFromIOQueue(PCB process) {
        if(waitingList.remove(process)) {
            // Remove its size from ram
            RAM.subtractFromUsage(process.getSize());

            // Set state to KILLED
            process.setProcessState(ProcessState.KILLED);

            // Add process to finished list in OS
            OperatingSystem.addFinishedProcess(process);

            System.out.println("(IO) - KILLED PROCESS [" +process.getPid()+ "].");
        }
    }

    PCB getMaxProcess() {
        Object[] list = waitingList.toArray();

        // Let the first process be the max size process
        PCB maxPCB = (PCB) list[0];

        for(int i = 1; i < list.length; i++) {
            PCB currentPCB = (PCB) list[i];

            // If current process size is greater than the max process
            if(currentPCB.getSize() > maxPCB.getSize())
                // Then, it is the new max process
                maxPCB = currentPCB;
        }

        return maxPCB;
    }

    boolean isEmpty() { return waitingList.isEmpty(); }
    PCB getCurrentProcess() { return currentProcess; }
    Queue<PCB> getWaitingList() { return waitingList; }
}
