package pieces;

import main.Board;

import java.awt.image.BufferedImage;

public class Bishop extends Piece {
    public Bishop(Board board, int col, int row, boolean isWhite) {
        super(board);
        this.col = col;
        this.row = row;
        this.xPos = col* board.tileSize;
        this.yPos = row* board.tileSize;

        this.isWhite = isWhite;
        this.name = "Bishop";

        this.sprite = sheet.getSubimage(2 * sheetScale, isWhite ? 0:sheetScale, sheetScale, sheetScale).getScaledInstance(board.tileSize, board.tileSize, BufferedImage.SCALE_AREA_AVERAGING);
    }

    @Override
    public boolean isValidMovement(int col, int row) {
        return Math.abs(this.col - col) == Math.abs(this.row - row);
    }

    @Override
    public boolean moveCollidesWithPiece(int col, int row) {
        //up left
        if(this.col > col && this.row > row) {
            for (int c=this.col-1, r=this.row-1; c>col; c--, r--) {
                if (board.getPiece(c, r) != null) {
                    return true;
                }
            }
        }
        //up right
        if(this.col < col && this.row > row) {
            for (int c=this.col+1, r=this.row-1; c<col; c++, r--) {
                if (board.getPiece(c, r) != null) {
                    return true;
                }
            }
        }
        //down left
        if(this.col > col && this.row < row) {
            for (int c=this.col-1, r=this.row+1; c>col; c--, r++) {
                if (board.getPiece(c, r) != null) {
                    return true;
                }
            }
        }
        //down right
        if(this.col < col && this.row < row) {
            for (int c=this.col+1, r=this.row+1; c<col; c++, r++) {
                if (board.getPiece(c, r) != null) {
                    return true;
                }
            }
        }

        return false;
    }
}
