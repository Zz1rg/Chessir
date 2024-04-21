package main;

import javafx.application.Application;
import javafx.embed.swing.SwingNode;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;

public class Main extends Application {
    public static void main(String[] args) {
        /*JFrame frame = new JFrame("Chess Game");
        frame.getContentPane().setBackground(Color.BLACK);
        frame.setLayout(new GridBagLayout());
        frame.setMinimumSize(new Dimension(1000, 1000));
        frame.setLocationRelativeTo(null);

        Board board = new Board();
        frame.add(board);

        frame.setVisible(true);*/
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        /*final SwingNode swingNode = new SwingNode();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Board board = new Board();
                swingNode.setContent(board);
            }
        });*/
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
        //RootPane.getInstance().getChildren().add(swingNode);
        stage.show();
    }
}
