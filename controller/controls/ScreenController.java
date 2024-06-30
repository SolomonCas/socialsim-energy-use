package com.socialsim.controller.controls;

import com.socialsim.controller.Main;
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
import javafx.scene.Group;
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
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.scene.layout.Pane;

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
        simulator.setFridgeWattageInUse(Float.parseFloat(fridgeWattage.getText()));
        simulator.setFridgeWattageActive(Float.parseFloat(fridgeWattage.getText()));
        //AIRCON
        simulator.setAirconWattage(Float.parseFloat(airconWattage.getText()));
        simulator.setAirconWattageActive(Float.parseFloat(airconWattage.getText()));
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

        List<Patch> floorPatches = new ArrayList<>();

        for (int i = 0; i < environment.getRows(); i++) {
            for (int j = 0; j < environment.getColumns(); j++) {
                floorPatches.add(environment.getPatch(i, j));
            }
        }


        /*** FLOORS ***/
        simulator.getEnvironment().getFloors().add(Floor.floorFactory.create(floorPatches, "floor"));



        /****** OUTSIDE OFFICE ******/

            /*** WALL TOPS OUTSIDE OFFICE ***/

            List<Patch> wallTopsOutside = new ArrayList<>();

            for (int j = 0; j <= 203; j++) {
                wallTopsOutside.add(environment.getPatch(0, j));
            }

            for (int i = 1; i <= 13; i++) {
                for (int j = 0; j <= 31; j++) {
                    wallTopsOutside.add(environment.getPatch(i, j));
                }
            }

            for (int i = 1; i <= 21; i++) {
                for (int j = 32; j <= 126; j++) {
                    wallTopsOutside.add(environment.getPatch(i, j));
                }
            }

            for (int i = 1; i <= 33; i++) {
                for (int j = 127; j <= 146; j++) {
                    wallTopsOutside.add(environment.getPatch(i, j));
                }
            }

            for (int i = 1; i <= 46; i++) {
                for (int j = 147; j <= 153; j++) {
                    wallTopsOutside.add(environment.getPatch(i, j));
                }
            }

            for (int i = 1; i <= 55; i++) {
                for (int j = 154; j <= 176; j++) {
                    wallTopsOutside.add(environment.getPatch(i, j));
                }
            }

            for (int i = 1; i <= 23; i++) {
                for (int j = 177; j <= 184; j++) {
                    wallTopsOutside.add(environment.getPatch(i, j));
                }
            }

            for (int i = 1; i <= 9; i++) {
                wallTopsOutside.add(environment.getPatch(i, 185));
            }

            for (int j = 185; j <= 191; j++) {
                wallTopsOutside.add(environment.getPatch(10, j));
            }

            for (int i = 1; i <= 18; i++) {
                wallTopsOutside.add(environment.getPatch(i, 203));
            }

            for (int j = 191; j <= 203; j++) {
                wallTopsOutside.add(environment.getPatch(19, j));
            }

            for (int i = 20; i <= 22; i++) {
                for (int j = 193; j <= 203; j++) {
                    wallTopsOutside.add(environment.getPatch(i, j));
                }
            }

            for (int i = 23; i <= 30; i++) {
                for (int j = 194; j <= 203; j++) {
                    wallTopsOutside.add(environment.getPatch(i, j));
                }
            }

            for (int i = 31; i <= 33; i++) {
                for (int j = 193; j <= 203; j++) {
                    wallTopsOutside.add(environment.getPatch(i, j));
                }
            }

            for (int i = 34; i <= 41; i++) {
                for (int j = 194; j <= 203; j++) {
                    wallTopsOutside.add(environment.getPatch(i, j));
                }
            }

            for (int i = 42; i <= 44; i++) {
                for (int j = 193; j <= 203; j++) {
                    wallTopsOutside.add(environment.getPatch(i, j));
                }
            }

            for (int i = 45; i <= 52; i++) {
                for (int j = 194; j <= 203; j++) {
                    wallTopsOutside.add(environment.getPatch(i, j));
                }
            }

            for (int i = 53; i <= 55; i++) {
                for (int j = 193; j <= 203; j++) {
                    wallTopsOutside.add(environment.getPatch(i, j));
                }
            }

            for (int j = 191; j <= 203; j++) {
                wallTopsOutside.add(environment.getPatch(56, j));
            }

            for (int i = 56; i <= 75; i++) {
                for (int j = 184; j <= 185; j++) {
                    wallTopsOutside.add(environment.getPatch(i, j));
                }
            }

            for (int i = 56; i <= 65; i++) {
                wallTopsOutside.add(environment.getPatch(i, 186));
            }

            for (int j = 187; j <= 191; j++) {
                wallTopsOutside.add(environment.getPatch(65, j));
            }

            for (int j = 186; j <= 203; j++) {
                wallTopsOutside.add(environment.getPatch(75, j));
            }

            for (int i = 57; i <= 74; i++) {
                wallTopsOutside.add(environment.getPatch(i, 203));
            }

            simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(wallTopsOutside, "wallTopOut"));




            /*** WALLS OUTSIDE OFFICE ***/

            List<Patch> wallsOutside = new ArrayList<>();

            for (int i = 1; i <= 3; i++) {
                for (int j = 186; j <= 202; j++) {
                    wallsOutside.add(environment.getPatch(i, j));
                }
            }

            for (int i = 11; i <= 13; i++) {
                for (int j = 185; j <= 190; j++) {
                    wallsOutside.add(environment.getPatch(i, j));
                }
            }

            for (int i = 20; i <= 22; i++) {
                for (int j = 191; j <= 192; j++) {
                    wallsOutside.add(environment.getPatch(i, j));
                }
            }

            for (int i = 24; i <= 26; i++) {
                for (int j = 177; j <= 184; j++) {
                    wallsOutside.add(environment.getPatch(i, j));
                }
            }

            for (int i = 57; i <= 59; i++) {
                for (int j = 192; j <= 202; j++) {
                    wallsOutside.add(environment.getPatch(i, j));
                }
            }

            for (int i = 66; i <= 68; i++) {
                for (int j = 186; j <= 191; j++) {
                    wallsOutside.add(environment.getPatch(i, j));
                }
            }

            simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(wallsOutside, "wallOut"));




            /*** ENTRY POINT WALLS OUTSIDE OFFICE ***/

            List<Patch> doorWallsOutside = new ArrayList<>();

            for (int i = 11; i <= 13; i++) {
                doorWallsOutside.add(environment.getPatch(i, 191));
            }

            for (int i = 23; i <= 25; i++) {
                doorWallsOutside.add(environment.getPatch(i, 193));
            }

            for (int i = 34; i <= 36; i++) {
                doorWallsOutside.add(environment.getPatch(i, 193));
            }

            for (int i = 45; i <= 47; i++) {
                doorWallsOutside.add(environment.getPatch(i, 193));
            }

            for (int i = 57; i <= 59; i++) {
                doorWallsOutside.add(environment.getPatch(i, 191));
            }

            simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(doorWallsOutside, "doorWallOut"));




            /*** PATCHES OUTSIDE BUILDING ***/

            List<Patch> patchesOutsideBuilding = new ArrayList<>();

            for (int i = 109; i <= 128; i++) {
                for (int j = 0; j <= 22; j++) {
                    patchesOutsideBuilding.add(environment.getPatch(i, j));
                }
            }

            for (int i = 111; i <= 128; i++) {
                for (int j = 23; j <= 133; j++) {
                    patchesOutsideBuilding.add(environment.getPatch(i, j));
                }
            }

            for (int i = 118; i <= 128; i++) {
                for (int j = 187; j <= 203; j++) {
                    patchesOutsideBuilding.add(environment.getPatch(i, j));
                }
            }

            simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(patchesOutsideBuilding, "outsideBuilding"));




            /*** MALE BATHROOM ***/

            List<Patch> maleBathroom = new ArrayList<>();

            for (int i = 4; i <= 9; i++) {
                for (int j = 186; j <= 202; j++) {
                    maleBathroom.add(environment.getPatch(i, j));
                }
            }

            for (int i = 10; i <= 13; i++) {
                for (int j = 192; j <= 202; j++) {
                    maleBathroom.add(environment.getPatch(i, j));
                }
            }

            for (int i = 14; i <= 18; i++) {
                for (int j = 191; j <= 202; j++) {
                    maleBathroom.add(environment.getPatch(i, j));
                }
            }

            simulator.getEnvironment().getBathrooms().add(Bathroom.bathroomFactory.create(maleBathroom, "male"));




            /*** FEMALE BATHROOM ***/

            List<Patch> femaleBathroom = new ArrayList<>();

            for (int i = 60; i <= 64; i++) {
                for (int j = 191; j <= 202; j++) {
                    femaleBathroom.add(environment.getPatch(i, j));
                }
            }

            for (int i = 65; i <= 68; i++) {
                for (int j = 192; j <= 202; j++) {
                    femaleBathroom.add(environment.getPatch(i, j));
                }
            }

            for (int i = 69; i <= 74; i++) {
                for (int j = 186; j <= 202; j++) {
                    femaleBathroom.add(environment.getPatch(i, j));
                }
            }

            simulator.getEnvironment().getBathrooms().add(Bathroom.bathroomFactory.create(femaleBathroom, "female"));


        /****** INSIDE OFFICE ******/

            /*** WALL TOPS FOR OFFICE OUTLINE ***/

            List<Patch> wallTopsOfficeOutline = new ArrayList<>();

            for (int i = 14; i <= 105; i++) {
                wallTopsOfficeOutline.add(environment.getPatch(i, 0));
            }

            for (int j = 1; j <= 30; j++) {
                wallTopsOfficeOutline.add(environment.getPatch(14, j));
            }

            for (int i = 14; i <= 22; i++) {
                wallTopsOfficeOutline.add(environment.getPatch(i, 31));
            }

            for (int j = 32; j <= 125; j++) {
                wallTopsOfficeOutline.add(environment.getPatch(22, j));
            }

            for (int i = 22; i <= 34; i++) {
                wallTopsOfficeOutline.add(environment.getPatch(i, 126));
            }

            for (int j = 127; j <= 145; j++) {
                wallTopsOfficeOutline.add(environment.getPatch(34, j));
            }

            for (int i = 34; i <= 47; i++) {
                wallTopsOfficeOutline.add(environment.getPatch(i, 146));
            }

            for (int j = 147; j <= 152; j++) {
                wallTopsOfficeOutline.add(environment.getPatch(47, j));
            }

            for (int i = 47; i <= 56; i++) {
                wallTopsOfficeOutline.add(environment.getPatch(i, 153));
            }

            for (int j = 154; j <= 176; j++) {
                wallTopsOfficeOutline.add(environment.getPatch(56, j));
            }

            for (int j = 182; j <= 183; j++) {
                wallTopsOfficeOutline.add(environment.getPatch(56, j));
            }

            for (int i = 57; i <= 76; i++) {
                wallTopsOfficeOutline.add(environment.getPatch(i, 183));
            }

            for (int j = 184; j <= 202; j++) {
                wallTopsOfficeOutline.add(environment.getPatch(76, j));
            }

            for (int i = 76; i <= 114; i++) {
                wallTopsOfficeOutline.add(environment.getPatch(i, 203));
            }

            for (int j = 187; j <= 202; j++) {
                wallTopsOfficeOutline.add(environment.getPatch(114, j));
            }

            for (int i = 114; i <= 125; i++) {
                wallTopsOfficeOutline.add(environment.getPatch(i, 186));
            }

            for (int j = 135; j <= 185; j++) {
                wallTopsOfficeOutline.add(environment.getPatch(125, j));
            }

            for (int i = 107; i <= 125; i++) {
                wallTopsOfficeOutline.add(environment.getPatch(i, 134));
            }

            for (int j = 24; j <= 133; j++) {
                wallTopsOfficeOutline.add(environment.getPatch(107, j));
            }

            for (int i = 105; i <= 107; i++) {
                wallTopsOfficeOutline.add(environment.getPatch(i, 23));
            }

            for (int j = 0; j <= 22; j++) {
                wallTopsOfficeOutline.add(environment.getPatch(105, j));
            }


            simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(wallTopsOfficeOutline, "outlineWallTop"));



            /*** WALLS FOR OFFICE OUTLINE ***/

            List<Patch> wallOfficeOutline = new ArrayList<>();

            for (int i = 15; i <= 17; i++) {
                for (int j = 1; j <= 30; j++) {
                    wallOfficeOutline.add(environment.getPatch(i, j));
                }
            }

            for (int i = 23; i <= 25; i++) {
                for (int j = 31; j <= 125; j++) {
                    wallOfficeOutline.add(environment.getPatch(i, j));
                }
            }

            for (int i = 35; i <= 37; i++) {
                for (int j = 127; j <= 145; j++) {
                    wallOfficeOutline.add(environment.getPatch(i, j));
                }
            }

            for (int i = 48; i <= 50; i++) {
                for (int j = 147; j <= 152; j++) {
                    wallOfficeOutline.add(environment.getPatch(i, j));
                }
            }

            for (int i = 57; i <= 59; i++) {
                for (int j = 153; j <= 176; j++) {
                    wallOfficeOutline.add(environment.getPatch(i, j));
                }
            }

            for (int i = 77; i <= 79; i++) {
                for (int j = 183; j <= 202; j++) {
                    wallOfficeOutline.add(environment.getPatch(i, j));
                }
            }

            for (int i = 115; i <= 117; i++) {
                for (int j = 187; j <= 203; j++) {
                    wallOfficeOutline.add(environment.getPatch(i, j));
                }
            }

            for (int i = 126; i <= 128; i++) {
                for (int j = 134; j <= 186; j++) {
                    wallOfficeOutline.add(environment.getPatch(i, j));
                }
            }

            for (int i = 108; i <= 110; i++) {
                for (int j = 23; j <= 133; j++) {
                    wallOfficeOutline.add(environment.getPatch(i, j));
                }
            }

            for (int i = 106; i <= 108; i++) {
                for (int j = 0; j <= 22; j++) {
                    wallOfficeOutline.add(environment.getPatch(i, j));
                }
            }

            simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(wallOfficeOutline, "outlineWall"));




        /*** RECEPTION AREA ***/

            /** Reception Floor **/
            List<Patch> floorReception = new ArrayList<>();

            for (int i = 56; i <= 59; i++) {
                for (int j = 177; j <= 181; j++) {
                    floorReception.add(environment.getPatch(i, j));
                }
            }

            for (int i = 60; i <= 66; i++) {
                for (int j = 169; j <= 182; j++) {
                    floorReception.add(environment.getPatch(i, j));
                }
            }

            for (int i = 67; i <= 75; i++) {
                for (int j = 170; j <= 182; j++) {
                    floorReception.add(environment.getPatch(i, j));
                }
            }

            simulator.getEnvironment().getReceptions().add(Reception.receptionFactory.create(floorReception, ""));


            /** Wall Tops **/
            List<Patch> wallTopsReception = new ArrayList<>();

            for (int i = 67; i <= 76; i++) {
                wallTopsReception.add(environment.getPatch(i, 169));
            }
            for (int j = 170; j <= 182; j++) {
                wallTopsReception.add(environment.getPatch(76, j));
            }

            simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(wallTopsReception, "wallTopIn"));

            /** Walls **/
            List<Patch> wallsReception = new ArrayList<>();

            for (int i = 77; i <= 79; i++) {
                for (int j = 169; j <= 182; j++) {
                    wallsReception.add(environment.getPatch(i, j));
                }
            }

            simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(wallsReception, "wallIn"));

            /** Walls that signify entry/exit points **/
            List<Patch> doorWallsReception = new ArrayList<>();

            for (int i = 57; i <= 59; i++) {
                doorWallsReception.add(environment.getPatch(i, 169));
            }
            for (int i = 57; i <= 59; i++) {
                doorWallsReception.add(environment.getPatch(i, 182));
            }

            simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(doorWallsReception, "doorWallIn"));



        /*** STAFF AREA ***/

            /** Staff Area Floor **/
            List<Patch> floorStaffArea = new ArrayList<>();

            for (int i = 67; i <= 71; i++) {
                for (int j = 144; j <= 151; j++) {
                    floorStaffArea.add(environment.getPatch(i, j));
                }
            }
            for (int i = 67; i <= 71; i++) {
                for (int j = 153; j <= 160; j++) {
                    floorStaffArea.add(environment.getPatch(i, j));
                }
            }
            for (int i = 76; i <= 80; i++) {
                for (int j = 144; j <= 151; j++) {
                    floorStaffArea.add(environment.getPatch(i, j));
                }
            }
            for (int i = 76; i <= 80; i++) {
                for (int j = 153; j <= 160; j++) {
                    floorStaffArea.add(environment.getPatch(i, j));
                }
            }

            simulator.getEnvironment().getStaffRooms().add(StaffArea.staffAreaFactory.create(floorStaffArea, ""));


            /** Wall Tops **/
            List<Patch> wallTopsStaffArea = new ArrayList<>();

            for (int i = 67; i <= 80; i++) {
                wallTopsStaffArea.add(environment.getPatch(i, 143));
            }
            for (int i = 67; i <= 80; i++) {
                wallTopsStaffArea.add(environment.getPatch(i, 152));
            }
            for (int i = 67; i <= 80; i++) {
                wallTopsStaffArea.add(environment.getPatch(i, 161));
            }
            for (int j = 144; j <= 151; j++) {
                wallTopsStaffArea.add(environment.getPatch(72, j));
            }
            for (int j = 153; j <= 160; j++) {
                wallTopsStaffArea.add(environment.getPatch(72, j));
            }


            simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(wallTopsStaffArea, "wallTopIn"));

            /** Walls **/
            List<Patch> wallsStaffArea = new ArrayList<>();

            for (int i = 73; i <= 75; i++) {
                for (int j = 144; j <= 151; j++) {
                    wallsStaffArea.add(environment.getPatch(i, j));
                }
            }
            for (int i = 73; i <= 75; i++) {
                for (int j = 153; j <= 160; j++) {
                    wallsStaffArea.add(environment.getPatch(i, j));
                }
            }


            simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(wallsStaffArea, "wallIn"));




        /*** SOLO ROOMS ***/

            /** Solo Room 1 Floor **/
            List<Patch> floorSR1 = new ArrayList<>();

            for (int i = 67; i <= 70; i++) {
                for (int j = 97; j <= 100; j++) {
                    floorSR1.add(environment.getPatch(i, j));
                }
            }
            for (int i = 71; i <= 75; i++) {
                for (int j = 94; j <= 101; j++) {
                    floorSR1.add(environment.getPatch(i, j));
                }
            }

            simulator.getEnvironment().getSoloRooms().add(SoloRoom.soloRoomFactory.create(floorSR1, "SR1"));

            /** Solo Room 2 Floor **/
            List<Patch> floorSR2 = new ArrayList<>();

            for (int i = 71; i <= 75; i++) {
                for (int j = 85; j <= 92; j++) {
                    floorSR2.add(environment.getPatch(i, j));
                }
            }
            for (int i = 76; i <= 79; i++) {
                for (int j = 86; j <= 89; j++) {
                    floorSR2.add(environment.getPatch(i, j));
                }
            }

            simulator.getEnvironment().getSoloRooms().add(SoloRoom.soloRoomFactory.create(floorSR2, "SR2"));

            /** Solo Room 3 Floor **/
            List<Patch> floorSR3 = new ArrayList<>();

            for (int i = 71; i <= 75; i++) {
                for (int j = 48; j <= 55; j++) {
                    floorSR3.add(environment.getPatch(i, j));
                }
            }
            for (int i = 76; i <= 79; i++) {
                for (int j = 51; j <= 54; j++) {
                    floorSR3.add(environment.getPatch(i, j));
                }
            }

            simulator.getEnvironment().getSoloRooms().add(SoloRoom.soloRoomFactory.create(floorSR3, "SR3"));

            /** Solo Room 4 Floor **/
            List<Patch> floorSR4 = new ArrayList<>();

            for (int i = 67; i <= 70; i++) {
                for (int j = 40; j <= 43; j++) {
                    floorSR4.add(environment.getPatch(i, j));
                }
            }
            for (int i = 71; i <= 75; i++) {
                for (int j = 39; j <= 46; j++) {
                    floorSR4.add(environment.getPatch(i, j));
                }
            }

            simulator.getEnvironment().getSoloRooms().add(SoloRoom.soloRoomFactory.create(floorSR4, "SR4"));


            /** Wall Tops **/
            List<Patch> wallTopsSoloRoom = new ArrayList<>();

            // Solo Rooms 1-2
            for (int i = 68; i <= 75; i++) {
                wallTopsSoloRoom.add(environment.getPatch(i, 84));
            }
            for (int i = 68; i <= 75; i++) {
                wallTopsSoloRoom.add(environment.getPatch(i, 93));
            }
            for (int i = 68; i <= 75; i++) {
                wallTopsSoloRoom.add(environment.getPatch(i, 102));
            }
            for (int j = 84; j <= 96; j++) {
                wallTopsSoloRoom.add(environment.getPatch(67, j));
            }
            for (int j = 101; j <= 102; j++) {
                wallTopsSoloRoom.add(environment.getPatch(67, j));
            }
            for (int j = 84; j <= 85; j++) {
                wallTopsSoloRoom.add(environment.getPatch(76, j));
            }
            for (int j = 90; j <= 102; j++) {
                wallTopsSoloRoom.add(environment.getPatch(76, j));
            }

            // Solo Rooms 3-4
            for (int i = 68; i <= 75; i++) {
                wallTopsSoloRoom.add(environment.getPatch(i, 38));
            }
            for (int i = 68; i <= 75; i++) {
                wallTopsSoloRoom.add(environment.getPatch(i, 47));
            }
            for (int i = 68; i <= 75; i++) {
                wallTopsSoloRoom.add(environment.getPatch(i, 56));
            }
            for (int j = 38; j <= 39; j++) {
                wallTopsSoloRoom.add(environment.getPatch(67, j));
            }
            for (int j = 44; j <= 56; j++) {
                wallTopsSoloRoom.add(environment.getPatch(67, j));
            }
            for (int j = 38; j <= 50; j++) {
                wallTopsSoloRoom.add(environment.getPatch(76, j));
            }
            for (int j = 55; j <= 56; j++) {
                wallTopsSoloRoom.add(environment.getPatch(76, j));
            }


            simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(wallTopsSoloRoom, "wallTopIn"));

            /** Walls **/
            List<Patch> wallsSoloRoom = new ArrayList<>();

            // Solo Rooms 1-2
            for (int i = 68; i <= 70; i++) {
                for (int j = 85; j <= 92; j++) {
                    wallsSoloRoom.add(environment.getPatch(i, j));
                }
            }
            for (int i = 68; i <= 70; i++) {
                for (int j = 94; j <= 95; j++) {
                    wallsSoloRoom.add(environment.getPatch(i, j));
                }
            }
            for (int i = 77; i <= 79; i++) {
                wallsSoloRoom.add(environment.getPatch(i, 84));
            }
            for (int i = 77; i <= 79; i++) {
                for (int j = 91; j <= 102; j++) {
                    wallsSoloRoom.add(environment.getPatch(i, j));
                }
            }

            // Solo Rooms 3-4
            for (int i = 68; i <= 70; i++) {
                for (int j = 45; j <= 46; j++) {
                    wallsSoloRoom.add(environment.getPatch(i, j));
                }
            }
            for (int i = 68; i <= 70; i++) {
                for (int j = 48; j <= 55; j++) {
                    wallsSoloRoom.add(environment.getPatch(i, j));
                }
            }
            for (int i = 77; i <= 79; i++) {
                for (int j = 38; j <= 49; j++) {
                    wallsSoloRoom.add(environment.getPatch(i, j));
                }
            }
            for (int i = 77; i <= 79; i++) {
                wallsSoloRoom.add(environment.getPatch(i, 56));
            }


            simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(wallsSoloRoom, "wallIn"));

            /** Walls that signify entry/exit points **/
            List<Patch> doorWallsSoloRoom = new ArrayList<>();

            // SR 1
            for (int i = 68; i <= 70; i++) {
                doorWallsSoloRoom.add(environment.getPatch(i, 96));
            }
            for (int i = 68; i <= 70; i++) {
                doorWallsSoloRoom.add(environment.getPatch(i, 101));
            }

            // SR 2
            for (int i = 77; i <= 79; i++) {
                doorWallsSoloRoom.add(environment.getPatch(i, 85));
            }
            for (int i = 77; i <= 79; i++) {
                doorWallsSoloRoom.add(environment.getPatch(i, 90));
            }

            // SR 3
            for (int i = 77; i <= 79; i++) {
                doorWallsSoloRoom.add(environment.getPatch(i, 50));
            }
            for (int i = 77; i <= 79; i++) {
                doorWallsSoloRoom.add(environment.getPatch(i, 55));
            }
            // SR 4
            for (int i = 68; i <= 70; i++) {
                doorWallsSoloRoom.add(environment.getPatch(i, 39));
            }
            for (int i = 68; i <= 70; i++) {
                doorWallsSoloRoom.add(environment.getPatch(i, 44));
            }

            simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(doorWallsSoloRoom, "doorWallIn"));




        /*** DATA CENTER ***/

            /** Data Center Floor **/
            List<Patch> floorDataCenter = new ArrayList<>();

            for (int i = 38; i <= 55; i++) {
                for (int j = 127; j <= 145; j++) {
                    floorDataCenter.add(environment.getPatch(i, j));
                }
            }
            for (int i = 56; i <= 59; i++) {
                for (int j = 140; j <= 144; j++) {
                    floorDataCenter.add(environment.getPatch(i, j));
                }
            }

            simulator.getEnvironment().getDataCenters().add(DataCenter.dataCenterFactory.create(floorDataCenter, ""));


            /** Wall Tops **/
            List<Patch> wallTopsDataCenter = new ArrayList<>();

            for (int i = 35; i <= 56; i++) {
                wallTopsDataCenter.add(environment.getPatch(i, 126));
            }
            for (int j = 127; j <= 139; j++) {
                wallTopsDataCenter.add(environment.getPatch(56, j));
            }
            for (int j = 145; j <= 146; j++) {
                wallTopsDataCenter.add(environment.getPatch(56, j));
            }
            for (int i = 48; i <= 55; i++) {
                wallTopsDataCenter.add(environment.getPatch(i, 146));
            }
            for (int i = 50; i <= 51; i++) {
                for (int j = 136; j <= 137; j++) {
                    wallTopsDataCenter.add(environment.getPatch(i, j));
                }
            }

            simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(wallTopsDataCenter, "wallTopIn"));

            /** Walls **/
            List<Patch> wallsDataCenter = new ArrayList<>();

            for (int i = 57; i <= 59; i++) {
                for (int j = 126; j <= 138; j++) {
                    wallsDataCenter.add(environment.getPatch(i, j));
                }
            }
            for (int i = 57; i <= 59; i++) {
                wallsDataCenter.add(environment.getPatch(i, 146));
            }
            for (int i = 52; i <= 54; i++) {
                for (int j = 136; j <= 137; j++) {
                    wallsDataCenter.add(environment.getPatch(i, j));
                }
            }

            simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(wallsDataCenter, "wallIn"));

            /** Walls that signify entry/exit points **/
            List<Patch> doorWallsDataCenter = new ArrayList<>();

            for (int i = 57; i <= 59; i++) {
                doorWallsDataCenter.add(environment.getPatch(i, 139));
            }
            for (int i = 57; i <= 59; i++) {
                doorWallsDataCenter.add(environment.getPatch(i, 145));
            }

            simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(doorWallsDataCenter, "doorWallIn"));




        /*** CONTROL CENTER ***/

            /** Control Center Floor **/
            List<Patch> floorControlCenter = new ArrayList<>();

            for (int i = 26; i <= 59; i++) {
                for (int j = 107; j <= 125; j++) {
                    floorControlCenter.add(environment.getPatch(i, j));
                }
            }

            simulator.getEnvironment().getControlCenters().add(ControlCenter.controlCenterFactory.create(floorControlCenter, ""));


            /** Wall Tops **/
            List<Patch> wallTopsControlCenter = new ArrayList<>();

            wallTopsControlCenter.add(environment.getPatch(34, 107));
            for (int j = 113; j <= 125; j++) {
                wallTopsControlCenter.add(environment.getPatch(34, j));
            }
            for (int j = 107; j <= 119; j++) {
                wallTopsControlCenter.add(environment.getPatch(56, j));
            }
            wallTopsControlCenter.add(environment.getPatch(56, 125));

            simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(wallTopsControlCenter, "wallTopIn"));

            /** Walls **/
            List<Patch> wallsControlCenter = new ArrayList<>();

            for (int i = 35; i <= 37; i++) {
                for (int j = 114; j <= 125; j++) {
                    wallsControlCenter.add(environment.getPatch(i, j));
                }
            }
            for (int i = 57; i <= 59; i++) {
                for (int j = 106; j <= 118; j++) {
                    wallsControlCenter.add(environment.getPatch(i, j));
                }
            }
            for (int i = 52; i <= 54; i++) {
                for (int j = 136; j <= 137; j++) {
                    wallsControlCenter.add(environment.getPatch(i, j));
                }
            }

            simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(wallsControlCenter, "wallIn"));

            /** Walls that signify entry/exit points **/
            List<Patch> doorWallsControlCenter = new ArrayList<>();

            for (int i = 35; i <= 37; i++) {
                doorWallsControlCenter.add(environment.getPatch(i, 107));
            }
            for (int i = 35; i <= 37; i++) {
                doorWallsControlCenter.add(environment.getPatch(i, 113));
            }
            for (int i = 57; i <= 59; i++) {
                doorWallsControlCenter.add(environment.getPatch(i, 119));
            }
            for (int i = 57; i <= 59; i++) {
                doorWallsControlCenter.add(environment.getPatch(i, 125));
            }

            simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(doorWallsControlCenter, "doorWallIn"));




        /*** LEARNING SPACES ***/

            /** Learning Space 1 Floor **/
            List<Patch> floorLS1 = new ArrayList<>();

            for (int i = 26; i <= 59; i++) {
                for (int j = 86; j <= 105; j++) {
                    floorLS1.add(environment.getPatch(i, j));
                }
            }

            simulator.getEnvironment().getLearningSpaces().add(LearningSpace.learningSpaceFactory.create(floorLS1, "LS1"));

            /** Learning Space 2 Floor **/
            List<Patch> floorLS2 = new ArrayList<>();

            for (int i = 26; i <= 59; i++) {
                for (int j = 65; j <= 84; j++) {
                    floorLS2.add(environment.getPatch(i, j));
                }
            }

            simulator.getEnvironment().getLearningSpaces().add(LearningSpace.learningSpaceFactory.create(floorLS2, "LS2"));

            /** Learning Space 3 Floor **/
            List<Patch> floorLS3 = new ArrayList<>();

            for (int i = 26; i <= 59; i++) {
                for (int j = 44; j <= 63; j++) {
                    floorLS3.add(environment.getPatch(i, j));
                }
            }

            simulator.getEnvironment().getLearningSpaces().add(LearningSpace.learningSpaceFactory.create(floorLS3, "LS3"));

            /** Learning Space 4 Floor **/
            List<Patch> floorLS4 = new ArrayList<>();

            for (int i = 26; i <= 59; i++) {
                for (int j = 23; j <= 42; j++) {
                    floorLS4.add(environment.getPatch(i, j));
                }
            }

            simulator.getEnvironment().getLearningSpaces().add(LearningSpace.learningSpaceFactory.create(floorLS4, "LS4"));


            /** Wall Tops **/
            List<Patch> wallTopsLS = new ArrayList<>();


            for (int j = 105; j <= 106; j++) {
                wallTopsLS.add(environment.getPatch(56, j));
            }
            for (int j = 84; j <= 99; j++) {
                wallTopsLS.add(environment.getPatch(56, j));
            }
            for (int j = 63; j <= 78; j++) {
                wallTopsLS.add(environment.getPatch(56, j));
            }
            for (int j = 37; j <= 57; j++) {
                wallTopsLS.add(environment.getPatch(56, j));
            }
            for (int j = 22; j <= 31; j++) {
                wallTopsLS.add(environment.getPatch(56, j));
            }
            for (int j = 23; j <= 30; j++) {
                wallTopsLS.add(environment.getPatch(22, j));
            }

            for (int i = 23; i <= 55; i++) {
                wallTopsLS.add(environment.getPatch(i, 106));
            }
            for (int i = 23; i <= 55; i++) {
                wallTopsLS.add(environment.getPatch(i, 85));
            }
            for (int i = 23; i <= 55; i++) {
                wallTopsLS.add(environment.getPatch(i, 64));
            }
            for (int i = 23; i <= 55; i++) {
                wallTopsLS.add(environment.getPatch(i, 43));
            }
            for (int i = 22; i <= 55; i++) {
                wallTopsLS.add(environment.getPatch(i, 22));
            }


            simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(wallTopsLS, "wallTopIn"));

            /** Walls **/
            List<Patch> wallsLS = new ArrayList<>();

            for (int i = 23; i <= 25; i++) {
                for (int j = 23; j <= 30; j++) {
                    wallsLS.add(environment.getPatch(i, j));
                }
            }
            for (int i = 57; i <= 59; i++) {
                for (int j = 22; j <= 30; j++) {
                    wallsLS.add(environment.getPatch(i, j));
                }
            }
            for (int i = 57; i <= 59; i++) {
                for (int j = 38; j <= 56; j++) {
                    wallsLS.add(environment.getPatch(i, j));
                }
            }
            for (int i = 57; i <= 59; i++) {
                for (int j = 64; j <= 77; j++) {
                    wallsLS.add(environment.getPatch(i, j));
                }
            }
            for (int i = 57; i <= 59; i++) {
                for (int j = 85; j <= 98; j++) {
                    wallsLS.add(environment.getPatch(i, j));
                }
            }


            simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(wallsLS, "wallIn"));

            /** Walls that signify entry/exit points **/
            List<Patch> doorWallsLS = new ArrayList<>();

            // LS 1
            for (int i = 57; i <= 59; i++) {
                doorWallsLS.add(environment.getPatch(i, 105));
            }
            for (int i = 57; i <= 59; i++) {
                doorWallsLS.add(environment.getPatch(i, 99));
            }

            // LS 2
            for (int i = 57; i <= 59; i++) {
                doorWallsLS.add(environment.getPatch(i, 84));
            }
            for (int i = 57; i <= 59; i++) {
                doorWallsLS.add(environment.getPatch(i, 78));
            }

            // LS 3
            for (int i = 57; i <= 59; i++) {
                doorWallsLS.add(environment.getPatch(i, 63));
            }
            for (int i = 57; i <= 59; i++) {
                doorWallsLS.add(environment.getPatch(i, 57));
            }

            // LS 4
            for (int i = 57; i <= 59; i++) {
                doorWallsLS.add(environment.getPatch(i, 37));
            }
            for (int i = 57; i <= 59; i++) {
                doorWallsLS.add(environment.getPatch(i, 31));
            }

            simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(doorWallsLS, "doorWallIn"));




        /*** BREAKER ROOM ***/

            /** Breaker Room Floor **/
            List<Patch> floorBreakerRoom = new ArrayList<>();

            for (int i = 18; i <= 21; i++) {
                for (int j = 22; j <= 30; j++) {
                    floorBreakerRoom.add(environment.getPatch(i, j));
                }
            }

            simulator.getEnvironment().getBreakerRooms().add(BreakerRoom.breakerRoomFactory.create(floorBreakerRoom, ""));

            /** Walls that signify entry/exit points **/
            List<Patch> doorWallsBreakerRoom = new ArrayList<>();

            for (int i = 15; i <= 17; i++) {
                doorWallsBreakerRoom.add(environment.getPatch(i, 22));
            }

            simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(doorWallsBreakerRoom, "doorWallIn"));




        /*** MEETING ROOM ***/

            /** Meeting Room Floor **/
            List<Patch> floorMeetingRoom = new ArrayList<>();

            for (int i = 27; i <= 59; i++) {
                for (int j = 1; j <= 15; j++) {
                    floorMeetingRoom.add(environment.getPatch(i, j));
                }
            }

            simulator.getEnvironment().getMeetingRooms().add(MeetingRoom.meetingRoomFactory.create(floorMeetingRoom, ""));


            /** Wall Tops **/
            List<Patch> wallTopsMeetingRoom = new ArrayList<>();

            for (int j = 1; j <= 16; j++) {
                wallTopsMeetingRoom.add(environment.getPatch(23, j));
            }
            for (int i = 24; i <= 55; i++) {
                wallTopsMeetingRoom.add(environment.getPatch(i, 16));
            }
            for (int j = 1; j <= 9; j++) {
                wallTopsMeetingRoom.add(environment.getPatch(56, j));
                }
            for (int j = 15; j <= 16; j++) {
                wallTopsMeetingRoom.add(environment.getPatch(56, j));
            }


            simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(wallTopsMeetingRoom, "wallTopIn"));

            /** Walls **/
            List<Patch> wallsMeetingRoom = new ArrayList<>();

            for (int i = 24; i <= 26; i++) {
                for (int j = 1; j <= 15; j++) {
                    wallsMeetingRoom.add(environment.getPatch(i, j));
                }
            }
            for (int i = 57; i <= 59; i++) {
                for (int j = 1; j <= 8; j++) {
                    wallsMeetingRoom.add(environment.getPatch(i, j));
                }
            }
            for (int i = 57; i <= 59; i++) {
                wallsMeetingRoom.add(environment.getPatch(i, 16));
            }

            simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(wallsMeetingRoom, "wallIn"));

            /** Walls that signify entry/exit points **/
            List<Patch> doorWallsMeetingRoom = new ArrayList<>();

            for (int i = 57; i <= 59; i++) {
                doorWallsMeetingRoom.add(environment.getPatch(i, 9));
            }
            for (int i = 57; i <= 59; i++) {
                doorWallsMeetingRoom.add(environment.getPatch(i, 15));
            }

            simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(doorWallsMeetingRoom, "doorWallIn"));



        /*** CONFERENCE ROOM ***/

            /** Conference Room Floor **/
            List<Patch> floorConferenceRoom = new ArrayList<>();

            for (int i = 89; i <= 106; i++) {
                for (int j = 143; j <= 168; j++) {
                    floorConferenceRoom.add(environment.getPatch(i, j));
                }
            }

            simulator.getEnvironment().getConferenceRooms().add(ConferenceRoom.conferenceRoomFactory.create(floorConferenceRoom, ""));


            /** Wall Tops **/
            List<Patch> wallTopsConferenceRoom = new ArrayList<>();

            for (int j = 156; j <= 157; j++) {
                wallTopsConferenceRoom.add(environment.getPatch(88, j));
            }
            for (int j = 142; j <= 143; j++) {
                wallTopsConferenceRoom.add(environment.getPatch(89, j));
            }
            for (int j = 149; j <= 162; j++) {
                wallTopsConferenceRoom.add(environment.getPatch(89, j));
            }
            for (int j = 168; j <= 169; j++) {
                wallTopsConferenceRoom.add(environment.getPatch(89, j));
            }
            for (int j = 156; j <= 157; j++) {
                wallTopsConferenceRoom.add(environment.getPatch(90, j));
            }
            for (int j = 143; j <= 144; j++) {
                wallTopsConferenceRoom.add(environment.getPatch(104, j));
            }
            for (int j = 142; j <= 169; j++) {
                wallTopsConferenceRoom.add(environment.getPatch(107, j));
            }
            for (int i = 90; i <= 106; i++) {
                wallTopsConferenceRoom.add(environment.getPatch(i, 142));
            }
            for (int i = 90; i <= 106; i++) {
                wallTopsConferenceRoom.add(environment.getPatch(i, 169));
            }


            simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(wallTopsConferenceRoom, "wallTopIn"));

            /** Walls **/
            List<Patch> wallsConferenceRoom = new ArrayList<>();

            for (int i = 90; i <= 92; i++) {
                for (int j = 150; j <= 155; j++) {
                    wallsConferenceRoom.add(environment.getPatch(i, j));
                }
            }
            for (int i = 90; i <= 92; i++) {
                for (int j = 158; j <= 161; j++) {
                    wallsConferenceRoom.add(environment.getPatch(i, j));
                }
            }
            for (int i = 91; i <= 93; i++) {
                for (int j = 156; j <= 157; j++) {
                    wallsConferenceRoom.add(environment.getPatch(i, j));
                }
            }
            for (int i = 105; i <= 106; i++) {
                for (int j = 143; j <= 144; j++) {
                    wallsConferenceRoom.add(environment.getPatch(i, j));
                }
            }
            for (int i = 108; i <= 109; i++) {
                for (int j = 143; j <= 144; j++) {
                    wallsConferenceRoom.add(environment.getPatch(i, j));
                }
            }
            for (int i = 108; i <= 110; i++) {
                for (int j = 145; j <= 169; j++) {
                    wallsConferenceRoom.add(environment.getPatch(i, j));
                }
            }

            simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(wallsConferenceRoom, "wallIn"));

            /** Walls that signify entry/exit points **/
            List<Patch> doorWallsConferenceRoom = new ArrayList<>();

            for (int i = 90; i <= 92; i++) {
                doorWallsConferenceRoom.add(environment.getPatch(i, 143));
            }
            for (int i = 90; i <= 92; i++) {
                doorWallsConferenceRoom.add(environment.getPatch(i, 149));
            }
            for (int i = 90; i <= 92; i++) {
                doorWallsConferenceRoom.add(environment.getPatch(i, 162));
            }
            for (int i = 90; i <= 92; i++) {
                doorWallsConferenceRoom.add(environment.getPatch(i, 168));
            }

            simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(doorWallsConferenceRoom, "doorWallIn"));



        /*** STORAGE ROOM ***/

            /** Storage Room Floor **/
            List<Patch> floorStorageRoom = new ArrayList<>();

            for (int i = 89; i <= 106; i++) {
                for (int j = 130; j <= 141; j++) {
                    floorStorageRoom.add(environment.getPatch(i, j));
                }
            }

            simulator.getEnvironment().getStorageRooms().add(StorageRoom.storageRoomFactory.create(floorStorageRoom, ""));


            /** Wall Tops **/
            List<Patch> wallTopsStorageRoom = new ArrayList<>();

            for (int j = 131; j <= 132; j++) {
                wallTopsStorageRoom.add(environment.getPatch(88, j));
            }
            for (int j = 129; j <= 135; j++) {
                wallTopsStorageRoom.add(environment.getPatch(89, j));
            }
            wallTopsStorageRoom.add(environment.getPatch(89, 141));
            for (int j = 131; j <= 132; j++) {
                wallTopsStorageRoom.add(environment.getPatch(90, j));
            }
            for (int j = 135; j <= 141; j++) {
                wallTopsStorageRoom.add(environment.getPatch(107, j));
            }

            simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(wallTopsStorageRoom, "wallTopIn"));

            /** Walls **/
            List<Patch> wallsStorageRoom = new ArrayList<>();

            for (int i = 90; i <= 92; i++) {
                for (int j = 129; j <= 130; j++) {
                    wallsStorageRoom.add(environment.getPatch(i, j));
                }
            }
            for (int i = 90; i <= 92; i++) {
                for (int j = 133; j <= 134; j++) {
                    wallsStorageRoom.add(environment.getPatch(i, j));
                }
            }
            for (int i = 91; i <= 93; i++) {
                for (int j = 131; j <= 132; j++) {
                    wallsStorageRoom.add(environment.getPatch(i, j));
                }
            }
            for (int i = 108; i <= 110; i++) {
                for (int j = 135; j <= 141; j++) {
                    wallsStorageRoom.add(environment.getPatch(i, j));
                }
            }

            simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(wallsStorageRoom, "wallIn"));

            /** Walls that signify entry/exit points **/
            List<Patch> doorWallsStorageRoom = new ArrayList<>();

            for (int i = 90; i <= 92; i++) {
                doorWallsStorageRoom.add(environment.getPatch(i, 135));
            }
            for (int i = 90; i <= 92; i++) {
                doorWallsStorageRoom.add(environment.getPatch(i, 141));
            }

            simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(doorWallsStorageRoom, "doorWallIn"));



        /*** FACULTY ROOM ***/

            /** Faculty Room Floor **/
            List<Patch> floorFacultyRoom = new ArrayList<>();

            for (int i = 87; i <= 106; i++) {
                for (int j = 99; j <= 127; j++) {
                    floorFacultyRoom.add(environment.getPatch(i, j));
                }
            }

            simulator.getEnvironment().getFacultyRooms().add(FacultyRoom.facultyRoomFactory.create(floorFacultyRoom, ""));


            /** Wall Tops **/
            List<Patch> wallTopsFacultyRoom = new ArrayList<>();

            for (int j = 99; j <= 120; j++) {
                wallTopsFacultyRoom.add(environment.getPatch(87, j));
            }
            for (int j = 127; j <= 128; j++) {
                wallTopsFacultyRoom.add(environment.getPatch(87, j));
            }
            for (int i = 101; i <= 102; i++) {
                for (int j = 102; j <= 103; j++) {
                    wallTopsFacultyRoom.add(environment.getPatch(i, j));
                }
            }
            for (int i = 98; i <= 106; i++) {
                wallTopsFacultyRoom.add(environment.getPatch(i, 99));
            }
            for (int i = 88; i <= 106; i++) {
                wallTopsFacultyRoom.add(environment.getPatch(i, 128));
            }

            simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(wallTopsFacultyRoom, "wallTopIn"));

            /** Walls **/
            List<Patch> wallsFacultyRoom = new ArrayList<>();

            for (int i = 88; i <= 90; i++) {
                for (int j = 100; j <= 119; j++) {
                    wallsFacultyRoom.add(environment.getPatch(i, j));
                }
            }
            for (int i = 103; i <= 105; i++) {
                for (int j = 102; j <= 103; j++) {
                    wallsFacultyRoom.add(environment.getPatch(i, j));
                }
            }

            simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(wallsFacultyRoom, "wallIn"));

            /** Walls that signify entry/exit points **/
            List<Patch> doorWallsFacultyRoom = new ArrayList<>();

            for (int i = 88; i <= 90; i++) {
                doorWallsFacultyRoom.add(environment.getPatch(i, 99));
            }
            for (int i = 88; i <= 90; i++) {
                doorWallsFacultyRoom.add(environment.getPatch(i, 120));
            }
            for (int i = 88; i <= 90; i++) {
                doorWallsFacultyRoom.add(environment.getPatch(i, 127));
            }

            simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(doorWallsFacultyRoom, "doorWallIn"));




        /*** RESEARCH CENTER ***/

            /** Research Center Floor **/
            List<Patch> floorResearchCenter = new ArrayList<>();

            for (int i = 87; i <= 106; i++) {
                for (int j = 24; j <= 98; j++) {
                    floorResearchCenter.add(environment.getPatch(i, j));
                }
            }

            simulator.getEnvironment().getResearchCenters().add(ResearchCenter.researchCenterFactory.create(floorResearchCenter, ""));


            /** Wall Tops **/
            List<Patch> wallTopsResearchCenter = new ArrayList<>();

            wallTopsResearchCenter.add(environment.getPatch(87, 24));
            for (int j = 30; j <= 98; j++) {
                wallTopsResearchCenter.add(environment.getPatch(87, j));
            }
            for (int i = 98; i <= 106; i++) {
                wallTopsResearchCenter.add(environment.getPatch(i, 98));
            }
            for (int i = 96; i <= 97; i++) {
                for (int j = 31; j <= 32; j++) {
                    wallTopsResearchCenter.add(environment.getPatch(i, j));
                }
            }
            for (int i = 96; i <= 97; i++) {
                for (int j = 53; j <= 54; j++) {
                    wallTopsResearchCenter.add(environment.getPatch(i, j));
                }
            }
            for (int i = 101; i <= 102; i++) {
                for (int j = 83; j <= 84; j++) {
                    wallTopsResearchCenter.add(environment.getPatch(i, j));
                }
            }

            simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(wallTopsResearchCenter, "wallTopIn"));

            /** Walls **/
            List<Patch> wallsResearchCenter = new ArrayList<>();

            for (int i = 88; i <= 90; i++) {
                for (int j = 31; j <= 97; j++) {
                    wallsResearchCenter.add(environment.getPatch(i, j));
                }
            }
            for (int i = 103; i <= 105; i++) {
                for (int j = 83; j <= 84; j++) {
                    wallsResearchCenter.add(environment.getPatch(i, j));
                }
            }
            for (int i = 98; i <= 100; i++) {
                for (int j = 53; j <= 54; j++) {
                    wallsResearchCenter.add(environment.getPatch(i, j));
                }
            }
            for (int i = 98; i <= 100; i++) {
                for (int j = 31; j <= 32; j++) {
                    wallsResearchCenter.add(environment.getPatch(i, j));
                }
            }

            simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(wallsResearchCenter, "wallIn"));

            /** Walls that signify entry/exit points **/
            List<Patch> doorWallsResearchCenter = new ArrayList<>();

            for (int i = 88; i <= 90; i++) {
                doorWallsResearchCenter.add(environment.getPatch(i, 24));
            }
            for (int i = 88; i <= 90; i++) {
                doorWallsResearchCenter.add(environment.getPatch(i, 30));
            }
            for (int i = 88; i <= 90; i++) {
                doorWallsResearchCenter.add(environment.getPatch(i, 98));
            }

            simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(doorWallsResearchCenter, "doorWallIn"));




        /*** DATA COLLECTION ROOM ***/

            /** Data Collection Room Floor **/
            List<Patch> floorDCRoom = new ArrayList<>();

            for (int i = 87; i <= 104; i++) {
                for (int j = 1; j <= 22; j++) {
                    floorDCRoom.add(environment.getPatch(i, j));
                }
            }

            simulator.getEnvironment().getDataCollectionRooms().add(DataCollectionRoom.dataCollectionRoomFactory.create(floorDCRoom, ""));


            /** Wall Tops **/
            List<Patch> wallTopsDCRoom = new ArrayList<>();

            for (int j = 1; j <= 8; j++) {
                wallTopsDCRoom.add(environment.getPatch(87, j));
            }
            for (int j = 14; j <= 16; j++) {
                wallTopsDCRoom.add(environment.getPatch(87, j));
            }
            for (int j = 22; j <= 23; j++) {
                wallTopsDCRoom.add(environment.getPatch(87, j));
            }
            for (int j = 1; j <= 6; j++) {
                wallTopsDCRoom.add(environment.getPatch(96, j));
            }
            wallTopsDCRoom.add(environment.getPatch(104, 6));
            for (int i = 88; i <= 104; i++) {
                wallTopsDCRoom.add(environment.getPatch(i, 23));
            }

            simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(wallTopsDCRoom, "wallTopIn"));

            /** Walls **/
            List<Patch> wallsDCRoom = new ArrayList<>();

            for (int i = 88; i <= 90; i++) {
                for (int j = 1; j <= 7; j++) {
                    wallsDCRoom.add(environment.getPatch(i, j));
                }
            }
            for (int i = 97; i <= 99; i++) {
                for (int j = 1; j <= 5; j++) {
                    wallsDCRoom.add(environment.getPatch(i, j));
                }
            }
            for (int i = 88; i <= 90; i++) {
                wallsDCRoom.add(environment.getPatch(i, 15));
            }

            simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(wallsDCRoom, "wallIn"));

            /** Walls that signify entry/exit points **/
            List<Patch> doorWallsDCRoom = new ArrayList<>();

            for (int i = 88; i <= 90; i++) {
                doorWallsDCRoom.add(environment.getPatch(i, 8));
            }
            for (int i = 88; i <= 90; i++) {
                doorWallsDCRoom.add(environment.getPatch(i, 14));
            }
            for (int i = 88; i <= 90; i++) {
                doorWallsDCRoom.add(environment.getPatch(i, 16));
            }
            for (int i = 88; i <= 90; i++) {
                doorWallsDCRoom.add(environment.getPatch(i, 22));
            }
            for (int i = 97; i <= 99; i++) {
                doorWallsDCRoom.add(environment.getPatch(i, 6));
            }

            simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(doorWallsDCRoom, "doorWallIn"));




        /*** HUMAN EXPERIENCE ROOM ***/

            /** Human Experience Room Floor **/
            List<Patch> floorHERoom = new ArrayList<>();

            for (int i = 71; i <= 86; i++) {
                for (int j = 1; j <= 15; j++) {
                    floorHERoom.add(environment.getPatch(i, j));
                }
            }

            simulator.getEnvironment().getHumanExpRooms().add(HumanExpRoom.humanExpRoomFactory.create(floorHERoom, ""));


            /** Wall Tops **/
            List<Patch> wallTopsHERoom = new ArrayList<>();

            for (int j = 1; j <= 15; j++) {
                wallTopsHERoom.add(environment.getPatch(67, j));
            }
            for (int i = 77; i <= 86; i++) {
                wallTopsHERoom.add(environment.getPatch(i, 15));
            }

            simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(wallTopsHERoom, "wallTopIn"));

            /** Walls **/
            List<Patch> wallsHERoom = new ArrayList<>();

            for (int i = 68; i <= 70; i++) {
                for (int j = 1; j <= 14; j++) {
                    wallsHERoom.add(environment.getPatch(i, j));
                }
            }

            simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(wallsHERoom, "wallIn"));

            /** Walls that signify entry/exit points **/
            List<Patch> doorWallsHERoom = new ArrayList<>();

            for (int i = 68; i <= 70; i++) {
                doorWallsHERoom.add(environment.getPatch(i, 15));
            }

            simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(doorWallsHERoom, "doorWallIn"));




        /*** CLINIC ***/

            /** Clinic Floor **/
            List<Patch> floorClinic = new ArrayList<>();

            for (int i = 80; i <= 87; i++) {
                for (int j = 186; j <= 193; j++) {
                    floorClinic.add(environment.getPatch(i, j));
                }
            }

            simulator.getEnvironment().getClinics().add(Clinic.clinicFactory.create(floorClinic, ""));


            /** Wall Tops **/
            List<Patch> wallTopsClinic = new ArrayList<>();

            for (int j = 186; j <= 194; j++) {
                wallTopsClinic.add(environment.getPatch(88, j));
            }
            for (int i = 85; i <= 87; i++) {
                wallTopsClinic.add(environment.getPatch(i, 186));
            }
            for (int i = 77; i <= 87; i++) {
                wallTopsClinic.add(environment.getPatch(i, 194));
            }

            simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(wallTopsClinic, "wallTopIn"));

            /** Walls **/
            List<Patch> wallsClinic = new ArrayList<>();

            for (int i = 89; i <= 91; i++) {
                for (int j = 187; j <= 194; j++) {
                    wallsClinic.add(environment.getPatch(i, j));
                }
            }

            simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(wallsClinic, "wallIn"));

            /** Walls that signify entry/exit points **/
            List<Patch> doorWallsClinic = new ArrayList<>();

            for (int i = 77; i <= 79; i++) {
                doorWallsHERoom.add(environment.getPatch(i, 186));
            }

            simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(doorWallsHERoom, "doorWallIn"));




        /*** DIRECTOR'S BATHROOM ***/

            /** Director's BathRoom Floor **/
            List<Patch> floorDB = new ArrayList<>();

            for (int i = 80; i <= 91; i++) {
                for (int j = 195; j <= 202; j++) {
                    floorDB.add(environment.getPatch(i, j));
                }
            }

            simulator.getEnvironment().getBathrooms().add(Bathroom.bathroomFactory.create(floorDB, "director"));


            /** Wall Tops **/
            List<Patch> wallTopsDB = new ArrayList<>();

            for (int j = 200; j <= 202; j++) {
                wallTopsDB.add(environment.getPatch(88, j));
            }

            simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(wallTopsDB, "wallTopIn"));

            /** Walls **/
            List<Patch> wallsDB = new ArrayList<>();

            for (int i = 89; i <= 91; i++) {
                for (int j = 201; j <= 202; j++) {
                    wallsDB.add(environment.getPatch(i, j));
                }
            }

            simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(wallsDB, "wallIn"));

            /** Walls that signify entry/exit points **/
            List<Patch> doorWallsDB = new ArrayList<>();

            for (int i = 89; i <= 91; i++) {
                doorWallsDB.add(environment.getPatch(i, 194));
            }
            for (int i = 89; i <= 91; i++) {
                doorWallsDB.add(environment.getPatch(i, 200));
            }

            simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(doorWallsDB, "doorWallIn"));




        /*** DIRECTOR'S ROOM ***/

            /** Director's Room Floor **/
            List<Patch> floorDRoom = new ArrayList<>();

            for (int i = 92; i <= 113; i++) {
                for (int j = 186; j <= 202; j++) {
                    floorDRoom.add(environment.getPatch(i, j));
                }
            }

            simulator.getEnvironment().getDirectorRooms().add(DirectorRoom.directorRoomFactory.create(floorDRoom, ""));


            /** Wall Tops **/
            List<Patch> wallTopsDRoom = new ArrayList<>();

            for (int i = 97; i <= 113; i++) {
                wallTopsDRoom.add(environment.getPatch(i, 186));
            }
            for (int i = 108; i <= 109; i++) {
                for (int j = 201; j <= 202; j++) {
                    wallTopsDRoom.add(environment.getPatch(i, j));
                }
            }

            simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(wallTopsDRoom, "wallTopIn"));

            /** Walls **/
            List<Patch> wallsDRoom = new ArrayList<>();

            for (int i = 110; i <= 112; i++) {
                for (int j = 201; j <= 202; j++) {
                    wallsDRoom.add(environment.getPatch(i, j));
                }
            }

            simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(wallsDRoom, "wallIn"));

            /** Walls that signify entry/exit points **/
            List<Patch> doorWallsDRoom = new ArrayList<>();

            for (int i = 89; i <= 91; i++) {
                doorWallsDRoom.add(environment.getPatch(i, 186));
            }

            simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(doorWallsDRoom, "doorWallIn"));




        /*** PANTRY ***/

            /** Pantry Floor **/
            List<Patch> floorPantry = new ArrayList<>();

            for (int i = 111; i <= 124; i++) {
                for (int j = 135; j <= 185; j++) {
                    floorPantry.add(environment.getPatch(i, j));
                }
            }

            simulator.getEnvironment().getPantries().add(Pantry.pantryFactory.create(floorPantry, ""));


            /** Wall Tops **/
            List<Patch> wallTopsPantry = new ArrayList<>();

            for (int i = 108; i <= 110; i++) {
                wallTopsPantry.add(environment.getPatch(i, 142));
            }
            for (int j = 143; j <= 144; j++) {
                wallTopsPantry.add(environment.getPatch(110, j));
            }
            for (int i = 116; i <= 117; i++) {
                for (int j = 144; j <= 145; j++) {
                    wallTopsPantry.add(environment.getPatch(i, j));
                }
            }
            for (int i = 116; i <= 117; i++) {
                for (int j = 165; j <= 166; j++) {
                    wallTopsPantry.add(environment.getPatch(i, j));
                }
            }

            simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(wallTopsPantry, "wallTopIn"));

            /** Walls **/
            List<Patch> wallsPantry = new ArrayList<>();

            for (int i = 111; i <= 113; i++) {
                for (int j = 142; j <= 144; j++) {
                    wallsPantry.add(environment.getPatch(i, j));
                }
            }
            for (int i = 118; i <= 120; i++) {
                for (int j = 144; j <= 145; j++) {
                    wallsPantry.add(environment.getPatch(i, j));
                }
            }
            for (int i = 118; i <= 120; i++) {
                for (int j = 165; j <= 166; j++) {
                    wallsPantry.add(environment.getPatch(i, j));
                }
            }

            simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(wallsPantry, "wallIn"));





        /*** AMENITIES ***/


            /*** MESA Table and Chair Set ***/

            // Table on North and West
            List<Patch> NWMESATableChairSetPatches = new ArrayList<>();
            NWMESATableChairSetPatches.add(environment.getPatch(67,153));
            CubicleMapper.draw(NWMESATableChairSetPatches, "MESA", "", "NORTH_AND_WEST", true);

            // Table on North and East
            List<Patch> NEMESATableChairSetPatches = new ArrayList<>();
            NEMESATableChairSetPatches.add(environment.getPatch(67,148));
            CubicleMapper.draw(NEMESATableChairSetPatches, "MESA", "", "NORTH_AND_EAST", false);

            // Table on South and West
            List<Patch> SWMESATableChairSetPatches = new ArrayList<>();
            SWMESATableChairSetPatches.add(environment.getPatch(76,153));
            CubicleMapper.draw(SWMESATableChairSetPatches, "MESA", "", "SOUTH_AND_WEST", false);

            // Table on South and East
            List<Patch> SEMESATableChairSetPatches = new ArrayList<>();
            SEMESATableChairSetPatches.add(environment.getPatch(76,148));
            CubicleMapper.draw(SEMESATableChairSetPatches, "MESA", "", "SOUTH_AND_EAST", false);

            /*** Cubicle Type A ***/

            // Two Cubicles Back-to-Back (Vertical) with appliance
            List<Patch> CubicleTypeA = new ArrayList<>();
            CubicleTypeA.add(environment.getPatch(43,111));
            CubicleMapper.draw(CubicleTypeA, "TYPE_A", "", "", true);


            /*** Cubicle Type B ***/

            // FACING WEST
            List<Patch> WCubicleTypeB = new ArrayList<>();
            WCubicleTypeB.add(environment.getPatch(95,105));
            WCubicleTypeB.add(environment.getPatch(95,115));
            WCubicleTypeB.add(environment.getPatch(95,125));
            WCubicleTypeB.add(environment.getPatch(99,105));
            WCubicleTypeB.add(environment.getPatch(99,115));
            WCubicleTypeB.add(environment.getPatch(99,125));
            CubicleMapper.draw(WCubicleTypeB, "TYPE_B", "WEST", "", true);

            // FACING WEST
            List<Patch> ECubicleTypeB = new ArrayList<>();
            ECubicleTypeB.add(environment.getPatch(95,108));
            ECubicleTypeB.add(environment.getPatch(95,118));
            ECubicleTypeB.add(environment.getPatch(99,108));
            ECubicleTypeB.add(environment.getPatch(99,118));
            CubicleMapper.draw(ECubicleTypeB, "TYPE_B", "EAST", "", true);


            /*** Cubicle Type C ***/

            // FACING WEST
            List<Patch> WCubicleTypeC = new ArrayList<>();
            WCubicleTypeC.add(environment.getPatch(41,120));
            WCubicleTypeC.add(environment.getPatch(44,120));
            WCubicleTypeC.add(environment.getPatch(47,120));
            WCubicleTypeC.add(environment.getPatch(50,120));
            CubicleMapper.draw(WCubicleTypeC, "TYPE_C", "WEST", "", false);


            /*** Reception Table ***/
            List<Patch> ReceptionTable1x8 = new ArrayList<>();
            ReceptionTable1x8.add(environment.getPatch(69,170)); // Reception Bar
            ReceptionTableMapper.draw(ReceptionTable1x8, "1x8");

            /*** Research Table ***/

            // Facing West with appliance
            List<Patch> FWResearchTableWithApp = new ArrayList<>();
            FWResearchTableWithApp.add(environment.getPatch(99,46));
            FWResearchTableWithApp.add(environment.getPatch(99,68));
            FWResearchTableWithApp.add(environment.getPatch(99,76));
            ResearchTableMapper.draw(FWResearchTableWithApp, "WEST", true);

            // Facing East with appliance
            List<Patch> FEResearchTableWithApp = new ArrayList<>();
            FEResearchTableWithApp.add(environment.getPatch(99,91));
            ResearchTableMapper.draw(FEResearchTableWithApp, "EAST", true);

            // Facing West no appliance
            List<Patch> FWResearchTableNoApp = new ArrayList<>();
            FWResearchTableNoApp.add(environment.getPatch(99,38));
            FWResearchTableNoApp.add(environment.getPatch(99,60));
            FWResearchTableNoApp.add(environment.getPatch(99,90));
            ResearchTableMapper.draw(FWResearchTableNoApp, "WEST", false);

            // Facing East no appliance
            List<Patch> FEResearchTableNoApp = new ArrayList<>();
            FEResearchTableNoApp.add(environment.getPatch(99,39));
            FEResearchTableNoApp.add(environment.getPatch(99,47));
            FEResearchTableNoApp.add(environment.getPatch(99,61));
            FEResearchTableNoApp.add(environment.getPatch(99,69));
            FEResearchTableNoApp.add(environment.getPatch(99,77));
            ResearchTableMapper.draw(FEResearchTableNoApp, "EAST", false);

            /*** Meeting Table ***/

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



            /*** Learning Table ***/
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

            /*** Pantry Table ***/

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
            
            // TYPE A CHAIRS
            List<Patch> southPantryChairTypeA = new ArrayList<>();
            southPantryChairTypeA.add(environment.getPatch(111,150));
            southPantryChairTypeA.add(environment.getPatch(111,151));
            southPantryChairTypeA.add(environment.getPatch(111,152));
            southPantryChairTypeA.add(environment.getPatch(111,153));
            southPantryChairTypeA.add(environment.getPatch(111,154));
            ChairMapper.draw(southPantryChairTypeA, 0, "SOUTH", "PANTRY_TYPE_A", "NULL");

            // TYPE B CHAIRS
            List<Patch> southPantryChairTypeB = new ArrayList<>();
            southPantryChairTypeB.add(environment.getPatch(111,145));
            southPantryChairTypeB.add(environment.getPatch(111,146));
            southPantryChairTypeB.add(environment.getPatch(111,147));
            southPantryChairTypeB.add(environment.getPatch(111,148));
            southPantryChairTypeB.add(environment.getPatch(111,149));
            ChairMapper.draw(southPantryChairTypeB, 0, "SOUTH", "PANTRY_TYPE_B", "NULL");


            /*** Director Table ***/

            List<Patch> directorTable = new ArrayList<>();
            directorTable.add(environment.getPatch(108,190));
            DirectorTableMapper.draw(directorTable, "HORIZONTAL", true);

            /*** Solo Table ***/

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

            /*** Human Experience Table ***/
            List<Patch> humanExpTable = new ArrayList<>();
            humanExpTable.add(environment.getPatch(77,5));
            HumanExpTableMapper.draw(humanExpTable, "5x1");

            /*** Data Collection Table ***/
            List<Patch> dataCollTable = new ArrayList<>();
            dataCollTable.add(environment.getPatch(91,2));
            DataCollTableMapper.draw(dataCollTable, "1x6");


            /*** White Board ***/

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

            /*** Elevator ***/
            List<Patch> elevator = new ArrayList<>();
            elevator.add(environment.getPatch(26,193));
            elevator.add(environment.getPatch(37,193));
            elevator.add(environment.getPatch(48,193));
            ElevatorMapper.draw(elevator, Elevator.ElevatorMode.ENTRANCE_AND_EXIT,  "VERTICAL");

            /*** Couch ***/
            List<Patch> couch = new ArrayList<>();
            couch.add(environment.getPatch(69,141));
            CouchMapper.draw(couch, "WEST");

            /*** Refrigerator ***/
            List<Patch> refrigerator = new ArrayList<>();
            refrigerator.add(environment.getPatch(110,141));
            RefrigeratorMapper.draw(refrigerator);

            /*** Water Dispenser ***/
            List<Patch> waterDispenser = new ArrayList<>();
            waterDispenser.add(environment.getPatch(110,140));
            WaterDispenserMapper.draw(waterDispenser);

            /*** Plant ***/
            List<Patch> plants = new ArrayList<>();
            plants.add(environment.getPatch(62,1));
            plants.add(environment.getPatch(74,179));
            plants.add(environment.getPatch(74,181));
            plants.add(environment.getPatch(75,180));
            plants.add(environment.getPatch(82,202));
            plants.add(environment.getPatch(84,202));
            plants.add(environment.getPatch(86,202));
            PlantMapper.draw(plants);

            /*** Trash Can ***/
            List<Patch> trashCans = new ArrayList<>();
            trashCans.add(environment.getPatch(80,199));
            trashCans.add(environment.getPatch(113,135));
            trashCans.add(environment.getPatch(107,201));
            TrashCanMapper.draw(trashCans);

            /*** Pantry Cabinet ***/
            List<Patch> pantryCabinets = new ArrayList<>();
            pantryCabinets.add(environment.getPatch(109,135));
            pantryCabinets.add(environment.getPatch(109,136));
            pantryCabinets.add(environment.getPatch(109,137));
            pantryCabinets.add(environment.getPatch(109,138));
            PantryCabinetMapper.draw(pantryCabinets);

            /*** Sink ***/

            // South
            List<Patch> southSinks = new ArrayList<>();
            southSinks.add(environment.getPatch(60,194));
            southSinks.add(environment.getPatch(60,197));
            southSinks.add(environment.getPatch(60,200));
            southSinks.add(environment.getPatch(80,197));
            southSinks.add(environment.getPatch(111,136));
            SinkMapper.draw(southSinks, "SOUTH");

            // North
            List<Patch> northSinks = new ArrayList<>();
            northSinks.add(environment.getPatch(18,194));
            northSinks.add(environment.getPatch(18,197));
            northSinks.add(environment.getPatch(18,200));
            SinkMapper.draw(northSinks, "NORTH");

            /*** Sink ***/

            // South
            List<Patch> southToilets = new ArrayList<>();
            southToilets.add(environment.getPatch(3,188));
            southToilets.add(environment.getPatch(3,191));
            southToilets.add(environment.getPatch(3,194));
            southToilets.add(environment.getPatch(3,197));
            southToilets.add(environment.getPatch(3,200));
            southToilets.add(environment.getPatch(79,200));
            ToiletMapper.draw(southToilets, "SOUTH");

            // North
            List<Patch> northToilets = new ArrayList<>();
            northToilets.add(environment.getPatch(73,188));
            northToilets.add(environment.getPatch(73,191));
            northToilets.add(environment.getPatch(73,194));
            northToilets.add(environment.getPatch(73,197));
            northToilets.add(environment.getPatch(73,200));
            ToiletMapper.draw(northToilets, "NORTH");

            /*** Coffee Maker Bar ***/
            List<Patch> coffeeMakerBar = new ArrayList<>();
            coffeeMakerBar.add(environment.getPatch(110,137));
            CoffeeMakerBarMapper.draw(coffeeMakerBar);

            /*** Kettle Bar ***/
            List<Patch> kettleBar = new ArrayList<>();
            kettleBar.add(environment.getPatch(110,138));
            KettleBarMapper.draw(kettleBar);

            /*** Microwave Bar ***/
            List<Patch> microwaveBar = new ArrayList<>();
            microwaveBar.add(environment.getPatch(111,135));
            MicrowaveBarMapper.draw(microwaveBar);

            /*** Switches ***/

            // South Light Switches
            List<Patch> southLightSwitches = new ArrayList<>();
            southLightSwitches.add(environment.getPatch(25,13));
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
            southACSwitches.add(environment.getPatch(25,14));
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

            /*** Aircon ***/
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

            /*** Lights ***/

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
            singlePendantLights.add(environment.getPatch(117,135));
            singlePendantLights.add(environment.getPatch(120,135));
            singlePendantLights.add(environment.getPatch(123,135));
            singlePendantLights.add(environment.getPatch(114,184));
            singlePendantLights.add(environment.getPatch(121,184));

            // Director Room
            singlePendantLights.add(environment.getPatch(98,198));

            // Director Bathroom
            singlePendantLights.add(environment.getPatch(82,196));
            singlePendantLights.add(environment.getPatch(82,201));

            // Hall
            singlePendantLights.add(environment.getPatch(75,78));
            singlePendantLights.add(environment.getPatch(75,106));

            LightMapper.draw(singlePendantLights, "SINGLE_PENDANT_LIGHT", "");


            //  HORIZONTAL LINEAR_PENDANT_LIGHT
            List<Patch> horizontalLinearPendantLights = new ArrayList<>();

            // Data Coll Room
            horizontalLinearPendantLights.add(environment.getPatch(91,5));

            // Faculty Room
            horizontalLinearPendantLights.add(environment.getPatch(97,109));
            horizontalLinearPendantLights.add(environment.getPatch(98,109));
            horizontalLinearPendantLights.add(environment.getPatch(97,115));
            horizontalLinearPendantLights.add(environment.getPatch(98,115));

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
            verticalLinearPendantLights.add(environment.getPatch(100,38));
            verticalLinearPendantLights.add(environment.getPatch(102,38));
            verticalLinearPendantLights.add(environment.getPatch(100,47));
            verticalLinearPendantLights.add(environment.getPatch(102,47));
            verticalLinearPendantLights.add(environment.getPatch(100,60));
            verticalLinearPendantLights.add(environment.getPatch(102,60));
            verticalLinearPendantLights.add(environment.getPatch(100,68));
            verticalLinearPendantLights.add(environment.getPatch(102,68));
            verticalLinearPendantLights.add(environment.getPatch(100,76));
            verticalLinearPendantLights.add(environment.getPatch(102,76));
            verticalLinearPendantLights.add(environment.getPatch(100,90));
            verticalLinearPendantLights.add(environment.getPatch(102,90));

            LightMapper.draw(verticalLinearPendantLights, "LINEAR_PENDANT_LIGHT", "VERTICAL");

            //  HORIZONTAL RECESSED_LINEAR_LIGHT
            List<Patch> horizontalRecessedLinearLights = new ArrayList<>();

            // Faculty Room
            horizontalRecessedLinearLights.add(environment.getPatch(96,126));
            horizontalRecessedLinearLights.add(environment.getPatch(102,126));

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



            //  VERTICAL TRACK_LIGHT
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