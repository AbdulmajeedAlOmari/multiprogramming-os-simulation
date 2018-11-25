import java.util.*;

public class RAM {
	private final static int RAM_SIZE = (int) (160*0.9);
	private int usage;
	private final static int ADDITIONAL_PROCESS = (int) (160*0.1);
	private int usageA;
//	private static Queue <PCB> jobQ = new PriorityQueue<PCB>(); do we have to create this queue?
	private static Queue <PCB> readyQ = new PriorityQueue<PCB>();
	private static Queue <PCB> waitingQ = new PriorityQueue<PCB>();//different from I/O queue

	public boolean isFull(){
		return usage == RAM_SIZE;
	}
	
	// to add new process
	public boolean addNewProcess(PCB obj){
		if(!(usage + obj.getSize()>=RAM_SIZE)){
		obj.setProcessState(ProcessState.READY);
		readyQ.add(obj);
		usage +=obj.getSize();
		return true;
		}
		obj.setProcessState(ProcessState.WAITING);
		waitingQ.add(obj);
		return false;
	}
	
	// to add additional CPU for old process
	public boolean addAdditionalProcess(PCB obj){
		if(!(usageA+obj.getSize()>=ADDITIONAL_PROCESS)){
			obj.setProcessState(ProcessState.READY);
		readyQ.add(obj);
		return true;
		}
		obj.setProcessState(ProcessState.WAITING);
		waitingQ.add(obj);
		return false;
	}
	
	//to serve from ready queue .. and check to add process form waiting queue .. 
	// read document point 6
	public PCB deQueue(){
		if(!readyQ.isEmpty()){
			PCB process = readyQ.remove();
			if(!(usageA-process.getBurstTime()<0)){
				usageA -=usageA-process.getSize();
			}else{ 
				usage -=usage-process.getSize();
			}
			if(!(waitingQ.isEmpty())&&(waitingQ.peek().getSize()+usage<=RAM_SIZE)){
				readyQ.add(waitingQ.remove());
				
			}
			return process;
		}
		return null;
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
		return waitingQ;
	}

	public static void setWaitingQ(PriorityQueue<PCB> waitingQ) {
		RAM.waitingQ = waitingQ;
	}
	
}
