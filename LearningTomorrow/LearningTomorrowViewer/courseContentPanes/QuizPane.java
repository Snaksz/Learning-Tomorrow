package LearningTomorrowViewer.courseContentPanes;

import LearningTomorrowViewer.ColorChanger;
import LearningTomorrowViewer.FontChanger;
import LearningTomorrowViewer.quiz.AnswerOption;
import LearningTomorrowViewer.quiz.Question;
import LearningTomorrowViewer.quiz.Quiz;
import javafx.scene.AccessibleRole;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.web.WebView;

import java.io.File;
import java.io.IOException;

public class QuizPane extends CourseContent {

    private static class OptionButton extends ToggleButton {
        protected Circle selectedIcon;
        protected Circle unselectedIcon;
        protected String option;
        protected OptionButton(String option) {
            super();
            selectedIcon = new Circle(10, Color.GREEN);
            unselectedIcon = new Circle(10, Color.rgb(0, 0, 0, 0));
            unselectedIcon.setStroke(Color.LIGHTGRAY);

            this.option = option;

            this.setGraphic(unselectedIcon);
            this.setText(option);

            this.setSelected(false);
        }

        protected void changeFont(FontChanger fontChanger, String fontName, int fontSize) {
            fontChanger.changeFont(this, fontName, fontSize);
        }

        public void changeColors(ColorChanger colorChanger, ColorMode m){
            //colorChanger.changeColor(optionWebView, m);
        }
    }

    private static class QuestionPane extends VBox {
        private final OptionButton[] toggleButtons;
        private final Button submitButton;

        protected QuestionPane(Question question) {
            super();

            Label questionPromptLabel = new Label();
            questionPromptLabel.setText(question.getPrompt());
            this.getChildren().add(questionPromptLabel);

            AnswerOption[] options = question.getOptions();
            toggleButtons = new OptionButton[options.length];

            for (int i = 0; i < toggleButtons.length; i++) {
                toggleButtons[i] = new OptionButton(options[i].option());

                // Accessibility stuff
                toggleButtons[i].setAccessibleRole(AccessibleRole.BUTTON);
                toggleButtons[i].setAccessibleRoleDescription("This button");
                toggleButtons[i].setAccessibleText("Option " + i);
                toggleButtons[i].setAccessibleHelp("Pick this option if you think that this option " + i + " is the answer");
                toggleButtons[i].setFocusTraversable(true);

                int finalI = i;
                toggleButtons[i].setOnAction(actionEvent -> {
                    if (!toggleButtons[finalI].isSelected()) {
                        toggleButtons[finalI].setGraphic(toggleButtons[finalI].selectedIcon);
                        question.selectOption(options[finalI]);
                    }
                    else {
                        toggleButtons[finalI].setGraphic(toggleButtons[finalI].unselectedIcon);
                        question.deselectOption(options[finalI]);
                    }
                });

                this.getChildren().add(toggleButtons[i]);
            }

            submitButton = new Button("Submit");

            this.getChildren().add(submitButton);
        }

        protected void changeFont(FontChanger fontChanger, String fontName, int fontSize) {
            for (OptionButton button : toggleButtons) {
                button.changeFont(fontChanger, fontName, fontSize);
            }
        }
    }


    private static class QuizCompletedPane extends VBox {
        protected QuizCompletedPane(Quiz quiz) {
            super();
            Label label = new Label();
            label.setText("You've completed the quiz with a score of " + quiz.getScore()
                    + " out of " + quiz.getQuestions().size());
            // Accessibility stuff
            label.setAccessibleRole(AccessibleRole.TEXT);
            label.setAccessibleRoleDescription("");
            label.setAccessibleText("You achieved a score of " + quiz.getScore() + " out of " + quiz.getQuestions().size());
            label.setAccessibleHelp("You may move on to the next section.");
            label.setFocusTraversable(true);
            this.getChildren().add(label);
        }
    }

    private Quiz quiz;
    private QuestionPane[] questionPanes;
    private QuizCompletedPane completedPane;

    /**
     * QuizPane constructor
     * @param pane the pane to add content to
     */
    public QuizPane(Pane pane) {
        this.pane = pane;
    }

    /**
     * Set up necessary GUI elements on the pane parameter
     */
    @Override
    public void setUpPane() {
        questionPanes = new QuestionPane[quiz.getQuestions().size()];

        for (int i = 0; i < questionPanes.length - 1; i++) {
            questionPanes[i] = new QuestionPane(quiz.getQuestions().get(i));
            // Accessible stuff
            questionPanes[i].setAccessibleRole(AccessibleRole.TAB_PANE);
            questionPanes[i].setAccessibleRoleDescription("Question " + i);
            questionPanes[i].setAccessibleText("The options are shown with the option button");
            questionPanes[i].setAccessibleHelp("Answer the current question to move on, hit tab to an option.");
            questionPanes[i].setFocusTraversable(true);
            int finalI = i;
            questionPanes[i].submitButton.setOnAction(actionEvent ->
                    QuizPane.this.pane.getChildren().set(0, questionPanes[finalI + 1]));
        }
        questionPanes[questionPanes.length - 1] = new QuestionPane(quiz.getQuestions().get(questionPanes.length - 1));
        completedPane = new QuizCompletedPane(quiz);
        questionPanes[questionPanes.length - 1].submitButton.setOnAction(actionEvent ->
                QuizPane.this.pane.getChildren().set(0, completedPane));

        this.pane.getChildren().add(questionPanes[0]);
    }

    /**
     * Load content from a content file stored at filepath
     * @param filepath path to the file storing the content
     * @throws IOException if there is an error reading the file
     */
    @Override
    public void loadContent(String filepath) throws IOException {
        quiz = new Quiz(new File(filepath));
        if (quiz.getQuestions().isEmpty()) throw new IOException("The quiz has no questions!");
        setUpPane();
    }

    @Override
    public void changeFont(FontChanger fontChanger, String fontName, int fontSize) {
        for (QuestionPane questionPane: questionPanes) {
            questionPane.changeFont(fontChanger, fontName, fontSize);
        }
    }
}
