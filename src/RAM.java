import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.PriorityBlockingQueue;

public class RAM extends SuperRAM {
	private static Queue<PCB> readyQ = new PriorityBlockingQueue<>();
	private static Queue<PCB> jobQ = new ConcurrentLinkedQueue<PCB>();
	private static IODevice device;
//	private DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
//	private Date date = new Date();

	RAM(IODevice device) {
		this.device = device;
	}

	@Override
    public void run() {
        //While OS is running, keep handling IO requests if available
        while(true) {
            
        	// if(currentProcess != null) << Question: Do we have use this line ?
			try {
				longTermScheduler();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }

    }

	public boolean isFull(){
		return super.getUsage() == SuperRAM.getRamSize();
	}
	
	public boolean addToJobQ(PCB obj){
		return jobQ.add(obj);
	}
	
	// to add new process   
	void longTermScheduler() throws InterruptedException {
		
		//This while loop add new process from jobQ to readyQ
		while((!jobQ.isEmpty()) && super.getUsage() + jobQ.peek().getSize() <= SuperRAM.getRamSize()){
			
			//This statement add loaded time to process if this is new process to 90% of size 
			//which is not additional process
			if(!readyQ.contains(jobQ.peek().getPid())){
				//TODO fix this line
				jobQ.peek().setLoadedTime(Clock.getCurrentMs());
			}
			
//			usage = usage + jobQ.peek().getSize();
			super.setUsage(getUsage() + jobQ.peek().getSize());
			readyQ.add(jobQ.remove());
		
		}
		// This statement add additional process to 10% of size
		if((!jobQ.isEmpty()) && super.getUsageA() + jobQ.peek().getSize() <= SuperRAM.getAdditionalProcess() && readyQ.contains(jobQ.peek().getPid())){
			readyQ.add(jobQ.remove());
			
//			usageA = usageA + jobQ.peek().getSize();
			super.setUsageA(getUsageA()+ jobQ.peek().getSize());
		}
		//This statement doing as point 6 says 
		jobQ.peek().setProcessState(ProcessState.WAITING);
		sleep(200);
		jobQ.peek().setProcessState(ProcessState.READY);
	}
	
	//to serve from ready queue .. and check to add process form waiting(not I/O waiting) queue .. 
	// read document point 6
	PCB deQueue(){
		if(!readyQ.isEmpty()){
			PCB process = readyQ.remove();
			if(process.getSize()>=0){
				
				if(!(super.getUsageA()-process.getSize()<0))
//					usageA = usageA-process.getSize();
					super.setUsageA(getUsageA()-process.getSize());
				else
					super.setUsage(getUsage()-process.getSize());
				
			}
			
//			if(!(jobQ.isEmpty())&&(jobQ.peek().getSize()+usage<=RAM_SIZE)){
//				readyQ.add(jobQ.remove());
//				
//			}
			return process;
		}
		return null;
	}

//	try {
//		// To forbid errors from happening, pause the thread
//		device.wait();
//	} catch(InterruptedException e) {
//		e.printStackTrace();
//	}

//	// Run the thread again
//		device.notify();

	private PCB getMaxProcess() {
		LinkedList<PCB> waitingList = (LinkedList<PCB>) device.getWaitingList();
		// Let the first process be the max size process
		PCB maxPCB = waitingList.get(0);

		for(int i = 1; i < waitingList.size(); i++) {
			PCB currentPCB = waitingList.get(i);

			// If current process size is greater than the max process
			if(currentPCB.getSize() > maxPCB.getSize())
				// Then, it is the new max process
				maxPCB = currentPCB;
		}

		return maxPCB;
	}

	private void killProcessInWaitingQ(PCB process) {
		LinkedList<PCB> waitingList = (LinkedList<PCB>) device.getWaitingList();
		PCB currentProcess = null;

		for(int i = 0; i < waitingList.size(); i++) {
			currentProcess = waitingList.get(i);

			// Did we find the process
			if(currentProcess.getPid() == process.getPid()) {
				// Remove it from IO device and save it in a variable
				PCB removedProcess = waitingList.remove(i);

				// Get process current size to subtract it from Usage
				int sizeOfProcess = removedProcess.getSize();

				subtractFromUsage(sizeOfProcess);

				// Set state to KILLED
				currentProcess.setProcessState(ProcessState.KILLED);

				// Add process to finished list in OS
				OperatingSystem.addFinishedProcess(currentProcess);
				break;
			}
		}
	}

	private void subtractFromUsage(int size) {
		// Check if we have enough UsageA to subtract from
		if(super.getUsageA() - size >= 0) {
			int newSizeOfUsage = super.getUsageA() - size;
			super.setUsageA(newSizeOfUsage);
		} else {
			// If not, then subtract from normal Usage
			int newSizeOfUsage = super.getUsage() - size;
			super.setUsage(newSizeOfUsage);
		}
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
		return jobQ;
	}

	public static void setWaitingQ(PriorityQueue<PCB> waitingQ) {
		RAM.jobQ = waitingQ;
	}
	
}
