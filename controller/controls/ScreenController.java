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

        for (int j = 186; j <= 191; j++) {
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

        Main.simulator.getEnvironment().getWalls().add(Wall.wallFactory.create(wallTopsOutside, "WallTopOut"));
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