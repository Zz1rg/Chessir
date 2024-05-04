package gui;

import javafx.scene.control.Button;
import main.Board;

public class UndoButton extends Button {

    public UndoButton(Board board) {
        this.setText("Undo");
        this.setPrefSize(100, 50);
        this.setOnMouseClicked(event -> board.getGameController().undoMove());
    }
}
