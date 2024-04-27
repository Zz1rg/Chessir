package gui;

import controller.GameController;
import javafx.animation.AnimationTimer;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import util.Team;

public class Stopwatch extends HBox {

    private final Team team;
    private GameController gameController;
    private long timerMs = 0;
    private long incrementMs = 0;
    private final AnimationTimer animationTimer = new AnimationTimer() {
        private long startingMs;
        private long startingTime = 0;

        @Override
        public void handle(long now) {
            if (startingTime == 0) {
                startingMs = timerMs;
                startingTime = now;
                // add a little bit of delay
                return;
            }
            long msElapsed = (now - startingTime) / 1_000_000;
            setTimerMs(startingMs - msElapsed);
            if (timerMs <= 0) {
                gameController.timeout(team);
            }
            setTimeText(timerMs);
        }

        @Override
        public void stop() {
            super.stop();
            // add a little bit of delay
            startingTime = 0;
        }
    };
    private final Label timeLabel = new Label();

    public Stopwatch(Team team, int seconds, int incrementSecs) {
        this.team = team;
        setTimerMs(seconds * 1000L);
        setIncrementMs(incrementSecs * 1000L);
        setTimeText(this.timerMs);
        setAlignment(Pos.CENTER);

        Pane timerBox = new Pane();
        timerBox.setBackground(Background.fill(Color.ORANGE));
        timerBox.setBorder(Border.stroke(Color.BLACK));
        timerBox.setPrefWidth(180);
        timerBox.setPrefHeight(60);

        timeLabel.setTextFill(Color.BLACK);
        timeLabel.setFont(Font.font(36));
        timeLabel.setTextAlignment(TextAlignment.CENTER);
        timeLabel.layoutXProperty().bind(timerBox.widthProperty().subtract(timeLabel.widthProperty()).divide(2));
        timeLabel.layoutYProperty().bind(timerBox.heightProperty().subtract(timeLabel.heightProperty()).divide(2));

        timerBox.getChildren().add(timeLabel);

        getChildren().add(timerBox);
    }

    public Stopwatch(Team team) {
        this(team, 0, 0);
    }

    public void startTimer(int countdownSec) {
        stopTimer();
        setTimerMs(countdownSec);
        setTimeText(timerMs);
        animationTimer.start();
    }

    public void startTimer() {
        animationTimer.start();
    }

    public void stopTimer() {
        animationTimer.stop();
    }

    private void setTimeText(long timerMs) {
        long ms = timerMs % 1000;
        long seconds = (timerMs / 1000) % 60;
        long minutes = timerMs / (60 * 1000);
        String minutesStr = (minutes / 10 == 0 ? "0" : "") + minutes;
        String secondsStr = (seconds / 10 == 0 ? "0" : "") + seconds;
        String msStr = (ms / 100 == 0 ? "0" : "") + (ms / 10 == 0 ? "0" : "") + ms;
        String timeStr = minutesStr + ":" + secondsStr + ":" + msStr;
        timeLabel.setText(timeStr);
    }


    public void setTimerMs(long timerMs) {
        long maxMs = (99 * 60 + 59) * 1000;
        if (timerMs < 0) {
            timerMs = 0;
        } else if (timerMs > maxMs) {
            timerMs = maxMs;
        }
        this.timerMs = timerMs;
    }

    public void setIncrementMs(long incrementMs) {
        if (incrementMs < 0) {
            incrementMs = 0;
        }
        this.incrementMs = incrementMs;
    }

    public Team getTeam() {
        return team;
    }

    public void addIncrement() {
        setTimerMs(timerMs + incrementMs);
        setTimeText(timerMs);
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public void resetTimer(int seconds, int incrementSecs) {
        setTimerMs(seconds * 1000L);
        setIncrementMs(incrementSecs * 1000L);
        setTimeText(this.timerMs);
    }
}
