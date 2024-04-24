package controller;

import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import main.Board;
import main.Move;
import pieces.Piece;

public class GameController {
    private final Board board;
    private boolean isWhiteTurn = true;

    public GameController(Board board) {
        this.board = board;
    }

    public void swapTurn() {
        isWhiteTurn = !isWhiteTurn;
    }

    public boolean isWhiteTurn() {
        return isWhiteTurn;
    }

    public void endGame() {
        System.exit(0);
    }

    // Called everytime after making a move
    public void checkForMate(Boolean isWhite, HBox root) {
        if (currentPlayerCanMove()) return;

        Piece king = board.findKing(isWhite);
        //Checkmate
        if (board.checkScanner.isKingChecked(new Move(board, king, king.getCol(), king.getRow()))) {
            System.out.println("Checkmate");
            Text checkMateText = new Text("Checkmate!");
            root.getChildren().clear();
            root.getChildren().add(checkMateText);
        }
        //Stalemate
        else {
            System.out.println("Stalemate");
            Text staleMateText = new Text("Stalemate!");
            root.getChildren().clear();
            root.getChildren().add(staleMateText);
        }
    }

    private boolean currentPlayerCanMove() {
        for (Piece piece : board.pieceList) {
            if (piece.isWhite == isWhiteTurn() && piece.canMove()) return true;
        }
        return false;
    }
}
