public class Burst {
    private int time;
    private BurstType type;

    public Burst(int time, BurstType type) {
        this.time = time;
        this.type = type;
    }

    /***
     * @return new time value
     */
    int decrementTime() {
        return --this.time;
    }

    BurstType getType() {
        return this.type;
    }
}
