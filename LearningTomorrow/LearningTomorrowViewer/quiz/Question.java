package LearningTomorrowViewer.quiz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Question {
    protected String questionPrompt;
    protected Selection totalSelection;
    protected Selection currentSelection;
    protected Selection correctSelection;

    public String getPrompt() {
        return questionPrompt;
    }

    public void selectOption(AnswerOption option) {
        currentSelection.selectOption(option);
    }

    public void deselectOption(AnswerOption option) {
        currentSelection.removeOption(option);
    }

    public AnswerOption[] getOptions(){
        List<AnswerOption> copy = new ArrayList<>(totalSelection.getSelection());
        Collections.shuffle(copy);
        return copy.toArray(new AnswerOption[0]);
    }

    public boolean checkSelection(){
        return currentSelection.equals(correctSelection);
    }

    protected String toStoreString() {
        StringBuilder sb = new StringBuilder();
        sb.append(questionPrompt);
        sb.append("\n");

        for (AnswerOption answerOption: correctSelection.getSelection()) {
            sb.append(answerOption.option());
            sb.append("\n");
        }
        for (AnswerOption answerOption: totalSelection.getSelection()) {
            if (!correctSelection.getSelection().contains(answerOption)) {
                sb.append(answerOption.option());
                sb.append("\n");
            }
        }

        return sb.toString();
    }
}
