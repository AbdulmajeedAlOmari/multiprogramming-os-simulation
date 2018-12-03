import java.util.LinkedList;

public class OperatingSystem {
    private static LinkedList<PCB> finishedProcesses = new LinkedList<>();


    
    public static void addFinishedProcess(PCB process) {
        finishedProcesses.add(process);
    }
}
