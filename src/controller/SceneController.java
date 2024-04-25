package controller;

import gui.UndoButton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import main.Board;

import static main.Main.appStage;

public final class SceneController {

    public static void switchToBoard() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(60, 0, 60, 0));
        root.setPrefHeight(800);
        root.setPrefWidth(1000);
        root.setBackground(new Background(new BackgroundFill(Color.GRAY, null, null)));

        Board board = new Board(root);
        root.setCenter(board);
        root.setLeft(new UndoButton(board));

        BorderPane.setMargin(board, new Insets(20, 160, 20, 160));
        root.setTop(board.getBlackStopwatch());
        root.setBottom(board.getWhiteStopwatch());

        showScene(root);
    }

    public static void switchToMainMenu() {
        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        // ???????
        root.setPrefHeight(800);
        root.setPrefWidth(1000);
        root.setBackground(Background.fill(Color.GRAY));

        Button playBtn = new Button();
        playBtn.setText("Play");
        playBtn.setOnAction(actionEvent -> {
            switchToBoard();
        });
        root.getChildren().add(playBtn);

        showScene(root);
    }

    private static void showScene(Parent root) {
        Scene scene = new Scene(root);
        appStage.setScene(scene);
        appStage.show();
    }
}
