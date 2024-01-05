package LearningTomorrowEditor.toolbar;

import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

/**
 * Class for the save button
 */
public class SaveButton extends Button {

    /**
     * Constructor for the SaveButton
     * @param color Color object for the color of the button
     */
    public SaveButton(Color color) {
        super();
        this.setText("Save");
        this.setBackground(new Background(new BackgroundFill(color,
                new CornerRadii(5), null)));
    }
}
