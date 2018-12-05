import Bursts.Burst;
import Bursts.CPUBurst;
import Bursts.IOBurst;

public class CPU extends Thread {
    private IODevice ioDevice;
    private PCB currentActiveProcess;
    private RAM ram;

    CPU(IODevice ioDevice, RAM ram) {
        this.ioDevice = ioDevice;
        this.ram = ram;
        this.currentActiveProcess = null;
    }

    @Override
    public void run() {
        while(true) {
            currentActiveProcess = RAM.getReadyQ().poll();

            if(currentActiveProcess != null) {
                handleCurrentProcess();
            } else {
                try {
                    sleep(1);
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }

                //This will increment the Clock by 1 millisecond
                Clock.incrementMs();
            }
        }
    }

    private void handleCurrentProcess() {
        // Rename the process for simplicity
        PCB process = this.currentActiveProcess;

        // Set its state to Running
        process.setProcessState(ProcessState.RUNNING);

        // Get its first burst
        Burst currentBurst = process.getCurrentBurst();

        // Increment the CPU Counter for this process
        process.incrementCpuCounter();

        // If it is CPU burst then process it
        while(currentBurst.getRemainingTime() > 0) {
            // Decrease the remaining time by 1 ms
            currentBurst.decrementRemainingTime();

            // TODO hook it with GUI

            // Increment the total time it was processed in CPU
            process.incrementCpuTotalTime();

            try {
                //Wait for 1 millisecond before proceeding
                sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // This will increment the Clock by 1 millisecond
            Clock.incrementMs();
        }

        // TODO handle processes memory size and manipulate it
        // TODO use Burst.getMemoryValue()
        
        // Get the next burst and load it in PCB
        Burst nextBurst = process.nextBurst();

        // Remove the process from RAM
//        ram.removeProcess(this.currentActiveProcess);
        
        
        if(nextBurst == null) {
            // Save the termination time of the process
            process.setFinishedTime(Clock.getCurrentMs());

            System.out.println("Finsiehd --> " + process.getPid());
            // This process finished all of its bursts normally
            process.setProcessState(ProcessState.TERMINATED);

            OperatingSystem.addFinishedProcess(process);
        } else {
        	// Handle IO Burst
        	
//        	System.out.println("["+process.getPid()+"] ("+process.getCurrentBurst().getRemainingTime()+") --> " + process.getPid());
            // The next burst is IO, so change its state to WAITING
            process.setProcessState(ProcessState.WAITING);
            ioDevice.addProcessToDevice(this.currentActiveProcess);
        }
        
        this.currentActiveProcess = null;
    }
}
