package controller;

import gui.MoveHistoryPane;
import gui.RestartButton;
import gui.UndoButton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import main.Board;
import util.Gamemode;

import static main.Main.appStage;

public final class SceneController {

    public static void switchToBoard(Gamemode gamemode) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(60, 85, 150, 0));
        root.setPrefHeight(720);
        root.setPrefWidth(1000);
        root.setBackground(new Background(new BackgroundFill(Color.GRAY, null, null)));

        // Board
        Board board = new Board(root, gamemode);
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

        GamemodeRow bulletRow = new GamemodeRow("bullet");
        GamemodeBtn bullet1 = new GamemodeBtn(Gamemode.Bullet1);
        bullet1.setText("1 min");
        GamemodeBtn bullet2 = new GamemodeBtn(Gamemode.Bullet1i1);
        bullet2.setText("1 | 1");
        GamemodeBtn bullet3 = new GamemodeBtn(Gamemode.Bullet2i1);
        bullet3.setText("2 | 1");
        bulletRow.getChildren().addAll(new GamemodeBtn[]{bullet1, bullet2, bullet3});

        GamemodeRow blitzRow = new GamemodeRow("blitz");
        GamemodeBtn blitz1 = new GamemodeBtn(Gamemode.Blitz3);
        blitz1.setText("3 min");
        GamemodeBtn blitz2 = new GamemodeBtn(Gamemode.Blitz3i2);
        blitz2.setText("3 | 2");
        GamemodeBtn blitz3 = new GamemodeBtn(Gamemode.Blitz5);
        blitz3.setText("5 min");
        blitzRow.getChildren().addAll(new GamemodeBtn[]{blitz1, blitz2, blitz3});

        GamemodeRow rapidRow = new GamemodeRow("rapid");
        GamemodeBtn rapid1 = new GamemodeBtn(Gamemode.Rapid10);
        rapid1.setText("10 min");
        GamemodeBtn rapid2 = new GamemodeBtn(Gamemode.Rapid15i10);
        rapid2.setText("15 | 10");
        GamemodeBtn rapid3 = new GamemodeBtn(Gamemode.Rapid30);
        rapid3.setText("30 min");
        rapidRow.getChildren().addAll(new GamemodeBtn[]{rapid1, rapid2, rapid3});

        root.getChildren().addAll(new GamemodeRow[]{bulletRow, blitzRow, rapidRow});

        showScene(root);
    }

    private static void showScene(Parent root) {
        Scene scene = new Scene(root, 1100, 950);
        appStage.setScene(scene);
        appStage.show();
    }

    private static class GamemodeBtn extends Button {
        GamemodeBtn(Gamemode gamemode) {
            setOnAction(actionEvent -> {
                switchToBoard(gamemode);
            });
            setPrefWidth(200);
            setPrefHeight(100);
            setFont(Font.font(36));
        }
    }

    private static class GamemodeRow extends HBox {
        GamemodeRow(String gamemodeStr) {
            String imgPath = ClassLoader.getSystemResource(gamemodeStr + ".png").toString();
            ImageView img = new ImageView(new Image(imgPath, 100, 100, true, true));
            getChildren().add(img);
            setAlignment(Pos.CENTER);
            setPadding(new Insets(24, 0, 24, 0));

            Pane blankBox = new Pane();
            blankBox.setPrefHeight(0);
            blankBox.setPrefWidth(36);
            getChildren().add(blankBox);
        }
    }
}
