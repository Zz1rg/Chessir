package gui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

// Make Stopwatch extend Label or the class we want Stopwatch to be ?
public class Stopwatch {

    private final Label label = new Label();
    private final Timeline timeline = new Timeline();
    private int second;

    public Stopwatch(int second) {
        this.second = second;
        label.setText(Integer.toString(second));
        label.setTextFill(Color.BLACK);
        label.setFont(Font.font(12));
        label.setTextAlignment(TextAlignment.CENTER);
        timeline.getKeyFrames().add(
                new KeyFrame(
                        Duration.seconds(0),
                        actionEvent -> {
                            label.setText(Integer.toString(this.second));
                        }
                )
        );
        timeline.getKeyFrames().add(
                new KeyFrame(
                        Duration.seconds(1),
                        actionEvent -> {
                            this.second--;
                            label.setText(Integer.toString(this.second));
                        }
                )
        );
    }

    public Stopwatch() {
        this(0);
    }

    public Label getLabel() {
        return label;
    }

    public void startTimer(int countdownSec) {
        timeline.stop();
        this.second = countdownSec;
        timeline.setCycleCount(countdownSec);
        timeline.playFromStart();
    }
}
