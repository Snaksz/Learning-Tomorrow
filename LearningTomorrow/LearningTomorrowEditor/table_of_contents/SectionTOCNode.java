package LearningTomorrowEditor.table_of_contents;

/**
 * Table of Content node representing a section (almost like a folder)
 */
public class SectionTOCNode extends TOCNode {
    private final String sectionName;

    /**
     * Constructor for a content node
     * @param sectionName name of the section
     */
    public SectionTOCNode(String sectionName) {
        this.sectionName = sectionName;
        this.nodeType = NodeType.SECTION;
    }

    /**
     * Return string representation of this TOCNode.
     * This is used to render the cells of the TreeView in TOC
     * @return name of this section
     */
    @Override
    public String toString() {
        return sectionName;
    }
}
