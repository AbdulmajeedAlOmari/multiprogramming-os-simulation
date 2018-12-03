package Bursts;

public class CPUBurst extends Burst {
    private int memoryValue;

    public CPUBurst(int time, int memoryValue) {
        super(time);
        this.memoryValue = memoryValue;
    }

    public int getMemoryValue() { return memoryValue; }
}
