package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.nio.file.Path;
import java.util.PriorityQueue;
import java.util.ArrayList;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.item.Item;
import seedu.address.commons.core.item.Task;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.exceptions.IllegalListException;
import seedu.address.model.item.ActiveRemindersList;
import seedu.address.model.item.EventList;
import seedu.address.model.item.ReminderList;
import seedu.address.model.item.TaskList;
import seedu.address.model.item.VisualizeList;

/**
 * Represents the model for ELISA
 */
public class ItemModelManager implements ItemModel {
    private TaskList taskList;
    private EventList eventList;
    private ReminderList reminderList;
    // The list to be used for visualizing in the Ui
    private VisualizeList visualList;
    private final UserPrefs userPrefs;
    private ItemStorage itemStorage;
    private final ElisaStateHistory elisaStateHistory;
    private boolean priorityMode = false;
    private PriorityQueue<Item> sortedTask = null;

    //Bryan Reminder
    //These three lists must be synchronized
    private ReminderList pastReminders;
    private ActiveRemindersList activeReminders;
    private ArrayList<Item> futureReminders;

    public ItemModelManager(ItemStorage itemStorage, ReadOnlyUserPrefs userPrefs, ElisaStateHistory elisaStateHistory) {

    //Bryan Reminder
    //These three lists must be synchronized
    private ReminderList pastReminders;
    private ActiveRemindersList activeReminders;
    private ArrayList<Item> futureReminders;

    public ItemModelManager(ItemStorage itemStorage, ReadOnlyUserPrefs userPrefs) {
        this.taskList = new TaskList();
        this.eventList = new EventList();
        this.reminderList = new ReminderList();
        this.visualList = taskList;
        this.itemStorage = itemStorage;
        this.userPrefs = new UserPrefs(userPrefs);
        this.elisaStateHistory = elisaStateHistory;

        //Bryan Reminder
        pastReminders = new ReminderList();

        activeReminders = new ActiveRemindersList(new ReminderList());
        /*
        activeReminders = new ListPropertyBase<Item>(new ReminderList()) {
            @Override
            public Object getBean() {
                return null;
            }

            @Override
            public String getName() {
                return null;
            }

            public synchronized Item popReminder() {
                if(!isEmpty()) {
                    return remove(0);
                } else {
                    //Should have this throw an exception
                    return null;
                }
            }

            public synchronized void addReminders(Collection<Item> reminders) {
                for (Item item:reminders) {
                    add(0, item);
                }
            }
        };
        */

        futureReminders = new ArrayList<Item>();

        //Bryan Reminder
        pastReminders = new ReminderList();

        activeReminders = new ActiveRemindersList(new ReminderList());
        /*
        activeReminders = new ListPropertyBase<Item>(new ReminderList()) {
            @Override
            public Object getBean() {
                return null;
            }

            @Override
            public String getName() {
                return null;
            }

            public synchronized Item popReminder() {
                if(!isEmpty()) {
                    return remove(0);
                } else {
                    //Should have this throw an exception
                    return null;
                }
            }

            public synchronized void addReminders(Collection<Item> reminders) {
                for (Item item:reminders) {
                    add(0, item);
                }
            }
        };
        */

        futureReminders = new ArrayList<Item>();

        for (int i = 0; i < itemStorage.size(); i++) {
            addToSeparateList(itemStorage.get(i));
        }

        elisaStateHistory.pushCommand(new ElisaStateManager(getItemStorage(), getVisualList()).deepCopy());
    }

    /* Bryan Reminder
     *
     * Referenced: https://docs.oracle.com/javafx/2/binding/jfxpub-binding.htm
     * for property naming conventions.
     *
     */

    //Function to get property
    @Override
    public ActiveRemindersList getActiveReminderListProperty() {
        return activeReminders;
    }

    //Function get property's value
    public final ObservableList<Item> getActiveReminderList() {
        return activeReminders.get();
    }
    //Function to edit property //which should trigger a change event
    public final void addReminderToActive(Item item) {
        activeReminders.add(item);
    }

    @Override
    public final ArrayList<Item> getFutureRemindersList() {
        return futureReminders;
    }

    /* Bryan Reminder
     *
     * Referenced: https://docs.oracle.com/javafx/2/binding/jfxpub-binding.htm
     * for property naming conventions.
     *
     */

    //Function to get property
    @Override
    public ActiveRemindersList getActiveReminderListProperty() {
        return activeReminders;
    }

    //Function get property's value
    public final ObservableList<Item> getActiveReminderList() {
        return activeReminders.get();
    }
    //Function to edit property //which should trigger a change event
    public final void addReminderToActive(Item item) {
        activeReminders.add(item);
    }

    @Override
    public final ArrayList<Item> getFutureRemindersList() {
        return futureReminders;
    }

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getItemStorageFilePath() {
        return userPrefs.getItemStorageFilePath();
    }

    @Override
    public void setItemStorageFilePath(Path itemStorageFilePath) {
        requireNonNull(itemStorageFilePath);
        userPrefs.setItemStorageFilePath(itemStorageFilePath);
    }

    //=========== AddressBook ================================================================================

    @Override
    public void setItemStorage(ItemStorage itemStorage) {
        this.itemStorage = itemStorage;
    }

    @Override
    public ItemStorage getItemStorage() {
        return itemStorage;
    }

    /**
     * Adds an item to the respective list. All items will be added to the central list.
     * It will also be added to the respective list depending on whether it is a task, event or a reminder.
     * @param item the item to be added to the program
     */
    public void addItem (Item item) {
        visualList.add(item);
        //TODO: Shouldnt addToSeparateList be successful before we store the item?
        itemStorage.add(item);
        addToSeparateList(item);
    }

    /**
     * Adds an item to a specific list
     * @param item the item to be added to the list
     * @param il the list the item is to be added to
     */
    public void addItem (Item item, VisualizeList il) {
        il.add(item);
    }

    /**
     * Helper function to add an item to it's respective list
     * @param item the item to be added into the lists
     */
    public void addToSeparateList(Item item) {
        if (item.hasTask()) {
            taskList.add(item);
        }

        if (item.hasEvent()) {
            eventList.add(item);
        }

        if (item.hasReminder()) {
            reminderList.add(item);
            futureReminders.add(item);
        }
    }

    @Override
    public void setState(ElisaState state) {
        setItemStorage(state.getStorage().deepCopy());
        setVisualizeList(state.getVisualizeList().deepCopy());
    }

    @Override
    public void setToCurrState() {
        setState(elisaStateHistory.peekCommand().deepCopy());
    }

    @Override
    public ElisaState getState() {
        return new ElisaStateManager(getItemStorage().deepCopy(), getVisualList()).deepCopy();
    }

    @Override
    public ElisaStateHistory getElisaStateHistory() {
        return elisaStateHistory;
    }

    @Override
    public void updateModelLists() {
        ItemStorage itemStorage = getItemStorage().deepCopy();
        emptyLists();
        for (int i = 0; i < itemStorage.size(); i++) {
            addToSeparateList(itemStorage.get(i));
        }
    }

    @Override
    public void updateState() {
        elisaStateHistory.pushCommand(getState().deepCopy());
    }

    /**
     * Remove an item from the current list.
     * @param index the item to be removed from the current list
     * @return the item that was removed
     */
    public Item removeItem(int index) {
        Item item = visualList.remove(index);
        if (visualList instanceof TaskList) {
            taskList.remove(item);
        } else if (visualList instanceof EventList) {
            eventList.remove(item);
        } else if (visualList instanceof ReminderList) {
            reminderList.remove(item);
        } else {
            // never reached here as there are only three variants for the visualList
        }

        return item;
    }

    /**
     * Deletes an item from the program.
     * @param index the index of the item to be deleted.
     * @return the item that was deleted from the program
     */
    public Item deleteItem(int index) {
        Item item = visualList.remove(index);
        itemStorage.remove(item);
        taskList.remove(item);
        eventList.remove(item);
        reminderList.remove(item);
        if (priorityMode) {
            getNextTask();
        }
        return item;
    }

    public VisualizeList getVisualList() {
        return this.visualList;
    }

    /**
     * Set a new item list to be the visualization list.
     * @param listString the string representation of the list to be visualized
     */
    public void setVisualList(String listString) throws IllegalValueException {
        switch(listString) {
        case "T":
            if (priorityMode) {
                setVisualList(getNextTask());
                break;
            }
            setVisualList(taskList);
            break;
        case "E":
            setVisualList(eventList);
            break;
        case "R":
            setVisualList(reminderList);
            break;
        default:
            throw new IllegalValueException(String.format("%s is no a valid list", listString));
        }
    }

    private void setVisualList(VisualizeList il) {
        this.visualList = il;
    }

    /**
     * Replaces one item with another item.
     * @param item the item to be replace
     * @param newItem the item that will replace the previous item
     */
    public void replaceItem(Item item, Item newItem) {
        int index = visualList.indexOf(item);
        visualList.setItem(index, newItem);

        if ((index = itemStorage.indexOf(item)) >= 0) {
            itemStorage.setItem(index, newItem);
        }

        if ((index = taskList.indexOf(item)) >= 0) {
            taskList.setItem(index, newItem);
        }

        if ((index = eventList.indexOf(item)) >= 0) {
            eventList.setItem(index, newItem);
        }

        if ((index = reminderList.indexOf(item)) >= 0) {
            reminderList.setItem(index, newItem);
        }

        if (priorityMode) {
            sortedTask.remove(item);
            sortedTask.offer(newItem);
            visualList = getNextTask();
        }
    }

    /**
     * Find an item based on its description.
     * @param searchStrings the string to search for within the description
     * @return the item list containing all the items that contain the search string
     */
    public VisualizeList findItem(String[] searchStrings) {
        this.visualList = visualList.find(searchStrings);
        return this.visualList;
    }

    @Override
    public void setVisualizeList(VisualizeList list) {
        this.visualList = list;
    }

    /**
     * Clears the storage for the current ELISA run.
     */
    public void clear() {
        setItemStorage(new ItemStorage());
        this.taskList = new TaskList();
        this.eventList = new EventList();
        this.reminderList = new ReminderList();
        this.visualList = taskList;
    }

    /**
     * Clears the 3 lists for re-populating
     * */
    public void emptyLists() {
        this.taskList.clear();
        this.eventList.clear();
        this.reminderList.clear();
    }

    /**
     * Sort the current visual list.
     */
    public void sort() {
        this.visualList = visualList.sort();
    }

    /**
     * Enable and disable the priority mode
     * @return a boolean value. If true, means priority mode is on, else returns false.
     * @throws IllegalListException if the visualList is not a task list.
     */
    public boolean togglePriorityMode() throws IllegalListException {
        if (!(visualList instanceof TaskList)) {
            throw new IllegalListException();
        }

        priorityMode = !priorityMode;
        if (!priorityMode) {
            sortedTask = null;
            this.visualList = taskList;
        } else {
            sortedTask = new PriorityQueue<>((item1, item2) -> {
                Task task1 = item1.getTask().get();
                Task task2 = item2.getTask().get();
                return task1.getPriority().compareTo(task2.getPriority());
            });
            for (int i = 0; i < taskList.size(); i++) {
                Item item = taskList.get(i);
                if (!item.getTask().get().isComplete()) {
                    sortedTask.add(item);
                }
            }
            this.visualList = getNextTask();
        }
        return priorityMode;
    }

    private VisualizeList getNextTask() {
        TaskList result = new TaskList();
        result.add(sortedTask.peek());
        return result;
    }

    /**
     * Mark an item with a task as done.
     * @param index the index of the item to be marked as done.
     * @return the item that is marked as done.
     * @throws IllegalListException if the operation is not done on a task list.
     */
    public Item markComplete(int index) throws IllegalListException {
        Item item;
        if (!(visualList instanceof TaskList)) {
            throw new IllegalListException();
        } else {
            item = visualList.get(index);
            Task task = item.getTask().get();
            Task newTask = task.markComplete();
            Item newItem = item.changeTask(newTask);
            replaceItem(item, newItem);
        }

        if (priorityMode) {
            sortedTask.poll();
            this.visualList = getNextTask();
        }

        return item;
    }

}
