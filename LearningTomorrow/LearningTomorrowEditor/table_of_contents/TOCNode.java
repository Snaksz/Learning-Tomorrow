package LearningTomorrowEditor.table_of_contents;

/**
 * Table of Contents (TOC) node. Can be either a section or a piece of content
 */
public abstract class TOCNode {
    /**
     * Enum for types of nodes
     */
    public enum NodeType {
        SECTION, CONTENT
    }
    public NodeType nodeType;

    /**
     * Return string representation of this TOCNode.
     * This is used to render the cells of the TreeView in TOC
     * @return string representation of this TOCNode
     */
    public abstract String toString();
}
