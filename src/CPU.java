import Bursts.Burst;
import Bursts.CPUBurst;
import Bursts.IOBurst;

public class CPU extends Thread {
    private IODevice ioDevice;
    private PCB currentActiveProcess;

    CPU() {
        this.ioDevice = new IODevice();
        this.currentActiveProcess = null;
        start();
    }

    @Override
    public void run() {
        while(true) {
            currentActiveProcess = RAM.retrieve();

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

        // Check if it is an IO burst or CPU burst
        if(currentBurst instanceof IOBurst) {
            ioDevice.addProcessToDevice(process);
            this.currentActiveProcess = null;
            return;
        }

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

        if(nextBurst == null) {
            // Save the termination time of the process
            process.setFinishedTime(Clock.getCurrentMs());

            //TODO add the process to Finished list in OperatingSystem

            // This process finished all of its bursts normally
            process.setProcessState(ProcessState.TERMINATED);
        } else if(nextBurst instanceof IOBurst) {
            // The next burst is IO, so change its state to WAITING
            process.setProcessState(ProcessState.WAITING);
            ioDevice.addProcessToDevice(this.currentActiveProcess);
        } else if (nextBurst instanceof CPUBurst) {
            // TODO check if we need this statement
            // Since the next burst is also CPU, we will continue processing it
            return;
        }

        //Remove from ram and re add it again
//        RAM.deQueue();
        //TODO fix this
//        RAM.addAdditionalProcess(process);

        this.currentActiveProcess = null;
    }
}
