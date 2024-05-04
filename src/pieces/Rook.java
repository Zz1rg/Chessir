package pieces;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import main.Board;
import util.MoveType;

public class Rook extends Piece {
    public Rook(Board board, int col, int row, boolean isWhite) {
        super(board);
        this.col = col;
        this.row = row;
        this.xPos = col * board.TILE_SIZE;
        this.yPos = row * board.TILE_SIZE;

        this.isWhite = isWhite;
        this.name = "Rook";

        String imagePath = isWhite ? "white_rook.png" : "black_rook.png";
        String classLoaderPath = ClassLoader.getSystemResource(imagePath).toString();
        this.sprite = new Image(classLoaderPath, board.TILE_SIZE, board.TILE_SIZE, false, false);

        ImageView imageView = new ImageView(sprite);
        this.getChildren().add(imageView);
    }

    @Override
    public boolean isValidMovement(int col, int row) {
        return this.col == col || this.row == row;
    }

    @Override
    public boolean moveCollidesWithPiece(int col, int row) {
        //left
        if (this.col > col) {
            for (int c = this.col - 1; c > col; c--) {
                if (board.getPiece(c, this.row) != null) {
                    return true;
                }
            }
        }
        //right
        if (this.col < col) {
            for (int c = this.col + 1; c < col; c++) {
                if (board.getPiece(c, this.row) != null) {
                    return true;
                }
            }
        }
        //up
        if (this.row > row) {
            for (int r = this.row - 1; r > row; r--) {
                if (board.getPiece(this.col, r) != null) {
                    return true;
                }
            }
        }
        //down
        if (this.row < row) {
            for (int r = this.row + 1; r < row; r++) {
                if (board.getPiece(this.col, r) != null) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean canMove() {
        return checkMovesFrom(MoveType.CROSS.getRelativeCoordinates());
    }
}
