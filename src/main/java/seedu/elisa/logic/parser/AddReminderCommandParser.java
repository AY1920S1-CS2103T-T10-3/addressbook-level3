package seedu.elisa.logic.parser;

import static seedu.elisa.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.elisa.logic.parser.CliSyntax.PREFIX_DATETIME;
import static seedu.elisa.logic.parser.CliSyntax.PREFIX_PRIORITY;
import static seedu.elisa.logic.parser.CliSyntax.PREFIX_REMINDER;
import static seedu.elisa.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Set;
import java.util.stream.Stream;

import seedu.elisa.commons.core.item.Item;
import seedu.elisa.commons.core.item.Item.ItemBuilder;
import seedu.elisa.commons.core.item.ItemDescription;
import seedu.elisa.commons.core.item.Reminder;
import seedu.elisa.logic.commands.AddCommand;
import seedu.elisa.logic.commands.AddReminderCommand;
import seedu.elisa.logic.parser.exceptions.ParseException;
import seedu.elisa.model.tag.Tag;

/**
 * Parses input arguments and creates a new AddCommand object
 */
public class AddReminderCommandParser implements Parser<AddCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddCommand
     * and returns an AddCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddCommand parse(String desc, String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_DATETIME, PREFIX_REMINDER, PREFIX_PRIORITY, PREFIX_TAG);

        // Reminder must be present.
        if (!arePrefixesPresent(argMultimap, PREFIX_REMINDER)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }

        ItemDescription description = ParserUtil.parseDescription(desc);
        // Reminder must be present.
        Reminder itemReminder = ParserUtil.parseReminder(argMultimap.getValue(PREFIX_REMINDER).get()).get();
        Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));

        ItemBuilder itemBuilder = new ItemBuilder();
        itemBuilder.setItemDescription(description);
        itemBuilder.setReminder(itemReminder);
        itemBuilder.setTags(tagList);

        Item newItem;
        try {
            newItem = itemBuilder.build();
        } catch (IllegalArgumentException e) {
            throw new ParseException(e.getMessage());
        }

        AddCommand addCommand = new AddReminderCommand(newItem);
        return addCommand;
    }


    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}

