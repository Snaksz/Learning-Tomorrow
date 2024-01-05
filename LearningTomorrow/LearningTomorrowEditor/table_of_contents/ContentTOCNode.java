package LearningTomorrowEditor.table_of_contents;

/**
 * Table of Contents node representing a piece of content (like Note, Video, or Quiz)
 */
public class ContentTOCNode extends TOCNode {

    public enum ContentType {
        NOTE, VIDEO, QUIZ
    }
    private final String contentName;
    private final String filename;
    private final ContentType contentType;

    /**
     * Constructor for a content node
     * @param contentName name of this content to be displayed
     * @param filename name of the file associated with this content
     */
    public ContentTOCNode(String contentName, ContentType contentType, String filename) {
        this.contentName = contentName;
        this.nodeType = NodeType.CONTENT;
        this.filename = filename;
        this.contentType = contentType;
    }

    /**
     * Get the type of content this node represents
     * @return a ContentType object
     */
    public ContentType getContentType() {
        return contentType;
    }

    /**
     * Get the file name of the file associated with this content
     * @return name of the file storing this content
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Return string representation of this TOCNode.
     * This is used to render the cells of the TreeView in TOC
     * @return name of this content
     */
    @Override
    public String toString() {
        return contentName + " : " + contentType;
    }
}
