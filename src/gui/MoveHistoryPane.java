package gui;

import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import main.Board;
import main.Move;
import pieces.Knight;
import pieces.Pawn;
import util.MoveRecord;

import java.util.ArrayList;

public class MoveHistoryPane extends Pane {
    private final Board board;

    private final VBox moveHistory = new VBox();

    private final ScrollPane scrollPane = new ScrollPane();

    private final char[] colNames = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'};

    public MoveHistoryPane(Board board) {
        this.board = board;
        this.setStyle("-fx-background-color: #E4E9F1; " + // Set background color to white
                "-fx-background-radius: 10; " + // Set corner radius to 10px
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,1), 10, 0, 0, 0);");
        initMoveHistory();
        initScrollPane();
        this.getChildren().add(scrollPane);
    }

    public void initScrollPane() {
        scrollPane.setPrefWidth(250);
        scrollPane.setPrefHeight(515);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setBackground(new Background(new BackgroundFill(Color.color(61.0 / 255, 179.0 / 255, 192.0 / 255, 1), new CornerRadii(10), Insets.EMPTY)));
        scrollPane.setContent(moveHistory);
    }

    public void initMoveHistory() {
        moveHistory.setPrefHeight(515);
        moveHistory.setPrefWidth(250);
        moveHistory.setStyle("-fx-background-color: #E4E9F1; " + // Set background color to white
                "-fx-background-radius: 10; " + // Set corner radius to 10px
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,1), 10, 0, 0, 0);");
        moveHistory.setPadding(new Insets(10));
        Text title = new Text("Move History");
        title.setFill(Color.BLACK);
        title.setFont(Font.font(20));
        moveHistory.getChildren().add(title);
    }

    public void addMoveRecord(ArrayList<MoveRecord> moveRecords) {
        reset();
        int i = 1;
        for (MoveRecord moveRecord : moveRecords) {
            HBox moveBox = new HBox();
            Move move = moveRecord.move();
            if (move.getPiece().isWhite) {
                moveBox.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, null, null)));
            } else {
                moveBox.setBackground(new Background(new BackgroundFill(Color.GRAY, null, null)));
            }
            Text newMove = new Text();
            String moveString = i++ + ". ";
            if (!(move.getPiece() instanceof Pawn)) {
                if (move.getPiece() instanceof Knight) {
                    moveString += "N ";
                } else {
                    moveString += move.getPiece().name.charAt(0) + " ";
                }
            }
            moveString += colNames[move.getOldCol()] + "" + (move.getOldRow() + 1);
            if (move.getCapturedPiece() != null) {
                moveString += "x";
            } else {
                moveString += "->";
            }
            moveString += colNames[move.getNewCol()] + "" + (move.getNewRow() + 1);
            newMove.setText(moveString);
            newMove.setFont(Font.font(15));
            moveBox.getChildren().add(newMove);
            moveHistory.getChildren().add(moveBox);
        }
    }

    public void reset() {
        this.getChildren().clear();
        moveHistory.getChildren().clear();
        Text title = new Text("Move History");
        title.setFill(Color.BLACK);
        title.setFont(Font.font(20));
        moveHistory.getChildren().add(title);
        scrollPane.setContent(moveHistory);
        this.getChildren().add(scrollPane);
    }
}