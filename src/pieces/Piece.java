package pieces;

import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import main.Board;
import main.Move;
import util.Coordinate;
import util.CoordinateRC;

import java.util.ArrayList;

public abstract class Piece extends HBox {

    public boolean isFirstMove = true;
    public int col, row;
    public int xPos, yPos;

    public boolean isWhite;
    public String name;
    public int value;

    Image sprite;

    Board board;

    public Piece(Board board) {
        this.board = board;
        this.setPrefHeight(board.tileSize);
        this.setPrefWidth(board.tileSize);
    }

    public boolean isValidMovement(int col, int row) {
        return true;
    }

    public boolean moveCollidesWithPiece(int col, int row) {
        return false;
    }

    public void firstMoved() {
        this.isFirstMove = false;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getxPos() {
        return xPos;
    }

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
    }

    public boolean isFirstMove() {
        return isFirstMove;
    }

    public void setFirstMove(boolean firstMove) {
        isFirstMove = firstMove;
    }

    public boolean isWhite() {
        return isWhite;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Coordinate getCoordinate() {
        return new CoordinateRC(row, col);
    }

    public abstract boolean canMove();

    protected boolean checkMovesFrom(ArrayList<Coordinate> relativeCoordinates) {
        for (Coordinate relativeCoordinate : relativeCoordinates) {
            Move move = new Move(board, this, getCoordinate().add(relativeCoordinate));
            if (board.isValidMove(move)) {
                System.out.println(getClass().toString() + " at rc(" + move.getNewRow() + "," + move.getNewCol() + ")");
                return true;
            }
        }
        return false;
    }
}
