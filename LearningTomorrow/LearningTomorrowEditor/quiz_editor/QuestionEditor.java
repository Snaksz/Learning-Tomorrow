package LearningTomorrowEditor.quiz_editor;

import LearningTomorrowViewer.quiz.QuestionBuilder;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

public class QuestionEditor {
    private ScrollPane scrollPane;
    private VBox vBoxContentPane;
    private HBox hBoxContentPane;
    private VBox firstColumnVBox, secondColumnVBox;
    private TextField questionPromptTextField;
    private List<TextField> correctOptionTextFields;
    private List<TextField> incorrectOptionTextFields;
    private final QuizEditorQuestion.QuestionType questionType;
    private QuizEditorQuestion question;
    public QuestionEditor(Stage window, QuizEditorQuestion.QuestionType questionType) {
        this.questionType = questionType;

        if (questionType == QuizEditorQuestion.QuestionType.SINGLE_SELECT) {
            window.setTitle("Add Question - Single-Select");
            initUISingleSelect(window);
        }
        else {
            window.setTitle("Add Question - Multi-Select");
            initUIMultiSelect(window);
        }

        Scene scene = new Scene(scrollPane);
        window.setScene(scene);
        window.show();
    }

    private void initUISingleSelect(Stage window) {
        vBoxContentPane = new VBox();

        Label questionPromptLabel = new Label("Set Question Prompt:");
        questionPromptTextField = new TextField();

        Label correctOptionLabel = new Label("Set Correct Option:");
        correctOptionTextFields = new ArrayList<>();
        correctOptionTextFields.add(new TextField());

        Label incorrectOptionLabel = new Label("Set Incorrect Options:");

        Button addIncorrectOptionButton = new Button("Add Incorrect Option");
        Button removeIncorrectOptionButton = new Button("Remove Last Incorrect Option");

        incorrectOptionTextFields = new ArrayList<>();

        addIncorrectOptionButton.setOnAction(actionEvent -> {
            incorrectOptionTextFields.add(new TextField());
            vBoxContentPane.getChildren().add(incorrectOptionTextFields.get(incorrectOptionTextFields.size() - 1));
        });
        removeIncorrectOptionButton.setOnAction(actionEvent -> {
            if (!incorrectOptionTextFields.isEmpty()) {
                vBoxContentPane.getChildren().remove(incorrectOptionTextFields.get(incorrectOptionTextFields.size() - 1));
                incorrectOptionTextFields.remove(incorrectOptionTextFields.size() - 1);
            }
        });

        vBoxContentPane.getChildren().addAll(questionPromptLabel, questionPromptTextField, correctOptionLabel);
        vBoxContentPane.getChildren().addAll(correctOptionTextFields);
        vBoxContentPane.getChildren().addAll(incorrectOptionLabel, addIncorrectOptionButton, removeIncorrectOptionButton);
        vBoxContentPane.getChildren().addAll(incorrectOptionTextFields);

        Button buildButton = getBuildButton(window, QuizEditorQuestion.QuestionType.SINGLE_SELECT);

        vBoxContentPane.getChildren().add(0, buildButton);

        scrollPane = new ScrollPane(vBoxContentPane);
    }

    private void initUIMultiSelect(Stage window) {
        hBoxContentPane = new HBox();
        firstColumnVBox = new VBox();
        secondColumnVBox = new VBox();

        Label questionPromptLabel = new Label("Set Question Prompt:");
        questionPromptTextField = new TextField();

        Label correctOptionLabel = new Label("Set Correct Option:");
        correctOptionTextFields = new ArrayList<>();
        correctOptionTextFields.add(new TextField());

        Button addCorrectOptionButton = new Button("Add Correct Option");
        Button removeCorrectOptionButton = new Button("Remove Last Correct Option");

        addCorrectOptionButton.setOnAction(actionEvent -> {
            correctOptionTextFields.add(new TextField());
            firstColumnVBox.getChildren().add(correctOptionTextFields.get(correctOptionTextFields.size() - 1));
        });
        removeCorrectOptionButton.setOnAction(actionEvent -> {
            if (correctOptionTextFields.size() > 1) {
                firstColumnVBox.getChildren().remove(correctOptionTextFields.get(correctOptionTextFields.size() - 1));
                correctOptionTextFields.remove(correctOptionTextFields.size() - 1);
            }
        });

        Label incorrectOptionLabel = new Label("Set Incorrect Options:");
        incorrectOptionTextFields = new ArrayList<>();

        Button addIncorrectOptionButton = new Button("Add Incorrect Option");
        Button removeIncorrectOptionButton = new Button("Remove Last Incorrect Option");

        addIncorrectOptionButton.setOnAction(actionEvent -> {
            incorrectOptionTextFields.add(new TextField());
            secondColumnVBox.getChildren().add(incorrectOptionTextFields.get(incorrectOptionTextFields.size() - 1));
        });
        removeIncorrectOptionButton.setOnAction(actionEvent -> {
            if (!incorrectOptionTextFields.isEmpty()) {
                secondColumnVBox.getChildren().remove(incorrectOptionTextFields.get(incorrectOptionTextFields.size() - 1));
                incorrectOptionTextFields.remove(incorrectOptionTextFields.size() - 1);
            }
        });


        firstColumnVBox.getChildren().addAll(correctOptionLabel, addCorrectOptionButton, removeCorrectOptionButton);
        firstColumnVBox.getChildren().addAll(correctOptionTextFields);

        secondColumnVBox.getChildren().addAll(incorrectOptionLabel, addIncorrectOptionButton, removeIncorrectOptionButton);

        Button buildButton = getBuildButton(window, QuizEditorQuestion.QuestionType.MULTI_SELECT);

        hBoxContentPane.getChildren().addAll(firstColumnVBox, secondColumnVBox);

        vBoxContentPane = new VBox();
        vBoxContentPane.getChildren().add(buildButton);
        vBoxContentPane.getChildren().addAll(questionPromptLabel, questionPromptTextField, hBoxContentPane);

        scrollPane = new ScrollPane(vBoxContentPane);
    }

    private Button getBuildButton(Stage window, QuizEditorQuestion.QuestionType questionType) {
        Button buildButton = new Button("Add Question");
        buildButton.setOnAction(actionEvent -> {
            question = new QuizEditorQuestion(questionType);
            question.setQuestionPrompt(questionPromptTextField.getText().strip());
            question.addCorrectOption(correctOptionTextFields.get(0).getText().strip());
            for (TextField textField: correctOptionTextFields) {
                question.addCorrectOption(textField.getText().strip());
            }
            for (TextField textField: incorrectOptionTextFields) {
                question.addIncorrectOption(textField.getText().strip());
            }
            window.fireEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSE_REQUEST));
        });
        return buildButton;
    }

    public QuizEditorQuestion buildQuestion() {
        return question;
    }
}
