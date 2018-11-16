public class PCB implements Comparable<PCB> {
    private int pid;
//    private String state;
//    private int pc;
//    private String registerInformation;
    private int arrivalTime;
    private int burstTime;
//    private String memoryRelatedInformation;
//    private String accountingInformation;
//    private String ioStatus;

    public PCB(int pid, int arrivalTime, int burstTime) {
        this.pid = pid;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
    }

    @Override
    public int compareTo(PCB pcb) {
        if (this.equals(pcb)) {
            return 0;
        } else if (this.getBurstTime() > pcb.getBurstTime()) {
            return 1;
        } else {
            return -1;
        }
    }

    public int getPid() {
        return pid;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getBurstTime() {
        return burstTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public void setBurstTime(int burstTime) {
        this.burstTime = burstTime;
    }
}