package pieces;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import main.Board;

import java.awt.image.BufferedImage;

public class Queen extends Piece {
    public Queen(Board board, int col, int row, boolean isWhite) {
        super(board);
        this.col = col;
        this.row = row;
        this.xPos = col* board.tileSize;
        this.yPos = row* board.tileSize;

        this.isWhite = isWhite;
        this.name = "Queen";

        String imagePath = isWhite ? "white_queen.png" : "black_queen.png";
        String classLoaderPath = ClassLoader.getSystemResource(imagePath).toString();
        this.sprite = new Image(classLoaderPath, board.tileSize, board.tileSize, false, false);

        ImageView imageView = new ImageView(sprite);
        this.getChildren().add(imageView);
    }

    @Override
    public boolean isValidMovement(int col, int row) {
        return (Math.abs(this.col - col) == Math.abs(this.row - row)) || (this.col == col || this.row == row);
    }

    @Override
    public boolean moveCollidesWithPiece(int col, int row) {
        if (this.col==col || this.row==row) {
            //left
            if(this.col > col) {
                for (int c=this.col-1; c>col; c--) {
                    if (board.getPiece(c, this.row) != null) {
                        return true;
                    }
                }
            }
            //right
            if(this.col < col) {
                for (int c=this.col+1; c<col; c++) {
                    if (board.getPiece(c, this.row) != null) {
                        return true;
                    }
                }
            }
            //up
            if(this.row > row) {
                for (int r=this.row-1; r>row; r--) {
                    if (board.getPiece(this.col, r) != null) {
                        return true;
                    }
                }
            }
            //down
            if(this.row < row) {
                for (int r=this.row+1; r<row; r++) {
                    if (board.getPiece(this.col, r) != null) {
                        return true;
                    }
                }
            }
        } else {
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
        }

        return false;
    }
}
