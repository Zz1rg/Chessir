package main;

import controller.GameController;
import pieces.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Board extends JPanel {

    public int tileSize = 85;
    int cols = 8;
    int rows = 8;

    ArrayList<Piece> pieceList = new ArrayList<>();

    public Piece selectedPiece;

    // TODO: migrate game logic into GameController class ?
    private final GameController gameController = new GameController();

    Input input = new Input(this);

    public int enPassantTile = -1;

    public Board() {
        this.setPreferredSize(new Dimension(cols * tileSize, rows * tileSize));

        this.addMouseListener(input);
        this.addMouseMotionListener(input);

        addPieces();
    }

    public void addPieces() {
        for (int i=0; i<=7; i++) {
            pieceList.add(new Pawn(this, i, 1, false));
        }

        for (int i=0; i<=7; i++) {
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
    }

    public void promotePawn(Move move) {
        pieceList.add(new Queen(this, move.newCol, move.newRow, move.piece.isWhite));
        capture(move.piece);
    }

    public void capture(Piece piece) {
        pieceList.remove(piece);
    }

    public boolean isValidMove(Move move) {
        if(sameTeam(move.piece, move.capturedPiece)) {
            return false;
        }

        if(!move.piece.isValidMovement(move.newCol, move.newRow)) {
            return false;
        }

        if(move.piece.moveCollidesWithPiece(move.newCol, move.newRow)) {
            return false;
        }
        if(move.newCol < 0 || move.newCol > 7 || move.newRow < 0 || move.newRow > 7) {
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

    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        //paint board
        for (int r = 0; r < rows; r++) {
            for (int c=0; c < cols; c++) {
                g2d.setColor((c+r)%2 == 0 ? new Color(234, 191, 153) : new Color(178, 110, 55));
                g2d.fillRect(c * tileSize, r * tileSize, tileSize, tileSize);
            }
        }

        //paint movable squares
        for (int r = 0; r < rows; r++) {
            for (int c=0; c < cols; c++) {
                if (selectedPiece != null) {
                    if(isValidMove(new Move(this, selectedPiece, c, r))) {
                        g2d.setColor(new Color(0, 255, 0, 100));
                        g2d.fillRect(c * tileSize, r * tileSize, tileSize, tileSize);
                    }
                }
            }
        }

        //paint pieces
        for (Piece piece : pieceList) {
            piece.paint(g2d);
        }
    }

    public GameController getGameController() {
        return gameController;
    }
}