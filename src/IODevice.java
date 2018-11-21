/***
 * This class represents an IO device that handles any IO requests<br>
 * We chose to set this as a Thread class because in reality, the<br>
 * CPU does not wait for IO bursts to finish before going to the <br>
 * next process.
 */
public class IODevice extends Thread {
    private PCB currentProcess;

    public IODevice() {
        this.currentProcess = null;
    }

    @Override
    public void run() {
        //While OS is running, keep handling IO requests if available
        while(true) {
            if(currentProcess != null) {
                handleIOBurst();
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
    public void handleIOBurst() {
        //TODO check if we need to change the state *HERE*
        this.currentProcess.setProcessState(ProcessState.WAITING);

        //TODO change this to use Burst instead of attributes in PCB
        while(currentProcess.getIOCounter() > 0) {
            int IOCounter = currentProcess.getIOCounter();
            currentProcess.setIOCounter(IOCounter - 1);
            currentProcess.setIOTime(currentProcess.getIOCounter() + 1);

            //TODO remove debug
            System.out.println("IO Device handling..");
            try {
                //Wait for 1 millisecond
                sleep(1);
            } catch(InterruptedException e) {
                //An interrupt was received.
                return;
            }
        }

        //TODO put process to ready queue again
        this.currentProcess.setProcessState(ProcessState.READY);
    }

    public void setCurrentProcess(PCB currentProcess) {
        this.currentProcess = currentProcess;
    }

    public PCB getCurrentProcess() { return currentProcess; }
}
