package LearningTomorrowViewer.quiz;

import java.util.ArrayList;
import java.util.List;

public class SingleSelectQuestionBuilder implements QuestionBuilder {

    private String questionPrompt;
    private List<AnswerOption> answerOptions;
    private AnswerOption correctOption;

    public SingleSelectQuestionBuilder() {
        reset();
    }

    @Override
    public void reset() {
        questionPrompt = "";
        answerOptions = new ArrayList<>();
        correctOption = null;
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
        correctOption = answerOption;
    }

    @Override
    public Question buildQuestion() {
        return new SingleSelectQuestion(questionPrompt, answerOptions, correctOption);
    }
}
