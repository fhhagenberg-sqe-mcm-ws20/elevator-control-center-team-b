package at.fhhagenberg.model;

import lombok.Getter;

/**
 *  Floor of a building
 *  contains information about pressed buttons
 */
public class Floor implements IFloor {

    @Getter
    private boolean downButton;
    @Getter
    private boolean upButton;

    public Floor(boolean downButton, boolean upButton) {
        this.downButton = downButton;
        this.upButton = upButton;
    }

    @Override
    public IFloor setDownButton(boolean active) {
        this.downButton = active;
        return this;
    }

    @Override
    public IFloor setUpButton(boolean active) {
        this.upButton = active;
        return this;
    }
}
