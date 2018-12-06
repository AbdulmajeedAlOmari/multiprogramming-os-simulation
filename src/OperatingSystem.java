import java.util.LinkedList;

public class OperatingSystem extends Thread {
    private static LinkedList<PCB> finishedProcesses = new LinkedList<>();
    private static int size = 0;
    private static boolean isFinished = false;
    private static IODevice io;
    private static RAM ram;
    private static CPU cpu;

//    RAM ram = new RAM(new IODevice());
//    CPU cpu = new CPU(new RAM(new IODevice()));
//    Clock clock =new Clock();
    public static void main(String[] args) {
    	
    	io = new IODevice();
    	ram = new RAM(io);
    	cpu = new CPU(io);
    	
    	
//        Clock clock =new Clock();

        // Add processes to Waiting For Allocation Queue
       for(PCB p : FileHandler.readFile()){
    	   size++;
    	   RAM.addToJobQ(p);
       }

       cpu.start();
       ram.start();
       io.start();

        // Run GUI thread
        GUI gui = new GUI();
        Thread t = new Thread(gui);
        t.start();

       new OperatingSystem().start();
	}

    @Override
    public void run() {
    	while(true) {
    		if(finishedProcesses.size() == OperatingSystem.size) {
    		    if(Utility.DEBUG_MODE) {
                    System.out.println("\t\tFINISHED!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                    System.out.println("Finished num: " + finishedProcesses.size());
                    System.out.println("RAM Total Usage : " + RAM.getTotalRamUsage());
                }
                stopAllThreads();
    			FileHandler.writeFile(finishedProcesses);
                this.stop();
    		}

    		if(Utility.DEBUG_MODE) {
                System.out.println("Finished num: " + finishedProcesses.size());
                System.out.println("RAM Total Usage : " + RAM.getTotalRamUsage());
                System.out.println("---------------------------");
            }

    		try {
    			sleep(1);
    		} catch (InterruptedException e) {
    			e.printStackTrace();
    		}
    	}
    };

    private void stopAllThreads() {
        io.stop();
        ram.stop();
        cpu.stop();
    }

    static void addFinishedProcess(PCB process) {
        finishedProcesses.add(process);
    }
    static boolean isFinished() { return isFinished; }
}
