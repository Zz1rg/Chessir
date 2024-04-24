package gui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

public class Stopwatch extends Label {

    private final Timeline timeline = new Timeline();
    private int second;

    public Stopwatch(int second) {
        this.second = second;
        setText(Integer.toString(second));
        setTextFill(Color.BLACK);
        setFont(Font.font(12));
        setTextAlignment(TextAlignment.CENTER);
        timeline.getKeyFrames().add(
                new KeyFrame(
                        Duration.seconds(0),
                        actionEvent -> {
                            setText(Integer.toString(this.second));
                        }
                )
        );
        timeline.getKeyFrames().add(
                new KeyFrame(
                        Duration.seconds(1),
                        actionEvent -> {
                            this.second--;
                            setText(Integer.toString(this.second));
                        }
                )
        );
    }

    public Stopwatch() {
        this(0);
    }

    public void startTimer(int countdownSec) {
        timeline.stop();
        this.second = countdownSec;
        timeline.setCycleCount(countdownSec);
        timeline.playFromStart();
    }
}
