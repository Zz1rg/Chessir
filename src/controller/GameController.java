package controller;

import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import main.Board;
import main.Move;
import pieces.Piece;

public class GameController {
    private boolean isWhiteTurn = true;

    public void swapTurn() {
        isWhiteTurn = !isWhiteTurn;
    }

    public boolean isWhiteTurn() {
        return isWhiteTurn;
    }

    public void endGame() {
        System.exit(0);
    }

    public void checkForMate(Board board, Boolean isWhite, HBox root) {
        Piece king = board.findKing(isWhite);

        //Checkmate
        if (board.checkScanner.isKingChecked(new Move(board, king, king.getCol(), king.getRow()))) {
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

            Text staleMateText = new Text("Stalemate!");
            root.getChildren().clear();
            root.getChildren().add(staleMateText);
        }
    }
}
