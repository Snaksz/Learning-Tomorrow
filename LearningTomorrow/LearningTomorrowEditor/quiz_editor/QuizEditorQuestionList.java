package LearningTomorrowEditor.quiz_editor;

import LearningTomorrowViewer.quiz.Question;
import LearningTomorrowViewer.quiz.Quiz;
import javafx.scene.control.ListView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class QuizEditorQuestionList extends ListView<QuizEditorQuestion> {

    public QuizEditorQuestionList(File file) throws IOException {
        super();
        loadFile(file);
    }

    public void saveFile(File file) throws IOException {
        List<Question> questionList = new ArrayList<>();

        for (QuizEditorQuestion question: this.getItems()) {
            questionList.add(question.buildQuestion());
        }

        Quiz quiz = new Quiz(questionList);
        if (file.exists())
            if (!file.delete()) throw new IOException("Cannot write file");
        quiz.storeQuiz(file.getParentFile(), file.getName());
    }

    private void loadFile(File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));

        while (br.ready()) {
            this.getItems().add(new QuizEditorQuestion(br));
        }

        br.close();
    }
}
