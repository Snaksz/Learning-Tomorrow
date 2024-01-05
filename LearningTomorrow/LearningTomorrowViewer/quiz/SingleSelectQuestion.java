package LearningTomorrowViewer.quiz;

import java.util.List;

public class SingleSelectQuestion extends Question{

    public SingleSelectQuestion(String questionString, List<AnswerOption> options, AnswerOption correctAnswer){
        questionPrompt = questionString;

        totalSelection = new MultiSelection();
        for (AnswerOption option: options) {
            totalSelection.selectOption(option);
        }

        correctSelection = new SingleSelection();
        correctSelection.selectOption(correctAnswer);

        currentSelection = new MultiSelection();
    }

    @Override
    protected String toStoreString() {
        return Quiz.SINGLE_SELECT_TYPE + "\n" + super.toStoreString();
    }
}
