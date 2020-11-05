package at.fhhagenberg.model;

public interface IFloor {

    boolean isDownButton();
    boolean isUpButton();

    void setDownButton(boolean active);
    void setUpButton(boolean active);
}
