/***
 * This class will behave like a clock for our Operating System
 * Time will be in Milliseconds
 *
 * Note: it will be incremented by the CPU
 */
public class Clock {
    private static int currentTime = 0;
    static int getCurrentMs() { return Clock.currentTime; }
    static void incrementMs() { Clock.currentTime++; }
}
