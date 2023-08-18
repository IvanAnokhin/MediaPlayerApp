package com.example.demo8;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MediaPlayerApp extends Application {

  private MediaPlayer mediaPlayer;
  private Slider volumeSlider;
  private Slider seekSlider;
  private Label volumeLabel;
  private Label timeLabel;
  private Label songLabel;

  private List<String> musicFiles = new ArrayList<>();
  private int currentTrackIndex = 0;

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {

    primaryStage.setTitle("Медиаплеер");

    Label titleLabel = new Label("Название трека:");
    songLabel = new Label("");
    volumeSlider = new Slider(0, 100, 50);
    seekSlider = new Slider();
    volumeLabel = new Label("Громкость: " + (int) volumeSlider.getValue());
    timeLabel = new Label("00:00 / 00:00");

    Button playButton = new Button("\u25B6");
    Button pauseButton = new Button("\u23F8");
    Button stopButton = new Button("\u25A0");
    Button prevButton = new Button("\u25C0\u25C0");
    Button nextButton = new Button("\u25B6\u25B6");

    playButton.setOnAction(event -> playMedia());
    pauseButton.setOnAction(event -> pauseMedia());
    stopButton.setOnAction(event -> stopMedia());
    prevButton.setOnAction(event -> playPreviousTrack());
    nextButton.setOnAction(event -> playNextTrack());

    HBox controlsBox = new HBox(10, prevButton, playButton, pauseButton, stopButton, nextButton);
    HBox volumeBox = new HBox(10, volumeSlider, volumeLabel);
    HBox buttonsBox = new HBox(10, controlsBox);
    VBox root = new VBox(20, titleLabel, songLabel, volumeBox, seekSlider, timeLabel, buttonsBox);

    initMusicFiles();

    Scene scene = new Scene(root, 310, 240);
    scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles.css")).toExternalForm());
    primaryStage.setScene(scene);
    primaryStage.show();

    volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
      if (mediaPlayer != null) {
        mediaPlayer.setVolume(newValue.doubleValue() / 100);
        volumeLabel.setText("Громкость: " + (int) newValue.doubleValue());
      }
    });
    seekSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
      if (seekSlider.isValueChanging() && mediaPlayer != null) {
        mediaPlayer.seek(Duration.seconds(newValue.doubleValue()));
        updateTimeLabel(Duration.seconds(newValue.doubleValue()));
      }
    });
  }

  private void initMusicFiles() {
    musicFiles.add("src/media/Eminem – Mockingbird.mp3");
    musicFiles.add("src/media/Aqua – Barbie Girl.mp3");
    musicFiles.add("src/media/Rihanna – Diamonds.mp3");
  }

  private void playMedia() {
    if (mediaPlayer != null) {
      mediaPlayer.stop();
    }

    String selectedFile = musicFiles.get(currentTrackIndex);
    Media media = new Media(new File(selectedFile).toURI().toString());
    mediaPlayer = new MediaPlayer(media);
    mediaPlayer.setOnReady(() -> {
      seekSlider.setMax(mediaPlayer.getTotalDuration().toSeconds());
      mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
        if (!seekSlider.isValueChanging()) {
          seekSlider.setValue(newValue.toSeconds());
        }
        updateTimeLabel(newValue);
      });
      songLabel.setText(selectedFile.substring(selectedFile.lastIndexOf("/") + 1));
    });

    mediaPlayer.play();
  }

  private void pauseMedia() {
    if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
      mediaPlayer.pause();
    }
  }

  private void stopMedia() {
    if (mediaPlayer != null) {
      mediaPlayer.stop();
    }
  }

  private void updateTimeLabel(Duration currentTime) {
    Duration totalDuration = mediaPlayer.getTotalDuration();
    String currentTimeStr = formatDuration(currentTime);
    String totalDurationStr = formatDuration(totalDuration);

    timeLabel.setText(currentTimeStr + " / " + totalDurationStr);
  }

  private String formatDuration(Duration duration) {
    int seconds = (int) Math.floor(duration.toSeconds()) % 60;
    int minutes = (int) Math.floor(duration.toMinutes());
    return String.format("%02d:%02d", minutes, seconds);
  }

  private void playPreviousTrack() {
    currentTrackIndex = (currentTrackIndex - 1 + musicFiles.size()) % musicFiles.size();
    playMedia();
  }

  private void playNextTrack() {
    currentTrackIndex = (currentTrackIndex + 1) % musicFiles.size();
    playMedia();
  }
}
