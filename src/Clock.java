/***
 * This class will behave like a clock for our Operating System
 * Time will be in Milliseconds
 */
public class Clock extends Thread {
    private long initialTime;

    public Clock() {
        this.initialTime = System.currentTimeMillis();
    }

    @Override
    public void run() {
        while(true) {
            try {
                RAM.longTermScheduler();
                sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    int getCurrentMs() {
        return (int) (System.currentTimeMillis() - this.initialTime);
    }
}
