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
        
        if(currentBurst instanceof IOBurst)
        	System.out.println("IO BURST IN CPU!!!");
        
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
        
        CPUBurst cpuBurst = null;
        
        try {
        	cpuBurst = (CPUBurst) process.getCurrentBurst();
        } catch(Exception e) {
        	System.out.println("Process [" + process.getPid() +"] is the issue.\n");
        	System.out.println("IO Counter: " + process.ioCounter);
        	System.out.println("IO Time: " + process.ioTotalTime +" ms");
        	System.out.println("CPU Counter: " + process.cpuCounter);
        	System.out.println("CPU Time: " + process.cpuTotalTime +" ms");
        	System.out.println("Next IO Time: " + process.getCurrentBurst().getRemainingTime());
        	System.exit(-1);
        }
        
        int memoryValue = cpuBurst.getMemoryValue();
        
        if(memoryValue != 0)
        	handleMemoryChange(memoryValue);
        
        // Get the next burst and load it in PCB
        Burst nextBurst = process.nextBurst();

        // If anything changed in the process, return
        if(!process.getProcessState().equals(ProcessState.RUNNING)) {
        	System.out.println("["+process.getPid()+"] is not RUNNING, its current state: ("+process.getProcessState()+"), returned...");
        	return;
        }
        
        // Remove the process from RAM
//        ram.removeProcess(this.currentActiveProcess);
        
        if(nextBurst == null) {
            // Save the termination time of the process
            process.setFinishedTime(Clock.getCurrentMs());

            System.out.println("Finsiehd --> " + process.getPid());
            // This process finished all of its bursts normally
            process.setProcessState(ProcessState.TERMINATED);

            OperatingSystem.addFinishedProcess(process);
        } else if(nextBurst instanceof IOBurst){
        	// Handle IO Burst
            process.setProcessState(ProcessState.WAITING);
            ioDevice.addProcessToDevice(this.currentActiveProcess);
        }
    }
    
    private void handleMemoryChange(int memoryValue) {
    	int newSize = this.currentActiveProcess.getSize() + memoryValue;
    	int currentSize = this.currentActiveProcess.getSize();
    	
    	if(newSize < 0) {
    		// Kill process
    		this.currentActiveProcess.setProcessState(ProcessState.KILLED);
    		
    		RAM.subtractFromUsage(currentSize);
    		
    		OperatingSystem.addFinishedProcess(this.currentActiveProcess);
    		return;
    	}
    	
    	Burst nextBurst = this.currentActiveProcess.nextBurst();
    	
    	if(nextBurst == null) {
    		// Kill process
    		this.currentActiveProcess.setProcessState(ProcessState.TERMINATED);
    		
    		RAM.subtractFromUsage(currentSize);
    		
    		OperatingSystem.addFinishedProcess(this.currentActiveProcess);
    		return;
    	}
    	
    	
    	if(!RAM.addToUsage(memoryValue)) {
    		if(!RAM.addToUsageA(memoryValue)) {
    			// Set its status to waiting
    			this.currentActiveProcess.setProcessState(ProcessState.WAITING);
    			
    			RAM.subtractFromUsage(currentSize);
    			RAM.addToWaitingQ(this.currentActiveProcess);
    		}
    	}
    	
    	this.currentActiveProcess.setSize(newSize);
    }
}
