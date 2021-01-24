package at.fhhagenberg.controller;

import at.fhhagenberg.model.Elevator;
import at.fhhagenberg.model.IBuildingElevator;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.property.Property;
import javafx.collections.ListChangeListener;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

import static at.fhhagenberg.controller.GuiConstants.*;

public class ElevatorController {

    public Label elevatorId;
    public Label nearestFloor;
    public AnchorPane root;
    public StackPane infoPane;
    public StackPane directionPane;
    public JFXComboBox<Integer> targetField;
    public Label speedField;
    public Label doorStatusField;
    public Label payloadField;
    public FlowPane floorButtonPane;
    private Elevator buildingElevator;
    private int id;

    // Left Menu and ID's of info boxes
    private VBox leftMenu;

    // Info ID's
    private String payloadInfoId;

    // List to save warnings and errors of elevator
    private final ArrayList<String> warningList = new ArrayList<>();
    private final ArrayList<String> errorList = new ArrayList<>();

    /**
     * Method to initialize the model
     *
     * @param buildingElevator the ElevatorObject
     * @param id               the ID of the elevator
     * @param floorCount       the floor count of the building
     * @param leftMenu         the left menu to display info messages
     */
    public void initModel(IBuildingElevator buildingElevator, int id, int floorCount, VBox leftMenu) {
        if (this.buildingElevator != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.buildingElevator = (Elevator) buildingElevator;
        this.leftMenu = leftMenu;
        this.id = id;
        setupIds();

        setupGuiProperties(floorCount);
    }

    private void setupGuiProperties(int floorCount) {
        // Set id component
        elevatorId.setText("ID: " + id);

        // Set nearest floor component
        String floorFormat = "Current floor: %d";
        nearestFloor.textProperty().bind(this.buildingElevator.nearestFloorProperty.asString(floorFormat));

        // Bind Combobox to pressed buttons
        targetField.setItems(this.buildingElevator.getFloorServices());
        targetField.valueProperty().bindBidirectional((Property) this.buildingElevator.getFloorTargetProperty());

        // Set direction component
        setDirectionArrow(this.buildingElevator.getDirection());
        this.buildingElevator.directionProperty.addListener((observableValue, oldValue, newValue)
                -> setDirectionArrow(newValue.intValue()));

        // Set door state component
        doorStatusField.textProperty().bind(this.buildingElevator.doorStateProperty);

        // Set speed component
        String speedFormat = "%d m/s";
        speedField.textProperty().bind(this.buildingElevator.speedProperty.asString(speedFormat));

        // Set payload component
        String weightFormat = "%d kg";
        payloadField.textProperty().bind(this.buildingElevator.payloadProperty.asString(weightFormat));

        checkPayload(this.buildingElevator.getPayloadProperty().getValue());
        this.buildingElevator.payloadProperty.addListener((observableValue, oldValue, newValue) -> {
            if (oldValue.intValue() > buildingElevator.getCapacity()) {
                checkPayload(newValue.intValue());
            }
        });

        setupButtons(floorCount);
    }

    private void setupButtons(int floorCount) {
        // Create all floor buttons
        for (int i = 0; i < floorCount; i++) {
            if (buildingElevator.servesFloor(i)) {
                this.floorButtonPane.getChildren().add(createButton(i));
            }
        }

        // Bind floor buttons to the pressed buttons list of the elevator
        buildingElevator.getFloorButtons().addListener((ListChangeListener<Integer>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    // Get only the first integer of the change as this one is the one we need
                    JFXButton currentFloorBtn = (JFXButton) floorButtonPane.lookup("#" + FLOOR_BTN_ID_PREFIX + change.getAddedSubList().get(0));
                    currentFloorBtn.getStyleClass().add(CLICKED_STYLE);
                } else if (change.wasRemoved()) {
                    JFXButton currentFloorBtn = (JFXButton) floorButtonPane.lookup("#" + FLOOR_BTN_ID_PREFIX + change.getRemoved().get(0));
                    currentFloorBtn.getStyleClass().remove(CLICKED_STYLE);
                    if (targetField.getItems().isEmpty()) {
                        targetField.getSelectionModel().clearSelection();
                    }
                }
            }
        });
    }

    /**
     * Method to setup ID's for labels so that we can operate on them later on.
     */
    private void setupIds() {
        payloadInfoId = PAYLOAD_ID_PREFIX + id;
    }

    /**
     * Method to change if we control an elevator manually.
     *
     * @param isAutoMode receives if auto mode is turned on
     */
    public void setAutoMode(boolean isAutoMode) {
        targetField.setDisable(isAutoMode);
        floorButtonPane.setDisable(isAutoMode);
    }

    /**
     * Method to set the direction arrow
     *
     * @param direction current direction of the elevator
     */
    private void setDirectionArrow(int direction) {
        directionPane.getChildren().clear();
        FontAwesomeIconView directionIcon;
        switch (direction) {
            case 0:
                directionIcon = new FontAwesomeIconView(FontAwesomeIcon.ARROW_UP);
                break;
            case 1:
                directionIcon = new FontAwesomeIconView(FontAwesomeIcon.ARROW_DOWN);
                break;
            default:
                directionIcon = new FontAwesomeIconView(FontAwesomeIcon.ARROWS_V);
                break;
        }
        directionPane.getChildren().add(directionIcon);
    }

    /**
     * Method to create an info
     *
     * @param infoType type of the info
     * @param infoId   id of the info
     * @param infoText text of the info
     */
    public void createInfo(String infoType, String infoId, String infoText) {
        if (infoType.equals(WARNING) && !warningList.contains(infoId)) {
            addInfoToLeftMenu(infoId, WARNING, infoText, WARNING_STYLE, FontAwesomeIcon.EXCLAMATION_TRIANGLE);
            updateTopIcon(WARNING);
            warningList.add(infoId);
        } else if (infoType.equals(ERROR) && !errorList.contains(infoId)) {
            addInfoToLeftMenu(infoId, ERROR, infoText, ERROR_STYLE, FontAwesomeIcon.EXCLAMATION);
            updateTopIcon(ERROR);
            errorList.add(infoId);
        }
    }

    /**
     * Method to add an info label to the left Menu
     *
     * @param labelId  ID of the label
     * @param infoType type of the Info
     * @param text     of the label
     * @param style    of the label according to type of info
     * @param iconType type of the Icon
     */
    public void addInfoToLeftMenu(String labelId, String infoType, String text, String style, FontAwesomeIcon iconType) {
        Label infoLabel = (Label) leftMenu.lookup("#" + labelId);
        FontAwesomeIconView labelIcon = new FontAwesomeIconView(iconType);
        if (infoLabel == null) {
            Label newInfoLabel = new Label(text);
            newInfoLabel.setId(labelId);
            newInfoLabel.getStyleClass().addAll(LEFT_BAR_LABEL_STYLE, style);
            newInfoLabel.setGraphic(labelIcon);

            VBox infoBox;
            if (infoType.equals(WARNING)) {
                infoBox = (VBox) leftMenu.lookup("#" + WARNING_BOX_ID);
            } else {
                infoBox = (VBox) leftMenu.lookup("#" + ERROR_BOX_ID);
                infoBox = (VBox) leftMenu.lookup("#" + ERROR_BOX_ID);
            }
            infoBox.getChildren().add(newInfoLabel);
        } else {
            infoLabel.getStyleClass().remove(WARNING_STYLE);
            infoLabel.getStyleClass().remove(ERROR_STYLE);
            infoLabel.getStyleClass().add(style);
            infoLabel.setText(text);
            infoLabel.setGraphic(labelIcon);
        }
    }

    /**
     * Method to update top icon of elevator view
     *
     * @param warningType type of the warning
     */
    public void updateTopIcon(String warningType) {
        if (warningType.equals(ERROR)) {
            infoPane.getChildren().clear();
            infoPane.getChildren().add(createIcon(ERROR_STYLE, FontAwesomeIcon.EXCLAMATION));
        } else {
            infoPane.getChildren().clear();
            infoPane.getChildren().add(createIcon(WARNING_STYLE, FontAwesomeIcon.EXCLAMATION_TRIANGLE));
        }
    }

    /**
     * Method to add icon to top
     *
     * @param style    class of the label
     * @param iconType of the icon
     */
    public FontAwesomeIconView createIcon(String style, FontAwesomeIcon iconType) {
        FontAwesomeIconView icon = new FontAwesomeIconView(iconType);
        icon.getStyleClass().add(style);
        return icon;
    }

    /**
     * Method to delete a warning or error from the list and the GUI
     *
     * @param infoId ID of the info to be removed from lists and GUI
     */
    public void deleteInfo(String infoId) {
        warningList.remove(infoId);
        errorList.remove(infoId);
        if (!errorList.isEmpty()) {
            updateTopIcon(ERROR);
        } else if (!warningList.isEmpty()) {
            updateTopIcon(WARNING);
        } else {
            infoPane.getChildren().clear();
        }
        VBox parent = (VBox) leftMenu.lookup("#" + infoId).getParent();
        parent.getChildren().remove(leftMenu.lookup("#" + infoId));
    }

    /**
     * Method to check if payload is in capacity
     *
     * @param payload of the current elevator
     */
    private void checkPayload(int payload) {
        int payloadCheck = this.buildingElevator.getCapacity() - payload;
        if (payloadCheck < -150) {
            errorList.remove(payloadInfoId);
            createInfo(WARNING, payloadInfoId, String.format("Elevator %d: Warning payload on a high level.", id));
        } else if (payloadCheck <= 0) {
            warningList.remove(payloadInfoId);
            createInfo(ERROR, payloadInfoId, String.format("Elevator %d: Error payload too high.", id));
        } else {
            if (!warningList.isEmpty() || !errorList.isEmpty()) {
                deleteInfo(payloadInfoId);
            }
        }
    }

    /**
     * Method to create a floor button
     */
    public JFXButton createButton(int floorNumber) {
        JFXButton floorButton = new JFXButton();
        floorButton.setId(FLOOR_BTN_ID_PREFIX + floorNumber);
        floorButton.setText(String.valueOf(floorNumber));
        floorButton.getStyleClass().add(ROUND_BUTTON_STYLE);
        if (buildingElevator.getFloorButtons().contains(floorNumber)) {
            floorButton.getStyleClass().add(CLICKED_STYLE);
        }
        floorButton.setOnMouseClicked(mouseEvent -> buildingElevator.addPressedFloorButton(floorNumber));
        return floorButton;
    }
}