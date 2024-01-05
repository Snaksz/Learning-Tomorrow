package LearningTomorrowEditor.quiz_editor;

import LearningTomorrowViewer.quiz.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class QuizEditorQuestion {
    public enum QuestionType {
        SINGLE_SELECT, MULTI_SELECT
    }
    private String questionPrompt;
    private List<String> correctOptions;
    private List<String> incorrectOptions;
    private final QuestionType questionType;

    public QuizEditorQuestion(QuestionType questionType) {
        questionPrompt = "";
        correctOptions = new ArrayList<>();
        incorrectOptions = new ArrayList<>();
        this.questionType = questionType;
    }

    public QuizEditorQuestion(BufferedReader br) throws IOException {
        correctOptions = new ArrayList<>();
        incorrectOptions = new ArrayList<>();

        String type = br.readLine();
        if (type == null) throw new IOException();
        type = type.strip();
        if (type.equals(Quiz.SINGLE_SELECT_TYPE)) {
            this.questionType = QuestionType.SINGLE_SELECT;

            String questionPrompt = br.readLine();
            if (questionPrompt == null || questionPrompt.isBlank()) throw new IOException();
            this.questionPrompt = questionPrompt;

            String correctOption = br.readLine();
            if (correctOption == null || correctOption.isBlank()) throw new IOException();
            this.correctOptions.add(correctOption);

            String line;
            while ((line = br.readLine()) != null && !line.isBlank()) {
                this.incorrectOptions.add(line);
            }
        }
        else {
            String[] split = type.split(",");

            type = split[0].strip();
            if (!type.equals(Quiz.MULTI_SELECT_TYPE)) throw new IOException();

            int numCorrect;
            try {
                numCorrect = Integer.parseInt(split[1].strip());
            } catch (NumberFormatException e) {
                throw new IOException();
            }
            this.questionType = QuestionType.MULTI_SELECT;

            String questionPrompt = br.readLine();
            if (questionPrompt == null || questionPrompt.isBlank()) throw new IOException();
            this.questionPrompt = questionPrompt;

            for (int i = 0; i < numCorrect; i++) {
                String correctOption = br.readLine();
                if (correctOption == null || correctOption.isBlank()) throw new IOException();
                this.correctOptions.add(correctOption);
            }

            String line;
            while ((line = br.readLine()) != null && !line.isBlank()) {
                this.incorrectOptions.add(line);
            }
        }

    }

    public void setQuestionPrompt(String questionPrompt) {
        this.questionPrompt = questionPrompt;
    }

    public void addCorrectOption(String option) {
        correctOptions.add(option);
    }

    public void addIncorrectOption(String option) {
        incorrectOptions.add(option);
    }

    public void editCorrectOption(String option, int index) {
        if (index < 0 || index >= correctOptions.size()) return;
        correctOptions.set(index, option);
    }

    public void editIncorrectOption(String option, int index) {
        if (index < 0 || index >= incorrectOptions.size()) return;
        incorrectOptions.set(index, option);
    }

    public void removeCorrectOption(int index) {
        if (index < 0 || index >= correctOptions.size()) return;
        correctOptions.remove(index);
    }

    public void removeIncorrectOption(int index) {
        if (index < 0 || index >= incorrectOptions.size()) return;
        incorrectOptions.remove(index);
    }

    public Question buildQuestion(){
        QuestionBuilder questionBuilder;
        switch (questionType) {
            case SINGLE_SELECT -> questionBuilder = new SingleSelectQuestionBuilder();
            case MULTI_SELECT -> questionBuilder = new MultiSelectQuestionBuilder();
            default -> {
                return null;
            }
        }
        questionBuilder.addPrompt(this.questionPrompt);
        for (String option: correctOptions) {
            if (option.isBlank()) questionBuilder.addCorrectAnswerOption(".");
            else questionBuilder.addCorrectAnswerOption(option);
        }
        for (String option: incorrectOptions) {
            if (option.isBlank()) questionBuilder.addIncorrectAnswerOption(".");
            else questionBuilder.addIncorrectAnswerOption(option);
        }
        return questionBuilder.buildQuestion();
    }

    public String toString() {
        return questionType.toString() + " Question: " + questionPrompt;
    }
}
