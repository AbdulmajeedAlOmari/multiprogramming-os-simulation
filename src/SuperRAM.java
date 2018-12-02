
public class SuperRAM extends Thread {
	private final static int RAM_SIZE = (int) (160*0.9);
	private int usage;
	private final static int ADDITIONAL_PROCESS = (int) (160*0.1);
	private int usageA;

	int getUsage() {
		return usage;
	}
	void setUsage(int usage) {
		this.usage = usage;
	}
	int getUsageA() {
		return usageA;
	}
	void setUsageA(int usageA) {
		this.usageA = usageA;
	}
	static int getRamSize() {
		return RAM_SIZE;
	}
	static int getAdditionalProcess() {
		return ADDITIONAL_PROCESS;
	}
}
