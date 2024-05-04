package gui;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import main.Board;

public class RestartButton extends Button {
    private Board board;

    public RestartButton(Board board) {
        this.board = board;
        this.setText("Restart");
        this.setPrefSize(100, 50);
        this.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                board.getGameController().resetGame();
            }
        });
    }
}
