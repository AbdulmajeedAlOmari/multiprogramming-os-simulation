import java.io.*;
import java.util.*;

import Bursts.Burst;
import Bursts.CPUBurst;
import Bursts.IOBurst;

public class FileHandler {

	public static LinkedList<PCB> readFile() {
		String processID, bursTime, processSize, IOTime;
		String line;
		LinkedList<PCB> processes = new LinkedList<>();

		try {
			BufferedReader bfr = new BufferedReader(new FileReader(Utility.INPUT_FILE_PATH));
			bfr.readLine();
			while ((line = bfr.readLine()) != null) {

				String[] processesInfo = line.split("\t");
				int sizeOfArray = processesInfo.length;

				int pid = Integer.parseInt(processesInfo[0]);

				Queue<Burst> bursts = new LinkedList<>();

				for (int i = 1; i < sizeOfArray; i += 3) {
					int cpu = Integer.parseInt(processesInfo[i]);

					if (i + 1 < sizeOfArray) {
						int memory = Integer.parseInt(processesInfo[i + 1]);

						if (i == 1)
							memory = Math.abs(memory);

						Burst cpuBurst = new CPUBurst(cpu, memory);

						bursts.add(cpuBurst);

						if (i + 2 < sizeOfArray) {
							int io = Integer.parseInt(processesInfo[i + 2]);

							Burst ioBurst = new IOBurst(io);

							bursts.add(ioBurst);
						}
					} else {
						Burst burst = new CPUBurst(cpu, 0);

						bursts.add(burst);
					}
				}

				CPUBurst currentBurst = (CPUBurst) bursts.peek();

				PCB process = new PCB(pid, currentBurst.getMemoryValue(), bursts);

				processes.add(process);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return processes;
	}
	
	public static void writeFile(LinkedList<PCB> finishedProcesses){

		// CPU Status
		int busyTime = CPU.getBusyTime();
		int idleTime = CPU.getIdleTime();

		try {
			// path to create your file
			File file = new File(Utility.OUTPUT_FILE_PATH);

			if(!file.exists()) {
					file.createNewFile();	
			}
			
			PrintWriter pw = new PrintWriter(file);
			
			pw.println("\t\t\t\t //����������[multiprogramming-os-simulation]����������\\\\");
			pw.println();
			pw.println("CPU Utilization: " + getCpuUtilization(busyTime, idleTime) + "%");
			pw.println("CPU busy time: " + busyTime + " ms");
			pw.println("CPU idle time: " + idleTime + " ms");
			pw.println("CPU total time: " + (busyTime + idleTime) + " ms");
			pw.println();
			pw.println( "#\t� process ID \t Loaded Time \t #Times in CPU \t Total in CPU \t #Times in IO \t Total in IO \t "
					+ "#Times for memory allocation \t Finished Time \t Final State");
			int i =0;
			for (PCB p : finishedProcesses){
			pw.println( ++i +"\t\t" + p.getPid() + "\t\t" + p.getLoadedTime() + "\t\t" + p.getCpuCounter() + "\t\t" + p.getCpuTotalTime() + "\t\t" + p.getIoCounter() + "\t\t" + p.getIoTotalTime() +
					" \t\t\t" + p.getMemoryCounter() + "\t\t\t" + "" + p.getFinishedTime() + "\t " + p.getProcessState() );
			pw.println();
			}
				pw.println("\t\t\t\t //��������������������-_END_-��������������������\\\\");
			pw.close();

            if(Utility.DEBUG_MODE)
			    System.out.println("Done");

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	private static double getCpuUtilization(int busyTime, int idleTime) {
		double percentage = (double) busyTime / (busyTime + idleTime);
		percentage = Math.round(percentage * 100);

		return percentage;
	}
}
