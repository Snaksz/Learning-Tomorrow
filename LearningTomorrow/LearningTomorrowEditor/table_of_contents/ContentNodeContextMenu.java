package LearningTomorrowEditor.table_of_contents;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

/**
 * Class for the context menu that pops up
 * when the user right-clicks on the TreeView cell
 */
public class ContentNodeContextMenu extends ContextMenu {
    private final MenuItem deleteContentMenuItem;

    /**
     * Constructor for the context menu
     */
    public ContentNodeContextMenu() {
        super();
        deleteContentMenuItem = new MenuItem("Delete Content");

        this.getItems().add(deleteContentMenuItem);
    }

    /**
     * Set an event handler for delete content
     * @param handler EventHandler<ActionEvent> instance
     */
    public void setDeleteContentHandler(EventHandler<ActionEvent> handler) {
        deleteContentMenuItem.setOnAction(handler);
    }
}
