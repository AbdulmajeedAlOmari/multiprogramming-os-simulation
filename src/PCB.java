
public class PCB implements Comparable {
	private int pid; //Process ID
	private int pCounter; //Program Counter
	private int IOCounter; //IO burst counter
	private int burstTime; //CPU burst time
	private int size; //Program full size in MB
	private int IOTime; //IO burst time
	private ProcessState processState; //Process State

	public PCB(int pid, int burstTime, int IOTime, int size, ProcessState processState) {
		this.pid = pid;
		this.burstTime = burstTime;
		this.IOTime = IOTime;
		this.size = size;
		this.processState = processState;
	}

	public String toString() {
		return "ID: " + pid + ". \n CPU time: " + burstTime + "ms \n Size: " + size + "Kb \n IO: " + IOTime + " ms.";

	}

	public int compareTo(Object obj) {

		return this.burstTime - ((PCB) obj).burstTime;
	}

	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}

	public int getpCounter() {
		return pCounter;
	}
	public void setpCounter(int pCounter) {
		this.pCounter = pCounter;
	}

	public int getIOCounter() {
		return IOCounter;
	}
	public void setIOCounter(int iOCounter) {
		IOCounter = iOCounter;
	}

	public int getBurstTime() {
		return burstTime;
	}
	public void setBurstTime(int burstTime) {
		this.burstTime = burstTime;
	}

	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}

	public int getIOTime() {
		return IOTime;
	}
	public void setIOTime(int iOTime) {
		IOTime = iOTime;
	}

	public ProcessState getProcessState() {
		return processState;
	}
	public void setProcessState(ProcessState processState) {
		this.processState = processState;
	}

}
