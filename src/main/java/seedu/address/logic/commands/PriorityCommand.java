package seedu.address.logic.commands;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.ItemModel;
import seedu.address.model.exceptions.IllegalListException;

/**
 * Toggle the state of ELISA between priority and non-priority mode.
 */
public class PriorityCommand extends UndoableCommand {
    public static final String COMMAND_WORD = "priority";

    private static final String PRIORITY_MODE_ON = "Priority mode activated, just manage this one task, that'll do.";
    private static final String PRIORITY_MODE_OFF = "Priority mode deactivated! Not so stressed anymore, are you?";
    private static final String PRIORITY_MODE_ERROR = "Priority mode can only be activated on task pane";

    @Override
    public CommandResult execute(ItemModel model) {
        try {
            boolean status = model.togglePriorityMode();
            return new CommandResult((status ? PRIORITY_MODE_ON : PRIORITY_MODE_OFF));
        } catch (IllegalListException e) {
            return new CommandResult(PRIORITY_MODE_ERROR);
        }
    }

    @Override
    public void reverse(ItemModel model) throws CommandException {
        try {
            model.togglePriorityMode();
        } catch (IllegalListException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getCommandWord() {
        return COMMAND_WORD;
    }
}
