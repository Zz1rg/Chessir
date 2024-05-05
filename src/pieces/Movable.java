package pieces;

public interface Movable {
        boolean isValidMovement(int col, int row);
        boolean moveCollidesWithPiece(int col, int row);
        void firstMoved();
}
