
public class PCB implements Comparable {
	private int pid, pCounter, IOCounter, burstTime, size, IOTime;
	char state;

	public PCB(int pid, int burstTime, int IOTime, int size, char state) {
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

	public char getState() {
		return state;
	}

	public void setState(char state) {
		this.state = state;
	}

}
