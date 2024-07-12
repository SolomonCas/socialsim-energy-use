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

    // Label: Current Agent Count
    @FXML private Label currentDirectorCount;
    @FXML private Label currentFacultyCount;
    @FXML private Label currentStudentCount;
    @FXML private Label currentMaintenanceCount;
    @FXML private Label currentGuardCount;

    // Current Interaction With Appliance Count
    @FXML private Label currentAirconInteractionCount;
    @FXML private Label currentLightInteractionCount;
    @FXML private Label currentFridgeInteractionCount;
    @FXML private Label currentWaterDispenserInteractionCount;

    // Label: Current Interaction Count
    @FXML private Label currentNonverbalCount;
    @FXML private Label currentCooperativeCount;
    @FXML private Label currentExchangeCount;

    // Label: Average Interaction Duration
    @FXML private Label averageNonverbalDuration;
    @FXML private Label averageCooperativeDuration;
    @FXML private Label averageExchangeDuration;


    // Label: Current Team Count
    @FXML private Label currentTeam1Count;
    @FXML private Label currentTeam2Count;
    @FXML private Label currentTeam3Count;
    @FXML private Label currentTeam4Count;


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
//    @FXML private Button resetToDefaultButton;


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
        currentAirconInteractionCount.setText(String.valueOf(Simulator.currentAirconInteractionCount));
        currentLightInteractionCount.setText(String.valueOf(Simulator.currentLightInteractionCount));
        currentFridgeInteractionCount.setText(String.valueOf(Simulator.currentFridgeInteractionCount));
        currentWaterDispenserInteractionCount.setText(String.valueOf(Simulator.currentWaterDispenserInteractionCount));

        currentTeam1Count.setText(String.valueOf(Simulator.currentTeam1Count));
        currentTeam2Count.setText(String.valueOf(Simulator.currentTeam2Count));
        currentTeam3Count.setText(String.valueOf(Simulator.currentTeam3Count));
        currentTeam4Count.setText(String.valueOf(Simulator.currentTeam4Count));



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

    }

    public boolean validateParameters() {
        boolean validParameters = Integer.parseInt(nonverbalMean.getText()) >= 0 && Integer.parseInt(nonverbalMean.getText()) >= 0
                && Integer.parseInt(cooperativeMean.getText()) >= 0 && Integer.parseInt(cooperativeStdDev.getText()) >= 0
                && Integer.parseInt(exchangeMean.getText()) >= 0 && Integer.parseInt(exchangeStdDev.getText()) >= 0
                && Integer.parseInt(fieldOfView.getText()) >= 0 && Integer.parseInt(fieldOfView.getText()) <= 360;
        if (!validParameters) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "", ButtonType.OK);
            Label label = new Label("Failed to initialize. Please make sure all values are greater than 0, and field of view is not greater than 360 degrees");
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
//        resetToDefaultButton.setDisable(true);
        configureIOSButton.setDisable(true);
        editInteractionButton.setDisable(true);
        //WATTAGE
