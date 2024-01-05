package LearningTomorrowEditor;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.*;

/**
 * Class for a Note content editor
 */
public class NotesEditor {
    private VBox vbox;
    private TextArea textArea;
    private File file;

    /**
     * Constructor for NotesEditor
     * @param pane the pane to add UI components to
     * @param file file to load
     */
    public NotesEditor(Pane pane, File file) {
        this.textArea = new TextArea();
        this.file = file;

        try {
            loadText();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not load the file");
            alert.setContentText("Could not load the file " + file.getName());

            alert.showAndWait();
            return;
        }

        vbox = new VBox();
        Label fileLabel = new Label("Editing - " + file.getName());
        vbox.getChildren().addAll(fileLabel, textArea);

        textArea.setPrefWidth(pane.getWidth());
        textArea.setPrefHeight(pane.getHeight() - fileLabel.getHeight());

        pane.getChildren().add(vbox);
    }

    /**
     * Load text into the text area from the file
     * @throws IOException if the file cannot be read
     */
    private void loadText() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        while ((line = br.readLine()) != null) {
            textArea.setText(textArea.getText() + line + "\n");
        }
    }

    /**
     * Save text into the file
     * @throws IOException if the file cannot be written
     */
    public void saveText() throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        bw.write(textArea.getText());
        bw.close();
    }

    /**
     * Getter for the file that this editor is editing
     * @return File object for the file
     */
    public File getFile() {
        return file;
    }
}
