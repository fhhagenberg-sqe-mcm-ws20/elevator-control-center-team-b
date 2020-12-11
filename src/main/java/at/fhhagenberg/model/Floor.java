package at.fhhagenberg.model;

import lombok.Getter;

/**
 *  Floor of a building
 *  contains information about pressed buttons
 */
public class Floor implements IFloor {
    @Getter
    private int number;
    @Getter
    private boolean downButton;
    @Getter
    private boolean upButton;

    public Floor(int number, boolean downButton, boolean upButton) {
        this.number = number;
        this.downButton = downButton;
        this.upButton = upButton;
    }

    @Override
    public void setDownButton(boolean active) {
        this.downButton = active;
    }

    @Override
    public void setUpButton(boolean active) {
        this.upButton = active;
    }

    @Override
    public void update(IFloor floor) {
        this.number = floor.getNumber();
        this.downButton = floor.isDownButton();
        this.upButton = floor.isUpButton();
    }
}
