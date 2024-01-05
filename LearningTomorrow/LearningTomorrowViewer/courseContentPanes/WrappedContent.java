package LearningTomorrowViewer.courseContentPanes;

import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.web.WebView;

import java.io.File;

public class WrappedContent extends AllContent {
    /**
     * This class exists so that if an audio file exists, then the methods pertaining to
     * accessible audio will now become fully functional.
     * */
    public WrappedContent(CourseContent c, String s){
        this.courseContent = c;
        File file = new File(s);
        Media m = new Media(file.toURI().toString());
        this.courseContent.setMediaPlayer(m);
    }
    // Getter methods are inherited from abstract class, as they are implemented.

    /** Behaviors are overridden to allow the accessibility feature to provide audio for the notes*/
    @Override
    public void articulateNotes(){
        getMediaPlayer().play();
    }
    @Override
    public void stopArticulation(){
        getMediaPlayer().stop();
    }
    @Override
    public boolean isPlaying(){
        if (this.getMediaPlayer().getStatus().equals(MediaPlayer.Status.PLAYING)) {
            return true;
        }
        else {return false;}
    }
}

