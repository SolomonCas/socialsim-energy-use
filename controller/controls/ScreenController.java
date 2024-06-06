package com.socialsim.controller.controls;

import com.socialsim.controller.Main;
import com.socialsim.controller.graphics.GraphicsController;
import com.socialsim.controller.graphics.amenity.mapper.*;
import com.socialsim.model.core.agent.AgentMovement;
import com.socialsim.model.core.environment.Environment;
import com.socialsim.model.core.environment.patchobject.passable.gate.Gate;
import com.socialsim.model.simulator.Simulator;

import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchfield.*;
import com.socialsim.model.simulator.SimulationTime;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

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

    // TextField
    @FXML private TextField nonverbalMean;
    @FXML private TextField nonverbalStdDev;
    @FXML private TextField cooperativeMean;
    @FXML private TextField cooperativeStdDev;
    @FXML private TextField exchangeMean;
    @FXML private TextField exchangeStdDev;
    @FXML private TextField fieldOfView;

    // Label: Current Agent Count
    @FXML private Label currentDirectorCount;
    @FXML private Label currentFacultyCount;
    @FXML private Label currentStudentCount;
    @FXML private Label currentMaintenanceCount;
    @FXML private Label currentGuardCount;

    // Label: Current Interaction With Appliance Count
    @FXML private Label currentAirconInteractionCount;
    @FXML private Label currentLightInteractionCount;
    @FXML private Label currentFridgeInteractionCount;
    @FXML private Label currentWaterDispenserInteractionCount;

    //TextField: Aircon
//    @FXML private TextField averageAirconWattage;
//    @FXML private TextField lowAirconWattage;
//    @FXML private TextField HighAirconWattage;

    //TextField: Light
//    @FXML private TextField averageLightWattage;
//    @FXML private TextField lowLightWattage;
//    @FXML private TextField HighLightWattage;

    //TextField: FRIDGE
//    @FXML private TextField openedFridgeWattage;
//    @FXML private TextField standbyFridgeWattage;
//    @FXML private TextField activeCycleFridgeWattage;

    //TextField: Water Dispenser
//    @FXML private TextField averageWaterDispenserWattage;
//    @FXML private TextField lowWaterDispenserWattage;
//    @FXML private TextField HighWaterDispenserWattage;
    @FXML private Text currentWattageCountText;
    @FXML private Text totalWattageCountText;

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
    @FXML private Button resetToDefaultButton;


    // Buttons: Simulate
    @FXML private Button resetButton;           // Reset Simulation
    @FXML private ToggleButton playButton;
    @FXML private Slider speedSlider;
    @FXML private Button exportToCSVButton;
    @FXML private Button exportHeatMapButton;

    
    
    
    
    

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

        // Energy Consumption Interactions
