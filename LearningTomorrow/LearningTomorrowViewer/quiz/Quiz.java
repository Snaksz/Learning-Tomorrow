package LearningTomorrowViewer.quiz;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.NotDirectoryException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class storing questions and answers for a quiz
 */
public class Quiz {

    public static final String SINGLE_SELECT_TYPE = "SingleSelect";
    public static final String MULTI_SELECT_TYPE = "MultiSelect";

    private List<Question> questions;

    /**
     * Quiz constructor
     * @param quizFile File object for the quiz file
     */
    public Quiz(File quizFile) throws IOException {
        questions = new ArrayList<>();
        loadQuiz(quizFile);
    }

    public Quiz(List<Question> questions) {
        this.questions = questions;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public int getScore() {
        int score = 0;
        for (Question question: questions) {
            if (question.checkSelection()) score++;
        }
        return score;
    }

    public void storeQuiz(File directory, String filename) throws IOException {
        if (!directory.isDirectory()) throw new NotDirectoryException(directory.getName());

        File quizFile = new File(directory.getAbsolutePath() + File.separator + filename);

        if (!quizFile.createNewFile()) throw new FileAlreadyExistsException(quizFile.getName());

        BufferedWriter bw = new BufferedWriter(new FileWriter(quizFile));

        for (Question question: questions) {
            bw.write(question.toStoreString());
            bw.write('\n');
        }

        bw.close();
    }

    /**
     * Load quiz from a file
     * @param quizFile File object for the quiz file
     * @throws IOException if the file cannot be read
     */
    private void loadQuiz(File quizFile) throws IOException {
        // Use BufferedReader
        // File format
        // Question 1 Type
        // Question
        // Option 1
        // Option 2
        // ...
        // Option n
        // empty line
        // Question 2 Type
        // Question
        // ...

        // If question type is multiselect, the type line will look like this
        // MultiSelect, n
        // where n is the number of correct options

        BufferedReader br = new BufferedReader(new FileReader(quizFile));
        while (br.ready()) {
            questions.add(loadQuestion(br));
        }

        br.close();
    }

    private Question loadQuestion(BufferedReader br) throws IOException {
        String questionType, questionPrompt;
        if ((questionType = br.readLine()) == null) throw new IOException();
        if ((questionPrompt = br.readLine()) == null) throw new IOException();

        String line;
        List<String> options = new ArrayList<>();
        while ((line = br.readLine()) != null && !line.isBlank()) {
            options.add(line.strip());
        }

        QuestionBuilder builder;

        if (questionType.startsWith(SINGLE_SELECT_TYPE)) {
            if (options.isEmpty()) throw new IOException();
            builder = new SingleSelectQuestionBuilder();
            builder.addPrompt(questionPrompt.strip());
            builder.addCorrectAnswerOption(options.get(0));
            for (int i = 1; i < options.size(); i++) {
                builder.addIncorrectAnswerOption(options.get(i));
            }
        }
        else if (questionType.startsWith(MULTI_SELECT_TYPE)) {
            int numCorrect;
            try {
                numCorrect = Integer.parseInt(questionType.split(",")[1].strip());
            } catch (NumberFormatException e) {
                throw new IOException();
            }
            if (options.size() < numCorrect) throw new IOException();

            builder = new MultiSelectQuestionBuilder();

            builder.addPrompt(questionPrompt.strip());
            for (int i = 0; i < numCorrect; i++) {
                builder.addCorrectAnswerOption(options.get(i));
            }
            for (int i = numCorrect; i < options.size(); i++) {
                builder.addIncorrectAnswerOption(options.get(i));
            }
        }
        else throw new IOException();

        return builder.buildQuestion();
    }
}
