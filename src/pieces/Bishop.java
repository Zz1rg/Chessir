package pieces;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import main.Board;
import util.MoveType;

public class Bishop extends Piece {
    public Bishop(Board board, int col, int row, boolean isWhite) {
        super(board);
        this.col = col;
        this.row = row;
        this.xPos = col * board.tileSize;
        this.yPos = row * board.tileSize;

        this.isWhite = isWhite;
        this.name = "Bishop";

        String imagePath = isWhite ? "white_bishop.png" : "black_bishop.png";
        String classLoaderPath = ClassLoader.getSystemResource(imagePath).toString();
        this.sprite = new Image(classLoaderPath, board.tileSize, board.tileSize, false, false);

        ImageView imageView = new ImageView(sprite);
        this.getChildren().add(imageView);
    }

    @Override
    public boolean isValidMovement(int col, int row) {
        return Math.abs(this.col - col) == Math.abs(this.row - row);
    }

    @Override
    public boolean moveCollidesWithPiece(int col, int row) {
        //up left
        if (this.col > col && this.row > row) {
            for (int c = this.col - 1, r = this.row - 1; c > col; c--, r--) {
                if (board.getPiece(c, r) != null) {
                    return true;
                }
            }
        }
        //up right
        if (this.col < col && this.row > row) {
            for (int c = this.col + 1, r = this.row - 1; c < col; c++, r--) {
                if (board.getPiece(c, r) != null) {
                    return true;
                }
            }
        }
        //down left
        if (this.col > col && this.row < row) {
            for (int c = this.col - 1, r = this.row + 1; c > col; c--, r++) {
                if (board.getPiece(c, r) != null) {
                    return true;
                }
            }
        }
        //down right
        if (this.col < col && this.row < row) {
            for (int c = this.col + 1, r = this.row + 1; c < col; c++, r++) {
                if (board.getPiece(c, r) != null) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean canMove() {
        return checkMovesFrom(MoveType.DIAGONAL.getRelativeCoordinates());
    }
}
