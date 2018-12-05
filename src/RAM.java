import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.PriorityBlockingQueue;

public class RAM extends Thread {
	private static Queue<PCB> readyQ = new PriorityBlockingQueue<>();
	private static Queue<PCB> waitingForAllocation = new ConcurrentLinkedQueue<>();
	
	public final static int RAM_SIZE = (int) (160*0.9);
	private static int usage;
	public final static int ADDITIONAL_PROCESS = (int) (160*0.1);
	private static int usageA;
	
	private static IODevice device;

	RAM(IODevice device) {
		RAM.device = device;
	}

	@Override
    public void run() {
        //While OS is running, keep handling IO requests if available
        while(true) {
            
        	// if(currentProcess != null) << Question: Do we have use this line ?
			try {
				longTermScheduler();
			} catch (InterruptedException e) {
				System.out.println("ERROR OCCURED");
				e.printStackTrace();
			}
        }

    }

	public boolean isFull(){
		return RAM.usage == RAM.RAM_SIZE;
	}
	
	public boolean addToJobQ(PCB obj){
		return waitingForAllocation.add(obj);
	}
	public static boolean addToReadyQ(PCB obj){
		return readyQ.add(obj);//Do we have consider the size of RAM or not? 
	}
	
	// to add new process   
	void longTermScheduler() throws InterruptedException {
		// Check for deadlock
		if(!waitingForAllocation.isEmpty() && readyQ.isEmpty()
				&& !isEnough(waitingForAllocation.peek().getSize())
				&& !device.getWaitingList().isEmpty()) {
			System.out.println("Deadlock solved for: [ " + waitingForAllocation.peek().getPid() + " ]");
			
			synchronized (device) {
				// Stop the device
				Thread.sleep(5);
			}
			
			PCB maxProcess = getMaxProcess();
			killProcessInWaitingQ(maxProcess);
			
			
			synchronized (device) {
				// Start the device
				device.notify();
			}
		}
		
		// Add waiting processes
		while(!waitingForAllocation.isEmpty() && isEnough(waitingForAllocation.peek().getSize())) {
			PCB process = waitingForAllocation.poll();
			
			readyQ.add(process);
			
			addToUsage(process.getSize());
		}
		
		// Time to sleep...
		sleep(200);
	}
	
	
	//to serve from ready queue .. and check to add process form waiting(not I/O waiting) queue .. 
	// read document point 6
	PCB deQueue(){
		if(readyQ.isEmpty()){
			return null;
		}
		
		PCB process = readyQ.remove();
		int processSize = process.getSize();
		
		if(usageA-processSize >= 0)
			usageA -= processSize;
		else
			usage -= processSize;

		return process;
	}

	private PCB getMaxProcess() {
		System.out.println("Waiting SIZE: " + device.getWaitingList().size());
		System.out.println("Ready SIZE: " + readyQ.size());
		System.out.println("Usage: " + getUsage());
		System.out.println("UsageA: " + getUsageA());
		
		Object[] waitingList = device.getWaitingList().toArray();
		
		// Let the first process be the max size process
		PCB maxPCB = (PCB) waitingList[0];

		for(int i = 1; i < waitingList.length; i++) {
			PCB currentPCB = (PCB) waitingList[i];

			// If current process size is greater than the max process
			if(currentPCB.getSize() > maxPCB.getSize())
				// Then, it is the new max process
				maxPCB = currentPCB;
		}

		return maxPCB;
	}

	private void killProcessInWaitingQ(PCB process) {
//		Object[] waitingList =device.getWaitingList().toArray();
//		PCB currentProcess = null;
		System.out.println("removing "+device.getWaitingList().remove(process));

//		for(int i = 0; i < waitingList.length; i++) {
//			currentProcess =(PCB) waitingList[i];
//
//			// Did we find the process
//			if(currentProcess.getPid() == process.getPid()) {
				// Remove it from IO device
//				waitingList.remove(i);

				// Get process current size to subtract it from Usage
				int sizeOfProcess = process.getSize();

				subtractFromUsage(sizeOfProcess);

				// Set state to KILLED
				process.setProcessState(ProcessState.KILLED);

				// Add process to finished list in OS
				OperatingSystem.addFinishedProcess(process);
				
//			}
//		}
	}

	private void subtractFromUsage(int size) {
		// Check if we have enough UsageA to subtract from
		if(usageA - size >= 0) {
			int newSizeOfUsage = usageA - size;
			usageA = newSizeOfUsage;
		} else {
			// If not, then subtract from normal Usage
			int newSizeOfUsage = usage - size;
			usage = newSizeOfUsage;
		}
	}
	
	private boolean addToUsage(int size) {
		if(usage + size > RAM_SIZE)
			return false;
		
		usage += size;
		
		return true;
	}
	
	private boolean addToUsageA(int size) {
		if(usageA + size > ADDITIONAL_PROCESS)
			return false;
		
		usageA += size;
		
		return true;
	}
	
	void removeProcess(PCB process) {
		if(readyQ.remove(process)) {
			subtractFromUsage(process.getSize());
		} else {
			System.out.println("DID NOT FIND PROCESS!");
		}
	}
	
	public boolean findByPid(Queue<PCB> Q,int pid){
		Queue<Integer> s = new PriorityBlockingQueue<Integer>();
		Queue<PCB> alterQueue = new PriorityBlockingQueue<PCB>();
		while (!Q.isEmpty()){
			PCB p = Q.remove();
			s.add(p.getPid());
			alterQueue.add(p);
		}
		if(s.contains(pid)){
			while(!alterQueue.isEmpty())
				Q.add(alterQueue.remove());
			return true;}
		while(!alterQueue.isEmpty())
			Q.add(alterQueue.remove());
		return false;
	}
	
	private boolean isEnough(int size) {
		return size + usage <= RAM_SIZE;
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

	public static Queue<PCB> getWaitingQ() {
		return waitingForAllocation;
	}

	public static void setWaitingQ(PriorityQueue<PCB> waitingQ) {
		RAM.waitingForAllocation = waitingQ;
	}
	
	static int getUsage() {
		return RAM.usage;
	}
	
	static void setUsage(int usage) {
		RAM.usage = usage;
	}
	
	static int getUsageA() {
		return RAM.usageA;
	}
	
	static void setUsageA(int usageA) {
		RAM.usageA = usageA;
	}
}
