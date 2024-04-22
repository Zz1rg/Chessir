package main;

import controller.GameController;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import pieces.*;

import javax.swing.*;
import java.awt.*;

import java.util.ArrayList;
import java.util.function.Function;

public class Board extends GridPane {

    public int tileSize = 85;
    public int cols = 8;
    public int rows = 8;

    public ArrayList<Piece> pieceList = new ArrayList<>();

    public Piece selectedPiece;

    // TODO: migrate game logic into GameController class ?
    //  (e.g. checkScanner, enPassantTile, makeMove, isValidMove, sameTeam, getPiece, findKing, capture, getTileNum, paint)
    private final GameController gameController = new GameController();

    public CheckScanner checkScanner = new CheckScanner(this);

    public int enPassantTile = -1;

    public Board() {
        this.setPrefHeight(rows * tileSize);
        this.setPrefWidth(cols * tileSize);

        //set numbers of col and row
        for (int i = 0; i < cols; i++) {
            this.getColumnConstraints().add(new ColumnConstraints(tileSize));
        }
        for (int i = 0; i < rows; i++) {
            this.getRowConstraints().add(new RowConstraints(tileSize));
        }

        this.setOnMousePressed(e -> {
            int col = (int) (e.getX() / tileSize);
            int row = (int) (e.getY() / tileSize);
            Piece clickedPiece = getPiece(col, row);

            // TODO: This looks too complicated, simplify it? ----IT's FINE
            if (selectedPiece == null) {
                selectPiece(clickedPiece);
            } else {
                if (clickedPiece == selectedPiece) {
                    selectedPiece = null;
                } else {
                    if (sameTeam(clickedPiece, selectedPiece)) {
                        selectPiece(clickedPiece);
                    } else {
                        Move move = new Move(this, selectedPiece, col, row);
                        makeMove(move);
                        // TODO: Move isValidMove(move) into makeMove(Move)? ----DONE
                        // (Not important) Check clickedPiece != selectedPiece in isValidMove(Move)? ----Not needed
                        // TODO: makeMove(Move) should also clear selectedPiece? a bit redundant but it probably should do that ----DONE
                        // TODO: Move swapTurn into makeMove(Move) ----DONE
                    }
                }
            }
            getChildren().clear();
            paint();
        });

        addPieces();
        paint();
    }

    // This function is only used in onMousePressed
    private void selectPiece(Piece piece) {
        if (piece == null) {
            return;
        }
        if (piece.isWhite != gameController.isWhiteTurn()) {
            return;
        }
        selectedPiece = piece;
    }

    public void addPieces() {
        for (int i = 0; i <= 7; i++) {
            pieceList.add(new Pawn(this, i, 1, false));
        }

        for (int i = 0; i <= 7; i++) {
            pieceList.add(new Pawn(this, i, 6, true));
        }

        pieceList.add(new Knight(this, 1, 0, false));
        pieceList.add(new Knight(this, 6, 0, false));
        pieceList.add(new Knight(this, 1, 7, true));
        pieceList.add(new Knight(this, 6, 7, true));
        pieceList.add(new King(this, 4, 0, false));
        pieceList.add(new King(this, 4, 7, true));
        pieceList.add(new Rook(this, 0, 0, false));
        pieceList.add(new Rook(this, 7, 0, false));
        pieceList.add(new Rook(this, 0, 7, true));
        pieceList.add(new Rook(this, 7, 7, true));
        pieceList.add(new Bishop(this, 2, 0, false));
        pieceList.add(new Bishop(this, 5, 0, false));
        pieceList.add(new Bishop(this, 2, 7, true));
        pieceList.add(new Bishop(this, 5, 7, true));
        pieceList.add(new Queen(this, 3, 0, false));
        pieceList.add(new Queen(this, 3, 7, true));
    }

    public Piece getPiece(int col, int row) {
        for (Piece piece : pieceList) {
            if (piece.col == col && piece.row == row) {
                return piece;
            }
        }
        return null;
    }

    public void makeMove(Move move) {
        if (isValidMove(move)) {
            if (move.piece instanceof Pawn) {
                movePawn(move);
            } else {
                move.piece.col = move.newCol;
                move.piece.row = move.newRow;
                move.piece.xPos = move.newCol * tileSize;
                move.piece.yPos = move.newRow * tileSize;

                if (move.piece.isFirstMove) {
                    move.piece.firstMoved();
                }

                capture(move.capturedPiece);
                gameController.swapTurn();
                selectedPiece = null;
            }
        }
    }

