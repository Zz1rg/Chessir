package gui;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

// USE THE OTHER ONE
public class StopwatchWithThread extends Thread {

    private final Label label = new Label();
    private int second;

    public StopwatchWithThread(int second) {
        setDaemon(true);
        setPriority(MAX_PRIORITY);
        this.second = second;
        label.setText(Integer.toString(second));
        label.setTextFill(Color.BLACK);
        label.setFont(Font.font(12));
        label.setTextAlignment(TextAlignment.CENTER);
    }

    public Label getLabel() {
        return label;
    }

    @Override
    public void run() {
        while (!interrupted() && second > 0) {
            try {
                //noinspection BusyWait
                sleep(1000);
            } catch (InterruptedException ignored) {
            }
            second--;
            Platform.runLater(() -> {
                label.setText(Integer.toString(second));
            });
        }
    }

    public void startTimer(int countdownSec) {
        this.second = countdownSec;
        if (isAlive()) {
            interrupt();
        }
        label.setText(Integer.toString(second));
        if (!isAlive()) {
            start();
        }
    }

    public void startTimer() {
        startTimer(second);
    }
}
