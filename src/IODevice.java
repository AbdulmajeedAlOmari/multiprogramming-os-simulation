import java.util.LinkedList;
import java.util.Queue;

/***
 * This class represents an IO device that handles any IO requests<br>
 * We chose to set this as a Thread class because in reality, the<br>
 * CPU does not wait for IO bursts to finish before going to the <br>
 * next process.
 */
public class IODevice extends Thread {
    private PCB currentProcess;
    private Queue<PCB> waitingList; //PCB in waiting state

    public IODevice() {
        this.currentProcess = null;
        this.waitingList = new LinkedList<>();
        super.start();
    }

    @Override
    public void run() {
        //While OS is running, keep handling IO requests if available
        while(true) {
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

        while(currentProcess.getCurrentBurst().getRemainingTime() > 0) {
            // Increment total IO time of process
            currentProcess.incrementIoTotalTime();

            // Decrement the process IO burst
            currentProcess.getCurrentBurst().decrementRemainingTime();

            try {
                //Wait for 1 millisecond
                sleep(1);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }

        this.currentProcess.setProcessState(ProcessState.READY);
        //TODO add the process to RAM again


        // Get next waiting process
        if(waitingList.size() > 0) {
            this.currentProcess = waitingList.poll();
        }
    }

    public void addProcessToDevice(PCB process) {
        if(this.currentProcess == null)
            this.currentProcess = process;
        else
            waitingList.add(process);

    }

    public void setCurrentProcess(PCB currentProcess) { this.currentProcess = currentProcess; }
    public PCB getCurrentProcess() { return currentProcess; }
    Queue<PCB> getWaitingList() { return waitingList; }
}
