package com.socialsim.controller.controls;

import com.socialsim.controller.Main;
import com.socialsim.controller.graphics.GraphicsController;
import com.socialsim.model.core.environment.Environment;

import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.simulator.SimulationTime;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.StackPane;

public class ScreenController extends Controller {
    private final double CANVAS_SCALE = 0.5;

    @FXML private Canvas backgroundCanvas;
    @FXML private Canvas foregroundCanvas;
    @FXML private Canvas markingsCanvas;
    @FXML private StackPane stackPane;
    @FXML private Button resetButton;
    @FXML private Button exportToCSVButton;
    @FXML private Button exportHeatMapButton;
    @FXML private ToggleButton playButton;
    @FXML private Slider speedSlider;
    public ScreenController() {
    }

    public void initializeOffice(Environment environment) {
        GraphicsController.tileSize = backgroundCanvas.getHeight() / Main.simulator.getEnvironment().getRows();
        mapEnvironment();
        Main.simulator.spawnInitialAgents(environment);
        drawInterface();
    }

    public void mapEnvironment() {

    }

    private void drawInterface() {
        drawEnvironmentViewBackground(Main.simulator.getEnvironment());
        drawEnvironmentViewForeground(Main.simulator.getEnvironment(), false);
    }

    public void drawEnvironmentViewBackground(Environment environment) {
        GraphicsController.requestDrawEnvironmentView(stackPane, environment, GraphicsController.tileSize, true, false);
    }

    public void drawEnvironmentViewForeground(Environment environment, boolean speedAware) {
        GraphicsController.requestDrawEnvironmentView(stackPane, environment, GraphicsController.tileSize, false, speedAware);
        requestUpdateInterfaceSimulationElements();
    }

    private void requestUpdateInterfaceSimulationElements() {
        Platform.runLater(this::updateSimulationTime);
        Platform.runLater(this::updateStatistics);
    }

    public void updateSimulationTime() {

    }

    public void updateStatistics() {

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

    public boolean validateParameters() {
        // insert code

        return true;
    }

    public void configureParameters(Environment environment) {
        // insert code
    }

    public void disableEdits() {
        // insert code
    }

    public void resetToDefault() {
        // insert code
    }




    @FXML
    private void initialize() {
        speedSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            SimulationTime.SLEEP_TIME_MILLISECONDS.set((int) (1.0 / newVal.intValue() * 1000));
        });

        resetToDefault();
        playButton.setDisable(true);
//        exportToCSVButton.setDisable(true);
//        exportHeatMapButton.setDisable(true);

        int width = 83;
        int length = 150;
        int rows = (int) Math.ceil(width / Patch.PATCH_SIZE_IN_SQUARE_METERS);
        int columns = (int) Math.ceil(length / Patch.PATCH_SIZE_IN_SQUARE_METERS);

        Environment environment = Environment.Factory.create(rows, columns);
        Main.simulator.resetToDefaultConfiguration(environment);

        Environment.configureDefaultIOS();
        Environment.configureDefaultInteractionTypeChances();

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
            initializeOffice(environment);
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