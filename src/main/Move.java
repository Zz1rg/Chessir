package main;

import pieces.Piece;
import util.Coordinate;

public class Move {
    public final int oldCol;
    public final int oldRow;
    public final int newCol;
    public final int newRow;

    public final Piece piece;
    private Piece capturedPiece;

    public Move(Board board, Piece piece, int newCol, int newRow) {
        this.piece = piece;
        this.oldCol = piece.getCol();
        this.oldRow = piece.getRow();
        this.newCol = newCol;
        this.newRow = newRow;
        this.capturedPiece = board.getPiece(newCol, newRow);
    }

    public Move(Board board, Piece piece, Coordinate newCoordinate) {
        this(board, piece, newCoordinate.getCol(), newCoordinate.getRow());
    }

    public int getOldCol() {
        return oldCol;
    }

    public int getOldRow() {
        return oldRow;
    }

    public int getNewCol() {
        return newCol;
    }

    public int getNewRow() {
        return newRow;
    }

    public Piece getPiece() {
        return piece;
    }

    public Piece getCapturedPiece() {
        return capturedPiece;
    }

    public void setCapturedPiece(Piece capturedPiece) {
        this.capturedPiece = capturedPiece;
    }
}
