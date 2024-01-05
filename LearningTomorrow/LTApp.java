import LearningTomorrowEditor.LTEditor;
import LearningTomorrowViewer.LTViewer;
import javafx.application.Application;
import javafx.stage.Stage;

public class LTApp extends Application {

    LTLauncher launcher;
    LTViewer viewer;
    LTEditor editor;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        launcher = new LTLauncher(stage);
    }
}