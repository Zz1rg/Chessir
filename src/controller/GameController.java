package controller;

import gui.Stopwatch;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import main.Board;
import main.Move;
import pieces.Piece;
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
    public void checkForMate(Boolean isWhite, Pane root) {
        if (currentPlayerCanMove()) return;

        Piece king = board.findKing(isWhite);
        //Checkmate
        if (board.checkScanner.isKingChecked(new Move(board, king, king.getCol(), king.getRow()), false)) {
            for (Piece piece : board.pieceList) {
                if (piece.isWhite == isWhite) {
                    for (int r = 0; r < 8; r++) {
                        for (int c = 0; c < 8; c++) {
                            if (board.isValidMove(new Move(board, piece, c, r))) {
                                System.out.println("C Not checkmate " + piece.name + " " + c + " " + r);
                                return;
                            }
                        }
                    }
                }
            }
            System.out.println("Checkmate");
            Text checkMateText = new Text("Checkmate!");
            root.getChildren().clear();
            root.getChildren().add(checkMateText);
        }
        //Stalemate
        else {
            for (Piece piece : board.pieceList) {
                if (piece.isWhite == isWhite) {
                    for (int r = 0; r < 8; r++) {
                        for (int c = 0; c < 8; c++) {
                            if (board.isValidMove(new Move(board, piece, c, r))) {
                                System.out.println("S Not checkmate " + piece.name + " " + c + " " + r);
                                return;
                            }
                        }
                    }
                }
            }
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
