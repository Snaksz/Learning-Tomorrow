package LearningTomorrowViewer;

import LearningTomorrowEditor.table_of_contents.ContentTOCNode;
import LearningTomorrowEditor.table_of_contents.TOCNode;
// Merged
import LearningTomorrowViewer.courseContentPanes.*;
import javafx.event.EventType;
// Merged
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
// Merged
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
// Merged
import javafx.scene.input.MouseEvent;
import java.beans.EventHandler;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.Callable;

public class LTViewer {
    public File filepath;
    public Stage stage;
    public Pane content_holder; // Displays the nodes
    public ArrayList<AllContent> content; // Holds the content that holds one node each
    private int fontSize;
    private String fontName;
    private AllContent currentContent;

    /**
     * LTViewer Constructor
     * @param stage The window object
     */
    public LTViewer(Stage stage, File file) {
        this.stage = stage;
        // set the stage
        this.content_holder = new Pane();
        // holds all content panes
        this.filepath = file;
        this.content = new ArrayList<AllContent>(); // Will store the actual NotesPane objects etc.
        // Allows the changing of what is displayed
        InitUI();
    }
    public void InitUI(){
        this.stage.setTitle("Learning Tomorrow - " + this.filepath.getName()); // Should add the course title too!
        // Prep course here (in this space)
        // call function with string path as parameter
        // loads all the files for the course!

        // Loop through all files under that folder / path
        File folder = this.filepath;
        TableOfContents table = null;
        // Take all the files out of the filepath specified
        File[] allFiles = folder.listFiles();
        for (File file : allFiles) {
            String tempFile = "Saved" + File.separator + "Viewer" + File.separator + this.filepath.getName() + File.separator + file.getName();
            loadFile(tempFile); // Try to load and process the given file
            // otherwise move on to next file
            if (tempFile.endsWith(".vwr")){
                table = new TableOfContents(new Pane(), tempFile);
            }
        }
        // Display introfile here

        // SplitPane to hold table of contents on left and content on right
        ScrollPane toolbarPane = new ScrollPane();
        ToolbarPane tools = new ToolbarPane();
        tools.setComboBoxChangeListener((observable, oldValue, newValue) -> {
            String fontName = tools.getComboBoxSelection();
            int fontSize = tools.getTextFieldInput();
            currentContent.changeFont(tools.fontChanger, fontName, fontSize);
        });
        tools.setTextFieldHandler((event) -> {
            String fontName = tools.getComboBoxSelection();
            int fontSize = tools.getTextFieldInput();
            currentContent.changeFont(tools.fontChanger, fontName, fontSize);
        });


        tools.setColorComboBoxChangeListener((observable, oldValue, newValue) -> {
            String ColorModeName = tools.getColorComboBoxSelection();
            // Find the colormode this corresponds to
            for (ColorMode x: tools.colorChanger.all_modes){
                if (Objects.equals(x.getModeName(), ColorModeName)){
                    currentContent.changeColor(tools.colorChanger, x);
                }
            }
            // use those as params to change the color of the given thing.
        });


        // EDIT
        // "How to use javafx." ChatGPT, 5 Dec. version, OpenAI, 5 Dec. 2023,
        // chat.openai.com/chat.
        // Add some content to the zoomPane (for demonstration purposes)
        Button sampleButton = new Button("Zoomable Button");
        this.content_holder.getChildren().add(sampleButton);

        // Attach key event handlers directly to the Pane
        this.content_holder.setOnKeyPressed(this::handleKeyPress);

        // Ensure the Pane can receive key events
        this.content_holder.setFocusTraversable(true);
        // EDIT


        toolbarPane.setContent(tools.getToolbarPane());
        SplitPane p;
        ScrollPane peter_grif = new ScrollPane(this.content_holder);
        if (table != null) {
            SplitPane h = new SplitPane(table.pane, peter_grif);
            h.setDividerPosition(0, 0.15);
            p = new SplitPane(toolbarPane, h);
        } else p = new SplitPane(toolbarPane, this.content_holder);
        // SplitPane h = new SplitPane(new Pane(), peter_grif);
        // h.setDividerPosition(0, 0.2);
        // p = new SplitPane(toolbarPane, h); Will in theory never happen so omit in merged
        // Merged ^ Make both visible fully
        p.setOrientation(javafx.geometry.Orientation.VERTICAL); // In either case
        // Lock divider in place
        double locked = 0.1;
        p.getDividers().get(0).positionProperty().addListener((observable,oldValue,newValue) -> {
            p.setDividerPosition(0, locked);
        });
        // TableOfContents table = new TableOfContents(new Pane(), "Saved" + File.separator + "Viewer" + File.separator + this.filepath.getName() + File.separator);
        // Create toolbar and put it with , then this.content holder
        // That's it don't touch / render anything else each section will be jumped to or rendered seperately.

        // Merged jumper method
        if (table != null){
            // addNodeToDisplay(table.pane);
            // Here content is loaded, so now we need to recurse through the tree and add the double click
            TableOfContents finalTable = table;
            table.addTreeViewDoubleClickHandler(mouseEvent -> {
                if (mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.getClickCount() == 2) {
                    TOCNode tocNode = finalTable.treeView.getSelectionModel().getSelectedItem().getValue();
                    if (tocNode.nodeType == TOCNode.NodeType.CONTENT) {
                        ContentTOCNode contentNode = (ContentTOCNode) tocNode;
                        for (AllContent x: this.content){
                            File file = new File(x.getCourseContent().getFileName());
                            String xFilename = file.getName();
                            if (xFilename.equals(contentNode.getFilename())) {
                                System.out.println("Loading content");
                                updateContentHolder(x);
                            }
                        }
                    }
                }
            });
            // table.treeView.getSelectionModel().getSelectedItem()
            // This method below will set-up the double-click action and section jumps
        }

        currentContent = this.content.get(0); // Set the first pane
        updateContentHolder(this.content.get(0)); // Set the first pane
        Scene scene = new Scene(p);
        this.stage.setMaximized(true);
        this.stage.setResizable(true);
        scene.setFill(Color.TRANSPARENT); // Color of backround(s) is to be changeable as part of settings
        // I.e. make a helper method to change the color of the scene

        // Below is a must, no doubt about it

        this.stage.setScene(scene);
        this.stage.show();
    }

