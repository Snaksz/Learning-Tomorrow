package LearningTomorrowViewer;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

import javafx.beans.value.ChangeListener;
import javafx.scene.layout.Pane;

public class ToolbarPane{

    private GridPane toolbarPane; // stage to store toolbar in
    private ComboBox<String> fontComboBox; // place to store

    private ComboBox<String> colorComboBox; // place to store
    private LTViewer ltviewer;
    public final FontChanger fontChanger;
    public final ColorChanger colorChanger;

    private TextField fontSizeTextField;

    public ToolbarPane() {
        fontComboBox = new ComboBox<String>();
        colorComboBox = new ComboBox<String>();

        fontChanger = new FontChanger(fontComboBox);
        colorChanger = new ColorChanger(colorComboBox);

        this.colorChanger.setFontChanger(this.fontChanger);
        this.fontChanger.setColorChanger(this.colorChanger);

        fontSizeTextField = new TextField();

        // Set prompt text
        fontSizeTextField.setText("14");

        // Add event filter to allow only numeric input
        fontSizeTextField.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            if (!event.getCharacter().matches("\\d")) {
                event.consume();
            }
        });

        this.toolbarPane = new GridPane();
        Label comboBoxLabel = new Label("Choose a font:");
        Label textFieldLabel = new Label("Choose font size:");
        Label colorBoxLabel = new Label("Choose a color mode");
        toolbarPane.add(comboBoxLabel, 0, 0);
        toolbarPane.add(textFieldLabel, 0, 1);
        toolbarPane.add(fontComboBox, 1, 0);
        toolbarPane.add(fontSizeTextField, 1, 1);
        toolbarPane.add(colorBoxLabel, 2, 0);
        toolbarPane.add(colorComboBox, 2, 1);
        toolbarPane.setHgap(10);
        toolbarPane.setVgap(10);

        ColumnConstraints firstColumnConstraints = new ColumnConstraints();
        firstColumnConstraints.setHalignment(HPos.RIGHT);
        ColumnConstraints secondColumnConstraints = new ColumnConstraints();
        secondColumnConstraints.setHalignment(HPos.LEFT);
        toolbarPane.getColumnConstraints().addAll(firstColumnConstraints, secondColumnConstraints);
    }

    public void setComboBoxChangeListener(ChangeListener<String> listener) {
        fontComboBox.valueProperty().addListener(listener);
    }

    public void setColorComboBoxChangeListener(ChangeListener<String> listener) {
        colorComboBox.valueProperty().addListener(listener);
    }

    public void setTextFieldHandler(EventHandler<ActionEvent> handler) {
        fontSizeTextField.setOnAction(handler);
    }

    public Pane getToolbarPane(){
        return this.toolbarPane;
    }

    public String getComboBoxSelection() {
        return fontComboBox.getSelectionModel().getSelectedItem();
    }

    public String getColorComboBoxSelection() {return colorComboBox.getSelectionModel().getSelectedItem();}

    public int getTextFieldInput() {
        try {
            return Integer.parseInt(fontSizeTextField.getText());
        }
        catch (NumberFormatException e) {
            fontSizeTextField.setText("14");
            return 14;
        }
    }

}
