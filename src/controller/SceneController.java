package controller;

import gui.MoveHistoryPane;
import gui.RestartButton;
import gui.UndoButton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import main.Board;

import static main.Main.appStage;

public final class SceneController {

    public static void switchToBoard() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(60, 85, 150, 0));
        root.setPrefHeight(720);
        root.setPrefWidth(1000);
        root.setBackground(new Background(new BackgroundFill(Color.GRAY, null, null)));

        // Board
        Board board = new Board(root);
        root.setCenter(board);

        // Buttons
        HBox buttons = new HBox();
        buttons.setPrefSize(100, 200);
        buttons.getChildren().add(new UndoButton(board));
        buttons.getChildren().add(new RestartButton(board));
        buttons.setSpacing(20);
        buttons.setAlignment(Pos.CENTER);

        // Move History
        MoveHistoryPane moveHistoryPane = new MoveHistoryPane(board);
        //Right Menu
        GridPane rightMenu = new GridPane();
        rightMenu.setPrefSize(250, 720);
        rightMenu.add(moveHistoryPane, 0, 0);
        rightMenu.add(buttons, 0, 1);
        rightMenu.getRowConstraints().add(new RowConstraints(515));
        rightMenu.setVgap(0);
        rightMenu.setAlignment(Pos.CENTER);
        root.setRight(rightMenu);
        BorderPane.setMargin(rightMenu, new Insets(20, 0, 20, 0));

        BorderPane.setMargin(board, new Insets(20, 60, 20, 95));
        root.setTop(board.getBlackStopwatch());
        root.setBottom(board.getWhiteStopwatch());
        board.getBlackStopwatch().setPadding(new Insets(0, 217, 0, 0));
        board.getWhiteStopwatch().setPadding(new Insets(0, 217, 0, 0));

        showScene(root);
    }

    public static void switchToMainMenu() {
        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
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
        Scene scene = new Scene(root, 1100, 950);
        appStage.setScene(scene);
        appStage.show();
    }
}
