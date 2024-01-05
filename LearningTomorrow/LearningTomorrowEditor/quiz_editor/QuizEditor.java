package LearningTomorrowEditor.quiz_editor;

import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.util.Optional;

public class QuizEditor {
    private VBox vbox;
    private QuizEditorQuestionList questionList;
    private File file;
    public QuizEditor(Pane pane, File file) {
        this.file = file;

        try {
            loadFile();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not load the file");
            alert.setContentText("Could not load the file " + file.getName());

            alert.showAndWait();
            return;
        }

        Button addQuestionButton = new Button("Add Question");
        Button removeQuestionButton = new Button("Remove Question");

        addQuestionButton.setOnAction(actionEvent -> {
            Alert alert = new Alert(Alert.AlertType.NONE);
            alert.setTitle("Add Question");
            alert.setHeaderText("Choose the type of the question to add:");

            // Create custom buttons
            ButtonType singleSelectButton = new ButtonType("Single-Select");
            ButtonType multiSelectButton = new ButtonType("Multi-Select");
            ButtonType cancelButton = new ButtonType("Cancel");

            // Set custom buttons
            alert.getButtonTypes().setAll(singleSelectButton, multiSelectButton, cancelButton);

            // Show the alert and wait for user action
            Optional<ButtonType> result = alert.showAndWait();

            // Process user's choice
            if (result.isPresent()) {
                if (result.get() != cancelButton) {
                    Stage window = new Stage();
                    QuestionEditor questionEditor;
                    if (result.get() == singleSelectButton) {
                        questionEditor = new QuestionEditor(window,
                                QuizEditorQuestion.QuestionType.SINGLE_SELECT);
                    }
                    else {
                        questionEditor = new QuestionEditor(window,
                                QuizEditorQuestion.QuestionType.MULTI_SELECT);
                    }
                    window.setOnCloseRequest(windowEvent -> {
                        questionList.getItems().add(questionEditor.buildQuestion());
                        window.close();
                    });
                }
            }
        });
        removeQuestionButton.setOnAction(actionEvent -> {
            questionList.getItems().remove(questionList.getSelectionModel().getSelectedItem());
        });

        vbox = new VBox();
        Label fileLabel = new Label("Editing - " + file.getName());
        vbox.getChildren().addAll(fileLabel, questionList, addQuestionButton, removeQuestionButton);

        pane.getChildren().add(vbox);
    }

    private void loadFile() throws IOException {
        questionList = new QuizEditorQuestionList(file);
        questionList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    public void saveFile() throws IOException {
        questionList.saveFile(file);
    }

    public File getFile() {
        return file;
    }
}
