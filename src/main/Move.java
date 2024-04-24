package main;

import pieces.Piece;
import util.Coordinate;

public class Move {
    public int oldCol;
    public int oldRow;
    public int newCol;
    public int newRow;

    public Piece piece;
    public Piece capturedPiece;

    public Move(Board board, Piece piece, int newCol, int newRow) {
        this.piece = piece;
        this.oldCol = piece.col;
        this.oldRow = piece.row;
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

    public void setOldCol(int oldCol) {
        this.oldCol = oldCol;
    }

    public int getOldRow() {
        return oldRow;
    }

    public void setOldRow(int oldRow) {
        this.oldRow = oldRow;
    }

    public int getNewCol() {
        return newCol;
    }

    public void setNewCol(int newCol) {
        this.newCol = newCol;
    }

    public int getNewRow() {
        return newRow;
    }

    public void setNewRow(int newRow) {
        this.newRow = newRow;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public Piece getCapturedPiece() {
        return capturedPiece;
    }

    public void setCapturedPiece(Piece capturedPiece) {
        this.capturedPiece = capturedPiece;
    }
}
