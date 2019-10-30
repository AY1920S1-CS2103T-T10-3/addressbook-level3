package seedu.elisa.model.item;

import java.util.Collection;

import javafx.beans.property.ListPropertyBase;
import seedu.elisa.commons.core.item.Item;

/**
 * TODO: Make this javadoc prettier.
 * An ActiveReminderList that extends from ListPropertyBase in order to be observable.
 */
public class ActiveRemindersList extends ListPropertyBase<Item> {

    public ActiveRemindersList (ReminderList reminderList) {
        super(reminderList);
    }

    @Override
    public Object getBean() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    /**
     * Adds a reminder to ActiveRemindersList
     *
     * @param reminders Collection of reminders to be added to the list.
     */
    public synchronized void addReminders(Collection<Item> reminders) {
        System.out.println("Starting to add.");
        for (Item item:reminders) {
            add(0, item);
        }
        System.out.println("Finished adding.");
        return;
    }
}
