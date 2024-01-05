package LearningTomorrowViewer;

import LearningTomorrowViewer.courseContentPanes.ColorMode;
import javafx.scene.control.ComboBox;
import javafx.scene.text.Font;
import javafx.scene.web.WebView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ColorChanger {
    private static final String COLOR_MODES_FILE_PATH = "LearningTomorrow" + File.separator
            + "LearningTomorrowViewer" + File.separator + "color_modes.txt";

    private ColorMode BASIC = new ColorMode("Classic", "white", "black");

    public ArrayList<ColorMode> all_modes;

    private ArrayList<String> mode_names;

    public String curr_col_txt = "black"; // To start
    public String curr_col_bck = "white"; // To start

    public FontChanger fontChanger;

    public ColorChanger(ComboBox<String> ColorComboBox) {
        this.all_modes = new ArrayList<>();
        try {
            loadColorModes(ColorComboBox);
        } catch (IOException ignored) {
            ColorComboBox.getItems().clear();
            ColorComboBox.getItems().add(BASIC.getModeName());
            all_modes.clear();
            all_modes.add((BASIC));
        }
        this.mode_names = new ArrayList<String>();
        for (ColorMode x: this.all_modes){
            this.mode_names.add(x.getModeName());
        }
        ColorComboBox.getSelectionModel().select("Classic");
    }

    public void setFontChanger(FontChanger fontChanger) {
        this.fontChanger = fontChanger;
    }

    private void loadColorModes(ComboBox<String> ColorModesComboBox) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(COLOR_MODES_FILE_PATH));
        String line;
        while ((line = br.readLine()) != null) {
            line = line.strip();
            String[] settings = line.split(" "); // Split around spaces
            ColorMode mode = new ColorMode(settings[0], settings[1], settings[2]);
            ColorModesComboBox.getItems().add(settings[0]);
            this.all_modes.add(mode);
        }
    }

    public void changeColor(WebView w, ColorMode m) {
        ArrayList<String> colors = m.getColors();
        this.curr_col_txt = colors.get(1);
        this.curr_col_bck = colors.get(0); // Update this for the Font changer to use
        w.getEngine().setUserStyleSheetLocation("data:text/css, body { color: " + colors.get(1) +"; font: " + this.fontChanger.size + "px " + this.fontChanger.font + ";" +
        "background-color: " + colors.get(0) +"; }");
    }

}
