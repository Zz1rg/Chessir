package gui;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import main.Board;

public class UndoButton extends Button {
    private Board board;

    public UndoButton(Board board) {
        this.board = board;
        this.setText("Undo");
        this.setPrefSize(100, 50);
        this.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                board.getGameController().undoMove();
            }
        });
    }
}
