import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.PriorityBlockingQueue;

import Bursts.CPUBurst;

public class RAM extends Thread {
	private static Queue<PCB> readyQ = new PriorityBlockingQueue<>();
	private static Queue<PCB> waitingForAllocation = new ConcurrentLinkedQueue<>();
	
	final static int RAM_SIZE = (int) (160*0.9);
	final static int ADDITIONAL_RAM_SIZE = (int) (160*0.1);
	private static int totalRamUsage;

	private static IODevice device;
	private static LineChart gui;

	RAM(IODevice device, LineChart gui) {
		RAM.totalRamUsage = 0;
		RAM.device = device;
		RAM.gui = gui;
	}

	@Override
    public void run() {
        //While OS is running, keep handling IO requests if available
        while(true) {

			try {
				longTermScheduler();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        }

    }
	
	static boolean addToJobQ(PCB obj){
		return waitingForAllocation.add(obj);
	}
	public static boolean addToReadyQ(PCB obj){
		return readyQ.add(obj);
	}
	
	// to add new process   
	void longTermScheduler() throws InterruptedException {
		// Save old ram usage info
		int oldTotalRamUsage = totalRamUsage;

		synchronized (device) {
			// Stop the device temporarly
			Thread.sleep(5);
		}


		// Check for deadlock
		if(isDeadlock()) {
			if(Utility.DEBUG_MODE)
				System.out.println("Deadlock solved for: [ " + waitingForAllocation.peek().getPid() + " ]");
			
			PCB maxProcess = device.getMaxProcess();
			device.killProcessFromIOQueue(maxProcess);
		}

		synchronized (device) {
			// Start the device again
			device.notify();
		}

		// Add waiting processes
		while(!waitingForAllocation.isEmpty() && isEnough(waitingForAllocation.peek().getSize())) {
			PCB process = waitingForAllocation.poll();

			if(process.getLoadedTime() == -1) {
				process.setLoadedTime(Clock.getCurrentMs());
			} else {
				// If not the first time, then increment its memoryCounter
				process.incrementMemoryCounter();
				if(Utility.DEBUG_MODE)
					System.out.println("Memory Counter: [ " + process.getPid() + " ] ---> " + process.getMemoryCounter());
			}

			if(process.getCurrentBurst() instanceof CPUBurst) {
				process.setProcessState(ProcessState.READY);
				readyQ.add(process);
			} else {
				// No need to change its state to WAITING since it is already!
				device.addProcessToDevice(process);
			}
			
			totalRamUsage += process.getSize();
		}

		if(totalRamUsage != oldTotalRamUsage)
			gui.addToDataset(Clock.getCurrentMs(), totalRamUsage);

		// Time to sleep...
		sleep(Utility.TIME * 200);
	}

	static boolean subtractFromUsage(int size) {
		// Check if we have enough UsageA to subtract from
		if(totalRamUsage - size < 0) {
			return false;
		}

		// Subtract it
		totalRamUsage -= size;

		return true;
	}

	synchronized static void handleMemoryValue(PCB process, int memoryValue) {
		int newSize = process.getSize() + memoryValue;

		if(newSize < 0) {
			// Kill process
			process.killProcess();
			return;
		}

		// Check if enough RAM is available
		if(!addToRamUsage(memoryValue)) {
			process.letProcessWait();
		}

		// Change process size to newSize
		process.setSize(newSize);
	}

	private synchronized static boolean addToRamUsage(int size) {
		if(totalRamUsage + size > RAM_SIZE + ADDITIONAL_RAM_SIZE) {
			return false;
		}

		totalRamUsage += size;

		return true;
	}

	private boolean isEnough(int size) {
		return totalRamUsage + size <= RAM_SIZE;
	}

	private boolean isDeadlock() {
		return !waitingForAllocation.isEmpty()
				&& readyQ.isEmpty()
				&& !isEnough(waitingForAllocation.peek().getSize())
				&& !device.isEmpty();
	}
	//This method retrieve but not remove from Queue
	static PCB retrieve(){
		return readyQ.peek();
	}

	public static Queue<PCB> getReadyQ() {
		return readyQ;
	}
	public static void setReadyQ(PriorityQueue<PCB> readyQ) {
		RAM.readyQ = readyQ;
	}
	static Queue<PCB> getWaitingQ() {
		return waitingForAllocation;
	}
	static int getTotalRamUsage() { return totalRamUsage; }
}
