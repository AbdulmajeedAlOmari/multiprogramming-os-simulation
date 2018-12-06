import java.util.LinkedList;

public class OperatingSystem extends Thread {
    private static LinkedList<PCB> finishedProcesses = new LinkedList<>();
    private static int size = 0;
    
//    RAM ram = new RAM(new IODevice());
//    CPU cpu = new CPU(new RAM(new IODevice()));
//    Clock clock =new Clock();
    public static void main(String[] args) {
    	
    	IODevice io = new IODevice();
    	RAM ram = new RAM(io);
    	CPU cpu = new CPU(io);
    	
//        Clock clock =new Clock();

        // Add processes to Job Queue
       for(PCB p : FileHandler.readFile()){
    	   size++;
    	   ram.addToJobQ(p);
       }

        io.start();
        ram.start();
        cpu.start();

//       System.out.println(ram.);
       new OperatingSystem().start();
	}

    @Override
    public void run() {
    	while(true) {
    		if(finishedProcesses.size() == OperatingSystem.size) {
    			System.out.println("FINISHED!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    			// TODO remove exit
    			System.exit(0);
    		}

            System.out.println("Finished num: " + finishedProcesses.size());
            System.out.println("RAM Total Usage : " + RAM.getTotalRamUsage());
            System.out.println("---------------------------");
    		try {
    			sleep(2000);
    		} catch (InterruptedException e) {
    			e.printStackTrace();
    		}
    	}
    };
    public static void addFinishedProcess(PCB process) {
        finishedProcesses.add(process);
    }
}
