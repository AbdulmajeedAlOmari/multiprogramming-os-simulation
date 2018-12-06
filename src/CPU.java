import Bursts.Burst;
import Bursts.CPUBurst;
import Bursts.IOBurst;

public class CPU extends Thread {
    private IODevice ioDevice;
    private PCB currentActiveProcess;
    
    CPU(IODevice ioDevice) {
        this.ioDevice = ioDevice;
        this.currentActiveProcess = null;
    }

    @Override
    public void run() {
        while(true) {
            currentActiveProcess = RAM.getReadyQ().poll();
//            System.out.println("ReadyQSize: " + RAM.getReadyQ().size());
//            System.out.println("RAM usage : " + RAM.getUsage());
//            System.out.println("RAM usageA : " + RAM.getUsageA());
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

        // Get the next burst and load it in PCB
        Burst nextBurst = process.nextBurst();
        
        if(nextBurst == null) {
            // This process finished all of its bursts normally
            process.terminateProcess();
            System.out.println("(CPU) - FINISHED ==============================> " + process.getPid());
            return;
        }

        CPUBurst cpuBurst =  (CPUBurst) currentBurst;

        // Calculate its new memory value
        int memoryValue = cpuBurst.getMemoryValue();

        if(memoryValue != 0)
            RAM.handleMemoryValue(process, memoryValue);

        // If anything changed in the process, return
        if(process.getProcessState().equals(ProcessState.RUNNING) && nextBurst instanceof IOBurst) {
            // Handle IO Burst
            process.setProcessState(ProcessState.WAITING);
            ioDevice.addProcessToDevice(this.currentActiveProcess);
        }
    }
}
