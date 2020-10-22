package at.fhhagenberg.floor;

public interface IFloor {

    boolean isDownButton();
    boolean isUpButton();

    IFloor setDownButton(boolean active);
    IFloor setUpButton(boolean active);
}
