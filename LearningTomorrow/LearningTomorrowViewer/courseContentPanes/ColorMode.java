package LearningTomorrowViewer.courseContentPanes;

import javafx.scene.paint.Color;

import java.awt.*;
import java.util.ArrayList;

public class ColorMode {
    private final String ModeName;
    private final String Backround_Color;
    private final String Text_Color;

    public ColorMode(String n, String b, String t){
        this.ModeName = n;
        this.Backround_Color = b;
        this.Text_Color = t;
    }

    public ArrayList<String> getColors(){
        ArrayList<String> colors = new ArrayList<String>();
        colors.add(this.Backround_Color);
        colors.add(this.Text_Color);
        return colors;
    }

    public String getModeName(){return this.ModeName;}
}
