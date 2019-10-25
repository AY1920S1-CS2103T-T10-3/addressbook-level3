package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.core.item.Item;
import seedu.address.commons.core.item.Event;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.ItemModel;
import seedu.address.model.RescheduleTask;

/**
 * Adds an Event to the item model.
 */
public class AddEventCommand extends AddCommand {

    public static final String SHOW_EVENT_VIEW = "E";
    public static final String MESSAGE_SUCCESS = "New Event added: %1$s";

    public AddEventCommand(Item item) {
        super(item);
    }

    @Override
    public CommandResult execute(ItemModel model) throws CommandException {
        requireNonNull(model);

        // Check if item already exists, else, add it to the model.
        if (model.hasItem(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_ITEM);
        } else {
            model.addItem(toAdd);
        }

        if (toAdd.hasAutoReschedule()) {
            Event event = toAdd.getEvent().get();
            RescheduleTask task = new RescheduleTask(toAdd, event.getPeriod(), model);
            autoRescheduleManager.add(task);
        }

        // Notify Ui to change the view the that of the newly added item.
        try {
            model.setVisualList(SHOW_EVENT_VIEW);
        } catch (Exception e) {
            // should not enter here as itemType is definitely valid.
        }

        //return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
    }

    @Override
    public String getCommandWord() {
        return COMMAND_WORD;
    }
}
