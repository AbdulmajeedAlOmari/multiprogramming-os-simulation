import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;
import javax.swing.*;

import Bursts.Burst;

public class GUI extends JFrame implements Runnable {
	JLabel l;

	Container c = getContentPane();
	Graphics graphics;

	public GUI() {
		l = new JLabel(getRamUsage());
		c.setLayout(new FlowLayout());
		c.add(l);
		setTitle("RAM View");
		setSize(2000, 600);
		setVisible(true);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.graphics = c.getGraphics();
	}

	public void paint(Graphics g) {
		g.drawRect(30, 200, 1385, 200);
		g.setColor(Color.WHITE);

		int starting = 40;
		g.setColor(Color.RED);

		// Get all ready processes
		Object[] arr = RAM.getReadyQ().toArray();

		for (Object obj : arr) {
			PCB process = (PCB) obj;
			int ending = starting + process.getSize();
			g.drawRect(starting, 200, ending, 200);
			g.drawString(getRamUsage(), 100, 150);
			starting = ending;
		}
	}

	@Override
	public void run() {
		// At first paint the graphics
		paint(graphics);

		while (true) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			// Repaint rectangle
			removeAll();
			revalidate();
			repaint();
		}
	}

	private String getRamUsage() {

		double totalUsage = RAM.getTotalRamUsage();
		int totalSize = RAM.RAM_SIZE + RAM.ADDITIONAL_RAM_SIZE;

		// Calculate percentage
		double percentage = (totalUsage / totalSize) * 100;

		// Round percentage to 2 decimal places
		percentage = Math.round(percentage * 100.0) / 100.0;

		return "RAM usage: " + percentage + "%";
	}
}
