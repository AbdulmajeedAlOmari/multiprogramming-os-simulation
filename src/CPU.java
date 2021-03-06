import Bursts.Burst;
import Bursts.CPUBurst;
import Bursts.IOBurst;

public class CPU extends Thread {
    private IODevice ioDevice;
    private PCB currentActiveProcess;
    private static int busyTime;
    private static int idleTime;
    private LineChart gui;

    CPU(IODevice ioDevice, LineChart gui) {
        this.ioDevice = ioDevice;
        this.gui = gui;

        this.currentActiveProcess = null;
        CPU.busyTime = 0;
        CPU.idleTime = 0;
    }

    @Override
    public void run() {
        while(true) {
            currentActiveProcess = RAM.getReadyQ().poll();
            if(currentActiveProcess != null) {
                handleCurrentProcess();
            } else {
                try {
                    sleep(Utility.TIME);
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }

                //This will increment the Clock by 1 millisecond
                Clock.incrementMs();

                // Increment CPU idle time
                CPU.idleTime++;
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

            // Increment CPU busyTime
            CPU.busyTime++;

            try {
                //Wait for x millisecond before proceeding
                sleep(Utility.TIME);
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

            if(Utility.DEBUG_MODE)
                System.out.println("(CPU) - FINISHED ==============================> " + process.getPid());
            return;
        }

        CPUBurst cpuBurst =  (CPUBurst) currentBurst;

        // Calculate its new memory value
        int memoryValue = cpuBurst.getMemoryValue();

        // Save old ram usage
        int oldTotalRamUsage = RAM.getTotalRamUsage();

        if(memoryValue != 0)
            RAM.handleMemoryValue(process, memoryValue);

        // If anything changed in the process, return
        if(process.getProcessState().equals(ProcessState.RUNNING) && nextBurst instanceof IOBurst) {
            // Handle IO Burst
            process.setProcessState(ProcessState.WAITING);
            ioDevice.addProcessToDevice(this.currentActiveProcess);
        }

        // Save current ram usage info in gui
        if(oldTotalRamUsage != RAM.getTotalRamUsage())
            gui.addToDataset(Clock.getCurrentMs(), RAM.getTotalRamUsage());
    }

    static int getBusyTime() { return CPU.busyTime; }
    static int getIdleTime() { return CPU.idleTime; }
}
