package LearningTomorrowViewer.quiz;

public interface QuestionBuilder {

    void reset();

    void addPrompt(String question);

    void addIncorrectAnswerOption(String option);

    void addCorrectAnswerOption(String option);

    Question buildQuestion();
}
