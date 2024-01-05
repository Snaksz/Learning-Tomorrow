package LearningTomorrowViewer;

import LearningTomorrowEditor.table_of_contents.ContentTOCNode;
import LearningTomorrowEditor.table_of_contents.SectionTOCNode;
import LearningTomorrowEditor.table_of_contents.TOCNode;
import LearningTomorrowEditor.table_of_contents.EditorTOC;
import javafx.event.EventHandler;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static LearningTomorrowEditor.table_of_contents.EditorTOC.DEPTH_DELIMITER;

public class TableOfContents {

    Pane pane;
    TreeView<TOCNode> treeView;
    TreeItem<TOCNode> treeRoot;

    public TableOfContents(Pane pane, String filename) {
        this.pane = pane;
        try {
            loadTableOfContents(filename);
        } catch (IOException e) {
            System.out.println("Cannot read file " + filename);
            e.printStackTrace();
        }
    }

    /**
     * Load Table of Contents from a file
     * @param filename file storing table of contents
     * @throws IOException if the file cannot be read
     */
    /* private void loadTableOfContents(String filename) throws IOException {
        File file_to_parse = new File(filename);
        if (file_to_parse.exists()){
            BufferedReader buff = new BufferedReader(new FileReader(file_to_parse));
            String a = buff.readLine();
            // level counter
            int counter = 0;
            int lineCounter = 0;
            TreeView<String> the_tree = new TreeView<>();
            the_tree.setRoot(new TreeItem<>("LearningTomorrow"));
            while (a != null){
                int numberOfTabs = checker(a);
                if (numberOfTabs == 0){
                    // add new subtree
                    the_tree.getRoot().getChildren().add(new TreeItem<>(a));
                    counter += 1;
                } else if (numberOfTabs == counter){
                    TreeItem<String> curr = the_tree.getRoot();
                    // counter is just depth, so starting at depth 0, iterate
                    // until we get to appropriate level, with the right most node.
                    for (int i = 0; i < counter; i++) {
                        curr = curr.getChildren().get(curr.getChildren().size() - 1);
                    }
                    curr.getParent().getChildren().add(new TreeItem<>(a));

                } else {
                    // make a new level
                    counter += 1;
                }
                lineCounter += 1;
                a = buff.readLine();
            }
            pane.getChildren().add(the_tree); */

    private void loadTableOfContents(String filename) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filename));
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
        this.treeView = new TreeView<>(root);
        this.treeRoot = root;
        this.pane.getChildren().add(this.treeView);
        br.close();
    }
    private int checker(String line){
        String[] lineSplit = line.split("\t");
        return lineSplit.length;
    }

    private void storeTableOfContents(String filename) throws IOException {

    }



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

    private int countDepth(String line) {
        int count = 0;
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == DEPTH_DELIMITER) count++;
            else break;
        }
        return count;
    }

    public void addTreeViewDoubleClickHandler(EventHandler<MouseEvent> handler) {
        treeView.addEventHandler(MouseEvent.MOUSE_CLICKED, handler);
    }

}
