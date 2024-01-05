package LearningTomorrowViewer.quiz;

import java.util.ArrayList;
import java.util.List;

public class MultiSelection extends Selection {

    public MultiSelection() {
        selectedOptions = new ArrayList<>();
    }

    @Override
    public boolean equals(Selection sel) {
        if (this.getSelection().size() != sel.getSelection().size()) return false;
        for (AnswerOption option: this.getSelection()) {
            if (!sel.getSelection().contains(option)) return false;
        }
        return true;
    }

    @Override
    public void selectOption(AnswerOption s) {
        if (!selectedOptions.contains(s)) selectedOptions.add(s);
    }

    @Override
    public void removeOption(AnswerOption s) {
        selectedOptions.remove(s);
    }

    @Override
    public List<AnswerOption> getSelection() {
        return selectedOptions;
    }
}
