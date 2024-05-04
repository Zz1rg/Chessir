package pieces;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import main.Board;
import util.MoveType;

public class Knight extends Piece {
    public Knight(Board board, int col, int row, boolean isWhite) {
        super(board);
        this.col = col;
        this.row = row;
        this.xPos = col * Board.TILE_SIZE;
        this.yPos = row * Board.TILE_SIZE;

        this.isWhite = isWhite;
        this.name = "Knight";

        String imagePath = isWhite ? "white_knight.png" : "black_knight.png";
        String classLoaderPath = ClassLoader.getSystemResource(imagePath).toString();
        this.sprite = new Image(classLoaderPath, Board.TILE_SIZE, Board.TILE_SIZE, false, false);

        ImageView imageView = new ImageView(sprite);
        this.getChildren().add(imageView);
    }

    @Override
    public boolean isValidMovement(int col, int row) {
        return Math.abs(this.col - col) * Math.abs(this.row - row) == 2;
    }

    @Override
    public boolean moveCollidesWithPiece(int col, int row) {
        return super.moveCollidesWithPiece(col, row);
    }

    @Override
    public boolean canMove() {
        return checkMovesFrom(MoveType.KNIGHT.getRelativeCoordinates());
    }
}
