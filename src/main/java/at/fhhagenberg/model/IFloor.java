package at.fhhagenberg.model;

public interface IFloor {

    boolean isDownButton();
    boolean isUpButton();

    IFloor setDownButton(boolean active);
    IFloor setUpButton(boolean active);
}
