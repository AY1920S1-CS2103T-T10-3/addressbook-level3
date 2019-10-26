package seedu.elisa.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.elisa.commons.core.LogsCenter;
import seedu.elisa.commons.exceptions.DataConversionException;
import seedu.elisa.model.ItemStorage;
import seedu.elisa.model.ReadOnlyUserPrefs;
import seedu.elisa.model.UserPrefs;

/**
 * Manages storage of AddressBook data in local storage.
 */
public class StorageManager implements Storage {

    private static final Logger logger = LogsCenter.getLogger(StorageManager.class);
    private ItemListStorage itemListStorage;
    private UserPrefsStorage userPrefsStorage;


    public StorageManager(ItemListStorage itemListStorage, UserPrefsStorage userPrefsStorage) {
        super();
        this.itemListStorage = itemListStorage;
        this.userPrefsStorage = userPrefsStorage;
    }

    // ================ UserPrefs methods ==============================

    @Override
    public Path getUserPrefsFilePath() {
        return userPrefsStorage.getUserPrefsFilePath();
    }

    @Override
    public Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException {
        return userPrefsStorage.readUserPrefs();
    }

    @Override
    public void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException {
        userPrefsStorage.saveUserPrefs(userPrefs);
    }


    // ================ AddressBook methods ==============================

    @Override
    public Path getItemListFilePath() {
        return itemListStorage.getItemListFilePath();
    }


    @Override
    public void saveItemStorage(ItemStorage itemStorage) throws IOException {
        saveItemStorage(itemStorage, itemListStorage.getItemListFilePath());
    }

    @Override
    public void saveItemStorage(ItemStorage itemStorage, Path filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        itemListStorage.saveItemStorage(itemStorage, filePath);
    }

    public ItemStorage toModelType() throws IOException, DataConversionException {
        return itemListStorage.toModelType();
    }

}
