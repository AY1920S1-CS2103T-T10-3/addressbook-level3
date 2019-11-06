package seedu.elisa.logic.parser;

import static seedu.elisa.logic.parser.CliSyntax.PREFIX_DATETIME;
import static seedu.elisa.logic.parser.CliSyntax.PREFIX_PRIORITY;
import static seedu.elisa.logic.parser.CliSyntax.PREFIX_REMINDER;
import static seedu.elisa.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import seedu.elisa.commons.core.item.*;
import seedu.elisa.commons.core.item.Item.ItemBuilder;
import seedu.elisa.commons.core.item.tag.Tag;
import seedu.elisa.logic.commands.AddCommand;
import seedu.elisa.logic.commands.AddReminderCommand;
import seedu.elisa.logic.parser.exceptions.ParseException;

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


        ItemDescription description = ParserUtil.parseDescription(desc);

        // Reminder must be present.
        if (!arePrefixesPresent(argMultimap, PREFIX_REMINDER)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException("I can't believe you forgot the reminder format again! "
                    + "The format should follow:\n\"reminder DESCRIPTION -r REMINDER [-t TAG]\"");
        }
        Reminder itemReminder = ParserUtil.parseReminder(argMultimap.getValue(PREFIX_REMINDER).get()).get();
        Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));

        Optional<Priority> priority = ParserUtil.parsePriority(argMultimap.getValue(PREFIX_PRIORITY).orElse(null));

        ItemBuilder itemBuilder = new ItemBuilder();
        itemBuilder.setItemDescription(description);
        itemBuilder.setReminder(itemReminder);
        itemBuilder.setTags(tagList);

        if (priority.isPresent()) {
            itemBuilder.setItemPriority(priority.get());
        }

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

