package seedu.address.model.item;

import java.util.Comparator;

import seedu.address.commons.core.item.Item;
import seedu.address.commons.core.item.Task;

/**
 * Object class to store all the items that are task within the program
 */
public class TaskList extends VisualizeList {
    public static final Comparator<Item> COMPARATOR = (item1, item2) -> {
        Task task1 = item1.getTask().get();
        Task task2 = item2.getTask().get();
        if (task1.isComplete() && !task2.isComplete()) {
            return 1;
        } else if (!task1.isComplete() && task2.isComplete()) {
            return -1;
        } else {
            return item1.getPriority().compareTo(item2.getPriority());
        }
    };

    public TaskList() {
        super();
    }

    /**
     * Sort the items in the task list. The items are first sorted by whether they are
     * done or not and then by their priority.
     * @return an VisualizeList of all the items sorted
     */
    public VisualizeList sort() {
        TaskList tl = new TaskList();
        for (Item item: list) {
            tl.add(item);
        }

        tl.list.sort(COMPARATOR);

        return tl;
    }

    /**
     * Finds a substring within the description of an item.
     * @param searchString a string to be search for within the description of an item
     * @return a new TaskList containing only the items that have the search string in their description
     */
    public VisualizeList find(String[] searchString) {
        TaskList tl = new TaskList();
        return find(searchString, tl);
    }

    @Override
    public VisualizeList deepCopy() {
        TaskList tl = new TaskList();
        return super.deepCopy(tl);
    }
}