    public void loadFile(String tempFile){
        // Goes into the files for the course, and processes all valid files
        // Will do this in order of the file number! Each file will have a number
        // This will be part of its description
        // This will be the base order of how they will appear in the Files
        if (tempFile.endsWith(".html")) {
            // Create the NotePane!
            Pane pane = new Pane();
            NotesPane notesPane = new NotesPane(pane);
            // load the content from given file
            try {
                notesPane.loadContent(tempFile); // Load the content into the temp_item's pane
                // Check if the audiofile exists
                String simple = tempFile.replace(".html", ".mp3");
                File ghj = new File(simple);
                String name = ghj.getName();
                name = name.substring(name.lastIndexOf(",") + 1);
                name = "Saved" + File.separator + "Viewer" + File.separator + "AudioforCSC108" + File.separator + name;
                File file = new File("Saved" + File.separator + "Viewer" + File.separator + "Audiofor" + "CSC108");
                // Check if this file exists in the Saved/Viewer directory
                File[] audios = file.listFiles();
                int simple_count = 0;
                if (file.exists()) {
                for (File mini: audios){
                    String mini_name = mini.getPath();
                    if (mini_name.equals(name)){
                        // Make a Decorated one
                        WrappedContent notFun = new WrappedContent(notesPane, mini.getAbsolutePath());
                        simple_count += 1;
                        addCourseContent(notFun);
                    }
                }}
                if (simple_count == 0){
                ConcreteContent fun = new ConcreteContent(notesPane);
                addCourseContent(fun);}
            } catch (IOException b) {
                // Do absolutely nothing if there is an error! (To not interrupt run of program)
            }
        }
        else if (tempFile.endsWith(".mp4")) {
            VideoPane oranges = new VideoPane(new Pane());
            try {
                oranges.loadContent(tempFile);
                /** Will be always unwrapped */
                ConcreteContent cheese = new ConcreteContent(oranges);
                addCourseContent(cheese); // Extract Pane and display
            } catch (IOException a) {

            }
        } else if (tempFile.endsWith(".qz")) {
            Pane pane = new Pane();
            QuizPane quizPane = new QuizPane(pane);
            // load the content from given file
            try {
                quizPane.loadContent(tempFile); // Load the content into the temp_item's pane
                pane.getChildren().clear();
                quizPane.setUpPane();
                /** for now assume its always unwrapped */
                ConcreteContent fun = new ConcreteContent(quizPane);
                addCourseContent(fun);
            } catch (IOException b) {
                // Do absolutely nothing if there is an error! (To not interrupt run of program)
            }
        }
        // update it each time something is added
        // Do nothing if we do not deal with the file type
        // Prevent code from crashing
        // Do not add an empty pane! Why would you?
        // That's the point of this non-block
    }
    public void addNodeToDisplay(Node n){
        this.content_holder.getChildren().add(n);
    }

