package seedu.elisa.logic;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javafx.beans.property.ListPropertyBase;
import seedu.elisa.commons.core.GuiSettings;
import seedu.elisa.commons.core.LogsCenter;
import seedu.elisa.commons.core.item.Item;
import seedu.elisa.logic.commands.Command;
import seedu.elisa.logic.commands.CommandResult;
import seedu.elisa.logic.commands.exceptions.CommandException;
import seedu.elisa.logic.parser.ElisaParser;
import seedu.elisa.logic.parser.exceptions.ParseException;
import seedu.elisa.model.AutoRescheduleManager;
import seedu.elisa.model.ItemModel;
import seedu.elisa.model.ItemStorage;
import seedu.elisa.model.item.VisualizeList;
import seedu.elisa.storage.Storage;

/**
 * The main LogicManager of the app.
 */
public class LogicManager implements Logic {
    public static final String FILE_OPS_ERROR_MESSAGE = "Could not save data to file: ";
    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final ItemModel model;
    private final Storage storage;
    private final ElisaParser addressBookParser;
    private final ScheduledThreadPoolExecutor checker;
    private final AutoRescheduleManager autoRescheduleManager;

    public LogicManager(ItemModel model, Storage storage) {
        this.storage = storage;
        this.model = model;
        addressBookParser = new ElisaParser(model.getElisaCommandHistory());
        autoRescheduleManager = AutoRescheduleManager.getInstance();
        autoRescheduleManager.initStorageEvents(model.getEventList(), model);

        Runnable checkTask = new CheckTaskRunnable(model);
        checker = new ScheduledThreadPoolExecutor(1);
        checker.scheduleAtFixedRate(checkTask, 0, 5, TimeUnit.SECONDS);
    }

    public final ListPropertyBase<Item> getActiveRemindersListProperty() {
        return model.getActiveReminderListProperty();
    }

    /**
     * Shutdown threads for Reminders, PriorityMode and AutoRescheduleManager
     */
    public final void shutdown() {
        checker.shutdown();
        autoRescheduleManager.shutdown();
        model.offPriorityMode();
    }

    @Override
    public CommandResult execute(String commandText) throws CommandException, ParseException {

        //Logging
        logger.info("----------------[USER COMMAND][" + commandText + "]");

        CommandResult commandResult;
        //Parse user input from String to a Command
        Command command = addressBookParser.parseCommand(commandText);
        commandResult = command.execute(model);

        try {
            storage.saveItemStorage(model.getItemStorage());
        } catch (IOException ioe) {
            throw new CommandException(FILE_OPS_ERROR_MESSAGE + ioe, ioe);
        }

        model.updateCommandHistory(command);

        return commandResult;
    }

    @Override
    public ItemStorage getItemStorage() {
        return model.getItemStorage();
    }

    @Override
    public VisualizeList getVisualList() {
        return model.getVisualList();
    }

    @Override
    public ItemModel getModel() {
        return model;
    }

    @Override
    public Path getAddressBookFilePath() {
        return model.getItemStorageFilePath();
    }

    @Override
    public GuiSettings getGuiSettings() {
        return model.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        model.setGuiSettings(guiSettings);
    }
}
