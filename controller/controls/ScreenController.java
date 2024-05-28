package com.socialsim.controller.controls;

import com.socialsim.controller.Main;
import com.socialsim.controller.graphics.GraphicsController;
import com.socialsim.model.core.agent.AgentMovement;
import com.socialsim.model.core.environment.Environment;
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


        /*** FLOORS ***/
        Main.simulator.getEnvironment().getFloors().add(Floor.floorFactory.create(floorPatches, "floor"));



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

            Main.simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(wallTopsOutside, "wallTopOut"));




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

            Main.simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(wallsOutside, "wallOut"));




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

            Main.simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(doorWallsOutside, "doorWallOut"));




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

            Main.simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(patchesOutsideBuilding, "outsideBuilding"));




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

            Main.simulator.getEnvironment().getBathrooms().add(Bathroom.bathroomFactory.create(maleBathroom, "male"));




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

            Main.simulator.getEnvironment().getBathrooms().add(Bathroom.bathroomFactory.create(femaleBathroom, "female"));


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


            Main.simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(wallTopsOfficeOutline, "outlineWallTop"));



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

            Main.simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(wallOfficeOutline, "outlineWall"));




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

            Main.simulator.getEnvironment().getReceptions().add(Reception.receptionFactory.create(floorReception, ""));


            /** Wall Tops **/
            List<Patch> wallTopsReception = new ArrayList<>();

            for (int i = 67; i <= 76; i++) {
                wallTopsReception.add(environment.getPatch(i, 169));
            }
            for (int j = 170; j <= 182; j++) {
                wallTopsReception.add(environment.getPatch(76, j));
            }

            Main.simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(wallTopsReception, "wallTopIn"));

            /** Walls **/
            List<Patch> wallsReception = new ArrayList<>();

            for (int i = 77; i <= 79; i++) {
                for (int j = 169; j <= 182; j++) {
                    wallsReception.add(environment.getPatch(i, j));
                }
            }

            Main.simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(wallsReception, "wallIn"));

            /** Walls that signify entry/exit points **/
            List<Patch> doorWallsReception = new ArrayList<>();

            for (int i = 57; i <= 59; i++) {
                doorWallsReception.add(environment.getPatch(i, 169));
            }
            for (int i = 57; i <= 59; i++) {
                doorWallsReception.add(environment.getPatch(i, 182));
            }

            Main.simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(doorWallsReception, "doorWallIn"));



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

            Main.simulator.getEnvironment().getStaffRooms().add(StaffArea.staffAreaFactory.create(floorStaffArea, ""));


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


            Main.simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(wallTopsStaffArea, "wallTopIn"));

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


            Main.simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(wallsStaffArea, "wallIn"));




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

            Main.simulator.getEnvironment().getSoloRooms().add(SoloRoom.soloRoomFactory.create(floorSR1, "SR1"));

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

            Main.simulator.getEnvironment().getSoloRooms().add(SoloRoom.soloRoomFactory.create(floorSR2, "SR2"));

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

            Main.simulator.getEnvironment().getSoloRooms().add(SoloRoom.soloRoomFactory.create(floorSR3, "SR3"));

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

            Main.simulator.getEnvironment().getSoloRooms().add(SoloRoom.soloRoomFactory.create(floorSR4, "SR4"));


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


            Main.simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(wallTopsSoloRoom, "wallTopIn"));

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


            Main.simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(wallsSoloRoom, "wallIn"));

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

            Main.simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(doorWallsSoloRoom, "doorWallIn"));




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

            Main.simulator.getEnvironment().getDataCenters().add(DataCenter.dataCenterFactory.create(floorDataCenter, ""));


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

            Main.simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(wallTopsDataCenter, "wallTopIn"));

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

            Main.simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(wallsDataCenter, "wallIn"));

            /** Walls that signify entry/exit points **/
            List<Patch> doorWallsDataCenter = new ArrayList<>();

            for (int i = 57; i <= 59; i++) {
                doorWallsDataCenter.add(environment.getPatch(i, 139));
            }
            for (int i = 57; i <= 59; i++) {
                doorWallsDataCenter.add(environment.getPatch(i, 145));
            }

            Main.simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(doorWallsDataCenter, "doorWallIn"));




        /*** CONTROL CENTER ***/

            /** Control Center Floor **/
            List<Patch> floorControlCenter = new ArrayList<>();

            for (int i = 26; i <= 59; i++) {
                for (int j = 107; j <= 125; j++) {
                    floorControlCenter.add(environment.getPatch(i, j));
                }
            }

            Main.simulator.getEnvironment().getControlCenters().add(ControlCenter.controlCenterFactory.create(floorControlCenter, ""));


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

            Main.simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(wallTopsControlCenter, "wallTopIn"));

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

            Main.simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(wallsControlCenter, "wallIn"));

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

            Main.simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(doorWallsControlCenter, "doorWallIn"));




        /*** LEARNING SPACES ***/

            /** Learning Space 1 Floor **/
            List<Patch> floorLS1 = new ArrayList<>();

            for (int i = 26; i <= 59; i++) {
                for (int j = 86; j <= 105; j++) {
                    floorLS1.add(environment.getPatch(i, j));
                }
            }

            Main.simulator.getEnvironment().getLearningSpaces().add(LearningSpace.learningSpaceFactory.create(floorLS1, "LS1"));

            /** Learning Space 2 Floor **/
            List<Patch> floorLS2 = new ArrayList<>();

            for (int i = 26; i <= 59; i++) {
                for (int j = 65; j <= 84; j++) {
                    floorLS2.add(environment.getPatch(i, j));
                }
            }

            Main.simulator.getEnvironment().getLearningSpaces().add(LearningSpace.learningSpaceFactory.create(floorLS2, "LS2"));

            /** Learning Space 3 Floor **/
            List<Patch> floorLS3 = new ArrayList<>();

            for (int i = 26; i <= 59; i++) {
                for (int j = 44; j <= 63; j++) {
                    floorLS3.add(environment.getPatch(i, j));
                }
            }

            Main.simulator.getEnvironment().getLearningSpaces().add(LearningSpace.learningSpaceFactory.create(floorLS3, "LS3"));

            /** Learning Space 4 Floor **/
            List<Patch> floorLS4 = new ArrayList<>();

            for (int i = 26; i <= 59; i++) {
                for (int j = 23; j <= 42; j++) {
                    floorLS4.add(environment.getPatch(i, j));
                }
            }

            Main.simulator.getEnvironment().getLearningSpaces().add(LearningSpace.learningSpaceFactory.create(floorLS4, "LS4"));


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


            Main.simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(wallTopsLS, "wallTopIn"));

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


            Main.simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(wallsLS, "wallIn"));

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

            Main.simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(doorWallsLS, "doorWallIn"));




        /*** BREAKER ROOM ***/

            /** Breaker Room Floor **/
            List<Patch> floorBreakerRoom = new ArrayList<>();

            for (int i = 18; i <= 21; i++) {
                for (int j = 22; j <= 30; j++) {
                    floorBreakerRoom.add(environment.getPatch(i, j));
                }
            }

            Main.simulator.getEnvironment().getBreakerRooms().add(BreakerRoom.breakerRoomFactory.create(floorBreakerRoom, ""));

            /** Walls that signify entry/exit points **/
            List<Patch> doorWallsBreakerRoom = new ArrayList<>();

            for (int i = 15; i <= 17; i++) {
                doorWallsBreakerRoom.add(environment.getPatch(i, 22));
            }

            Main.simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(doorWallsBreakerRoom, "doorWallIn"));




        /*** MEETING ROOM ***/

            /** Meeting Room Floor **/
            List<Patch> floorMeetingRoom = new ArrayList<>();

            for (int i = 27; i <= 59; i++) {
                for (int j = 1; j <= 15; j++) {
                    floorMeetingRoom.add(environment.getPatch(i, j));
                }
            }

            Main.simulator.getEnvironment().getMeetingRooms().add(MeetingRoom.meetingRoomFactory.create(floorMeetingRoom, ""));


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


            Main.simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(wallTopsMeetingRoom, "wallTopIn"));

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

            Main.simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(wallsMeetingRoom, "wallIn"));

            /** Walls that signify entry/exit points **/
            List<Patch> doorWallsMeetingRoom = new ArrayList<>();

            for (int i = 57; i <= 59; i++) {
                doorWallsMeetingRoom.add(environment.getPatch(i, 9));
            }
            for (int i = 57; i <= 59; i++) {
                doorWallsMeetingRoom.add(environment.getPatch(i, 15));
            }

            Main.simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(doorWallsMeetingRoom, "doorWallIn"));



        /*** CONFERENCE ROOM ***/

            /** Conference Room Floor **/
            List<Patch> floorConferenceRoom = new ArrayList<>();

            for (int i = 89; i <= 106; i++) {
                for (int j = 143; j <= 168; j++) {
                    floorConferenceRoom.add(environment.getPatch(i, j));
                }
            }

            Main.simulator.getEnvironment().getConferenceRooms().add(ConferenceRoom.conferenceRoomFactory.create(floorConferenceRoom, ""));


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


            Main.simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(wallTopsConferenceRoom, "wallTopIn"));

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

            Main.simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(wallsConferenceRoom, "wallIn"));

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

            Main.simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(doorWallsConferenceRoom, "doorWallIn"));



        /*** STORAGE ROOM ***/

            /** Storage Room Floor **/
            List<Patch> floorStorageRoom = new ArrayList<>();

            for (int i = 89; i <= 106; i++) {
                for (int j = 130; j <= 141; j++) {
                    floorStorageRoom.add(environment.getPatch(i, j));
                }
            }

            Main.simulator.getEnvironment().getStorageRooms().add(StorageRoom.storageRoomFactory.create(floorStorageRoom, ""));


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

            Main.simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(wallTopsStorageRoom, "wallTopIn"));

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

            Main.simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(wallsStorageRoom, "wallIn"));

            /** Walls that signify entry/exit points **/
            List<Patch> doorWallsStorageRoom = new ArrayList<>();

            for (int i = 90; i <= 92; i++) {
                doorWallsStorageRoom.add(environment.getPatch(i, 135));
            }
            for (int i = 90; i <= 92; i++) {
                doorWallsStorageRoom.add(environment.getPatch(i, 141));
            }

            Main.simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(doorWallsStorageRoom, "doorWallIn"));



        /*** FACULTY ROOM ***/

            /** Faculty Room Floor **/
            List<Patch> floorFacultyRoom = new ArrayList<>();

            for (int i = 87; i <= 106; i++) {
                for (int j = 99; j <= 127; j++) {
                    floorFacultyRoom.add(environment.getPatch(i, j));
                }
            }

            Main.simulator.getEnvironment().getFacultyRooms().add(FacultyRoom.facultyRoomFactory.create(floorFacultyRoom, ""));


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

            Main.simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(wallTopsFacultyRoom, "wallTopIn"));

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

            Main.simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(wallsFacultyRoom, "wallIn"));

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

            Main.simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(doorWallsFacultyRoom, "doorWallIn"));




        /*** RESEARCH CENTER ***/

            /** Research Center Floor **/
            List<Patch> floorResearchCenter = new ArrayList<>();

            for (int i = 87; i <= 106; i++) {
                for (int j = 24; j <= 98; j++) {
                    floorResearchCenter.add(environment.getPatch(i, j));
                }
            }

            Main.simulator.getEnvironment().getResearchCenters().add(ResearchCenter.researchCenterFactory.create(floorResearchCenter, ""));


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

            Main.simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(wallTopsResearchCenter, "wallTopIn"));

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

            Main.simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(wallsResearchCenter, "wallIn"));

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

            Main.simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(doorWallsResearchCenter, "doorWallIn"));




        /*** DATA COLLECTION ROOM ***/

            /** Data Collection Room Floor **/
            List<Patch> floorDCRoom = new ArrayList<>();

            for (int i = 87; i <= 104; i++) {
                for (int j = 1; j <= 22; j++) {
                    floorDCRoom.add(environment.getPatch(i, j));
                }
            }

            Main.simulator.getEnvironment().getDataCollectionRooms().add(DataCollectionRoom.dataCollectionRoomFactory.create(floorDCRoom, ""));


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

            Main.simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(wallTopsDCRoom, "wallTopIn"));

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

            Main.simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(wallsDCRoom, "wallIn"));

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

            Main.simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(doorWallsDCRoom, "doorWallIn"));




        /*** HUMAN EXPERIENCE ROOM ***/

            /** Human Experience Room Floor **/
            List<Patch> floorHERoom = new ArrayList<>();

            for (int i = 71; i <= 86; i++) {
                for (int j = 1; j <= 15; j++) {
                    floorHERoom.add(environment.getPatch(i, j));
                }
            }

            Main.simulator.getEnvironment().getHumanExpRooms().add(HumanExpRoom.humanExpRoomFactory.create(floorHERoom, ""));


            /** Wall Tops **/
            List<Patch> wallTopsHERoom = new ArrayList<>();

            for (int j = 1; j <= 15; j++) {
                wallTopsHERoom.add(environment.getPatch(67, j));
            }
            for (int i = 77; i <= 86; i++) {
                wallTopsHERoom.add(environment.getPatch(i, 15));
            }

            Main.simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(wallTopsHERoom, "wallTopIn"));

            /** Walls **/
            List<Patch> wallsHERoom = new ArrayList<>();

            for (int i = 68; i <= 70; i++) {
                for (int j = 1; j <= 14; j++) {
                    wallsHERoom.add(environment.getPatch(i, j));
                }
            }

            Main.simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(wallsHERoom, "wallIn"));

            /** Walls that signify entry/exit points **/
            List<Patch> doorWallsHERoom = new ArrayList<>();

            for (int i = 68; i <= 70; i++) {
                doorWallsHERoom.add(environment.getPatch(i, 15));
            }

            Main.simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(doorWallsHERoom, "doorWallIn"));




        /*** CLINIC ***/

            /** Clinic Floor **/
            List<Patch> floorClinic = new ArrayList<>();

            for (int i = 80; i <= 87; i++) {
                for (int j = 186; j <= 193; j++) {
                    floorClinic.add(environment.getPatch(i, j));
                }
            }

            Main.simulator.getEnvironment().getClinics().add(Clinic.clinicFactory.create(floorClinic, ""));


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

            Main.simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(wallTopsClinic, "wallTopIn"));

            /** Walls **/
            List<Patch> wallsClinic = new ArrayList<>();

            for (int i = 89; i <= 91; i++) {
                for (int j = 187; j <= 194; j++) {
                    wallsClinic.add(environment.getPatch(i, j));
                }
            }

            Main.simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(wallsClinic, "wallIn"));

            /** Walls that signify entry/exit points **/
            List<Patch> doorWallsClinic = new ArrayList<>();

            for (int i = 77; i <= 79; i++) {
                doorWallsHERoom.add(environment.getPatch(i, 186));
            }

            Main.simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(doorWallsHERoom, "doorWallIn"));




        /*** DIRECTOR'S BATHROOM ***/

            /** Director's BathRoom Floor **/
            List<Patch> floorDB = new ArrayList<>();

            for (int i = 80; i <= 91; i++) {
                for (int j = 195; j <= 202; j++) {
                    floorDB.add(environment.getPatch(i, j));
                }
            }

            Main.simulator.getEnvironment().getBathrooms().add(Bathroom.bathroomFactory.create(floorDB, "director"));


            /** Wall Tops **/
            List<Patch> wallTopsDB = new ArrayList<>();

            for (int j = 200; j <= 202; j++) {
                wallTopsDB.add(environment.getPatch(88, j));
            }

            Main.simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(wallTopsDB, "wallTopIn"));

            /** Walls **/
            List<Patch> wallsDB = new ArrayList<>();

            for (int i = 89; i <= 91; i++) {
                for (int j = 201; j <= 202; j++) {
                    wallsDB.add(environment.getPatch(i, j));
                }
            }

            Main.simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(wallsDB, "wallIn"));

            /** Walls that signify entry/exit points **/
            List<Patch> doorWallsDB = new ArrayList<>();

            for (int i = 89; i <= 91; i++) {
                doorWallsDB.add(environment.getPatch(i, 194));
            }
            for (int i = 89; i <= 91; i++) {
                doorWallsDB.add(environment.getPatch(i, 200));
            }

            Main.simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(doorWallsDB, "doorWallIn"));




        /*** DIRECTOR'S ROOM ***/

            /** Director's Room Floor **/
            List<Patch> floorDRoom = new ArrayList<>();

            for (int i = 92; i <= 113; i++) {
                for (int j = 186; j <= 202; j++) {
                    floorDRoom.add(environment.getPatch(i, j));
                }
            }

            Main.simulator.getEnvironment().getDirectorRooms().add(DirectorRoom.directorRoomFactory.create(floorDRoom, ""));


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

            Main.simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(wallTopsDRoom, "wallTopIn"));

            /** Walls **/
            List<Patch> wallsDRoom = new ArrayList<>();

            for (int i = 110; i <= 112; i++) {
                for (int j = 201; j <= 202; j++) {
                    wallsDRoom.add(environment.getPatch(i, j));
                }
            }

            Main.simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(wallsDRoom, "wallIn"));

            /** Walls that signify entry/exit points **/
            List<Patch> doorWallsDRoom = new ArrayList<>();

            for (int i = 89; i <= 91; i++) {
                doorWallsDRoom.add(environment.getPatch(i, 186));
            }

            Main.simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(doorWallsDRoom, "doorWallIn"));




        /*** PANTRY ***/

            /** Pantry Floor **/
            List<Patch> floorPantry = new ArrayList<>();

            for (int i = 111; i <= 124; i++) {
                for (int j = 135; j <= 185; j++) {
                    floorPantry.add(environment.getPatch(i, j));
                }
            }

            Main.simulator.getEnvironment().getPantries().add(Pantry.pantryFactory.create(floorPantry, ""));


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

            Main.simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(wallTopsPantry, "wallTopIn"));

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

            Main.simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(wallsPantry, "wallIn"));

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
        int width = 129;
        int length = 204;
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