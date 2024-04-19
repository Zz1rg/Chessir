package main;

import pieces.Piece;

public class Move {
    public int oldCol;
    public int oldRow;
    public int newCol;
    public int newRow;

    Piece piece;
    Piece capturedPiece;

    public Move(Board board, Piece piece, int newCol, int newRow) {
        this.piece = piece;
        this.oldCol = piece.col;
        this.oldRow = piece.row;
        this.newCol = newCol;
        this.newRow = newRow;
        this.capturedPiece = board.getPiece(newCol, newRow);
    }
}
