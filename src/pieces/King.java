package pieces;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import main.Board;
import main.Move;
import util.MoveType;

public class King extends Piece {
    public King(Board board, int col, int row, boolean isWhite) {
        super(board);
        this.col = col;
        this.row = row;
        this.xPos = col * Board.TILE_SIZE;
        this.yPos = row * Board.TILE_SIZE;

        this.isWhite = isWhite;
        this.name = "King";

        String imagePath = isWhite ? "white_king.png" : "black_king.png";
        String classLoaderPath = ClassLoader.getSystemResource(imagePath).toString();
        this.sprite = new Image(classLoaderPath, Board.TILE_SIZE, Board.TILE_SIZE, false, false);

        ImageView imageView = new ImageView(sprite);
        this.getChildren().add(imageView);
    }

    @Override
    public boolean isValidMovement(int col, int row) {
        return Math.abs(this.col - col) <= 1 && Math.abs(this.row - row) <= 1 || canCastle(col, row);
    }

    @Override
    public boolean canMove() {
        return checkMovesFrom(MoveType.KING.getRelativeCoordinates());
    }

    private boolean canCastle(int col, int row) {
        if (this.isFirstMove && row == this.row) {
            if (col == 6) {
                Piece rook = board.getPiece(7, row);
                if (rook != null && rook instanceof Rook && rook.isFirstMove) {
                    return board.getPiece(5, row) == null
                            && board.getPiece(6, row) == null
                            && !board.checkScanner.isKingChecked(new Move(board, this, 5, row), false)
                            && !board.checkScanner.isKingChecked(new Move(board, this, 4, row), false);
                }
            } else if (col == 2) {
                Piece rook = board.getPiece(0, row);
                if (rook != null && rook instanceof Rook && rook.isFirstMove) {
                    return board.getPiece(1, row) == null
                            && board.getPiece(2, row) == null
                            && board.getPiece(3, row) == null
                            && !board.checkScanner.isKingChecked(new Move(board, this, 3, row), false)
                            && !board.checkScanner.isKingChecked(new Move(board, this, 4, row), false);
                }
            }
        }
        return false;
    }
}
