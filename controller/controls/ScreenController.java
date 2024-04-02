package com.socialsim.controller.controls;

import com.socialsim.controller.Main;
import com.socialsim.controller.graphics.GraphicsController;
import com.socialsim.model.core.environment.Environment;

import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchfield.*;
import com.socialsim.model.simulator.SimulationTime;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class ScreenController extends Controller {
    private final double CANVAS_SCALE = 0.5;

    @FXML private Canvas backgroundCanvas;
    @FXML private Canvas foregroundCanvas;
    @FXML private Canvas markingsCanvas;
    @FXML private StackPane stackPane;
    @FXML private Button resetButton;
    @FXML private Button configureIOSButton;
    @FXML private Button exportToCSVButton;
    @FXML private Button exportHeatMapButton;
    @FXML private ToggleButton playButton;
    @FXML private Slider speedSlider;
    public ScreenController() {
    }

    public void initialize(Environment environment) {
        GraphicsController.tileSize = backgroundCanvas.getHeight() / Main.simulator.getEnvironment().getRows();
        mapEnvironment();
        Main.simulator.spawnInitialAgents(environment);
        environment.configureDefaultIOS();
        drawInterface();
    }

    public void mapEnvironment() {
        Environment environment = Main.simulator.getEnvironment();

        List<Patch> wallPatches = new ArrayList<>();
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

        // Clinic & Executive Dean environment Wall
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
                else if ((i == 58 || i == 59) && (j < 23 || (j > 24 && j < 42) || j > 43)) {
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

        // Executive Dean's Bathroom
        List<Patch> execDeanBathroom = new ArrayList<>();
        for (int i = 46; i < 56; i++) {
            for (int j = 143; j < 149; j++) {
                execDeanBathroom.add(environment.getPatch(i, j));
            }
        }
        Main.simulator.getEnvironment().getBathrooms().add(Bathroom.bathroomFactory.create(execDeanBathroom, 3));

        // Executive Dean's Office
        List<Patch> execDeanOfficeAreaPatches = new ArrayList<>();
        for (int i = 57; i < 77; i++) {
            for (int j = 135; j < 149; j++) {
                execDeanOfficeAreaPatches.add(environment.getPatch(i, j));
            }
        }
        Main.simulator.getEnvironment().getDeanRooms().add(DeanRoom.deanRoomFactory.create(execDeanOfficeAreaPatches, 1));


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
        configureIOSButton.setDisable(true);
    }

    public void resetToDefault() {
        // insert code
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

//        environment.copyDefaultToIOS();
//        environment.copyDefaultToInteractionTypeChances();
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
            initialize(environment);
            environment.convertIOSToChances();
            setElements();
            playButton.setDisable(false);
//            exportToCSVButton.setDisable(true);
//            exportHeatMapButton.setDisable(true);
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