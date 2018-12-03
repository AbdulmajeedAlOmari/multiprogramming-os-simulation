package Bursts;

public abstract class Burst {
    private int remainingTime;

    public Burst(int time) {
        this.remainingTime = time;
    }

    public int getRemainingTime() {
        return this.remainingTime;
    }
    public int decrementRemainingTime() {
        return --this.remainingTime;
    }
}
