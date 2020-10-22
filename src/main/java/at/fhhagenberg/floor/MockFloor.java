package at.fhhagenberg.floor;

import lombok.Getter;

/**
 * Mock class for floor
 */
public class MockFloor implements IFloor {

    @Getter
    private boolean downButton;
    @Getter
    private boolean upButton;

    public MockFloor(boolean downButton, boolean upButton) {
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
