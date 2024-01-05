package LearningTomorrowViewer.quiz;

import java.util.ArrayList;
import java.util.List;

public class MultiSelectQuestionBuilder implements QuestionBuilder{
    private String questionPrompt;
    private List<AnswerOption> answerOptions;
    private List<AnswerOption> correctOptions;

    public MultiSelectQuestionBuilder() {
        reset();
    }

    @Override
    public void reset() {
        questionPrompt = "";
        answerOptions = new ArrayList<>();
        correctOptions = new ArrayList<>();
    }

    @Override
    public void addPrompt(String question) {
        questionPrompt = question;
    }

    @Override
    public void addIncorrectAnswerOption(String option) {
        AnswerOption answerOption = new AnswerOption(option);
        answerOptions.add(answerOption);
    }

    @Override
    public void addCorrectAnswerOption(String option) {
        AnswerOption answerOption = new AnswerOption(option);
        answerOptions.add(answerOption);
        correctOptions.add(answerOption);
    }

    @Override
    public Question buildQuestion() {
        return new MultiSelectQuestion(questionPrompt, answerOptions, correctOptions);
    }
}
