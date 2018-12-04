import java.io.*;
import java.util.*;

import Bursts.Burst;
import Bursts.CPUBurst;
import Bursts.IOBurst;

public class FileHandler {

	public static void readFile() {
		String processID, bursTime, processSize, IOTime;
		String line;
		LinkedList<PCB> processes = new LinkedList<>();

		try {
			BufferedReader bfr = new BufferedReader(new FileReader("C:\\Users\\A.N\\Desktop\\cpumemoryio.txt"));
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

				PCB process = new PCB(pid, String.valueOf(pid), 0, currentBurst.getMemoryValue(), ProcessState.NEW,
						bursts);

				processes.add(process);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		for(PCB p : processes) {
			System.out.println("--- " + p.getPid() + " ---");
			CPUBurst first = (CPUBurst) p.getCurrentBurst();
			System.out.print(first.getRemainingTime() + "\t");
			System.out.print(first.getMemoryValue() + "\t");
			
			Queue<Burst> bursts = p.burstQueue;
			
			while(bursts.peek() != null) {
				Burst current = bursts.poll();
				if(current instanceof CPUBurst) {
					CPUBurst cpu = (CPUBurst) current;
					System.out.print(cpu.getRemainingTime() + "\t");
					System.out.print(cpu.getMemoryValue() + "\t");
				} else {
					System.out.print(current.getRemainingTime() + "\t");
				}
			}
			System.out.println();
		}

		// while)
		// System.out.println(num);

		// Here is the actual code
		// ----------------------------------------
		// for (int i = 1; i < num; i++) {
		// bursTime = processesInfo[i].split("\t")[0].trim();
		// processSize = processesInfo[(i + 1)].split("\t")[0].trim();
		// IOTime = processesInfo[(i + 2)].split("\t")[0].trim();
		// String info1[] = { bursTime, processSize, IOTime };
		// temp2.add(info1);
		// }

		// processID = processesInfo[0].split("\t")[0].trim();
		// System.out.println(processID);
		// PCB process = new PCB(processID, bursTime, processSize, IOTime, State.New);
		// Here we add the processes to the processes Q and to the Job Q
		// for (String[] pro : temp2) {
		// for (String infotmation : pro)
		// System.out.print(infotmation + "\t");
		// System.out.println();
		// }

	}

	/*
	 * private static void outtpt() {
	 * 
	 * int terminated = 0; int killed = 0; int processesCPUBound = 0; //processes
	 * list for (PCB element : (processes list)){
	 * 
	 * programmeSize += element.size;
	 * 
	 * if(element.state == State.Terminated) processesNormally++; else
	 * if(element.state == State.Killed) killed++;
	 * 
	 * if(element.getIOTime() > element.getIOCounter()) processesCPUBound++; }
	 * System.out.println(
	 * "---------------------------------------------------------------------------------------------"
	 * ); PCB temp; System.out.println("1-Process ID: " + temp.getPid());
	 * System.out.println("2-The Program name: " );
	 * System.out.println("3-When it was loded into the ready queue: " );
	 * System.out.println("4-Total time spent in the CPU: " );
	 * System.out.println("5-Number of times it preformed an IO: " );
	 * System.out.println("6-Total time spent in performing IO: ");
	 * System.out.println("7-Number of times it was wating for memory: ");
	 * System.out.println("8-Time it was terminated or was killed: ");
	 * System.out.println("9-Its final state: ");
	 * System.out.println("CUP utilization: "); System.out.println(
	 * "---------------------------------------------------------------------------------------------"
	 * ); }
	 * 
	 * public static void writeOnTextFile() { try { ArrayList<Integer> size = new
	 * ArrayList<>(); BufferedWriter bfw = new BufferedWriter(new
	 * FileWriter("Processes.txt")); bfw.close(); } catch (Exception e) {
	 * System.out.println(e.getMessage()); } }
	 */
	public static void main(String[] args) {
		readFile();
	}
}
