package LearningTomorrowViewer.courseContentPanes;

import LearningTomorrowViewer.ColorChanger;
import LearningTomorrowViewer.FontChanger;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebView;
import javafx.scene.web.WebEngine;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class NotesPane extends CourseContent {
    /**
     * NotesPane constructor
     * @param pane the pane to add content to
     */
    public NotesPane(Pane pane) {
        this.pane = pane;
    }
    private WebView webview;
    /**
     * Set up necessary GUI elements on the pane parameter
     */
    @Override
    public void setUpPane() {
    /*
    How to set up the pane? Need the content to be loaded into it first.
    The pane setup should adjust the images?
    The pane set-up should change the fonts?

    Set-up the default pane look.
    */
        getPane().setStyle("-fx-background-color: #FFFFFF");
    }


    /**
     * Load content from a content file stored at filepath
     * @param filepath path to the file storing the content
     * @throws IOException if there is an error reading the file
     */
    @Override
    public void loadContent(String filepath) throws IOException {
        if (filepath.endsWith(".html")) {
            // Process html code!
            // Will be the most-used file format for this project
            setUpPane();
            File file = new File(filepath);
            String loadable = file.toURI().toString(); // Got rid of checking if file exists, as it must exist

            WebView htmlNode = new WebView(); // Error lies with creating Webview object
            WebEngine htmlRenderer = htmlNode.getEngine();
            htmlRenderer.load(loadable);
            getPane().getChildren().add(htmlNode); // Add the node to the pane

            // Better solution store node too!
            this.webview = htmlNode; // Allows us to resize and force it!
            this.contentFileName = filepath;
        }

    }

    // Got rid of txt file support since we should be using the more standardized html
    // Need to reimplement font change, and color change for the Webview Node!
    @Override
    public Pane getPane(){
        return this.pane;
    }

    public WebView getWebview(){return this.webview;}

    @Override
    public void changeFont(FontChanger fontChanger, String fontName, int fontSize) {
        fontChanger.changeFont(this.webview, fontName, fontSize);
    }

    @Override
    public void changeColors(ColorChanger colorChanger, ColorMode m){
        colorChanger.changeColor(this.webview, m);
    }

}
