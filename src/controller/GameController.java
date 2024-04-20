package controller;

public class GameController {
    private boolean isWhiteTurn = true;

    public void swapTurn() {
        isWhiteTurn = !isWhiteTurn;
    }

    public boolean isWhiteTurn() {
        return isWhiteTurn;
    }
}
