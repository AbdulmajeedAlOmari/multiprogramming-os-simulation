import Bursts.Burst;

import java.util.Queue;

public class PCB implements Comparable {
	private int pid; //Process ID
	private int loadedTime; //When it was loaded in Ready queue

	private int cpuCounter; //Number of times this process became in RUNNING state
	private int ioCounter; //Number of times this process became in WAITING state
	private int memoryCounter; //Number of times this process requested memory allocation
	private int ioTotalTime; //Total time of executed IO burst
	private int cpuTotalTime; //Total time of executed CPU burst
//	private int waitingCounter; //Number of times this process was waiting for memory space
	private int finishedTime; //The time when this process TERMINATED/KILLED

	private ProcessState processState; //Process State

	private int size; //Program full size in MB

	private Burst currentBurst; //The current burst in PCB (FCFS)
	public Queue<Burst> burstQueue;

	PCB(int pid, int size, Queue<Burst> burstQueue) {
		this.pid = pid;
		this.loadedTime = -1;

		this.cpuCounter = 0;
		this.ioCounter = 0;
		this.ioTotalTime = 0;
		this.memoryCounter = 0;
		this.cpuTotalTime = 0;
//		this.waitingCounter = 0;
		this.finishedTime = 0;

		this.size = size;
		this.processState = ProcessState.WAITING;

		//Set first element of the queue as currentBurst
		this.currentBurst = burstQueue.poll();

		//Put the rest of the queue in burstQueue for future use
		this.burstQueue = burstQueue;
	}
	public String toString() {
		return "/——————————¦¦[ " + pid + " ]¦¦——————————\\"
				+"» CPU time: " + cpuTotalTime
				+ " ms \n» Size: " + size + " Kb \n» IO: " + ioTotalTime + " ms.\n"
				+ "\\———————————¦-_END_-¦———————————/";
	}

	/***
	 * This method will be used for PriorityQueue prioritization comparator
	 */
	public int compareTo(Object obj) {
		return this.currentBurst.getRemainingTime() - ((PCB) obj).currentBurst.getRemainingTime();
	}

	/***
	 * This method change the current burst to the next available one and returns it.
	 * If there were no bursts it will return null.
	 */
	Burst nextBurst() {
		this.currentBurst = this.burstQueue.poll();
		return this.currentBurst;
	}

	void killProcess() {
		this.setFinishedTime(Clock.getCurrentMs());

		this.setProcessState(ProcessState.KILLED);

		RAM.subtractFromUsage(this.size);

		OperatingSystem.addFinishedProcess(this);

		if(OperatingSystem.isFullyFinished())
			OperatingSystem.stopOS();

		if(Utility.DEBUG_MODE)
			System.out.println("(PCB) - KILLED PROCESS [" + pid + "].");
	}

	void terminateProcess() {
		this.setFinishedTime(Clock.getCurrentMs());

		this.setProcessState(ProcessState.TERMINATED);

		RAM.subtractFromUsage(this.size);

		OperatingSystem.addFinishedProcess(this);

		if(OperatingSystem.isFullyFinished())
			OperatingSystem.stopOS();

		if(Utility.DEBUG_MODE)
			System.out.println("(PCB) - TERMINATED PROCESS [" + pid + "].");
	}

	void letProcessWait() {
		// Set its status to waiting
		this.setProcessState(ProcessState.WAITING);

		RAM.subtractFromUsage(this.size);

		RAM.addToJobQ(this);

		if(Utility.DEBUG_MODE)
			System.out.println("(PCB) - toWAIT PROCESS [" + pid + "].");
	}

	void letProcessReady() {
		this.setProcessState(ProcessState.READY);

		RAM.addToReadyQ(this);

		if(Utility.DEBUG_MODE)
			System.out.println("(PCB) - toREADY PROCESS [" + pid + "].");
	}

	// Getters/Setters
	void setLoadedTime(int loadedTime) { this.loadedTime = loadedTime; }

	void incrementCpuCounter() { this.cpuCounter++; }
	void incrementIoCounter() { this.ioCounter++; }
	void incrementMemoryCounter() { this.memoryCounter++; }
	void incrementIoTotalTime() { this.ioTotalTime++; }
	void incrementCpuTotalTime() { this.cpuTotalTime++; }
	void setFinishedTime(int finishedTime) { this.finishedTime = finishedTime; }

	ProcessState getProcessState() { return processState; }
	void setProcessState(ProcessState processState) { this.processState = processState; }

	int getSize() { return size; }
	void setSize(int size) { this.size = size; }

	Burst getCurrentBurst() { return this.currentBurst; }
	int getPid() {return pid;}
	int getCpuCounter() {
		return cpuCounter;
	}
	int getIoCounter() {
		return ioCounter;
	}
	int getMemoryCounter() { return memoryCounter; }
	int getIoTotalTime() {
		return ioTotalTime;
	}
	int getCpuTotalTime() {
		return cpuTotalTime;
	}
	int getLoadedTime() { return loadedTime; }
	int getFinishedTime() { return finishedTime; }

	
}
