package controller;

import gui.MoveHistoryPane;
import gui.Stopwatch;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import main.Board;
import main.Move;
import pieces.Piece;
import util.EndGame;
import util.MoveRecord;
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
    public void checkForMate(Boolean isWhite) {
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
        for (Piece piece : board.getPieceList()) {
            if (piece.isWhite() == isWhiteTurn() && piece.canMove()) return true;
        }
        return false;
    }

    public void resetGame() {
        board.getWhiteStopwatch().stopTimer();
        board.getBlackStopwatch().stopTimer();
        SceneController.switchToBoard(board.getGamemode());
    }

    public void undoMove() {
        if (board.getMoveHistory().isEmpty()) return;
        MoveRecord lastMoveRecord = board.getMoveHistory().get(board.getMoveHistory().size() - 1);
        board.getMoveHistory().remove(board.getMoveHistory().size() - 1);
        Move lastMove = lastMoveRecord.move();
        board.setEnPassantTile(lastMoveRecord.enPassantTile());

        Piece lastPiece = lastMove.getPiece();
        lastPiece.setCol(lastMove.getOldCol());
        lastPiece.setRow(lastMove.getOldRow());
        lastPiece.setxPos(lastMove.getOldCol() * Board.TILE_SIZE);
        lastPiece.setyPos(lastMove.getOldRow() * Board.TILE_SIZE);
        lastPiece.setFirstMove(lastMoveRecord.isPieceFirstMove());
        if (lastMoveRecord.isCastle()) {
            Piece rook = board.getPiece(lastMove.getNewCol() == 6 ? 5 : 3, lastMove.getNewRow());
            rook.setCol(lastMove.getNewCol() == 6 ? 7 : 0);
            rook.setxPos(rook.getCol() * Board.TILE_SIZE);
            rook.setFirstMove(lastMoveRecord.isPieceFirstMove());
        }

        Piece lastCapturedPiece = lastMove.getCapturedPiece();
        if (lastCapturedPiece != null) {
            lastCapturedPiece.setCol(lastMove.getNewCol());
            lastCapturedPiece.setRow(lastMove.getNewRow());
            board.getPieceList().add(lastCapturedPiece);
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
                ((MoveHistoryPane) child).addMoveRecord(board.getMoveHistory());
                break;
            }
        }
    }

    public void timeout(Team team) {
        switch (team) {
            case WHITE: {
                board.getWhiteStopwatch().stopTimer();
                board.getBlackStopwatch().stopTimer();
//                System.out.println("WHITE TIMEOUT");
                SceneController.switchToEndGame(EndGame.TIMEOUT, Team.BLACK);
                break;
            }
            case BLACK: {
                board.getBlackStopwatch().stopTimer();
                board.getWhiteStopwatch().stopTimer();
//                System.out.println("BLACK TIMEOUT");
                SceneController.switchToEndGame(EndGame.TIMEOUT, Team.WHITE);
                break;
            }
        }
    }
}
