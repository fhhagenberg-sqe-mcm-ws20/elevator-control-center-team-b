package at.fhhagenberg.model;

public interface IFloor {

    boolean isDownButton();
    boolean isUpButton();
    int getNumber();

    void setDownButton(boolean active);
    void setUpButton(boolean active);

    void update(IFloor floor);
}
