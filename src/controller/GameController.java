package controller;

import gui.MoveHistoryPane;
import gui.Stopwatch;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import main.Board;
import main.Move;
import pieces.Piece;
import util.EndGame;
import util.Team;

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

    public void switchTimer() {
        if (isWhiteTurn) {
            blackStopwatch.stopTimer();
            blackStopwatch.addIncrement();

            whiteStopwatch.startTimer();
        } else {
            whiteStopwatch.stopTimer();
            whiteStopwatch.addIncrement();

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
            board.getBlackStopwatch().stopTimer();
            board.getWhiteStopwatch().stopTimer();
            SceneController.switchToEndGame(EndGame.CHECKMATE, isWhite ? Team.BLACK : Team.WHITE);
        }
        //Stalemate
        else {
            System.out.println("Stalemate");
            board.getBlackStopwatch().stopTimer();
            board.getWhiteStopwatch().stopTimer();
            SceneController.switchToEndGame(EndGame.STALEMATE, null);
        }
    }

    private boolean currentPlayerCanMove() {
        for (Piece piece : board.pieceList) {
            if (piece.isWhite == isWhiteTurn() && piece.canMove()) return true;
        }
        return false;
    }

    public void resetGame() {
        SceneController.switchToBoard(board.getGamemode());
    }

    public void undoMove() {
        if (board.moveHistory.isEmpty()) return;
        Move lastMove = board.moveHistory.get(board.moveHistory.size() - 1);
        board.moveHistory.remove(board.getMoveHistory().size() - 1);
        lastMove.getPiece().setCol(lastMove.getOldCol());
        lastMove.getPiece().setRow(lastMove.getOldRow());
        lastMove.getPiece().setxPos(lastMove.getOldCol() * board.tileSize);
        lastMove.getPiece().setyPos(lastMove.getOldRow() * board.tileSize);
        if (lastMove.getCapturedPiece() != null) {
            lastMove.getCapturedPiece().setCol(lastMove.getNewCol());
            lastMove.getCapturedPiece().setRow(lastMove.getNewRow());
            board.getPieceList().add(lastMove.getCapturedPiece());
        }
        swapTurn();
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
                board.getWhiteStopwatch().stopTimer();
//                System.out.println("WHITE TIMEOUT");
                SceneController.switchToEndGame(EndGame.TIMEOUT, Team.BLACK);
                break;
            }
            case BLACK: {
                board.getBlackStopwatch().stopTimer();
//                System.out.println("BLACK TIMEOUT");
                SceneController.switchToEndGame(EndGame.TIMEOUT, Team.WHITE);
                break;
            }
        }
    }
}
