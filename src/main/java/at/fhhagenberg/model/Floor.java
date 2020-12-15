package at.fhhagenberg.model;

import javafx.beans.property.SimpleBooleanProperty;
import lombok.Getter;

/**
 * Floor of a building
 * contains information about pressed buttons
 */
public class Floor implements IFloor {

    @Getter
    private int number;
    @Getter
    private final SimpleBooleanProperty downButtonProperty;
    @Getter
    private final SimpleBooleanProperty upButtonProperty;

    public Floor(int number, boolean downButton, boolean upButton) {
        this.number = number;
        this.downButtonProperty = new SimpleBooleanProperty(downButton);
        this.upButtonProperty = new SimpleBooleanProperty(upButton);
    }

    @Override
    public boolean isDownButtonActive() {
        return downButtonProperty.getValue();
    }

    @Override
    public boolean isUpButtonActive() {
        return upButtonProperty.getValue();
    }

    public void setDownButtonActive(boolean active) {
        this.downButtonProperty.set(active);
    }

    public void setUpButtonActive(boolean active) {
        this.upButtonProperty.set(active);
    }

    @Override
    public void update(IFloor floor) {
        this.number = floor.getNumber();
        this.downButtonProperty.set(floor.isDownButtonActive());
        this.upButtonProperty.set(floor.isUpButtonActive());
    }
}
