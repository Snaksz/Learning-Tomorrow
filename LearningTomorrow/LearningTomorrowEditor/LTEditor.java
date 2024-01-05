package LearningTomorrowEditor;

import LearningTomorrowEditor.quiz_editor.QuizEditor;
import LearningTomorrowEditor.table_of_contents.ContentTOCNode;
import LearningTomorrowEditor.table_of_contents.EditorTOC;
import LearningTomorrowEditor.table_of_contents.TOCNode;
import LearningTomorrowEditor.toolbar.EditorToolbar;
import LearningTomorrowViewer.courseContentPanes.NotesPane;
import LearningTomorrowViewer.courseContentPanes.QuizPane;
import LearningTomorrowViewer.courseContentPanes.VideoPane;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Class to create a course
 */
public class LTEditor {

    private static final String PATH_TO_SAVED_EDITOR_FILES = "Saved" + File.separator + "Editor";
    private static final String PATH_TO_SAVED_VIEWER_FILES = "Saved" + File.separator + "Viewer";

    private ContentTOCNode.ContentType currentContent = null;

    private Stage stage;
    private NotesEditor notesEditor;
    private VideoEditor videoEditor;
    private QuizEditor quizEditor;

    private EditorToolbar toolbar;

    private Pane toolbarPane;
    private EditorTOC tableOfContents;
    private Pane editorPane;

    // Components laid out as
    // Toolbar
    // -------
    // TOC | editor
    private SplitPane verticalSplitPane;
    private SplitPane horizontalSplitPane;
    private File courseDirectory;

    /**
     * Constructor for LTEditor
     * @param stage window object to add UI components to
     */
    public LTEditor(Stage stage) {
        this.stage = stage;

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("New Course");
        dialog.setHeaderText("Please enter the name of your course:");
        dialog.setContentText("Name");

        // Show the dialog and capture the user's response
        dialog.showAndWait().ifPresent(this::initUI);
    }

    /**
     * Initialize UI components
     * @param courseName name of the course entered by the user
     */
    private void initUI(String courseName) {
        courseDirectory = new File(PATH_TO_SAVED_EDITOR_FILES + File.separator + courseName);
        if (!courseDirectory.mkdir()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error creating a directory. It might already exist");
            alert.showAndWait().ifPresent(ev -> {
                stage.close();
                System.exit(1);
            });
        }

        verticalSplitPane = new SplitPane();
        horizontalSplitPane = new SplitPane();

        toolbarPane = new Pane();
        ScrollPane scrollableToolbarPane = new ScrollPane(toolbarPane);

        setUpTableOfContents(courseName);
        setUpToolbar();

        editorPane = new Pane();
        Label editorLabel = new Label("Editor Tab");
        editorPane.getChildren().add(editorLabel);

        horizontalSplitPane.getItems().addAll(tableOfContents.getTreeView(), editorPane);

        stage.setTitle("Learning Tomorrow Editor - " + courseName);
        Scene scene = new Scene(verticalSplitPane);
        this.stage.setMaximized(true);
        this.stage.setResizable(true);
        scene.setFill(Color.WHITE);
        this.stage.setScene(scene);
        this.stage.show();

        verticalSplitPane.setOrientation(Orientation.VERTICAL);
        verticalSplitPane.getItems().addAll(scrollableToolbarPane, horizontalSplitPane);
        verticalSplitPane.setDividerPosition(0, toolbarPane.getHeight() / stage.getHeight());
        verticalSplitPane.getDividers().get(0).positionProperty().addListener((observable, oldValue, newValue )
                -> verticalSplitPane.setDividerPosition(0, toolbarPane.getHeight() / stage.getHeight()));

        horizontalSplitPane.setDividerPosition(0, 0.3);
    }

