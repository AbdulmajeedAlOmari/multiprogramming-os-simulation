import java.util.LinkedList;

public class OperatingSystem {
    private static LinkedList<PCB> finishedProcesses = new LinkedList<>();
    private static int size = 0;
    private static IODevice io;
    private static RAM ram;
    private static CPU cpu;
    private static LineChart gui;

//    RAM ram = new RAM(new IODevice());
//    CPU cpu = new CPU(new RAM(new IODevice()));
//    Clock clock =new Clock();
    public static void main(String[] args) {
        // Make gui to save all information of ram usage
        gui = new LineChart();

    	io = new IODevice();
    	ram = new RAM(io, gui);
    	cpu = new CPU(io, gui);

        // Add processes to Waiting For Allocation Queue
       for(PCB p : FileHandler.readFile()){
    	   size++;
    	   RAM.addToJobQ(p);
       }

       cpu.start();
       ram.start();
       io.start();
	}

	static void stopOS() {
        try {
            sleepAllThreads();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Add the last size of memory ( ZERO )
        gui.addToDataset(Clock.getCurrentMs(), RAM.getTotalRamUsage());

        FileHandler.writeFile(finishedProcesses);
        System.out.println("Multiprogramming Operating System Simulation - [ Finished ]");
    }

    private static void sleepAllThreads() throws InterruptedException {
        io.sleep(1000);
        ram.sleep(1000);
        cpu.sleep(1000);
    }

    static void addFinishedProcess(PCB process) {
        finishedProcesses.add(process);
    }
    static boolean isFullyFinished() { return OperatingSystem.size == OperatingSystem.finishedProcesses.size(); }
}
