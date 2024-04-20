package pieces;

import main.Board;

import java.awt.image.BufferedImage;

public class Pawn extends Piece {
    public Pawn(Board board, int col, int row, boolean isWhite) {
        super(board);
        this.col = col;
        this.row = row;
        this.xPos = col* board.tileSize;
        this.yPos = row* board.tileSize;

        this.isWhite = isWhite;
        this.name = "Pawn";

        this.sprite = sheet.getSubimage(5 * sheetScale, isWhite ? 0:sheetScale, sheetScale, sheetScale).getScaledInstance(board.tileSize, board.tileSize, BufferedImage.SCALE_SMOOTH);
    }

    @Override
    public boolean isValidMovement(int col, int row) {
        int colorIndex = isWhite ? 1 : -1;

        //push1
        if (this.col == col && this.row - colorIndex == row && board.getPiece(col, row) == null) {
            return true;
        }

        //push2
        if (isFirstMove && this.col == col && this.row - colorIndex * 2 == row && board.getPiece(col, row) == null && board.getPiece(col, row + colorIndex) == null) {
            return true;
        }

        //capture left
        if (this.col - 1 == col && row + colorIndex == this.row && board.getPiece(col, row) != null) {
            return true;
        }

        //capture right
        if (this.col + 1 == col && row + colorIndex == this.row && board.getPiece(col, row) != null) {
            return true;
        }

        //enPassant left
        if (board.getTileNum(col, row) == board.enPassantTile && this.col - 1 == col && row + colorIndex == this.row && board.getPiece(col, row + colorIndex) != null) {
            return true;
        }

        //enPassant right
        if (board.getTileNum(col, row) == board.enPassantTile && this.col + 1 == col && row + colorIndex == this.row && board.getPiece(col, row + colorIndex) != null) {
            return true;
        }

        return false;
    }
}
