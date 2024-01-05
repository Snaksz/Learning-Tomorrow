package LearningTomorrowViewer;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.text.Font;
import javafx.scene.web.WebView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FontChanger {

    private static final String FONT_FILE_PATH = "LearningTomorrow" + File.separator
            + "LearningTomorrowViewer" + File.separator + "fonts.txt";
    private static final String DEFAULT_FONT = "Times New Roman";

    private final List<String> fontStrings;
    private final List<Font> fontList;

    public ColorChanger colorChanger;

    public int size = 14;
    public String font = "Times New Roman";

    public FontChanger(ComboBox<String> fontComboBox) {
        fontList = new ArrayList<>();
        try {
            loadFonts(fontComboBox);
        } catch (IOException ignored) {
            fontComboBox.getItems().clear();
            fontComboBox.getItems().add(DEFAULT_FONT);
            fontList.clear();
            fontList.add(Font.font(DEFAULT_FONT));
        }
        fontStrings = fontComboBox.getItems();
        fontComboBox.getSelectionModel().select("Times New Roman");

    }

    public void setColorChanger(ColorChanger colorChanger) {
        this.colorChanger = colorChanger;
    }

    public void changeFont(WebView w, String fontName, int fontSize) {
        if (!fontStrings.contains(fontName)) return;
        this.font = fontName;
        this.size = fontSize;
        w.getEngine().setUserStyleSheetLocation("data:text/css, body { color: " + this.colorChanger.curr_col_txt + "; font: " + fontSize + "px " + fontName + ";" +
                "background-color: " + this.colorChanger.curr_col_bck +"; }");
    }

    public void changeFont(Label label, String fontName, int fontSize)  {
        if (!fontStrings.contains(fontName)) return;
        label.setFont(new Font(fontName, fontSize));
    }

    public void changeFont(ToggleButton btn, String fontName, int fontSize)  {
        if (!fontStrings.contains(fontName)) return;
        btn.setFont(new Font(fontName, fontSize));
    }

    private void loadFonts(ComboBox<String> fontComboBox) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(FONT_FILE_PATH));
        String line;
        while ((line = br.readLine()) != null) {
            line = line.strip();
            fontComboBox.getItems().add(line);
            fontList.add(Font.font(line));
        }
    }

    /*
    public void inc_font_size_ft(Button input) {
        // Purpose: The increase font size button is made.
        // Setting up button characterisitcs
        input.setId("Increase Font Size");
        input.setAccessibleRoleDescription("Increase Font Size");
        input.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
        input.setPrefSize(200, 50);
        input.setFont(new Font(16));

        // inputting the actions for button
        input.setOnAction(e -> {
            this.ltviewer.changeDefaultFontSizeUp();
        });

    }
    public void dec_font_size_ft(Button input) {
        // Purpose: The decrease font size button is made.
        // Setting up button characterisitcs
        input.setId("Increase Font Size");
        input.setAccessibleRoleDescription("Increase Font Size");
        input.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
        input.setPrefSize(200, 50);
        input.setFont(new Font(16));

        // inputting the actions for button
        input.setOnAction(e -> {
            this.ltviewer.changeDefaultFontSizeDown();
        });
    }
    public void change_font_size(Button input) {
        // Purpose: The change font size button is made.
        // Setting up button characterisitcs
        input.setId("Increase Font Size");
        input.setAccessibleRoleDescription("Increase Font Size");
        input.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
        input.setPrefSize(200, 50);
        input.setFont(new Font(16));

        // inputting the actions for button
        input.setOnAction(e -> {

        });
    }
    */
}
