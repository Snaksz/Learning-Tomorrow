import LearningTomorrowEditor.LTEditor;
import LearningTomorrowViewer.LTViewer;
import javafx.animation.FadeTransition;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;

public class LTLauncher {

    private class ViewerLaunchButton extends Button {
        private static final String text = "Open file to study";
        protected ViewerLaunchButton() {
            BackgroundFill backgroundFill = new BackgroundFill(inactiveButtonBackgroundColor,
                    CornerRadii.EMPTY, javafx.geometry.Insets.EMPTY);
            Background background = new Background(backgroundFill);
            this.setBackground(background);

            this.setTextFill(inactiveButtonTextColor);
            this.setText(text);

            this.setOnAction((actionEvent) -> LTLauncher.this.launchViewer());
        }
    }

    private class EditorLaunchButton extends Button {
        private static final String text = "Open file to edit";
        protected EditorLaunchButton() {
            BackgroundFill backgroundFill = new BackgroundFill(inactiveButtonBackgroundColor,
                    CornerRadii.EMPTY, javafx.geometry.Insets.EMPTY);
            Background background = new Background(backgroundFill);
            this.setBackground(background);

            this.setTextFill(inactiveButtonTextColor);
            this.setText(text);

            this.setOnAction((actionEvent) -> LTLauncher.this.launchEditor());
        }
    }

    private static class ColorFadeAnimation {
        private FadeTransition animation1, animation2;
        private Label label = null;
        private VBox vbox = null;
        private final Color activeColor;
        private final Color inactiveColor;

        protected ColorFadeAnimation(Label label, Color activeColor, Color inactiveColor) {
            this.label = label;
            this.activeColor = activeColor;
            this.inactiveColor = inactiveColor;
        }

        protected ColorFadeAnimation(VBox vbox, Color activeColor, Color inactiveColor) {
            this.vbox = vbox;
            this.activeColor = activeColor;
            this.inactiveColor = inactiveColor;
        }

        protected void animateActive(double time) {
            animate(time, activeColor);
        }

        protected void animateInactive(double time) {
            animate(time, inactiveColor);
        }

        private void animate(double time, Color toColor) {
            Region node;
            if (vbox != null)
                node = vbox;
            else if (label != null)
                node = label;
            else
                return;

            if (animation1 != null) animation1.stop();
            if (animation2 != null) animation2.stop();
            animation1 = new FadeTransition(new Duration(time/2), node);
            animation1.setFromValue(1.0);
            animation1.setToValue(0.0);
            animation1.setOnFinished((actionEvent) -> {
                if (node == label)
                    label.setTextFill(toColor);
                else {
                    BackgroundFill backgroundFill = new BackgroundFill(toColor,
                            CornerRadii.EMPTY, javafx.geometry.Insets.EMPTY);
                    Background background = new Background(backgroundFill);
                    node.setBackground(background);
                }
                animation2 = new FadeTransition(new Duration(time/2), node);
                animation2.setFromValue(0.0);
                animation2.setToValue(1.0);
                animation2.play();
            });
            animation1.play();
        }
    }

    private static class ButtonColorFadeAnimation {
        private FadeTransition backgroundAnimation1, backgroundAnimation2;
        private FadeTransition textAnimation1, textAnimation2;
        private final Button button;
        private final Color activeBackgroundColor;
        private final Color inactiveBackgroundColor;
        private final Color activeTextColor;
        private final Color inactiveTextColor;

        protected ButtonColorFadeAnimation(Button button,
                                           Color activeBackgroundColor,
                                           Color inactiveBackgroundColor,
                                           Color activeTextColor,
                                           Color inactiveTextColor) {
            this.button = button;
            this.activeBackgroundColor = activeBackgroundColor;
            this.inactiveBackgroundColor = inactiveBackgroundColor;
            this.activeTextColor = activeTextColor;
            this.inactiveTextColor = inactiveTextColor;
        }

        protected void animateActive(double time) {
            animate(time, activeBackgroundColor, activeTextColor);
        }

        protected void animateInactive(double time) {
            animate(time, inactiveBackgroundColor, inactiveTextColor);
        }

