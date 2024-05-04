package gui;

import javafx.scene.control.Button;
import main.Board;

public class RestartButton extends Button {

    public RestartButton(Board board) {
        this.setText("Restart");
        this.setPrefSize(100, 50);
        this.setOnMouseClicked(event -> board.getGameController().resetGame());
    }
}
