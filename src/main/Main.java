package main;

import gui.Stopwatch;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(60, 0, 60, 0));
        root.setPrefHeight(800);
        root.setPrefWidth(1000);
        root.setBackground(new Background(new BackgroundFill(Color.GRAY, null, null)));

        Board board = new Board(root);
        root.setCenter(board);
        Stopwatch whiteStopwatch = new Stopwatch(), blackStopwatch = new Stopwatch();
        root.setTop(blackStopwatch);
        BorderPane.setAlignment(blackStopwatch, Pos.CENTER);
        root.setBottom(whiteStopwatch);
        BorderPane.setAlignment(whiteStopwatch, Pos.CENTER);

        whiteStopwatch.startTimer(60);

        Scene newScene = new Scene(root);
        stage.setScene(newScene);
        stage.setTitle("Chessir");
        stage.show();
    }
}
