package at.fhhagenberg.floor;

public interface IFloor {
    boolean downButtonActive();
    boolean upButtonActive();

    IFloor setDownButton(boolean active);
    IFloor setUpButton(boolean active);
}
