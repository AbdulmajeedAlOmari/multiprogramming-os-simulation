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
            }
        }
    }

    private void handleCurrentProcess() {
        //Rename the process for simplicity
        PCB process = this.currentActiveProcess;

        // Set its state to Running
        process.setProcessState(ProcessState.RUNNING);

        // Get its first burst
        Burst currentBurst = process.getCurrentBurst();

        // Check if it is an IO burst or CPU burst
        if(currentBurst.getType().equals(BurstType.IO)) {
            ioDevice.addProcessToDevice(process);
            this.currentActiveProcess = null;
            return;
        }

        //Increment the CPU Counter for this process
        process.incrementCpuCounter();

        // If it is CPU burst then process it
        while(currentBurst.getRemainingTime() > 0) {
            // Decrease the remaining time by 1 ms
            currentBurst.decrementRemainingTime();

            // TODO hook it with GUI

            //Increment the total time it was processed in CPU
            process.incrementCpuTotalTime();

            try {
                //Wait for 1 millisecond before proceeding
                sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // TODO handle processes memory size and manipulate it

        // Get the next burst and load it in PCB
        Burst nextBurst = process.nextBurst();

        if(nextBurst == null) {
            // TODO save the time when it was TERMINATED/KILLED
            // This process finished all of its bursts normally
            process.setProcessState(ProcessState.TERMINATED);
        } else if(nextBurst.getType().equals(BurstType.IO)) {
            // The next burst is IO, so change its state to WAITING
            process.setProcessState(ProcessState.WAITING);
            ioDevice.addProcessToDevice(this.currentActiveProcess);
        } else if (nextBurst.getType().equals(BurstType.CPU)) {
            // TODO check if we need this statement
            // Since the next burst is also CPU, we will continue processing it
            return;
        }

        //Remove from ram and re add it again
        RAM.deQueue();
        RAM.addAdditionalProcess(process);

        this.currentActiveProcess = null;
    }
}
