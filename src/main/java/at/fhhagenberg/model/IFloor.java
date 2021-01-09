package at.fhhagenberg.model;

import javafx.beans.property.SimpleBooleanProperty;

public interface IFloor {

    int getNumber();

    SimpleBooleanProperty getDownButtonProperty();

    SimpleBooleanProperty getUpButtonProperty();

    boolean isDownButtonActive();

    boolean isUpButtonActive();

    void setDownButtonActive(boolean active);

    void setUpButtonActive(boolean active);

    void update(IFloor floor);
}