    public Node getNode(AllContent c){return c.getPane();}

    public void addCourseContent(AllContent c){
        this.content.add(c);
    }

    public void updateContentHolder(AllContent c){
        // find current node and make sure it is not playing
        for (AllContent k: this.content){
            if (k.isPlaying()){
                k.stopArticulation();
            }
        } // Stop old articulation
        this.content_holder.getChildren().clear(); // Full reset to then update
        // Add the one content section
        this.content_holder.getChildren().add(c.getPane()); // Add the pane for that course c
        this.currentContent = c;
        c.articulateNotes();

    }

    /** This function takes in a webview object and changes the font
     * size of the given webview to the size a.
     * */
    public void changeFontSize(WebView w){
        w.getEngine().setUserStyleSheetLocation("data:,body { font: " + this.fontSize + "px " + this.fontName + "; }");
    }

    /** This function will take all the Course Content Nodes and will change the font size
     * of those that contain WebViews as attributes.
     * */
    public void changeFontSizeAll(){
        for (AllContent c: this.content){
            if (c.getWebview() != null){
                changeFontSize(c.getWebview());
            }
        }
    }

    /** This function allows to manually change the font size & update it on the display */
    public void changeDefaultFontSizeUp(){
        this.fontSize += 2;
        if (this.fontSize >= 50){
            this.fontSize = 14; // Reset back to default
        }
        changeFontSizeAll(); // Updates it on the display
    }
    /** This function allows to manually change the font size & update it on the display */
    public void changeDefaultFontSizeDown(){
        this.fontSize -= 2;
        if (this.fontSize <= 0){
            this.fontSize = 14; // Reset back to default value
        }
        changeFontSizeAll();
    }

    /** This function allows to manually change the font style & update it on the display */
    public void changeDefaultFontName(String a){
        this.fontName = a;
        changeFontSizeAll();
    }

    public void changeDefaultSizeAndName(int a, String b){
        this.fontSize = a;
        this.fontName = b;
        changeFontSizeAll();
    }
    // All these function are good to go, they just need to be called by a button / however a user will request them.
    // Functions have been tested on their own
    private void handleKeyPress(KeyEvent event) {
        // "How to use javafx." ChatGPT, 5 Dec. version, OpenAI, 5 Dec. 2023,
        // chat.openai.com/chat.
        double SCALE_DELTA = 1.05;
        if (event.isControlDown()) {
            if (event.getCode() == KeyCode.PLUS || event.getCode() == KeyCode.EQUALS) {
                zoom(SCALE_DELTA);
            } else if (event.getCode() == KeyCode.MINUS) {
                zoom(1 / SCALE_DELTA);
            }
        }
    }

    private void zoom(double scaleFactor) {
        // "How to use javafx." ChatGPT, 5 Dec. version, OpenAI, 5 Dec. 2023,
        // chat.openai.com/chat.
        double currentScale = this.content_holder.getTransforms().isEmpty() ? 1 : ((Scale) this.content_holder.getTransforms().get(0)).getX();

        // Ensure that the new scale is within desired bounds (adjust as needed)
        double newScale = Math.min(Math.max(currentScale * scaleFactor, 0.1), 10);

        // Apply the scale transformation to the pane
        this.content_holder.getTransforms().setAll(new Scale(newScale, newScale));
    }

    // Implement those changes as needed.
    // You will only have to display one html file or video at a time
    // It will be jumped to by table of contents
    // File is good to go for the merge! (Changes in Table of Contents Needs to be added**
}
