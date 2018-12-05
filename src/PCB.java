import Bursts.Burst;

import java.util.Queue;

public class PCB implements Comparable {
	private int pid; //Process ID
	private String name; //Program name
	private int loadedTime; //When it was loaded in Ready queue

	private int cpuCounter; //Number of times this process became in RUNNING state
	private int ioCounter; //Number of times this process became in WAITING state
	private int ioTotalTime; //Total time of executed IO burst
	private int cpuTotalTime; //Total time of executed CPU burst
	private int waitingCounter; //Number of times this process was waiting for memory space
	private int finishedTime; //The time when this process TERMINATED/KILLED

	private ProcessState processState; //Process State

	private int size; //Program full size in MB

	private Burst currentBurst; //The current burst in PCB (FCFS)
	 Queue<Burst> burstQueue;

	public PCB(int pid, String name, int loadedTime, int size, ProcessState processState, Queue<Burst> burstQueue) {
		this.pid = pid;
		this.name = name;
		this.loadedTime = loadedTime;

		this.cpuCounter = 0;
		this.ioCounter = 0;
		this.ioTotalTime = 0;
		this.cpuTotalTime = 0;
		this.waitingCounter = 0;
		this.finishedTime = 0;

		this.size = size;
		this.processState = processState;

		//Set first element of the queue as currentBurst
		this.currentBurst = burstQueue.poll();

		//Put the rest of the queue in burstQueue for future use
		this.burstQueue = burstQueue;
	}

	/***
	 * This method prints PCB info as a String
	 * a. Process ID
	 * b. Program name
	 * c. When it was loaded into the ready queue.
	 * d. Number of times it was in the CPU.
	 * e. Total time spent in the CPU
	 * f. Number of times it performed an IO.
	 * g. Total time spent in performing IO
	 * h. Number of times it was waiting for memory.
	 * i. Time it terminated or was killed
	 * j. Its final state: Killed or Terminated
	 * /////k. CPU Utilization/////
	 */
	public String toString() {
		return "/——————————¦¦[ " + name + " ]¦¦——————————\\"
				+ "» ID: " + pid + ". \n» CPU time: " + cpuTotalTime
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


	// Getters/Setters
	void setLoadedTime(int loadedTime) { this.loadedTime = loadedTime; }

	void incrementCpuCounter() { this.cpuCounter++; }
	void incrementIoCounter() { this.ioCounter++; }
	void incrementIoTotalTime() { this.ioTotalTime++; }
	void incrementCpuTotalTime() { this.cpuTotalTime++; }
	void incrementWaitingCounter() { this.waitingCounter++; }
	void setFinishedTime(int finishedTime) { this.finishedTime = finishedTime; }

	ProcessState getProcessState() { return processState; }
	void setProcessState(ProcessState processState) { this.processState = processState; }

	int getSize() { return size; }
	void setSize(int size) { this.size = size; }

	Burst getCurrentBurst() { return this.currentBurst; }
	int getPid() {return pid;}
}
