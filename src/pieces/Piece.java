package pieces;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import main.Board;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Piece extends HBox {

    public boolean isFirstMove = true;
    public int col, row;
    public int xPos, yPos;

    public boolean isWhite;
    public String name;
    public int value;

    BufferedImage sheet;
    {
        try {
            sheet = ImageIO.read(ClassLoader.getSystemResource("pieces.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    protected int sheetScale = sheet.getWidth()/6;

    Image sprite;

    Board board;

    public Piece (Board board) {
        this.board = board;
        this.setPrefHeight(board.tileSize);
        this.setPrefWidth(board.tileSize);
    }

    /*public void paint(Board board) {
        //g2d.drawImage(sprite, xPos, yPos, null);
        board.getChildren().add(this);
    }*/

    public boolean isValidMovement(int col, int row) {
        return true;
    }

    public boolean moveCollidesWithPiece(int col, int row) {
        return false;
    }

    public void firstMoved() {
        this.isFirstMove = false;
    }
}