    public void movePawn(Move move) {
        //enPassant
        int colorIndex = move.piece.isWhite ? 1 : -1;
        int promotionRow = move.piece.isWhite ? 0 : 7;

        if (getTileNum(move.newCol, move.newRow) == enPassantTile) {
            move.capturedPiece = getPiece(move.newCol, move.newRow + colorIndex);
        }
        if (Math.abs(move.newRow - move.piece.row) == 2) {
            enPassantTile = getTileNum(move.newCol, move.newRow + colorIndex);
        } else {
            enPassantTile = -1;
        }

        //Promotion
        if (move.newRow == promotionRow) {
            promotePawn(move);
        }

        move.piece.col = move.newCol;
        move.piece.row = move.newRow;
        move.piece.xPos = move.newCol * tileSize;
        move.piece.yPos = move.newRow * tileSize;

        if (move.piece.isFirstMove) {
            move.piece.firstMoved();
        }

        capture(move.capturedPiece);
        gameController.swapTurn();
        selectedPiece = null;
    }

    public void promotePawn(Move move) {
        pieceList.add(new Queen(this, move.newCol, move.newRow, move.piece.isWhite));
        capture(move.piece);
    }

    public void capture(Piece piece) {
        pieceList.remove(piece);
    }

    public boolean isValidMove(Move move) {
        if (sameTeam(move.piece, move.capturedPiece)) {
            return false;
        }
        if (!move.piece.isValidMovement(move.newCol, move.newRow)) {
            return false;
        }
        if (move.piece.moveCollidesWithPiece(move.newCol, move.newRow)) {
            return false;
        }
        if (move.newCol < 0 || move.newCol > 7 || move.newRow < 0 || move.newRow > 7) {
            return false;
        }
        if (checkScanner.isKingChecked(move)) {
            return false;
        }

        return true;
    }

    public boolean sameTeam(Piece piece1, Piece piece2) {
        if (piece1 == null || piece2 == null) {
            return false;
        }
        return piece1.isWhite == piece2.isWhite;
    }

    public int getTileNum(int col, int row) {
        return row * rows + col;
    }

    public Piece findKing(boolean isWhite) {
        for (Piece piece : pieceList) {
            if (piece instanceof King && piece.isWhite == isWhite) {
                return piece;
            }
        }
        return null;
    }

    public void paint() {
        Piece king = findKing(gameController.isWhiteTurn());

        //paint board
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                //black tile
                Pane blackPane = new Pane();
                blackPane.setBackground(new Background(new BackgroundFill(Color.color(234.0 / 255, 191.0 / 255, 153.0 / 255), null, null)));
                blackPane.setPrefHeight(tileSize);
                blackPane.setPrefWidth(tileSize);
                //white tile
                Pane whitePane = new Pane();
                whitePane.setBackground(new Background(new BackgroundFill(Color.color(178.0 / 255, 110.0 / 255, 55.0 / 255), null, null)));
                whitePane.setPrefHeight(tileSize);
                whitePane.setPrefWidth(tileSize);
                //this.setColor((c+r)%2 == 0 ? new Color(234, 191, 153) : new Color(178, 110, 55));
                if ((c + r) % 2 == 0) {
                    this.add(blackPane, c, r);
                } else {
                    this.add(whitePane, c, r);
                }
                //this.fillRect(c * tileSize, r * tileSize, tileSize, tileSize);
            }
        }

        //paint movable squares
        if (selectedPiece != null) {
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    if (selectedPiece != null) {
                        if (isValidMove(new Move(this, selectedPiece, c, r))) {
                            //available move tile
                            Pane greenPane = new Pane();
                            greenPane.setBackground(new Background(new BackgroundFill(Color.color(0, 255.0 / 255, 0, 50.0 / 100), null, null)));
                            greenPane.setPrefHeight(tileSize);
                            greenPane.setPrefWidth(tileSize);
                            /*this.setColor(new Color(0, 255, 0, 100));
                            this.fillRect(c * tileSize, r * tileSize, tileSize, tileSize);*/
                            this.add(greenPane, c, r);
                        }
                    }
                }
            }
        }

        //paint checked king
        if (checkScanner.isKingChecked(new Move(this, king, king.col, king.row))) {
            //checked king tile
            Pane redPane = new Pane();
            redPane.setBackground(new Background(new BackgroundFill(Color.RED, null, null)));
            redPane.setPrefHeight(tileSize);
            redPane.setPrefWidth(tileSize);
            //this.setColor(new Color(255, 0, 0, 121));
            //this.fillRect(king.col * tileSize, king.row * tileSize, tileSize, tileSize);
            this.add(redPane, king.col, king.row);
        }

        //paint pieces
        for (Piece piece : pieceList) {
            this.add(piece, piece.xPos / tileSize, piece.yPos / tileSize);
        }
    }

    public GameController getGameController() {
        return gameController;
    }

    public Board getBoard() {
        return this;
    }
}