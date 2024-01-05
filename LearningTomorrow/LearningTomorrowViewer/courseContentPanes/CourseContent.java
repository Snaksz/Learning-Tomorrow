package LearningTomorrowViewer.courseContentPanes;

import LearningTomorrowViewer.ColorChanger;
import LearningTomorrowViewer.FontChanger;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.web.WebView;

import java.io.IOException;

abstract public class CourseContent {
    protected Pane pane;
    private WebView webview = null;
    public String contentFileName = "";
    private MediaPlayer mediaPlayer = null;
    /**
     * Set up necessary GUI elements
     */
    abstract public void setUpPane();

    /**
     * Load content from a content file stored at filepath
     * @param filepath path to the file storing the content
     * @throws IOException if there is an error reading the file
     */
    abstract public void loadContent(String filepath) throws IOException;

    /**
     * Get the javafx pane object
     * @return javafx pane object
     */
    public Pane getPane() {
        return this.pane;
    }

    public WebView getWebview(){return this.webview;}

    public abstract void changeFont(FontChanger fontChanger, String fontName, int fontSize);

    public void changeColors(ColorChanger colorChanger, ColorMode m){
        if (this.webview != null){
            colorChanger.changeColor(this.webview, m);
        }
    };

    public MediaPlayer getMediaPlayer() {
        return this.mediaPlayer;
    }

    public void setMediaPlayer(Media m){
        this.mediaPlayer = new MediaPlayer(m);
    }

    public String getFileName(){
        return this.contentFileName;
    }

}