//        openedFridgeWattage = ;
//        standbyFridgeWattage;
//        activeCycleFridgeWattage;
//
//        averageWaterDispenserWattage;
//        lowWaterDispenserWattage;
//        HighWaterDispenserWattage;

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

        totalWattageCountText.setText("Total Watts: " + String.format("%.02f",Simulator.totalWattageCount) + " W");

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
        resetToDefaultButton.setDisable(true);
        configureIOSButton.setDisable(true);
        editInteractionButton.setDisable(true);
    }

    public void resetToDefault() {
        nonverbalMean.setText(Integer.toString(AgentMovement.defaultNonverbalMean));
        nonverbalStdDev.setText(Integer.toString(AgentMovement.defaultNonverbalStdDev));
        cooperativeMean.setText(Integer.toString(AgentMovement.defaultCooperativeMean));
        cooperativeStdDev.setText(Integer.toString(AgentMovement.defaultCooperativeStdDev));
        exchangeMean.setText(Integer.toString(AgentMovement.defaultExchangeMean));
        exchangeStdDev.setText(Integer.toString(AgentMovement.defaultExchangeStdDev));
        fieldOfView.setText(Integer.toString(AgentMovement.defaultFieldOfView));
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
        GraphicsController.tileSize = backgroundCanvas.getHeight() / Main.simulator.getEnvironment().getRows();
        mapEnvironment();
        Main.simulator.spawnInitialAgents(environment);
        drawInterface();
    }

    public void mapEnvironment() {
        Environment environment = Main.simulator.getEnvironment();

        List<Patch> floorPatches = new ArrayList<>();

        for (int i = 0; i < environment.getRows(); i++) {
            for (int j = 0; j < environment.getColumns(); j++) {
                floorPatches.add(environment.getPatch(i, j));
            }
        }

        Main.simulator.getEnvironment().getFloors().add(Floor.floorFactory.create(floorPatches, 1));

        List<Patch> wallPatches = new ArrayList<>();

        /** WALL PATCHES **/

        // Small Meeting Wall
        for (int i = 12; i < 29; i++) {
            for (int j = 1; j < 13; j++) {
                if (i < 13 && j > 6) {
                    wallPatches.add(environment.getPatch(i, j));
                }
                else if (i >= 13) {
                    wallPatches.add(environment.getPatch(i, j));
                }

            }
        }


        // Human Experience and Data Collection Wall
        for (int i = 41; i < 64; i++) {
            if (i < 51) {
                for (int j = 1; j < 15; j++) {
                    wallPatches.add(environment.getPatch(i, j));
                }
            }
            else {
                for (int j = 1; j < 20; j++) {
                    wallPatches.add(environment.getPatch(i, j));
                }
            }
        }


        // Research Space and Faculty Room Wall
        for (int i = 52; i < 66; i++) {
            for (int j = 20; j < 92; j++) {
                wallPatches.add(environment.getPatch(i, j));
            }
        }


        // Large meeting room and Staff room (?) Wall
        for (int i = 56; i < 70; i++) {
            for (int j = 92; j < 125; j++) {
                wallPatches.add(environment.getPatch(i, j));
            }
        }


        // Learning Area 1, 2, 3, 4 Wall
        for (int i = 12; i < 30; i++) {
            for (int j = 19; j < 83; j++) {
                wallPatches.add(environment.getPatch(i, j));
            }
        }


        // Control Center Wall
        for (int i = 12; i < 30; i++) {
            for (int j = 83; j < 96; j++) {
                wallPatches.add(environment.getPatch(i, j));
            }
        }


        // Database Center Wall
        for (int i = 21; i < 30; i++) {
            for (int j = 96; j < 113; j++) {
                wallPatches.add(environment.getPatch(i, j));
            }
        }


        // Open Area 1 & 2 Wall
        for (int i = 38; i < 45; i++) {
            for (int j = 24; j < 39; j++) {
                wallPatches.add(environment.getPatch(i, j));
            }
        }


        // Open Area 3 & 4 Wall
        for (int i = 38; i < 45; i++) {
            for (int j = 65; j < 80; j++) {
                wallPatches.add(environment.getPatch(i, j));
            }
        }


        // Management and Executive Secretary Area Wall
        for (int i = 37; i < 48; i++) {
            for (int j = 95; j < 110; j++) {
                wallPatches.add(environment.getPatch(i, j));
            }
        }


        // Reception Wall
        for (int i = 37; i < 46; i++) {
            for (int j = 120; j < 134; j++) {
                wallPatches.add(environment.getPatch(i, j));
            }
        }


        // Clinic & Executive Director environment Wall
        for (int i = 46; i < 77; i++) {
            for (int j = 134; j < 149; j++) {
                wallPatches.add(environment.getPatch(i, j));
            }
        }


        // Outer Wall
        for (int i = 0; i < 84; i++) {
            for (int j = 0; j < 150; j++) {
                if (i < 5) {
                    wallPatches.add(environment.getPatch(i, j));
                }
                else if ((i >= 5 && i < 12) && (j <= 6 || j >= 19)) {
                    wallPatches.add(environment.getPatch(i, j));
                }
                else if (i == 12 && (j <= 6 || (j >= 96 && j < 135))) {
                    wallPatches.add(environment.getPatch(i, j));
                }
                else if ((i >= 13 && i < 17) && (j == 0 || (j >= 96 && j < 135))) {
                    wallPatches.add(environment.getPatch(i, j));
                }
                else if (i == 17 && (j == 0 || (j >= 96 && j < 126))) {
                    wallPatches.add(environment.getPatch(i, j));
                }
                else if ((i >= 18 && i < 21) && (j == 0 || (j >= 96 && j < 113) || (j >= 118 && j < 126))) {
                    wallPatches.add(environment.getPatch(i, j));
                }
                else if ((i >= 21 && i < 29) && (j == 0 || (j >= 118 && j < 126))) {
                    wallPatches.add(environment.getPatch(i, j));
                }
                else if (i == 29 && (j == 0 || (j >= 118 && j <= 134))) {
                    wallPatches.add(environment.getPatch(i, j));
                }
                else if ((i >= 30 && i < 34) && (j == 0 || j == 134)) {
                    wallPatches.add(environment.getPatch(i, j));
                }
                else if ((i >= 34 && i < 46) && (j == 0 || j >= 134)) {
                    wallPatches.add(environment.getPatch(i, j));
                }
                if ((i >= 46 && i < 64) && (j == 0 || j == 149)) {
                    wallPatches.add(environment.getPatch(i, j));
                }
                else if (i == 64 && (j < 20 || j == 149)) {
                    wallPatches.add(environment.getPatch(i, j));
                }
                else if (i == 65 && (j == 19 || j == 149)) {
                    wallPatches.add(environment.getPatch(i, j));
                }
                else if (i == 66 && ((j >= 19 && j <= 91) || j == 149)) {
                    wallPatches.add(environment.getPatch(i, j));
                }
                else if ((i >= 67 && i < 69) && (j == 91 || j == 149)) {
                    wallPatches.add(environment.getPatch(i, j));
                }
                else if (i == 69 && ((j >= 88 && j <= 91) || j == 149)) {
                    wallPatches.add(environment.getPatch(i, j));
                }
                else if ((i >= 70 && i < 77) && (j == 88 || j == 149)) {
                    wallPatches.add(environment.getPatch(i, j));
                }
                else if (i == 77 && (j == 88 || j >= 134)) {
                    wallPatches.add(environment.getPatch(i, j));
                }
                else if ((i >= 78 && i < 82) && (j == 88|| j == 134)) {
                    wallPatches.add(environment.getPatch(i, j));
                }
                else if (i == 82 && (j >= 88 && j <= 134)) {
                    wallPatches.add(environment.getPatch(i, j));
                }
            }
        }
        Main.simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(wallPatches, 1));



        /** ROOM PATCHES **/

        // Small Meeting Room
        List<Patch> meetingRoom1Patches = new ArrayList<>();
        for (int i = 13; i < 28; i++) {
            for (int j = 1; j < 12; j++) {
                meetingRoom1Patches.add(environment.getPatch(i, j));
            }
        }
        Main.simulator.getEnvironment().getMeetingRooms().add(MeetingRoom.meetingRoomFactory.create(meetingRoom1Patches, 1));


        // Human Experience Room
        List<Patch> humanExperienceRoomPatches = new ArrayList<>();
        for (int i = 42; i < 51; i++) {
            for (int j = 1; j < 14; j++) {
                humanExperienceRoomPatches.add(environment.getPatch(i, j));
            }
        }
        Main.simulator.getEnvironment().getHumanExpRooms().add(HumanExpRoom.humanExpRoomFactory.create(humanExperienceRoomPatches, 1));


        // Data Collection Room
        List<Patch> dataCollectionRoomPatches = new ArrayList<>();
        for (int i = 52; i < 64; i++) {
            for (int j = 1; j < 19; j++) {
                dataCollectionRoomPatches.add(environment.getPatch(i, j));
            }
        }
        Main.simulator.getEnvironment().getDataCollectionRooms().add(DataCollectionRoom.dataCollectionRoomFactory.create(dataCollectionRoomPatches, 1));


        // Research Area Room
        List<Patch> researchCenterPatches = new ArrayList<>();
        for (int i = 53; i < 66; i++) {
            for (int j = 20; j < 75; j++) {
                if (i < 58 || i > 59) {
                    researchCenterPatches.add(environment.getPatch(i, j));
                }
                else if (j < 23 || j > 24 && j < 42 || j > 43) {
                    researchCenterPatches.add(environment.getPatch(i, j));
                }

            }
        }
        Main.simulator.getEnvironment().getResearchCenters().add(ResearchCenter.researchCenterFactory.create(researchCenterPatches, 1));


        // Faculty Room
        List<Patch> facultyRoomPatches = new ArrayList<>();
        for (int i = 53; i < 66; i++) {
            for (int j = 76; j < 91; j++) {
                facultyRoomPatches.add(environment.getPatch(i, j));
            }
        }
        Main.simulator.getEnvironment().getFacultyRooms().add(FacultyRoom.facultyRoomFactory.create(facultyRoomPatches, 1));


        // Storage Room
        List<Patch> storageRoomPatches = new ArrayList<>();
        for (int i = 57; i < 69; i++) {
            for (int j = 92; j < 102; j++) {
                storageRoomPatches.add(environment.getPatch(i, j));
            }
        }
        Main.simulator.getEnvironment().getStorageRooms().add(StorageRoom.storageRoomFactory.create(storageRoomPatches, 1));


        // Large Meeting Room
        List<Patch> meetingRoom2Patches = new ArrayList<>();
        for (int i = 57; i < 69; i++) {
            for (int j = 103; j < 124; j++) {
                meetingRoom2Patches.add(environment.getPatch(i, j));
            }
        }
        Main.simulator.getEnvironment().getMeetingRooms().add(MeetingRoom.meetingRoomFactory.create(meetingRoom2Patches, 2));


        // Pantry Area
        List<Patch> pantryPatches = new ArrayList<>();
        for (int i = 70; i < 82; i++) {
            for (int j = 89; j < 134; j++) {
                pantryPatches.add(environment.getPatch(i, j));
            }
        }
        Main.simulator.getEnvironment().getPantries().add(Pantry.pantryFactory.create(pantryPatches, 1));


        // Learning Area 1 (Leftmost)
        List<Patch> learningAreaPatches1 = new ArrayList<>();
        for (int i = 12; i < 29; i++) {
            for (int j = 20; j < 35; j++) {
                learningAreaPatches1.add(environment.getPatch(i, j));
            }
        }
        Main.simulator.getEnvironment().getLearningSpaces().add(LearningSpace.learningSpaceFactory.create(learningAreaPatches1, 1));


        // Learning Area 2
        List<Patch> learningAreaPatches2 = new ArrayList<>();
        for (int i = 12; i < 29; i++) {
            for (int j = 36; j < 51; j++) {
                learningAreaPatches2.add(environment.getPatch(i, j));
            }
        }
        Main.simulator.getEnvironment().getLearningSpaces().add(LearningSpace.learningSpaceFactory.create(learningAreaPatches2, 2));


        // Learning Area 3
        List<Patch> learningAreaPatches3 = new ArrayList<>();
        for (int i = 12; i < 29; i++) {
            for (int j = 52; j < 67; j++) {
                learningAreaPatches3.add(environment.getPatch(i, j));
            }
        }
        Main.simulator.getEnvironment().getLearningSpaces().add(LearningSpace.learningSpaceFactory.create(learningAreaPatches3, 3));


        // Learning Area 4 (Rightmost)
        List<Patch> learningAreaPatches4 = new ArrayList<>();
        for (int i = 12; i < 29; i++) {
            for (int j = 68; j < 83; j++) {
                learningAreaPatches4.add(environment.getPatch(i, j));
            }
        }
        Main.simulator.getEnvironment().getLearningSpaces().add(LearningSpace.learningSpaceFactory.create(learningAreaPatches4, 4));


        // Control Center (Up/Back)
        List<Patch> controlCenterAreaPatches1 = new ArrayList<>();
        for (int i = 12; i < 17; i++) {
            for (int j = 84; j < 96; j++) {
                controlCenterAreaPatches1.add(environment.getPatch(i, j));
            }
        }
        Main.simulator.getEnvironment().getControlCenters().add(ControlCenter.controlCenterFactory.create(controlCenterAreaPatches1, 1));


        // Control Center (Down/Front)
        List<Patch> controlCenterAreaPatches2 = new ArrayList<>();
        for (int i = 18; i < 29; i++) {
            for (int j = 84; j < 96; j++) {
                controlCenterAreaPatches2.add(environment.getPatch(i, j));
            }
        }
        Main.simulator.getEnvironment().getControlCenters().add(ControlCenter.controlCenterFactory.create(controlCenterAreaPatches2, 2));


        // Database Control Center
        List<Patch> databaseControlAreaPatches = new ArrayList<>();
        for (int i = 21; i < 29; i++) {
            for (int j = 97; j < 112; j++) {
                databaseControlAreaPatches.add(environment.getPatch(i, j));
            }
        }
        Main.simulator.getEnvironment().getDataCenters().add(DataCenter.dataCenterFactory.create(databaseControlAreaPatches, 1));


        // Open Area Study Area 1 (Leftmost)
        List<Patch> openAreaPatches1 = new ArrayList<>();
        for (int i = 39; i < 44; i++) {
            for (int j = 25; j < 31; j++) {
                openAreaPatches1.add(environment.getPatch(i, j));
            }
        }
        Main.simulator.getEnvironment().getSoloRooms().add(SoloRoom.soloRoomFactory.create(openAreaPatches1, 1));


        // Open Area Study Area 2
        List<Patch> openAreaPatches2 = new ArrayList<>();
        for (int i = 39; i < 44; i++) {
            for (int j = 32; j < 38; j++) {
                openAreaPatches2.add(environment.getPatch(i, j));
            }
        }
        Main.simulator.getEnvironment().getSoloRooms().add(SoloRoom.soloRoomFactory.create(openAreaPatches2, 2));


        // Open Area Study Area 3
        List<Patch> openAreaPatches3 = new ArrayList<>();
        for (int i = 39; i < 44; i++) {
            for (int j = 66; j < 72; j++) {
                openAreaPatches3.add(environment.getPatch(i, j));
            }
        }
        Main.simulator.getEnvironment().getSoloRooms().add(SoloRoom.soloRoomFactory.create(openAreaPatches3, 3));


        // Open Area Study Area 4 (Rightmost)
        List<Patch> openAreaPatches4 = new ArrayList<>();
        for (int i = 39; i < 44; i++) {
            for (int j = 73; j < 79; j++) {
                openAreaPatches4.add(environment.getPatch(i, j));
            }
        }
        Main.simulator.getEnvironment().getSoloRooms().add(SoloRoom.soloRoomFactory.create(openAreaPatches4, 4));

        // Break Area
        List<Patch> breakAreaPatches = new ArrayList<>();
        for (int i = 38; i < 48; i++) {
            for (int j = 92; j < 95; j++) {
                breakAreaPatches.add(environment.getPatch(i, j));
            }
        }
        Main.simulator.getEnvironment().getBreakAreas().add(BreakArea.breakAreaFactory.create(breakAreaPatches, 1));


        // Staff Area Top
        List<Patch> staffAreaPatches1 = new ArrayList<>();
        for (int i = 37; i < 42; i++) {
            for (int j = 96; j < 109; j++) {
                if (j != 102) {
                    staffAreaPatches1.add(environment.getPatch(i, j));
                }

            }
        }
        Main.simulator.getEnvironment().getStaffRooms().add(StaffArea.staffAreaFactory.create(staffAreaPatches1, 1));


        // Staff Area Bottom
        List<Patch> staffAreaPatches2 = new ArrayList<>();
        for (int i = 43; i < 48; i++) {
            for (int j = 96; j < 109; j++) {
                if (j != 102) {
                    staffAreaPatches2.add(environment.getPatch(i, j));
                }
            }
        }
        Main.simulator.getEnvironment().getStaffRooms().add(StaffArea.staffAreaFactory.create(staffAreaPatches2, 2));


        // Men's Bathroom
        List<Patch> mBathroomPatches = new ArrayList<>();
        for (int i = 1; i < 11; i++) {
            for (int j = 135; j < 149; j++) {
                mBathroomPatches.add(environment.getPatch(i, j));
            }
        }
        Main.simulator.getEnvironment().getBathrooms().add(Bathroom.bathroomFactory.create(mBathroomPatches, 2));


        // Women's Bathroom
        List<Patch> fBathroomPatches = new ArrayList<>();
        for (int i = 35; i < 45; i++) {
            for (int j = 135; j < 149; j++) {
                fBathroomPatches.add(environment.getPatch(i, j));
            }
        }
        Main.simulator.getEnvironment().getBathrooms().add(Bathroom.bathroomFactory.create(fBathroomPatches, 1));


        // Reception Area
        List<Patch> receptionPatches = new ArrayList<>();
        for (int i = 30; i < 45; i++) {
            for (int j = 121; j < 134; j++) {
                receptionPatches.add(environment.getPatch(i, j));
            }
        }

        Main.simulator.getEnvironment().getReceptions().add(Reception.receptionFactory.create(receptionPatches, 1));


        // Clinic (Front/Left)
        List<Patch> breakroomPatches1 = new ArrayList<>();
        for (int i = 46; i < 56; i++) {
            for (int j = 135; j < 142; j++) {
                breakroomPatches1.add(environment.getPatch(i, j));
            }
        }
        Main.simulator.getEnvironment().getStorageRooms().add(StorageRoom.storageRoomFactory.create(breakroomPatches1, 2));


        // Executive Director's Bathroom
        List<Patch> execDirectorBathroom = new ArrayList<>();
        for (int i = 46; i < 56; i++) {
            for (int j = 143; j < 149; j++) {
                execDirectorBathroom.add(environment.getPatch(i, j));
            }
        }
        Main.simulator.getEnvironment().getBathrooms().add(Bathroom.bathroomFactory.create(execDirectorBathroom, 3));


        // Executive Director's Office
        List<Patch> execDirectorOfficeAreaPatches = new ArrayList<>();
        for (int i = 57; i < 77; i++) {
            for (int j = 135; j < 149; j++) {
                execDirectorOfficeAreaPatches.add(environment.getPatch(i, j));
            }
        }
        Main.simulator.getEnvironment().getDirectorRooms().add(DirectorRoom.directorRoomFactory.create(execDirectorOfficeAreaPatches, 1));

        /** AMENITIES **/

        // Storage Cabinet RIGHT
        List<Patch> storageCabinetPatches = new ArrayList<>();
        storageCabinetPatches.add(environment.getPatch(52,135));
        StorageCabinetMapper.draw(storageCabinetPatches, "RIGHT");

        // Pantry Down Cabinets
        List<Patch> cabinetDownPatches = new ArrayList<>();

        cabinetDownPatches.add(environment.getPatch(70,89));
        cabinetDownPatches.add(environment.getPatch(70,94));
        cabinetDownPatches.add(environment.getPatch(70,96));

        cabinetDownPatches.add(environment.getPatch(23,113));

        cabinetDownPatches.add(environment.getPatch(42,110));
        cabinetDownPatches.add(environment.getPatch(42,118));

        CabinetMapper.draw(cabinetDownPatches, "DOWN");



        // Pantry Up Cabinets
        List<Patch> cabinetUpPatches = new ArrayList<>();

        cabinetUpPatches.add(environment.getPatch(64,20));
        cabinetUpPatches.add(environment.getPatch(64,22));
        cabinetUpPatches.add(environment.getPatch(64,24));
        cabinetUpPatches.add(environment.getPatch(64,26));

        cabinetUpPatches.add(environment.getPatch(54,92));

        cabinetUpPatches.add(environment.getPatch(40,110));
        cabinetUpPatches.add(environment.getPatch(40,118));

        CabinetMapper.draw(cabinetUpPatches, "UP");



        // Initialize chairPatches
        List<Patch> chairPatches = new ArrayList<>();



        // Management and Executive Secretary chairs
        chairPatches.add(environment.getPatch(38,100));
        chairPatches.add(environment.getPatch(41,100));
        chairPatches.add(environment.getPatch(38,104));
        chairPatches.add(environment.getPatch(41,104));

        chairPatches.add(environment.getPatch(43,100));
        chairPatches.add(environment.getPatch(46,100));
        chairPatches.add(environment.getPatch(43,104));
        chairPatches.add(environment.getPatch(46,104));


        // Data Collection Chairs
        chairPatches.add(environment.getPatch(53,1));
        chairPatches.add(environment.getPatch(53,3));
        chairPatches.add(environment.getPatch(53,5));

        ChairMapper.draw(chairPatches);

        List<Patch> learningAreaCollabDeskPatches = new ArrayList<>();

        // Learning Area 1-4 Study Tables
        for (int i = 18; i < 24; i+=5) {
            for (int j = 22; j < 80; j += 16) {
                learningAreaCollabDeskPatches.add(environment.getPatch(i, j));
                learningAreaCollabDeskPatches.add(environment.getPatch(i, j + 8));
            }
        }
        CollabDeskMapper.draw(learningAreaCollabDeskPatches, "HORIZONTAL", 3);

        List<Patch> couchRightPatches = new ArrayList<>();
        couchRightPatches.add(environment.getPatch(39,93));
        CouchMapper.draw(couchRightPatches, "RIGHT");




        // Initialize cubicleUpPatches
        List<Patch> cubicleUpPatches = new ArrayList<>();


        // Faculty Cubicle
        cubicleUpPatches.add(environment.getPatch(60,89));
        cubicleUpPatches.add(environment.getPatch(60,85));
        cubicleUpPatches.add(environment.getPatch(60,83));
        cubicleUpPatches.add(environment.getPatch(60,79));
        cubicleUpPatches.add(environment.getPatch(60,77));

        CubicleMapper.draw(cubicleUpPatches, "UP", true);



        List<Patch> cubicleDownPatches = new ArrayList<>();

        // Faculty Cubicle
        cubicleDownPatches.add(environment.getPatch(58,89));
        cubicleDownPatches.add(environment.getPatch(58,85));
        cubicleDownPatches.add(environment.getPatch(58,83));
        cubicleDownPatches.add(environment.getPatch(58,79));
        cubicleDownPatches.add(environment.getPatch(58,77));


        // Control Center Cubicle
        cubicleDownPatches.add(environment.getPatch(21,86));


        CubicleMapper.draw(cubicleDownPatches, "DOWN", true);



        // Initialize cubicleLeftPatches
        List<Patch> cubicleLeftPatches = new ArrayList<>();


        // Research Center
        cubicleLeftPatches.add(environment.getPatch(61,54));
        cubicleLeftPatches.add(environment.getPatch(59,61));


        CubicleMapper.draw(cubicleLeftPatches, "LEFT", true);



        //Initializa cubicleDownWithoutAppliance
        List<Patch> cubicleDownWithoutAppliancePatches = new ArrayList<>();


        // Control Center Cubicle
        cubicleDownWithoutAppliancePatches.add(environment.getPatch(23,86));


        // Open Area 2 & 4 Cubicle
        cubicleDownWithoutAppliancePatches.add(environment.getPatch(39,34));
        cubicleDownWithoutAppliancePatches.add(environment.getPatch(39,68));


        CubicleMapper.draw(cubicleDownWithoutAppliancePatches, "DOWN", false);



        // Initialize cubicleUpWithoutAppliance
        List<Patch> cubicleUpWithoutAppliancePatches = new ArrayList<>();


        // Open Area 1 & 3 Cubicle
        cubicleUpWithoutAppliancePatches.add(environment.getPatch(42,27));
        cubicleUpWithoutAppliancePatches.add(environment.getPatch(42,75));


        CubicleMapper.draw(cubicleUpWithoutAppliancePatches, "UP", false);



        // Initialize cubicleLeftWithoutAppliancePatches
        List<Patch> cubicleLeftWithoutAppliancePatches = new ArrayList<>();


        // Control Center Cubicle
        cubicleLeftWithoutAppliancePatches.add(environment.getPatch(19,93));
        cubicleLeftWithoutAppliancePatches.add(environment.getPatch(21,93));
        cubicleLeftWithoutAppliancePatches.add(environment.getPatch(23,93));
        cubicleLeftWithoutAppliancePatches.add(environment.getPatch(25,93));


        // Research Area Cubicle
        cubicleLeftWithoutAppliancePatches.add(environment.getPatch(57,28));
        cubicleLeftWithoutAppliancePatches.add(environment.getPatch(59,28));
        cubicleLeftWithoutAppliancePatches.add(environment.getPatch(61,28));

        cubicleLeftWithoutAppliancePatches.add(environment.getPatch(57,35));
        cubicleLeftWithoutAppliancePatches.add(environment.getPatch(59,35));
        cubicleLeftWithoutAppliancePatches.add(environment.getPatch(61,35));

        cubicleLeftWithoutAppliancePatches.add(environment.getPatch(57,47));
        cubicleLeftWithoutAppliancePatches.add(environment.getPatch(59,47));
        cubicleLeftWithoutAppliancePatches.add(environment.getPatch(61,47));

        cubicleLeftWithoutAppliancePatches.add(environment.getPatch(57,54));
        cubicleLeftWithoutAppliancePatches.add(environment.getPatch(59,54));

        cubicleLeftWithoutAppliancePatches.add(environment.getPatch(57,61));

        cubicleLeftWithoutAppliancePatches.add(environment.getPatch(61,61));

        cubicleLeftWithoutAppliancePatches.add(environment.getPatch(57,69));
        cubicleLeftWithoutAppliancePatches.add(environment.getPatch(59,69));
        cubicleLeftWithoutAppliancePatches.add(environment.getPatch(61,69));

        CubicleMapper.draw(cubicleLeftWithoutAppliancePatches, "LEFT", false);

        List<Patch> cubicleRightPatches = new ArrayList<>();
        cubicleRightPatches.add(environment.getPatch(57,30));
        cubicleRightPatches.add(environment.getPatch(59,30));
        cubicleRightPatches.add(environment.getPatch(61,30));

        cubicleRightPatches.add(environment.getPatch(57,37));
        cubicleRightPatches.add(environment.getPatch(59,37));
        cubicleRightPatches.add(environment.getPatch(61,37));

        cubicleRightPatches.add(environment.getPatch(57,49));
        cubicleRightPatches.add(environment.getPatch(59,49));
        cubicleRightPatches.add(environment.getPatch(61,49));

        cubicleRightPatches.add(environment.getPatch(57,56));
        cubicleRightPatches.add(environment.getPatch(59,56));
        cubicleRightPatches.add(environment.getPatch(61,56));

        cubicleRightPatches.add(environment.getPatch(57,63));
        cubicleRightPatches.add(environment.getPatch(59,63));
        cubicleRightPatches.add(environment.getPatch(61,63));

        cubicleRightPatches.add(environment.getPatch(57,71));
        cubicleRightPatches.add(environment.getPatch(59,71));
        cubicleRightPatches.add(environment.getPatch(61,71));

        CubicleMapper.draw(cubicleRightPatches, "RIGHT", false);






        // Initialize rightWhiteboardPatches
        List<Patch> rightWhiteboardPatches = new ArrayList<>();


        // Small Meeting Whiteboard
        rightWhiteboardPatches.add(environment.getPatch(18,1));
        rightWhiteboardPatches.add(environment.getPatch(20,1));
        rightWhiteboardPatches.add(environment.getPatch(22,1));


        // Research Area Whiteboard (Vertical)
        rightWhiteboardPatches.add(environment.getPatch(58,22));
        rightWhiteboardPatches.add(environment.getPatch(58,25));

        rightWhiteboardPatches.add(environment.getPatch(58,41));
        rightWhiteboardPatches.add(environment.getPatch(58,44));
        WhiteboardMapper.draw(rightWhiteboardPatches, "RIGHT");






        // Initialize topWhiteboardPatches
        List<Patch> topWhiteboardPatches = new ArrayList<>();

        // Research Area Whiteboard (Horizontal)
        topWhiteboardPatches.add(environment.getPatch(57,23));
        topWhiteboardPatches.add(environment.getPatch(57,42));


        // Learning Area 1 Whiteboard
        topWhiteboardPatches.add(environment.getPatch(12,20));
        topWhiteboardPatches.add(environment.getPatch(12,22));
        topWhiteboardPatches.add(environment.getPatch(12,31));
        topWhiteboardPatches.add(environment.getPatch(12,33));


        // Learning Area 2 Whiteboard
        topWhiteboardPatches.add(environment.getPatch(12,36));
        topWhiteboardPatches.add(environment.getPatch(12,38));
        topWhiteboardPatches.add(environment.getPatch(12,47));
        topWhiteboardPatches.add(environment.getPatch(12,49));


        // Learning Area 3 Whiteboard
        topWhiteboardPatches.add(environment.getPatch(12,52));
        topWhiteboardPatches.add(environment.getPatch(12,54));
        topWhiteboardPatches.add(environment.getPatch(12,63));
        topWhiteboardPatches.add(environment.getPatch(12,65));


        // Learning Area 4 Whiteboard
        topWhiteboardPatches.add(environment.getPatch(12,68));
        topWhiteboardPatches.add(environment.getPatch(12,70));
        topWhiteboardPatches.add(environment.getPatch(12,79));
        topWhiteboardPatches.add(environment.getPatch(12,81));

        WhiteboardMapper.draw(topWhiteboardPatches, "UP");







        // Initialize doorRightPatches
        List<Patch> doorRightPatches = new ArrayList<>();

        // Small Meeting Room Door
        doorRightPatches.add(environment.getPatch(13,12));
        doorRightPatches.add(environment.getPatch(24,12));


        // Human Experience Room Door
        doorRightPatches.add(environment.getPatch(42,14));


        // Bet. Research and Faculty Room Door
        doorRightPatches.add(environment.getPatch(53,75));
        DoorMapper.draw(doorRightPatches, "RIGHT");








        // Initialize doorLeftPatches
        List<Patch> doorLeftPatches = new ArrayList<>();

        // Clinic Door (Left/Front)
        doorLeftPatches.add(environment.getPatch(47,134));


        // Executive Room Door
        doorLeftPatches.add(environment.getPatch(58,134));
        DoorMapper.draw(doorLeftPatches, "LEFT");







        // Initialize doorUpPatches
        List<Patch> doorUpPatches = new ArrayList<>();


        // Staff Room Door (?)
        doorUpPatches.add(environment.getPatch(56,98));


        // Large Meeting Room Doors
        doorUpPatches.add(environment.getPatch(56,103));
        doorUpPatches.add(environment.getPatch(56,120));


        // Research Area Room Door
        doorUpPatches.add(environment.getPatch(52,20));


        // Faculty Room Door
        doorUpPatches.add(environment.getPatch(52,87));


        // Data Collection Room Door
        doorUpPatches.add(environment.getPatch(51,15));


        // Bet Human Experience & Data Collection Room Door
        doorUpPatches.add(environment.getPatch(51,10));


        // Clinic Door (Right/Back)
        doorUpPatches.add(environment.getPatch(56,144));


        // Open Area 1 Door
        doorUpPatches.add(environment.getPatch(38,25));


        // Open Area 3 Door (RightMost)
        doorUpPatches.add(environment.getPatch(44,66));

        DoorMapper.draw(doorUpPatches, "UP");








        // Initialize doorDownPatches
        List<Patch> doorDownPatches = new ArrayList<>();



        // Learning Area 1 Door (Leftmost)
        doorDownPatches.add(environment.getPatch(29,31));


        // Learning Area 2 Door
        doorDownPatches.add(environment.getPatch(29,47));


        // Learning Area 3 Door
        doorDownPatches.add(environment.getPatch(29,63));


        // Learning Area 4 Door (RightMost)
        doorDownPatches.add(environment.getPatch(29,79));


        // Control Center Door (Front/Down)
        doorDownPatches.add(environment.getPatch(29,92));


        // Control Center Door (Back/Up)
        doorDownPatches.add(environment.getPatch(17,84));


        // Database Center Door
        doorDownPatches.add(environment.getPatch(29,108));


        // Open Area 2 Door (LeftMost)
        doorDownPatches.add(environment.getPatch(44,34));


        // Open Area 4 Door
        doorDownPatches.add(environment.getPatch(38,75));

        DoorMapper.draw(doorDownPatches, "DOWN");

        List<Patch> maleBathroomDoorPatches = new ArrayList<>();
        // Men's Bathroom Door
        maleBathroomDoorPatches.add(environment.getPatch(11,135));
        MaleBathroomDoorMapper.draw(maleBathroomDoorPatches, "DOWN");

        List<Patch> femaleBathroomDoorPatches = new ArrayList<>();
        // Women's Bathroom Door
        femaleBathroomDoorPatches.add(environment.getPatch(34,135));
        FemaleBathroomDoorMapper.draw(femaleBathroomDoorPatches, "DOWN");

        List<Patch> mainEntranceDoorPatches = new ArrayList<>();
        // Main Entrance Door
        mainEntranceDoorPatches.add(environment.getPatch(29,130));
        MainEntranceDoorMapper.draw(mainEntranceDoorPatches, "DOWN");


        // DESK PATCHES

        // Large Meeting Room Desk
        List<Patch> meetingDeskHorizontalPatches = new ArrayList<>();
        meetingDeskHorizontalPatches.add(environment.getPatch(61,108));
        MeetingDeskMapper.draw(meetingDeskHorizontalPatches, "HORIZONTAL", 5, true);

        // Small Meeting Room Desk
        List<Patch> meetingDeskVerticalPatches = new ArrayList<>();
        meetingDeskVerticalPatches.add(environment.getPatch(17,6));

        // Executive Meeting Desk
        meetingDeskVerticalPatches.add(environment.getPatch(62,144));
        MeetingDeskMapper.draw(meetingDeskVerticalPatches, "VERTICAL", 4, false);

        // Executive Director's Office Desk
        List<Patch> horizontalOfficeDeskPatches = new ArrayList<>();
        horizontalOfficeDeskPatches.add(environment.getPatch(72,136));
        DirectorTableMapper.draw(horizontalOfficeDeskPatches, "HORIZONTAL");

        List<Patch> horizontalCollabDeskPatches = new ArrayList<>();
        horizontalCollabDeskPatches.add(environment.getPatch(52,1));
        OfficeDeskMapper.draw(horizontalCollabDeskPatches, "HORIZONTAL", 3);






        // Office Elevator (Spawn and Despawn point)
        List<Patch> GateExitPatches = new ArrayList<>();
        GateExitPatches.add(environment.getPatch(21,149));
        GateExitPatches.add(environment.getPatch(29,149));
        OfficeGateMapper.draw(GateExitPatches, Gate.GateMode.EXIT);

        List<Patch> GateEntrancePatches = new ArrayList<>();
        GateEntrancePatches.add(environment.getPatch(13,149));
        OfficeGateMapper.draw(GateEntrancePatches, Gate.GateMode.ENTRANCE);

