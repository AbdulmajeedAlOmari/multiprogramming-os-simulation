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
//        super.start();
    }

    @Override
    public void run() {
        //While OS is running, keep handling IO requests if available
        while(true) {
        	currentProcess = waitingList.poll();
//        	System.out.println("IO Device Queue size: " + waitingList.size());
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

    /***
     * This method handles <strong>current process</strong> in the IO Device.<br/>
     * - Requirements:
     * <ul>
     *     <li>There is a current process in the IO Device.</li>
     * </ul>
     */
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
        currentProcess.setProcessState(ProcessState.READY);
        
        // Get next burst
        Burst nextBurst = currentProcess.nextBurst();
        
        if(nextBurst == null) {
        	 // Save the termination time of the process
        	currentProcess.setFinishedTime(Clock.getCurrentMs());

            System.out.println("Finsiehd --> " + currentProcess.getPid());
            // This process finished all of its bursts normally
            currentProcess.setProcessState(ProcessState.TERMINATED);
            
            RAM.subtractFromUsage(currentProcess.getSize());
            
            OperatingSystem.addFinishedProcess(currentProcess);
        } else {
        	if(currentProcess == null)
            	System.out.println("NULL EXCEPTION");
        	RAM.addToReadyQ(currentProcess);
        }
    }

    public void addProcessToDevice(PCB process) {
    	waitingList.add(process);
    }

    public void setCurrentProcess(PCB currentProcess) { IODevice.currentProcess = currentProcess; }
    public PCB getCurrentProcess() { return currentProcess; }
    Queue<PCB> getWaitingList() { return waitingList; }
}
