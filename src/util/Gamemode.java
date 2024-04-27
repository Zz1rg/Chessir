package util;

public enum Gamemode {
    BULLET1(1, 0),
    BULLET1I1(1, 1),
    BULLET2I1(2, 1),
    BLITZ3(3, 0),
    BLITZ3I2(3, 2),
    BLITZ5(5, 0),
    RAPID10(10, 0),
    RAPID15I10(15, 10),
    RAPID30(30, 0);

    private final int minutes;
    private final int incrementSecs;

    Gamemode(int minutes, int incrementSecs) {
        this.minutes = minutes;
        this.incrementSecs = incrementSecs;
    }

    public int getSeconds() {
        return minutes * 60;
    }

    public int getIncrementSecs() {
        return incrementSecs;
    }
}
