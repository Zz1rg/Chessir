package main;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        HBox root = new HBox();
        root.setPadding(new Insets(60, 0, 0, 0));
        root.setPrefHeight(800);
        root.setPrefWidth(1000);
        root.setAlignment(Pos.CENTER);
        root.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
        root.getChildren().add(new Board());
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
        stage.setTitle("Chessir");
        stage.show();
    }
}
