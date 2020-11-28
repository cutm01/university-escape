package cz.vse.java.cutm01.adventure.ui;

import cz.vse.java.cutm01.adventure.gamelogic.ItemName;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.ListCell;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class InventoryItem extends CheckBoxListCell<String> {
    private final Map<String, Image> inventoryItemsImages = loadItemImages();
    private final String itemNameEnumValue; //Enum value for game item (e.q. "BOTTLE")
    private final String itemName; //used as game command execution argument, defined in ItemName Enum
    private final String itemNameToDisplay; //is shown in game GUI, defined in ItemNameToDisplay Enum
    private final ImageView displayImage = new ImageView();
    private final BooleanProperty checked = new SimpleBooleanProperty();

    public InventoryItem(String itemNameEnumValue) {
        this.itemNameEnumValue = itemNameEnumValue;
        this.itemName = ItemName.getItemName(itemNameEnumValue);
        this.itemNameToDisplay = ItemNameToDisplay.getItemNameToDisplay(itemNameEnumValue);
        setChecked(false);
    }

    @Override
    public void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            displayImage.setImage(null);
            setText(null);
            setGraphic(null);
        }
        else {
            displayImage.setImage(inventoryItemsImages.get(itemNameEnumValue.toLowerCase()));
            setText(itemNameToDisplay);
            setGraphic(displayImage);
        }
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemNameToDisplay() {
        return itemNameToDisplay;
    }

    public final boolean getChecked() {
        return checkedProperty().get();
    }

    public final void setChecked(boolean checked) {
        checkedProperty().set(checked);
    }

    public BooleanProperty checkedProperty() {
        return checked;
    }

    @Override
    public String toString() {
        return getItemNameToDisplay();
    }

    /**
     * Method to load images for all game items
     * @return Map where key is name of game item and value is corresponding image
     */
    private Map<String, Image> loadItemImages() {
        Map<String, Image> loadedImages = new HashMap<>();

        Object[] itemNames = ItemName.values();
        for(Object o : itemNames) {
            String itemName = o.toString().toLowerCase();

            InputStream imageStream = getClass().getClassLoader().getResourceAsStream(itemName + ".png");
            Image itemImage = new Image(imageStream);

            loadedImages.put(o.toString().toLowerCase(), itemImage);
        }

        return loadedImages;
    }
}