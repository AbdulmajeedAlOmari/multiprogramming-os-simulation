import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class RAM extends Thread {
	private final static int RAM_SIZE = (int) (160*0.9);
	private int usage;
	private final static int ADDITIONAL_PROCESS = (int) (160*0.1);
	private int usageA;
	private static Queue <PCB> readyQ = new PriorityQueue<PCB>();
	private static Queue <PCB> jobQ = new ConcurrentLinkedQueue<PCB>();
	private DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	private Date date = new Date();
	
	@Override
    public void run() {
        //TODO check whether we need (isRunning) flag or shall we use (synchronized) keyword only
        //While OS is running, keep handling IO requests if available
        //TODO change to Listener
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
		return usage == RAM_SIZE;
	}
	
	public boolean addToJobQ(PCB obj){
		return jobQ.add(obj);
	}
	
	// to add new process   
	public void longTermScheduler() throws InterruptedException {
		
		//This while loop add new process from jobQ to readyQ
		while((!jobQ.isEmpty()) && usage + jobQ.peek().getSize()<=RAM_SIZE){ 
			
			//This statement add loaded time to process if this is new process to 90% of size 
			//which is not additional process
			if(!readyQ.contains(jobQ.peek().getPid())){ 
			jobQ.peek().setLoadedTime(dateFormat.format(date));}
			
			usage = usage + jobQ.peek().getSize();
			readyQ.add(jobQ.remove());
		
		}
		// This statement add additional process to 10% of size
		if((!jobQ.isEmpty()) && usageA + jobQ.peek().getSize() <= ADDITIONAL_PROCESS && readyQ.contains(jobQ.peek().getPid())){
			readyQ.add(jobQ.remove());
			usageA = usageA + jobQ.peek().getSize();
		}
		//This statement doing as point 6 says 
		jobQ.peek().setProcessState(ProcessState.WAITING);
		sleep(200);
		jobQ.peek().setProcessState(ProcessState.READY);
	}
	
	//to serve from ready queue .. and check to add process form waiting(not I/O waiting) queue .. 
	// read document point 6
	public PCB deQueue(){
		if(!readyQ.isEmpty()){
			PCB process = readyQ.remove();
			if(process.getSize()>=0){
				
				if(!(usageA-process.getSize()<0))
					usageA = usageA-process.getSize();
				else
					usage =usage-process.getSize();
				
			}
			
//			if(!(jobQ.isEmpty())&&(jobQ.peek().getSize()+usage<=RAM_SIZE)){
//				readyQ.add(jobQ.remove());
//				
//			}
			return process;
		}
		return null;
	}
	
	public PCB retrieve(){//This method retrieve but not remove from Queue
		
			return readyQ.peek(); // will return null if the queue is empty
	}

	public int getUsage() {
		return usage;
	}

	public void setUsage(int usage) {
		this.usage = usage;
	}

	public int getUsageA() {
		return usageA;
	}

	public void setUsageA(int usageA) {
		this.usageA = usageA;
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
