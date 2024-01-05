package LearningTomorrowViewer.courseContentPanes;

import LearningTomorrowViewer.FontChanger;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.web.WebView;

public class ConcreteContent extends AllContent {
    /**
     * This class models the unwrapped Content
     * It does support trying to play audio, but these actions are nullified
     * as there is no audio to be played
     * The functions are useful however, as it allows for the different functionality of
     * The wrapped counterpart from the non-accessible audio version.
     * I.e. If accessible toggle is off (in toolbar) then this version will be used.
     * */
    public ConcreteContent(CourseContent c){
        this.courseContent = c;
    }

    @Override
    public boolean isPlaying(){
        return false; // Always not playing accessible audio as it cannot
    }
    // Other behaviors need no implementation, as they are meant to do nothing
    // No audio to play, no audio to stop.

    // All getter methods will be inherited.
}
