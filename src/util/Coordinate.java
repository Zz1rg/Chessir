package util;

public abstract class Coordinate {
    protected final int row;
    protected final int col;

    protected Coordinate(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public Coordinate add(Coordinate coordinate) {
        return new CoordinateRC(this.row + coordinate.getRow(), this.col + coordinate.getCol());
    }
}
