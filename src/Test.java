import java.util.LinkedList;
import java.util.Queue;

import Bursts.Burst;
import Bursts.CPUBurst;

public class Test {
//	public static void main(String[] args) {
//		Queue<Burst> bursts = new LinkedList<>();
//		bursts.add(new CPUBurst(1, 0));
//		bursts.add(new CPUBurst(1, 0));
//		bursts.add(new CPUBurst(1, 0));
//		bursts.add(new CPUBurst(1, 0));
//		bursts.add(new CPUBurst(1, 0));
//		
//		PCB p1 = new PCB(1, "0", 0, 12, ProcessState.NEW, bursts);
//		PCB p2 = new PCB(2, "1", 0, 32, ProcessState.NEW, bursts);
//		PCB p3 = new PCB(3, "2", 0, 44, ProcessState.NEW, bursts);
//		PCB p4 = new PCB(4, "3", 0, 55, ProcessState.NEW, bursts);
//		PCB p5 = new PCB(5, "4", 0, 66, ProcessState.NEW, bursts);
//		
//		RAM ram = new RAM(new IODevice());
//		
//		RAM.getReadyQ().add(p1);
//		RAM.getReadyQ().add(p2);
//		RAM.getReadyQ().add(p3);
//		RAM.getReadyQ().add(p4);
//		RAM.getReadyQ().add(p5);
//		
//		for(Object p : RAM.getReadyQ().toArray()) {
//			PCB process = (PCB) p;
//			System.out.print(process.getPid() + "\t");
//		}
//		
//		System.out.println("\n\n");
//		
//		ram.removeProcess(p3);
//
//		for(Object p : RAM.getReadyQ().toArray()) {
//			PCB process = (PCB) p;
//			System.out.print(process.getPid() + "\t");
//		}
//	}
}
