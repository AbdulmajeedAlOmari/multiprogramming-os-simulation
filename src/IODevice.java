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
    private Queue<PCB> waitingList;
    private boolean isRunning;

    public IODevice() {
        this.currentProcess = null;
        this.waitingList = new LinkedList<>();
        this.isRunning = false;
        super.start();
    }

    @Override
    public void run() {
        //TODO check whether we need (isRunning) flag or shall we use (synchronized) keyword only
        //While OS is running, keep handling IO requests if available
        while(true) {
            if(this.currentProcess != null && !this.isRunning) {
                handleIORequest();
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
        //Flag for the run method that it is processing something
        this.isRunning = true;

        //TODO use correct attributes from PCB
        while(currentProcess.getIOCounter() > 0) {
            int IOCounter = currentProcess.getIOCounter();
            currentProcess.setIOCounter(IOCounter - 1);
            currentProcess.setIOTime(currentProcess.getIOCounter() + 1);

            try {
                //Wait for 1 millisecond
                sleep(1);
            } catch(InterruptedException e) {
                e.printStackTrace();
                return;
            }
        }

        //TODO put process to ready queue again
        this.currentProcess.setProcessState(ProcessState.READY);

        //TODO ask Ahmad about this
        // Get next waiting process
        if(waitingList.size() > 0) {
            this.currentProcess = waitingList.poll();
        }

        //Flag for the run method that it finished the processing
        this.isRunning = false;
    }

    public void addProcessToDevice(PCB process) {
        // Set process state as (Waiting)
        process.setProcessState(ProcessState.WAITING);

        if(this.currentProcess == null)
            this.currentProcess = process;
        else
            waitingList.add(process);

    }

    public void setCurrentProcess(PCB currentProcess) { this.currentProcess = currentProcess; }
    public PCB getCurrentProcess() { return currentProcess; }
}
