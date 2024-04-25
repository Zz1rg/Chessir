package main;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;

public class Main extends Application {
    public static void main(String[] args) {
        URL resource = ClassLoader.getSystemResource("sincerely.mp3");
        MediaPlayer bgm = new MediaPlayer(new Media(resource.toString()));
        bgm.setOnEndOfMedia(new Runnable() {
            public void run() {
                bgm.seek(Duration.ZERO);
            }
        });
        bgm.play();
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
        BorderPane.setMargin(board, new Insets(20, 160, 20, 160));

        root.setTop(board.getBlackStopwatch());
        root.setBottom(board.getWhiteStopwatch());

        Scene newScene = new Scene(root);
        stage.setResizable(false);
        stage.setScene(newScene);
        stage.setTitle("Chessir");
        stage.show();
    }
}
