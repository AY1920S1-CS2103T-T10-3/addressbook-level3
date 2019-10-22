package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.DoneCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.JokeCommand;
import seedu.address.logic.commands.PriorityCommand;
import seedu.address.logic.commands.ShowCommand;
import seedu.address.logic.commands.SortCommand;
import seedu.address.logic.commands.UndoCommand;

import seedu.address.logic.parser.exceptions.ParseException;

import seedu.address.model.ElisaCommandHistory;
import seedu.address.model.JokeList;

/**
 * Parses user input.
 */
public class AddressBookParser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT =
            Pattern.compile("(?<commandWord>\\S+)(?<description>[^-]*)(?<flags>.*)");

    private ElisaCommandHistory elisaCommandHistory;
    private JokeList jokeList;

    public AddressBookParser(ElisaCommandHistory elisaCommandHistory, JokeList jokeList) {
        this.elisaCommandHistory = elisaCommandHistory;
        this.jokeList = jokeList;
    }

    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     * @throws ParseException if the user input does not conform the expected format
     */
    public Command parseCommand(String userInput) throws ParseException {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            throw new ParseException(MESSAGE_INVALID_COMMAND_FORMAT);
        }

        final String commandWord = matcher.group("commandWord");
        final String description = matcher.group("description");
        final String flags = " " + matcher.group("flags");

        switch (commandWord) {

        case "task":
            return new AddTaskCommandParser().parse(description, flags);

        case "event":
            return new AddEventCommandParser().parse(description, flags);

        case "reminder":
            return new AddReminderCommandParser().parse(description, flags);

        case EditCommand.COMMAND_WORD:
            return new EditCommandParser().parse(description, flags);

        case DeleteCommand.COMMAND_WORD:
            return new DeleteCommandParser().parse(description, flags);

        case ClearCommand.COMMAND_WORD:
            return new ClearCommand();

        case FindCommand.COMMAND_WORD:
            return new FindCommandParser().parse(description, flags);

        case UndoCommand.COMMAND_WORD:
            return new UndoCommand(elisaCommandHistory);

        case ShowCommand.COMMAND_WORD:
            return new ShowCommandParser().parse(description, flags);

        case SortCommand.COMMAND_WORD:
            return new SortCommand();

        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();

        case PriorityCommand.COMMAND_WORD:
            return new PriorityCommand();

        case DoneCommand.COMMAND_WORD:
            return new DoneCommandParser().parse(description, flags);

        case JokeCommand.COMMAND_WORD:
            return new JokeCommand(jokeList);
        /*
        case ShowCommand.COMMAND_WORD:
            return new ShowCommand(description);
        */
        default:
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }
    }

}
