
public class PCB implements Comparable {
	private int pid, pCounter, IOCounter, burstTime, size, IOTime;
	private State state;

	public PCB(int pid, int burstTime, int IOTime, int size, State state) {
		this.pid = pid;
		this.burstTime = burstTime;
		this.IOTime = IOTime;
		this.size = size;
		this.state = state;
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

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

}
