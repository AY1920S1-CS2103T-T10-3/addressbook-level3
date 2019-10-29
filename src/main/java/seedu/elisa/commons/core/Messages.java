package seedu.elisa.commons.core;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "I have no clue what you just said";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format!";
    public static final String MESSAGE_INVALID_PERSON_DISPLAYED_INDEX = "The person index provided is invalid";
    public static final String MESSAGE_INVALID_ITEM_DISPLAYED_INDEX = "The item index provided is invalid";
    public static final String MESSAGE_ITEM_LISTED_OVERVIEW = "Here you go, %1$d items listed!";
    public static final String MESSAGE_NOTHING_TO_UNDO = "Nothing to undo, buddy."
            + " Maybe try actually doing something first";
    public static final String MESSAGE_INVALID_FAST_REMINDER_FORMAT = "Don't you remember?"
            + "It should be in the format of \"(Positive Integer).(Time unit).later\""
            + "Eg. \"3.hour.later\" or \"10.min.later\"";

}
