package LearningTomorrowEditor.toolbar;

import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

/**
 * Class for the export button
 */
public class ExportButton extends Button {

    /**
     * Constructor for the ExportButton
     * @param color Color object for the color of the button
     */
    public ExportButton(Color color) {
        super();
        this.setText("Export");
        this.setBackground(new Background(new BackgroundFill(color,
                new CornerRadii(5), null)));
    }
}
