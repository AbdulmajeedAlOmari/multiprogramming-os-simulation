import java.util.*;

public class RAM {
	private final static int RAM_SIZE = (int) (160*0.9);
	private static int usage;
	private final static int ADDITIONAL_PROCESS = (int) (160*0.1);
	private static int usageA;
//	private static Queue <PCB> jobQ = new PriorityQueue<PCB>(); do we have to create this queue?
	private static Queue <PCB> readyQ = new PriorityQueue<PCB>();
	private static Queue <PCB> waitingQ = new PriorityQueue<PCB>(); //different from I/O queue
	
	// to add new process
	static boolean addNewProcess(PCB process){
		if(usage + process.getSize() > RAM_SIZE){
            process.setProcessState(ProcessState.WAITING);
            waitingQ.add(process);
            return false;
		}

        process.setProcessState(ProcessState.READY);
        readyQ.add(process);
        usage +=process.getSize();
        return true;
	}

    //TODO Ask Abdulmajeed about this method and its conditions
	// to add additional CPU for old process
	static boolean addAdditionalProcess(PCB process){
		if(usageA+process.getSize() < ADDITIONAL_PROCESS){
			process.setProcessState(ProcessState.READY);
            readyQ.add(process);
            return true;
		}

        process.setProcessState(ProcessState.WAITING);
        waitingQ.add(process);
        return false;
	}
	
	// to serve from ready queue .. and check to add process form waiting queue ..
	// read document point 6
	static PCB deQueue(){
		if(readyQ.isEmpty())
		    return null;

        PCB process = readyQ.remove();

        if(usageA-process.getCurrentBurst().getRemainingTime() >= 0)
            usageA -= usageA-process.getSize();
        else
            usage -= usage-process.getSize();

        // If there is available space in ram, then add new process to it.
        if(!waitingQ.isEmpty() && (waitingQ.peek().getSize()+usage<=RAM_SIZE))
            readyQ.add(waitingQ.remove());

        return process;
	}
	
	public PCB retrieve(){
		return readyQ.peek(); // will return null if the queue is empty
	}

    static boolean isFull(){
        return usage == RAM_SIZE;
    }

	// Getters/Setters
	public int getUsage() { return usage; }
	public void setUsage(int usage) { RAM.usage = usage; }

	public int getUsageA() { return usageA; }
	public void setUsageA(int usageA) { RAM.usageA = usageA; }

	public static Queue<PCB> getReadyQ() { return readyQ; }
	public static void setReadyQ(PriorityQueue<PCB> readyQ) { RAM.readyQ = readyQ; }

	public static Queue<PCB> getWaitingQ() { return waitingQ; }
	public static void setWaitingQ(PriorityQueue<PCB> waitingQ) { RAM.waitingQ = waitingQ; }
	
}
