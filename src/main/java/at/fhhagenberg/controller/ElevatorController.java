package at.fhhagenberg.controller;

import at.fhhagenberg.model.Elevator;
import at.fhhagenberg.model.IBuildingElevator;
import com.jfoenix.controls.JFXComboBox;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.property.Property;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class ElevatorController {

    public Label elevator_id;
    public Label nearest_floor;
    public AnchorPane root;
    public StackPane info_pane;
    public StackPane direction_pane;
    public JFXComboBox<Integer> target_field;
    public Label speed_field;
    public Label door_status_field;
    public Label payload_field;
    private Elevator buildingElevator;
    private int ID;

    // Left Menu and ID's of info boxes
    private VBox leftMenu;
    private final String WARNING_BOX_ID = "#warning_box";
    private final String ERROR_BOX_ID = "#error_box";


    // Warning and Error type
    private final String WARNING = "WARNING";
    private final String ERROR = "ERROR";

    // Style classes
    private final String WARNING_STYLE_CLASS = "warning";
    private final String ERROR_STYLE_CLASS = "danger";
    private final String LEFT_BAR_LABEL_STYLE_CLASS = "left-bar-info";


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
        this.ID = id;
        setupIds();

        // Set id component
        elevator_id.setText("ID: " + id);

        // Set nearest floor component
        String floorFormat = "Current floor: %d";
        nearest_floor.textProperty().bind(this.buildingElevator.nearestFloorProperty.asString(floorFormat));
        // TODO: This line is only for testing, remove it!
        nearest_floor.setOnMouseClicked(mouseEvent -> ((Elevator) buildingElevator).setWeight(350));

        // Set target component
        ObservableList<Integer> elevatorFloors = FXCollections.observableArrayList();
        for (int i = 0; i < floorCount; i++) {
            elevatorFloors.add(i);
        }
        target_field.getItems().addAll(elevatorFloors);
        target_field.valueProperty().bindBidirectional((Property) this.buildingElevator.getFloorTargetProperty());

        // Set direction component
        setDirectionArrow(this.buildingElevator.getDirection());
        this.buildingElevator.directionProperty.addListener((observableValue, oldValue, newValue)
                -> setDirectionArrow(newValue.intValue()));

        // Set door state component
        door_status_field.textProperty().bind(this.buildingElevator.doorStateProperty);
        // TODO: This line is only for testing, remove it!
        door_status_field.setOnMouseClicked(mouseEvent -> ((Elevator) buildingElevator).setWeight(250));


        // Set speed component
        String speedFormat = "%d m/s";
        speed_field.textProperty().bind(this.buildingElevator.speedProperty.asString(speedFormat));
        // TODO: This line is only for testing, remove it!
        speed_field.setOnMouseClicked(mouseEvent -> createInfo(WARNING, "test" + ID, "Elevator " + ID + ": Hey"));


        // Set payload component
        String weightFormat = "%d kg";
        payload_field.textProperty().bind(this.buildingElevator.payloadProperty.asString(weightFormat));
        // TODO: This line is only for testing, remove it!
        payload_field.setOnMouseClicked(mouseEvent -> ((Elevator) buildingElevator).setWeight(450));

        checkPayload(this.buildingElevator.getPayloadProperty().getValue());
        this.buildingElevator.payloadProperty.addListener((observableValue, oldValue, newValue) -> {
            if (oldValue.intValue() > buildingElevator.getCapacity()) {
                checkPayload(newValue.intValue());
            }
        });
    }

    /**
     * Method to setup ID's for labels so that we can operate on them later on.
     */
    private void setupIds() {
        payloadInfoId = "PAYLOAD" + ID;
    }

    /**
     * Method to change if we control an elevator manually.
     *
     * @param isAutoMode receives if auto mode is turned on
     */
    public void setAutoMode(boolean isAutoMode) {
        target_field.setDisable(isAutoMode);
    }

    /**
     * Method to set the direction arrow
     *
     * @param direction current direction of the elevator
     */
    private void setDirectionArrow(int direction) {
        direction_pane.getChildren().clear();
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
        direction_pane.getChildren().add(directionIcon);
    }

    /**
     * Method to create an info
     *
     * @param infoType type of the info
     * @param infoId   id of the info
     * @param infoText text of theinfo
     */
    private void createInfo(String infoType, String infoId, String infoText) {
        if (infoType.equals(WARNING)) {
            if (!warningList.contains(infoId)) {
                addInfoToLeftMenu(infoId, WARNING, infoText, WARNING_STYLE_CLASS, FontAwesomeIcon.EXCLAMATION_TRIANGLE);
                updateTopIcon(WARNING);
                if (!warningList.contains(infoId)) {
                    warningList.add(infoId);
                }
            }
        } else if (infoType.equals(ERROR)) {
            if (!errorList.contains(infoId)) {
                addInfoToLeftMenu(infoId, ERROR, infoText, ERROR_STYLE_CLASS, FontAwesomeIcon.EXCLAMATION);
                updateTopIcon(ERROR);
                if (!errorList.contains(infoId)) {
                    errorList.add(infoId);
                }
            }
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
            newInfoLabel.getStyleClass().addAll(LEFT_BAR_LABEL_STYLE_CLASS,
                    style);
            newInfoLabel.setGraphic(labelIcon);
            VBox infoBox;
            if (infoType.equals(WARNING)) {
                infoBox = (VBox) leftMenu.lookup(WARNING_BOX_ID);
            } else {
                infoBox = (VBox) leftMenu.lookup(ERROR_BOX_ID);
            }
            infoBox.getChildren().add(newInfoLabel);
        } else {
            if (!infoLabel.getStyleClass().contains(style)) {
                VBox infoBox;
                if (infoType.equals(ERROR)) {
                    infoBox = (VBox) leftMenu.lookup(ERROR_BOX_ID);
                    infoBox.getChildren().add(infoLabel);
                    infoLabel.getStyleClass().remove(WARNING_STYLE_CLASS);
                } else {
                    infoBox = (VBox) leftMenu.lookup(WARNING_BOX_ID);
                    infoBox.getChildren().add(infoLabel);
                    infoLabel.getStyleClass().remove(ERROR_STYLE_CLASS);
                }
                infoLabel.getStyleClass().add(style);
                infoLabel.setText(text);
                infoLabel.setGraphic(labelIcon);
            }
        }
    }

    /**
     * Method to update top icon of elevator view
     *
     * @param warningType type of the warning
     */
    public void updateTopIcon(String warningType) {
        if (warningType.equals(ERROR)) {
            info_pane.getChildren().clear();
            info_pane.getChildren().add(createIcon(ERROR_STYLE_CLASS, FontAwesomeIcon.EXCLAMATION));
        } else {
            if (errorList.isEmpty()) {
                info_pane.getChildren().clear();
                info_pane.getChildren().add(createIcon(WARNING_STYLE_CLASS, FontAwesomeIcon.EXCLAMATION_TRIANGLE));
            }
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
    public void deleteWarningOrError(String infoId) {
        warningList.remove(infoId);
        errorList.remove(infoId);
        if (!errorList.isEmpty()) {
            updateTopIcon(ERROR);
        } else if (!warningList.isEmpty()) {
            updateTopIcon(WARNING);
        } else {
            info_pane.getChildren().clear();
        }
        VBox parent = (VBox) leftMenu.lookup("#" + infoId).getParent();
        parent.getChildren().remove(leftMenu.lookup("#" + infoId));
    }

    /**
     * Method to check if payload is in capacity
     *
     * @param payload of the current elevatpr
     */
    private void checkPayload(int payload) {
        int payloadCheck = this.buildingElevator.getCapacity() - payload;
        if (payloadCheck < 0 && payloadCheck > -150) {
            errorList.remove(payloadInfoId);
            createInfo(WARNING, payloadInfoId, String.format("Elevator %d: Warning payload on a high level.", ID));
        } else if (payloadCheck <= -150) {
            warningList.remove(payloadInfoId);
            createInfo(ERROR, payloadInfoId, String.format("Elevator %d: Error payload too high.", ID));
        } else {
            if (!warningList.isEmpty() || !errorList.isEmpty()) {
                deleteWarningOrError(payloadInfoId);
            }
        }
    }
}