        private void animate(double time, Color backgroundColor, Color textColor) {
            if (backgroundAnimation1 != null) backgroundAnimation1.stop();
            if (backgroundAnimation2 != null) backgroundAnimation2.stop();
            if (textAnimation1 != null) textAnimation1.stop();
            if (textAnimation2 != null) textAnimation2.stop();
            backgroundAnimation1 = new FadeTransition(new Duration(time/2), button);
            backgroundAnimation1.setFromValue(1.0);
            backgroundAnimation1.setToValue(0.0);
            backgroundAnimation1.setOnFinished((actionEvent) -> {
                BackgroundFill backgroundFill = new BackgroundFill(backgroundColor,
                        CornerRadii.EMPTY, javafx.geometry.Insets.EMPTY);
                Background background = new Background(backgroundFill);
                button.setBackground(background);
                button.setTextFill(textColor);
                backgroundAnimation2 = new FadeTransition(new Duration(time/2), button);
                backgroundAnimation2.setFromValue(0.0);
                backgroundAnimation2.setToValue(1.0);
                backgroundAnimation2.play();
            });
            textAnimation1 = new FadeTransition(new Duration(time/2), button);
            textAnimation1.setFromValue(1.0);
            textAnimation1.setToValue(0.0);
            textAnimation1.setOnFinished((actionEvent) -> {
                BackgroundFill backgroundFill = new BackgroundFill(backgroundColor,
                        CornerRadii.EMPTY, javafx.geometry.Insets.EMPTY);
                Background background = new Background(backgroundFill);
                button.setBackground(background);
                button.setTextFill(textColor);
                textAnimation2 = new FadeTransition(new Duration(time/2), button);
                textAnimation2.setFromValue(0.0);
                textAnimation2.setToValue(1.0);
                textAnimation2.play();
            });
            backgroundAnimation1.play();
            textAnimation1.play();
        }
    }

    private static final String WINDOW_TITLE = "LearningTomorrow Launcher";
    private static final String VIEWER_LABEL_TEXT = "LearningTomorrow for Students";
    private static final String EDITOR_LABEL_TEXT = "LearningTomorrow for Instructors";

    private static final String PATH_TO_SAVED_EDITOR_FILES = "Saved" + File.separator + "Editor";
    private static final String PATH_TO_SAVED_VIEWER_FILES = "Saved" + File.separator + "Viewer";
    private static final String EDITOR_FILES_EXT = ".edt";
    private static final String VIEWER_FILES_EXT = ".vwr";
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;
    private static final String FONT_NAME = "Times New Roman";
    private static final int FONT_SIZE = 20;
    private static final String BUTTON_FONT_NAME = "Times New Roman";
    private static final int BUTTON_FONT_SIZE = 15;

    private Stage stage;
    private Scene scene;
    private SplitPane splitPane;
    private VBox editorVBox;
    private Label editorLabel;
    private ComboBox<String> editorComboBox;
    private EditorLaunchButton editorButton;
    private ColorFadeAnimation editorVBoxAnimation, editorLabelAnimation;
    private ButtonColorFadeAnimation editorButtonAnimation;
    private VBox viewerVBox;
    private Label viewerLabel;
    private ComboBox<String> viewerComboBox;
    private ViewerLaunchButton viewerButton;
    private ColorFadeAnimation viewerVBoxAnimation, viewerLabelAnimation;
    private ButtonColorFadeAnimation viewerButtonAnimation;
    private LTViewer viewer;
    private LTEditor editor;

    private static final double animationDuration = 750.0;
    private static final Color activeBackgroundColor = Color.LIGHTBLUE;
    private static final Color inactiveBackgroundColor = Color.web("0x000237");
    private static final Color activeLabelColor = Color.BLACK;
    private static final Color inactiveLabelColor = Color.WHITE;
    private static final Color activeButtonBackgroundColor = Color.GREEN;
    private static final Color inactiveButtonBackgroundColor = Color.INDIGO;
    private static final Color activeButtonTextColor = Color.WHITE;
    private static final Color inactiveButtonTextColor = Color.WHITE;


    public LTLauncher(Stage stage) {
        this.stage = stage;
        resetLauncher();
    }