//        List<Patch> securityPatches = new ArrayList<>();
//        securityPatches.add(environment.getPatch(13,143));
//        securityPatches.add(environment.getPatch(32,132));
//        SecurityMapper.draw(securityPatches);






        // PLANT PATCHES

        List<Patch> plantPatches = new ArrayList<>();
        // Reception Plants
        plantPatches.add(environment.getPatch(36,133));
        plantPatches.add(environment.getPatch(37,133));

        plantPatches.add(environment.getPatch(29,1));
        plantPatches.add(environment.getPatch(40,1));

        // Pantry Plants
        plantPatches.add(environment.getPatch(81,103));
        plantPatches.add(environment.getPatch(81,105));
        plantPatches.add(environment.getPatch(81,107));
        plantPatches.add(environment.getPatch(81,109));
        plantPatches.add(environment.getPatch(81,111));
        plantPatches.add(environment.getPatch(81,113));

        // Executive Director's Plants
        plantPatches.add(environment.getPatch(48,148));
        plantPatches.add(environment.getPatch(50,148));
        plantPatches.add(environment.getPatch(52,148));
        plantPatches.add(environment.getPatch(54,148));
        PlantMapper.draw(plantPatches);





        //  RECEPTION TABLE PATCHES

        List<Patch> receptionTablePatches = new ArrayList<>();
        // Reception Table
        receptionTablePatches.add(environment.getPatch(39,122));
        ReceptionTableMapper.draw(receptionTablePatches);


        // PANTRY TABLE UP PATCHES

        List<Patch> pantryTableRightPatches = new ArrayList<>();
        // Pantry Table
        pantryTableRightPatches.add(environment.getPatch(74,130));
        PantryTableMapper.draw(pantryTableRightPatches, "RIGHT");


        // PANTRY TABLE RIGHT PATCHES

        List<Patch> pantryTableUpPatches = new ArrayList<>();
        // Pantry Tables
        pantryTableUpPatches.add(environment.getPatch(77,95));
        pantryTableUpPatches.add(environment.getPatch(77,105));
        pantryTableUpPatches.add(environment.getPatch(73,119));
        pantryTableUpPatches.add(environment.getPatch(78,119));
        PantryTableMapper.draw(pantryTableUpPatches, "UP");



        // TABLE UP PATCHES

        List<Patch> tableUpPatches = new ArrayList<>();
        // Management and Executive Secretary Table
        tableUpPatches.add(environment.getPatch(38,101));
        tableUpPatches.add(environment.getPatch(40,101));
        tableUpPatches.add(environment.getPatch(38,103));
        tableUpPatches.add(environment.getPatch(40,103));


        tableUpPatches.add(environment.getPatch(45,101));
        tableUpPatches.add(environment.getPatch(43,101));
        tableUpPatches.add(environment.getPatch(45,103));
        tableUpPatches.add(environment.getPatch(43,103));
        TableMapper.draw(tableUpPatches, "RIGHT");

        List<Patch> tableRightPatches = new ArrayList<>();

        // TABLE RIGHT PATCHES


        // Management and Executive Secretary Table
        tableRightPatches.add(environment.getPatch(37,100));
        tableRightPatches.add(environment.getPatch(37,103));
        tableRightPatches.add(environment.getPatch(47,100));
        tableRightPatches.add(environment.getPatch(47,103));
        TableMapper.draw(tableRightPatches, "UP");





        // Toilet

        List<Patch> toiletPatches = new ArrayList<>();
        for (int j = 141; j < 148; j += 2) {
            // Men's Bathroom Toilet
            toiletPatches.add(environment.getPatch(1,j));
            // Women's Bathroom Toilet
            toiletPatches.add(environment.getPatch(44,j));
        }
        ToiletMapper.draw(toiletPatches);



        // Executive Director's Toilet
        List<Patch> officeToiletPatches = new ArrayList<>();
        officeToiletPatches.add(environment.getPatch(46,147));
        OfficeToiletMapper.draw(officeToiletPatches);






        // Sink

        List<Patch> sinkPatches = new ArrayList<>();
        for (int j = 141; j < 148; j += 2) {
            // Men's Bathroom Sink
            sinkPatches.add(environment.getPatch(10,j));

            // Women's Bathroom Sink
            sinkPatches.add(environment.getPatch(35,j));
        }
        SinkMapper.draw(sinkPatches);


        List<Patch> officeSinkPatches = new ArrayList<>();
        // Executive Directors's Sink

        officeSinkPatches.add(environment.getPatch(46,144));





        // Pantry Sink

        officeSinkPatches.add(environment.getPatch(70,92));
        OfficeSinkMapper.draw(officeSinkPatches);



        // Refrigerator

        List<Patch> fridgePatches = new ArrayList<>();
        fridgePatches.add(environment.getPatch(70,101));
        FridgeMapper.draw(fridgePatches);



        // Water Dispenser

        List<Patch> waterDispenserPatches = new ArrayList<>();
        waterDispenserPatches.add(environment.getPatch(70,99));
        WaterDispenserMapper.draw(waterDispenserPatches);



        // Server

        List<Patch> serverPatches = new ArrayList<>();
        serverPatches.add(environment.getPatch(22,98));
        serverPatches.add(environment.getPatch(25,103));
        ServerMapper.draw(serverPatches);




        // Trash
        List<Patch> trashPatches = new ArrayList<>();
        trashPatches.add(environment.getPatch(74,89));
        TrashMapper.draw(trashPatches);


    }

    private void drawInterface() {
        drawEnvironmentViewBackground(Main.simulator.getEnvironment());
        drawEnvironmentViewForeground(Main.simulator.getEnvironment(), false);
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
        LocalTime currentTime = Main.simulator.getSimulationTime().getTime();
        long elapsedTime = Main.simulator.getSimulationTime().getStartTime().until(currentTime, ChronoUnit.SECONDS) / 5;
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

        totalWattageCountText.setText("Total Watts: " + String.format("%.02f", Simulator.totalWattageCount) + " W");
    }


    public void setElements() {
        stackPane.setScaleX(CANVAS_SCALE);
        stackPane.setScaleY(CANVAS_SCALE);

        double rowsScaled = Main.simulator.getEnvironment().getRows() * GraphicsController.tileSize;
        double columnsScaled = Main.simulator.getEnvironment().getColumns() * GraphicsController.tileSize;

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
        int width = 83;
        int length = 150;
        int rows = (int) Math.ceil(width / Patch.PATCH_SIZE_IN_SQUARE_METERS);
        int columns = (int) Math.ceil(length / Patch.PATCH_SIZE_IN_SQUARE_METERS);
        Environment environment = Environment.Factory.create(rows, columns);
        Main.simulator.resetToDefaultConfiguration(environment);

        // Configure
        Environment.configureDefaultIOS();
        Environment.configureDefaultInteractionTypeChances();
        
        // Copy
        environment.copyDefaultToIOS();
        environment.copyDefaultToInteractionTypeChances();
    }

    @FXML
    public void initializeAction() {
        
        
        if (Main.simulator.isRunning()) {
            playAction();
            playButton.setSelected(false);
        }
        
        if (validateParameters()) {
            Environment environment = Main.simulator.getEnvironment();
            this.configureParameters(environment);
            initializeEnvironment(environment);
            environment.convertIOSToChances();
            setElements();
            playButton.setDisable(false);
            exportToCSVButton.setDisable(true);
            exportHeatMapButton.setDisable(true);
            Main.simulator.replenishStaticVars();
            disableEdits();
        }
        
    }

    @FXML
    public void playAction() {
        if (!Main.simulator.isRunning()) {
            Main.simulator.setRunning(true);
            Main.simulator.getPlaySemaphore().release();
            playButton.setText("Pause");
            exportToCSVButton.setDisable(true);
            exportHeatMapButton.setDisable(true);
        }
        else {
            Main.simulator.setRunning(false);
            playButton.setText("Play");
            exportToCSVButton.setDisable(false);
            exportHeatMapButton.setDisable(false);
        }
    }




}