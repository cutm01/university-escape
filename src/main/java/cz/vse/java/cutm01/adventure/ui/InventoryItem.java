package cz.vse.java.cutm01.adventure.ui;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class InventoryItem {
    private final String itemName ;
    private final BooleanProperty selected = new SimpleBooleanProperty();

    public InventoryItem(String itemName) {
        this.itemName = itemName ;
    }

    public String getName() {
        return itemName ;
    }

    public final boolean isSelected() {
        return getSelectedProperty().get();
    }

    public final void setSelected(boolean selected) {
        getSelectedProperty().set(selected);
    }

    public BooleanProperty getSelectedProperty() {
        return selected ;
    }

    @Override
    public String toString() {
        return getName();
    }
}