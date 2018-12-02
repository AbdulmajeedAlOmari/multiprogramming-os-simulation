import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.PriorityBlockingQueue;

public class RAM extends SuperRAM {
	
	private static Queue<PCB> readyQ = new PriorityBlockingQueue<>();
	private static Queue<PCB> jobQ = new ConcurrentLinkedQueue<PCB>();
//	private DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
//	private Date date = new Date();

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
		return super.getUsage() == super.getRamSize();
	}
	
	public boolean addToJobQ(PCB obj){
		return jobQ.add(obj);
	}
	
	// to add new process   
	/*static*/ void longTermScheduler() throws InterruptedException {
		
		//This while loop add new process from jobQ to readyQ
		while((!jobQ.isEmpty()) && super.getUsage() + jobQ.peek().getSize()<=super.getRamSize()){ 
			
			//This statement add loaded time to process if this is new process to 90% of size 
			//which is not additional process
			if(!readyQ.contains(jobQ.peek().getPid())){
				//TODO fix this line
				jobQ.peek().setLoadedTime(OperatingSystem.clock.getCurrentMs());
			}
			
//			usage = usage + jobQ.peek().getSize();
			super.setUsage(getUsage() + jobQ.peek().getSize());
			readyQ.add(jobQ.remove());
		
		}
		// This statement add additional process to 10% of size
		if((!jobQ.isEmpty()) && super.getUsageA() + jobQ.peek().getSize() <= super.getAdditionalProcess() && readyQ.contains(jobQ.peek().getPid())){
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
	/*static*/ PCB deQueue(){
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
