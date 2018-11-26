public class Burst {
    private int remainingTime;
    private BurstType type;

    public Burst(int time, BurstType type) {
        this.remainingTime = time;
        this.type = type;
    }

    int getRemainingTime() {
        return this.remainingTime;
    }

    /***
     * @return new time value
     */
    int decrementRemainingTime() {
        return --this.remainingTime;
    }

    BurstType getType() {
        return this.type;
    }
}
