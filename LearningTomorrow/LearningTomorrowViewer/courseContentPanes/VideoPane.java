package LearningTomorrowViewer.courseContentPanes;

import LearningTomorrowViewer.ColorChanger;
import LearningTomorrowViewer.FontChanger;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.ToggleButton;

import javafx.scene.paint.Color;
import java.io.File;
import java.io.IOException;
import javafx.scene.control.Slider;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.text.Text;

public class VideoPane extends CourseContent {

    private MediaView mediaView;
    private Button playbutton, pausebutton, resetbutton;
    private File file;
    private Media video;
    private MediaPlayer mediaPlayer;
    private HBox hbox;
    private VBox vBox;
    private ToggleButton playPauseButton;
    private ImageView playIcon;
    private ImageView pauseIcon;
    private Timeline timeline;

    // Text to display video time information
    private Label timeLabel;
    private Button skipForwardButton;
    private Button skipBackwardButton;
    private ImageView forwardImageView;
    private ImageView backImageView;
    /**
     * VideoPane constructor
     * @param pane the pane to add content to
     */
    public VideoPane(Pane pane) {
        this.pane = pane;
        this.mediaView = null;
        this.hbox = null;
        this.vBox = null;
    }

    /**
     * Set up necessary GUI elements on the pane parameter
     */
    // "How to use panes" ChatGPT, 3 Dec. version, OpenAI, 3 Dec. 2023,
    // chat.openai.com/chat
    public void setUpPane() {
        // Now the mediaview is made so as to view the video
        // Method sets up the video
        // border.widthProperty().bind(this.pane.widthProperty());
        // border.heightProperty().bind(this.pane.heightProperty());
        // this.mediaView.setFitHeight(this.mediaView.getFitHeight());
        // this.mediaView.setFitWidth(this.mediaView.getFitWidth());
//        this.mediaView.setFitWidth();
//
//        this.mediaView.setFitHeight(this.pane.getHeight());
//        this.mediaView.setFitWidth(this.pane.getWidth());
        //this.hbox = new HBox(this.mediaView);
        // Bind the prefHeight of the HBox to the height of the Pane
        //this.hbox.prefHeightProperty().bind(this.pane.heightProperty());
        // Bind the prefHeight of the HBox to the height of the Pane
        //this.hbox.prefWidthProperty().bind(this.pane.widthProperty());


        // Bind the fitHeight property of MediaView to the height property of its parent (HBox)
        //this.mediaView.fitHeightProperty().bind(this.hbox.heightProperty());


        this.mediaView.setAccessibleText("VIDEO PLAYER");

        mediaView.fitWidthProperty().bind(pane.widthProperty());
        mediaView.fitHeightProperty().bind(pane.heightProperty());

        // Create buttons
        // Create buttons for skipping forward and backward
        // Create an ImageView for the restart icon
        Image forwardImage = new Image(getClass().getResourceAsStream("Button_Images" + File.separator + "forward.png"));
        forwardImageView = new ImageView(forwardImage);
        forwardImageView.setFitWidth(20);
        forwardImageView.setFitHeight(20);
        skipForwardButton = new Button("", forwardImageView);
        skipForwardButton.setOnAction(event -> skipForward());

        Image backImage = new Image(getClass().getResourceAsStream("Button_Images" + File.separator + "backward.png"));
        backImageView = new ImageView(backImage);
        backImageView.setFitWidth(20);
        backImageView.setFitHeight(20);
        skipBackwardButton = new Button("", backImageView);
        skipBackwardButton.setOnAction(event -> skipBackward());
        // Play Pause Button
        // Load play and pause icons
        Image playImage = new Image(getClass().getResourceAsStream("Button_Images" + File.separator+  "play.png"));
        Image pauseImage = new Image(getClass().getResourceAsStream("Button_Images" + File.separator+  "pause.png"));

        // Create ImageViews for the icons
        playIcon = new ImageView(playImage);
        playIcon.setFitWidth(20);
        playIcon.setFitHeight(20);

        pauseIcon = new ImageView(pauseImage);
        pauseIcon.setFitWidth(20);
        pauseIcon.setFitHeight(20);

        // Create a ToggleButton with the play icon
        playPauseButton = new ToggleButton("", playIcon);

        // Set the action for the button
        playPauseButton.setOnAction(event -> {
            // its currently playing so need pause button
            if (this.mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                // If selected, change to pause icon and add play logic
                playPauseButton.setGraphic(playIcon);
                this.mediaPlayer.pause();
            } else {
                // its paused so need a play button
                // If not selected, change to play icon and add pause logic
                playPauseButton.setGraphic(pauseIcon);
                this.mediaPlayer.play();
            }
        });
        mediaPlayer.setOnEndOfMedia(() -> {
            this.mediaPlayer.pause();
            playPauseButton.setGraphic(playIcon);
        });
        // restart button
        // Create a restart icon image
        Image restartIcon = new Image(getClass().getResourceAsStream("Button_Images" + File.separator + "restart.png"));

        // Create an ImageView for the restart icon
        ImageView restartImageView = new ImageView(restartIcon);
        restartImageView.setFitWidth(20);
        restartImageView.setFitHeight(20);

        Button restartButton = new Button("", restartImageView);
        restartButton.setOnAction(e -> restart());

        // Set the positions of buttons (optional)
        restartButton.setLayoutX(20);
        restartButton.setLayoutY(100);


        // Create a slider for volume control
        Slider volumeSlider = new Slider(0, 1, 1);
        volumeSlider.setShowTickMarks(false);
        volumeSlider.setShowTickLabels(false);
        // Set the preferred size of the volume slider
        volumeSlider.setPrefWidth(120); // Adjust the width as needed
        volumeSlider.setPrefHeight(10); // Adjust the height as needed

        // Add listener to track changes in the slider value
        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            double volume = newValue.doubleValue();
            mediaPlayer.setVolume(volume);

            // You can add your audio control logic here
            // For example, update the audio volume using the 'volume' value
        });

        // Load the speaker icon
        Image speakerIcon = new Image(getClass().getResourceAsStream("Button_Images" + File.separator + "speaker.png"));
        ImageView speakerImageView = new ImageView(speakerIcon);
        speakerImageView.setFitWidth(20);
        speakerImageView.setFitHeight(20);



        // Create a Label for displaying video time information
        timeLabel = new Label();
        timeLabel.setTextFill(Color.BLACK); // Set text color to white (adjust as needed)

        // Add listener to update timeLabel
        this.mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
            updateTimeLabel();
        });

        // Initialize timeLabel immediately after loading content
        updateTimeLabel();


        // Create an HBox for buttons
        hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);
        hbox.setSpacing(0); // Adjust the spacing value as needed

        // Create a separator
        Separator separator = new Separator();
        separator.setPadding(new Insets(0, 0, 0, 0)); // Adjust the values as needed
        hbox.getChildren().addAll(timeLabel, playPauseButton, restartButton, skipBackwardButton, skipForwardButton, separator, speakerImageView, volumeSlider);
        hbox.setAlignment(Pos.CENTER_LEFT);


        // centers to the left the hbox vbox that contains the videos and all
        VBox controlsBox = new VBox();
        controlsBox.setAlignment(Pos.TOP_CENTER);
        controlsBox.getChildren().addAll(hbox);
        controlsBox.setSpacing(0); // spacing to reduce clutter


        // Create a VBox to hold the media view and the controls
        vBox = new VBox();
        vBox.getChildren().addAll(mediaView, controlsBox);

        // Add the VBox to the pane
        this.pane.getChildren().add(vBox);
    }

    /**
     * Load content from a content file stored at filepath
     * @param filepath path to the file storing the content
     * @throws IOException if there is an error reading the file
     */
    @Override
    public void loadContent(String filepath) throws IOException {
        // This pane will hold media, i.e. a video or an image
        // Maybe make another pane to only hold an image? idk to be determined...
        // Create a Media object
        File file = new File(filepath);
        Media media = new Media(file.toURI().toString());
        // Create a MediaPlayer
        mediaPlayer = new MediaPlayer(media);
        // Create a MediaView
        this.mediaView = new MediaView(mediaPlayer);
        // calling to setup the file
        setUpPane();
        // old code
//        File videoFile = new File(filepath);
//        video = new Media(videoFile.toURI().toString());
//        mediaPlayer = new MediaPlayer(video);
//        MediaView mediaView = new MediaView(mediaPlayer);
//        this.mediaView = mediaView;
//        setUpPane();
        // old code
        // Add the MediaView to the Pane
        // this.pane.getChildren().add(this.mediaView);
    }

    @Override
    public void changeFont(FontChanger fontChanger, String fontName, int fontSize) {
        // No fonts to change???
    }

    @Override
    public void changeColors(ColorChanger colorChanger, ColorMode m){}
    // Do nothing for a video

    public void restart(){
        // Method to reset the video
        this.mediaPlayer.seek(Duration.ZERO);

    }

    private void updateTimeLabel() {
        if (this.mediaPlayer != null) {
            Duration currentTime = this.mediaPlayer.getCurrentTime();
            Duration totalDuration = this.mediaPlayer.getTotalDuration();

            // Format current time and total duration
            String currentTimeStr = formatDuration(currentTime);
            String totalDurationStr = formatDuration(totalDuration);

            // Set the text of the Label object
            timeLabel.setText(currentTimeStr + " / " + totalDurationStr);
        }
    }

    // Helper method to format duration as "minutes:seconds"
    private String formatDuration(Duration duration) {
        long minutes = (long) duration.toMinutes();
        long seconds = (long) duration.toSeconds() % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
    private void skipForward() {
        if (mediaPlayer != null) {
            Duration currentTime = mediaPlayer.getCurrentTime();
            Duration skipTime = currentTime.add(Duration.seconds(5));
            mediaPlayer.seek(skipTime);
        }
    }

    private void skipBackward() {
        if (mediaPlayer != null) {
            Duration currentTime = mediaPlayer.getCurrentTime();
            Duration skipTime = currentTime.subtract(Duration.seconds(5));
            mediaPlayer.seek(skipTime);
        }
    }



    public MediaView getMediaView(){
        return this.mediaView;
    }

}

