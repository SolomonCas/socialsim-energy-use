package com.socialsim.controller.controls;

import com.socialsim.controller.graphics.GraphicsController;
import com.socialsim.controller.graphics.amenity.mapper.*;
import com.socialsim.model.core.agent.AgentMovement;
import com.socialsim.model.core.environment.Environment;
import com.socialsim.model.core.environment.patchobject.passable.elevator.Elevator;
import com.socialsim.model.simulator.Simulator;

import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchfield.*;
import com.socialsim.model.simulator.SimulationTime;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javafx.scene.input.ScrollEvent;
import javafx.scene.input.MouseEvent;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static com.socialsim.controller.Main.simulator;

public class ScreenController extends Controller {


    // VARIABLES
    private final double CANVAS_SCALE = 0.5;

    // Canvas
    @FXML private Canvas backgroundCanvas;
    @FXML private Canvas foregroundCanvas;
    @FXML private Canvas markingsCanvas;

    // StackPane
    @FXML private StackPane stackPane;

    // Text
    @FXML private Text elapsedTimeText;

    // WATTAGE TEXT
    @FXML private Text totalWattageCountText;

    // TextField
    @FXML private TextField nonverbalMean;
    @FXML private TextField nonverbalStdDev;
    @FXML private TextField cooperativeMean;
    @FXML private TextField cooperativeStdDev;
    @FXML private TextField exchangeMean;
    @FXML private TextField exchangeStdDev;
    @FXML private TextField fieldOfView;


    //WATER DISPENSER
    @FXML private TextField waterDispenserWattage;
    @FXML private TextField waterDispenserWattageInUse;
    @FXML private TextField waterDispenserWattageActive;
    //FRIDGE
    @FXML private TextField fridgeWattage;
    @FXML private TextField fridgeWattageInUse;
    @FXML private TextField fridgeWattageActive;
    //AIRCON
    @FXML private TextField airconWattage;
    @FXML private TextField airconWattageActive;
    //LIGHT
    @FXML private TextField lightWattage;
    //MONITOR
    @FXML private TextField monitorWattage;
    //COFFEE MAKER
    @FXML private TextField coffeeMakerWattageLow;
    @FXML private TextField coffeeMakerWattageHigh;

    //Agent Chances
    @FXML private TextField greenChance;
    @FXML private TextField nonGreenChance;
    @FXML private TextField neutralChance;

    @FXML private TextField studentNum;
    @FXML private TextField facultyNum;
    @FXML private TextField teamNum;


    // Label: Current Agent Count
    @FXML private Label currentDirectorCount;
    @FXML private Label currentFacultyCount;
    @FXML private Label currentStudentCount;
    @FXML private Label currentMaintenanceCount;
    @FXML private Label currentGuardCount;

    // Current Interaction With Appliance Count
    @FXML private Label currentAirconCount;
    @FXML private Label currentLightCount;
    @FXML private Label currentFridgeInteractionCount;
    @FXML private Label currentWaterDispenserInteractionCount;
    @FXML private Label currentMonitorCount;
    @FXML private Label currentCoffeeMakerCount;

    //AIRCON and LIGHT SWITCH INTERACTIONS
    @FXML private Label currentAirconTurnOnCount;
    @FXML private Label currentAirconTurnOffCount;

    @FXML private Label currentLightTurnOnCount;
    @FXML private Label currentLightTurnOffCount;

    // Label: Current Interaction Count
    @FXML private Label currentNonverbalCount;
    @FXML private Label currentCooperativeCount;
    @FXML private Label currentExchangeCount;

    // Label: Average Interaction Duration
    @FXML private Label averageNonverbalDuration;
    @FXML private Label averageCooperativeDuration;
    @FXML private Label averageExchangeDuration;


    // Label: Current Team Count


    // Label: Current Director to ____ Interaction Count
    @FXML private Label currentDirectorFacultyCount;
    @FXML private Label currentDirectorStudentCount;
    @FXML private Label currentDirectorMaintenanceCount;
    @FXML private Label currentDirectorGuardCount;


    // Label: Current Faculty to ____ Interaction Count
    @FXML private Label currentFacultyFacultyCount;
    @FXML private Label currentFacultyStudentCount;
    @FXML private Label currentFacultyMaintenanceCount;
    @FXML private Label currentFacultyGuardCount;


    // Label: Current Student to ____ Interaction Count
    @FXML private Label currentStudentStudentCount;
    @FXML private Label currentStudentMaintenanceCount;
    @FXML private Label currentStudentGuardCount;

    // Label: Current Maintenance to ____ Interaction Count
    @FXML private Label currentMaintenanceMaintenanceCount;
    @FXML private Label currentMaintenanceGuardCount;

    // Label: Current Guard to Guard Interaction Count
    @FXML private Label currentGuardGuardCount;



    // Buttons: Parameters
    @FXML private Button configureIOSButton;    // Configure IOS Levels (Under Parameters)
    @FXML private Button editInteractionButton; // Edit Interaction Type Chances (Under Parameters)
    @FXML private Button resetToDefaultButton;


    // Buttons: Simulate
    @FXML private Button resetButton;           // Reset Simulation
    @FXML private ToggleButton playButton;
    @FXML private Slider speedSlider;
    @FXML private Button exportToCSVButton;
    @FXML private Button exportHeatMapButton;

    private double zoomFactor = 1.0;
    private double zoomDelta = 0.1;

    private double mouseAnchorX;
    private double mouseAnchorY;

    private double translateAnchorX;
    private double translateAnchorY;

    @FXML
    private TitledPane energyConsumptionPane;

    @FXML
    private TitledPane socialInteractionPane;

    @FXML
    private VBox vbox;

    // CONSTRUCTOR
    public ScreenController() {
    }


    // METHODS: SET PARAMETERS

    public void configureParameters(Environment environment) {


        // Interactions
        environment.setNonverbalMean(Integer.parseInt(nonverbalMean.getText()));
        environment.setNonverbalStdDev(Integer.parseInt(nonverbalStdDev.getText()));
        environment.setCooperativeMean(Integer.parseInt(cooperativeMean.getText()));
        environment.setCooperativeStdDev(Integer.parseInt(cooperativeStdDev.getText()));
        environment.setExchangeMean(Integer.parseInt(exchangeMean.getText()));
        environment.setExchangeStdDev(Integer.parseInt(exchangeStdDev.getText()));
        environment.setFieldOfView(Integer.parseInt(fieldOfView.getText()));

        //Wattage

        totalWattageCountText.setText("Total Watts: " + String.format("%.03f",Simulator.totalWattageCount) + " W");
        //WATER DISPENSER
        simulator.setWaterDispenserWattage(Float.parseFloat(waterDispenserWattage.getText()));
        simulator.setWaterDispenserWattageInUse(Float.parseFloat(waterDispenserWattageInUse.getText()));
        simulator.setWaterDispenserWattageActive(Float.parseFloat(waterDispenserWattageActive.getText()));
        //FRIDGE
        simulator.setFridgeWattage(Float.parseFloat(fridgeWattage.getText()));
        simulator.setFridgeWattageInUse(Float.parseFloat(fridgeWattageInUse.getText()));
        simulator.setFridgeWattageActive(Float.parseFloat(fridgeWattageActive.getText()));
        //AIRCON
        simulator.setAirconWattage(Float.parseFloat(airconWattage.getText()));
        simulator.setAirconWattageActive(Float.parseFloat(airconWattageActive.getText()));
        //LIGHT
        simulator.setLightWattage(Float.parseFloat(lightWattage.getText()));
        //MONITOR
        simulator.setMonitorWattage(Float.parseFloat(monitorWattage.getText()));
        //COFFEE MAKER
        simulator.setCoffeeMakerWattageLow(Float.parseFloat(coffeeMakerWattageLow.getText()));
        simulator.setCoffeeMakerWattageHigh(Float.parseFloat(coffeeMakerWattageHigh.getText()));


        // Current Agent Count Per Type
        currentDirectorCount.setText(String.valueOf(Simulator.currentDirectorCount));
        currentFacultyCount.setText(String.valueOf(Simulator.currentFacultyCount));
        currentStudentCount.setText(String.valueOf(Simulator.currentStudentCount));
        currentMaintenanceCount.setText(String.valueOf(Simulator.currentMaintenanceCount));
        currentGuardCount.setText(String.valueOf(Simulator.currentGuardCount));

        currentNonverbalCount.setText(String.valueOf(Simulator.currentNonverbalCount));
        currentCooperativeCount.setText(String.valueOf(Simulator.currentCooperativeCount));
        currentExchangeCount.setText(String.valueOf(Simulator.currentExchangeCount));
        averageNonverbalDuration.setText(String.valueOf(Simulator.averageNonverbalDuration));
        averageCooperativeDuration.setText(String.valueOf(Simulator.averageCooperativeDuration));
        averageExchangeDuration.setText(String.valueOf(Simulator.averageExchangeDuration));

        // Current Appliance Interaction Count
        currentAirconCount.setText(String.valueOf(Simulator.currentAirconCount));
        currentLightCount.setText(String.valueOf(Simulator.currentLightCount));
        currentMonitorCount.setText(String.valueOf(Simulator.currentMonitorCount));

        currentCoffeeMakerCount.setText(String.valueOf(Simulator.currentCoffeeMakerCount));

        currentAirconTurnOnCount.setText(String.valueOf(Simulator.currentAirconTurnOnCount));
        currentAirconTurnOffCount.setText(String.valueOf(Simulator.currentAirconTurnOffCount));

        currentLightTurnOnCount.setText(String.valueOf(Simulator.currentLightTurnOnCount));
        currentLightTurnOffCount.setText(String.valueOf(Simulator.currentLightTurnOffCount));

        currentFridgeInteractionCount.setText(String.valueOf(Simulator.currentFridgeInteractionCount));
        currentWaterDispenserInteractionCount.setText(String.valueOf(Simulator.currentWaterDispenserInteractionCount));



        currentDirectorFacultyCount.setText(String.valueOf(Simulator.currentDirectorFacultyCount));
        currentDirectorStudentCount.setText(String.valueOf(Simulator.currentDirectorStudentCount));
        currentDirectorMaintenanceCount.setText(String.valueOf(Simulator.currentDirectorMaintenanceCount));
        currentDirectorGuardCount.setText(String.valueOf(Simulator.currentDirectorGuardCount));



        currentFacultyFacultyCount.setText(String.valueOf(Simulator.currentFacultyFacultyCount));
        currentFacultyStudentCount.setText(String.valueOf(Simulator.currentFacultyStudentCount));
        currentFacultyMaintenanceCount.setText(String.valueOf(Simulator.currentFacultyMaintenanceCount));
        currentFacultyGuardCount.setText(String.valueOf(Simulator.currentFacultyGuardCount));



        currentStudentStudentCount.setText(String.valueOf(Simulator.currentStudentStudentCount));
        currentStudentMaintenanceCount.setText(String.valueOf(Simulator.currentStudentMaintenanceCount));
        currentStudentGuardCount.setText(String.valueOf(Simulator.currentStudentGuardCount));



        currentMaintenanceMaintenanceCount.setText(String.valueOf(Simulator.currentMaintenanceMaintenanceCount));
        currentMaintenanceGuardCount.setText(String.valueOf(Simulator.currentMaintenanceGuardCount));



        currentGuardGuardCount.setText(String.valueOf(Simulator.currentGuardGuardCount));

        //AGENT CHANCES
        Simulator.setGreenChance(Double.parseDouble(greenChance.getText()));
        Simulator.setNonGreenChance(Double.parseDouble(nonGreenChance.getText()));
        Simulator.setNeutralChance(Double.parseDouble(neutralChance.getText()));

        Simulator.setStudentNum(Integer.parseInt(studentNum.getText()));
        Simulator.setFacultyNum(Integer.parseInt(facultyNum.getText()));
        Simulator.setTeamNum(Integer.parseInt(teamNum.getText()));

    }

