package LearningTomorrowEditor.table_of_contents;

import javafx.event.EventHandler;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Table of Contents for LTEditor
 */
public class EditorTOC {
    public static final char DEPTH_DELIMITER = '>';
    private final TreeView<TOCNode> treeView;
    private TreeItem<TOCNode> treeRoot;
    private final SectionNodeContextMenu sectionNodeContextMenu;
    private final ContentNodeContextMenu contentNodeContextMenu;

    /**
     * Editor Table of Contents constructor
     * @param courseName name of the course
     */
    public EditorTOC(String courseName, File courseDirectory) {
        treeView = new TreeView<>();
        treeView.setEditable(true);
        treeRoot = new TreeItem<>(new SectionTOCNode(courseName));
        treeView.setRoot(treeRoot);

        sectionNodeContextMenu = new SectionNodeContextMenu();
        contentNodeContextMenu = new ContentNodeContextMenu();

        ContextMenuHandlerInitializer.initSectionNodeContextMenuHandlers(
                treeView, sectionNodeContextMenu, courseDirectory
        );
        ContextMenuHandlerInitializer.initContentNodeContextMenuHandlers(
                treeView, contentNodeContextMenu, courseDirectory
        );

        treeView.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                TOCNode selectedNode = treeView.getSelectionModel().getSelectedItem().getValue();
                if (selectedNode.nodeType == TOCNode.NodeType.SECTION) {
                    sectionNodeContextMenu.show(treeView, mouseEvent.getScreenX(), mouseEvent.getScreenY());
                }
                else if (selectedNode.nodeType == TOCNode.NodeType.CONTENT) {
                    contentNodeContextMenu.show(treeView, mouseEvent.getScreenX(), mouseEvent.getScreenY());
                }
            }
        });
    }

    /**
     * Getter method for the TreeView object
     * @return TreeView to display the table of contents
     */
    public TreeView<TOCNode> getTreeView() {
        return treeView;
    }

    /**
     * Saves the table of contents
     * @param filepath file to write the table into. This file must be empty when passed in!
     * @throws IOException if an exception occurs while writing
     */
    public void saveTreeView(File filepath) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(filepath));
        saveTreeNode(bw, treeRoot, 0);
        bw.close();
    }

    /**
     * Writes a node into the BufferedWriter
     * @param bw BufferedWriter to write into
     * @param treeNode node to save
     * @param depth depth of the node along the tree
     * @throws IOException if an exception occurs while writing
     */
    private void saveTreeNode(BufferedWriter bw, TreeItem<TOCNode> treeNode, int depth) throws IOException {
        for (int i = 0; i < depth; i++) {
            bw.write(DEPTH_DELIMITER);
        }
        TOCNode tocNode = treeNode.getValue();
        switch (tocNode.nodeType) {
            case SECTION -> {
                bw.write(tocNode.toString());
                bw.write('\n');
                for (TreeItem<TOCNode> subtree: treeNode.getChildren()) {
                    saveTreeNode(bw, subtree, depth + 1);
                }
            }
            case CONTENT -> {
                bw.write(tocNode.toString().split(":")[0].strip());
                bw.write(',');
                bw.write(((ContentTOCNode)tocNode).getContentType().toString());
                bw.write(',');
                bw.write(((ContentTOCNode)tocNode).getFilename());
                bw.write('\n');
            }
        }
    }

    /**
     * Loads a tree view representing this table of contents
     * @param filepath file to load from
     * @throws IOException if an exception occurs while reading
     */
    public void loadTreeView(File filepath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filepath));
        Map<Integer, TreeItem<TOCNode>> map = new HashMap<>();

        String line = br.readLine();
        if (line == null) throw new IOException("The table of contents file is empty!");
        TreeItem<TOCNode> root = new TreeItem<>(new SectionTOCNode(line.strip()));
        map.put(0, root);

        while ((line = br.readLine()) != null) {
            line = line.strip();
            int depth = countDepth(line);
            if (!map.containsKey(depth - 1))
                throw new IOException("The table of contents file is not properly formatted!");
            line = line.substring(depth);
            TOCNode tocNode = getTocNode(line);
            TreeItem<TOCNode> node = new TreeItem<>(tocNode);
            map.get(depth - 1).getChildren().add(node);
            map.put(depth, node);
        }

        treeView.setRoot(root);
        this.treeRoot = root;
        treeView.setEditable(true);

        br.close();
    }

    /**
     * Parses a loaded line to get a TOCNode
     * @param line string loaded from the saved TOC file
     * @return TOCNode that the line represents
     * @throws IOException if the line is not formatted properly
     */
    private TOCNode getTocNode(String line) throws IOException {
        String[] split = line.split(",");
        TOCNode tocNode;
        if (split.length == 1) {
            tocNode = new SectionTOCNode(split[0].strip());
        }
        else if (split.length == 3) {
            ContentTOCNode.ContentType contentType = getContentType(split[1].strip());
            if (contentType == null)
                throw new IOException("The table of contents file is not properly formatted!");
            tocNode = new ContentTOCNode(split[0].strip(), contentType, split[2].strip());
        }
        else throw new IOException("The table of contents file is not properly formatted!");
        return tocNode;
    }

    /**
     * Returns ContentType object based on its string representation
     * @param str string representation of the ContentType object
     * @return ContentType object
     */
    private ContentTOCNode.ContentType getContentType(String str) {
        switch (str) {
            case "VIDEO" -> {
                return ContentTOCNode.ContentType.VIDEO;
            }
            case "NOTE" -> {
                return ContentTOCNode.ContentType.NOTE;
            }
            case "QUIZ" -> {
                return ContentTOCNode.ContentType.QUIZ;
            }
            default -> {
                return null;
            }
        }
    }

    /**
     * Counts the number of DEPTH_DELIMITER chars which represent
     * the depth at which the node should be in the tree view.
     * @param line line representing the node
     * @return number of DEPTH_DELIMITER chars at the beginning of the line
     */
    private int countDepth(String line) {
        int count = 0;
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == DEPTH_DELIMITER) count++;
            else break;
        }
        return count;
    }
}