    /**
     * Set up Table of Contents for the Editor
     * @param courseName name of the course entered by the user
     */
    private void setUpTableOfContents(String courseName) {
        tableOfContents = new EditorTOC(courseName, courseDirectory);

        tableOfContents.getTreeView().addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.getClickCount() == 2) {
                TOCNode tocNode = tableOfContents.getTreeView().getSelectionModel().getSelectedItem().getValue();
                if (tocNode.nodeType == TOCNode.NodeType.CONTENT) {
                    ContentTOCNode contentTOCNode = (ContentTOCNode) tocNode;
                    switch (contentTOCNode.getContentType()) {
                        case NOTE -> setUpNotesPane(new File(courseDirectory.getAbsolutePath()
                                + File.separator + contentTOCNode.getFilename()));
                        case VIDEO -> setUpVideoPane(new File(courseDirectory
                                + File.separator + contentTOCNode.getFilename()));
                        case QUIZ -> setUpQuizPane(new File(courseDirectory.getAbsolutePath()
                                + File.separator + contentTOCNode.getFilename()));
                    }
                }
            }
        });
    }

    /**
     * Set up Toolbar for the editor
     */
    private void setUpToolbar() {
        toolbar = new EditorToolbar(toolbarPane);

        toolbar.setSaveHandler(actionEvent -> {
            try {
                saveCourse(courseDirectory.getName());
            } catch (IOException e){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Error saving the file");
                alert.showAndWait();
            }
        });

        toolbar.setPreviewHandler(actionEvent -> previewContent());
        toolbar.setExportHandler(actionEvent -> exportCourse());
    }

    /**
     * Save all course files
     * @param courseName name of the course, which coincides with the folder name
     * @throws IOException if there was an IOException trying to save the file or some file was not formatted properly
     */
    private void saveCourse(String courseName) throws IOException {
        //if (!courseDirectory.isDirectory()) return;
        File tocFile = new File(courseDirectory.getAbsolutePath() + File.separator + courseName + ".vwr");
        if (tocFile.exists()) {
            if (!tocFile.delete()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Error saving the file");
                alert.showAndWait();
            }
        }
        if (!tocFile.createNewFile()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error saving the file");
            alert.showAndWait();
        }
        tableOfContents.saveTreeView(tocFile);

        if (currentContent == ContentTOCNode.ContentType.NOTE) {
            notesEditor.saveText();
        }
        if (currentContent == ContentTOCNode.ContentType.QUIZ) {
            quizEditor.saveFile();
        }
    }

    /**
     * Load up the preview of the currently displayed content
     */
    private void previewContent() {
        if (currentContent == null) return;

        Stage previewWindow = new Stage();
        previewWindow.setTitle("Preview");
        Pane previewPane = new Pane();
        ScrollPane previewScrollPane = new ScrollPane(previewPane);
        Scene previewScene = new Scene(previewScrollPane);
        previewWindow.setScene(previewScene);
        switch (currentContent) {
            case NOTE -> {
                NotesPane notesPane = new NotesPane(previewPane);
                try {
                    notesPane.loadContent(notesEditor.getFile().getAbsolutePath());
                } catch (IOException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Error previewing the file");
                    alert.showAndWait();
                    previewWindow.close();
                }
                previewWindow.show();
            }
            case VIDEO -> {
                VideoPane videoPane = new VideoPane(previewPane);
                try {
                    videoPane.loadContent(videoEditor.getFile().getAbsolutePath());
                } catch (IOException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Error previewing the file");
                    alert.showAndWait();
                    previewWindow.close();
                }
                previewWindow.show();
            }
            case QUIZ -> {
                QuizPane quizPane = new QuizPane(previewPane);
                try {
                    quizPane.loadContent(quizEditor.getFile().getAbsolutePath());
                } catch (IOException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Error previewing the file");
                    alert.showAndWait();
                    previewWindow.close();
                }
                previewWindow.show();
            }
        }
    }

    /**
     * Export the course to the Viewer folder
     */
    private void exportCourse() {
        File f = new File(PATH_TO_SAVED_VIEWER_FILES);
        Path dir = Paths.get(f.toURI());

        if (!(Files.exists(dir) && Files.isDirectory(dir))) return;

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for (Path file : stream) {
                if (file.toFile().getName().equals(courseDirectory.getName())) {
                    if (!file.toFile().delete()) throw new IOException();
                }
            }
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error exporting the course");
            alert.setContentText("Cannot delete existing folder");
            alert.showAndWait();
            return;
        }

        Path sourceDirectory = Paths.get(courseDirectory.toURI());

        File destinationFolderFile = new File(PATH_TO_SAVED_VIEWER_FILES
                + File.separator + courseDirectory.getName());
        if (!destinationFolderFile.mkdir()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error exporting the course");
            alert.setContentText("Cannot create a new folder");
            alert.showAndWait();
            return;
        }
        Path destinationDirectory = Paths.get(destinationFolderFile.toURI());
        try {
            copyFolder(sourceDirectory, destinationDirectory);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error exporting the course");
            alert.setContentText("Cannot copy the folder");
            alert.showAndWait();
        }

    }

    /**
     * Copy all contents of a directory into another directory
     * @param source Path object for the source directory
     * @param destination Path object for the destination directory
     * @throws IOException if a file cannot be read/written
     */
    private static void copyFolder(Path source, Path destination) throws IOException {
        // This method was generated by ChatGPT
        Files.walkFileTree(source, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Path targetFile = destination.resolve(source.relativize(file));
                Files.copy(file, targetFile, StandardCopyOption.REPLACE_EXISTING);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                Path targetDir = destination.resolve(source.relativize(dir));
                Files.createDirectories(targetDir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    /**
     * Set up Notes Editor pane
     * @param file file to load
     */
    private void setUpNotesPane(File file) {
        editorPane.getChildren().clear();
        notesEditor = new NotesEditor(editorPane, file);
        currentContent = ContentTOCNode.ContentType.NOTE;
    }

    /**
     * Set up Video Editor pane
     * @param file file to load
     */
    private void setUpVideoPane(File file) {
        editorPane.getChildren().clear();
        videoEditor = new VideoEditor(editorPane, file);
        currentContent = ContentTOCNode.ContentType.VIDEO;
    }

    /**
     * Set up Quiz Editor pane
     * @param file file to load
     */
    private void setUpQuizPane(File file) {
        editorPane.getChildren().clear();
        quizEditor = new QuizEditor(editorPane, file);
        currentContent = ContentTOCNode.ContentType.QUIZ;
    }
}
