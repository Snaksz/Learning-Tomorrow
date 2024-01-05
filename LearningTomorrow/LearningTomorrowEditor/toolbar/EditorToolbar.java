package LearningTomorrowEditor.toolbar;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * Class for the toolbar in the editor
 */
public class EditorToolbar {
    private static final Color buttonColor = Color.GREEN;
    private HBox layout;
    private SaveButton saveButton;
    private PreviewButton previewButton;
    private ExportButton exportButton;

    /**
     * Constructor for the editor toolbar
     * @param pane pane to add UI components to
     */
    public EditorToolbar(Pane pane) {
        layout = new HBox();
        layout.setSpacing(5);

        saveButton = new SaveButton(buttonColor);
        previewButton = new PreviewButton(buttonColor);
        exportButton = new ExportButton(buttonColor);

        layout.getChildren().addAll(saveButton, previewButton, exportButton);
        pane.getChildren().add(layout);
    }

    /**
     * Event Handler setter for when the save button is pressed
     * @param handler instance of EventHandler<ActionEvent>
     */
    public void setSaveHandler(EventHandler<ActionEvent> handler) {
        saveButton.setOnAction(handler);
    }

    /**
     * Event Handler setter for when the preview button is pressed
     * @param handler instance of EventHandler<ActionEvent>
     */
    public void setPreviewHandler(EventHandler<ActionEvent> handler) {
        previewButton.setOnAction(handler);
    }

    /**
     * Event Handler setter for when the export button is pressed
     * @param handler instance of EventHandler<ActionEvent>
     */
    public void setExportHandler(EventHandler<ActionEvent> handler) {
        exportButton.setOnAction(handler);
    }
}
