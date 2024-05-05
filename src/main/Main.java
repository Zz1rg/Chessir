package main;

import controller.SceneController;
import javafx.application.Application;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;

public class Main extends Application {
    private static Stage APP_STAGE;

    public static void main(String[] args) {
        URL resource = ClassLoader.getSystemResource("sincerely.mp3");
        MediaPlayer bgm = new MediaPlayer(new Media(resource.toString()));
        bgm.setOnEndOfMedia(() -> bgm.seek(Duration.ZERO));
        bgm.play();
        launch();
    }

    @Override
    public void start(Stage stage) {
        APP_STAGE = stage;
        stage.setResizable(false);
        stage.setTitle("Chessir");
        SceneController.switchToMainMenu();
    }

    public static Stage getAppStage() {
        return APP_STAGE;
    }
}