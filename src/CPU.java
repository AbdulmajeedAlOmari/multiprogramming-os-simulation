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
            currentActiveProcess = RAM.deQueue();

            if(currentActiveProcess != null) {
                handleCurrentProcess();
            }
        }
    }

    private void handleCurrentProcess() {
        // Set its state to Running
        this.currentActiveProcess.setProcessState(ProcessState.RUNNING);

        // Get its first burst
        Burst currentBurst = this.currentActiveProcess.getCurrentBurst();

        // Check if it is an IO burst or CPU burst
        if(currentBurst.getType().equals(BurstType.IO)) {
            ioDevice.addProcessToDevice(this.currentActiveProcess);
            this.currentActiveProcess = null;
            return;
        }

        // If it is CPU burst then process it
        while(currentBurst.getRemainingTime() > 0) {
            // Decrease the remaining time by 1 ms
            currentBurst.decrementRemainingTime();

            // TODO Handle process logic and hook it with GUI

            try {
                sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // TODO handle processes memory size and manipulate it

        // Get the next burst and load it in PCB
        Burst nextBurst = this.currentActiveProcess.getNextBurst();

        if(nextBurst == null) {
            // This process finished all of its bursts normally
            this.currentActiveProcess.setProcessState(ProcessState.TERMINATED);
        } else if(nextBurst.getType().equals(BurstType.IO)) {
            // The next burst is IO, so change its state to WAITING
            this.currentActiveProcess.setProcessState(ProcessState.WAITING);
            ioDevice.addProcessToDevice(this.currentActiveProcess);
        } else if (nextBurst.getType().equals(BurstType.CPU)) {
            // TODO check if we need this statement
            // Since the next burst is also CPU, we will continue processing it
            return;
        }

        this.currentActiveProcess = null;
    }
}
