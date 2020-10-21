package at.fhhagenberg.floor;

/**
 * Mock class for floor
 */
public class MockFloor implements IFloor {
    private boolean downButton = false;
    private boolean upButton = false;

    public MockFloor(boolean downButton, boolean upButton) {
        this.downButton = downButton;
        this.upButton = upButton;
    }

    @Override
    public boolean downButtonActive() {
        return downButton;
    }

    @Override
    public boolean upButtonActive() {
        return upButton;
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