    public boolean validateParameters() {
        boolean validParameters = Integer.parseInt(nonverbalMean.getText()) >= 0 && Integer.parseInt(nonverbalMean.getText()) >= 0
                && Integer.parseInt(cooperativeMean.getText()) >= 0 && Integer.parseInt(cooperativeStdDev.getText()) >= 0
                && Integer.parseInt(exchangeMean.getText()) >= 0 && Integer.parseInt(exchangeStdDev.getText()) >= 0
                && Integer.parseInt(fieldOfView.getText()) >= 0 && Integer.parseInt(fieldOfView.getText()) <= 360
                && Integer.parseInt(studentNum.getText()) >= 0 && Integer.parseInt(facultyNum.getText()) >= 0
                && Integer.parseInt(teamNum.getText()) >= 0 && ((Double.parseDouble(greenChance.getText()) +
                Double.parseDouble(nonGreenChance.getText()) + Double.parseDouble(neutralChance.getText())) == 1.00);
        if (!validParameters) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "", ButtonType.OK);
            Label label = new Label("Failed to initialize. Please make sure all values are greater than 0, field of view is not greater than 360 degrees, and total energy profile chances equate to 1.");
            label.setWrapText(true);
            alert.getDialogPane().setContent(label);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                alert.close();
            }
        }

        return validParameters;
    }


    public void disableEdits() {
        nonverbalMean.setDisable(true);
        nonverbalStdDev.setDisable(true);
        cooperativeMean.setDisable(true);
        cooperativeStdDev.setDisable(true);
        exchangeMean.setDisable(true);
        exchangeStdDev.setDisable(true);
        fieldOfView.setDisable(true);
        resetToDefaultButton.setDisable(true);
        configureIOSButton.setDisable(true);
        editInteractionButton.setDisable(true);
        //WATTAGE
        airconWattage.setDisable(true);
        airconWattageActive.setDisable(true);

        lightWattage.setDisable(true);

        monitorWattage.setDisable(true);

        coffeeMakerWattageLow.setDisable(true);
        coffeeMakerWattageHigh.setDisable(true);

        fridgeWattage.setDisable(true);
        fridgeWattageActive.setDisable(true);
        fridgeWattageInUse.setDisable(true);

        waterDispenserWattage.setDisable(true);
        waterDispenserWattageActive.setDisable(true);
        waterDispenserWattageInUse.setDisable(true);
        //AGENT CHANCES
        greenChance.setDisable(true);
        nonGreenChance.setDisable(true);
        neutralChance.setDisable(true);

        studentNum.setDisable(true);
        facultyNum.setDisable(true);
        teamNum.setDisable(true);
    }

    public void resetToDefault() {
        nonverbalMean.setText(Integer.toString(AgentMovement.defaultNonverbalMean));
        nonverbalStdDev.setText(Integer.toString(AgentMovement.defaultNonverbalStdDev));
        cooperativeMean.setText(Integer.toString(AgentMovement.defaultCooperativeMean));
        cooperativeStdDev.setText(Integer.toString(AgentMovement.defaultCooperativeStdDev));
        exchangeMean.setText(Integer.toString(AgentMovement.defaultExchangeMean));
        exchangeStdDev.setText(Integer.toString(AgentMovement.defaultExchangeStdDev));
        fieldOfView.setText(Integer.toString(AgentMovement.defaultFieldOfView));

        waterDispenserWattage.setText(Float.toString(Simulator.getWaterDispenserWattage()));
        waterDispenserWattageInUse.setText(Float.toString(Simulator.getWaterDispenserWattageInUse()));
        waterDispenserWattageActive.setText(Float.toString(Simulator.getWaterDispenserWattageActive()));

        fridgeWattage.setText(Float.toString(Simulator.getFridgeWattage()));
        fridgeWattageInUse.setText(Float.toString(Simulator.getFridgeWattageInUse()));
        fridgeWattageActive.setText(Float.toString(Simulator.getFridgeWattageActive()));

        airconWattage.setText(Float.toString(Simulator.getAirconWattage()));
        airconWattageActive.setText(Float.toString(Simulator.getAirconWattageActive()));

        lightWattage.setText(Float.toString(Simulator.getLightWattage()));

        monitorWattage.setText(Float.toString(Simulator.getMonitorWattage()));

        coffeeMakerWattageLow.setText(Float.toString(Simulator.getCoffeeMakerWattageLow()));
        coffeeMakerWattageHigh.setText(Float.toString(Simulator.getCoffeeMakerWattageHigh()));


        //AGENT CHANCES
        greenChance.setText(Double.toString(Simulator.getGreenChance()));
        nonGreenChance.setText(Double.toString(Simulator.getNonGreenChance()));
        neutralChance.setText(Double.toString(Simulator.getNeutralChance()));

        studentNum.setText(Integer.toString(Simulator.getStudentNum()));
        facultyNum.setText(Integer.toString(Simulator.getFacultyNum()));
        teamNum.setText(Integer.toString(Simulator.getTeamNum()));

    }

    public void openIOSLevels() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/socialsim/view/ConfigureIOS.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Configure IOS Levels");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void openEditInteractions() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/socialsim/view/EditInteractions.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Edit Interaction Type Chances");
            stage.setScene(new Scene(root));
            stage.show();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }


    // METHODS: SIMULATE

    public void initializeEnvironment(Environment environment) {
        GraphicsController.tileSize = backgroundCanvas.getHeight() / simulator.getEnvironment().getRows();
        mapEnvironment();
        simulator.spawnInitialAgents(environment);
        drawInterface();
    }

    public void mapEnvironment() {
        Environment environment = simulator.getEnvironment();

        /* Floors (for each zone inside office) */

        List<Patch>
                // consistent across all layouts
                hallway = new ArrayList<>(), elevatorLobby = new ArrayList<>(),
                maleBathroom = new ArrayList<>(), femaleBathroom = new ArrayList<>(),
                reception = new ArrayList<>(), breakerRoom = new ArrayList<>(), pantry = new ArrayList<>(),

                // small to big changes in other layouts
                directorRoom = new ArrayList<>(), directorBathroom = new ArrayList<>(),
                conferenceRoom = new ArrayList<>(), meetingRoom = new ArrayList<>(),
                controlCenter = new ArrayList<>(), controlCenterCCTV = new ArrayList<>(),
                dataCenter = new ArrayList<>(), mesa = new ArrayList<>(),
                SR1 = new ArrayList<>(), SR2 = new ArrayList<>(),  SR3 = new ArrayList<>(), SR4 = new ArrayList<>(),
                LS1 = new ArrayList<>(), LS2 = new ArrayList<>(),  LS3 = new ArrayList<>(), LS4 = new ArrayList<>(),
                researchCenter = new ArrayList<>(), facultyRoom = new ArrayList<>(),
                humanExpRoom = new ArrayList<>(), dataCollectionRoom = new ArrayList<>(),
                storageRoom = new ArrayList<>(), clinic = new ArrayList<>();

        Object[][] floorRanges =  {
                // consistent across all layouts
                {elevatorLobby, 14, 64, 177, 192},
                {hallway, 18, 21, 1, 21}, {hallway, 22, 59, 17, 21}, {hallway, 51, 59, 147, 152},
                {hallway, 60, 66, 1, 168}, {hallway, 67, 79, 16, 37}, {hallway, 67, 79, 57, 83},
                {hallway, 67, 79, 103, 142}, {hallway, 67, 79, 162, 168},
                {hallway, 80, 86, 16, 128}, {hallway, 80, 87, 129, 132}, {hallway, 80, 88, 133, 142},
                {hallway, 81, 88, 143, 161}, {hallway, 80, 88, 162, 185}, {hallway, 89, 110, 170, 185},
                {maleBathroom, 4, 13, 186, 202}, {maleBathroom, 14, 18, 191, 202},
                {femaleBathroom, 65, 74, 186, 202}, {femaleBathroom, 60, 64, 191, 202},
                {reception, 56, 75, 169, 183}, {breakerRoom, 18, 21, 22, 30}, {pantry, 111, 124, 135, 185},
                
                // small to big changes in other layouts
                {directorRoom, 92, 113, 186, 202}, {directorBathroom, 80, 91, 195, 202},
                {conferenceRoom, 89, 106, 143, 168}, {meetingRoom,22, 59, 1, 16},
                {controlCenter, 38, 59, 107, 125}, {controlCenterCCTV, 26, 37, 107, 125},
                {dataCenter, 38, 59, 127, 145}, {mesa, 67, 80, 144, 160},
                {SR1, 67, 79, 94, 101}, {SR2, 67, 79, 85, 92}, {SR3, 67, 79, 48, 55}, {SR4, 67, 79, 39, 46},
                {LS1, 26, 59, 86, 105}, {LS2, 26, 59, 65, 84}, {LS3, 26, 59, 44, 63}, {LS4, 26, 59, 22, 42},
                {researchCenter, 87, 106, 24, 98}, {facultyRoom, 87, 106, 99, 127},
                {humanExpRoom, 67, 86, 1, 15}, {dataCollectionRoom, 87, 104, 1, 22},
                {storageRoom, 89, 106, 129, 141}, {clinic, 80, 91, 186, 193}
        };

        for (Object[] range : floorRanges) {
            List<Patch> floorPatches =  (List<Patch>) range[0];
            int startRow = (int) range[1];
            int endRow = (int) range[2];
            int startColumn = (int) range[3];
            int endColumn = (int) range[4];
            for (int i = startRow; i <= endRow; i ++) {
                for (int j = startColumn; j <= endColumn; j ++) {
                    floorPatches.add(environment.getPatch(i, j));
                }
            }
        }

        simulator.getEnvironment().getElevatorLobbies().add(ElevatorLobby.elevatorLobbyFactory.create(elevatorLobby, ""));
        simulator.getEnvironment().getFloors().add(Floor.floorFactory.create(hallway, "dimHallway"));
        simulator.getEnvironment().getBathrooms().add(Bathroom.bathroomFactory.create(maleBathroom, "maleBathroom"));
        simulator.getEnvironment().getBathrooms().add(Bathroom.bathroomFactory.create(femaleBathroom, "femaleBathroom"));
        simulator.getEnvironment().getBathrooms().add(Bathroom.bathroomFactory.create(directorBathroom, "dimDirectorBathroom"));
        simulator.getEnvironment().getBreakerRooms().add(BreakerRoom.breakerRoomFactory.create(breakerRoom, "dimBreakerRoom"));
        simulator.getEnvironment().getReceptions().add(Reception.receptionFactory.create(reception, "dimReception"));
        simulator.getEnvironment().getDirectorRooms().add(DirectorRoom.directorRoomFactory.create(directorRoom, "dimDirectorRoom"));
        simulator.getEnvironment().getConferenceRooms().add(ConferenceRoom.conferenceRoomFactory.create(conferenceRoom, "dimConferenceRoom"));
        simulator.getEnvironment().getMeetingRooms().add(MeetingRoom.meetingRoomFactory.create(meetingRoom, "dimMeetingRoom"));
        simulator.getEnvironment().getControlCenters().add(ControlCenter.controlCenterFactory.create(controlCenter, "dimControlCenter"));
        simulator.getEnvironment().getControlCenters().add(ControlCenter.controlCenterFactory.create(controlCenterCCTV, "dimCCTV"));
        simulator.getEnvironment().getDataCenters().add(DataCenter.dataCenterFactory.create(dataCenter, "dimDataCenter"));
        simulator.getEnvironment().getSoloRooms().add(SoloRoom.soloRoomFactory.create(SR1, "dimSR1"));
        simulator.getEnvironment().getSoloRooms().add(SoloRoom.soloRoomFactory.create(SR2, "dimSR2"));
        simulator.getEnvironment().getSoloRooms().add(SoloRoom.soloRoomFactory.create(SR3, "dimSR3"));
        simulator.getEnvironment().getSoloRooms().add(SoloRoom.soloRoomFactory.create(SR4, "dimSR4"));
        simulator.getEnvironment().getLearningSpaces().add(LearningSpace.learningSpaceFactory.create(LS1, "dimLS1"));
        simulator.getEnvironment().getLearningSpaces().add(LearningSpace.learningSpaceFactory.create(LS2, "dimLS2"));
        simulator.getEnvironment().getLearningSpaces().add(LearningSpace.learningSpaceFactory.create(LS3, "dimLS3"));
        simulator.getEnvironment().getLearningSpaces().add(LearningSpace.learningSpaceFactory.create(LS4, "dimLS4"));
        simulator.getEnvironment().getResearchCenters().add(ResearchCenter.researchCenterFactory.create(researchCenter, "dimResearchCenter"));
        simulator.getEnvironment().getFacultyRooms().add(FacultyRoom.facultyRoomFactory.create(facultyRoom, "dimFacultyRoom"));
        simulator.getEnvironment().getHumanExpRooms().add(HumanExpRoom.humanExpRoomFactory.create(humanExpRoom, "dimHumExpRoom"));
        simulator.getEnvironment().getDataCollectionRooms().add(DataCollectionRoom.dataCollectionRoomFactory.create(dataCollectionRoom, "dimDataCollRoom"));
        simulator.getEnvironment().getStorageRooms().add(StorageRoom.storageRoomFactory.create(storageRoom, "dimStorageRoom"));
        simulator.getEnvironment().getClinics().add(Clinic.clinicFactory.create(clinic, "dimClinic"));
        simulator.getEnvironment().getMESAs().add(MESA.MESAFactory.create(mesa, "dimMESA"));
        simulator.getEnvironment().getPantries().add(Pantry.pantryFactory.create(pantry, "dimPantry"));

        /* Office Next Door */

        List<Patch> officeNextDoor = new ArrayList<>();

        for (int i = 0; i <= 55; i++) {
            int startColumn = 0;
            int endColumn = 183;

            if (i >= 14 && i <= 21) {
                startColumn = 32;
            } else if (i >= 22 && i <= 26) {
                startColumn = 127;
            } else if (i >= 27 && i <= 33) {
                startColumn = 127;
                endColumn = 175;
            } else if (i >= 34 && i <= 46) {
                startColumn = 147;
                endColumn = 175;
            } else if (i >= 47) {
                startColumn = 154;
                endColumn = 175;
            }

            for (int j = startColumn; j <= endColumn; j++) {
                officeNextDoor.add(environment.getPatch(i, j));
            }
        }

        simulator.getEnvironment().getDividers().add(Divider.dividerFactory.create(officeNextDoor, "officeNextDoor"));


        /* Parking Lot */

        List<Patch> parkingLot = new ArrayList<>();

        for (int i = 109; i <= 110; i++ ) {
            for (int j = 0; j <= 22; j++ ) {
                parkingLot.add(environment.getPatch(i, j));
            }
        }

        for (int i = 111; i <= 128; i++ ) {
            for (int j = 0; j <= 133; j++ ) {
                parkingLot.add(environment.getPatch(i, j));
            }
        }

        for (int i = 118; i <= 128; i++ ) {
            for (int j = 187; j <= 203; j++ ) {
                parkingLot.add(environment.getPatch(i, j));
            }
        }

        simulator.getEnvironment().getDividers().add(Divider.dividerFactory.create(parkingLot, "parkingLot"));

        /* Permanent Walls */

        List<Patch> permanentWalls = new ArrayList<>();

        int[][] permanentWallRanges = {

                // Male Bathroom
                {1, 186, 202}, {1, 186, 202}, {11, 185, 191}, {20, 191, 192},

                // Female Bathroom
                {57, 191, 202}, {66, 186, 191},

                // Office
                {15, 1, 30}, {23, 22, 125}, {35, 126, 145}, {48, 146, 152}, {57, 153, 176}, {77, 169, 202},
                {115, 187, 203}, {126, 134, 186}, {108, 23, 133}, {106, 0, 22}, {24, 177, 184},

                // Pillars
                {52, 136, 137}, {98, 31, 32}, {98, 53, 54}, {103, 84, 85}, {103, 102, 103}, {91, 131, 132},
                {91, 156, 157}, {110, 201, 202}, {118, 144, 145}, {118, 165, 166}

        };

        for (int[] range : permanentWallRanges) {
            int startRow = range[0];
            int endRow = startRow + 2;
            int startColumn = range[1];
            int endColumn = range[2];

            for (int i = startRow; i <= endRow; i ++) {
                for (int j = startColumn; j <= endColumn; j++) {
                    permanentWalls.add(environment.getPatch(i, j));
                }
            }
        }

        simulator.getEnvironment().getDividers().add(Divider.dividerFactory.create(permanentWalls, "permanentWall"));


        /* Permanent Wall Tops */

        List<Patch> permanentWallTops = new ArrayList<>();

        int[][] horizontalRangesPWT = {

                // Office Outline
                {14, 0, 31}, {22, 22, 126}, {34, 126, 146}, {47, 146, 153}, {56, 153, 176}, {23, 176, 184},
                {56, 183, 184}, {76, 169, 203}, {114, 186, 203}, {125, 134, 186}, {107, 23, 134}, {105, 0, 23},

                // Male Bathroom
                {0, 185, 203}, {10, 185, 191}, {19, 191, 203},

                // Female Bathroom
                {56, 191, 203}, {65, 186, 191}, {75, 185, 203}
        };

        int[][] verticalRangesPWT = {

                // Office Outline
                {0, 15, 104}, {31, 15, 21}, {126, 23, 33}, {146, 35, 46}, {153, 48, 55}, {176, 24, 55},
                {184, 0, 22}, {184, 57, 75}, {169, 67, 75}, {203, 77, 113}, {186, 115, 124}, {134, 108, 124},
                {23, 106, 106},

                // Male Bathroom
                {185, 1, 9}, {203, 1, 18},

                // Female Bathroom
                {185, 56, 74}, {203, 57, 74}

        };

        int[][] pillarTops = {
                {50, 136, 1}, {96, 31, 1}, {96, 53, 1}, {101, 84, 1}, {101, 102, 1}, {88, 131, 2},
                {88, 156, 2}, {108, 201, 1}, {116, 144, 1}, {116, 165, 1}
        };


        // Elevator Area
        for (int i = 20; i <= 55; i++) {
            for (int j = 193; j <= 203; j++) {
                permanentWallTops.add(environment.getPatch(i, j));
            }
        }

        for (int[] range : horizontalRangesPWT) {
            int row = range[0];
            int startColumn = range[1];
            int endColumn = range[2];

            for (int j = startColumn; j <= endColumn; j++) {
                permanentWallTops.add(environment.getPatch(row, j));
            }
        }

        for (int[] range : verticalRangesPWT) {
            int column = range[0];
            int startRow = range[1];
            int endRow = range[2];

            for (int i = startRow; i <= endRow; i++) {
                permanentWallTops.add(environment.getPatch(i, column));
            }
        }

        for (int[] range : pillarTops) {
            int startRow = range[0];
            int endRow = startRow + range[2];
            int startColumn = range[1];
            int endColumn = startColumn + 1;

            for (int i = startRow; i <= endRow; i ++) {
                for (int j = startColumn; j <= endColumn; j++) {
                    permanentWallTops.add(environment.getPatch(i, j));
                }
            }
        }

        simulator.getEnvironment().getDividers().add(Divider.dividerFactory.create(permanentWallTops, "permanentWallTop"));


        /* Walls (inside office) */

        List<Patch> walls = new ArrayList<>();

        int[][] wallRanges = {

                // Meeting Room
                {23, 1, 16}, {57, 1, 9}, {57, 15, 16},

                // Learning Spaces
                {57, 22, 31}, {57, 37, 57}, {57, 63, 78}, {57, 84, 99}, {57, 105, 106},

                // Solo Rooms
                {68, 38, 39}, {68, 44, 56}, {68, 84, 96}, {68, 101, 102},
                {77, 38, 50}, {77, 55, 56}, {77, 84, 85}, {77, 90, 102},

                // MESA
                {72, 143, 161},

                // Data & Control Centers
                {57, 106, 119}, {57, 125, 139}, {57, 145, 146}, {35, 106, 107}, {35, 113, 125},

                // Human Experiment Room, Data Collection Room, Research Center,
                // Faculty Room, Storage Room, Conference Room
                {68, 1, 15}, {88, 1, 8}, {88, 14, 16}, {88, 22, 24}, {88, 30, 120}, {88, 127, 128},
                {90, 129, 130}, {90, 133, 135}, {90, 141, 143}, {90, 149, 155}, {90, 158, 162},
                {90, 168, 169}, {97, 1, 6}, {106, 142, 144}, {110, 142, 144}, {106, 143, 144},
                {108, 135, 169},

                // Director Room & Bathroom and Clinic
                {89, 186, 195}, {89, 200, 202}

        };

        for (int[] range : wallRanges) {
            int startRow = range[0];
            int endRow = startRow + 2;
            int startColumn = range[1];
            int endColumn = range[2];

            for (int i = startRow; i <= endRow; i ++) {
                for (int j = startColumn; j <= endColumn; j++) {
                    walls.add(environment.getPatch(i, j));
                }
            }
        }

        simulator.getEnvironment().getDividers().add(Divider.dividerFactory.create(walls, "wall"));

        /* Wall Tops (inside office) */

        List<Patch> wallTops = new ArrayList<>();

        int[][] horizontalRangesWT = {

                // Meeting Room
                {22, 1, 16}, {56, 1, 9}, {56, 15, 16},

                // Learning Spaces
                {56, 22, 31}, {56, 37, 57}, {56, 63, 78}, {56, 84, 99}, {56, 105, 106},

                // Solo Rooms
                {67, 38, 39}, {67, 44, 56}, {67, 84, 96}, {67, 101, 102},
                {76, 38, 50}, {76, 55, 56}, {76, 84, 85}, {76, 90, 102},

                // MESA
                {72, 143, 161},

                // Data & Control Centers
                {56, 106, 119}, {56, 125, 139}, {56, 145, 146}, {34, 106, 107}, {34, 113, 125},

                // Human Experiment Room, Data Collection Room, Research Center,
                // Faculty Room, Storage Room, Conference Room
                {67, 1, 15}, {87, 1, 8}, {87, 14, 16}, {87, 22, 24}, {87, 30, 120}, {87, 127, 128},
                {89, 129, 130}, {89, 133, 135}, {89, 141, 143}, {89, 149, 155}, {89, 158, 162},
                {89, 168, 169}, {96, 1, 6}, {105, 142, 144}, {109, 142, 144}, {105, 143, 144}, {104, 6, 6},
                {107, 135, 169},

                // Director Room & Bathroom and Clinic
                {88, 186, 195}, {88, 200, 202}
        };

        int[][] verticalRangesWT = {

                // Meeting Room
                {16, 23, 55},

                // Learning Spaces
                {22, 23, 56}, {43, 23, 56}, {64, 23, 56}, {85, 23, 56}, {106, 23, 56},

                // Solo Rooms
                {38, 68, 75}, {47, 68, 75}, {56, 68, 75}, {84, 68, 75}, {93, 68, 75}, {102, 68, 75},

                // MESA
                {143, 67, 80}, {152, 67, 80}, {161, 67, 80},

                // Data & Control Centers
                {126, 35, 55}, {146, 48, 55},

                // Human Experiment Room, Data Collection Room, Research Center,
                // Faculty Room, Storage Room, Conference Room
                {15, 77, 86}, {23, 88, 104}, {98, 98, 106}, {128, 88, 106}, {142, 90, 109}, {169, 90, 106},

                // Director Room & Bathroom and Clinic
                {186, 85, 87}, {186, 97, 113}, {194, 77, 87}

        };

        for (int[] range : horizontalRangesWT) {
            int row = range[0];
            int startColumn = range[1];
            int endColumn = range[2];

            for (int j = startColumn; j <= endColumn; j++) {
                wallTops.add(environment.getPatch(row, j));
            }
        }

        for (int[] range : verticalRangesWT) {
            int column = range[0];
            int startRow = range[1];
            int endRow = range[2];

            for (int i = startRow; i <= endRow; i++) {
                wallTops.add(environment.getPatch(i, column));
            }
        }

        simulator.getEnvironment().getDividers().add(Divider.dividerFactory.create(wallTops, "wallTop"));


        /* Door Patches */

        List<Patch> permanentDoorPatches = new ArrayList<>();

        int[][] permanentDoorRanges = {

                // Elevators
                {193, 23}, {193, 34}, {193, 45},

                // Male Bathroom
                {191, 11},

                // Female Bathroom
                {191, 57},

                // Office
                {183, 57}, {169, 57}, {22, 15},


                // Inside Office
                {9, 57}, {15, 57}, {31, 57}, {37, 57}, {57, 57}, {63, 57}, {78, 57}, {84, 57}, {99, 57}, {105, 57},
                {119, 57}, {125, 57}, {139, 57}, {145, 57}, {15, 68}, {39, 68}, {44, 68}, {96, 68}, {101, 68},
                {50, 77}, {55, 77}, {85, 77}, {90, 77}, {8, 88}, {14, 88}, {16, 88}, {22, 88}, {24, 88}, {30, 88},
                {120, 88}, {127, 88}, {135, 90}, {141, 90}, {143, 90}, {149, 90}, {162, 90}, {168, 90}, {186, 89},
                {195, 89}, {200, 89}

        };

        for (int[] range : permanentDoorRanges) {
            int column = range[0];
            int startRow = range[1];
            int endRow = startRow + 2;


            for (int i = startRow; i <= endRow; i ++) {
                permanentDoorPatches.add(environment.getPatch(i, column));
            }
        }

        simulator.getEnvironment().getDividers().add(Divider.dividerFactory.create(permanentDoorPatches, "doorPatch"));


        /* AMENITIES */
        
        /* Cubicles */
        
        List<Patch> nwMESA = new ArrayList<>(), neMESA = new ArrayList<>(), swMESA = new ArrayList<>(), seMESA = new ArrayList<>(),
                cubicleA  = new ArrayList<>(), westCubicleB  = new ArrayList<>(), eastCubicleB  = new ArrayList<>(),
                westCubicleC  = new ArrayList<>();

        Object[][] cubicleRanges =  {

                // MESA
                {nwMESA, 67, 153}, {neMESA, 67, 148},
                {swMESA, 76, 153}, {seMESA, 76, 148},

                // Type A
                {cubicleA, 43, 111},

                // Type B
                {westCubicleB, 95, 105}, {westCubicleB, 95, 115}, {westCubicleB, 95, 125},
                {westCubicleB, 99, 105}, {westCubicleB, 99, 115}, {westCubicleB, 99, 125},
                
                {eastCubicleB, 95, 108}, {eastCubicleB, 95, 118},
                {eastCubicleB, 99, 108}, {eastCubicleB, 99, 118},

                // Type C
                {westCubicleC, 41, 120}, {westCubicleC, 44, 120},
                {westCubicleC, 47, 120}, {westCubicleC, 50, 120},
        };
        
        for (Object[] range : cubicleRanges) {
            List<Patch> amenityPatches =  (List<Patch>) range[0];
            int row = (int) range[1];
            int column = (int) range[2];
            amenityPatches.add(environment.getPatch(row, column));
        }
        
        CubicleMapper.draw(nwMESA, "MESA", "", "NORTH_AND_WEST", false, 0);
        CubicleMapper.draw(neMESA, "MESA", "", "NORTH_AND_EAST", false, 0);
        CubicleMapper.draw(swMESA, "MESA", "", "SOUTH_AND_WEST", false, 0);
        CubicleMapper.draw(seMESA, "MESA", "", "SOUTH_AND_EAST", false, 0);
        CubicleMapper.draw(cubicleA, "TYPE_A", "", "", true, 1);
        CubicleMapper.draw(westCubicleB, "TYPE_B", "WEST", "", true, 2);
        CubicleMapper.draw(eastCubicleB, "TYPE_B", "EAST", "", true, 2);
        CubicleMapper.draw(westCubicleC, "TYPE_C", "WEST", "", false, 0);
        
        /* Reception Table */
        
        List<Patch> ReceptionTable1x8 = new ArrayList<>();
        ReceptionTable1x8.add(environment.getPatch(69,170));
        ReceptionTableMapper.draw(ReceptionTable1x8, "1x8");
        
        /* Research Tables */
        
        List<Patch> westResearchTable = new ArrayList<>(), westMonitorResearchTable = new ArrayList<>(),
                eastResearchTable = new ArrayList<>(), eastMonitorResearchTable = new ArrayList<>();
        
        Object[][] researchTableRanges =  {
                {westResearchTable, 100, 38}, {eastResearchTable, 100, 39},
                {westMonitorResearchTable, 100, 46}, {eastResearchTable, 100, 47},
                {westResearchTable, 100, 60}, {eastResearchTable, 100, 61},
                {westMonitorResearchTable, 100, 68}, {eastResearchTable, 100, 69},
                {westMonitorResearchTable, 100, 76}, {eastResearchTable, 100, 77},
                {westResearchTable, 100, 90}, {eastMonitorResearchTable, 100, 91}
        };
        
        for (Object[] range : researchTableRanges) {
            List<Patch> amenityPatches =  (List<Patch>) range[0];
            int row = (int) range[1];
            int column = (int) range[2];
            amenityPatches.add(environment.getPatch(row, column));
        }

        ResearchTableMapper.draw(westResearchTable, "WEST", false);
        ResearchTableMapper.draw(eastResearchTable, "EAST", false);
        ResearchTableMapper.draw(westMonitorResearchTable, "WEST", true);
        ResearchTableMapper.draw(eastMonitorResearchTable, "EAST", true);

        
        /* Learning Tables */
        
        List<Patch> learningTables = new ArrayList<>();
        
        Object[][] learningTableRanges =  {
                // Learning Space 1
                {33, 89}, {33, 98},
                {47, 89}, {47, 98},

                // Learning Space 2
                {33, 68}, {33, 77},
                {47, 68}, {47, 77},

                // Learning Space 3
                {33, 47}, {33, 56},
                {47, 47}, {47, 56},

                // Learning Space 4
                {33, 26}, {33, 35},
                {47, 26}, {47, 35}
        };

        for (Object[] range : learningTableRanges) {
            int row = (int) range[0];
            int column = (int) range[1];
            learningTables.add(environment.getPatch(row, column));
        }
        
        LearningTableMapper.draw(learningTables, "HORIZONTAL");

        /* Meeting Tables */
        
        List<Patch> largeVerticalMeetingTables = new ArrayList<>(), leftLargeHorizontalMeetingTables = new ArrayList<>(),
                rightLargeHorizontalMeetingTables = new ArrayList<>(), smallVerticalMeetingTables = new ArrayList<>();
        
        Object[][] meetingTableRanges =  {
                // Conference Room
                {leftLargeHorizontalMeetingTables, 98, 147}, {rightLargeHorizontalMeetingTables, 98, 156},

                // Meeting Room
                {largeVerticalMeetingTables, 36, 7},

                // Director Room
                {smallVerticalMeetingTables, 97, 198},
        };
        
        for (Object[] range : meetingTableRanges) {
            List<Patch> amenityPatches =  (List<Patch>) range[0];
            int row = (int) range[1];
            int column = (int) range[2];
            amenityPatches.add(environment.getPatch(row, column));
        }

        MeetingTableMapper.draw(largeVerticalMeetingTables, "VERTICAL", "LARGE", "");
        MeetingTableMapper.draw(leftLargeHorizontalMeetingTables, "HORIZONTAL", "LARGE", "LEFT");
        MeetingTableMapper.draw(rightLargeHorizontalMeetingTables, "HORIZONTAL", "LARGE", "RIGHT");
        MeetingTableMapper.draw(smallVerticalMeetingTables, "VERTICAL", "SMALL", "");


        /* Pantry Tables and Chairs */

        List<Patch> typeApantryTables = new ArrayList<>(), typeBpantryTables = new ArrayList<>(),
                typeApantryChairs = new ArrayList<>(), typeBpantryChairs = new ArrayList<>();


        Object[][] pantryTableRanges =  {

                // Type A
                {typeApantryTables, 117, 180}, {typeApantryTables, 120, 175},

                // Type B
                {typeBpantryTables, 117, 169}, {typeBpantryTables, 121, 139}, {typeBpantryTables,  121, 148},
                {typeBpantryTables, 121, 154}, {typeBpantryTables, 121, 160}
        };

        for (Object[] range : pantryTableRanges) {
            List<Patch> amenityPatches =  (List<Patch>) range[0];
            int row = (int) range[1];
            int column = (int) range[2];
            amenityPatches.add(environment.getPatch(row, column));
        }

        PantryTableMapper.draw(typeApantryTables, "TYPE_A");
        PantryTableMapper.draw(typeBpantryTables, "TYPE_B");

        Object[][] pantryChairRanges =  {

                // Type A
                {typeApantryChairs, 111, 150}, {typeApantryChairs, 111, 151}, {typeApantryChairs, 111, 152},
                {typeApantryChairs, 111, 153}, {typeApantryChairs, 111, 154},

                // Type B
                {typeBpantryChairs, 111, 145}, {typeBpantryChairs, 111, 146}, {typeBpantryChairs, 111, 147},
                {typeBpantryChairs, 111, 148}, {typeBpantryChairs, 111, 149}
        };
        
        for (Object[] range : pantryChairRanges) {
            List<Patch> amenityPatches =  (List<Patch>) range[0];
            int row = (int) range[1];
            int column = (int) range[2];
            amenityPatches.add(environment.getPatch(row, column));
        }

        ChairMapper.draw(typeApantryChairs, 0, "SOUTH","PANTRY_TYPE_A", "");
        ChairMapper.draw(typeBpantryChairs, 0, "SOUTH","PANTRY_TYPE_B", "");
        
        /* Director Table */

        List<Patch> directorTable = new ArrayList<>();
        directorTable.add(environment.getPatch(108,190));
        DirectorTableMapper.draw(directorTable, "HORIZONTAL", "SOUTH", true);


        /* TABLE 2x2 */
        
        List<Patch> table2x2 = new ArrayList<>();
        table2x2.add(environment.getPatch(45,127));
        Table2x2Mapper.draw(table2x2);


        /* Solo Table */

        // Top
        List<Patch> topSoloTables = new ArrayList<>();
        topSoloTables.add(environment.getPatch(71,48));
        topSoloTables.add(environment.getPatch(71,85));
        SoloTableMapper.draw(topSoloTables, "1x8", "TOP");

        // Bottom
        List<Patch> bottomSoloTables = new ArrayList<>();
        bottomSoloTables.add(environment.getPatch(75,39));
        bottomSoloTables.add(environment.getPatch(75,94));
        SoloTableMapper.draw(bottomSoloTables, "1x8", "BOTTOM");

        /* Human Experience Table */
        List<Patch> humanExpTable = new ArrayList<>();
        humanExpTable.add(environment.getPatch(77,5));
        HumanExpTableMapper.draw(humanExpTable, "5x1");
        
        /* Data Collection Table */
        List<Patch> dataCollTable = new ArrayList<>();
        dataCollTable.add(environment.getPatch(91,2));
        DataCollTableMapper.draw(dataCollTable, "1x6");
        
        /* White Board */

        List<Patch> northWhiteBoard = new ArrayList<>(), southWhiteBoard = new ArrayList<>(), westWhiteBoard = new ArrayList<>(),
                    eastWhiteBoard_4 = new ArrayList<>(), eastWhiteBoard_11 = new ArrayList<>();
        
        Object[][] whiteBoardRanges =  {
                // Research Center Pillars
                {westWhiteBoard, 96, 30}, {northWhiteBoard, 95, 31}, {eastWhiteBoard_4, 96, 33},
                {westWhiteBoard, 96, 52}, {northWhiteBoard, 95, 53}, {eastWhiteBoard_4, 96, 55},

                // Meeting Room
                {eastWhiteBoard_11, 36, 1},

                // Learning Spaces
                {southWhiteBoard, 23, 26}, {southWhiteBoard, 23, 35},
                {southWhiteBoard, 23, 47}, {southWhiteBoard, 23, 56},
                {southWhiteBoard, 23, 68}, {southWhiteBoard, 23, 77},
                {southWhiteBoard, 23, 89}, {southWhiteBoard, 23, 98},
        };

        for (Object[] range : whiteBoardRanges) {
            List<Patch> amenityPatches =  (List<Patch>) range[0];
            int row = (int)  range[1];
            int column = (int) range[2];
            amenityPatches.add(environment.getPatch(row, column));
        }

        WhiteboardMapper.draw(northWhiteBoard, "NORTH", "2");
        WhiteboardMapper.draw(southWhiteBoard, "SOUTH", "5");
        WhiteboardMapper.draw(westWhiteBoard, "WEST", "4");
        WhiteboardMapper.draw(eastWhiteBoard_4, "EAST", "4");
        WhiteboardMapper.draw(eastWhiteBoard_11, "EAST", "11");


        /* Elevator */
        List<Patch> elevator = new ArrayList<>();
        elevator.add(environment.getPatch(26,193));
        elevator.add(environment.getPatch(37,193));
        elevator.add(environment.getPatch(48,193));
        ElevatorMapper.draw(elevator, Elevator.ElevatorMode.ENTRANCE_AND_EXIT,  "VERTICAL");

        /* Couch */
        List<Patch> couch = new ArrayList<>();
        couch.add(environment.getPatch(69,141));
        CouchMapper.draw(couch, "WEST");

        /* Refrigerator */
        List<Patch> refrigerator = new ArrayList<>();
        refrigerator.add(environment.getPatch(110,141));
        RefrigeratorMapper.draw(refrigerator);

        /* Water Dispenser */
        List<Patch> waterDispenser = new ArrayList<>();
        waterDispenser.add(environment.getPatch(110,140));
        WaterDispenserMapper.draw(waterDispenser);


        /* Plants, Trash Cans, Pantry Cabinets, Coffee Maker Bar, Kettle Bar, Microwave Bar */

        List<Patch> plants = new ArrayList<>(), trashCans = new ArrayList<>(), pantryCabinets = new ArrayList<>(),
                    coffeeMakerBar = new ArrayList<>(), kettleBar = new ArrayList<>(), microwaveBar = new ArrayList<>();

        Object[][] simpleAmenityRanges =  {

                // Plants
                {plants, 62, 2}, {plants, 74, 179}, {plants, 74, 181}, {plants, 75, 180},
                {plants, 82, 201}, {plants, 84, 201}, {plants, 86, 201},

                // Trash Cans
                {trashCans, 80, 199}, {trashCans, 113, 135}, {trashCans, 107, 201},

                // Pantry Cabinets
                {pantryCabinets, 109, 135}, {pantryCabinets, 109, 136}, {pantryCabinets, 109, 137}, {pantryCabinets, 109, 138},

                // Coffee Maker Bar
                {coffeeMakerBar, 110, 137},

                // Kettle Bar
                {kettleBar, 110, 138},

                // Microwave Bar
                {microwaveBar, 111, 135}
        };

        for (Object[] range : simpleAmenityRanges) {
            List<Patch> amenityPatches =  (List<Patch>) range[0];
            int row = (int) range[1];
            int column = (int) range[2];
            amenityPatches.add(environment.getPatch(row, column));
        }
        
        PlantMapper.draw(plants);
        TrashCanMapper.draw(trashCans);
        PantryCabinetMapper.draw(pantryCabinets);
        CoffeeMakerBarMapper.draw(coffeeMakerBar);
        KettleBarMapper.draw(kettleBar);
        MicrowaveBarMapper.draw(microwaveBar);


        /* Sinks & Toilets */

        List<Patch> southSinks = new ArrayList<>(), southOfficeSinks = new ArrayList<>(), northSinks = new ArrayList<>(),
                    southToilets = new ArrayList<>(), southOfficeToilets = new ArrayList<>(), northToilets  = new ArrayList<>();

        Object[][] sinkToiletRanges =  {

                // Male Bathroom
                {southToilets, 3, 188}, {southToilets, 3, 191},
                {southToilets, 3, 194}, {southToilets, 3, 197},
                {southToilets, 3, 200}, {northSinks, 18, 194},
                {northSinks, 18, 197}, {northSinks, 18, 200},

                // Female Bathroom
                {northToilets, 73, 188}, {northToilets, 73, 191},
                {northToilets, 73, 194}, {northToilets, 73, 197},
                {northToilets, 73, 200}, {southSinks,  60, 194},
                {southSinks, 60, 197}, {southSinks, 60, 200},

                // Director Bathroom
                {southOfficeToilets, 79, 200}, {southOfficeSinks, 80, 197},

                // Pantry
                {southOfficeSinks, 111, 136},
        };

        for (Object[] range : sinkToiletRanges) {
            List<Patch> amenityPatches =  (List<Patch>) range[0];
            int row = (int) range[1];
            int column = (int) range[2];
            amenityPatches.add(environment.getPatch(row, column));
        }
        
        SinkMapper.draw(southSinks, "SOUTH", "SINK");
        SinkMapper.draw(southOfficeSinks, "SOUTH", "OFFICE_SINK");
        SinkMapper.draw(northSinks, "NORTH", "SINK");
        ToiletMapper.draw(southToilets, "SOUTH", "TOILET");
        ToiletMapper.draw(southOfficeToilets, "SOUTH", "OFFICE_TOILET");
        ToiletMapper.draw(northToilets, "NORTH", "TOILET");


        /* Switches */

        List<Patch> southLightSwitches = new ArrayList<>(), northLightSwitches = new ArrayList<>(), westLightSwitches = new ArrayList<>(), eastLightSwitches = new ArrayList<>(),
                    southAirconSwitches = new ArrayList<>(), northAirconSwitches = new ArrayList<>(), westAirconSwitches = new ArrayList<>(), eastAirconSwitches = new ArrayList<>();

        Object[][] switchRanges =  {

                // Meeting Room
                {southLightSwitches,24, 13}, {southAirconSwitches, 24, 14},

                // Learning Spaces
                {eastLightSwitches, 53, 23}, {eastAirconSwitches, 54, 23},
                {westLightSwitches, 53, 105}, {westAirconSwitches, 54, 105},

                // Control Center
                {southLightSwitches,36, 115}, {southAirconSwitches, 36, 116},

                // Data Center
                {westLightSwitches, 53, 145}, {westAirconSwitches, 54, 145},

                // Hallway
                {eastLightSwitches, 57, 147}, {eastAirconSwitches, 58, 147},

                // Reception
                {southLightSwitches,58, 174}, {southAirconSwitches, 58, 175},

                // Human Experience Room
                {southLightSwitches,69, 12}, {southAirconSwitches, 69, 13},

                // Data Collection Room
                {northLightSwitches, 95, 5}, {northAirconSwitches, 95, 6},

                // Research Center
                {westLightSwitches, 103, 83}, {westAirconSwitches, 104, 83},

                // Faculty Room
                {southLightSwitches,89, 117}, {southAirconSwitches, 89, 118},

                // Storage Room
                {southLightSwitches,91, 133},

                // Conference Room
                {southLightSwitches,91, 151}, {southAirconSwitches, 91, 152},

                // Clinic
                {southLightSwitches,78, 188}, {southAirconSwitches, 78, 189},

                // Director Room
                {southLightSwitches,90, 188}, {southAirconSwitches, 90, 189},

                // Pantry
                {westLightSwitches, 112, 185}, {southAirconSwitches, 109, 168},

                // Solo Rooms
                {westLightSwitches, 72, 101}, {eastLightSwitches, 74, 85},
                {westLightSwitches, 74, 55}, {eastLightSwitches, 72, 39},

                // Director Bathroom
                {eastLightSwitches, 86, 195},
        };

        for (Object[] range : switchRanges) {
            List<Patch> amenityPatches =  (List<Patch>) range[0];
            int row = (int) range[1];
            int column = (int) range[2];
            amenityPatches.add(environment.getPatch(row, column));
        }

        SwitchMapper.draw(southLightSwitches, "LIGHT", "SOUTH");
        SwitchMapper.draw(northLightSwitches, "LIGHT", "NORTH");
        SwitchMapper.draw(westLightSwitches, "LIGHT", "WEST");
        SwitchMapper.draw(eastLightSwitches, "LIGHT", "EAST");
        SwitchMapper.draw(southAirconSwitches, "AC", "SOUTH");
        SwitchMapper.draw(northAirconSwitches, "AC", "NORTH");
        SwitchMapper.draw(westAirconSwitches, "AC", "WEST");
        SwitchMapper.draw(eastAirconSwitches, "AC", "EAST");


        /* Aircons */

        List<Patch> aircons = new ArrayList<>();

        Object[][] airconRanges =  {

                // Meeting Room
                {42, 7},

                // Learning Spaces
                {28, 35}, {50, 25},
                {27, 55}, {50, 48}, {50, 56},
                {27, 78}, {47, 70},
                {27, 99}, {47, 91},

                // Control Center
                {41, 118}, {50, 118},

                // Data Center
                {41, 132}, {41, 139},

                // Hallway
                {72, 22}, {72, 62}, {72, 108}, {72, 120},
                {72, 132}, {62, 147},

                // Reception
                {66, 179},

                // Human Experience Room
                {72, 6},

                // Data Collection Room
                {93, 9},

                // Research Center
                {92, 34}, {92, 40}, {97, 71}, {85, 88},

                // Faculty Room
                {95, 101}, {95, 112}, {100, 122},

                // Conference Room
                {94, 149}, {94, 161},

                // Clinic
                {82, 189},

                // Director Room
                {99, 191},

                // Pantry
                {120, 151},
        };

        for (Object[] range : airconRanges) {
            int row = (int) range[0];
            int column = (int) range[1];
            aircons.add(environment.getPatch(row, column));
        }

        AirconMapper.draw(aircons, false);


        /* Lights */

        List<Patch> singlePendantLights = new ArrayList<>(), singleRecessedLights = new ArrayList<>(), horizontalPendantLights = new ArrayList<>(), verticalPendantLights = new ArrayList<>(),
                    horizontalRecessedLights = new ArrayList<>(), verticalRecessedLights = new ArrayList<>(), horizontalTrackLights = new ArrayList<>(), verticalTrackLights = new ArrayList<>();

        Object[][] lightRanges =  {

                // Meeting Room
                {horizontalTrackLights, 30, 7},
                {verticalRecessedLights, 32, 8}, {verticalRecessedLights, 47, 8},

                // Learning Space 4
                {horizontalTrackLights, 27, 27}, {horizontalTrackLights, 43, 27},
                {horizontalTrackLights, 43, 35}, {horizontalTrackLights, 54, 27},
                {verticalRecessedLights, 28, 25}, {verticalRecessedLights, 28, 40},
                {verticalRecessedLights, 50, 33},

                // Learning Space 3
                {horizontalTrackLights, 27, 49}, {horizontalTrackLights, 43, 48},
                {horizontalTrackLights, 43, 56}, {horizontalTrackLights, 54, 52},
                {verticalRecessedLights, 28, 46}, {verticalRecessedLights, 28, 58},
                {verticalRecessedLights, 52, 54},

                // Learning Space 2
                {horizontalTrackLights, 27, 69}, {horizontalTrackLights, 43, 69},
                {horizontalTrackLights, 43, 77}, {horizontalTrackLights, 54, 73},
                {verticalRecessedLights, 27, 67}, {verticalRecessedLights, 27, 75},
                {verticalRecessedLights, 52, 67}, {verticalRecessedLights, 52, 75},

                // Learning Space 1
                {horizontalTrackLights, 27, 90}, {horizontalTrackLights, 43, 90},
                {horizontalTrackLights, 43, 98}, {horizontalTrackLights, 54, 94},
                {verticalRecessedLights, 27, 88}, {verticalRecessedLights, 27, 96},
                {verticalRecessedLights, 52, 88}, {verticalRecessedLights, 52, 96},

                // Control Center
                {verticalRecessedLights, 39, 109}, {verticalRecessedLights, 39, 116},
                {verticalRecessedLights, 39, 123}, {verticalRecessedLights, 53, 109},
                {verticalRecessedLights, 53, 123},

                // Data Center
                {verticalRecessedLights, 41, 129}, {verticalRecessedLights, 41, 136},
                {verticalRecessedLights, 41, 143},

                // Hallway
                {verticalRecessedLights, 31, 19}, {verticalRecessedLights, 49, 19},
                {verticalRecessedLights, 62, 4}, {verticalRecessedLights, 62, 16},
                {verticalRecessedLights, 62, 138}, {verticalRecessedLights, 71, 138},
                {verticalRecessedLights, 80, 138}, {verticalRecessedLights, 62, 158},
                {verticalRecessedLights, 62, 165}, {verticalRecessedLights, 71, 165},
                {verticalRecessedLights, 80, 165}, {verticalRecessedLights, 85, 177},
                {verticalRecessedLights, 100, 177},
                {horizontalRecessedLights, 63, 22}, {horizontalRecessedLights, 83, 22},
                {horizontalRecessedLights, 63, 30}, {horizontalRecessedLights, 83, 30},
                {horizontalRecessedLights, 63, 38}, {horizontalRecessedLights, 83, 38},
                {horizontalRecessedLights, 63, 46}, {horizontalRecessedLights, 83, 46},
                {horizontalRecessedLights, 63, 54}, {horizontalRecessedLights, 83, 54},
                {horizontalRecessedLights, 63, 62}, {horizontalRecessedLights, 83, 62},
                {horizontalRecessedLights, 63, 70}, {horizontalRecessedLights, 83, 70},
                {horizontalRecessedLights, 63, 78}, {horizontalRecessedLights, 83, 78},
                {horizontalRecessedLights, 63, 86}, {horizontalRecessedLights, 83, 86},
                {horizontalRecessedLights, 63, 94}, {horizontalRecessedLights, 83, 94},
                {horizontalRecessedLights, 63, 102}, {horizontalRecessedLights, 83, 102},
                {horizontalRecessedLights, 63, 110}, {horizontalRecessedLights, 83, 110},
                {horizontalRecessedLights, 63, 118}, {horizontalRecessedLights, 83, 118},
                {horizontalRecessedLights, 63, 126}, {horizontalRecessedLights, 83, 126},
                {horizontalPendantLights, 75, 62}, {horizontalPendantLights, 75, 120},
                {verticalPendantLights, 72, 33},
                {singlePendantLights, 75, 78}, {singlePendantLights, 75, 108},

                // Reception
                {verticalRecessedLights, 62, 172}, {verticalRecessedLights, 62, 180},
                {verticalRecessedLights, 71, 172}, {verticalRecessedLights, 71, 180},
                {verticalTrackLights, 66, 182},

                // Human Experience Room
                {verticalRecessedLights, 78, 6}, {verticalRecessedLights, 78, 10},

                // Data Collection Room
                {verticalRecessedLights, 93, 5}, {verticalRecessedLights, 93, 19},

                // Research Center
                {verticalRecessedLights, 92, 32}, {verticalRecessedLights, 92, 38},
                {verticalRecessedLights, 92, 44}, {verticalRecessedLights, 92, 50},
                {verticalRecessedLights, 92, 56}, {verticalRecessedLights, 92, 62},
                {verticalRecessedLights, 92, 68}, {verticalRecessedLights, 92, 74},
                {verticalRecessedLights, 92, 80}, {verticalRecessedLights, 92, 86},
                {verticalRecessedLights, 92, 92},
                {verticalRecessedLights, 97, 62}, {verticalRecessedLights, 97, 68},
                {verticalRecessedLights, 97, 74}, {verticalRecessedLights, 97, 80},
                {verticalRecessedLights, 97, 86}, {verticalRecessedLights, 97, 92},
                {verticalPendantLights, 100, 41}, {verticalPendantLights, 102, 41},
                {verticalPendantLights, 100, 49}, {verticalPendantLights, 102, 49},
                {verticalPendantLights, 100, 63}, {verticalPendantLights, 102, 63},
                {verticalPendantLights, 100, 71}, {verticalPendantLights, 102, 71},
                {verticalPendantLights, 100, 79}, {verticalPendantLights, 102, 79},
                {verticalPendantLights, 100, 93}, {verticalPendantLights, 102, 93},
                {singlePendantLights, 97, 27}, {singlePendantLights, 103, 27},
                {singlePendantLights, 93, 72}, {singlePendantLights, 93, 78},
                {singlePendantLights, 93, 84}, {singlePendantLights, 93, 90},

                // Faculty Room
                {horizontalTrackLights, 93, 121}, {horizontalTrackLights, 104, 111},
                {horizontalTrackLights, 104, 121}, {verticalRecessedLights, 92, 102},
                {verticalRecessedLights, 92, 107}, {verticalRecessedLights, 92, 114},
                {verticalRecessedLights, 99, 102}, {verticalRecessedLights, 99, 107},
                {horizontalRecessedLights, 94, 126}, {horizontalRecessedLights, 103, 126},
                {horizontalPendantLights, 98, 111}, {horizontalPendantLights, 98, 113},
                {horizontalPendantLights, 99, 111}, {horizontalPendantLights, 99, 113},

                // Storage Room
                {verticalRecessedLights, 95, 134}, {verticalRecessedLights, 103, 134},

                // Conference Room
                {verticalTrackLights, 96, 144}, {verticalTrackLights, 96, 167},
                {verticalRecessedLights, 95, 147}, {verticalRecessedLights, 95, 158},
                {horizontalPendantLights, 99, 153}, {horizontalPendantLights, 99, 155},
                {horizontalPendantLights, 99, 157},

                // Clinic
                {verticalRecessedLights, 82, 187}, {verticalRecessedLights, 82, 192},

                // Director Room
                {verticalRecessedLights, 93, 189}, {verticalRecessedLights, 93, 194},
                {verticalRecessedLights, 104, 189}, {verticalRecessedLights, 109, 194},
                {verticalRecessedLights, 111, 189}, {horizontalTrackLights, 112, 190},
                {horizontalPendantLights, 107, 191}, {singlePendantLights, 98, 198},

                // Director Bathroom
                {singlePendantLights, 83, 197}, {singlePendantLights, 83, 200},

                // Pantry
                {singlePendantLights, 114, 184}, {singlePendantLights, 122, 184},
                {singlePendantLights, 114, 137}, {singlePendantLights, 118, 137},
                {singlePendantLights, 122, 137}, {horizontalRecessedLights, 114, 146},
                {horizontalRecessedLights, 114, 158}, {horizontalRecessedLights, 114, 170},
                {horizontalRecessedLights, 123, 161}, {horizontalRecessedLights, 123, 170},
                {verticalTrackLights, 115, 149}, {verticalTrackLights, 115, 155},
                {verticalTrackLights, 115, 167}, {verticalTrackLights, 115, 178},
                {horizontalTrackLights, 113, 136}, {horizontalTrackLights, 118, 151},
                {verticalRecessedLights, 122, 138}, {verticalRecessedLights, 122, 143},

                // Solo Rooms
                {singleRecessedLights, 72, 42}, {singleRecessedLights, 74, 52},
                {singleRecessedLights, 72, 98}, {singleRecessedLights, 74, 88},


        };

        for (Object[] range : lightRanges) {
            List<Patch> amenityPatches = (List<Patch>) range[0];
            int row = (int) range[1];
            int column = (int) range[2];
            amenityPatches.add(environment.getPatch(row, column));
        }
        
        LightMapper.draw(singlePendantLights, "SINGLE_PENDANT_LIGHT", "");
        LightMapper.draw(singleRecessedLights, "SINGLE_RECESSED_LIGHT", "");
        LightMapper.draw(horizontalPendantLights, "LINEAR_PENDANT_LIGHT", "HORIZONTAL");
        LightMapper.draw(verticalPendantLights, "LINEAR_PENDANT_LIGHT", "VERTICAL");
        LightMapper.draw(horizontalRecessedLights, "RECESSED_LINEAR_LIGHT", "HORIZONTAL");
        LightMapper.draw(verticalRecessedLights, "RECESSED_LINEAR_LIGHT", "VERTICAL");
        LightMapper.draw(horizontalTrackLights, "TRACK_LIGHT", "HORIZONTAL");
        LightMapper.draw(verticalTrackLights, "TRACK_LIGHT", "VERTICAL");


        /* WINDOW + BLINDS */
        List<Patch> glass = new ArrayList<>(), southFacultyClosedBlinds = new ArrayList<>(),
                    eastHallwayWindowBlinds = new ArrayList<>(), eastWindowBlinds = new ArrayList<>(), northSouthWindowBlinds = new ArrayList<>(),
                    westWindowBlinds = new ArrayList<>();

        Object[][] windowBlindsRanges = {
                // Hallway
                {eastHallwayWindowBlinds, 61, 1},

                // Human Experience Room
                {eastWindowBlinds, 72, 1},
                {eastWindowBlinds, 77, 1},
                {eastWindowBlinds, 82, 1},

                // Data Collection Room
                {glass, 88, 1},
                {eastWindowBlinds, 91, 1},
                {northSouthWindowBlinds, 104, 8, 21, 5},

                // Research Center
                {northSouthWindowBlinds, 106, 24, 97, 5},

                // Faculty Room
                {southFacultyClosedBlinds, 108, 99, 127, 5},

                // Pantry Room
                {eastWindowBlinds, 115, 135},
                {eastWindowBlinds, 120, 135},
                {northSouthWindowBlinds, 124, 136, 184, 5},

                // Director Bathroom
                {westWindowBlinds, 80, 202},
                {westWindowBlinds, 84, 202},

                // Director Room
                {westWindowBlinds, 93, 202},
                {westWindowBlinds, 98, 202},
                {westWindowBlinds, 103, 202},
                {northSouthWindowBlinds, 113, 188, 201, 5},
        };

        for (Object[] range : windowBlindsRanges) {
            List<Patch> patches = (List<Patch>) range[0];
            int row = (int) range[1];

            if (range.length == 3) {
                int column = (int) range[2];
                patches.add(environment.getPatch(row, column));
            } else if (range.length == 5) {
                int start = (int) range[2];
                int end = (int) range[3];
                int step = (int) range[4];
                for (int i = start; i <= end; i += step) {
                    patches.add(environment.getPatch(row, i));
                }
            }
        }

        WindowBlindsMapper.draw(glass, "GLASS", 7);
        WindowBlindsMapper.draw(southFacultyClosedBlinds, "CLOSED_SOUTH_FROM_OUTSIDE", 4);
        WindowBlindsMapper.draw(northSouthWindowBlinds, "OPENED_NORTH_AND_SOUTH", 4);
        WindowBlindsMapper.draw(eastWindowBlinds, "OPENED_EAST", 4);
        WindowBlindsMapper.draw(eastHallwayWindowBlinds, "OPENED_EAST", 5);
        WindowBlindsMapper.draw(westWindowBlinds, "OPENED_WEST", 4);


        /* CABINETS & DRAWERS + STORAGE */

        List<Patch> storage = new ArrayList<>(), southCabinet = new ArrayList<>(), southDrawers = new ArrayList<>(),
                    northCabinet1x2 = new ArrayList<>(), eastDoubleDrawers = new ArrayList<>(), westDoubleDrawers = new ArrayList<>();

        storage.add(environment.getPatch(85, 187));
        southCabinet.add(environment.getPatch(52, 147));
        southDrawers.add(environment.getPatch(50, 151));
        southDrawers.add(environment.getPatch(50, 152));
        northCabinet1x2.add(environment.getPatch(88, 129));
        eastDoubleDrawers.add(environment.getPatch(72, 162));
        westDoubleDrawers.add(environment.getPatch(72, 168));

        StorageMapper.draw(storage, "DOUBLE_DRAWERS", "EAST");
        CabinetDrawerMapper.draw(southCabinet, "CABINET", "SOUTH");
        CabinetDrawerMapper.draw(southDrawers, "DRAWERS", "SOUTH");
        CabinetDrawerMapper.draw(northCabinet1x2, "CABINET_1x2", "NORTH");
        CabinetDrawerMapper.draw(eastDoubleDrawers, "DOUBLE_DRAWERS", "EAST");
        CabinetDrawerMapper.draw(westDoubleDrawers, "DOUBLE_DRAWERS", "WEST");


        /* SERVER */

        List<Patch> serverTypeA = new ArrayList<>(), serverTypeB = new ArrayList<>();

        serverTypeA.add(environment.getPatch(43, 129));
        serverTypeB.add(environment.getPatch(47, 134));
        serverTypeB.add(environment.getPatch(47, 135));

        ServerMapper.draw(serverTypeA, "TYPE_A");
        ServerMapper.draw(serverTypeB, "TYPE_B");

    }


    public void initializeLayoutA(Environment environment) {
        GraphicsController.tileSize = backgroundCanvas.getHeight() / simulator.getEnvironment().getRows();
        mapLayoutA();
        simulator.spawnInitialAgents(environment);
        drawInterface();
    }

    public void mapLayoutA() {
        Environment environment = simulator.getEnvironment();

        /* Floors (for each zone inside office) */

        List<Patch>
                // consistent across all layouts
                hallway = new ArrayList<>(), elevatorLobby = new ArrayList<>(),
                maleBathroom = new ArrayList<>(), femaleBathroom = new ArrayList<>(),
                receptionA = new ArrayList<>(), breakerRoom = new ArrayList<>(), pantry = new ArrayList<>(),

                // small to big changes in other layouts
                directorRoom = new ArrayList<>(), directorBathroom = new ArrayList<>(),
                conferenceRoom = new ArrayList<>(), meetingRoom = new ArrayList<>(),
                dataCenter = new ArrayList<>(), mesa = new ArrayList<>(),
                SR1 = new ArrayList<>(), SR2 = new ArrayList<>(),  SR3 = new ArrayList<>(), SR4 = new ArrayList<>(),
                LS1 = new ArrayList<>(), LS2 = new ArrayList<>(),  LS3 = new ArrayList<>(), LS4 = new ArrayList<>(),
                researchCenter = new ArrayList<>(), facultySpace = new ArrayList<>(),
                humanExpRoom = new ArrayList<>(), dataCollectionRoom = new ArrayList<>(),
                storageRoom = new ArrayList<>(), clinic = new ArrayList<>();

        Object[][] floorRanges =  {

                // consistent across all layouts
                {elevatorLobby, 14, 64, 177, 192},
                {maleBathroom, 4, 13, 186, 202}, {maleBathroom, 14, 18, 191, 202},
                {femaleBathroom, 65, 74, 186, 202}, {femaleBathroom, 60, 64, 191, 202},
                {receptionA, 56, 75, 169, 183}, {breakerRoom, 18, 21, 22, 30}, {pantry, 111, 124, 135, 185},

                // small to big changes in other layouts
                {hallway, 18, 21, 1, 21}, {hallway, 22, 45, 18, 21}, {hallway, 60, 66, 1, 15},
                {hallway, 60, 86, 16, 22}, {hallway, 46, 59, 18, 32}, {hallway, 60, 72, 23, 32},
                {hallway, 56, 63, 51, 79}, {hallway, 56, 63, 98, 119}, {hallway, 46, 55, 33, 119},
                {hallway, 64, 72, 33, 108}, {hallway, 51, 59, 147, 152}, {hallway, 60, 79, 147, 168},
                {hallway, 80, 81, 147, 185}, {hallway, 82, 99, 147, 148}, {hallway, 100, 106, 147, 185},
                {hallway, 82, 99, 165, 185}, {hallway, 107, 110, 170, 185}, {hallway, 60, 72, 109, 146},
                {directorRoom, 26, 59, 1, 16}, {directorBathroom, 26, 36, 8, 16},
                {conferenceRoom, 26, 45, 23, 50}, {meetingRoom, 26, 45, 52, 70},
                {facultySpace, 26, 45, 72, 125}, {facultySpace, 38, 41, 126, 145},
                {researchCenter, 73, 106, 23, 145},
                {dataCenter, 46, 59, 121, 145}, {mesa, 82, 99, 149, 164},
                {SR1, 56, 63, 89, 97}, {SR2, 56, 63, 80, 88}, {SR3, 56, 63, 42, 50}, {SR4, 56, 63, 33, 41},
                {humanExpRoom, 67, 86, 1, 15}, {dataCollectionRoom, 87, 104, 1, 22},
                {storageRoom, 80, 100, 186, 202}, {clinic, 101, 113, 186, 202},

        };

        for (Object[] range : floorRanges) {
            List<Patch> floorPatches =  (List<Patch>) range[0];
            int startRow = (int) range[1];
            int endRow = (int) range[2];
            int startColumn = (int) range[3];
            int endColumn = (int) range[4];
            for (int i = startRow; i <= endRow; i ++) {
                for (int j = startColumn; j <= endColumn; j ++) {
                    floorPatches.add(environment.getPatch(i, j));
                }
            }
        }

        simulator.getEnvironment().getElevatorLobbies().add(ElevatorLobby.elevatorLobbyFactory.create(elevatorLobby, ""));
        simulator.getEnvironment().getFloors().add(Floor.floorFactory.create(hallway, "dimHallway"));
        simulator.getEnvironment().getBathrooms().add(Bathroom.bathroomFactory.create(maleBathroom, "maleBathroom"));
        simulator.getEnvironment().getBathrooms().add(Bathroom.bathroomFactory.create(femaleBathroom, "femaleBathroom"));
        simulator.getEnvironment().getBathrooms().add(Bathroom.bathroomFactory.create(directorBathroom, "dimDirectorBathroom"));
        simulator.getEnvironment().getBreakerRooms().add(BreakerRoom.breakerRoomFactory.create(breakerRoom, "dimBreakerRoom"));
        simulator.getEnvironment().getReceptions().add(Reception.receptionFactory.create(receptionA, "dimReception"));
        simulator.getEnvironment().getDirectorRooms().add(DirectorRoom.directorRoomFactory.create(directorRoom, "dimDirectorRoom"));
        simulator.getEnvironment().getConferenceRooms().add(ConferenceRoom.conferenceRoomFactory.create(conferenceRoom, "dimConferenceRoom"));
        simulator.getEnvironment().getMeetingRooms().add(MeetingRoom.meetingRoomFactory.create(meetingRoom, "dimMeetingRoom"));
        simulator.getEnvironment().getFacultyRooms().add(FacultyRoom.facultyRoomFactory.create(facultySpace, "dimFacultySpace"));
        simulator.getEnvironment().getDataCenters().add(DataCenter.dataCenterFactory.create(dataCenter, "dimDataCenter"));
        simulator.getEnvironment().getSoloRooms().add(SoloRoom.soloRoomFactory.create(SR1, "dimSR1"));
        simulator.getEnvironment().getSoloRooms().add(SoloRoom.soloRoomFactory.create(SR2, "dimSR2"));
        simulator.getEnvironment().getSoloRooms().add(SoloRoom.soloRoomFactory.create(SR3, "dimSR3"));
        simulator.getEnvironment().getSoloRooms().add(SoloRoom.soloRoomFactory.create(SR4, "dimSR4"));
        simulator.getEnvironment().getResearchCenters().add(ResearchCenter.researchCenterFactory.create(researchCenter, "dimResearchCenter"));
        simulator.getEnvironment().getHumanExpRooms().add(HumanExpRoom.humanExpRoomFactory.create(humanExpRoom, "dimHumExpRoom"));
        simulator.getEnvironment().getDataCollectionRooms().add(DataCollectionRoom.dataCollectionRoomFactory.create(dataCollectionRoom, "dimDataCollRoom"));
        simulator.getEnvironment().getStorageRooms().add(StorageRoom.storageRoomFactory.create(storageRoom, "dimStorageRoom"));
        simulator.getEnvironment().getClinics().add(Clinic.clinicFactory.create(clinic, "dimClinic"));
        simulator.getEnvironment().getMESAs().add(MESA.MESAFactory.create(mesa, "dimMESA"));
        simulator.getEnvironment().getPantries().add(Pantry.pantryFactory.create(pantry, "dimPantry"));

        /* Office Next Door */

        List<Patch> officeNextDoor = new ArrayList<>();

        for (int i = 0; i <= 55; i++) {
            int startColumn = 0;
            int endColumn = 183;

            if (i >= 14 && i <= 21) {
                startColumn = 32;
            } else if (i >= 22 && i <= 26) {
                startColumn = 127;
            } else if (i >= 27 && i <= 33) {
                startColumn = 127;
                endColumn = 175;
            } else if (i >= 34 && i <= 46) {
                startColumn = 147;
                endColumn = 175;
            } else if (i >= 47) {
                startColumn = 154;
                endColumn = 175;
            }

            for (int j = startColumn; j <= endColumn; j++) {
                officeNextDoor.add(environment.getPatch(i, j));
            }
        }

        simulator.getEnvironment().getDividers().add(Divider.dividerFactory.create(officeNextDoor, "officeNextDoor"));


        /* Parking Lot */

        List<Patch> parkingLot = new ArrayList<>();

        for (int i = 109; i <= 110; i++ ) {
            for (int j = 0; j <= 22; j++ ) {
                parkingLot.add(environment.getPatch(i, j));
            }
        }

        for (int i = 111; i <= 128; i++ ) {
            for (int j = 0; j <= 133; j++ ) {
                parkingLot.add(environment.getPatch(i, j));
            }
        }

        for (int i = 118; i <= 128; i++ ) {
            for (int j = 187; j <= 203; j++ ) {
                parkingLot.add(environment.getPatch(i, j));
            }
        }

        simulator.getEnvironment().getDividers().add(Divider.dividerFactory.create(parkingLot, "parkingLot"));


        /* Permanent Wall */

        List<Patch> permanentWalls = new ArrayList<>();

        int[][] permanentWallRanges = {

                // Male Bathroom
                {1, 186, 202}, {1, 186, 202}, {11, 185, 191}, {20, 191, 192},

                // Female Bathroom
                {57, 191, 202}, {66, 186, 191},

                // Office
                {15, 1, 30}, {23, 22, 125}, {35, 126, 145}, {48, 146, 152}, {57, 153, 176}, {77, 169, 202},
                {115, 187, 203}, {126, 134, 186}, {108, 23, 133}, {106, 0, 22}, {24, 177, 184},

                // Pillars
                {52, 136, 137}, {98, 31, 32}, {98, 53, 54}, {103, 84, 85}, {103, 102, 103}, {91, 131, 132},
                {91, 156, 157}, {110, 201, 202}, {118, 144, 145}, {118, 165, 166}

        };

        for (int[] range : permanentWallRanges) {
            int startRow = range[0];
            int endRow = startRow + 2;
            int startColumn = range[1];
            int endColumn = range[2];

            for (int i = startRow; i <= endRow; i ++) {
                for (int j = startColumn; j <= endColumn; j++) {
                    permanentWalls.add(environment.getPatch(i, j));
                }
            }
        }

        simulator.getEnvironment().getDividers().add(Divider.dividerFactory.create(permanentWalls, "permanentWall"));


        /* Permanent Wall Top */
        List<Patch> permanentWallTops = new ArrayList<>();

        int[][] horizontalRangesPWT = {

                // Office Outline
                {14, 0, 31}, {22, 22, 126}, {34, 126, 146}, {47, 146, 153}, {56, 153, 176}, {23, 176, 184},
                {56, 183, 184}, {76, 169, 203}, {114, 186, 203}, {125, 134, 186}, {107, 23, 134}, {105, 0, 23},

                // Male Bathroom
                {0, 185, 203}, {10, 185, 191}, {19, 191, 203},

                // Female Bathroom
                {56, 191, 203}, {65, 186, 191}, {75, 185, 203}
        };

        int[][] verticalRangesPWT = {

                // Office Outline
                {0, 15, 104}, {31, 15, 21}, {126, 23, 33}, {146, 35, 46}, {153, 48, 55}, {176, 24, 55},
                {184, 0, 22}, {184, 57, 75}, {169, 67, 75}, {203, 77, 113}, {186, 115, 124}, {134, 108, 124},
                {23, 106, 106},

                // Male Bathroom
                {185, 1, 9}, {203, 1, 18},

                // Female Bathroom
                {185, 56, 74}, {203, 57, 74}

        };

        int[][] pillarTops = {
                {50, 136, 1}, {96, 31, 1}, {96, 53, 1}, {101, 84, 1}, {101, 102, 1}, {88, 131, 2},
                {88, 156, 2}, {108, 201, 1}, {116, 144, 1}, {116, 165, 1}
        };


        // Elevator Area
        for (int i = 20; i <= 55; i++) {
            for (int j = 193; j <= 203; j++) {
                permanentWallTops.add(environment.getPatch(i, j));
            }
        }

        for (int[] range : horizontalRangesPWT) {
            int row = range[0];
            int startColumn = range[1];
            int endColumn = range[2];

            for (int j = startColumn; j <= endColumn; j++) {
                permanentWallTops.add(environment.getPatch(row, j));
            }
        }

        for (int[] range : verticalRangesPWT) {
            int column = range[0];
            int startRow = range[1];
            int endRow = range[2];

            for (int i = startRow; i <= endRow; i++) {
                permanentWallTops.add(environment.getPatch(i, column));
            }
        }

        for (int[] range : pillarTops) {
            int startRow = range[0];
            int endRow = startRow + range[2];
            int startColumn = range[1];
            int endColumn = startColumn + 1;

            for (int i = startRow; i <= endRow; i ++) {
                for (int j = startColumn; j <= endColumn; j++) {
                    permanentWallTops.add(environment.getPatch(i, j));
                }
            }
        }

        simulator.getEnvironment().getDividers().add(Divider.dividerFactory.create(permanentWallTops, "permanentWallTop"));


        /* Walls (inside office) */

        List<Patch> walls = new ArrayList<>();

        int[][] wallRanges = {

                // Director Room & Bathroom
                {23, 1, 17}, {34, 8, 16}, {57, 1, 2}, {57, 9, 17},

                // Conference, Meeting, & Faculty Room, and Data & Control Centers
                {43, 22, 23}, {43, 30, 43}, {43, 50, 52}, {43, 59, 92},
                {43, 99, 101}, {43, 108, 145}, {57, 120, 138}, {57, 145, 146},

                // Human Experiment & Data Collection Rooms, Research Center, Learning Spaces, & Pantry
                {68, 1, 15}, {88, 1, 8}, {88, 14, 16}, {88, 22, 23}, {97, 1, 6},
                {74, 23, 55}, {74, 61, 76}, {74, 82, 97}, {74, 103, 118}, {74, 124, 139}, {74, 145, 146},
                {108, 135, 169},

                // Clinic and Storage Room
                {98, 186, 202},
        };

        for (int[] range : wallRanges) {
            int startRow = range[0];
            int endRow = startRow + 2;
            int startColumn = range[1];
            int endColumn = range[2];

            for (int i = startRow; i <= endRow; i ++) {
                for (int j = startColumn; j <= endColumn; j++) {
                    walls.add(environment.getPatch(i, j));
                }
            }
        }

        simulator.getEnvironment().getDividers().add(Divider.dividerFactory.create(walls, "wall"));


        /* Door Patches (Column, Row) */

        List<Patch> doorPatches = new ArrayList<>();

        int[][] doorRanges = {

                /* Permanent */

                // Elevators
                {193, 23}, {193, 34}, {193, 45},
                // Male Bathroom
                {191, 11},
                // Female Bathroom
                {191, 57},
                // Breaker Room, Reception,
                {183, 57}, {169, 57}, {22, 15},

                
                /* Not Permanent (inside office) */

                {186, 77},              // Clinic
                {186, 98},              // Storage Room
                {8, 23},                // Director Bathroom
                {2, 57}, {9, 57},       // Director Room
                {52, 43}, {59, 43},     // Meeting Room
                {138, 57}, {138, 57},   // Data Center
                {15, 68},               // Human Experience Room
                // Data Collection Room
                {8, 88}, {14, 88}, {16, 88}, {22, 88}, {6, 97},
                // Conference Room
                {23, 43}, {30, 43}, {43, 43}, {50, 43},
                // Faculty Space
                {92, 43}, {99, 43}, {101, 43}, {108, 43}, {126, 35},
                // Student Space
                {23, 78}, {55, 74}, {61, 74},
                {76, 74}, {82, 74},
                {97, 74}, {103, 74},
                {118, 74}, {124, 74},
                {139, 74}, {145, 74}


        };

        for (int[] range : doorRanges) {
            int column = range[0];
            int startRow = range[1];
            int endRow = startRow + 2;


            for (int i = startRow; i <= endRow; i ++) {
                doorPatches.add(environment.getPatch(i, column));
            }
        }

        simulator.getEnvironment().getDividers().add(Divider.dividerFactory.create(doorPatches, "doorPatch"));


        /* Wall Tops (inside office) */

        List<Patch> wallTops = new ArrayList<>();

        int[][] horizontalRangesWT = {

                // Director Room & Bathroom
                {22, 1, 17}, {33, 8, 16}, {56, 1, 2}, {56, 9, 17},

                // Conference, Meeting, & Faculty Room, and Data & Control Centers
                {42, 22, 23}, {42, 30, 43}, {42, 50, 52}, {42, 59, 92},
                {42, 99, 101}, {42, 108, 145}, {56, 120, 138}, {56, 145, 146},

                // Human Experiment & Data Collection Rooms, Research Center, Learning Spaces, & Pantry
                {67, 1, 15}, {87, 1, 8}, {87, 14, 16}, {87, 22, 23}, {96, 1, 6}, {104, 6, 6},
                {73, 23, 55}, {73, 61, 76}, {73, 82, 97}, {73, 103, 118}, {73, 124, 139}, {73, 145, 146},
                {107, 135, 169},

                // Clinic and Storage Room
                {97, 186, 202},


        };

        int[][] verticalRangesWT = {

                // Director Room & Bathroom
                {8, 30, 32}, {17, 23, 55},

                // Conference, Meeting, & Faculty Room, and Data & Control Centers
                {22, 23, 41}, {51, 23, 41}, {71, 23, 41}, {120, 43, 55}, {146, 48, 55},

                // Human Experiment & Data Collection Rooms, Research Center, Learning Spaces, & Pantry
                {15, 77, 86}, {23, 74, 77}, {23, 86, 104}, {146, 74, 106},

                // Clinic and Storage Room
                {186, 85, 96}, {186, 107, 113},

        };

        for (int[] range : horizontalRangesWT) {
            int row = range[0];
            int startColumn = range[1];
            int endColumn = range[2];

            for (int j = startColumn; j <= endColumn; j++) {
                wallTops.add(environment.getPatch(row, j));
            }
        }

        for (int[] range : verticalRangesWT) {
            int column = range[0];
            int startRow = range[1];
            int endRow = range[2];

            for (int i = startRow; i <= endRow; i++) {
                wallTops.add(environment.getPatch(i, column));
            }
        }

        simulator.getEnvironment().getDividers().add(Divider.dividerFactory.create(wallTops, "wallTop"));



        /* Layout A Amenities */



        /* Elevators */

        List<Patch> elevator = new ArrayList<>();
        elevator.add(environment.getPatch(26,193));
        elevator.add(environment.getPatch(37,193));
        elevator.add(environment.getPatch(48,193));
        ElevatorMapper.draw(elevator, Elevator.ElevatorMode.ENTRANCE_AND_EXIT,  "VERTICAL");




        /* Cubicles */

        List<Patch> nwMESA = new ArrayList<>(), neMESA = new ArrayList<>(), swMESA = new ArrayList<>(), seMESA = new ArrayList<>(),
                    cubicleA  = new ArrayList<>(), westCubicleB  = new ArrayList<>(), eastCubicleB  = new ArrayList<>(),
                    westCubicleC  = new ArrayList<>(), eastCubicleC  = new ArrayList<>(),
                    southCubicleC  = new ArrayList<>(), northCubicleC  = new ArrayList<>();

        Object[][] cubicleRanges =  {

                // MESA
                {nwMESA, 84, 151}, {neMESA, 84, 159},
                {swMESA, 93, 151}, {seMESA, 93, 159},

                // Type A
                {cubicleA, 29, 105},

                // Type B
                {westCubicleB, 30, 77}, {westCubicleB, 34, 77},
                {westCubicleB, 30, 87}, {westCubicleB, 34, 87},
                {westCubicleB, 30, 97}, {westCubicleB, 34, 97},
                {eastCubicleB, 30, 80}, {eastCubicleB, 34, 80},
                {eastCubicleB, 30, 90}, {eastCubicleB, 34, 90},

                // Type C
                {westCubicleC, 29, 120}, {westCubicleC, 33, 120},
                {eastCubicleC, 29, 116}, {eastCubicleC, 33, 116},
                {northCubicleC, 59, 36}, {northCubicleC, 59, 92},
                {southCubicleC, 59, 45}, {southCubicleC, 59, 83},
        };


        for (Object[] range : cubicleRanges) {
            List<Patch> amenityPatches =  (List<Patch>) range[0];
            int row = (int) range[1];
            int column = (int) range[2];
            amenityPatches.add(environment.getPatch(row, column));
        }

        CubicleMapper.draw(nwMESA, "MESA", "", "NORTH_AND_WEST", false, 0);
        CubicleMapper.draw(neMESA, "MESA", "", "NORTH_AND_EAST", false, 0);
        CubicleMapper.draw(swMESA, "MESA", "", "SOUTH_AND_WEST", false, 0);
        CubicleMapper.draw(seMESA, "MESA", "", "SOUTH_AND_EAST", false, 0);
        CubicleMapper.draw(cubicleA, "TYPE_A", "", "", true, 1);
        CubicleMapper.draw(westCubicleB, "TYPE_B", "WEST", "", true, 2);
        CubicleMapper.draw(eastCubicleB, "TYPE_B", "EAST", "", true, 2);
        CubicleMapper.draw(westCubicleC, "TYPE_C", "WEST", "", false, 0);
        CubicleMapper.draw(eastCubicleC, "TYPE_C", "EAST", "", false, 0);
        CubicleMapper.draw(northCubicleC, "TYPE_C", "NORTH", "", false, 0);
        CubicleMapper.draw(southCubicleC, "TYPE_C", "SOUTH", "", false, 0);




        /* Tables & Chairs */

        List<Patch> table2x2 = new ArrayList<>(), receptionTable = new ArrayList<>(), directorTable = new ArrayList<>(),
                    humanExpTable = new ArrayList<>(), dataCollTable = new ArrayList<>(),
                    // Research Tables
                    westResearchTable = new ArrayList<>(), westMonitorResearchTable = new ArrayList<>(),
                    eastResearchTable = new ArrayList<>(), eastMonitorResearchTable = new ArrayList<>(),
                    // Learning Tables
                    learningTables = new ArrayList<>(),
                    // Meeting Tables
                    largeHorizontalMeetingTables = new ArrayList<>(), leftLargeHorizontalMeetingTables = new ArrayList<>(),
                    rightLargeHorizontalMeetingTables = new ArrayList<>(), smallVerticalMeetingTables = new ArrayList<>(),
                    // Pantry Tables & Chairs
                    typeApantryTables = new ArrayList<>(), typeBpantryTables = new ArrayList<>(),
                    typeApantryChairs = new ArrayList<>(), typeBpantryChairs = new ArrayList<>();

        table2x2.add(environment.getPatch(53,124));
        receptionTable.add(environment.getPatch(69,170));
        directorTable.add(environment.getPatch(40,12));
        humanExpTable.add(environment.getPatch(77,5));
        dataCollTable.add(environment.getPatch(91,2));

        // Research Tables
        Object[][] researchTableRanges =  {
                {westResearchTable, 79, 31}, {eastMonitorResearchTable, 79, 32},
                {westMonitorResearchTable, 90, 31}, {eastResearchTable, 90, 32},
                {westMonitorResearchTable, 102, 31}, {eastMonitorResearchTable, 102, 32},
                {westMonitorResearchTable, 79, 53}, {eastResearchTable, 79, 54},
                {westResearchTable, 90, 53}, {eastResearchTable, 90, 54},
                {westResearchTable, 102, 53}, {eastResearchTable, 102, 54},
        };

        for (Object[] range : researchTableRanges) {
            List<Patch> amenityPatches =  (List<Patch>) range[0];
            int row = (int) range[1];
            int column = (int) range[2];
            amenityPatches.add(environment.getPatch(row, column));
        }

        // Learning Tables
        Object[][] learningTableRanges =  {

                // Learning Space 1     // Learning Space 2     // Learning Space 3   // Learning Space 4
                {84, 129}, {84, 138},   {84, 108}, {84, 117},   {84, 87}, {84, 96},   {84, 66}, {84, 75},
                {98, 129}, {98, 138},   {98, 108}, {98, 117},   {98, 87}, {98, 96},   {98, 66}, {98, 75}

        };

        // Meeting Tables
        for (Object[] range : learningTableRanges) {
            int row = (int) range[0];
            int column = (int) range[1];
            learningTables.add(environment.getPatch(row, column));
        }

        Object[][] meetingTableRanges =  {

                // Conference Room
                {leftLargeHorizontalMeetingTables, 32, 28}, {rightLargeHorizontalMeetingTables, 32, 37},

                // Meeting Room
                {largeHorizontalMeetingTables, 32, 57},

                // Director Room
                {smallVerticalMeetingTables, 43, 5},
        };

        for (Object[] range : meetingTableRanges) {
            List<Patch> amenityPatches =  (List<Patch>) range[0];
            int row = (int) range[1];
            int column = (int) range[2];
            amenityPatches.add(environment.getPatch(row, column));
        }

        // Pantry Tables & Chairs
        Object[][] pantryTableRanges =  {

                // Type A
                {typeApantryTables, 117, 180}, {typeApantryTables, 120, 175},

                // Type B
                {typeBpantryTables, 117, 169}, {typeBpantryTables, 121, 139}, {typeBpantryTables,  121, 148},
                {typeBpantryTables, 121, 154}, {typeBpantryTables, 121, 160}
        };

        for (Object[] range : pantryTableRanges) {
            List<Patch> amenityPatches =  (List<Patch>) range[0];
            int row = (int) range[1];
            int column = (int) range[2];
            amenityPatches.add(environment.getPatch(row, column));
        }

        Object[][] pantryChairRanges =  {

                // Type A
                {typeApantryChairs, 111, 150}, {typeApantryChairs, 111, 151}, {typeApantryChairs, 111, 152},
                {typeApantryChairs, 111, 153}, {typeApantryChairs, 111, 154},

                // Type B
                {typeBpantryChairs, 111, 145}, {typeBpantryChairs, 111, 146}, {typeBpantryChairs, 111, 147},
                {typeBpantryChairs, 111, 148}, {typeBpantryChairs, 111, 149}
        };

        for (Object[] range : pantryChairRanges) {
            List<Patch> amenityPatches =  (List<Patch>) range[0];
            int row = (int) range[1];
            int column = (int) range[2];
            amenityPatches.add(environment.getPatch(row, column));
        }

        Table2x2Mapper.draw(table2x2);
        ReceptionTableMapper.draw(receptionTable, "1x8");
        DirectorTableMapper.draw(directorTable, "HORIZONTAL", "NORTH", true);
        HumanExpTableMapper.draw(humanExpTable, "5x1");
        DataCollTableMapper.draw(dataCollTable, "1x6");
        // Research Table
        ResearchTableMapper.draw(westResearchTable, "WEST", false);
        ResearchTableMapper.draw(eastResearchTable, "EAST", false);
        ResearchTableMapper.draw(westMonitorResearchTable, "WEST", true);
        ResearchTableMapper.draw(eastMonitorResearchTable, "EAST", true);
        // Learning Table
        LearningTableMapper.draw(learningTables, "HORIZONTAL");
        // Meeting Tables
        MeetingTableMapper.draw(largeHorizontalMeetingTables, "HORIZONTAL", "LARGE", "");
        MeetingTableMapper.draw(leftLargeHorizontalMeetingTables, "HORIZONTAL", "LARGE", "LEFT");
        MeetingTableMapper.draw(rightLargeHorizontalMeetingTables, "HORIZONTAL", "LARGE", "RIGHT");
        MeetingTableMapper.draw(smallVerticalMeetingTables, "VERTICAL", "SMALL", "");
        // Pantry Tables and Chairs
        PantryTableMapper.draw(typeApantryTables, "TYPE_A");
        PantryTableMapper.draw(typeBpantryTables, "TYPE_B");
        ChairMapper.draw(typeApantryChairs, 0, "SOUTH","PANTRY_TYPE_A", "");
        ChairMapper.draw(typeBpantryChairs, 0, "SOUTH","PANTRY_TYPE_B", "");




        /* Plants, Trash Cans, Pantry Cabinets, Coffee Maker Bar, Kettle Bar, Microwave Bar, Water Dispenser, Refrigerator, Couch */

        List<Patch> plants = new ArrayList<>(), trashCans = new ArrayList<>(), pantryCabinets = new ArrayList<>(),
                coffeeMakerBar = new ArrayList<>(), kettleBar = new ArrayList<>(), microwaveBar = new ArrayList<>(),
                refrigerator = new ArrayList<>(), waterDispenser = new ArrayList<>(), couch = new ArrayList<>();

        Object[][] simpleAmenityRanges =  {

                // Plants
                {plants, 63, 2},
                {plants, 74, 179}, {plants, 74, 181}, {plants, 75, 180}, // Reception Area
                {plants, 32, 9}, {plants, 32, 16},

                // Trash Cans
                {trashCans, 26, 13}, {trashCans, 40, 11}, // director room & bathroom
                {trashCans, 113, 135}, // pantry

                // Pantry Cabinets
                {pantryCabinets, 109, 135}, {pantryCabinets, 109, 136}, {pantryCabinets, 109, 137}, {pantryCabinets, 109, 138},

                // Coffee Maker Bar
                {coffeeMakerBar, 110, 137},

                // Kettle Bar
                {kettleBar, 110, 138},

                // Microwave Bar
                {microwaveBar, 111, 135},

                // Refrigerator
                {refrigerator, 110, 141},

                // Water Dispenser
                {waterDispenser, 110, 140},

                // Couch
                {couch, 69, 167}
        };

        for (Object[] range : simpleAmenityRanges) {
            List<Patch> amenityPatches =  (List<Patch>) range[0];
            int row = (int) range[1];
            int column = (int) range[2];
            amenityPatches.add(environment.getPatch(row, column));
        }

        PlantMapper.draw(plants);
        TrashCanMapper.draw(trashCans);
        PantryCabinetMapper.draw(pantryCabinets);
        CoffeeMakerBarMapper.draw(coffeeMakerBar);
        KettleBarMapper.draw(kettleBar);
        MicrowaveBarMapper.draw(microwaveBar);
        WaterDispenserMapper.draw(waterDispenser);
        RefrigeratorMapper.draw(refrigerator);
        CouchMapper.draw(couch, "WEST");




        /* Sinks & Toilets */

        List<Patch> southSinks = new ArrayList<>(), southOfficeSinks = new ArrayList<>(), northSinks = new ArrayList<>(),
                southToilets = new ArrayList<>(), southOfficeToilets = new ArrayList<>(), northToilets  = new ArrayList<>();

        Object[][] sinkToiletRanges =  {

                // Male Bathroom
                {southToilets, 3, 188}, {southToilets, 3, 191},
                {southToilets, 3, 194}, {southToilets, 3, 197},
                {southToilets, 3, 200}, {northSinks, 18, 194},
                {northSinks, 18, 197}, {northSinks, 18, 200},

                // Female Bathroom
                {northToilets, 73, 188}, {northToilets, 73, 191},
                {northToilets, 73, 194}, {northToilets, 73, 197},
                {northToilets, 73, 200}, {southSinks,  60, 194},
                {southSinks, 60, 197}, {southSinks, 60, 200},

                // Director Bathroom
                {southOfficeToilets, 25, 15}, {southOfficeSinks, 26, 12},

                // Pantry
                {southOfficeSinks, 111, 136},
        };

        for (Object[] range : sinkToiletRanges) {
            List<Patch> amenityPatches =  (List<Patch>) range[0];
            int row = (int) range[1];
            int column = (int) range[2];
            amenityPatches.add(environment.getPatch(row, column));
        }

        SinkMapper.draw(southSinks, "SOUTH", "SINK");
        SinkMapper.draw(southOfficeSinks, "SOUTH", "OFFICE_SINK");
        SinkMapper.draw(northSinks, "NORTH", "SINK");
        ToiletMapper.draw(southToilets, "SOUTH", "TOILET");
        ToiletMapper.draw(southOfficeToilets, "SOUTH", "OFFICE_TOILET");
        ToiletMapper.draw(northToilets, "NORTH", "TOILET");

        /* Switches */

        List<Patch> southLightSwitches = new ArrayList<>(), northLightSwitches = new ArrayList<>(), westLightSwitches = new ArrayList<>(), eastLightSwitches = new ArrayList<>(),
                southAirconSwitches = new ArrayList<>(), northAirconSwitches = new ArrayList<>(), westAirconSwitches = new ArrayList<>(), eastAirconSwitches = new ArrayList<>();

        Object[][] switchRanges =  {

                // Director Room
                {eastLightSwitches, 53, 1}, {eastAirconSwitches, 54, 1},

                // Director Bathroom
                {southLightSwitches, 24, 10},

                // Conference Room
                {eastLightSwitches, 39, 23}, {eastAirconSwitches, 40, 23},

                // Meeting Room
                {eastLightSwitches, 39, 52}, {eastAirconSwitches, 40, 52},

                // Faculty Space
                {eastLightSwitches, 39, 72}, {eastAirconSwitches, 40, 72},
                {westLightSwitches, 32, 125}, {westAirconSwitches, 33, 125},

                // Data Center
                {westLightSwitches, 53, 145}, {westAirconSwitches, 54, 145},

                // Hallway
                {eastLightSwitches, 57, 147}, {eastAirconSwitches, 58, 147},

                // Reception
                {southLightSwitches, 58, 174}, {southAirconSwitches, 58, 175},

                // MESA
                {southLightSwitches, 92, 156}, {southAirconSwitches, 92, 157},

                // Clinic
                {southLightSwitches, 78, 188}, {southAirconSwitches, 78, 189},

                // Storage Room
                {southLightSwitches, 99, 188},

                // Pantry
                {westLightSwitches, 112, 185}, {southAirconSwitches, 109, 168},

                // Human Experience  Room
                {southLightSwitches, 69, 12}, {southAirconSwitches, 69, 13},

                // Data Collection Room
                {northLightSwitches, 95, 5}, {northAirconSwitches, 95, 6},

                // Student Space
                {southLightSwitches, 75, 25}, {southAirconSwitches, 75, 26},
                {southLightSwitches, 75, 63}, {southAirconSwitches, 75, 64},
                {southLightSwitches, 75, 84}, {southAirconSwitches, 75, 85},
                {southLightSwitches, 75, 105}, {southAirconSwitches, 75, 106},
                {southLightSwitches, 75, 126}, {southAirconSwitches, 75, 127},

                // Solo Rooms
                {westLightSwitches, 60, 35}, {eastLightSwitches, 59, 48},
                {westLightSwitches, 59, 86}, {eastLightSwitches, 59, 86},


        };

        for (Object[] range : switchRanges) {
            List<Patch> amenityPatches =  (List<Patch>) range[0];
            int row = (int) range[1];
            int column = (int) range[2];
            amenityPatches.add(environment.getPatch(row, column));
        }

        SwitchMapper.draw(southLightSwitches, "LIGHT", "SOUTH");
        SwitchMapper.draw(northLightSwitches, "LIGHT", "NORTH");
        SwitchMapper.draw(westLightSwitches, "LIGHT", "WEST");
        SwitchMapper.draw(eastLightSwitches, "LIGHT", "EAST");
        SwitchMapper.draw(southAirconSwitches, "AC", "SOUTH");
        SwitchMapper.draw(northAirconSwitches, "AC", "NORTH");
        SwitchMapper.draw(westAirconSwitches, "AC", "WEST");
        SwitchMapper.draw(eastAirconSwitches, "AC", "EAST");


        /* Aircons */

        List<Patch> aircons = new ArrayList<>();

        Object[][] airconRanges =  {

                // Meeting Room
                {32, 63},

                // Learning Spaces
                {81, 65}, {103, 75},
                {81, 88}, {81, 96}, {104, 95},
                {84, 110}, {104, 118},
                {78, 138}, {98, 131},

                // Data Center
                {47, 128}, {47, 137},

                // Hallway
                {59, 22}, {59, 62}, {59, 99}, {59, 111},
                {65, 130}, {69, 156},

                // Reception
                {66, 179},

                // Human Experience Room
                {72, 6},

                // Data Collection Room
                {93, 9},

                // Research Center
                {92, 34}, {92, 40}, {97, 71}, {85, 88},

                // Faculty Space
                {30, 73}, {30, 84}, {35, 84}, {37, 105}, {37, 120},

                // Conference Room
                {37, 30}, {37, 42},

                // Clinic
                {82, 189},

                // Director Room
                {50, 8},

                // Pantry
                {120, 151},
        };

        for (Object[] range : airconRanges) {
            int row = (int) range[0];
            int column = (int) range[1];
            aircons.add(environment.getPatch(row, column));
        }

        AirconMapper.draw(aircons, false);


        /* Lights */

        List<Patch> singlePendantLights = new ArrayList<>(), singleRecessedLights = new ArrayList<>(), horizontalPendantLights = new ArrayList<>(), verticalPendantLights = new ArrayList<>(),
                horizontalRecessedLights = new ArrayList<>(), verticalRecessedLights = new ArrayList<>(), horizontalTrackLights = new ArrayList<>(), verticalTrackLights = new ArrayList<>();

        Object[][] lightRanges =  {

                // Meeting Room
                {horizontalTrackLights, 27, 57}, {horizontalTrackLights, 27, 62},
                {verticalRecessedLights, 30, 54}, {verticalRecessedLights, 35, 54},
                {verticalRecessedLights, 30, 68}, {verticalRecessedLights, 35, 68},

                // Research Center
                {verticalRecessedLights, 78, 27}, {verticalRecessedLights, 83, 27},
                {verticalRecessedLights, 88, 27}, {verticalRecessedLights, 93, 27},
                {verticalRecessedLights, 98, 27}, {verticalRecessedLights, 103, 27},
                {verticalRecessedLights, 98, 42}, {verticalRecessedLights, 93, 42},
                {verticalRecessedLights, 88, 42}, {verticalRecessedLights, 83, 42},
                {verticalRecessedLights, 78, 42},
                {verticalRecessedLights, 78, 58}, {verticalRecessedLights, 83, 58},
                {verticalRecessedLights, 88, 58}, {verticalRecessedLights, 93, 58},
                {verticalRecessedLights, 98, 58}, {verticalRecessedLights, 103, 58},
                {verticalPendantLights, 79, 34}, {verticalPendantLights, 81, 34},
                {verticalPendantLights, 90, 34}, {verticalPendantLights, 92, 34},
                {verticalPendantLights, 102, 34}, {verticalPendantLights, 104, 34},
                {verticalPendantLights, 79, 51}, {verticalPendantLights, 81, 51},
                {verticalPendantLights, 90, 51}, {verticalPendantLights, 92, 51},
                {verticalPendantLights, 102, 51}, {verticalPendantLights, 104, 51},
                {singlePendantLights, 97, 25}, {singlePendantLights, 102, 25},
                {singlePendantLights, 83, 60}, {singlePendantLights, 88, 60},
                {singlePendantLights, 93, 60}, {singlePendantLights, 98, 60},

                // Learning Space 4
                {horizontalTrackLights, 78, 67}, {horizontalTrackLights, 89, 67},
                {horizontalTrackLights, 89, 75}, {horizontalTrackLights, 105, 67},
                {verticalRecessedLights, 81, 73}, {verticalRecessedLights, 103, 65},
                {verticalRecessedLights, 103, 80},

                // Learning Space 3
                {horizontalTrackLights, 78, 92}, {horizontalTrackLights, 89, 88},
                {horizontalTrackLights, 89, 96}, {horizontalTrackLights, 105, 89},
                {verticalRecessedLights, 79, 94}, {verticalRecessedLights, 103, 86},
                {verticalRecessedLights, 103, 98},

                // Learning Space 2
                {horizontalTrackLights, 78, 113}, {horizontalTrackLights, 89, 109},
                {horizontalTrackLights, 89, 117}, {horizontalTrackLights, 105, 109},
                {verticalRecessedLights, 79, 107}, {verticalRecessedLights, 104, 107},
                {verticalRecessedLights, 79, 115}, {verticalRecessedLights, 104, 115},

                // Learning Space 1
                {horizontalTrackLights, 78, 130}, {horizontalTrackLights, 94, 130},
                {horizontalTrackLights, 94, 138}, {horizontalTrackLights, 105, 134},
                {verticalRecessedLights, 78, 128}, {verticalRecessedLights, 78, 136},
                {verticalRecessedLights, 103, 128}, {verticalRecessedLights, 103, 128},

                // Faculty Space
                {horizontalTrackLights, 39, 83}, {horizontalTrackLights, 39, 93},
                {horizontalTrackLights, 27, 86},
                {verticalRecessedLights, 27, 79}, {verticalRecessedLights, 34, 79},
                {verticalRecessedLights, 92, 114}, {verticalRecessedLights, 99, 102},
                {verticalRecessedLights, 99, 107}, {verticalRecessedLights, 27, 113},
                {verticalRecessedLights, 27, 103}, {verticalRecessedLights, 39, 103},
                {verticalRecessedLights, 27, 123}, {verticalRecessedLights, 39, 123},
                {horizontalRecessedLights, 29, 98}, {horizontalRecessedLights, 38, 98},

                {horizontalPendantLights, 33, 83}, {horizontalPendantLights, 33, 85},
                {horizontalPendantLights, 34, 83}, {horizontalPendantLights, 34, 85},

                {verticalRecessedLights, 39, 109}, {verticalRecessedLights, 39, 116},
                {verticalRecessedLights, 39, 123}, 

                // Data Center
                {verticalRecessedLights, 47, 124}, {verticalRecessedLights, 47, 133},
                {verticalRecessedLights, 47, 142},

                // Hallway
                {verticalPendantLights, 59, 28},
                {horizontalPendantLights, 60, 55}, {horizontalPendantLights, 60, 104},
                {singlePendantLights, 60, 71}, {singlePendantLights, 60, 102},


                {verticalRecessedLights, 31, 19}, {verticalRecessedLights, 49, 19},
                {verticalRecessedLights, 62, 4}, {verticalRecessedLights, 62, 16},

                {verticalRecessedLights, 54, 149}, {verticalRecessedLights, 62, 149},
                {verticalRecessedLights, 76, 149}, {verticalRecessedLights, 104, 149},

                {verticalRecessedLights, 62, 157}, {verticalRecessedLights, 62, 164},
                {verticalRecessedLights, 76, 164}, {verticalRecessedLights, 104, 164},

                {verticalRecessedLights, 83, 171}, {verticalRecessedLights, 83, 179},
                {verticalRecessedLights, 90, 171}, {verticalRecessedLights, 90, 179},
                {verticalRecessedLights, 97, 171}, {verticalRecessedLights, 97, 179},

                {horizontalRecessedLights, 69, 22}, {horizontalRecessedLights, 49, 22},
                {horizontalRecessedLights, 69, 30}, {horizontalRecessedLights, 49, 30},
                {horizontalRecessedLights, 69, 38}, {horizontalRecessedLights, 49, 38},
                {horizontalRecessedLights, 69, 46}, {horizontalRecessedLights, 49, 46},
                {horizontalRecessedLights, 69, 54}, {horizontalRecessedLights, 49, 54},
                {horizontalRecessedLights, 69, 62}, {horizontalRecessedLights, 49, 62},
                {horizontalRecessedLights, 69, 70}, {horizontalRecessedLights, 49, 70},
                {horizontalRecessedLights, 69, 78}, {horizontalRecessedLights, 49, 78},
                {horizontalRecessedLights, 69, 86}, {horizontalRecessedLights, 49, 86},
                {horizontalRecessedLights, 69, 94}, {horizontalRecessedLights, 49, 94},
                {horizontalRecessedLights, 69, 102}, {horizontalRecessedLights, 49, 102},
                {horizontalRecessedLights, 69, 110}, {horizontalRecessedLights, 49, 110},

                {horizontalRecessedLights, 63, 118}, {horizontalRecessedLights, 69, 118},
                {horizontalRecessedLights, 63, 126}, {horizontalRecessedLights, 69, 126},
                {horizontalRecessedLights, 63, 134}, {horizontalRecessedLights, 69, 134},
                {horizontalRecessedLights, 63, 142}, {horizontalRecessedLights, 69, 142},


                // Reception
                {verticalRecessedLights, 62, 172}, {verticalRecessedLights, 62, 180},
                {verticalRecessedLights, 71, 172}, {verticalRecessedLights, 71, 180},
                {verticalTrackLights, 66, 182},

                // MESA
                {verticalRecessedLights, 90, 149}, {verticalRecessedLights, 90, 164},
                {verticalPendantLights, 87, 150}, {verticalPendantLights, 87, 163},
                {verticalPendantLights, 93, 150}, {verticalPendantLights, 93, 163},

                // Human Experience Room
                {verticalRecessedLights, 78, 6}, {verticalRecessedLights, 78, 10},

                // Data Collection Room
                {verticalRecessedLights, 93, 5}, {verticalRecessedLights, 93, 19},

                // Storage Room
                {verticalRecessedLights, 103, 194}, {verticalRecessedLights, 111, 194},

                // Conference Room
                {verticalTrackLights, 30, 25}, {verticalTrackLights, 30, 48},
                {verticalRecessedLights, 36, 28}, {verticalRecessedLights, 36, 39},
                {horizontalPendantLights, 33, 34}, {horizontalPendantLights, 33, 36},
                {horizontalPendantLights, 33, 38},

                // Clinic
                {verticalRecessedLights, 81, 194}, {verticalRecessedLights, 93, 194},

                // Director Room
                {verticalRecessedLights, 27, 4}, {verticalRecessedLights, 36, 4},
                {verticalRecessedLights, 45, 2}, {verticalRecessedLights, 53, 4},
                {verticalRecessedLights, 45, 13}, {horizontalTrackLights, 40, 3},
                {horizontalPendantLights, 41, 13}, {singlePendantLights, 48, 5},

                // Director Bathroom
                {singlePendantLights, 30, 10}, {singlePendantLights, 30, 15},

                // Pantry
                {singlePendantLights, 114, 184}, {singlePendantLights, 122, 184},
                {singlePendantLights, 114, 137}, {singlePendantLights, 118, 137},
                {singlePendantLights, 122, 137}, {horizontalRecessedLights, 114, 146},
                {horizontalRecessedLights, 114, 158}, {horizontalRecessedLights, 114, 170},
                {horizontalRecessedLights, 123, 161}, {horizontalRecessedLights, 123, 170},
                {verticalTrackLights, 115, 149}, {verticalTrackLights, 115, 155},
                {verticalTrackLights, 115, 167}, {verticalTrackLights, 115, 178},
                {horizontalTrackLights, 113, 136}, {horizontalTrackLights, 118, 151},
                {verticalRecessedLights, 122, 138}, {verticalRecessedLights, 122, 143},

                // Solo Rooms
                {singleRecessedLights, 62, 37}, {singleRecessedLights, 57, 46},
                {singleRecessedLights, 57, 84}, {singleRecessedLights, 62, 93},


        };

        for (Object[] range : lightRanges) {
            List<Patch> amenityPatches = (List<Patch>) range[0];
            int row = (int) range[1];
            int column = (int) range[2];
            amenityPatches.add(environment.getPatch(row, column));
        }

        LightMapper.draw(singlePendantLights, "SINGLE_PENDANT_LIGHT", "");
        LightMapper.draw(singleRecessedLights, "SINGLE_RECESSED_LIGHT", "");
        LightMapper.draw(horizontalPendantLights, "LINEAR_PENDANT_LIGHT", "HORIZONTAL");
        LightMapper.draw(verticalPendantLights, "LINEAR_PENDANT_LIGHT", "VERTICAL");
        LightMapper.draw(horizontalRecessedLights, "RECESSED_LINEAR_LIGHT", "HORIZONTAL");
        LightMapper.draw(verticalRecessedLights, "RECESSED_LINEAR_LIGHT", "VERTICAL");
        LightMapper.draw(horizontalTrackLights, "TRACK_LIGHT", "HORIZONTAL");
        LightMapper.draw(verticalTrackLights, "TRACK_LIGHT", "VERTICAL");


        /* WINDOW + BLINDS */
        List<Patch> glass = new ArrayList<>(), southFacultyClosedBlinds = new ArrayList<>(), northSouthWindowBlinds_3 = new ArrayList<>(), southFacultyClosedBlinds_3 = new ArrayList<>(),
                eastHallwayWindowBlinds = new ArrayList<>(), eastWindowBlinds = new ArrayList<>(), northSouthWindowBlinds = new ArrayList<>(),
                westWindowBlinds_4 = new ArrayList<>(), westWindowBlinds_5 = new ArrayList<>();

        Object[][] windowBlindsRanges = {
                // Hallway
                {eastHallwayWindowBlinds, 61, 1},

                // Human Experience Room
                {eastWindowBlinds, 72, 1},
                {eastWindowBlinds, 77, 1},
                {eastWindowBlinds, 82, 1},

                // Data Collection Room
                {glass, 88, 1},
                {eastWindowBlinds, 91, 1},
                {northSouthWindowBlinds, 104, 8, 21, 5},

                // Student Space
                {northSouthWindowBlinds, 106, 24, 63, 6},
                {northSouthWindowBlinds_3, 106, 71, 73, 3},
                {northSouthWindowBlinds, 106, 81, 84, 4},
                {northSouthWindowBlinds_3, 106, 92, 94, 3},
                {southFacultyClosedBlinds, 108, 102, 105, 4},
                {southFacultyClosedBlinds_3, 108, 113, 115, 3},
                {southFacultyClosedBlinds_3, 108, 123, 125, 3},

                // Pantry Room
                {eastWindowBlinds, 115, 135},
                {eastWindowBlinds, 120, 135},
                {northSouthWindowBlinds, 124, 136, 184, 5},

                // Clinic
                {westWindowBlinds_4, 81, 202},
                {westWindowBlinds_4, 86, 202},
                {westWindowBlinds_4, 92, 202},

                // Storage Room
                {westWindowBlinds_5, 102, 202},
                {northSouthWindowBlinds, 113, 188, 201, 5},
        };

        for (Object[] range : windowBlindsRanges) {
            List<Patch> patches = (List<Patch>) range[0];
            int row = (int) range[1];

            if (range.length == 3) {
                int column = (int) range[2];
                patches.add(environment.getPatch(row, column));
            } else if (range.length == 5) {
                int start = (int) range[2];
                int end = (int) range[3];
                int step = (int) range[4];
                for (int i = start; i <= end; i += step) {
                    patches.add(environment.getPatch(row, i));
                }
            }
        }

        WindowBlindsMapper.draw(glass, "GLASS", 7);
        WindowBlindsMapper.draw(southFacultyClosedBlinds, "CLOSED_SOUTH_FROM_OUTSIDE", 4);
        WindowBlindsMapper.draw(southFacultyClosedBlinds_3, "CLOSED_SOUTH_FROM_OUTSIDE", 3);
        WindowBlindsMapper.draw(northSouthWindowBlinds_3, "OPENED_NORTH_AND_SOUTH", 3);
        WindowBlindsMapper.draw(northSouthWindowBlinds, "OPENED_NORTH_AND_SOUTH", 4);
        WindowBlindsMapper.draw(eastWindowBlinds, "OPENED_EAST", 4);
        WindowBlindsMapper.draw(eastHallwayWindowBlinds, "OPENED_EAST", 5);
        WindowBlindsMapper.draw(westWindowBlinds_4, "OPENED_WEST", 4);
        WindowBlindsMapper.draw(westWindowBlinds_5, "OPENED_WEST", 5);


        /* CABINETS & DRAWERS + STORAGE */

        List<Patch> storage = new ArrayList<>(), southCabinet = new ArrayList<>(), southDrawers = new ArrayList<>(),
                northCabinet1x2 = new ArrayList<>(), eastDoubleDrawers = new ArrayList<>(), westDoubleDrawers = new ArrayList<>();

        storage.add(environment.getPatch(94, 187));
        southCabinet.add(environment.getPatch(52, 147));
        southDrawers.add(environment.getPatch(50, 151));
        southDrawers.add(environment.getPatch(50, 152));
        northCabinet1x2.add(environment.getPatch(88, 129));
        westDoubleDrawers.add(environment.getPatch(87, 185));
        westDoubleDrawers.add(environment.getPatch(92, 185));

        StorageMapper.draw(storage, "DOUBLE_DRAWERS", "EAST");
        CabinetDrawerMapper.draw(southCabinet, "CABINET", "SOUTH");
        CabinetDrawerMapper.draw(southDrawers, "DRAWERS", "SOUTH");
        CabinetDrawerMapper.draw(northCabinet1x2, "CABINET_1x2", "NORTH");
        CabinetDrawerMapper.draw(eastDoubleDrawers, "DOUBLE_DRAWERS", "EAST");
        CabinetDrawerMapper.draw(westDoubleDrawers, "DOUBLE_DRAWERS", "WEST");


        /* SERVER */

        List<Patch> serverTypeA = new ArrayList<>(), serverTypeB = new ArrayList<>();

        serverTypeA.add(environment.getPatch(53, 129));
        serverTypeB.add(environment.getPatch(52, 133));
        serverTypeB.add(environment.getPatch(52, 134));

        ServerMapper.draw(serverTypeA, "TYPE_A");
        ServerMapper.draw(serverTypeB, "TYPE_B");


        /* White Board */

        List<Patch> northWhiteBoard_2 = new ArrayList<>(), eastWhiteBoard_4 = new ArrayList<>(), westWhiteBoard = new ArrayList<>(),
                    northWhiteBoard_5 = new ArrayList<>(), southWhiteBoard = new ArrayList<>();

        Object[][] whiteBoardRanges =  {
                // Research Center Pillars
                {westWhiteBoard, 96, 30}, {northWhiteBoard_2, 95, 31}, {eastWhiteBoard_4, 96, 33},
                {westWhiteBoard, 96, 52}, {northWhiteBoard_2, 95, 53}, {eastWhiteBoard_4, 96, 55},

                // Meeting Room
                {southWhiteBoard, 23, 59},

                // Learning Spaces
                {northWhiteBoard_5, 106, 65}, {northWhiteBoard_5, 106, 75},
                {northWhiteBoard_5, 106, 86}, {northWhiteBoard_5, 106, 96},
                {northWhiteBoard_5, 106, 107}, {northWhiteBoard_5, 106, 117},
                {northWhiteBoard_5, 106, 128}, {northWhiteBoard_5, 106, 138},
        };

        for (Object[] range : whiteBoardRanges) {
            List<Patch> amenityPatches =  (List<Patch>) range[0];
            int row = (int)  range[1];
            int column = (int) range[2];
            amenityPatches.add(environment.getPatch(row, column));
        }

        WhiteboardMapper.draw(northWhiteBoard_2, "NORTH", "2");
        WhiteboardMapper.draw(southWhiteBoard, "SOUTH", "5");
        WhiteboardMapper.draw(westWhiteBoard, "WEST", "4");
        WhiteboardMapper.draw(eastWhiteBoard_4, "EAST", "4");
        WhiteboardMapper.draw(northWhiteBoard_5, "NORTH", "5");


    }


    public void initializeLayoutB(Environment environment) {
        GraphicsController.tileSize = backgroundCanvas.getHeight() / simulator.getEnvironment().getRows();
        mapLayoutB();
        simulator.spawnInitialAgents(environment);
        drawInterface();
    }

    public void mapLayoutB() {
        Environment environment = simulator.getEnvironment();

        /* Floors (for each zone inside office) */

        List<Patch>
                // consistent across all layouts
                hallway = new ArrayList<>(), elevatorLobby = new ArrayList<>(),
                maleBathroom = new ArrayList<>(), femaleBathroom = new ArrayList<>(),
                reception = new ArrayList<>(), breakerRoom = new ArrayList<>(), pantry = new ArrayList<>(),

                // small to big changes in other layouts
                directorRoom = new ArrayList<>(), directorBathroom = new ArrayList<>(),
                conferenceRoom = new ArrayList<>(), meetingRoom = new ArrayList<>(),
                dataCenter = new ArrayList<>(), dataCenterCCTV = new ArrayList<>(),
                controlCenter = new ArrayList<>(), mesa = new ArrayList<>(),
                SR1 = new ArrayList<>(), SR2 = new ArrayList<>(),  SR3 = new ArrayList<>(), SR4 = new ArrayList<>(),
                LS1 = new ArrayList<>(), LS2 = new ArrayList<>(),  LS3 = new ArrayList<>(), LS4 = new ArrayList<>(),
                researchCenter = new ArrayList<>(), facultyRoom = new ArrayList<>(),
                humanExpRoom = new ArrayList<>(), dataCollectionRoom = new ArrayList<>(),
                storageRoom = new ArrayList<>(), clinic = new ArrayList<>();

        Object[][] floorRanges =  {
                // consistent across all layouts
                {elevatorLobby, 14, 64, 177, 192},
                {hallway, 18, 21, 1, 21}, {hallway, 22, 59, 17, 21}, {hallway, 51, 59, 147, 152},
                {hallway, 60, 66, 1, 168}, {hallway, 67, 79, 16, 37}, {hallway, 67, 79, 57, 83},
                {hallway, 67, 79, 103, 142}, {hallway, 67, 79, 162, 168},
                {hallway, 80, 86, 16, 128}, {hallway, 80, 87, 129, 132}, {hallway, 80, 88, 133, 142},
                {hallway, 81, 88, 143, 161}, {hallway, 80, 88, 162, 185}, {hallway, 89, 110, 170, 185},
                {maleBathroom, 4, 13, 186, 202}, {maleBathroom, 14, 18, 191, 202},
                {femaleBathroom, 65, 74, 186, 202}, {femaleBathroom, 60, 64, 191, 202},
                {reception, 56, 75, 169, 183}, {breakerRoom, 18, 21, 22, 30}, {pantry, 111, 124, 135, 185},

                // small to big changes in other layouts
                {directorRoom, 92, 113, 186, 202}, {directorBathroom, 80, 91, 195, 202},
                {conferenceRoom, 89, 106, 143, 168}, {meetingRoom,22, 59, 1, 16},
                {dataCenter, 38, 59, 107, 125}, {dataCenterCCTV, 26, 37, 107, 125},
                {controlCenter, 38, 59, 127, 145}, {mesa, 67, 80, 144, 160},
                {SR1, 67, 79, 94, 101}, {SR2, 67, 79, 85, 92}, {SR3, 67, 79, 48, 55}, {SR4, 67, 79, 39, 46},
                {LS1, 26, 59, 86, 105}, {LS2, 26, 59, 65, 84}, {LS3, 26, 59, 44, 63}, {LS4, 26, 59, 22, 42},
                {researchCenter, 87, 106, 24, 98}, {facultyRoom, 87, 106, 99, 127},
                {humanExpRoom, 67, 86, 1, 15}, {dataCollectionRoom, 87, 104, 1, 22},
                {storageRoom, 89, 106, 129, 141}, {clinic, 80, 91, 186, 193}
        };

        for (Object[] range : floorRanges) {
            List<Patch> floorPatches =  (List<Patch>) range[0];
            int startRow = (int) range[1];
            int endRow = (int) range[2];
            int startColumn = (int) range[3];
            int endColumn = (int) range[4];
            for (int i = startRow; i <= endRow; i ++) {
                for (int j = startColumn; j <= endColumn; j ++) {
                    floorPatches.add(environment.getPatch(i, j));
                }
            }
        }

        simulator.getEnvironment().getElevatorLobbies().add(ElevatorLobby.elevatorLobbyFactory.create(elevatorLobby, ""));
        simulator.getEnvironment().getFloors().add(Floor.floorFactory.create(hallway, "dimHallway"));
        simulator.getEnvironment().getBathrooms().add(Bathroom.bathroomFactory.create(maleBathroom, "maleBathroom"));
        simulator.getEnvironment().getBathrooms().add(Bathroom.bathroomFactory.create(femaleBathroom, "femaleBathroom"));
        simulator.getEnvironment().getBathrooms().add(Bathroom.bathroomFactory.create(directorBathroom, "dimDirectorBathroom"));
        simulator.getEnvironment().getBreakerRooms().add(BreakerRoom.breakerRoomFactory.create(breakerRoom, "dimBreakerRoom"));
        simulator.getEnvironment().getReceptions().add(Reception.receptionFactory.create(reception, "dimReception"));
        simulator.getEnvironment().getDirectorRooms().add(DirectorRoom.directorRoomFactory.create(directorRoom, "dimDirectorRoom"));
        simulator.getEnvironment().getConferenceRooms().add(ConferenceRoom.conferenceRoomFactory.create(conferenceRoom, "dimConferenceRoom"));
        simulator.getEnvironment().getMeetingRooms().add(MeetingRoom.meetingRoomFactory.create(meetingRoom, "dimMeetingRoom"));
        simulator.getEnvironment().getDataCenters().add(DataCenter.dataCenterFactory.create(dataCenter, "dimDataCenter"));
        simulator.getEnvironment().getDataCenters().add(DataCenter.dataCenterFactory.create(dataCenterCCTV, "dimCCTV"));
        simulator.getEnvironment().getControlCenters().add(ControlCenter.controlCenterFactory.create(controlCenter, "dimControlCenter"));
        simulator.getEnvironment().getSoloRooms().add(SoloRoom.soloRoomFactory.create(SR1, "dimSR1"));
        simulator.getEnvironment().getSoloRooms().add(SoloRoom.soloRoomFactory.create(SR2, "dimSR2"));
        simulator.getEnvironment().getSoloRooms().add(SoloRoom.soloRoomFactory.create(SR3, "dimSR3"));
        simulator.getEnvironment().getSoloRooms().add(SoloRoom.soloRoomFactory.create(SR4, "dimSR4"));
        simulator.getEnvironment().getLearningSpaces().add(LearningSpace.learningSpaceFactory.create(LS1, "dimLS1"));
        simulator.getEnvironment().getLearningSpaces().add(LearningSpace.learningSpaceFactory.create(LS2, "dimLS2"));
        simulator.getEnvironment().getLearningSpaces().add(LearningSpace.learningSpaceFactory.create(LS3, "dimLS3"));
        simulator.getEnvironment().getLearningSpaces().add(LearningSpace.learningSpaceFactory.create(LS4, "dimLS4"));
        simulator.getEnvironment().getResearchCenters().add(ResearchCenter.researchCenterFactory.create(researchCenter, "dimResearchCenter"));
        simulator.getEnvironment().getFacultyRooms().add(FacultyRoom.facultyRoomFactory.create(facultyRoom, "dimFacultyRoom"));
        simulator.getEnvironment().getHumanExpRooms().add(HumanExpRoom.humanExpRoomFactory.create(humanExpRoom, "dimHumExpRoom"));
        simulator.getEnvironment().getDataCollectionRooms().add(DataCollectionRoom.dataCollectionRoomFactory.create(dataCollectionRoom, "dimDataCollRoom"));
        simulator.getEnvironment().getStorageRooms().add(StorageRoom.storageRoomFactory.create(storageRoom, "dimStorageRoom"));
        simulator.getEnvironment().getClinics().add(Clinic.clinicFactory.create(clinic, "dimClinic"));
        simulator.getEnvironment().getMESAs().add(MESA.MESAFactory.create(mesa, "dimMESA"));
        simulator.getEnvironment().getPantries().add(Pantry.pantryFactory.create(pantry, "dimPantry"));

        /* Office Next Door */

        List<Patch> officeNextDoor = new ArrayList<>();

        for (int i = 0; i <= 55; i++) {
            int startColumn = 0;
            int endColumn = 183;

            if (i >= 14 && i <= 21) {
                startColumn = 32;
            } else if (i >= 22 && i <= 26) {
                startColumn = 127;
            } else if (i >= 27 && i <= 33) {
                startColumn = 127;
                endColumn = 175;
            } else if (i >= 34 && i <= 46) {
                startColumn = 147;
                endColumn = 175;
            } else if (i >= 47) {
                startColumn = 154;
                endColumn = 175;
            }

            for (int j = startColumn; j <= endColumn; j++) {
                officeNextDoor.add(environment.getPatch(i, j));
            }
        }

        simulator.getEnvironment().getDividers().add(Divider.dividerFactory.create(officeNextDoor, "officeNextDoor"));


        /* Parking Lot */

        List<Patch> parkingLot = new ArrayList<>();

        for (int i = 109; i <= 110; i++ ) {
            for (int j = 0; j <= 22; j++ ) {
                parkingLot.add(environment.getPatch(i, j));
            }
        }

        for (int i = 111; i <= 128; i++ ) {
            for (int j = 0; j <= 133; j++ ) {
                parkingLot.add(environment.getPatch(i, j));
            }
        }

        for (int i = 118; i <= 128; i++ ) {
            for (int j = 187; j <= 203; j++ ) {
                parkingLot.add(environment.getPatch(i, j));
            }
        }

        simulator.getEnvironment().getDividers().add(Divider.dividerFactory.create(parkingLot, "parkingLot"));

        /* Permanent Walls */

        List<Patch> permanentWalls = new ArrayList<>();

        int[][] permanentWallRanges = {

                // Male Bathroom
                {1, 186, 202}, {1, 186, 202}, {11, 185, 191}, {20, 191, 192},

                // Female Bathroom
                {57, 191, 202}, {66, 186, 191},

                // Office
                {15, 1, 30}, {23, 22, 125}, {35, 126, 145}, {48, 146, 152}, {57, 153, 176}, {77, 169, 202},
                {115, 187, 203}, {126, 134, 186}, {108, 23, 133}, {106, 0, 22}, {24, 177, 184},

                // Pillars
                {52, 136, 137}, {98, 31, 32}, {98, 53, 54}, {103, 84, 85}, {103, 102, 103}, {91, 131, 132},
                {91, 156, 157}, {110, 201, 202}, {118, 144, 145}, {118, 165, 166}

        };

        for (int[] range : permanentWallRanges) {
            int startRow = range[0];
            int endRow = startRow + 2;
            int startColumn = range[1];
            int endColumn = range[2];

            for (int i = startRow; i <= endRow; i ++) {
                for (int j = startColumn; j <= endColumn; j++) {
                    permanentWalls.add(environment.getPatch(i, j));
                }
            }
        }

        simulator.getEnvironment().getDividers().add(Divider.dividerFactory.create(permanentWalls, "permanentWall"));


        /* Permanent Wall Tops */

        List<Patch> permanentWallTops = new ArrayList<>();

        int[][] horizontalRangesPWT = {

                // Office Outline
                {14, 0, 31}, {22, 22, 126}, {34, 126, 146}, {47, 146, 153}, {56, 153, 176}, {23, 176, 184},
                {56, 183, 184}, {76, 169, 203}, {114, 186, 203}, {125, 134, 186}, {107, 23, 134}, {105, 0, 23},

                // Male Bathroom
                {0, 185, 203}, {10, 185, 191}, {19, 191, 203},

                // Female Bathroom
                {56, 191, 203}, {65, 186, 191}, {75, 185, 203}
        };

        int[][] verticalRangesPWT = {

                // Office Outline
                {0, 15, 104}, {31, 15, 21}, {126, 23, 33}, {146, 35, 46}, {153, 48, 55}, {176, 24, 55},
                {184, 0, 22}, {184, 57, 75}, {169, 67, 75}, {203, 77, 113}, {186, 115, 124}, {134, 108, 124},
                {23, 106, 106},

                // Male Bathroom
                {185, 1, 9}, {203, 1, 18},

                // Female Bathroom
                {185, 56, 74}, {203, 57, 74}

        };

        int[][] pillarTops = {
                {50, 136, 1}, {96, 31, 1}, {96, 53, 1}, {101, 84, 1}, {101, 102, 1}, {88, 131, 2},
                {88, 156, 2}, {108, 201, 1}, {116, 144, 1}, {116, 165, 1}
        };


        // Elevator Area
        for (int i = 20; i <= 55; i++) {
            for (int j = 193; j <= 203; j++) {
                permanentWallTops.add(environment.getPatch(i, j));
            }
        }

        for (int[] range : horizontalRangesPWT) {
            int row = range[0];
            int startColumn = range[1];
            int endColumn = range[2];

            for (int j = startColumn; j <= endColumn; j++) {
                permanentWallTops.add(environment.getPatch(row, j));
            }
        }

        for (int[] range : verticalRangesPWT) {
            int column = range[0];
            int startRow = range[1];
            int endRow = range[2];

            for (int i = startRow; i <= endRow; i++) {
                permanentWallTops.add(environment.getPatch(i, column));
            }
        }

        for (int[] range : pillarTops) {
            int startRow = range[0];
            int endRow = startRow + range[2];
            int startColumn = range[1];
            int endColumn = startColumn + 1;

            for (int i = startRow; i <= endRow; i ++) {
                for (int j = startColumn; j <= endColumn; j++) {
                    permanentWallTops.add(environment.getPatch(i, j));
                }
            }
        }

        simulator.getEnvironment().getDividers().add(Divider.dividerFactory.create(permanentWallTops, "permanentWallTop"));


        /* Walls (inside office) */

        List<Patch> walls = new ArrayList<>();

        int[][] wallRanges = {

                // Meeting Room
                {23, 1, 16}, {57, 1, 9}, {57, 15, 16},

                // Learning Spaces
                {57, 22, 31}, {57, 37, 57}, {57, 63, 78}, {57, 84, 99}, {57, 105, 106},

                // Solo Rooms
                {68, 38, 39}, {68, 44, 56}, {68, 84, 96}, {68, 101, 102},
                {77, 38, 50}, {77, 55, 56}, {77, 84, 85}, {77, 90, 102},

                // MESA
                {72, 143, 161},

                // Data & Control Centers
                {57, 106, 119}, {57, 125, 139}, {57, 145, 146}, {35, 106, 107}, {35, 113, 125},

                // Human Experiment Room, Data Collection Room, Research Center,
                // Faculty Room, Storage Room, Conference Room
                {68, 1, 15}, {88, 1, 8}, {88, 14, 16}, {88, 22, 24}, {88, 30, 120}, {88, 127, 128},
                {90, 129, 130}, {90, 133, 135}, {90, 141, 143}, {90, 149, 155}, {90, 158, 162},
                {90, 168, 169}, {97, 1, 6}, {106, 142, 144}, {110, 142, 144}, {106, 143, 144},
                {108, 135, 169},

                // Director Room & Bathroom and Clinic
                {89, 186, 195}, {89, 200, 202}

        };

        for (int[] range : wallRanges) {
            int startRow = range[0];
            int endRow = startRow + 2;
            int startColumn = range[1];
            int endColumn = range[2];

            for (int i = startRow; i <= endRow; i ++) {
                for (int j = startColumn; j <= endColumn; j++) {
                    walls.add(environment.getPatch(i, j));
                }
            }
        }

        simulator.getEnvironment().getDividers().add(Divider.dividerFactory.create(walls, "wall"));

        /* Wall Tops (inside office) */

        List<Patch> wallTops = new ArrayList<>();

        int[][] horizontalRangesWT = {

                // Meeting Room
                {22, 1, 16}, {56, 1, 9}, {56, 15, 16},

                // Learning Spaces
                {56, 22, 31}, {56, 37, 57}, {56, 63, 78}, {56, 84, 99}, {56, 105, 106},

                // Solo Rooms
                {67, 38, 39}, {67, 44, 56}, {67, 84, 96}, {67, 101, 102},
                {76, 38, 50}, {76, 55, 56}, {76, 84, 85}, {76, 90, 102},

                // MESA
                {72, 143, 161},

                // Data & Control Centers
                {56, 106, 119}, {56, 125, 139}, {56, 145, 146}, {34, 106, 107}, {34, 113, 125},

                // Human Experiment Room, Data Collection Room, Research Center,
                // Faculty Room, Storage Room, Conference Room
                {67, 1, 15}, {87, 1, 8}, {87, 14, 16}, {87, 22, 24}, {87, 30, 120}, {87, 127, 128},
                {89, 129, 130}, {89, 133, 135}, {89, 141, 143}, {89, 149, 155}, {89, 158, 162},
                {89, 168, 169}, {96, 1, 6}, {105, 142, 144}, {109, 142, 144}, {105, 143, 144}, {104, 6, 6},
                {107, 135, 169},

                // Director Room & Bathroom and Clinic
                {88, 186, 195}, {88, 200, 202}
        };

        int[][] verticalRangesWT = {

                // Meeting Room
                {16, 23, 55},

                // Learning Spaces
                {22, 23, 56}, {43, 23, 56}, {64, 23, 56}, {85, 23, 56}, {106, 23, 56},

                // Solo Rooms
                {38, 68, 75}, {47, 68, 75}, {56, 68, 75}, {84, 68, 75}, {93, 68, 75}, {102, 68, 75},

                // MESA
                {143, 67, 80}, {152, 67, 80}, {161, 67, 80},

                // Data & Control Centers
                {126, 35, 55}, {146, 48, 55},

                // Human Experiment Room, Data Collection Room, Research Center,
                // Faculty Room, Storage Room, Conference Room
                {15, 77, 86}, {23, 88, 104}, {98, 98, 106}, {128, 88, 106}, {142, 90, 109}, {169, 90, 106},

                // Director Room & Bathroom and Clinic
                {186, 85, 87}, {186, 97, 113}, {194, 77, 87}

        };

        for (int[] range : horizontalRangesWT) {
            int row = range[0];
            int startColumn = range[1];
            int endColumn = range[2];

            for (int j = startColumn; j <= endColumn; j++) {
                wallTops.add(environment.getPatch(row, j));
            }
        }

        for (int[] range : verticalRangesWT) {
            int column = range[0];
            int startRow = range[1];
            int endRow = range[2];

            for (int i = startRow; i <= endRow; i++) {
                wallTops.add(environment.getPatch(i, column));
            }
        }

        simulator.getEnvironment().getDividers().add(Divider.dividerFactory.create(wallTops, "wallTop"));


        /* Door Patches */

        List<Patch> permanentDoorPatches = new ArrayList<>();

        int[][] permanentDoorRanges = {

                // Elevators
                {193, 23}, {193, 34}, {193, 45},

                // Male Bathroom
                {191, 11},

                // Female Bathroom
                {191, 57},

                // Office
                {183, 57}, {169, 57}, {22, 15},


                // Inside Office
                {9, 57}, {15, 57}, {31, 57}, {37, 57}, {57, 57}, {63, 57}, {78, 57}, {84, 57}, {99, 57}, {105, 57},
                {119, 57}, {125, 57}, {139, 57}, {145, 57}, {15, 68}, {39, 68}, {44, 68}, {96, 68}, {101, 68},
                {50, 77}, {55, 77}, {85, 77}, {90, 77}, {8, 88}, {14, 88}, {16, 88}, {22, 88}, {24, 88}, {30, 88},
                {120, 88}, {127, 88}, {135, 90}, {141, 90}, {143, 90}, {149, 90}, {162, 90}, {168, 90}, {186, 89},
                {195, 89}, {200, 89}

        };

        for (int[] range : permanentDoorRanges) {
            int column = range[0];
            int startRow = range[1];
            int endRow = startRow + 2;


            for (int i = startRow; i <= endRow; i ++) {
                permanentDoorPatches.add(environment.getPatch(i, column));
            }
        }

        simulator.getEnvironment().getDividers().add(Divider.dividerFactory.create(permanentDoorPatches, "doorPatch"));


        /* AMENITIES */

        /* Cubicles */

        List<Patch> nwMESA = new ArrayList<>(), neMESA = new ArrayList<>(), swMESA = new ArrayList<>(), seMESA = new ArrayList<>(),
                cubicleA  = new ArrayList<>(), westCubicleB  = new ArrayList<>(), eastCubicleB  = new ArrayList<>(),
                westCubicleC  = new ArrayList<>();

        Object[][] cubicleRanges =  {

                // MESA
                {nwMESA, 67, 153}, {neMESA, 67, 148},
                {swMESA, 76, 153}, {seMESA, 76, 148},

                // Type A
                {cubicleA, 43, 111},

                // Type B
                {westCubicleB, 95, 105}, {westCubicleB, 95, 115}, {westCubicleB, 95, 125},
                {westCubicleB, 99, 105}, {westCubicleB, 99, 115}, {westCubicleB, 99, 125},

                {eastCubicleB, 95, 108}, {eastCubicleB, 95, 118},
                {eastCubicleB, 99, 108}, {eastCubicleB, 99, 118},

                // Type C
                {westCubicleC, 41, 120}, {westCubicleC, 44, 120},
                {westCubicleC, 47, 120}, {westCubicleC, 50, 120},
        };

        for (Object[] range : cubicleRanges) {
            List<Patch> amenityPatches =  (List<Patch>) range[0];
            int row = (int) range[1];
            int column = (int) range[2];
            amenityPatches.add(environment.getPatch(row, column));
        }

        CubicleMapper.draw(nwMESA, "MESA", "", "NORTH_AND_WEST", false, 0);
        CubicleMapper.draw(neMESA, "MESA", "", "NORTH_AND_EAST", false, 0);
        CubicleMapper.draw(swMESA, "MESA", "", "SOUTH_AND_WEST", false, 0);
        CubicleMapper.draw(seMESA, "MESA", "", "SOUTH_AND_EAST", false, 0);
        CubicleMapper.draw(cubicleA, "TYPE_A", "", "", true, 1);
        CubicleMapper.draw(westCubicleB, "TYPE_B", "WEST", "", true, 2);
        CubicleMapper.draw(eastCubicleB, "TYPE_B", "EAST", "", true, 2);
        CubicleMapper.draw(westCubicleC, "TYPE_C", "WEST", "", false, 0);

        /* Reception Table */

        List<Patch> ReceptionTable1x8 = new ArrayList<>();
        ReceptionTable1x8.add(environment.getPatch(69,170));
        ReceptionTableMapper.draw(ReceptionTable1x8, "1x8");

        /* Research Tables */

        List<Patch> westResearchTable = new ArrayList<>(), westMonitorResearchTable = new ArrayList<>(),
                eastResearchTable = new ArrayList<>(), eastMonitorResearchTable = new ArrayList<>();

        Object[][] researchTableRanges =  {
                {westResearchTable, 100, 38}, {eastResearchTable, 100, 39},
                {westMonitorResearchTable, 100, 46}, {eastResearchTable, 100, 47},
                {westResearchTable, 100, 60}, {eastResearchTable, 100, 61},
                {westMonitorResearchTable, 100, 68}, {eastResearchTable, 100, 69},
                {westMonitorResearchTable, 100, 76}, {eastResearchTable, 100, 77},
                {westResearchTable, 100, 90}, {eastMonitorResearchTable, 100, 91}
        };

        for (Object[] range : researchTableRanges) {
            List<Patch> amenityPatches =  (List<Patch>) range[0];
            int row = (int) range[1];
            int column = (int) range[2];
            amenityPatches.add(environment.getPatch(row, column));
        }

        ResearchTableMapper.draw(westResearchTable, "WEST", false);
        ResearchTableMapper.draw(eastResearchTable, "EAST", false);
        ResearchTableMapper.draw(westMonitorResearchTable, "WEST", true);
        ResearchTableMapper.draw(eastMonitorResearchTable, "EAST", true);


        /* Learning Tables */

        List<Patch> learningTables = new ArrayList<>();

        Object[][] learningTableRanges =  {
                // Learning Space 1
                {33, 89}, {33, 98},
                {47, 89}, {47, 98},

                // Learning Space 2
                {33, 68}, {33, 77},
                {47, 68}, {47, 77},

                // Learning Space 3
                {33, 47}, {33, 56},
                {47, 47}, {47, 56},

                // Learning Space 4
                {33, 26}, {33, 35},
                {47, 26}, {47, 35}
        };

        for (Object[] range : learningTableRanges) {
            int row = (int) range[0];
            int column = (int) range[1];
            learningTables.add(environment.getPatch(row, column));
        }

        LearningTableMapper.draw(learningTables, "HORIZONTAL");

        /* Meeting Tables */

        List<Patch> largeVerticalMeetingTables = new ArrayList<>(), leftLargeHorizontalMeetingTables = new ArrayList<>(),
                rightLargeHorizontalMeetingTables = new ArrayList<>(), smallVerticalMeetingTables = new ArrayList<>();

        Object[][] meetingTableRanges =  {
                // Conference Room
                {leftLargeHorizontalMeetingTables, 98, 147}, {rightLargeHorizontalMeetingTables, 98, 156},

                // Meeting Room
                {largeVerticalMeetingTables, 36, 7},

                // Director Room
                {smallVerticalMeetingTables, 97, 198},
        };

        for (Object[] range : meetingTableRanges) {
            List<Patch> amenityPatches =  (List<Patch>) range[0];
            int row = (int) range[1];
            int column = (int) range[2];
            amenityPatches.add(environment.getPatch(row, column));
        }

        MeetingTableMapper.draw(largeVerticalMeetingTables, "VERTICAL", "LARGE", "");
        MeetingTableMapper.draw(leftLargeHorizontalMeetingTables, "HORIZONTAL", "LARGE", "LEFT");
        MeetingTableMapper.draw(rightLargeHorizontalMeetingTables, "HORIZONTAL", "LARGE", "RIGHT");
        MeetingTableMapper.draw(smallVerticalMeetingTables, "VERTICAL", "SMALL", "");


        /* Pantry Tables and Chairs */

        List<Patch> typeApantryTables = new ArrayList<>(), typeBpantryTables = new ArrayList<>(),
                typeApantryChairs = new ArrayList<>(), typeBpantryChairs = new ArrayList<>();


        Object[][] pantryTableRanges =  {

                // Type A
                {typeApantryTables, 117, 180}, {typeApantryTables, 120, 175},

                // Type B
                {typeBpantryTables, 117, 169}, {typeBpantryTables, 121, 139}, {typeBpantryTables,  121, 148},
                {typeBpantryTables, 121, 154}, {typeBpantryTables, 121, 160}
        };

        for (Object[] range : pantryTableRanges) {
            List<Patch> amenityPatches =  (List<Patch>) range[0];
            int row = (int) range[1];
            int column = (int) range[2];
            amenityPatches.add(environment.getPatch(row, column));
        }

        PantryTableMapper.draw(typeApantryTables, "TYPE_A");
        PantryTableMapper.draw(typeBpantryTables, "TYPE_B");

        Object[][] pantryChairRanges =  {

                // Type A
                {typeApantryChairs, 111, 150}, {typeApantryChairs, 111, 151}, {typeApantryChairs, 111, 152},
                {typeApantryChairs, 111, 153}, {typeApantryChairs, 111, 154},

                // Type B
                {typeBpantryChairs, 111, 145}, {typeBpantryChairs, 111, 146}, {typeBpantryChairs, 111, 147},
                {typeBpantryChairs, 111, 148}, {typeBpantryChairs, 111, 149}
        };

        for (Object[] range : pantryChairRanges) {
            List<Patch> amenityPatches =  (List<Patch>) range[0];
            int row = (int) range[1];
            int column = (int) range[2];
            amenityPatches.add(environment.getPatch(row, column));
        }

        ChairMapper.draw(typeApantryChairs, 0, "SOUTH","PANTRY_TYPE_A", "");
        ChairMapper.draw(typeBpantryChairs, 0, "SOUTH","PANTRY_TYPE_B", "");

        /* Director Table */

        List<Patch> directorTable = new ArrayList<>();
        directorTable.add(environment.getPatch(108,190));
        DirectorTableMapper.draw(directorTable, "HORIZONTAL", "SOUTH", true);


        /* TABLE 2x2 */

        List<Patch> table2x2 = new ArrayList<>();
        table2x2.add(environment.getPatch(45,127));
        Table2x2Mapper.draw(table2x2);


        /* Solo Table */

        // Top
        List<Patch> topSoloTables = new ArrayList<>();
        topSoloTables.add(environment.getPatch(71,48));
        topSoloTables.add(environment.getPatch(71,85));
        SoloTableMapper.draw(topSoloTables, "1x8", "TOP");

        // Bottom
        List<Patch> bottomSoloTables = new ArrayList<>();
        bottomSoloTables.add(environment.getPatch(75,39));
        bottomSoloTables.add(environment.getPatch(75,94));
        SoloTableMapper.draw(bottomSoloTables, "1x8", "BOTTOM");

        /* Human Experience Table */
        List<Patch> humanExpTable = new ArrayList<>();
        humanExpTable.add(environment.getPatch(77,5));
        HumanExpTableMapper.draw(humanExpTable, "5x1");

        /* Data Collection Table */
        List<Patch> dataCollTable = new ArrayList<>();
        dataCollTable.add(environment.getPatch(91,2));
        DataCollTableMapper.draw(dataCollTable, "1x6");

        /* White Board */

        List<Patch> northWhiteBoard = new ArrayList<>(), southWhiteBoard = new ArrayList<>(), westWhiteBoard = new ArrayList<>(),
                eastWhiteBoard_4 = new ArrayList<>(), eastWhiteBoard_11 = new ArrayList<>();

        Object[][] whiteBoardRanges =  {
                // Research Center Pillars
                {westWhiteBoard, 96, 30}, {northWhiteBoard, 95, 31}, {eastWhiteBoard_4, 96, 33},
                {westWhiteBoard, 96, 52}, {northWhiteBoard, 95, 53}, {eastWhiteBoard_4, 96, 55},

                // Meeting Room
                {eastWhiteBoard_11, 36, 1},

                // Learning Spaces
                {southWhiteBoard, 23, 26}, {southWhiteBoard, 23, 35},
                {southWhiteBoard, 23, 47}, {southWhiteBoard, 23, 56},
                {southWhiteBoard, 23, 68}, {southWhiteBoard, 23, 77},
                {southWhiteBoard, 23, 89}, {southWhiteBoard, 23, 98},
        };

        for (Object[] range : whiteBoardRanges) {
            List<Patch> amenityPatches =  (List<Patch>) range[0];
            int row = (int)  range[1];
            int column = (int) range[2];
            amenityPatches.add(environment.getPatch(row, column));
        }

        WhiteboardMapper.draw(northWhiteBoard, "NORTH", "2");
        WhiteboardMapper.draw(southWhiteBoard, "SOUTH", "5");
        WhiteboardMapper.draw(westWhiteBoard, "WEST", "4");
        WhiteboardMapper.draw(eastWhiteBoard_4, "EAST", "4");
        WhiteboardMapper.draw(eastWhiteBoard_11, "EAST", "11");


        /* Elevator */
        List<Patch> elevator = new ArrayList<>();
        elevator.add(environment.getPatch(26,193));
        elevator.add(environment.getPatch(37,193));
        elevator.add(environment.getPatch(48,193));
        ElevatorMapper.draw(elevator, Elevator.ElevatorMode.ENTRANCE_AND_EXIT,  "VERTICAL");

        /* Couch */
        List<Patch> couch = new ArrayList<>();
        couch.add(environment.getPatch(69,141));
        CouchMapper.draw(couch, "WEST");

        /* Refrigerator */
        List<Patch> refrigerator = new ArrayList<>();
        refrigerator.add(environment.getPatch(110,141));
        RefrigeratorMapper.draw(refrigerator);

        /* Water Dispenser */
        List<Patch> waterDispenser = new ArrayList<>();
        waterDispenser.add(environment.getPatch(110,140));
        WaterDispenserMapper.draw(waterDispenser);


        /* Plants, Trash Cans, Pantry Cabinets, Coffee Maker Bar, Kettle Bar, Microwave Bar */

        List<Patch> plants = new ArrayList<>(), trashCans = new ArrayList<>(), pantryCabinets = new ArrayList<>(),
                coffeeMakerBar = new ArrayList<>(), kettleBar = new ArrayList<>(), microwaveBar = new ArrayList<>();

        Object[][] simpleAmenityRanges =  {

                // Plants
                {plants, 62, 2}, {plants, 74, 179}, {plants, 74, 181}, {plants, 75, 180},
                {plants, 82, 201}, {plants, 84, 201}, {plants, 86, 201},

                // Trash Cans
                {trashCans, 80, 199}, {trashCans, 113, 135}, {trashCans, 107, 201},

                // Pantry Cabinets
                {pantryCabinets, 109, 135}, {pantryCabinets, 109, 136}, {pantryCabinets, 109, 137}, {pantryCabinets, 109, 138},

                // Coffee Maker Bar
                {coffeeMakerBar, 110, 137},

                // Kettle Bar
                {kettleBar, 110, 138},

                // Microwave Bar
                {microwaveBar, 111, 135}
        };

        for (Object[] range : simpleAmenityRanges) {
            List<Patch> amenityPatches =  (List<Patch>) range[0];
            int row = (int) range[1];
            int column = (int) range[2];
            amenityPatches.add(environment.getPatch(row, column));
        }

        PlantMapper.draw(plants);
        TrashCanMapper.draw(trashCans);
        PantryCabinetMapper.draw(pantryCabinets);
        CoffeeMakerBarMapper.draw(coffeeMakerBar);
        KettleBarMapper.draw(kettleBar);
        MicrowaveBarMapper.draw(microwaveBar);


        /* Sinks & Toilets */

        List<Patch> southSinks = new ArrayList<>(), southOfficeSinks = new ArrayList<>(), northSinks = new ArrayList<>(),
                southToilets = new ArrayList<>(), southOfficeToilets = new ArrayList<>(), northToilets  = new ArrayList<>();

        Object[][] sinkToiletRanges =  {

                // Male Bathroom
                {southToilets, 3, 188}, {southToilets, 3, 191},
                {southToilets, 3, 194}, {southToilets, 3, 197},
                {southToilets, 3, 200}, {northSinks, 18, 194},
                {northSinks, 18, 197}, {northSinks, 18, 200},

                // Female Bathroom
                {northToilets, 73, 188}, {northToilets, 73, 191},
                {northToilets, 73, 194}, {northToilets, 73, 197},
                {northToilets, 73, 200}, {southSinks,  60, 194},
                {southSinks, 60, 197}, {southSinks, 60, 200},

                // Director Bathroom
                {southOfficeToilets, 79, 200}, {southOfficeSinks, 80, 197},

                // Pantry
                {southOfficeSinks, 111, 136},
        };

        for (Object[] range : sinkToiletRanges) {
            List<Patch> amenityPatches =  (List<Patch>) range[0];
            int row = (int) range[1];
            int column = (int) range[2];
            amenityPatches.add(environment.getPatch(row, column));
        }

        SinkMapper.draw(southSinks, "SOUTH", "SINK");
        SinkMapper.draw(southOfficeSinks, "SOUTH", "OFFICE_SINK");
        SinkMapper.draw(northSinks, "NORTH", "SINK");
        ToiletMapper.draw(southToilets, "SOUTH", "TOILET");
        ToiletMapper.draw(southOfficeToilets, "SOUTH", "OFFICE_TOILET");
        ToiletMapper.draw(northToilets, "NORTH", "TOILET");


        /* Switches */

        List<Patch> southLightSwitches = new ArrayList<>(), northLightSwitches = new ArrayList<>(), westLightSwitches = new ArrayList<>(), eastLightSwitches = new ArrayList<>(),
                southAirconSwitches = new ArrayList<>(), northAirconSwitches = new ArrayList<>(), westAirconSwitches = new ArrayList<>(), eastAirconSwitches = new ArrayList<>();

        Object[][] switchRanges =  {

                // Meeting Room
                {southLightSwitches,24, 13}, {southAirconSwitches, 24, 14},

                // Learning Spaces
                {eastLightSwitches, 53, 23}, {eastAirconSwitches, 54, 23},
                {westLightSwitches, 53, 105}, {westAirconSwitches, 54, 105},

                // Control Center
                {southLightSwitches,36, 115}, {southAirconSwitches, 36, 116},

                // Data Center
                {westLightSwitches, 53, 145}, {westAirconSwitches, 54, 145},

                // Hallway
                {eastLightSwitches, 57, 147}, {eastAirconSwitches, 58, 147},

                // Reception
                {southLightSwitches,58, 174}, {southAirconSwitches, 58, 175},

                // Human Experience Room
                {southLightSwitches,69, 12}, {southAirconSwitches, 69, 13},

                // Data Collection Room
                {northLightSwitches, 95, 5}, {northAirconSwitches, 95, 6},

                // Research Center
                {westLightSwitches, 103, 83}, {westAirconSwitches, 104, 83},

                // Faculty Room
                {southLightSwitches,89, 117}, {southAirconSwitches, 89, 118},

                // Storage Room
                {southLightSwitches,91, 133},

                // Conference Room
                {southLightSwitches,91, 151}, {southAirconSwitches, 91, 152},

                // Clinic
                {southLightSwitches,78, 188}, {southAirconSwitches, 78, 189},

                // Director Room
                {southLightSwitches,90, 188}, {southAirconSwitches, 90, 189},

                // Pantry
                {westLightSwitches, 112, 185}, {southAirconSwitches, 109, 168},

                // Solo Rooms
                {westLightSwitches, 72, 101}, {eastLightSwitches, 74, 85},
                {westLightSwitches, 74, 55}, {eastLightSwitches, 72, 39},

                // Director Bathroom
                {eastLightSwitches, 86, 195},
        };

        for (Object[] range : switchRanges) {
            List<Patch> amenityPatches =  (List<Patch>) range[0];
            int row = (int) range[1];
            int column = (int) range[2];
            amenityPatches.add(environment.getPatch(row, column));
        }

        SwitchMapper.draw(southLightSwitches, "LIGHT", "SOUTH");
        SwitchMapper.draw(northLightSwitches, "LIGHT", "NORTH");
        SwitchMapper.draw(westLightSwitches, "LIGHT", "WEST");
        SwitchMapper.draw(eastLightSwitches, "LIGHT", "EAST");
        SwitchMapper.draw(southAirconSwitches, "AC", "SOUTH");
        SwitchMapper.draw(northAirconSwitches, "AC", "NORTH");
        SwitchMapper.draw(westAirconSwitches, "AC", "WEST");
        SwitchMapper.draw(eastAirconSwitches, "AC", "EAST");


        /* Aircons */

        List<Patch> aircons = new ArrayList<>();

        Object[][] airconRanges =  {

                // Meeting Room
                {42, 7},

                // Learning Spaces
                {28, 35}, {50, 25},
                {27, 55}, {50, 48}, {50, 56},
                {27, 78}, {47, 70},
                {27, 99}, {47, 91},

                // Control Center
                {41, 118}, {50, 118},

                // Data Center
                {41, 132}, {41, 139},

                // Hallway
                {72, 22}, {72, 62}, {72, 108}, {72, 120},
                {72, 132}, {62, 147},

                // Reception
                {66, 179},

                // Human Experience Room
                {72, 6},

                // Data Collection Room
                {93, 9},

                // Research Center
                {92, 34}, {92, 40}, {97, 71}, {85, 88},

                // Faculty Room
                {95, 101}, {95, 112}, {100, 122},

                // Conference Room
                {94, 149}, {94, 161},

                // Clinic
                {82, 189},

                // Director Room
                {99, 191},

                // Pantry
                {120, 151},
        };

        for (Object[] range : airconRanges) {
            int row = (int) range[0];
            int column = (int) range[1];
            aircons.add(environment.getPatch(row, column));
        }

        AirconMapper.draw(aircons, false);


        /* Lights */

        List<Patch> singlePendantLights = new ArrayList<>(), singleRecessedLights = new ArrayList<>(), horizontalPendantLights = new ArrayList<>(), verticalPendantLights = new ArrayList<>(),
                horizontalRecessedLights = new ArrayList<>(), verticalRecessedLights = new ArrayList<>(), horizontalTrackLights = new ArrayList<>(), verticalTrackLights = new ArrayList<>();

        Object[][] lightRanges =  {

                // Meeting Room
                {horizontalTrackLights, 30, 7},
                {verticalRecessedLights, 32, 8}, {verticalRecessedLights, 47, 8},

                // Learning Space 4
                {horizontalTrackLights, 27, 27}, {horizontalTrackLights, 43, 27},
                {horizontalTrackLights, 43, 35}, {horizontalTrackLights, 54, 27},
                {verticalRecessedLights, 28, 25}, {verticalRecessedLights, 28, 40},
                {verticalRecessedLights, 50, 33},

                // Learning Space 3
                {horizontalTrackLights, 27, 49}, {horizontalTrackLights, 43, 48},
                {horizontalTrackLights, 43, 56}, {horizontalTrackLights, 54, 52},
                {verticalRecessedLights, 28, 46}, {verticalRecessedLights, 28, 58},
                {verticalRecessedLights, 52, 54},

                // Learning Space 2
                {horizontalTrackLights, 27, 69}, {horizontalTrackLights, 43, 69},
                {horizontalTrackLights, 43, 77}, {horizontalTrackLights, 54, 73},
                {verticalRecessedLights, 27, 67}, {verticalRecessedLights, 27, 75},
                {verticalRecessedLights, 52, 67}, {verticalRecessedLights, 52, 75},

                // Learning Space 1
                {horizontalTrackLights, 27, 90}, {horizontalTrackLights, 43, 90},
                {horizontalTrackLights, 43, 98}, {horizontalTrackLights, 54, 94},
                {verticalRecessedLights, 27, 88}, {verticalRecessedLights, 27, 96},
                {verticalRecessedLights, 52, 88}, {verticalRecessedLights, 52, 96},

                // Control Center
                {verticalRecessedLights, 39, 109}, {verticalRecessedLights, 39, 116},
                {verticalRecessedLights, 39, 123}, {verticalRecessedLights, 53, 109},
                {verticalRecessedLights, 53, 123},

                // Data Center
                {verticalRecessedLights, 41, 129}, {verticalRecessedLights, 41, 136},
                {verticalRecessedLights, 41, 143},

                // Hallway
                {verticalRecessedLights, 31, 19}, {verticalRecessedLights, 49, 19},
                {verticalRecessedLights, 62, 4}, {verticalRecessedLights, 62, 16},
                {verticalRecessedLights, 62, 138}, {verticalRecessedLights, 71, 138},
                {verticalRecessedLights, 80, 138}, {verticalRecessedLights, 62, 158},
                {verticalRecessedLights, 62, 165}, {verticalRecessedLights, 71, 165},
                {verticalRecessedLights, 80, 165}, {verticalRecessedLights, 85, 177},
                {verticalRecessedLights, 100, 177},
                {horizontalRecessedLights, 63, 22}, {horizontalRecessedLights, 83, 22},
                {horizontalRecessedLights, 63, 30}, {horizontalRecessedLights, 83, 30},
                {horizontalRecessedLights, 63, 38}, {horizontalRecessedLights, 83, 38},
                {horizontalRecessedLights, 63, 46}, {horizontalRecessedLights, 83, 46},
                {horizontalRecessedLights, 63, 54}, {horizontalRecessedLights, 83, 54},
                {horizontalRecessedLights, 63, 62}, {horizontalRecessedLights, 83, 62},
                {horizontalRecessedLights, 63, 70}, {horizontalRecessedLights, 83, 70},
                {horizontalRecessedLights, 63, 78}, {horizontalRecessedLights, 83, 78},
                {horizontalRecessedLights, 63, 86}, {horizontalRecessedLights, 83, 86},
                {horizontalRecessedLights, 63, 94}, {horizontalRecessedLights, 83, 94},
                {horizontalRecessedLights, 63, 102}, {horizontalRecessedLights, 83, 102},
                {horizontalRecessedLights, 63, 110}, {horizontalRecessedLights, 83, 110},
                {horizontalRecessedLights, 63, 118}, {horizontalRecessedLights, 83, 118},
                {horizontalRecessedLights, 63, 126}, {horizontalRecessedLights, 83, 126},
                {horizontalPendantLights, 75, 62}, {horizontalPendantLights, 75, 120},
                {verticalPendantLights, 72, 33},
                {singlePendantLights, 75, 78}, {singlePendantLights, 75, 108},

                // Reception
                {verticalRecessedLights, 62, 172}, {verticalRecessedLights, 62, 180},
                {verticalRecessedLights, 71, 172}, {verticalRecessedLights, 71, 180},
                {verticalTrackLights, 66, 182},

                // Human Experience Room
                {verticalRecessedLights, 78, 6}, {verticalRecessedLights, 78, 10},

                // Data Collection Room
                {verticalRecessedLights, 93, 5}, {verticalRecessedLights, 93, 19},

                // Research Center
                {verticalRecessedLights, 92, 32}, {verticalRecessedLights, 92, 38},
                {verticalRecessedLights, 92, 44}, {verticalRecessedLights, 92, 50},
                {verticalRecessedLights, 92, 56}, {verticalRecessedLights, 92, 62},
                {verticalRecessedLights, 92, 68}, {verticalRecessedLights, 92, 74},
                {verticalRecessedLights, 92, 80}, {verticalRecessedLights, 92, 86},
                {verticalRecessedLights, 92, 92},
                {verticalRecessedLights, 97, 62}, {verticalRecessedLights, 97, 68},
                {verticalRecessedLights, 97, 74}, {verticalRecessedLights, 97, 80},
                {verticalRecessedLights, 97, 86}, {verticalRecessedLights, 97, 92},

                {verticalPendantLights, 100, 41}, {verticalPendantLights, 102, 41},
                {verticalPendantLights, 100, 49}, {verticalPendantLights, 102, 49},
                {verticalPendantLights, 100, 63}, {verticalPendantLights, 102, 63},
                {verticalPendantLights, 100, 71}, {verticalPendantLights, 102, 71},
                {verticalPendantLights, 100, 79}, {verticalPendantLights, 102, 79},
                {verticalPendantLights, 100, 93}, {verticalPendantLights, 102, 93},

                {singlePendantLights, 97, 27}, {singlePendantLights, 103, 27},
                {singlePendantLights, 93, 72}, {singlePendantLights, 93, 78},
                {singlePendantLights, 93, 84}, {singlePendantLights, 93, 90},

                // Faculty Room
                {horizontalTrackLights, 93, 121}, {horizontalTrackLights, 104, 111},
                {horizontalTrackLights, 104, 121}, {verticalRecessedLights, 92, 102},
                {verticalRecessedLights, 92, 107}, {verticalRecessedLights, 92, 114},
                {verticalRecessedLights, 99, 102}, {verticalRecessedLights, 99, 107},
                {horizontalRecessedLights, 94, 126}, {horizontalRecessedLights, 103, 126},
                {horizontalPendantLights, 98, 111}, {horizontalPendantLights, 98, 113},
                {horizontalPendantLights, 99, 111}, {horizontalPendantLights, 99, 113},

                // Storage Room
                {verticalRecessedLights, 95, 134}, {verticalRecessedLights, 103, 134},

                // Conference Room
                {verticalTrackLights, 96, 144}, {verticalTrackLights, 96, 167},
                {verticalRecessedLights, 95, 147}, {verticalRecessedLights, 95, 158},
                {horizontalPendantLights, 99, 153}, {horizontalPendantLights, 99, 155},
                {horizontalPendantLights, 99, 157},

                // Clinic
                {verticalRecessedLights, 82, 187}, {verticalRecessedLights, 82, 192},

                // Director Room
                {verticalRecessedLights, 93, 189}, {verticalRecessedLights, 93, 194},
                {verticalRecessedLights, 104, 189}, {verticalRecessedLights, 109, 194},
                {verticalRecessedLights, 111, 189}, {horizontalTrackLights, 112, 190},
                {horizontalPendantLights, 107, 191}, {singlePendantLights, 98, 198},

                // Director Bathroom
                {singlePendantLights, 83, 197}, {singlePendantLights, 83, 200},

                // Pantry
                {singlePendantLights, 114, 184}, {singlePendantLights, 122, 184},
                {singlePendantLights, 114, 137}, {singlePendantLights, 118, 137},
                {singlePendantLights, 122, 137}, {horizontalRecessedLights, 114, 146},
                {horizontalRecessedLights, 114, 158}, {horizontalRecessedLights, 114, 170},
                {horizontalRecessedLights, 123, 161}, {horizontalRecessedLights, 123, 170},
                {verticalTrackLights, 115, 149}, {verticalTrackLights, 115, 155},
                {verticalTrackLights, 115, 167}, {verticalTrackLights, 115, 178},
                {horizontalTrackLights, 113, 136}, {horizontalTrackLights, 118, 151},
                {verticalRecessedLights, 122, 138}, {verticalRecessedLights, 122, 143},

                // Solo Rooms
                {singleRecessedLights, 72, 42}, {singleRecessedLights, 74, 52},
                {singleRecessedLights, 72, 98}, {singleRecessedLights, 74, 88},


        };

        for (Object[] range : lightRanges) {
            List<Patch> amenityPatches = (List<Patch>) range[0];
            int row = (int) range[1];
            int column = (int) range[2];
            amenityPatches.add(environment.getPatch(row, column));
        }

        LightMapper.draw(singlePendantLights, "SINGLE_PENDANT_LIGHT", "");
        LightMapper.draw(singleRecessedLights, "SINGLE_RECESSED_LIGHT", "");
        LightMapper.draw(horizontalPendantLights, "LINEAR_PENDANT_LIGHT", "HORIZONTAL");
        LightMapper.draw(verticalPendantLights, "LINEAR_PENDANT_LIGHT", "VERTICAL");
        LightMapper.draw(horizontalRecessedLights, "RECESSED_LINEAR_LIGHT", "HORIZONTAL");
        LightMapper.draw(verticalRecessedLights, "RECESSED_LINEAR_LIGHT", "VERTICAL");
        LightMapper.draw(horizontalTrackLights, "TRACK_LIGHT", "HORIZONTAL");
        LightMapper.draw(verticalTrackLights, "TRACK_LIGHT", "VERTICAL");


        /* WINDOW + BLINDS */
        List<Patch> glass = new ArrayList<>(), southFacultyClosedBlinds = new ArrayList<>(), northFacultyClosedBlinds = new ArrayList<>(),
                eastHallwayWindowBlinds = new ArrayList<>(), eastWindowBlinds = new ArrayList<>(), northSouthWindowBlinds = new ArrayList<>(),
                westWindowBlinds = new ArrayList<>();

        Object[][] windowBlindsRanges = {
                // Hallway
                {eastHallwayWindowBlinds, 61, 1},

                // Human Experience Room
                {eastWindowBlinds, 72, 1},
                {eastWindowBlinds, 77, 1},
                {eastWindowBlinds, 82, 1},

                // Data Collection Room
                {glass, 88, 1},
                {eastWindowBlinds, 91, 1},
                {northSouthWindowBlinds, 104, 8, 21, 5},

                // Research Center
                {northSouthWindowBlinds, 106, 24, 97, 5},

                // Faculty Room
                {southFacultyClosedBlinds, 108, 99, 127, 5},
                {northFacultyClosedBlinds, 106, 99, 127, 5},

                // Pantry Room
                {eastWindowBlinds, 115, 135},
                {eastWindowBlinds, 120, 135},
                {northSouthWindowBlinds, 124, 136, 184, 5},

                // Director Bathroom
                {westWindowBlinds, 80, 202},
                {westWindowBlinds, 84, 202},

                // Director Room
                {westWindowBlinds, 93, 202},
                {westWindowBlinds, 98, 202},
                {westWindowBlinds, 103, 202},
                {northSouthWindowBlinds, 113, 188, 201, 5},
        };

        for (Object[] range : windowBlindsRanges) {
            List<Patch> patches = (List<Patch>) range[0];
            int row = (int) range[1];

            if (range.length == 3) {
                int column = (int) range[2];
                patches.add(environment.getPatch(row, column));
            } else if (range.length == 5) {
                int start = (int) range[2];
                int end = (int) range[3];
                int step = (int) range[4];
                for (int i = start; i <= end; i += step) {
                    patches.add(environment.getPatch(row, i));
                }
            }
        }

        WindowBlindsMapper.draw(glass, "GLASS", 7);
        WindowBlindsMapper.draw(southFacultyClosedBlinds, "CLOSED_SOUTH_FROM_OUTSIDE", 4);
        WindowBlindsMapper.draw(northFacultyClosedBlinds, "CLOSED_NORTH", 4);
        WindowBlindsMapper.draw(northSouthWindowBlinds, "OPENED_NORTH_AND_SOUTH", 4);
        WindowBlindsMapper.draw(eastWindowBlinds, "OPENED_EAST", 4);
        WindowBlindsMapper.draw(eastHallwayWindowBlinds, "OPENED_EAST", 5);
        WindowBlindsMapper.draw(westWindowBlinds, "OPENED_WEST", 4);


        /* CABINETS & DRAWERS + STORAGE */

        List<Patch> storage = new ArrayList<>(), southCabinet = new ArrayList<>(), southDrawers = new ArrayList<>(),
                northCabinet1x2 = new ArrayList<>(), eastDoubleDrawers = new ArrayList<>(), westDoubleDrawers = new ArrayList<>();

        storage.add(environment.getPatch(85, 187));
        southCabinet.add(environment.getPatch(52, 147));
        southDrawers.add(environment.getPatch(50, 151));
        southDrawers.add(environment.getPatch(50, 152));
        northCabinet1x2.add(environment.getPatch(88, 129));
        eastDoubleDrawers.add(environment.getPatch(72, 162));
        westDoubleDrawers.add(environment.getPatch(72, 168));

        StorageMapper.draw(storage, "DOUBLE_DRAWERS", "EAST");
        CabinetDrawerMapper.draw(southCabinet, "CABINET", "SOUTH");
        CabinetDrawerMapper.draw(southDrawers, "DRAWERS", "SOUTH");
        CabinetDrawerMapper.draw(northCabinet1x2, "CABINET_1x2", "NORTH");
        CabinetDrawerMapper.draw(eastDoubleDrawers, "DOUBLE_DRAWERS", "EAST");
        CabinetDrawerMapper.draw(westDoubleDrawers, "DOUBLE_DRAWERS", "WEST");


        /* SERVER */

        List<Patch> serverTypeA = new ArrayList<>(), serverTypeB = new ArrayList<>();

        serverTypeA.add(environment.getPatch(43, 129));
        serverTypeB.add(environment.getPatch(47, 134));
        serverTypeB.add(environment.getPatch(47, 135));

        ServerMapper.draw(serverTypeA, "TYPE_A");
        ServerMapper.draw(serverTypeB, "TYPE_B");

    }


    private void drawInterface() {
        drawEnvironmentViewBackground(simulator.getEnvironment());
        drawEnvironmentViewForeground(simulator.getEnvironment(), false);
    }

    public void drawEnvironmentViewBackground(Environment environment) {
        GraphicsController.requestDrawView(stackPane, environment, GraphicsController.tileSize, true, false);
    }

    public void drawEnvironmentViewForeground(Environment environment, boolean speedAware) {
        GraphicsController.requestDrawView(stackPane, environment, GraphicsController.tileSize, false, speedAware);
        requestUpdateInterfaceSimulationElements();
    }

    private void requestUpdateInterfaceSimulationElements() {
        Platform.runLater(this::updateSimulationTime);
        Platform.runLater(this::updateStatistics);
    }

    public void updateSimulationTime() {
        LocalTime currentTime = simulator.getSimulationTime().getTime();
        long elapsedTime = simulator.getSimulationTime().getStartTime().until(currentTime, ChronoUnit.SECONDS) / 5;
        String timeString;
        timeString = String.format("%02d", currentTime.getHour()) + ":" + String.format("%02d", currentTime.getMinute()) + ":" + String.format("%02d", currentTime.getSecond());
        elapsedTimeText.setText("Current time: " + timeString + " (" + elapsedTime + " ticks)");

    }


    public void updateStatistics() {

        // Current Agent Count
        currentDirectorCount.setText(String.valueOf(Simulator.currentDirectorCount));
        currentFacultyCount.setText(String.valueOf(Simulator.currentFacultyCount));
        currentStudentCount.setText(String.valueOf(Simulator.currentStudentCount));
        currentMaintenanceCount.setText(String.valueOf(Simulator.currentMaintenanceCount));
        currentGuardCount.setText(String.valueOf(Simulator.currentGuardCount));

        // Current Interaction Count
        currentNonverbalCount.setText(String.valueOf(Simulator.currentNonverbalCount));
        currentCooperativeCount.setText(String.valueOf(Simulator.currentCooperativeCount));
        currentExchangeCount.setText(String.valueOf(Simulator.currentExchangeCount));


        // Average Interaction Duration
        averageNonverbalDuration.setText(String.format("%.02f", Simulator.averageNonverbalDuration));
        averageCooperativeDuration.setText(String.format("%.02f", Simulator.averageCooperativeDuration));
        averageExchangeDuration.setText(String.format("%.02f", Simulator.averageExchangeDuration));


        // Current Team Count


        // Current Director to ____ Interaction Count
        currentDirectorFacultyCount.setText(String.valueOf(Simulator.currentDirectorFacultyCount));
        currentDirectorStudentCount.setText(String.valueOf(Simulator.currentDirectorStudentCount));
        currentDirectorMaintenanceCount.setText(String.valueOf(Simulator.currentDirectorMaintenanceCount));
        currentDirectorGuardCount.setText(String.valueOf(Simulator.currentDirectorGuardCount));


        // Current Faculty to ____ Interaction Count
        currentFacultyFacultyCount.setText(String.valueOf(Simulator.currentFacultyFacultyCount));
        currentFacultyStudentCount.setText(String.valueOf(Simulator.currentFacultyStudentCount));
        currentFacultyMaintenanceCount.setText(String.valueOf(Simulator.currentFacultyMaintenanceCount));
        currentFacultyGuardCount.setText(String.valueOf(Simulator.currentFacultyGuardCount));


        // Current Student to ____ Interaction Count
        currentStudentStudentCount.setText(String.valueOf(Simulator.currentStudentStudentCount));
        currentStudentMaintenanceCount.setText(String.valueOf(Simulator.currentStudentMaintenanceCount));
        currentStudentGuardCount.setText(String.valueOf(Simulator.currentStudentGuardCount));

        // Current Maintenance to ____ Interaction Count
        currentMaintenanceMaintenanceCount.setText(String.valueOf(Simulator.currentMaintenanceMaintenanceCount));
        currentMaintenanceGuardCount.setText(String.valueOf(Simulator.currentMaintenanceGuardCount));

        // Current Guard to Guard Interaction Count
        currentGuardGuardCount.setText(String.valueOf(Simulator.currentGuardGuardCount));

        // Current Appliance Interaction Count
        currentAirconCount.setText(String.valueOf(Simulator.currentAirconCount));
        currentLightCount.setText(String.valueOf(Simulator.currentLightCount));
        currentMonitorCount.setText(String.valueOf(Simulator.currentMonitorCount));
        currentCoffeeMakerCount.setText(String.valueOf(Simulator.currentCoffeeMakerCount));

        currentAirconTurnOnCount.setText(String.valueOf(Simulator.currentAirconTurnOnCount));
        currentAirconTurnOffCount.setText(String.valueOf(Simulator.currentAirconTurnOffCount));

        currentLightTurnOnCount.setText(String.valueOf(Simulator.currentLightTurnOnCount));
        currentLightTurnOffCount.setText(String.valueOf(Simulator.currentLightTurnOffCount));

        currentFridgeInteractionCount.setText(String.valueOf(Simulator.currentFridgeInteractionCount));
        currentWaterDispenserInteractionCount.setText(String.valueOf(Simulator.currentWaterDispenserInteractionCount));

        // WATTAGE
        totalWattageCountText.setText("Total Watts: " + String.format("%.03f",Simulator.totalWattageCount) + " W");

    }


    public void setElements() {
        stackPane.setScaleX(CANVAS_SCALE);
        stackPane.setScaleY(CANVAS_SCALE);

        double rowsScaled = simulator.getEnvironment().getRows() * GraphicsController.tileSize;
        double columnsScaled = simulator.getEnvironment().getColumns() * GraphicsController.tileSize;

        stackPane.setPrefWidth(columnsScaled);
        stackPane.setPrefHeight(rowsScaled);

        backgroundCanvas.setWidth(columnsScaled);
        backgroundCanvas.setHeight(rowsScaled);

        foregroundCanvas.setWidth(columnsScaled);
        foregroundCanvas.setHeight(rowsScaled);

        markingsCanvas.setWidth(columnsScaled);
        markingsCanvas.setHeight(rowsScaled);
    }

    public void exportToCSV(){
        try {
            Simulator.exportToCSV();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void exportHeatMap() {
        try {
            Simulator.exportHeatMap();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }



    @FXML
    private void initialize() {
        speedSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            SimulationTime.SLEEP_TIME_MILLISECONDS.set((int) (1.0 / newVal.intValue() * 1000));
        });
        resetToDefault();
        playButton.setDisable(true);
        exportToCSVButton.setDisable(true);
        exportHeatMapButton.setDisable(true);


        // Variables
        int width = 129;
        int length = 204;
        int rows = (int) Math.ceil(width / Patch.PATCH_SIZE_IN_SQUARE_METERS);
        int columns = (int) Math.ceil(length / Patch.PATCH_SIZE_IN_SQUARE_METERS);
        Environment environment = Environment.Factory.create(rows, columns);
        simulator.resetToDefaultConfiguration(environment);

        // Configure
        Environment.configureDefaultIOS();
        Environment.configureDefaultInteractionTypeChances();

        // Copy
        environment.copyDefaultToIOS();
        environment.copyDefaultToInteractionTypeChances();

        // Set up zooming and panning
        setupZoomAndPan();
        adjustTitledPaneHeight(energyConsumptionPane, 400.0);
        adjustTitledPaneHeight(socialInteractionPane, 400.0);
    }

    @FXML
    public void initializeAction() {


        if (simulator.isRunning()) {
            playAction();
            playButton.setSelected(false);
        }

        if (validateParameters()) {
            Environment environment = simulator.getEnvironment();
            this.configureParameters(environment);
            initializeEnvironment(environment);
            environment.convertIOSToChances();
            setElements();
            playButton.setDisable(false);
            exportToCSVButton.setDisable(true);
            exportHeatMapButton.setDisable(true);
            simulator.replenishStaticVars();
            disableEdits();
        }
    }

    @FXML
    public void initializeLayoutA() {


        if (simulator.isRunning()) {
            playAction();
            playButton.setSelected(false);
        }

        if (validateParameters()) {
            Environment environment = simulator.getEnvironment();
            this.configureParameters(environment);
            initializeLayoutA(environment);
            environment.convertIOSToChances();
            setElements();
            playButton.setDisable(false);
            exportToCSVButton.setDisable(true);
            exportHeatMapButton.setDisable(true);
            simulator.replenishStaticVars();
            disableEdits();
        }
    }

    @FXML
    public void initializeLayoutB() {


        if (simulator.isRunning()) {
            playAction();
            playButton.setSelected(false);
        }

        if (validateParameters()) {
            Environment environment = simulator.getEnvironment();
            this.configureParameters(environment);
            initializeLayoutB(environment);
            environment.convertIOSToChances();
            setElements();
            playButton.setDisable(false);
            exportToCSVButton.setDisable(true);
            exportHeatMapButton.setDisable(true);
            simulator.replenishStaticVars();
            disableEdits();
        }
    }

    @FXML
    public void playAction() {
        if (!simulator.isRunning()) {
            simulator.setRunning(true);
            simulator.getPlaySemaphore().release();
            playButton.setText("Pause");
            exportToCSVButton.setDisable(true);
            exportHeatMapButton.setDisable(true);
        }
        else {
            simulator.setRunning(false);
            playButton.setText("Play");
            exportToCSVButton.setDisable(false);
            exportHeatMapButton.setDisable(false);
        }
    }


    // ZOOMING AND PANNING - PANNING DOES NOT WORK YET

    private void setupZoomAndPan() {
        stackPane.setOnScroll(this::handleZoom);
        stackPane.setOnMousePressed(this::handleMousePressed);
        stackPane.setOnMouseDragged(this::handleMouseDragged);
        stackPane.setOnMouseReleased(this::handleMouseReleased);
    }

    private void handleZoom(ScrollEvent event) {
        if (event.getDeltaY() == 0) {
            return;
        }

        double zoomFactorDelta = zoomDelta * Math.abs(event.getDeltaY()) / 40.0;

        if (event.getDeltaY() < 0) {
            zoomFactor -= zoomFactorDelta;
        } else {
            zoomFactor += zoomFactorDelta;
        }

        if (zoomFactor < 0.1) {
            zoomFactor = 0.1;
        }

        double currentScale = stackPane.getScaleX();
        double scaleChange = zoomFactor / currentScale;

        double f = (scaleChange - 1);

        double dx = (event.getX() - (stackPane.getBoundsInParent().getWidth() / 2 + stackPane.getBoundsInParent().getMinX()));
        double dy = (event.getY() - (stackPane.getBoundsInParent().getHeight() / 2 + stackPane.getBoundsInParent().getMinY()));

        stackPane.setScaleX(zoomFactor);
        stackPane.setScaleY(zoomFactor);

        stackPane.setTranslateX(stackPane.getTranslateX() - f * dx);
        stackPane.setTranslateY(stackPane.getTranslateY() - f * dy);
    }

    private void handleMousePressed(MouseEvent event) {
        mouseAnchorX = event.getX();
        mouseAnchorY = event.getY();

        translateAnchorX = stackPane.getTranslateX();
        translateAnchorY = stackPane.getTranslateY();

        stackPane.setCursor(Cursor.CLOSED_HAND);
    }

    private void handleMouseDragged(MouseEvent event) {
        stackPane.setTranslateX(translateAnchorX + event.getSceneX() - mouseAnchorX);
        stackPane.setTranslateY(translateAnchorY + event.getSceneY() - mouseAnchorY);
    }

    private void handleMouseReleased(MouseEvent event) {
        stackPane.setCursor(Cursor.DEFAULT);
    }

    private void adjustTitledPaneHeight(TitledPane pane, double expandedHeight) {
        pane.expandedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                pane.setPrefHeight(expandedHeight);
            } else {
                pane.setPrefHeight(0);
            }
            if(vbox != null){
                vbox.layout(); // Refresh the layout of the VBox to reflect changes
            }
        });
    }
}