package LearningTomorrowViewer.quiz;

import java.util.ArrayList;
import java.util.List;

public class SingleSelection extends Selection {

    public SingleSelection() {
        selectedOptions = new ArrayList<>(1);
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
        if (!selectedOptions.isEmpty()) selectedOptions.remove(0);
        selectedOptions.add(s);
    }

    @Override
    public void removeOption(AnswerOption s) {
        selectedOptions.remove(0);
    }

    @Override
    public List<AnswerOption> getSelection() {
        return selectedOptions;
    }
}
