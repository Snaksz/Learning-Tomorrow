package LearningTomorrowEditor.table_of_contents;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;


public class SectionNodeContextMenu extends ContextMenu {
    private final MenuItem addSectionMenuItem;
    private final MenuItem addNoteMenuItem;
    private final MenuItem addVideoMenuItem;
    private final MenuItem addQuizMenuItem;
    private final MenuItem deleteSectionMenuItem;
    public SectionNodeContextMenu() {
        super();
        Menu newMenu = new Menu("New");
        addSectionMenuItem = new MenuItem("Section");
        addNoteMenuItem = new MenuItem("Note");
        addVideoMenuItem = new MenuItem("Video");
        addQuizMenuItem = new MenuItem("Quiz");
        newMenu.getItems().addAll(addSectionMenuItem, addNoteMenuItem, addVideoMenuItem, addQuizMenuItem);

        deleteSectionMenuItem = new MenuItem("Delete Section");

        this.getItems().addAll(newMenu, deleteSectionMenuItem);
    }

    public void setAddSectionHandler(EventHandler<ActionEvent> handler) {
        addSectionMenuItem.setOnAction(handler);
    }

    public void setAddNoteHandler(EventHandler<ActionEvent> handler) {
        addNoteMenuItem.setOnAction(handler);
    }

    public void setAddVideoHandler(EventHandler<ActionEvent> handler) {
        addVideoMenuItem.setOnAction(handler);
    }

    public void setAddQuizHandler(EventHandler<ActionEvent> handler) {
        addQuizMenuItem.setOnAction(handler);
    }

    public void setDeleteSectionHandler(EventHandler<ActionEvent> handler) {
        deleteSectionMenuItem.setOnAction(handler);
    }
}
