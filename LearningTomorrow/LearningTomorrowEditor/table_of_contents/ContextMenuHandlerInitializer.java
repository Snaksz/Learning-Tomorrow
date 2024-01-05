package LearningTomorrowEditor.table_of_contents;

import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

/**
 * Utility class initialize the context menu event handlers
 */
public class ContextMenuHandlerInitializer {
    /**
     * Add an event handler for section nodes
     * @param treeView tree view object storing all nodes
     * @param contextMenu context menu for section nodes
     * @param courseDirectory path to the file directory for this course
     */
    public static void initSectionNodeContextMenuHandlers(
            TreeView<TOCNode> treeView, SectionNodeContextMenu contextMenu, File courseDirectory) {

        contextMenu.setAddSectionHandler(actionEvent -> {
            TreeItem<TOCNode> selectedNode = treeView.getSelectionModel().getSelectedItem();
            if (selectedNode.getValue().nodeType == TOCNode.NodeType.SECTION) {
                TextInputDialog dialog = getDialogBox("New Section",
                        "Please enter the title of your new section:", "Title");
                dialog.showAndWait().ifPresent(response -> {
                    SectionTOCNode newTOCNode = new SectionTOCNode(response);
                    TreeItem<TOCNode> newTreeItem = new TreeItem<>(newTOCNode);
                    selectedNode.getChildren().add(newTreeItem);
                });

            }
        });

        contextMenu.setAddNoteHandler(actionEvent -> {
            TreeItem<TOCNode> selectedNode = treeView.getSelectionModel().getSelectedItem();
            if (selectedNode.getValue().nodeType == TOCNode.NodeType.SECTION) {
                TextInputDialog dialog = getDialogBox("New Note - Title",
                        "Please enter the title of your new note:", "Title");
                dialog.showAndWait().ifPresent(title -> {
                    TextInputDialog dialogFilename = getDialogBox("New Note - Filename",
                            "Please enter the filename for your new note:", "Filename");
                    dialogFilename.showAndWait().ifPresent(filename -> {
                        filename += ".html";
                        try {
                            createFile(filename, courseDirectory);
                            ContentTOCNode newTOCNode = new ContentTOCNode(title,
                                    ContentTOCNode.ContentType.NOTE, filename);

                            TreeItem<TOCNode> newTreeItem = new TreeItem<>(newTOCNode);
                            selectedNode.getChildren().add(newTreeItem);
                        } catch (IOException e) {
                            displayAlert("Error", "Cannot create the file. It might already exist");
                        }
                    });
                });
            }
        });

        contextMenu.setAddVideoHandler(actionEvent -> {
            TreeItem<TOCNode> selectedNode = treeView.getSelectionModel().getSelectedItem();
            if (selectedNode.getValue().nodeType == TOCNode.NodeType.SECTION) {
                TextInputDialog dialog = getDialogBox("New Video - Title",
                        "Please enter the title of your new video:", "Title");
                dialog.showAndWait().ifPresent(title -> {
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Select Video File");
                    fileChooser.getExtensionFilters().add(
                            new FileChooser.ExtensionFilter("Video Files", "*.mp4", "*.m4a", "*.m4v")
                    );

                    Stage fileChooserStage = new Stage();
                    File videoFile = fileChooser.showOpenDialog(fileChooserStage);

                    if (videoFile == null) {
                        displayAlert("Error", "File was not chosen, or was not a valid video file. " +
                                "The video file must either .mp4, .m4a, or .m4v format");
                    }
                    else {
                        try {
                            copyFileToCourseDirectory(videoFile, courseDirectory);
                            ContentTOCNode newTOCNode = new ContentTOCNode(title,
                                    ContentTOCNode.ContentType.VIDEO, videoFile.getName());

                            TreeItem<TOCNode> newTreeItem = new TreeItem<>(newTOCNode);
                            selectedNode.getChildren().add(newTreeItem);
                        }
                        catch (IOException e) {
                            displayAlert("Error", "Cannot move the file to the course directory");
                        }
                    }
                });
            }
        });

        contextMenu.setAddQuizHandler(actionEvent -> {
            TreeItem<TOCNode> selectedNode = treeView.getSelectionModel().getSelectedItem();
            if (selectedNode.getValue().nodeType == TOCNode.NodeType.SECTION) {
                TextInputDialog dialog = getDialogBox("New Quiz - Title",
                        "Please enter the title of your new quiz:", "Title");
                dialog.showAndWait().ifPresent(title -> {
                    TextInputDialog dialogFilename = getDialogBox("New Quiz - Filename",
                            "Please enter the filename for your new quiz:", "Filename");
                    dialogFilename.showAndWait().ifPresent(filename -> {
                        filename += ".qz";
                        try {
                            createFile(filename, courseDirectory);
                            ContentTOCNode newTOCNode = new ContentTOCNode(title,
                                    ContentTOCNode.ContentType.QUIZ, filename);

                            TreeItem<TOCNode> newTreeItem = new TreeItem<>(newTOCNode);
                            selectedNode.getChildren().add(newTreeItem);
                        } catch (IOException e) {
                            displayAlert("Error", "Cannot create the file. It might already exist");
                        }
                    });
                });
            }
        });

        contextMenu.setDeleteSectionHandler(actionEvent -> {
            TreeItem<TOCNode> selectedNode = treeView.getSelectionModel().getSelectedItem();
            if (selectedNode.getParent() == null) {
                displayAlert("Error", "Cannot delete the root directory");
                return;
            }
            if (selectedNode.getValue().nodeType == TOCNode.NodeType.SECTION) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirm Content Deletion");
                alert.setHeaderText("Are you sure you want to delete this section?");
                alert.setContentText(selectedNode.toString());

                Optional<ButtonType> result = alert.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.OK) {
                    TreeItem<TOCNode> parent = selectedNode.getParent();
                    for (TreeItem<TOCNode> node: selectedNode.getChildren()) {
                        parent.getChildren().add(node);
                    }
                    parent.getChildren().remove(selectedNode);
                }
            }
        });
    }

    /**
     * Add an event handler for content nodes
     * @param treeView tree view object storing all nodes
     * @param contextMenu context menu for content nodes
     * @param courseDirectory path to the file directory for this course
     */
    public static void initContentNodeContextMenuHandlers(
            TreeView<TOCNode> treeView, ContentNodeContextMenu contextMenu, File courseDirectory) {

        contextMenu.setDeleteContentHandler(actionEvent -> {
            TreeItem<TOCNode> selectedNode = treeView.getSelectionModel().getSelectedItem();
            if (selectedNode.getValue().nodeType == TOCNode.NodeType.CONTENT) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirm Content Deletion");
                alert.setHeaderText("Are you sure you want to delete this content?");
                alert.setContentText(selectedNode.toString());

                Optional<ButtonType> result = alert.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.OK) {
                    TreeItem<TOCNode> parent = selectedNode.getParent();
                    ContentTOCNode tocNode = (ContentTOCNode)selectedNode.getValue();
                    File file = new File(courseDirectory + File.separator + tocNode.getFilename());
                    if (!file.delete()) System.out.println("Could not delete the file");
                    parent.getChildren().remove(selectedNode);
                }
            }
        });
    }

    /**
     * Get a popup window to prompt the user for a text input
     * @param title title of the popup window
     * @param headerText header text of the popup window
     * @param contentText content text of the popup window
     * @return the TextInputDialog object for this popup window
     */
    private static TextInputDialog getDialogBox(String title, String headerText, String contentText) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(headerText);
        dialog.setContentText(contentText);
        return dialog;
    }

    /**
     * Display a popup window to indicate an error
     * @param title title of the popup window
     * @param message error message
     */
    private static void displayAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(title);
        alert.setContentText(message);

        alert.showAndWait();
    }

    /**
     * Add the file to the directory
     * @param file file to add
     * @param directory destination directory
     * @throws IOException if the file cannot be written
     */
    private static void copyFileToCourseDirectory(File file, File directory) throws IOException {
        Path sourcePath = Paths.get(file.toURI());

        // Destination directory path
        Path destinationDirectory = Paths.get(directory.toURI());

        if (!Files.exists(destinationDirectory)) {
            Files.createDirectories(destinationDirectory);
        }
        Path destinationPath = destinationDirectory.resolve(sourcePath.getFileName());
        Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * Create the file on disk
     * @param filename name of the file
     * @param directory destination directory
     * @throws IOException if the file cannot be created
     */
    private static void createFile(String filename, File directory) throws IOException {
        File file = new File(directory.getAbsolutePath() + File.separator + filename);
        if (!file.createNewFile()) throw new IOException();
    }
}
