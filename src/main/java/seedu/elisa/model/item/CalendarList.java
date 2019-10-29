package seedu.elisa.model.item;

import seedu.elisa.commons.core.item.Item;

/**
 * Object class to store all the items that are calendars within the program
 */
public class CalendarList extends VisualizeList {
    public CalendarList() {
        super();
    }

    @Override
    public VisualizeList find(String[] searchString) {
        return null;
    }

    @Override
    public VisualizeList deepCopy() {
        return null;
    }

    @Override
    public VisualizeList sort() {
        return null;
    }

    public boolean belongToList(Item item) {
        return item.hasEvent();
    }

}
