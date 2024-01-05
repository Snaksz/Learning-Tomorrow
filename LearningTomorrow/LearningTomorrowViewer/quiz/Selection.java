package LearningTomorrowViewer.quiz;

import java.util.List;

public abstract class Selection {
    protected List<AnswerOption> selectedOptions;

    public abstract boolean equals(Selection sel);

    public abstract void selectOption(AnswerOption s);

    public abstract void removeOption(AnswerOption s);

    public abstract List<AnswerOption> getSelection();

}
