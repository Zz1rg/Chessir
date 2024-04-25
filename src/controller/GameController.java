package controller;

import gui.Stopwatch;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import main.Board;
import main.Move;
import pieces.Piece;

import java.util.ArrayList;

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
    public void checkForMate(Boolean isWhite, BorderPane root) {
        if (currentPlayerCanMove()) return;

        Piece king = board.findKing(isWhite);
        //Checkmate
        if (board.checkScanner.isKingChecked(new Move(board, king, king.getCol(), king.getRow()), false)) {
            System.out.println("Checkmate");
            Text checkMateText = new Text("Checkmate!");
            checkMateText.setFill(Color.WHITE);
            checkMateText.setFont(Font.font(50));
            root.getChildren().clear();
            root.setCenter(checkMateText);
        }
        //Stalemate
        else {
            System.out.println("Stalemate");
            Text staleMateText = new Text("Stalemate!");
            staleMateText.setFill(Color.WHITE);
            staleMateText.setFont(Font.font(50));
            root.getChildren().clear();
            root.setCenter(staleMateText);
        }
    }

    private boolean currentPlayerCanMove() {
        for (Piece piece : board.pieceList) {
            if (piece.isWhite == isWhiteTurn() && piece.canMove()) return true;
        }
        return false;
    }

    private void resetGame() {
        board.getChildren().clear();
        board.pieceList.clear();
        board.moveHistory.clear();
        board.initBoard();
        isWhiteTurn = true;
    }

    public void undoMove() {
        if (board.moveHistory.isEmpty()) return;
        board.moveHistory.remove(board.getMoveHistory().size() - 1);
        ArrayList<Move> moveHistoryCopy = new ArrayList<>(board.getMoveHistory());
        resetGame();
        for (Move move : moveHistoryCopy) {
            board.makeMove(new Move(board, board.getPiece(move.getOldCol(), move.getOldRow()), move.getNewCol(), move.getNewRow()));
        }
        board.getChildren().clear();
        board.paint();
    }
}