//        waterDispenserWattage.setDisable(true);
//        waterDispenserWattageActive.setDisable(true);

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


        /* Male Bathroom */

        List<Patch> maleBathroom = new ArrayList<>();

        for (int i = 4; i <= 13; i++) {
            for (int j = 186; j <= 202; j++) {
                maleBathroom.add(environment.getPatch(i, j));
            }
        }

        for (int i = 14; i <= 18; i++) {
            for (int j = 191; j <= 202; j++) {
                maleBathroom.add(environment.getPatch(i, j));
            }
        }

        simulator.getEnvironment().getBathrooms().add(Bathroom.bathroomFactory.create(maleBathroom, "maleBathroom"));


        /* Female Bathroom */

        List<Patch> femaleBathroom = new ArrayList<>();

        for (int i = 60; i <= 64; i++) {
            for (int j = 191; j <= 202; j++) {
                femaleBathroom.add(environment.getPatch(i, j));
            }
        }

        for (int i = 65; i <= 74; i++) {
            for (int j = 186; j <= 202; j++) {
                femaleBathroom.add(environment.getPatch(i, j));
            }
        }

        simulator.getEnvironment().getBathrooms().add(Bathroom.bathroomFactory.create(femaleBathroom, "femaleBathroom"));


        /* Breaker Room */

        List<Patch> breakerRoom = new ArrayList<>();

        for (int i = 18; i <= 21; i++) {
            for (int j = 22; j <= 30; j++) {
                breakerRoom.add(environment.getPatch(i, j));
            }
        }

        simulator.getEnvironment().getBreakerRooms().add(BreakerRoom.breakerRoomFactory.create(breakerRoom, ""));


        /* Meeting Room */

        List<Patch> meetingRoom = new ArrayList<>();

        for (int i = 22; i <= 59; i++) {
            for (int j = 1; j <= 16; j++) {
                meetingRoom.add(environment.getPatch(i, j));
            }
        }

        simulator.getEnvironment().getMeetingRooms().add(MeetingRoom.meetingRoomFactory.create(meetingRoom, ""));


        /* Learning Space 4 */

        List<Patch> LS4 = new ArrayList<>();

        for (int i = 26; i <= 59; i++) {
            for (int j = 22; j <= 42; j++) {
                LS4.add(environment.getPatch(i, j));
            }
        }

        simulator.getEnvironment().getLearningSpaces().add(LearningSpace.learningSpaceFactory.create(LS4, "LS4"));

        /* Learning Space 3 */

        List<Patch> LS3 = new ArrayList<>();

        for (int i = 26; i <= 59; i++) {
            for (int j = 44; j <= 63; j++) {
                LS3.add(environment.getPatch(i, j));
            }
        }

        simulator.getEnvironment().getLearningSpaces().add(LearningSpace.learningSpaceFactory.create(LS3, "LS3"));

        /* Learning Space 2 */

        List<Patch> LS2 = new ArrayList<>();

        for (int i = 26; i <= 59; i++) {
            for (int j = 65; j <= 84; j++) {
                LS2.add(environment.getPatch(i, j));
            }
        }

        simulator.getEnvironment().getLearningSpaces().add(LearningSpace.learningSpaceFactory.create(LS2, "LS2"));

        /* Learning Space 1 */

        List<Patch> LS1 = new ArrayList<>();

        for (int i = 26; i <= 59; i++) {
            for (int j = 86; j <= 105; j++) {
                LS1.add(environment.getPatch(i, j));
            }
        }

        simulator.getEnvironment().getLearningSpaces().add(LearningSpace.learningSpaceFactory.create(LS1, "LS1"));


        /* Solo Room 4 */

        List<Patch> SR4 = new ArrayList<>();

        for (int i = 67; i <= 79; i++) {
            for (int j = 39; j <= 46; j++) {
                SR4.add(environment.getPatch(i, j));
            }
        }

        simulator.getEnvironment().getSoloRooms().add(SoloRoom.soloRoomFactory.create(SR4, "SR4"));

        /* Solo Room 3 */

        List<Patch> SR3 = new ArrayList<>();

        for (int i = 67; i <= 79; i++) {
            for (int j = 48; j <= 55; j++) {
                SR3.add(environment.getPatch(i, j));
            }
        }

        simulator.getEnvironment().getSoloRooms().add(SoloRoom.soloRoomFactory.create(SR3, "SR3"));

        /* Solo Room 2 */

        List<Patch> SR2 = new ArrayList<>();

        for (int i = 67; i <= 79; i++) {
            for (int j = 85; j <= 92; j++) {
                SR2.add(environment.getPatch(i, j));
            }
        }

        simulator.getEnvironment().getSoloRooms().add(SoloRoom.soloRoomFactory.create(SR2, "SR2"));

        /* Solo Room 1 */

        List<Patch> SR1 = new ArrayList<>();

        for (int i = 67; i <= 79; i++) {
            for (int j = 94; j <= 101; j++) {
                SR1.add(environment.getPatch(i, j));
            }
        }

        simulator.getEnvironment().getSoloRooms().add(SoloRoom.soloRoomFactory.create(SR1, "SR1"));


        /* MESA */

        List<Patch> mesa = new ArrayList<>();

        for (int i = 67; i <= 80; i++) {
            for (int j = 144; j <= 160; j++) {
                mesa.add(environment.getPatch(i, j));
            }
        }

        simulator.getEnvironment().getMESAs().add(MESA.MESAFactory.create(mesa, ""));


        /* Reception */

        List<Patch> reception = new ArrayList<>();

        for (int i = 56; i <= 75; i++) {
            for (int j = 169; j <= 183; j++) {
                reception.add(environment.getPatch(i, j));
            }
        }

        simulator.getEnvironment().getReceptions().add(Reception.receptionFactory.create(reception, ""));


        /* Data Center */

        List<Patch> dataCenter = new ArrayList<>();

        for (int i = 26; i <= 59; i++) {
            for (int j = 107; j <= 125; j++) {
                dataCenter.add(environment.getPatch(i, j));
            }
        }

        simulator.getEnvironment().getDataCenters().add(DataCenter.dataCenterFactory.create(dataCenter, ""));


        /* Control Center */

        List<Patch> controlCenter = new ArrayList<>();

        for (int i = 38; i <= 59; i++) {
            for (int j = 127; j <= 145; j++) {
                controlCenter.add(environment.getPatch(i, j));
            }
        }

        simulator.getEnvironment().getControlCenters().add(ControlCenter.controlCenterFactory.create(controlCenter, ""));


        /* Human Experiment Room */

        List<Patch> humanExpRoom = new ArrayList<>();

        for (int i = 67; i <= 86; i++) {
            for (int j = 1; j <= 15; j++) {
                humanExpRoom.add(environment.getPatch(i, j));
            }
        }

        simulator.getEnvironment().getHumanExpRooms().add(HumanExpRoom.humanExpRoomFactory.create(humanExpRoom, ""));


        /* Data Collection Room */

        List<Patch> dataCollectionRoom = new ArrayList<>();

        for (int i = 87; i <= 104; i++) {
            for (int j = 1; j <= 22; j++) {
                dataCollectionRoom.add(environment.getPatch(i, j));
            }
        }

        simulator.getEnvironment().getDataCollectionRooms().add(DataCollectionRoom.dataCollectionRoomFactory.create(dataCollectionRoom, ""));


        /* Research Center */

        List<Patch> researchCenter = new ArrayList<>();

        for (int i = 87; i <= 106; i++) {
            for (int j = 24; j <= 98; j++) {
                researchCenter.add(environment.getPatch(i, j));
            }
        }

        simulator.getEnvironment().getResearchCenters().add(ResearchCenter.researchCenterFactory.create(researchCenter, ""));


        /* Faculty Room */

        List<Patch> facultyRoom = new ArrayList<>();

        for (int i = 87; i <= 106; i++) {
            for (int j = 99; j <= 127; j++) {
                facultyRoom.add(environment.getPatch(i, j));
            }
        }

        simulator.getEnvironment().getFacultyRooms().add(FacultyRoom.facultyRoomFactory.create(facultyRoom, ""));


        /* Storage Room */

        List<Patch> storageRoom = new ArrayList<>();

        for (int i = 89; i <= 106; i++) {
            for (int j = 129; j <= 141; j++) {
                storageRoom.add(environment.getPatch(i, j));
            }
        }

        simulator.getEnvironment().getStorageRooms().add(StorageRoom.storageRoomFactory.create(storageRoom, ""));


        /* Conference Room */

        List<Patch> conferenceRoom = new ArrayList<>();

        for (int i = 89; i <= 106; i++) {
            for (int j = 143; j <= 168; j++) {
                conferenceRoom.add(environment.getPatch(i, j));
            }
        }

        simulator.getEnvironment().getConferenceRooms().add(ConferenceRoom.conferenceRoomFactory.create(conferenceRoom, ""));


        /* Clinic Room */

        List<Patch> clinicRoom = new ArrayList<>();

        for (int i = 80; i <= 91; i++) {
            for (int j = 186; j <= 193; j++) {
                clinicRoom.add(environment.getPatch(i, j));
            }
        }

        simulator.getEnvironment().getClinics().add(Clinic.clinicFactory.create(clinicRoom, ""));


        /* Director Bathroom */

        List<Patch> directorBathroom = new ArrayList<>();

        for (int i = 80; i <= 91; i++) {
            for (int j = 195; j <= 202; j++) {
                directorBathroom.add(environment.getPatch(i, j));
            }
        }

        simulator.getEnvironment().getBathrooms().add(Bathroom.bathroomFactory.create(directorBathroom, "directorBathroom"));


        /* Director Room */

        List<Patch> directorRoom = new ArrayList<>();

        for (int i = 92; i <= 113; i++) {
            for (int j = 186; j <= 202; j++) {
                directorRoom.add(environment.getPatch(i, j));
            }
        }

        simulator.getEnvironment().getDirectorRooms().add(DirectorRoom.directorRoomFactory.create(directorRoom, ""));


        /* Pantry */

        List<Patch> pantry = new ArrayList<>();

        for (int i = 111; i <= 124; i++) {
            for (int j = 135; j <= 185; j++) {
                pantry.add(environment.getPatch(i, j));
            }
        }

        simulator.getEnvironment().getPantries().add(Pantry.pantryFactory.create(pantry, ""));

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



        /* MESA Table and Chair Set */

        // Table on North and West
        List<Patch> NWMESATableChairSetPatches = new ArrayList<>();
        NWMESATableChairSetPatches.add(environment.getPatch(67,153));
        CubicleMapper.draw(NWMESATableChairSetPatches, "MESA", "", "NORTH_AND_WEST", false, 0);

        // Table on North and East
        List<Patch> NEMESATableChairSetPatches = new ArrayList<>();
        NEMESATableChairSetPatches.add(environment.getPatch(67,148));
        CubicleMapper.draw(NEMESATableChairSetPatches, "MESA", "", "NORTH_AND_EAST", false, 0);

        // Table on South and West
        List<Patch> SWMESATableChairSetPatches = new ArrayList<>();
        SWMESATableChairSetPatches.add(environment.getPatch(76,153));
        CubicleMapper.draw(SWMESATableChairSetPatches, "MESA", "", "SOUTH_AND_WEST", false, 0);

        // Table on South and East
        List<Patch> SEMESATableChairSetPatches = new ArrayList<>();
        SEMESATableChairSetPatches.add(environment.getPatch(76,148));
        CubicleMapper.draw(SEMESATableChairSetPatches, "MESA", "", "SOUTH_AND_EAST", false, 0);

        /* Cubicle Type A */

        // Two Cubicles Back-to-Back (Vertical) with appliance
        List<Patch> CubicleTypeA = new ArrayList<>();
        CubicleTypeA.add(environment.getPatch(43,111));
        CubicleMapper.draw(CubicleTypeA, "TYPE_A", "", "", true, 1);


        /* Cubicle Type B */

        // FACING WEST
        List<Patch> WCubicleTypeB = new ArrayList<>();
        WCubicleTypeB.add(environment.getPatch(95,105));
        WCubicleTypeB.add(environment.getPatch(95,115));
        WCubicleTypeB.add(environment.getPatch(95,125));
        WCubicleTypeB.add(environment.getPatch(99,105));
        WCubicleTypeB.add(environment.getPatch(99,115));
        WCubicleTypeB.add(environment.getPatch(99,125));
        CubicleMapper.draw(WCubicleTypeB, "TYPE_B", "WEST", "", true, 2);

        // FACING WEST
        List<Patch> ECubicleTypeB = new ArrayList<>();
        ECubicleTypeB.add(environment.getPatch(95,108));
        ECubicleTypeB.add(environment.getPatch(95,118));
        ECubicleTypeB.add(environment.getPatch(99,108));
        ECubicleTypeB.add(environment.getPatch(99,118));
        CubicleMapper.draw(ECubicleTypeB, "TYPE_B", "EAST", "", true, 2);


        /* Cubicle Type C */

        // FACING WEST
        List<Patch> WCubicleTypeC = new ArrayList<>();
        WCubicleTypeC.add(environment.getPatch(41,120));
        WCubicleTypeC.add(environment.getPatch(44,120));
        WCubicleTypeC.add(environment.getPatch(47,120));
        WCubicleTypeC.add(environment.getPatch(50,120));
        CubicleMapper.draw(WCubicleTypeC, "TYPE_C", "WEST", "", false, 0);


        /* Reception Table */
        List<Patch> ReceptionTable1x8 = new ArrayList<>();
        ReceptionTable1x8.add(environment.getPatch(69,170)); // Reception Bar
        ReceptionTableMapper.draw(ReceptionTable1x8, "1x8");

        /* Research Table */

        // Facing West
        List<Patch> westResearchTable = new ArrayList<>();
        westResearchTable.add(environment.getPatch(99,46));
        westResearchTable.add(environment.getPatch(99,68));
        westResearchTable.add(environment.getPatch(99,76));
        westResearchTable.add(environment.getPatch(99,38));
        westResearchTable.add(environment.getPatch(99,60));
        westResearchTable.add(environment.getPatch(99,90));
        ResearchTableMapper.draw(westResearchTable, "WEST", true);

        // Facing East
        List<Patch> eastResearchTable = new ArrayList<>();
        eastResearchTable.add(environment.getPatch(99,91));
        eastResearchTable.add(environment.getPatch(99,39));
        eastResearchTable.add(environment.getPatch(99,47));
        eastResearchTable.add(environment.getPatch(99,61));
        eastResearchTable.add(environment.getPatch(99,69));
        eastResearchTable.add(environment.getPatch(99,77));
        ResearchTableMapper.draw(eastResearchTable, "EAST", true);

        /* Meeting Table */

        // VERTICAL LARGE
        List<Patch> VerticalLargeTable = new ArrayList<>();
        VerticalLargeTable.add(environment.getPatch(35,7));
        MeetingTableMapper.draw(VerticalLargeTable, "VERTICAL", "LARGE", "");

        // VERTICAL SMALL
        List<Patch> VerticalSmallTable = new ArrayList<>();
        VerticalSmallTable.add(environment.getPatch(97,198));
        MeetingTableMapper.draw(VerticalSmallTable, "VERTICAL", "SMALL", "");

        // HORIZONTAL LARGE
        List<Patch> LeftHorizontalLargeTable = new ArrayList<>();
        LeftHorizontalLargeTable.add(environment.getPatch(98,147));
        MeetingTableMapper.draw(LeftHorizontalLargeTable, "HORIZONTAL", "LARGE", "LEFT");
        List<Patch> RightHorizontalLargeTable = new ArrayList<>();
        RightHorizontalLargeTable.add(environment.getPatch(98,156));
        MeetingTableMapper.draw(RightHorizontalLargeTable, "HORIZONTAL", "LARGE", "RIGHT");



        /* Learning Table */
        // HORIZONTAL
        List<Patch> HorizontalLearningTable = new ArrayList<>();
        HorizontalLearningTable.add(environment.getPatch(33,26));
        HorizontalLearningTable.add(environment.getPatch(33,35));
        HorizontalLearningTable.add(environment.getPatch(33,47));
        HorizontalLearningTable.add(environment.getPatch(33,56));
        HorizontalLearningTable.add(environment.getPatch(33,68));
        HorizontalLearningTable.add(environment.getPatch(33,77));
        HorizontalLearningTable.add(environment.getPatch(33,89));
        HorizontalLearningTable.add(environment.getPatch(33,98));
        HorizontalLearningTable.add(environment.getPatch(47,26));
        HorizontalLearningTable.add(environment.getPatch(47,35));
        HorizontalLearningTable.add(environment.getPatch(47,47));
        HorizontalLearningTable.add(environment.getPatch(47,56));
        HorizontalLearningTable.add(environment.getPatch(47,68));
        HorizontalLearningTable.add(environment.getPatch(47,77));
        HorizontalLearningTable.add(environment.getPatch(47,89));
        HorizontalLearningTable.add(environment.getPatch(47,98));
        LearningTableMapper.draw(HorizontalLearningTable, "HORIZONTAL");

        /* Pantry Table */

        // TYPE A
        List<Patch> pantryTableTypeA = new ArrayList<>();
        pantryTableTypeA.add(environment.getPatch(117,180));
        pantryTableTypeA.add(environment.getPatch(120,175));
        PantryTableMapper.draw(pantryTableTypeA, "TYPE_A");

        // TYPE B
        List<Patch> pantryTableTypeB = new ArrayList<>();
        pantryTableTypeB.add(environment.getPatch(117,169));
        pantryTableTypeB.add(environment.getPatch(121,139));
        pantryTableTypeB.add(environment.getPatch(121,148));
        pantryTableTypeB.add(environment.getPatch(121,154));
        pantryTableTypeB.add(environment.getPatch(121,160));
        PantryTableMapper.draw(pantryTableTypeB, "TYPE_B");

