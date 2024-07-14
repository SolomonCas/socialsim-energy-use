package com.socialsim.controller.controls;

import com.socialsim.controller.graphics.GraphicsController;
import com.socialsim.controller.graphics.amenity.mapper.*;
import com.socialsim.model.core.agent.AgentMovement;
import com.socialsim.model.core.environment.Environment;
import com.socialsim.model.core.environment.patchobject.passable.elevator.Elevator;
import com.socialsim.model.core.environment.patchobject.passable.goal.Switch;
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
import javafx.stage.Window;

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

        totalWattageCountText.setText("Total Watts: " + String.format("%.03f",Simulator.totalWattageCount) + " Wh");
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

    public void updateEnvironment(PatchField patchField) {
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


        /* Permanent Door Patches */

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
                {72, 22}, {72, 62}, {71, 99}, {71, 105},
                {71, 111}, {71, 125}, {62, 147},{62, 147},

                // Reception
                {66, 179},

                // Human Experience Room
                {73, 6},

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

        /* Switches */

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
                {verticalRecessedLights, 62, 138}, {verticalRecessedLights, 54, 150},
                {verticalRecessedLights, 62, 158}, {verticalRecessedLights, 62, 165},
                {verticalRecessedLights, 73, 138}, {verticalRecessedLights, 71, 165},
                {verticalRecessedLights, 83, 138}, {verticalRecessedLights, 80, 165},
                {verticalRecessedLights, 85, 177}, {verticalRecessedLights, 100, 177},
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
                {horizontalPendantLights, 75, 62}, {horizontalPendantLights, 75, 118},
                {verticalPendantLights, 72, 33},
                {singlePendantLights, 75, 78}, {singlePendantLights, 75, 106},

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

        List<Patch> glass = new ArrayList<>();
        List<Patch> facultyGlass = new ArrayList<>();
        List<Patch> eastHallwayWindowBlinds = new ArrayList<>();
        List<Patch> northSouthWindowBlinds = new ArrayList<>();
        List<Patch> eastPantryWindowBlinds = new ArrayList<>();
        List<Patch> westWindowBlinds = new ArrayList<>();
        List<Patch> westDirectorWindowBlinds = new ArrayList<>();

        // Hallway
        eastHallwayWindowBlinds.add(environment.getPatch(61, 1));

        // Data Collection Room
        glass.add(environment.getPatch(88, 1));

        for (int j = 8; j <= 21; j += 5) {
            northSouthWindowBlinds.add(environment.getPatch(104, j));
        }

        // Research Center
        for (int j = 24; j <= 97; j += 5) {
            northSouthWindowBlinds.add(environment.getPatch(106, j));

        }

        // Faculty Room
        for (int j = 100; j <= 127; j += 6) {
            facultyGlass.add(environment.getPatch(109, j));

        }

        // Pantry Room
        eastPantryWindowBlinds.add(environment.getPatch(115, 135));


        for (int j = 136; j <= 184; j += 5) {
            northSouthWindowBlinds.add(environment.getPatch(124, j));
        }

        // Director Bathroom
        for (int i = 81; i <= 86; i += 4) {
            westDirectorWindowBlinds.add(environment.getPatch(i, 202));
        }


        // Director Room
        for (int i = 93; i <= 106; i += 5) {
            westWindowBlinds.add(environment.getPatch(i, 202));

        }

        for (int j = 188; j <= 201; j += 5) {
            northSouthWindowBlinds.add(environment.getPatch(113, j));

        }

        WindowBlindsMapper.draw(glass, "GLASS", 7);
        WindowBlindsMapper.draw(facultyGlass, "GLASS", 4);
        WindowBlindsMapper.draw(northSouthWindowBlinds, "OPENED_NORTH_AND_SOUTH", 4);
        WindowBlindsMapper.draw(eastPantryWindowBlinds, "OPENED_EAST", 8);
        WindowBlindsMapper.draw(eastHallwayWindowBlinds, "OPENED_EAST", 5);
        WindowBlindsMapper.draw(westWindowBlinds, "OPENED_WEST", 4);
        WindowBlindsMapper.draw(westDirectorWindowBlinds, "OPENED_WEST", 2);


        /* CABINETS & DRAWERS + STORAGE */

        List<Patch> storage = new ArrayList<>();
        List<Patch> southCabinet = new ArrayList<>();
        List<Patch> southDrawers = new ArrayList<>();
        List<Patch> northCabinet1x2 = new ArrayList<>();
        List<Patch> eastDoubleDrawers = new ArrayList<>();
        List<Patch> westDoubleDrawers = new ArrayList<>();

        storage.add(environment.getPatch(85, 187));
        StorageMapper.draw(storage, "DOUBLE_DRAWERS", "EAST");


        southCabinet.add(environment.getPatch(52, 147));
        CabinetDrawerMapper.draw(southCabinet, "CABINET", "SOUTH");

        southDrawers.add(environment.getPatch(50, 151));
        southDrawers.add(environment.getPatch(50, 152));
        CabinetDrawerMapper.draw(southDrawers, "DRAWERS", "SOUTH");

        northCabinet1x2.add(environment.getPatch(88, 129));
        CabinetDrawerMapper.draw(northCabinet1x2, "CABINET_1x2", "NORTH");

        eastDoubleDrawers.add(environment.getPatch(72, 162));
        CabinetDrawerMapper.draw(eastDoubleDrawers, "DOUBLE_DRAWERS", "EAST");

        westDoubleDrawers.add(environment.getPatch(72, 168));
        CabinetDrawerMapper.draw(westDoubleDrawers, "DOUBLE_DRAWERS", "WEST");

        /* SERVER */

        List<Patch> serverTypeA = new ArrayList<>();
        serverTypeA.add(environment.getPatch(43, 129));
        ServerMapper.draw(serverTypeA, "TYPE_A");

        List<Patch> serverTypeB = new ArrayList<>();
        serverTypeB.add(environment.getPatch(47, 134));
        serverTypeB.add(environment.getPatch(47, 135));
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
        int maxRows = environment.getRows();
        int maxColumns = environment.getColumns();

        /* Floor */

        List<Patch> floor = new ArrayList<>();

        for (int i = 0; i < maxRows; i++) {
            for (int j = 0; j < maxColumns; j++) {
                floor.add(environment.getPatch(i, j));
            }
        }

        simulator.getEnvironment().getFloors().add(Floor.floorFactory.create(floor, "floor"));


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
            for (int j = 0; j <= 203; j++ ) {
                parkingLot.add(environment.getPatch(i, j));
            }
        }

        simulator.getEnvironment().getDividers().add(Divider.dividerFactory.create(parkingLot, "parkingLot"));


        /* Floors (for each zone inside office) */

        List<Patch>
                    // consistent across all layouts
                    maleBathroom = new ArrayList<>(),femaleBathroom = new ArrayList<>(),
                    breakerRoom = new ArrayList<>(), reception = new ArrayList<>(), pantry = new ArrayList<>(),

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
                {maleBathroom, "maleBathroom", 4, 13, 186, 202}, {maleBathroom, "maleBathroom", 14, 18, 191, 202},
                {femaleBathroom, "femaleBathroom", 65, 74, 186, 202}, {femaleBathroom, "femaleBathroom", 60, 64, 191, 202},
                {breakerRoom, "", 18, 21, 22, 30}, {reception, "", 56, 75, 169, 183}, {pantry, "", 111, 124, 135, 185},

                // small to big changes in other layouts
                {directorRoom, "", 26, 59, 1, 16}, {directorBathroom, "directorBathroom", 26, 36, 8, 16},
                {conferenceRoom, "", 26, 45, 23, 50}, {meetingRoom, "", 26, 45, 52, 70},
                {dataCenter, "", 26, 45, 101, 125}, {dataCenterCCTV, "CCTV", 38, 45, 126, 145},
                {controlCenter, "", 46, 59, 110, 145}, {mesa, "", 82, 99, 149, 164},
                {SR1, "SR1", 56, 63, 89, 97}, {SR2, "SR2", 56, 63, 80, 88}, {SR3, "SR3", 56, 63, 42, 50}, {SR4, "SR4", 56, 63, 33, 41},
                {LS1, "LS1", 73, 106, 125, 145}, {LS2, "LS2", 73, 106, 104, 124}, {LS3, "LS3", 73, 106, 83, 103}, {LS4, "LS4", 73, 106, 62, 82},
                {researchCenter, "", 73, 106, 24, 61}, {facultyRoom, "", 26, 45, 72, 100},
                {humanExpRoom, "", 67, 86, 1, 14}, {dataCollectionRoom, "", 87, 104, 1, 22},
                {storageRoom, "", 80, 100, 186, 202}, {clinic, "", 101, 113, 186, 202},

        };


        for (Object[] range : floorRanges) {
            List<Patch> floorPatches =  (List<Patch>) range[0];
            String str = (String) range[1];
            int startRow = (int) range[2];
            int endRow = (int) range[3];
            int startColumn = (int) range[4];
            int endColumn = (int) range[5];

            for (int i = startRow; i <= endRow; i ++) {
                for (int j = startColumn; j <= endColumn; j ++) {
                    floorPatches.add(environment.getPatch(i, j));
                }
            }

            switch (floorPatches) {
                case List<Patch> list when list == maleBathroom || list == femaleBathroom || list == directorBathroom ->
                        simulator.getEnvironment().getBathrooms().add(Bathroom.bathroomFactory.create(floorPatches, str));
                case List<Patch> list when list == breakerRoom ->
                        simulator.getEnvironment().getBreakerRooms().add(BreakerRoom.breakerRoomFactory.create(floorPatches, str));
                case List<Patch> list when list == reception ->
                        simulator.getEnvironment().getReceptions().add(Reception.receptionFactory.create(floorPatches, str));
                case List<Patch> list when list == directorRoom ->
                        simulator.getEnvironment().getDirectorRooms().add(DirectorRoom.directorRoomFactory.create(floorPatches, str));
                case List<Patch> list when list == conferenceRoom ->
                        simulator.getEnvironment().getConferenceRooms().add(ConferenceRoom.conferenceRoomFactory.create(floorPatches, str));
                case List<Patch> list when list == meetingRoom ->
                        simulator.getEnvironment().getMeetingRooms().add(MeetingRoom.meetingRoomFactory.create(floorPatches, str));
                case List<Patch> list when list == dataCenter || list == dataCenterCCTV->
                        simulator.getEnvironment().getDataCenters().add(DataCenter.dataCenterFactory.create(floorPatches, str));
                case List<Patch> list when list == controlCenter ->
                        simulator.getEnvironment().getControlCenters().add(ControlCenter.controlCenterFactory.create(floorPatches, str));
                case List<Patch> list when list == SR1 || list == SR2 || list == SR3 || list == SR4 ->
                        simulator.getEnvironment().getSoloRooms().add(SoloRoom.soloRoomFactory.create(floorPatches, str));
                case List<Patch> list when list == LS1 || list == LS2 || list == LS3 || list == LS4 ->
                        simulator.getEnvironment().getLearningSpaces().add(LearningSpace.learningSpaceFactory.create(floorPatches, str));
                case List<Patch> list when list == researchCenter ->
                        simulator.getEnvironment().getResearchCenters().add(ResearchCenter.researchCenterFactory.create(floorPatches, str));
                case List<Patch> list when list == facultyRoom ->
                        simulator.getEnvironment().getFacultyRooms().add(FacultyRoom.facultyRoomFactory.create(floorPatches, str));
                case List<Patch> list when list == humanExpRoom ->
                        simulator.getEnvironment().getHumanExpRooms().add(HumanExpRoom.humanExpRoomFactory.create(floorPatches, str));
                case List<Patch> list when list == dataCollectionRoom ->
                        simulator.getEnvironment().getDataCollectionRooms().add(DataCollectionRoom.dataCollectionRoomFactory.create(floorPatches, str));
                case List<Patch> list when list == storageRoom ->
                        simulator.getEnvironment().getStorageRooms().add(StorageRoom.storageRoomFactory.create(floorPatches, str));
                case List<Patch> list when list == clinic ->
                        simulator.getEnvironment().getClinics().add(Clinic.clinicFactory.create(floorPatches, str));
                case List<Patch> list when list == mesa ->
                        simulator.getEnvironment().getMESAs().add(MESA.MESAFactory.create(floorPatches, str));
                case List<Patch> list when list == pantry ->
                        simulator.getEnvironment().getPantries().add(Pantry.pantryFactory.create(floorPatches, str));
                default -> throw new IllegalStateException("Unexpected value: " + floorPatches);
            }

        }


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
                {43, 99, 101}, {43, 108, 145}, {57, 109, 138}, {57, 145, 146},

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


        /* Permanent Door Patches */

        List<Patch> doorPatches = new ArrayList<>();

        int[][] doorRanges = {

                /* Permanent */

                // Elevators
                {193, 23}, {193, 34}, {193, 45},

                // Male Bathroom
                {191, 11},

                // Female Bathroom
                {191, 57},

                // Office
                {183, 57}, {169, 57}, {22, 15},

                /* Not Permanent (inside office) */



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
                {42, 99, 101}, {42, 108, 145}, {56, 109, 138}, {56, 145, 146},

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
                {22, 23, 41}, {51, 23, 41}, {71, 23, 41}, {109, 43, 55}, {146, 48, 55},

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


        /* Reception Table */
        List<Patch> receptionTable = new ArrayList<>();
        receptionTable.add(environment.getPatch(69,170));
        ReceptionTableMapper.draw(receptionTable, "1x8");


        /* Cubicles */

        List<Patch> nwMESA = new ArrayList<>(), neMESA = new ArrayList<>(), swMESA = new ArrayList<>(), seMESA = new ArrayList<>(),
                    cubicleA  = new ArrayList<>(), westCubicleB  = new ArrayList<>(), eastCubicleB  = new ArrayList<>(),
                    westCubicleC  = new ArrayList<>(), eastCubicleC  = new ArrayList<>(),
                    southCubicleC  = new ArrayList<>(), northCubicleC  = new ArrayList<>();


        Object[][] cubicleRanges =  {

                // MESA
                {nwMESA, "MESA", "", "NORTH_AND_WEST", false, 0, 84, 151}, {neMESA, "MESA", "", "NORTH_AND_EAST", false, 0, 84, 159},
                {swMESA, "MESA", "", "SOUTH_AND_WEST", false, 0, 93, 151}, {seMESA, "MESA", "", "SOUTH_AND_EAST", false, 0, 93, 159},

                // Type A
                {cubicleA, "TYPE_A", "", "", true, 1, 29, 105},

                // Type B
                {westCubicleB, "TYPE_B", "WEST", "", true, 2, 30, 77}, {westCubicleB, "TYPE_B", "WEST", "", true, 2, 34, 77},
                {westCubicleB, "TYPE_B", "WEST", "", true, 2, 30, 87}, {westCubicleB, "TYPE_B", "WEST", "", true, 2, 34, 87},
                {westCubicleB, "TYPE_B", "WEST", "", true, 2, 30, 97}, {westCubicleB, "TYPE_B", "WEST", "", true, 2, 34, 97},
                {eastCubicleB, "TYPE_B", "EAST", "", true, 2, 30, 80}, {eastCubicleB, "TYPE_B", "EAST", "", true, 2, 34, 80},
                {eastCubicleB, "TYPE_B", "EAST", "", true, 2, 30, 90}, {eastCubicleB, "TYPE_B", "EAST", "", true, 2, 34, 90},

                // Type C
                {westCubicleC, "TYPE_C", "WEST", "", false, 0, 29, 120}, {westCubicleC, "TYPE_C", "WEST", "", false, 0, 33, 120},
                {eastCubicleC, "TYPE_C", "EAST", "", false, 0, 29, 116}, {eastCubicleC, "TYPE_C", "EAST", "", false, 0, 33, 116},
                {northCubicleC, "TYPE_C", "NORTH", "", false, 0, 59, 36}, {northCubicleC, "TYPE_C", "NORTH", "", false, 0, 59, 92},
                {southCubicleC, "TYPE_C", "SOUTH", "", false, 0, 59, 45}, {southCubicleC, "TYPE_C", "SOUTH", "", false, 0, 59, 83},
        };


        for (Object[] range : cubicleRanges) {
            List<Patch> amenityPatches =  (List<Patch>) range[0];
            String type = (String) range[1];
            String facing = (String) range[2];
            String tableOn = (String) range[3];
            boolean withAppliance = (boolean) range[4];
            int numberOfMonitors = (int) range[5];
            int row = (int) range[6];
            int column = (int) range[7];

            amenityPatches.add(environment.getPatch(row, column));
            CubicleMapper.draw(amenityPatches, type, facing, tableOn, withAppliance, numberOfMonitors);
        }


        /* Research Tables */

        List<Patch> westResearchTable = new ArrayList<>(), westMonitorResearchTable = new ArrayList<>(),
                    eastResearchTable = new ArrayList<>(), eastMonitorResearchTable = new ArrayList<>();


        Object[][] researchTableRanges =  {

                {westResearchTable, "WEST", false, 79, 31}, {eastMonitorResearchTable, "EAST", true, 79, 32},
                {westMonitorResearchTable, "WEST", true, 90, 31}, {eastResearchTable, "EAST", false, 90, 32},
                {westMonitorResearchTable, "WEST", true, 102, 31}, {eastMonitorResearchTable, "EAST", true, 102, 32},

                {westMonitorResearchTable, "WEST", true, 79, 53}, {eastResearchTable, "EAST", false, 79, 54},
                {westResearchTable, "WEST", false, 90, 53}, {eastResearchTable, "EAST", false, 90, 54},
                {westResearchTable, "WEST", false, 102, 53}, {eastResearchTable, "EAST", false, 102, 54},
        };


        for (Object[] range : researchTableRanges) {
            List<Patch> amenityPatches =  (List<Patch>) range[0];
            String facing = (String) range[1];
            boolean withAppliance = (boolean) range[2];
            int row = (int) range[3];
            int column = (int) range[4];

            amenityPatches.add(environment.getPatch(row, column));
            ResearchTableMapper.draw(amenityPatches, facing, withAppliance);
        }


        /* Learning Tables */

        List<Patch> learningTables = new ArrayList<>();


        Object[][] learningTableRanges =  {

                // Learning Space 1
                {84, 129}, {84, 138},
                {98, 129}, {98, 138},

                // Learning Space 2
                {84, 108}, {84, 117},
                {98, 108}, {98, 117},

                // Learning Space 3
                {84, 87}, {84, 96},
                {98, 87}, {98, 96},

                // Learning Space 4
                {84, 66}, {84, 75},
                {98, 66}, {98, 75}
        };


        for (Object[] range : learningTableRanges) {
            List<Patch> amenityPatches =  (List<Patch>) range[0];
            String orientation = (String) range[1];
            int row = (int) range[2];
            int column = (int) range[3];

            amenityPatches.add(environment.getPatch(row, column));
            LearningTableMapper.draw(amenityPatches, orientation);
        }


        /* Meeting Tables */

        List<Patch> largeHorizontalMeetingTables = new ArrayList<>(), leftLargeHorizontalMeetingTables = new ArrayList<>(),
                    rightLargeHorizontalMeetingTables = new ArrayList<>(), smallVerticalMeetingTables = new ArrayList<>();


        Object[][] meetingTableRanges =  {

                // Conference Room
                {leftLargeHorizontalMeetingTables, "HORIZONTAL", "LARGE", "LEFT", 32, 28}, {rightLargeHorizontalMeetingTables, "HORIZONTAL", "LARGE", "RIGHT", 32, 37},

                // Meeting Room
                {largeHorizontalMeetingTables, "HORIZONTAL", "LARGE", "", 32, 57},

                // Director Room
                {smallVerticalMeetingTables, "VERTICAL", "SMALL", "", 43, 5},
        };


        for (Object[] range : meetingTableRanges) {
            List<Patch> amenityPatches =  (List<Patch>) range[0];
            String orientation = (String) range[1];
            String size = (String) range[2];
            String position = (String) range[3];
            int row = (int) range[4];
            int column = (int) range[5];

            amenityPatches.add(environment.getPatch(row, column));
            MeetingTableMapper.draw(amenityPatches, orientation, size, position);
        }


        /* Pantry Tables and Chairs */

        List<Patch> typeApantryTables = new ArrayList<>(), typeBpantryTables = new ArrayList<>(),
                typeApantryChairs = new ArrayList<>(), typeBpantryChairs = new ArrayList<>();


        Object[][] pantryTableRanges =  {

                // Type A
                {typeApantryTables, "TYPE_A", 117, 180}, {typeApantryTables, "TYPE_A", 120, 175},

                // Type B
                {typeBpantryTables, "TYPE_B", 117, 169}, {typeBpantryTables, "TYPE_B", 121, 139}, {typeBpantryTables, "TYPE_B", 121, 148},
                {typeBpantryTables, "TYPE_B", 121, 154}, {typeBpantryTables, "TYPE_B", 121, 160}
        };

        Object[][] pantryChairRanges =  {

                // Type A
                {typeApantryChairs, "PANTRY_TYPE_A", 111, 150}, {typeApantryChairs, "PANTRY_TYPE_A", 111, 151}, {typeApantryChairs, "PANTRY_TYPE_A", 111, 152},
                {typeApantryChairs, "PANTRY_TYPE_A", 111, 153}, {typeApantryChairs, "PANTRY_TYPE_A", 111, 154},

                // Type B
                {typeBpantryChairs, "PANTRY_TYPE_B", 111, 145}, {typeBpantryChairs, "PANTRY_TYPE_B", 111, 146}, {typeBpantryChairs, "PANTRY_TYPE_B", 111, 147},
                {typeBpantryChairs, "PANTRY_TYPE_B", 111, 148}, {typeBpantryChairs, "PANTRY_TYPE_B", 111, 149}
        };


        for (Object[] range : pantryTableRanges) {
            List<Patch> amenityPatches =  (List<Patch>) range[0];
            String type = (String) range[1];
            int row = (int) range[2];
            int column = (int) range[3];

            amenityPatches.add(environment.getPatch(row, column));
            PantryTableMapper.draw(amenityPatches, type);
        }

        for (Object[] range : pantryChairRanges) {
            List<Patch> amenityPatches =  (List<Patch>) range[0];
            String type = (String) range[1];
            int row = (int) range[2];
            int column = (int) range[3];

            amenityPatches.add(environment.getPatch(row, column));
            ChairMapper.draw(amenityPatches, 0, "SOUTH", type, "");
        }


        /* Human Experience Table */
        List<Patch> humanExpTable = new ArrayList<>();
        humanExpTable.add(environment.getPatch(77,5));
        HumanExpTableMapper.draw(humanExpTable, "5x1");

        /* Data Collection Table */
        List<Patch> dataCollTable = new ArrayList<>();
        dataCollTable.add(environment.getPatch(91,2));
        DataCollTableMapper.draw(dataCollTable, "1x6");

        /* Director Table */
        List<Patch> directorTable = new ArrayList<>();
        directorTable.add(environment.getPatch(40,12));
        DirectorTableMapper.draw(directorTable, "HORIZONTAL", "NORTH", true);

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

        /* Plant */
        List<Patch> plants = new ArrayList<>();
        plants.add(environment.getPatch(62,2));
        plants.add(environment.getPatch(74,179));
        plants.add(environment.getPatch(74,181));
        plants.add(environment.getPatch(75,180));
        plants.add(environment.getPatch(82,201));
        plants.add(environment.getPatch(84,201));
        plants.add(environment.getPatch(86,201));
        PlantMapper.draw(plants);

        /* Trash Can */
        List<Patch> trashCans = new ArrayList<>();
        trashCans.add(environment.getPatch(26,13));
        trashCans.add(environment.getPatch(40,11));
        trashCans.add(environment.getPatch(113,135));
        TrashCanMapper.draw(trashCans);

        /* Pantry Cabinet */
        List<Patch> pantryCabinets = new ArrayList<>();
        pantryCabinets.add(environment.getPatch(109,135));
        pantryCabinets.add(environment.getPatch(109,136));
        pantryCabinets.add(environment.getPatch(109,137));
        pantryCabinets.add(environment.getPatch(109,138));
        PantryCabinetMapper.draw(pantryCabinets);

        /* Office Toilet/Director Toilet */
        List<Patch> southOfficeToilets = new ArrayList<>();
        southOfficeToilets.add(environment.getPatch(79, 200));
        ToiletMapper.draw(southOfficeToilets, "SOUTH", "OfficeToilet");

        /* Director Sink/ Office Sink */
        List<Patch> southOfficeSinks = new ArrayList<>();
        southOfficeSinks.add(environment.getPatch(80,197));
        southOfficeSinks.add(environment.getPatch(111,136));
        SinkMapper.draw(southOfficeSinks, "SOUTH", "OfficeSink");

        /* Sink */

        // South
        List<Patch> southSinks = new ArrayList<>();
        southSinks.add(environment.getPatch(60,194));
        southSinks.add(environment.getPatch(60,197));
        southSinks.add(environment.getPatch(60,200));

        SinkMapper.draw(southSinks, "SOUTH", "Sink");

        // North
        List<Patch> northSinks = new ArrayList<>();
        northSinks.add(environment.getPatch(18,194));
        northSinks.add(environment.getPatch(18,197));
        northSinks.add(environment.getPatch(18,200));
        SinkMapper.draw(northSinks, "NORTH", "Sink");

        /* Toilet */

        // South
        List<Patch> southToilets = new ArrayList<>();
        southToilets.add(environment.getPatch(3,188));
        southToilets.add(environment.getPatch(3,191));
        southToilets.add(environment.getPatch(3,194));
        southToilets.add(environment.getPatch(3,197));
        southToilets.add(environment.getPatch(3,200));

        ToiletMapper.draw(southToilets, "SOUTH", "Toilet");

        // North
        List<Patch> northToilets = new ArrayList<>();
        northToilets.add(environment.getPatch(73,188));
        northToilets.add(environment.getPatch(73,191));
        northToilets.add(environment.getPatch(73,194));
        northToilets.add(environment.getPatch(73,197));
        northToilets.add(environment.getPatch(73,200));
        ToiletMapper.draw(northToilets, "NORTH", "Toilet");

        /* Coffee Maker Bar */
        List<Patch> coffeeMakerBar = new ArrayList<>();
        coffeeMakerBar.add(environment.getPatch(110,137));
        CoffeeMakerBarMapper.draw(coffeeMakerBar);

        /* Kettle Bar */
        List<Patch> kettleBar = new ArrayList<>();
        kettleBar.add(environment.getPatch(110,138));
        KettleBarMapper.draw(kettleBar);

        /* Microwave Bar */
        List<Patch> microwaveBar = new ArrayList<>();
        microwaveBar.add(environment.getPatch(111,135));
        MicrowaveBarMapper.draw(microwaveBar);


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

        currentAirconTurnOnCount.setText(String.valueOf(Simulator.currentAirconTurnOnCount));
        currentAirconTurnOffCount.setText(String.valueOf(Simulator.currentAirconTurnOffCount));

        currentLightTurnOnCount.setText(String.valueOf(Simulator.currentLightTurnOnCount));
        currentLightTurnOffCount.setText(String.valueOf(Simulator.currentLightTurnOffCount));

        currentFridgeInteractionCount.setText(String.valueOf(Simulator.currentFridgeInteractionCount));
        currentWaterDispenserInteractionCount.setText(String.valueOf(Simulator.currentWaterDispenserInteractionCount));

        // WATTAGE
        totalWattageCountText.setText("Total Watts: " + String.format("%.03f",Simulator.totalWattageCount) + " Wh");

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

//    @FXML
//    public void initializeLayoutB() {
//
//
//        if (simulator.isRunning()) {
//            playAction();
//            playButton.setSelected(false);
//        }
//
//        if (validateParameters()) {
//            Environment environment = simulator.getEnvironment();
//            this.configureParameters(environment);
//            initializeLayoutB(environment);
//            environment.convertIOSToChances();
//            setElements();
//            playButton.setDisable(false);
//            exportToCSVButton.setDisable(true);
//            exportHeatMapButton.setDisable(true);
//            simulator.replenishStaticVars();
//            disableEdits();
//        }
//    }

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