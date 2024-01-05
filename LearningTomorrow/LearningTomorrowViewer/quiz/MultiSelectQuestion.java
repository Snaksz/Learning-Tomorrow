package LearningTomorrowViewer.quiz;

import java.util.List;

public class MultiSelectQuestion extends Question {
    public MultiSelectQuestion(String questionString, List<AnswerOption> options, List<AnswerOption> correctOptions){
        questionPrompt = questionString;

        totalSelection = new MultiSelection();
        for (AnswerOption option: options) {
            totalSelection.selectOption(option);
        }

        correctSelection = new MultiSelection();
        for (AnswerOption option: correctOptions) {
            correctSelection.selectOption(option);
        }

        currentSelection = new MultiSelection();
    }

    @Override
    protected String toStoreString() {
        return Quiz.MULTI_SELECT_TYPE + ", " + correctSelection.getSelection().size() + "\n" + super.toStoreString();
    }
}
