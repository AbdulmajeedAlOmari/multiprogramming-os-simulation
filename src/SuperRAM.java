
public class SuperRAM extends Thread {

	private final static int RAM_SIZE = (int) (160*0.9);
	private int usage;
	private final static int ADDITIONAL_PROCESS = (int) (160*0.1);
	private int usageA;
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
	public static int getRamSize() {
		return RAM_SIZE;
	}
	public static int getAdditionalProcess() {
		return ADDITIONAL_PROCESS;
	}
}