//        // TYPE A CHAIRS
//        List<Patch> southPantryChairTypeA = new ArrayList<>();
//        southPantryChairTypeA.add(environment.getPatch(111,150));
//        southPantryChairTypeA.add(environment.getPatch(111,151));
//        southPantryChairTypeA.add(environment.getPatch(111,152));
//        southPantryChairTypeA.add(environment.getPatch(111,153));
//        southPantryChairTypeA.add(environment.getPatch(111,154));
//        ChairMapper.draw(southPantryChairTypeA, 0, "SOUTH", "PANTRY_TYPE_A", "NULL");
//
//        // TYPE B CHAIRS
//        List<Patch> southPantryChairTypeB = new ArrayList<>();
//        southPantryChairTypeB.add(environment.getPatch(111,145));
//        southPantryChairTypeB.add(environment.getPatch(111,146));
//        southPantryChairTypeB.add(environment.getPatch(111,147));
//        southPantryChairTypeB.add(environment.getPatch(111,148));
//        southPantryChairTypeB.add(environment.getPatch(111,149));
//        ChairMapper.draw(southPantryChairTypeB, 0, "SOUTH", "PANTRY_TYPE_B", "NULL");


        /* Director Table */

        List<Patch> directorTable = new ArrayList<>();
        directorTable.add(environment.getPatch(108,190));
        DirectorTableMapper.draw(directorTable, "HORIZONTAL", true);


        /* TABLE 2x2 */
        List<Patch> table2x2 = new ArrayList<>();
        table2x2.add(environment.getPatch(45,127));
        Table2x2Mapper.draw(table2x2);


        /* Solo Table */

        // TOP
        List<Patch> topSoloTables = new ArrayList<>();
        topSoloTables.add(environment.getPatch(71,48));
        topSoloTables.add(environment.getPatch(71,85));
        SoloTableMapper.draw(topSoloTables, "1x8", "TOP");

        // BOTTOM
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

        // NORTH
        List<Patch> NorthWhiteBoard = new ArrayList<>();
        NorthWhiteBoard.add(environment.getPatch(95,31));
        NorthWhiteBoard.add(environment.getPatch(95,53));
        WhiteboardMapper.draw(NorthWhiteBoard, "NORTH", "2");

        // SOUTH
        List<Patch> SouthWhiteBoard = new ArrayList<>();
        SouthWhiteBoard.add(environment.getPatch(23,25));
        SouthWhiteBoard.add(environment.getPatch(23,35));
        SouthWhiteBoard.add(environment.getPatch(23,47));
        SouthWhiteBoard.add(environment.getPatch(23,56));
        SouthWhiteBoard.add(environment.getPatch(23,68));
        SouthWhiteBoard.add(environment.getPatch(23,77));
        SouthWhiteBoard.add(environment.getPatch(23,89));
        SouthWhiteBoard.add(environment.getPatch(23,98));
        WhiteboardMapper.draw(SouthWhiteBoard, "SOUTH", "5");

        // WEST
        List<Patch> WestWhiteBoard = new ArrayList<>();
        WestWhiteBoard.add(environment.getPatch(96,30));
        WestWhiteBoard.add(environment.getPatch(96,52));
        WhiteboardMapper.draw(WestWhiteBoard, "WEST", "4");

        // EAST 4
        List<Patch> East4WhiteBoard = new ArrayList<>();
        East4WhiteBoard.add(environment.getPatch(96,33));
        East4WhiteBoard.add(environment.getPatch(96,55));
        WhiteboardMapper.draw(East4WhiteBoard, "EAST", "4");

        // EAST 11
        List<Patch> East11WhiteBoard = new ArrayList<>();
        East11WhiteBoard.add(environment.getPatch(35,1));
        WhiteboardMapper.draw(East11WhiteBoard, "EAST", "11");

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
        trashCans.add(environment.getPatch(80,199));
        trashCans.add(environment.getPatch(113,135));
        trashCans.add(environment.getPatch(107,201));
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

        /* Switches */

        // South Light Switches
        List<Patch> southLightSwitches = new ArrayList<>();
        southLightSwitches.add(environment.getPatch(24,13));
        southLightSwitches.add(environment.getPatch(69,12));
        southLightSwitches.add(environment.getPatch(36,115));
        southLightSwitches.add(environment.getPatch(89,117));
        southLightSwitches.add(environment.getPatch(53,136));
        southLightSwitches.add(environment.getPatch(91,133));
        southLightSwitches.add(environment.getPatch(91,151));
        southLightSwitches.add(environment.getPatch(58,174));
        southLightSwitches.add(environment.getPatch(78,188));
        southLightSwitches.add(environment.getPatch(90,188));
        SwitchMapper.draw(southLightSwitches, "LIGHT", "SOUTH");

        // South AC Switches
        List<Patch> southACSwitches = new ArrayList<>();
        southACSwitches.add(environment.getPatch(24,14));
        southACSwitches.add(environment.getPatch(69,13));
        southACSwitches.add(environment.getPatch(36,116));
        southACSwitches.add(environment.getPatch(89,118));
        southACSwitches.add(environment.getPatch(53,137));
        southACSwitches.add(environment.getPatch(91,134));
        southACSwitches.add(environment.getPatch(91,152));
        southACSwitches.add(environment.getPatch(58,175));
        southACSwitches.add(environment.getPatch(78,189));
        southACSwitches.add(environment.getPatch(90,189));
        southACSwitches.add(environment.getPatch(109,168));
        SwitchMapper.draw(southACSwitches, "AC", "SOUTH");

        // North Light Switches
        List<Patch> northLightSwitches = new ArrayList<>();
        northLightSwitches.add(environment.getPatch(95,4));
        SwitchMapper.draw(northLightSwitches, "LIGHT", "NORTH");

        // North AC Switches
        List<Patch> northACSwitches = new ArrayList<>();
        northACSwitches.add(environment.getPatch(95,5));
        SwitchMapper.draw(northACSwitches, "AC", "NORTH");

        // East Light Switches
        List<Patch> eastLightSwitches = new ArrayList<>();
        eastLightSwitches.add(environment.getPatch(53,23));
        eastLightSwitches.add(environment.getPatch(72,39));
        eastLightSwitches.add(environment.getPatch(74,85));
        eastLightSwitches.add(environment.getPatch(56,147));
        eastLightSwitches.add(environment.getPatch(87,195));
        SwitchMapper.draw(eastLightSwitches, "LIGHT", "EAST");

        // East AC Switches
        List<Patch> eastACSwitches = new ArrayList<>();
        eastACSwitches.add(environment.getPatch(54,23));
        eastACSwitches.add(environment.getPatch(57,147));
        SwitchMapper.draw(eastACSwitches, "AC", "EAST");

        // West Light Switches
        List<Patch> westLightSwitches = new ArrayList<>();
        westLightSwitches.add(environment.getPatch(74,55));
        westLightSwitches.add(environment.getPatch(72,101));
        westLightSwitches.add(environment.getPatch(53,105));
        westLightSwitches.add(environment.getPatch(103,82));
        westLightSwitches.add(environment.getPatch(112,185));
        SwitchMapper.draw(westLightSwitches, "LIGHT", "WEST");

        // West AC Switches
        List<Patch> westACSwitches = new ArrayList<>();
        westACSwitches.add(environment.getPatch(54,105));
        westACSwitches.add(environment.getPatch(104,82));
        SwitchMapper.draw(westACSwitches, "AC", "WEST");

        /* Aircon */
        List<Patch> aircons = new ArrayList<>();
        aircons.add(environment.getPatch(40,7));
        aircons.add(environment.getPatch(94,9));

        // Research Center
        aircons.add(environment.getPatch(92,34));
        aircons.add(environment.getPatch(92,40));
        aircons.add(environment.getPatch(97,71));
        aircons.add(environment.getPatch(95,88));

        // Faculty Room
        aircons.add(environment.getPatch(95,101));
        aircons.add(environment.getPatch(95,112));
        aircons.add(environment.getPatch(100,122));

        // Conference Room
        aircons.add(environment.getPatch(94,149));
        aircons.add(environment.getPatch(94,161));

        aircons.add(environment.getPatch(119,151));
        aircons.add(environment.getPatch(99,191));
        aircons.add(environment.getPatch(82,189));
        aircons.add(environment.getPatch(66,175));
        aircons.add(environment.getPatch(39,132));
        aircons.add(environment.getPatch(41,117));
        aircons.add(environment.getPatch(50,117));
        aircons.add(environment.getPatch(39,139));
        aircons.add(environment.getPatch(27,99));
        aircons.add(environment.getPatch(27,78));
        aircons.add(environment.getPatch(47,70));
        aircons.add(environment.getPatch(27,55));
        aircons.add(environment.getPatch(50,48));
        aircons.add(environment.getPatch(50,56));
        aircons.add(environment.getPatch(28,35));
        aircons.add(environment.getPatch(50,25));

        aircons.add(environment.getPatch(72,21));
        aircons.add(environment.getPatch(72,62));
        aircons.add(environment.getPatch(72,99));
        aircons.add(environment.getPatch(72,111));
        aircons.add(environment.getPatch(72,125));
        AirconMapper.draw(aircons);

        /* Lights */

        // SINGLE_PENDANT_LIGHT
        List<Patch> singlePendantLights = new ArrayList<>();

        // Research Center
        singlePendantLights.add(environment.getPatch(97,27));
        singlePendantLights.add(environment.getPatch(103,27));
        singlePendantLights.add(environment.getPatch(93,69));
        singlePendantLights.add(environment.getPatch(93,76));
        singlePendantLights.add(environment.getPatch(93,83));
        singlePendantLights.add(environment.getPatch(93,90));

        // Pantry
        singlePendantLights.add(environment.getPatch(117,136));
        singlePendantLights.add(environment.getPatch(120,136));
        singlePendantLights.add(environment.getPatch(123,136));
        singlePendantLights.add(environment.getPatch(114,184));
        singlePendantLights.add(environment.getPatch(121,184));

        // Director Room
        singlePendantLights.add(environment.getPatch(98,198));

        // Director Bathroom
        singlePendantLights.add(environment.getPatch(82,197));
        singlePendantLights.add(environment.getPatch(82,200));

        // Hall
        singlePendantLights.add(environment.getPatch(75,78));
        singlePendantLights.add(environment.getPatch(75,106));

        LightMapper.draw(singlePendantLights, "SINGLE_PENDANT_LIGHT", "");


        //  HORIZONTAL LINEAR_PENDANT_LIGHT
        List<Patch> horizontalLinearPendantLights = new ArrayList<>();

        // Data Coll Room
        horizontalLinearPendantLights.add(environment.getPatch(91,5));

        // Faculty Room
        horizontalLinearPendantLights.add(environment.getPatch(97,112));
        horizontalLinearPendantLights.add(environment.getPatch(98,112));

        // Conference Room
        horizontalLinearPendantLights.add(environment.getPatch(99,153));
        horizontalLinearPendantLights.add(environment.getPatch(99,155));
        horizontalLinearPendantLights.add(environment.getPatch(99,157));

        // Director Room
        horizontalLinearPendantLights.add(environment.getPatch(107,191));

        // MESA
        horizontalLinearPendantLights.add(environment.getPatch(68,146));
        horizontalLinearPendantLights.add(environment.getPatch(79,146));
        horizontalLinearPendantLights.add(environment.getPatch(68,157));
        horizontalLinearPendantLights.add(environment.getPatch(79,157));

        // Hall
        horizontalLinearPendantLights.add(environment.getPatch(75,62));
        horizontalLinearPendantLights.add(environment.getPatch(75,118));



        LightMapper.draw(horizontalLinearPendantLights, "LINEAR_PENDANT_LIGHT", "HORIZONTAL");

        //  VERTICAL LINEAR_PENDANT_LIGHT
        List<Patch> verticalLinearPendantLights = new ArrayList<>();

        // Hall
        verticalLinearPendantLights.add(environment.getPatch(73,33));
        verticalLinearPendantLights.add(environment.getPatch(75,33));

        // Research Center
        verticalLinearPendantLights.add(environment.getPatch(99,36));
        verticalLinearPendantLights.add(environment.getPatch(101,36));
        verticalLinearPendantLights.add(environment.getPatch(99,44));
        verticalLinearPendantLights.add(environment.getPatch(101,44));
        verticalLinearPendantLights.add(environment.getPatch(99,58));
        verticalLinearPendantLights.add(environment.getPatch(101,58));
        verticalLinearPendantLights.add(environment.getPatch(99,66));
        verticalLinearPendantLights.add(environment.getPatch(101,66));
        verticalLinearPendantLights.add(environment.getPatch(99,74));
        verticalLinearPendantLights.add(environment.getPatch(101,74));
        verticalLinearPendantLights.add(environment.getPatch(99,88));
        verticalLinearPendantLights.add(environment.getPatch(101,88));

        LightMapper.draw(verticalLinearPendantLights, "LINEAR_PENDANT_LIGHT", "VERTICAL");

        //  HORIZONTAL RECESSED_LINEAR_LIGHT
        List<Patch> horizontalRecessedLinearLights = new ArrayList<>();

        // Pantry
        horizontalRecessedLinearLights.add(environment.getPatch(114,147));
        horizontalRecessedLinearLights.add(environment.getPatch(114,158));
        horizontalRecessedLinearLights.add(environment.getPatch(114,170));
        horizontalRecessedLinearLights.add(environment.getPatch(123,161));
        horizontalRecessedLinearLights.add(environment.getPatch(123,170));

        // Hall
        horizontalRecessedLinearLights.add(environment.getPatch(63,22));
        horizontalRecessedLinearLights.add(environment.getPatch(63,30));
        horizontalRecessedLinearLights.add(environment.getPatch(63,38));
        horizontalRecessedLinearLights.add(environment.getPatch(63,46));
        horizontalRecessedLinearLights.add(environment.getPatch(63,54));
        horizontalRecessedLinearLights.add(environment.getPatch(63,62));
        horizontalRecessedLinearLights.add(environment.getPatch(63,70));
        horizontalRecessedLinearLights.add(environment.getPatch(63,78));
        horizontalRecessedLinearLights.add(environment.getPatch(63,86));
        horizontalRecessedLinearLights.add(environment.getPatch(63,94));
        horizontalRecessedLinearLights.add(environment.getPatch(63,102));
        horizontalRecessedLinearLights.add(environment.getPatch(63,110));
        horizontalRecessedLinearLights.add(environment.getPatch(63,118));
        horizontalRecessedLinearLights.add(environment.getPatch(63,126));
        horizontalRecessedLinearLights.add(environment.getPatch(83,22));
        horizontalRecessedLinearLights.add(environment.getPatch(83,30));
        horizontalRecessedLinearLights.add(environment.getPatch(83,38));
        horizontalRecessedLinearLights.add(environment.getPatch(83,46));
        horizontalRecessedLinearLights.add(environment.getPatch(83,54));
        horizontalRecessedLinearLights.add(environment.getPatch(83,62));
        horizontalRecessedLinearLights.add(environment.getPatch(83,70));
        horizontalRecessedLinearLights.add(environment.getPatch(83,78));
        horizontalRecessedLinearLights.add(environment.getPatch(83,86));
        horizontalRecessedLinearLights.add(environment.getPatch(83,94));
        horizontalRecessedLinearLights.add(environment.getPatch(83,102));
        horizontalRecessedLinearLights.add(environment.getPatch(83,110));
        horizontalRecessedLinearLights.add(environment.getPatch(83,118));
        horizontalRecessedLinearLights.add(environment.getPatch(83,126));

        LightMapper.draw(horizontalRecessedLinearLights, "RECESSED_LINEAR_LIGHT", "HORIZONTAL");


        //  VERTICAL RECESSED_LINEAR_LIGHT
        List<Patch> verticalRecessedLinearLights = new ArrayList<>();

        // MeetingRoom
        verticalRecessedLinearLights.add(environment.getPatch(31,8));
        verticalRecessedLinearLights.add(environment.getPatch(46,8));

        // HumanExpRoom
        verticalRecessedLinearLights.add(environment.getPatch(78,6));
        verticalRecessedLinearLights.add(environment.getPatch(78,10));

        // Data Coll Room
        verticalRecessedLinearLights.add(environment.getPatch(93,5));

        // Research Center
        verticalRecessedLinearLights.add(environment.getPatch(92,33));
        verticalRecessedLinearLights.add(environment.getPatch(92,39));
        verticalRecessedLinearLights.add(environment.getPatch(92,45));
        verticalRecessedLinearLights.add(environment.getPatch(92,51));
        verticalRecessedLinearLights.add(environment.getPatch(92,64));
        verticalRecessedLinearLights.add(environment.getPatch(92,71));
        verticalRecessedLinearLights.add(environment.getPatch(92,78));
        verticalRecessedLinearLights.add(environment.getPatch(92,85));
        verticalRecessedLinearLights.add(environment.getPatch(92,92));
        verticalRecessedLinearLights.add(environment.getPatch(97,39));
        verticalRecessedLinearLights.add(environment.getPatch(97,46));
        verticalRecessedLinearLights.add(environment.getPatch(97,70));
        verticalRecessedLinearLights.add(environment.getPatch(97,78));
        verticalRecessedLinearLights.add(environment.getPatch(97,85));
        verticalRecessedLinearLights.add(environment.getPatch(97,92));

        // Faculty Room
        verticalRecessedLinearLights.add(environment.getPatch(92,102));
        verticalRecessedLinearLights.add(environment.getPatch(92,107));
        verticalRecessedLinearLights.add(environment.getPatch(92,114));
        verticalRecessedLinearLights.add(environment.getPatch(98,107));
        verticalRecessedLinearLights.add(environment.getPatch(98,114));

        // Storage Room
        verticalRecessedLinearLights.add(environment.getPatch(95,134));
        verticalRecessedLinearLights.add(environment.getPatch(103,134));

        // Conference Room
        verticalRecessedLinearLights.add(environment.getPatch(95,158));

        // Director Room
        verticalRecessedLinearLights.add(environment.getPatch(93,189));
        verticalRecessedLinearLights.add(environment.getPatch(104,189));
        verticalRecessedLinearLights.add(environment.getPatch(111,189));
        verticalRecessedLinearLights.add(environment.getPatch(93,194));
        verticalRecessedLinearLights.add(environment.getPatch(109,194));

        // Clinic
        verticalRecessedLinearLights.add(environment.getPatch(82,187));
        verticalRecessedLinearLights.add(environment.getPatch(82,192));

        // Reception
        verticalRecessedLinearLights.add(environment.getPatch(62,172));
        verticalRecessedLinearLights.add(environment.getPatch(71,172));
        verticalRecessedLinearLights.add(environment.getPatch(62,179));
        verticalRecessedLinearLights.add(environment.getPatch(71,179));

        // Data Center
        verticalRecessedLinearLights.add(environment.getPatch(39,129));
        verticalRecessedLinearLights.add(environment.getPatch(39,136));
        verticalRecessedLinearLights.add(environment.getPatch(39,143));

        // Control Center
        verticalRecessedLinearLights.add(environment.getPatch(39,109));
        verticalRecessedLinearLights.add(environment.getPatch(52,109));
        verticalRecessedLinearLights.add(environment.getPatch(39,116));
        verticalRecessedLinearLights.add(environment.getPatch(39,123));
        verticalRecessedLinearLights.add(environment.getPatch(52,123));

        // Learning Space 1
        verticalRecessedLinearLights.add(environment.getPatch(27,88));
        verticalRecessedLinearLights.add(environment.getPatch(52,88));
        verticalRecessedLinearLights.add(environment.getPatch(27,96));
        verticalRecessedLinearLights.add(environment.getPatch(52,96));

        // Learning Space 2
        verticalRecessedLinearLights.add(environment.getPatch(27,67));
        verticalRecessedLinearLights.add(environment.getPatch(52,67));
        verticalRecessedLinearLights.add(environment.getPatch(27,75));
        verticalRecessedLinearLights.add(environment.getPatch(52,75));

        // Learning Space 3
        verticalRecessedLinearLights.add(environment.getPatch(28,46));
        verticalRecessedLinearLights.add(environment.getPatch(28,58));
        verticalRecessedLinearLights.add(environment.getPatch(52,54));

        // Learning Space 4
        verticalRecessedLinearLights.add(environment.getPatch(28,25));
        verticalRecessedLinearLights.add(environment.getPatch(28,40));
        verticalRecessedLinearLights.add(environment.getPatch(50,33));

        // Hall
        verticalRecessedLinearLights.add(environment.getPatch(31,19));
        verticalRecessedLinearLights.add(environment.getPatch(49,19));
        verticalRecessedLinearLights.add(environment.getPatch(62,4));
        verticalRecessedLinearLights.add(environment.getPatch(62,16));
        verticalRecessedLinearLights.add(environment.getPatch(54,149));
        verticalRecessedLinearLights.add(environment.getPatch(62,138));
        verticalRecessedLinearLights.add(environment.getPatch(62,158));
        verticalRecessedLinearLights.add(environment.getPatch(62,166));
        verticalRecessedLinearLights.add(environment.getPatch(71,138));
        verticalRecessedLinearLights.add(environment.getPatch(71,146));
        verticalRecessedLinearLights.add(environment.getPatch(71,158));
        verticalRecessedLinearLights.add(environment.getPatch(71,166));
        verticalRecessedLinearLights.add(environment.getPatch(80,138));
        verticalRecessedLinearLights.add(environment.getPatch(80,146));
        verticalRecessedLinearLights.add(environment.getPatch(80,158));
        verticalRecessedLinearLights.add(environment.getPatch(80,166));

        LightMapper.draw(verticalRecessedLinearLights, "RECESSED_LINEAR_LIGHT", "VERTICAL");


        //  HORIZONTAL TRACK_LIGHT
        List<Patch> horizontalTrackLights = new ArrayList<>();

        // Meeting Room
        horizontalTrackLights.add(environment.getPatch(29,7));

        // Research Center
        horizontalTrackLights.add(environment.getPatch(95,34));
        horizontalTrackLights.add(environment.getPatch(95,48));
        horizontalTrackLights.add(environment.getPatch(95,59));
        horizontalTrackLights.add(environment.getPatch(95,75));
        horizontalTrackLights.add(environment.getPatch(95,91));

        // Faculty Room
        horizontalTrackLights.add(environment.getPatch(93,121));
        horizontalTrackLights.add(environment.getPatch(102,111));
        horizontalTrackLights.add(environment.getPatch(102,121));

        // Pantry
        horizontalTrackLights.add(environment.getPatch(118,151));
        horizontalTrackLights.add(environment.getPatch(114,135));

        // Director Room
        horizontalTrackLights.add(environment.getPatch(112,190));

        // Learning Space 1
        horizontalTrackLights.add(environment.getPatch(27,90));
        horizontalTrackLights.add(environment.getPatch(43,90));
        horizontalTrackLights.add(environment.getPatch(43,98));
        horizontalTrackLights.add(environment.getPatch(54,94));


        // Learning Space 2
        horizontalTrackLights.add(environment.getPatch(27,69));
        horizontalTrackLights.add(environment.getPatch(43,69));
        horizontalTrackLights.add(environment.getPatch(43,77));
        horizontalTrackLights.add(environment.getPatch(54,73));


        // Learning Space 3
        horizontalTrackLights.add(environment.getPatch(27,49));
        horizontalTrackLights.add(environment.getPatch(43,48));
        horizontalTrackLights.add(environment.getPatch(54,52));

        horizontalTrackLights.add(environment.getPatch(43,56));

        // Learning Space 4
        horizontalTrackLights.add(environment.getPatch(27,27));
        horizontalTrackLights.add(environment.getPatch(43,27));
        horizontalTrackLights.add(environment.getPatch(54,27));
        horizontalTrackLights.add(environment.getPatch(43,35));


        LightMapper.draw(horizontalTrackLights, "TRACK_LIGHT", "HORIZONTAL");



        /* VERTICAL TRACK_LIGHT */

        List<Patch> verticalTrackLights = new ArrayList<>();

        // Conference Room
        verticalTrackLights.add(environment.getPatch(96,144));
        verticalTrackLights.add(environment.getPatch(96,167));

        // Pantry
        verticalTrackLights.add(environment.getPatch(116,149));
        verticalTrackLights.add(environment.getPatch(116,155));
        verticalTrackLights.add(environment.getPatch(116,167));
        verticalTrackLights.add(environment.getPatch(116,178));

        // Reception
        verticalTrackLights.add(environment.getPatch(66,180));

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

        System.out.println("rows" + maxRows);
        System.out.println("cols" + maxColumns);

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


        /* Cubicles */

        List<Patch> nwMESA = new ArrayList<>(), neMESA = new ArrayList<>(), swMESA = new ArrayList<>(), seMESA = new ArrayList<>(),
                    cubicleA  = new ArrayList<>(), westCubicleB  = new ArrayList<>(), eastCubicleB  = new ArrayList<>(),
                    westCubicleC  = new ArrayList<>(), eastCubicleC  = new ArrayList<>();


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
        currentTeam1Count.setText(String.valueOf(Simulator.currentTeam1Count));
        currentTeam2Count.setText(String.valueOf(Simulator.currentTeam2Count));
        currentTeam3Count.setText(String.valueOf(Simulator.currentTeam3Count));
        currentTeam4Count.setText(String.valueOf(Simulator.currentTeam4Count));


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
        currentAirconInteractionCount.setText(String.valueOf(Simulator.currentAirconInteractionCount));
        currentLightInteractionCount.setText(String.valueOf(Simulator.currentLightInteractionCount));
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
}