    public void resetLauncher() {
        Font font = new Font(FONT_NAME, FONT_SIZE);
        Font buttonFont = new Font(BUTTON_FONT_NAME, BUTTON_FONT_SIZE);

        editorVBox = new VBox(10);
        editorVBox.setAlignment(javafx.geometry.Pos.CENTER);

        editorLabel = new Label(EDITOR_LABEL_TEXT);
        editorLabel.setTextFill(inactiveLabelColor);
        editorLabel.setFont(font);

        editorComboBox = new ComboBox<>();
        editorComboBox.getItems().add("Create New File");
        editorComboBox.setPromptText("Select an option");

        StackPane editorComboBoxRoot = new StackPane();
        editorComboBoxRoot.getChildren().add(editorComboBox);

        editorButton = new EditorLaunchButton();
        editorButton.setFont(buttonFont);

        editorVBox.getChildren().addAll(editorLabel, editorComboBoxRoot, editorButton);

        editorLabelAnimation = new ColorFadeAnimation(editorLabel,
                activeLabelColor, inactiveLabelColor);
        editorVBoxAnimation = new ColorFadeAnimation(editorVBox,
                activeBackgroundColor, inactiveBackgroundColor);
        editorButtonAnimation = new ButtonColorFadeAnimation(editorButton,
                activeButtonBackgroundColor, inactiveButtonBackgroundColor,
                activeButtonTextColor, inactiveButtonTextColor);

        editorVBox.setOnMouseEntered((actionEvent) -> {
            editorLabelAnimation.animateActive(animationDuration);
            editorVBoxAnimation.animateActive(animationDuration);
            editorButtonAnimation.animateActive(animationDuration);
        });

        editorVBox.setOnMouseExited((actionEvent) -> {
            editorLabelAnimation.animateInactive(animationDuration);
            editorVBoxAnimation.animateInactive(animationDuration);
            editorButtonAnimation.animateInactive(animationDuration);
        });

        viewerVBox = new VBox(10);
        viewerVBox.setAlignment(javafx.geometry.Pos.CENTER);

        viewerLabel = new Label(VIEWER_LABEL_TEXT);
        viewerLabel.setTextFill(inactiveLabelColor);
        viewerLabel.setFont(font);

        viewerComboBox = new ComboBox<>();
        populateComboBox(viewerComboBox, PATH_TO_SAVED_VIEWER_FILES, VIEWER_FILES_EXT);
        viewerComboBox.setPromptText("Select an option");

        StackPane viewerComboBoxRoot = new StackPane();
        viewerComboBoxRoot.getChildren().add(viewerComboBox);

        viewerButton = new ViewerLaunchButton();
        viewerButton.setFont(buttonFont);

        viewerVBox.getChildren().addAll(viewerLabel, viewerComboBoxRoot, viewerButton);

        viewerLabelAnimation = new ColorFadeAnimation(viewerLabel, activeLabelColor, inactiveLabelColor);
        viewerVBoxAnimation = new ColorFadeAnimation(viewerVBox, activeBackgroundColor, inactiveBackgroundColor);
        viewerButtonAnimation = new ButtonColorFadeAnimation(viewerButton,
                activeButtonBackgroundColor, inactiveButtonBackgroundColor,
                activeButtonTextColor, inactiveButtonTextColor);

        viewerVBox.setOnMouseEntered((actionEvent) -> {
            viewerLabelAnimation.animateActive(animationDuration);
            viewerVBoxAnimation.animateActive(animationDuration);
            viewerButtonAnimation.animateActive(animationDuration);
        });

        viewerVBox.setOnMouseExited((actionEvent) -> {
            viewerLabelAnimation.animateInactive(animationDuration);
            viewerVBoxAnimation.animateInactive(animationDuration);
            viewerButtonAnimation.animateInactive(animationDuration);
        });

        splitPane = new SplitPane();
        splitPane.getItems().addAll(editorVBox, viewerVBox);

        BackgroundFill backgroundFill = new BackgroundFill(inactiveBackgroundColor,
                CornerRadii.EMPTY, javafx.geometry.Insets.EMPTY);
        Background background = new Background(backgroundFill);
        editorVBox.setBackground(background);

        backgroundFill = new BackgroundFill(inactiveBackgroundColor,
                CornerRadii.EMPTY, javafx.geometry.Insets.EMPTY);
        background = new Background(backgroundFill);
        viewerVBox.setBackground(background);

        backgroundFill = new BackgroundFill(Color.BLACK,
                CornerRadii.EMPTY, javafx.geometry.Insets.EMPTY);
        background = new Background(backgroundFill);
        splitPane.setBackground(background);

        stage.setMaximized(true);
        stage.setResizable(true);
        stage.setMinWidth(WINDOW_WIDTH);
        stage.setMinHeight(WINDOW_HEIGHT);
        Screen main_screen = Screen.getPrimary();
        stage.setMaxHeight(main_screen.getBounds().getHeight());
        stage.setMaxWidth(main_screen.getBounds().getWidth());
        //

        splitPane.setDividerPosition(0, 0.5);
        splitPane.getDividers().get(0).positionProperty().addListener(
                (observable, oldValue, newValue ) -> splitPane.setDividerPosition(0, 0.5));

        stage.setTitle(WINDOW_TITLE);
        scene = new Scene(splitPane);
        scene.setFill(null);
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }

    private void populateComboBox(ComboBox<String> comboBox, String folderPath, String ext) {
        File folder = new File(folderPath);
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory() && containsFileWithExt(file, ext)) {
                        comboBox.getItems().add(file.getName());
                    }
                }
            }
        } else {
            System.out.println("Folder " + folderPath + " does not exist or is not a directory.");
        }
    }

    private boolean containsFileWithExt(File directory, String ext) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(ext)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void launchViewer() {
        System.out.println("Launching Viewer...");
        String filename = PATH_TO_SAVED_VIEWER_FILES + File.separator + viewerComboBox.getValue();
        File file = new File(filename);
        if (!file.exists()) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText("Cannot launch the viewer");
            errorAlert.setContentText("The course was not selected or does not exist.");
            errorAlert.showAndWait();
            return;
        }
        viewer = new LTViewer(stage, file);
    }

    protected void launchEditor() {
        System.out.println("Launching Editor...");
        editor = new LTEditor(stage);
    }

}
