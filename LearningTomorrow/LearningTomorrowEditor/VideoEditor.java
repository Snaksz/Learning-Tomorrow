package LearningTomorrowEditor;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.IOException;

/**
 * Class for a Video content editor
 */
public class VideoEditor {
    private VBox vbox;
    private File file;

    /**
     * Constructor for VideoEditor
     * @param pane
     * @param file
     */
    public VideoEditor(Pane pane, File file) {
        this.file = file;

        vbox = new VBox();
        Label fileLabel = new Label(file.getName());
        Label noEditLabel = new Label("Video editing is not available");
        vbox.getChildren().addAll(fileLabel, noEditLabel);

        pane.getChildren().add(vbox);
    }

    /**
     * Getter for the file that this editor is editing
     * @return File object for the file
     */
    public File getFile() {
        return file;
    }
}
