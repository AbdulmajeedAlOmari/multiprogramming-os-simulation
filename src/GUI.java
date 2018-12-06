import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;
import javax.swing.*;

import Bursts.Burst;

public class GUI extends JFrame implements Runnable {
	Object[] arr;
	JLabel l;

	Container c = getContentPane();
	Graphics graphics;

	public GUI() {
		l = new JLabel("RAM usge: " + (RAM.RAM_SIZE + RAM.ADDITIONAL_RAM_SIZE) + " %");
		c.setLayout(new FlowLayout());
		c.add(l);
		setTitle("RAM View");
		setSize(2000, 600);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.arr = RAM.getReadyQ().toArray();
		this.graphics = c.getGraphics();

	}

	public void paint(Graphics g) {
		g.drawRect(30, 200, 1385, 200);
		g.setColor(Color.WHITE);
		int starting = 40;
		g.setColor(Color.RED);
		for (Object obj : arr) {
			PCB process = (PCB) obj;
			int ending = starting + process.getSize();
			g.drawRect(starting, 200, ending, 200);
			g.drawString("RAM usge: " + (((RAM.RAM_SIZE + RAM.ADDITIONAL_RAM_SIZE) / 160) * 100 + " %"), 100, 150);

			starting = ending;
		}

		try {
			Thread.sleep(100 + (long) (Math.random() * 100));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// do{
		//
		// g.fillRect(20, 200, 60, 200);
		// g.fillRect(90, 200, 110, 200);
		// g.drawRect(10, 200, 1900, 200);
		// g.setColor(Color.WHITE);
		// for(int i = 0; i < arr.length; i++) {
		// g.fillRect((arr[i].getsize()*0.1),200, arr[i].getsize, 200);
		// g.setColor(Color.RED);
		// }
		// }while(arr[0].getsize !=0)

	}

	@Override
	public void run() {
		while (true) {
			paint(graphics);
		}
	}

//	public static void main(String[] args) {
//		Queue<Burst> bursts = new LinkedList<Burst>();
//
//		// bursts.add(new CPUBurst(0, 10));
//		// bursts.add(new CPUBurst(0, 10));
//		// bursts.add(new CPUBurst(0, 10));
//		// bursts.add(new CPUBurst(0, 10));
//		// bursts.add(new CPUBurst(0, 10));
//		// bursts.add(new CPUBurst(0, 10));
//		//
//		// PCB p1 = new PCB(1, "1", 0, 20, ProcessState.WAITING, bursts);
//
//		LinkedList<PCB> processes = FileHandler.readFile();
//
//		for (PCB p : processes) {
//			RAM.getReadyQ().add(p);
//		}
//
//		GUI gui = new GUI();
//		Thread t = new Thread(gui);
//		t.start();
//
//	}
}
