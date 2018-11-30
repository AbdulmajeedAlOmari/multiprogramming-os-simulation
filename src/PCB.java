import java.util.Queue;

public class PCB implements Comparable {
	//TODO check what exactly is needed and what is not
	private int pid; //Process ID
	private int pCounter; //Program Counter
	private int IOTotalTime; //Total time of executed IO burst
	private int CPUTotalTime; //Total time of executed CPU burst
	private int size; //Program full size in MB
	private ProcessState processState; //Process State
	private Burst currentBurst;
	private Queue<Burst> burstQueue;

	public PCB(int pid, int size, ProcessState processState, Queue<Burst> burstQueue) {
		this.pid = pid;
		this.size = size;
		this.processState = processState;

		//Set first element of the queue as currentBurst
		this.currentBurst = burstQueue.poll();

		//Put the rest of the queue in burstQueue for future use
		this.burstQueue = burstQueue;
	}

	/***
	 * This method prints PCB as a String
	 * @return String
	 */
	public String toString() {
		return "ID: " + pid + ". \n CPU time: " + CPUTotalTime + "ms \n Size: " + size + "Kb \n IO: " + IOTotalTime + " ms.";
	}

	/***
	 * This method will be used for PriorityQueue prioritization comparator
	 * @param obj
	 * @return Integer: can be 0, >0, <0
	 */
	public int compareTo(Object obj) {
		return this.currentBurst.getRemainingTime() - ((PCB) obj).currentBurst.getRemainingTime();
	}

	Burst getNextBurst() {
		this.currentBurst = this.burstQueue.poll();
		return this.currentBurst;
	}

	// Getters/Setters
	int getPid() { return pid; }
	void setPid(int pid) { this.pid = pid; }

	int getpCounter() { return pCounter; }
	void setpCounter(int pCounter) { this.pCounter = pCounter; }

	//TODO do we need all of this?
	int getIOTotalTime() { return this.IOTotalTime; }
	void setIOTotalTime(int iOCounter) { this.IOTotalTime = iOCounter; }
	void incrementIOTotalTime() { this.IOTotalTime++; }

	Burst getCurrentBurst() { return this.currentBurst; }

	int getSize() { return size; }
	void setSize(int size) { this.size = size; }

	public ProcessState getProcessState() {
		return processState;
	}
	public void setProcessState(ProcessState processState) {
		this.processState = processState;
	}

}
