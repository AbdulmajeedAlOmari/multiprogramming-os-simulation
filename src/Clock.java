/***
 * This class will behave like a clock for our Operating System
 * Time will be in Milliseconds
 */
public class Clock {
    private long initialTime;

    Clock() {
        this.initialTime = System.currentTimeMillis();
    }

    int getCurrentMs() {
        return (int) (System.currentTimeMillis() - this.initialTime);
    }
}
