package LearningTomorrowViewer.courseContentPanes;

import LearningTomorrowViewer.ColorChanger;
import LearningTomorrowViewer.FontChanger;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.web.WebView;

import javax.swing.text.AbstractDocument;

public abstract class AllContent {
    public CourseContent courseContent;
    /**
     * Getter methods (works for both ConcreteContent and WrappedContent)
     * */
    public Pane getPane(){
        return this.courseContent.getPane();
    }

    public WebView getWebview(){
        return this.courseContent.getWebview();
    }

    public MediaPlayer getMediaPlayer(){
        return this.courseContent.getMediaPlayer();
    }

    /** Behaviors these will be overridden and used by the different classes*/
    public void articulateNotes(){}
    public void stopArticulation(){}
    public boolean isPlaying(){return false;}
    public CourseContent getCourseContent(){return this.courseContent;}

    public void changeFont(FontChanger fontChanger, String fontName, int fontSize){
        this.courseContent.changeFont(fontChanger, fontName, fontSize);
    }

    public void changeColor(ColorChanger colorChanger, ColorMode m){
        this.courseContent.changeColors(colorChanger, m);
    }
}
