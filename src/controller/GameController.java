package controller;

import gui.MoveHistoryPane;
import gui.Stopwatch;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import main.Board;
import main.Move;
import pieces.Piece;
import util.Team;

import java.util.ArrayList;

public class GameController {
    private final Board board;
    private final Stopwatch blackStopwatch, whiteStopwatch;
    private boolean isWhiteTurn = true;

    public GameController(Board board) {
        this.board = board;
        this.blackStopwatch = board.getBlackStopwatch();
        this.whiteStopwatch = board.getWhiteStopwatch();
        this.blackStopwatch.setGameController(this);
        this.whiteStopwatch.setGameController(this);
        this.whiteStopwatch.startTimer();
    }

    public void swapTurn() {
        isWhiteTurn = !isWhiteTurn;
        switchTimer();
    }

    private void switchTimer() {
        if (isWhiteTurn) {
            blackStopwatch.stopTimer();
            whiteStopwatch.startTimer();
        } else {
            whiteStopwatch.stopTimer();
            blackStopwatch.startTimer();
        }
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

    public void resetGame() {
        board.getChildren().clear();
        board.pieceList.clear();
        board.moveHistory.clear();
        board.initBoard();
        isWhiteTurn = true;
        updateMoveHistory();
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
        updateMoveHistory();
    }

    public void updateMoveHistory() {
        GridPane rightComp = (GridPane) board.getRoot().getRight();
        ObservableList<Node> children = rightComp.getChildren();
        for (Node child : children) {
            if (child instanceof MoveHistoryPane) {
                ((MoveHistoryPane) child).addMove(board.getMoveHistory());
                break;
            }
        }
    }

    public void timeout(Team team) {
        switch (team) {
            case WHITE: {
//                blackStopwatch.stopTimer();
                System.out.println("WHITE TIMEOUT");
                break;
            }
            case BLACK: {
//                whiteStopwatch.stopTimer();
                System.out.println("BLACK TIMEOUT");
                break;
            }
        }
    }
}
