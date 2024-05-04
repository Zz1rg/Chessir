package pieces;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import main.Board;
import util.Coordinate;
import util.MoveType;

import java.util.ArrayList;

public class Pawn extends Piece {
    public Pawn(Board board, int col, int row, boolean isWhite) {
        super(board);
        this.col = col;
        this.row = row;
        this.xPos = col * Board.TILE_SIZE;
        this.yPos = row * Board.TILE_SIZE;

        this.isWhite = isWhite;
        this.name = "Pawn";

        String imagePath = isWhite ? "white_pawn.png" : "black_pawn.png";
        String classLoaderPath = ClassLoader.getSystemResource(imagePath).toString();
        this.sprite = new Image(classLoaderPath, Board.TILE_SIZE, Board.TILE_SIZE, false, false);

        ImageView imageView = new ImageView(sprite);
        this.getChildren().add(imageView);
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
        if (board.getTileNum(col, row) == board.getEnPassantTile() && this.col - 1 == col && row + colorIndex == this.row && board.getPiece(col, row + colorIndex) != null) {
            return true;
        }

        //enPassant right
        return board.getTileNum(col, row) == board.getEnPassantTile() && this.col + 1 == col && row + colorIndex == this.row && board.getPiece(col, row + colorIndex) != null;
    }

    @Override
    public boolean canMove() {
        ArrayList<Coordinate> possibleRelativeCoordinates = new ArrayList<>();
        // Use PAWN_FIRST_MOVE, so it'll always check everything
        if (isWhite()) {
            possibleRelativeCoordinates.addAll(MoveType.WHITE_PAWN_ATTACK.getRelativeCoordinates());
            possibleRelativeCoordinates.addAll(MoveType.WHITE_PAWN_FIRST_MOVE.getRelativeCoordinates());
        } else {
            possibleRelativeCoordinates.addAll(MoveType.BLACK_PAWN_ATTACK.getRelativeCoordinates());
            possibleRelativeCoordinates.addAll(MoveType.BLACK_PAWN_FIRST_MOVE.getRelativeCoordinates());
        }

        return checkMovesFrom(possibleRelativeCoordinates);
    }
}
