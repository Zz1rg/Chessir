package util;

public enum Gamemode {
    Bullet1(1, 0),
    Bullet1i1(1, 1),
    Bullet2i1(2, 1),
    Blitz3(3, 0),
    Blitz3i2(3, 2),
    Blitz5(5, 0),
    Rapid10(10, 0),
    Rapid15i10(15, 10),
    Rapid30(30, 0);

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
