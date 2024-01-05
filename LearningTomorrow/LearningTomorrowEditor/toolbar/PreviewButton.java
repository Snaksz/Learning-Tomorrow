package LearningTomorrowEditor.toolbar;

import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

/**
 * Class for the preview button
 */
public class PreviewButton extends Button {

    /**
     * Constructor for the PreviewButton
     * @param color Color object for the color of the button
     */
    public PreviewButton(Color color) {
        super();
        this.setText("Preview");
        this.setBackground(new Background(new BackgroundFill(color,
                new CornerRadii(5), null)));
    }
}
