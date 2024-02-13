package com.socialsim.controller.office.controls;

import com.socialsim.controller.generic.controls.ScreenController;
import com.socialsim.controller.Main;
import com.socialsim.controller.office.graphics.OfficeGraphicsController;
import com.socialsim.controller.office.graphics.amenity.mapper.*;
import com.socialsim.model.core.agent.office.OfficeAgentMovement;
import com.socialsim.model.core.environment.generic.Patch;
import com.socialsim.model.core.environment.generic.patchfield.Wall;
import com.socialsim.model.core.environment.office.Office;
import com.socialsim.model.core.environment.office.patchfield.*;
import com.socialsim.model.core.environment.office.patchobject.passable.gate.OfficeGate;
import com.socialsim.model.simulator.SimulationTime;
import com.socialsim.model.simulator.office.OfficeSimulator;
import com.socialsim.model.simulator.university.UniversitySimulator;
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

public class OfficeScreenController extends ScreenController {

    @FXML private StackPane stackPane;
    @FXML private Canvas backgroundCanvas;
    @FXML private Canvas foregroundCanvas;
    @FXML private Canvas markingsCanvas;
    @FXML private Text elapsedTimeText;
    @FXML private ToggleButton playButton;
    @FXML private Button resetButton;
    @FXML private Slider speedSlider;
    @FXML private Button resetToDefaultButton;
    @FXML private TextField nonverbalMean;
    @FXML private TextField nonverbalStdDev;
    @FXML private TextField cooperativeMean;
    @FXML private TextField cooperativeStdDev;
    @FXML private TextField exchangeMean;
    @FXML private TextField exchangeStdDev;
    @FXML private TextField maxClients;
    @FXML private TextField maxDrivers;
    @FXML private TextField maxVisitors;
    @FXML private TextField maxCurrentClients;
    @FXML private TextField maxCurrentDrivers;
    @FXML private TextField maxCurrentVisitors;
    @FXML private TextField fieldOfView;
    @FXML private Button configureIOSButton;
    @FXML private Button editInteractionButton;
    @FXML private Label currentManagerCount;
    @FXML private Label currentBusinessCount;
    @FXML private Label currentResearchCount;
    @FXML private Label currentTechnicalCount;
    @FXML private Label currentSecretaryCount;
    @FXML private Label currentClientCount;
    @FXML private Label currentDriverCount;
    @FXML private Label currentVisitorCount;
    @FXML private Label currentNonverbalCount;
    @FXML private Label currentCooperativeCount;
    @FXML private Label currentExchangeCount;
    @FXML private Label averageNonverbalDuration;
    @FXML private Label averageCooperativeDuration;
    @FXML private Label averageExchangeDuration;
    @FXML private Label currentTeam1Count;
    @FXML private Label currentTeam2Count;
    @FXML private Label currentTeam3Count;
    @FXML private Label currentTeam4Count;
    @FXML private Label currentBossManagerCount;
    @FXML private Label currentBossBusinessCount;
    @FXML private Label currentBossResearcherCount;
    @FXML private Label currentBossTechnicalCount;
    @FXML private Label currentBossJanitorCount;
    @FXML private Label currentBossClientCount;
    @FXML private Label currentBossDriverCount;
    @FXML private Label currentBossVisitorCount;
    @FXML private Label currentBossGuardCount;
    @FXML private Label currentBossReceptionistCount;
    @FXML private Label currentBossSecretaryCount;
    @FXML private Label currentManagerManagerCount;
    @FXML private Label currentManagerBusinessCount;
    @FXML private Label currentManagerResearcherCount;
    @FXML private Label currentManagerTechnicalCount;
    @FXML private Label currentManagerJanitorCount;
    @FXML private Label currentManagerClientCount;
    @FXML private Label currentManagerDriverCount;
    @FXML private Label currentManagerVisitorCount;
    @FXML private Label currentManagerGuardCount;
    @FXML private Label currentManagerReceptionistCount;
    @FXML private Label currentManagerSecretaryCount;
    @FXML private Label currentBusinessBusinessCount;
    @FXML private Label currentBusinessResearcherCount;
    @FXML private Label currentBusinessTechnicalCount;
    @FXML private Label currentBusinessJanitorCount;
    @FXML private Label currentBusinessClientCount;
    @FXML private Label currentBusinessDriverCount;
    @FXML private Label currentBusinessVisitorCount;
    @FXML private Label currentBusinessGuardCount;
    @FXML private Label currentBusinessReceptionistCount;
    @FXML private Label currentBusinessSecretaryCount;
    @FXML private Label currentResearcherResearcherCount;
    @FXML private Label currentResearcherTechnicalCount;
    @FXML private Label currentResearcherJanitorCount;
    @FXML private Label currentResearcherClientCount;
    @FXML private Label currentResearcherDriverCount;
    @FXML private Label currentResearcherVisitorCount;
    @FXML private Label currentResearcherGuardCount;
    @FXML private Label currentResearcherReceptionistCount;
    @FXML private Label currentResearcherSecretaryCount;
    @FXML private Label currentTechnicalTechnicalCount;
    @FXML private Label currentTechnicalJanitorCount;
    @FXML private Label currentTechnicalClientCount;
    @FXML private Label currentTechnicalDriverCount;
    @FXML private Label currentTechnicalVisitorCount;
    @FXML private Label currentTechnicalGuardCount;
    @FXML private Label currentTechnicalReceptionistCount;
    @FXML private Label currentTechnicalSecretaryCount;
    @FXML private Label currentJanitorJanitorCount;
    @FXML private Label currentJanitorClientCount;
    @FXML private Label currentJanitorDriverCount;
    @FXML private Label currentJanitorVisitorCount;
    @FXML private Label currentJanitorSecretaryCount;
    @FXML private Label currentClientClientCount;
    @FXML private Label currentClientDriverCount;
    @FXML private Label currentClientVisitorCount;
    @FXML private Label currentClientGuardCount;
    @FXML private Label currentClientReceptionistCount;
    @FXML private Label currentClientSecretaryCount;
    @FXML private Label currentDriverDriverCount;
    @FXML private Label currentDriverVisitorCount;
    @FXML private Label currentDriverGuardCount;
    @FXML private Label currentDriverReceptionistCount;
    @FXML private Label currentDriverSecretaryCount;
    @FXML private Label currentVisitorVisitorCount;
    @FXML private Label currentVisitorGuardCount;
    @FXML private Label currentVisitorReceptionistCount;
    @FXML private Label currentVisitorSecretaryCount;
    @FXML private Label currentGuardSecretaryCount;
    @FXML private Label currentReceptionistSecretaryCount;
    @FXML private Label currentSecretarySecretaryCount;
    @FXML private Button exportToCSVButton;
    @FXML private Button exportHeatMapButton;

    private final double CANVAS_SCALE = 0.5;

    public OfficeScreenController() {
    }

    public StackPane getStackPane() {
        return stackPane;
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

        int width = 84;
        int length = 142;
        int rows = (int) Math.ceil(width / Patch.PATCH_SIZE_IN_SQUARE_METERS);
        int columns = (int) Math.ceil(length / Patch.PATCH_SIZE_IN_SQUARE_METERS);
        Office office = Office.OfficeFactory.create(rows, columns);
        Main.officeSimulator.resetToDefaultConfiguration(office);
        Office.configureDefaultIOS();
        office.copyDefaultToIOS();
        Office.configureDefaultInteractionTypeChances();
        office.copyDefaultToInteractionTypeChances();
    }

    @FXML
    public void initializeAction() {
        if (Main.officeSimulator.isRunning()) {
            playAction();
            playButton.setSelected(false);
        }
        if (validateParameters()) {
            Office office = Main.officeSimulator.getOffice();
            this.configureParameters(office);
            initializeOffice(office);
            office.convertIOSToChances();
            setElements();
            playButton.setDisable(false);
            exportToCSVButton.setDisable(true);
            exportHeatMapButton.setDisable(true);
            Main.officeSimulator.replenishStaticVars();
            disableEdits();
        }
    }

    public void initializeOffice(Office office) {
        OfficeGraphicsController.tileSize = backgroundCanvas.getHeight() / Main.officeSimulator.getOffice().getRows();
        mapOffice();
//        Main.officeSimulator.spawnInitialAgents(office);
        drawInterface();
    }

    public void mapOffice() {
        Office office = Main.officeSimulator.getOffice();

        List<Patch> wallPatches = new ArrayList<>();
        // Small Meeting Wall
        for (int i = 12; i < 29; i++) {
            for (int j = 1; j < 13; j++) {
                if (i < 13 && j > 6) {
                    wallPatches.add(office.getPatch(i, j));
                }
                else if (i >= 13) {
                    wallPatches.add(office.getPatch(i, j));
                }

            }
        }

        // Human Experience and Data Collection Wall
        for (int i = 41; i < 64; i++) {
            if (i < 51) {
                for (int j = 1; j < 15; j++) {
                    wallPatches.add(office.getPatch(i, j));
                }
            }
            else {
                for (int j = 1; j < 20; j++) {
                    wallPatches.add(office.getPatch(i, j));
                }
            }

        }

        // Research Space and Faculty Room Wall
        for (int i = 52; i < 66; i++) {
            for (int j = 20; j < 84; j++) {
                wallPatches.add(office.getPatch(i, j));
            }
        }

        // Large meeting room and Staff room (?) Wall
        for (int i = 56; i < 70; i++) {
            for (int j = 84; j < 117; j++) {
                    wallPatches.add(office.getPatch(i, j));
            }
        }

        // Learning Area 1, 2, 3, 4 Wall
        for (int i = 12; i < 30; i++) {
            for (int j = 19; j < 75; j++) {
                wallPatches.add(office.getPatch(i, j));
            }
        }

        // Control Center Wall
        for (int i = 12; i < 30; i++) {
            for (int j = 75; j < 86; j++) {
                if (i < 18 && j < 85) {
                    wallPatches.add(office.getPatch(i, j));
                }
                else if (i >= 18){
                    wallPatches.add(office.getPatch(i, j));
                }

            }
        }

        // Database Center Wall
        for (int i = 18; i < 30; i++) {
            for (int j = 86; j < 100; j++) {
                wallPatches.add(office.getPatch(i, j));
            }
        }

        // Open Area 1 & 2 Wall
        for (int i = 37; i < 46; i++) {
            for (int j = 23; j < 40; j++) {
                wallPatches.add(office.getPatch(i, j));
            }
        }

        // Open Area 3 & 4 Wall
        for (int i = 37; i < 46; i++) {
            for (int j = 56; j < 73; j++) {
                wallPatches.add(office.getPatch(i, j));
            }
        }

        // Management and Executive Secretary Area Wall
        for (int i = 37; i < 48; i++) {
            for (int j = 88; j < 102; j++) {
                if (j == 88 || j == 101 || i == 42) {
                    wallPatches.add(office.getPatch(i, j));
                }
            }
        }

        // Reception Wall
        for (int i = 37; i < 46; i++) {
            for (int j = 112; j < 126; j++) {
                wallPatches.add(office.getPatch(i, j));
            }
        }

        // Clinic & Executive Dean Office Wall
        for (int i = 46; i < 77; i++) {
            for (int j = 126; j < 141; j++) {
                wallPatches.add(office.getPatch(i, j));
            }
        }

        Main.officeSimulator.getOffice().getWalls().add(Wall.wallFactory.create(wallPatches, 1));

        List<Patch> outerWallPatches = new ArrayList<>();

        // Outer Walls
        for (int i = 0; i < 84; i++) {
            for (int j = 0; j < 142; j++) {
                if (i < 5) {
                    outerWallPatches.add(office.getPatch(i, j));
                }
                else if ((i >= 5 && i < 12) && (j <= 6 || j >= 19)) {
                    outerWallPatches.add(office.getPatch(i, j));
                }
                else if (i == 12 && (j <= 6 || (j >= 85 && j < 127) || j >= 137)) {
                    outerWallPatches.add(office.getPatch(i, j));
                }
                else if ((i >= 13 && i < 17) && (j == 0 || (j >= 85 && j < 127) || j >= 137)) {
                    outerWallPatches.add(office.getPatch(i, j));
                }
                else if (i == 17 && (j == 0 || (j >= 85 && j < 118) || j >= 137)) {
                    outerWallPatches.add(office.getPatch(i, j));
                }
                else if ((i >= 18 && i < 29) && (j == 0 || (j >= 110 && j < 118) || j >= 137)) {
                    outerWallPatches.add(office.getPatch(i, j));
                }
                else if (i == 29 && (j == 0 || (j >= 110 && j < 127) || j >= 137)) {
                    outerWallPatches.add(office.getPatch(i, j));
                }
                else if ((i >= 30 && i < 34) && (j == 0 || j == 126 || j >= 137)) {
                    outerWallPatches.add(office.getPatch(i, j));
                }
                else if ((i >= 34 && i < 46) && (j == 0 || j >= 126)) {
                    outerWallPatches.add(office.getPatch(i, j));
                }
                else if ((i >= 46 && i < 64) && (j == 0 || j == 141)) {
                    outerWallPatches.add(office.getPatch(i, j));
                }
                else if (i == 64 && (j < 20 || j == 141)) {
                    outerWallPatches.add(office.getPatch(i, j));
                }
                else if (i == 65 && (j == 19 || j == 141)) {
                    outerWallPatches.add(office.getPatch(i, j));
                }
                else if (i == 66 && ((j >= 19 && j <= 83) || j == 141)) {
                    outerWallPatches.add(office.getPatch(i, j));
                }
                else if ((i >= 67 && i < 69) && (j == 83 || j == 141)) {
                    outerWallPatches.add(office.getPatch(i, j));
                }
                else if (i == 69 && ((j >= 80 && j <= 83) || j == 141)) {
                    outerWallPatches.add(office.getPatch(i, j));
                }
                else if ((i >= 70 && i < 77) && (j == 80|| j == 141)) {
                    outerWallPatches.add(office.getPatch(i, j));
                }
                else if (i == 77 && (j == 80 || j >= 126)) {
                    outerWallPatches.add(office.getPatch(i, j));
                }
                else if ((i >= 78 && i < 83) && (j == 80|| j == 126)) {
                    outerWallPatches.add(office.getPatch(i, j));
                }
                else if (i == 83 && (j >= 80 && j <= 126)) {
                    outerWallPatches.add(office.getPatch(i, j));
                }
//                else if ((i == 64 || i == 65) && (j < 20 || j == 141)) {
//                    outerWallPatches.add(office.getPatch(i, j));
//                }
//                else if ((i >= 66 && i < 70) && (j < 84 || j == 141)) {
//                    outerWallPatches.add(office.getPatch(i, j));
//                }
//                else if ((i >= 70 && i < 77) && (j < 81|| j == 141)) {
//                    outerWallPatches.add(office.getPatch(i, j));
//                }
//                else if ((i >= 77 && i < 83) && (j < 81|| j >= 126)) {
//                    outerWallPatches.add(office.getPatch(i, j));
//                }
//                else if (i == 83) {
//                    outerWallPatches.add(office.getPatch(i, j));
//                }
            }
        }

        Main.officeSimulator.getOffice().getWalls().add(Wall.wallFactory.create(outerWallPatches, 2));

        // Small Meeting Room
        List<Patch> meetingRoom1Patches = new ArrayList<>();
        for (int i = 13; i < 28; i++) {
            for (int j = 1; j < 12; j++) {
                meetingRoom1Patches.add(office.getPatch(i, j));
            }
        }
        Main.officeSimulator.getOffice().getMeetingRooms().add(MeetingRoom.meetingRoomFactory.create(meetingRoom1Patches, 1));

        // Human Experience Room
        List<Patch> humanExperienceRoomPatches = new ArrayList<>();
        for (int i = 42; i < 51; i++) {
            for (int j = 1; j < 14; j++) {
                humanExperienceRoomPatches.add(office.getPatch(i, j));
            }
        }
        Main.officeSimulator.getOffice().getLaboratories().add(Laboratory.laboratoryFactory.create(humanExperienceRoomPatches, 1));

        // Data Collection Room
        List<Patch> dataCollectionRoomPatches = new ArrayList<>();
        for (int i = 52; i < 64; i++) {
            for (int j = 1; j < 19; j++) {
                dataCollectionRoomPatches.add(office.getPatch(i, j));
            }
        }
        Main.officeSimulator.getOffice().getLaboratories().add(Laboratory.laboratoryFactory.create(dataCollectionRoomPatches, 2));

        // Research Area Room
        List<Patch> researchAreaPatches = new ArrayList<>();
        for (int i = 53; i < 66; i++) {
            for (int j = 20; j < 67; j++) {
                researchAreaPatches.add(office.getPatch(i, j));
            }
        }
        Main.officeSimulator.getOffice().getStudyAreas().add(StudyArea.studyAreaFactory.create(researchAreaPatches, 5));

        // Faculty Room
        List<Patch> officeRoomPatches = new ArrayList<>();
        for (int i = 53; i < 66; i++) {
            for (int j = 68; j < 83; j++) {
                officeRoomPatches.add(office.getPatch(i, j));
            }
        }
        Main.officeSimulator.getOffice().getOfficeRooms().add(OfficeRoom.officeRoomFactory.create(officeRoomPatches, 1));

        // Staff Room(?)
        List<Patch> staffRoomAreaPatches = new ArrayList<>();
        for (int i = 57; i < 69; i++) {
            for (int j = 84; j < 94; j++) {
                staffRoomAreaPatches.add(office.getPatch(i, j));
            }
        }
        Main.officeSimulator.getOffice().getStaffOffices().add(StaffOffice.staffOfficeFactory.create(staffRoomAreaPatches, 4));

        // Large Meeting Room
        List<Patch> meetingRoom2Patches = new ArrayList<>();
        for (int i = 57; i < 69; i++) {
            for (int j = 95; j < 116; j++) {
                meetingRoom2Patches.add(office.getPatch(i, j));
            }
        }
        Main.officeSimulator.getOffice().getMeetingRooms().add(MeetingRoom.meetingRoomFactory.create(meetingRoom2Patches, 2));

        // Pantry Area
        List<Patch> pantryPatches = new ArrayList<>();
        for (int i = 70; i < 83; i++) {
            for (int j = 81; j < 126; j++) {
                pantryPatches.add(office.getPatch(i, j));
            }
        }
        Main.officeSimulator.getOffice().getCafeterias().add(Cafeteria.cafeteriaFactory.create(pantryPatches, 1));

        // Learning Area 1 (Leftmost)
        List<Patch> learningAreaPatches1 = new ArrayList<>();
        for (int i = 12; i < 29; i++) {
            for (int j = 20; j < 33; j++) {
                learningAreaPatches1.add(office.getPatch(i, j));
            }
        }
        Main.officeSimulator.getOffice().getStudyAreas().add(StudyArea.studyAreaFactory.create(learningAreaPatches1, 1));

        // Learning Area 2
        List<Patch> learningAreaPatches2 = new ArrayList<>();
        for (int i = 12; i < 29; i++) {
            for (int j = 34; j < 47; j++) {
                learningAreaPatches2.add(office.getPatch(i, j));
            }
        }
        Main.officeSimulator.getOffice().getStudyAreas().add(StudyArea.studyAreaFactory.create(learningAreaPatches2, 2));

        // Learning Area 3
        List<Patch> learningAreaPatches3 = new ArrayList<>();
        for (int i = 12; i < 29; i++) {
            for (int j = 48; j < 61; j++) {
                learningAreaPatches3.add(office.getPatch(i, j));
            }
        }
        Main.officeSimulator.getOffice().getStudyAreas().add(StudyArea.studyAreaFactory.create(learningAreaPatches3, 3));

        // Learning Area 4 (Rightmost)
        List<Patch> learningAreaPatches4 = new ArrayList<>();
        for (int i = 12; i < 29; i++) {
            for (int j = 62; j < 75; j++) {
                learningAreaPatches4.add(office.getPatch(i, j));
            }
        }
        Main.officeSimulator.getOffice().getStudyAreas().add(StudyArea.studyAreaFactory.create(learningAreaPatches4, 4));

        // Control Center (Up/Back)
        List<Patch> controlCenterAreaPatches1 = new ArrayList<>();
        for (int i = 12; i < 19; i++) {
            for (int j = 76; j < 85; j++) {
                controlCenterAreaPatches1.add(office.getPatch(i, j));
            }
        }
        Main.officeSimulator.getOffice().getStaffOffices().add(StaffOffice.staffOfficeFactory.create(controlCenterAreaPatches1, 1));

        // Control Center (Down/Front)
        List<Patch> controlCenterAreaPatches2 = new ArrayList<>();
        for (int i = 20; i < 29; i++) {
            for (int j = 76; j < 85; j++) {
                controlCenterAreaPatches2.add(office.getPatch(i, j));
            }
        }
        Main.officeSimulator.getOffice().getStaffOffices().add(StaffOffice.staffOfficeFactory.create(controlCenterAreaPatches2, 2));

        // Database Control Center
        List<Patch> databaseControlAreaPatches = new ArrayList<>();
        for (int i = 18; i < 29; i++) {
            for (int j = 86; j < 99; j++) {
                databaseControlAreaPatches.add(office.getPatch(i, j));
            }
        }
        Main.officeSimulator.getOffice().getStaffOffices().add(StaffOffice.staffOfficeFactory.create(databaseControlAreaPatches, 3));

        // Open Area Study Area 1 (Leftmost)
        List<Patch> openAreaPatches1 = new ArrayList<>();
        for (int i = 38; i < 45; i++) {
            for (int j = 24; j < 31; j++) {
                    openAreaPatches1.add(office.getPatch(i, j));
            }
        }
        Main.officeSimulator.getOffice().getStudyAreas().add(StudyArea.studyAreaFactory.create(openAreaPatches1, 6));

        // Open Area Study Area 2
        List<Patch> openAreaPatches2 = new ArrayList<>();
        for (int i = 38; i < 45; i++) {
            for (int j = 32; j < 39; j++) {
                openAreaPatches2.add(office.getPatch(i, j));
            }
        }
        Main.officeSimulator.getOffice().getStudyAreas().add(StudyArea.studyAreaFactory.create(openAreaPatches2, 7));

        // Open Area Study Area 3
        List<Patch> openAreaPatches3 = new ArrayList<>();
        for (int i = 38; i < 45; i++) {
            for (int j = 57; j < 64; j++) {
                openAreaPatches3.add(office.getPatch(i, j));
            }
        }
        Main.officeSimulator.getOffice().getStudyAreas().add(StudyArea.studyAreaFactory.create(openAreaPatches3, 8));

        // Open Area Study Area 4 (Rightmost)
        List<Patch> openAreaPatches4 = new ArrayList<>();
        for (int i = 38; i < 45; i++) {
            for (int j = 65; j < 72; j++) {
                openAreaPatches4.add(office.getPatch(i, j));
            }
        }
        Main.officeSimulator.getOffice().getStudyAreas().add(StudyArea.studyAreaFactory.create(openAreaPatches4, 9));

        // Management and Executive Secretary Area 1
        List<Patch> managementAndExecSecretaryAreaPatches1 = new ArrayList<>();
        for (int i = 37; i < 42; i++) {
            for (int j = 89; j < 101; j++) {
                    managementAndExecSecretaryAreaPatches1.add(office.getPatch(i, j));
            }
        }
        Main.officeSimulator.getOffice().getBreakrooms().add(Breakroom.breakroomFactory.create(managementAndExecSecretaryAreaPatches1, 1));

        // Management and Executive Secretary Area 2
        List<Patch> managementAndExecSecretaryAreaPatches2 = new ArrayList<>();
        for (int i = 43; i < 48; i++) {
            for (int j = 89; j < 101; j++) {
                    managementAndExecSecretaryAreaPatches2.add(office.getPatch(i, j));
            }
        }
        Main.officeSimulator.getOffice().getBreakrooms().add(Breakroom.breakroomFactory.create(managementAndExecSecretaryAreaPatches2, 2));

        // Men's Bathroom
        List<Patch> mBathroomPatches = new ArrayList<>();
        for (int i = 1; i < 11; i++) {
            for (int j = 127; j < 141; j++) {
                mBathroomPatches.add(office.getPatch(i, j));
            }
        }
        Main.officeSimulator.getOffice().getBathrooms().add(Bathroom.bathroomFactory.create(mBathroomPatches, 2));

        // Women's Bathroom
        List<Patch> fBathroomPatches = new ArrayList<>();
        for (int i = 35; i < 45; i++) {
            for (int j = 127; j < 141; j++) {
                fBathroomPatches.add(office.getPatch(i, j));
            }
        }
        Main.officeSimulator.getOffice().getBathrooms().add(Bathroom.bathroomFactory.create(fBathroomPatches, 1));

        // Reception Area
        List<Patch> receptionPatches = new ArrayList<>();
        for (int i = 30; i < 45; i++) {
            for (int j = 113; j < 126; j++) {
                receptionPatches.add(office.getPatch(i, j));
            }
        }
        Main.officeSimulator.getOffice().getReceptions().add(Reception.receptionFactory.create(receptionPatches, 1));

        // Clinic (Front/Left)
        List<Patch> breakroomPatches1 = new ArrayList<>();
        for (int i = 46; i < 56; i++) {
            for (int j = 127; j < 132; j++) {
                breakroomPatches1.add(office.getPatch(i, j));
            }
        }
        Main.officeSimulator.getOffice().getBreakrooms().add(Breakroom.breakroomFactory.create(breakroomPatches1, 3));

        // Clinic (Back/Right)
        List<Patch> breakroomPatches2 = new ArrayList<>();
        for (int i = 46; i < 56; i++) {
            for (int j = 133; j < 141; j++) {
                breakroomPatches2.add(office.getPatch(i, j));
            }
        }
        Main.officeSimulator.getOffice().getBreakrooms().add(Breakroom.breakroomFactory.create(breakroomPatches2, 4));

        // Executive Dean's Office
        List<Patch> execDeanOfficeAreaPatches = new ArrayList<>();
        for (int i = 57; i < 77; i++) {
            for (int j = 127; j < 141; j++) {
                execDeanOfficeAreaPatches.add(office.getPatch(i, j));
            }
        }
        Main.officeSimulator.getOffice().getOfficeRooms().add(OfficeRoom.officeRoomFactory.create(execDeanOfficeAreaPatches, 2));

        List<Patch> cabinetDownPatches = new ArrayList<>();
        // Pantry
        cabinetDownPatches.add(office.getPatch(70,81));
        cabinetDownPatches.add(office.getPatch(70,86));
        cabinetDownPatches.add(office.getPatch(70,88));
        CabinetMapper.draw(cabinetDownPatches, "DOWN");

        List<Patch> cabinetUpPatches = new ArrayList<>();
        cabinetUpPatches.add(office.getPatch(64,20));
        cabinetUpPatches.add(office.getPatch(64,22));
        cabinetUpPatches.add(office.getPatch(64,24));
        cabinetUpPatches.add(office.getPatch(64,26));
//        cabinetUpPatches.add(office.getPatch(58,50));
//        cabinetUpPatches.add(office.getPatch(58,53));
        CabinetMapper.draw(cabinetUpPatches, "UP");

        List<Patch> chairPatches = new ArrayList<>();

        // Reception Chair
        chairPatches.add(office.getPatch(40,120));
        // Large Meeting Chair
        for (int j = 101; j < 110; j+=2) {
            chairPatches.add(office.getPatch(60,j));
            chairPatches.add(office.getPatch(65,j));
        }
        chairPatches.add(office.getPatch(62,99));
        chairPatches.add(office.getPatch(62,110));
        // Small Meeting Chair
        for (int i = 17; i < 24; i+=2) {
            chairPatches.add(office.getPatch(i,5));
            chairPatches.add(office.getPatch(i,8));
        }
        chairPatches.add(office.getPatch(16,6));
        chairPatches.add(office.getPatch(25,6));
        ChairMapper.draw(chairPatches);

//        List<Patch> collabDeskPatches = new ArrayList<>();
//        collabDeskPatches.add(office.getPatch(8,64));
//        collabDeskPatches.add(office.getPatch(15,64));
//        collabDeskPatches.add(office.getPatch(22,64));
//        collabDeskPatches.add(office.getPatch(29,64));
//        collabDeskPatches.add(office.getPatch(36,64));
//        CollabDeskMapper.draw(collabDeskPatches);

//        List<Patch> couchDownPatches = new ArrayList<>();
//        couchDownPatches.add(office.getPatch(22,7));
//        couchDownPatches.add(office.getPatch(47,7));
//        CouchMapper.draw(couchDownPatches, "DOWN");
//
        List<Patch> couchRightPatches = new ArrayList<>();
        couchRightPatches.add(office.getPatch(39,86));
        CouchMapper.draw(couchRightPatches, "RIGHT");

        List<Patch> cubicleUpPatches = new ArrayList<>();
        // Faculty Cubicle
        cubicleUpPatches.add(office.getPatch(60,81));
        cubicleUpPatches.add(office.getPatch(60,77));
        cubicleUpPatches.add(office.getPatch(60,75));
        cubicleUpPatches.add(office.getPatch(60,71));
        cubicleUpPatches.add(office.getPatch(60,69));

        // Open Area 1 & 3 Cubicle
        cubicleUpPatches.add(office.getPatch(60,71));
        cubicleUpPatches.add(office.getPatch(60,69));
        CubicleMapper.draw(cubicleUpPatches, "UP");

        List<Patch> cubicleDownPatches = new ArrayList<>();
        // Faculty Cubicle
        cubicleDownPatches.add(office.getPatch(58,81));
        cubicleDownPatches.add(office.getPatch(58,77));
        cubicleDownPatches.add(office.getPatch(58,75));
        cubicleDownPatches.add(office.getPatch(58,71));
        cubicleDownPatches.add(office.getPatch(58,69));

        // Open Area 2 & 4 Cubicle
        cubicleDownPatches.add(office.getPatch(58,71));
        cubicleDownPatches.add(office.getPatch(58,69));
        CubicleMapper.draw(cubicleDownPatches, "DOWN");

        List<Patch> whiteboardPatches = new ArrayList<>();
        // Small Meeting Whiteboard
        whiteboardPatches.add(office.getPatch(18,1));
        whiteboardPatches.add(office.getPatch(20,1));
        whiteboardPatches.add(office.getPatch(22,1));
        // Large Meeting Whiteboard
        whiteboardPatches.add(office.getPatch(60,95));
        whiteboardPatches.add(office.getPatch(62,95));
        whiteboardPatches.add(office.getPatch(64,95));
        WhiteboardMapper.draw(whiteboardPatches);

        List<Patch> doorRightPatches = new ArrayList<>();
        // Small Meeting Room Door
        doorRightPatches.add(office.getPatch(13,12));
        doorRightPatches.add(office.getPatch(24,12));
        // Human Experience Room Door
        doorRightPatches.add(office.getPatch(42,14));
        // Bet. Research and Faculty Room Door
        doorRightPatches.add(office.getPatch(53,67));
        DoorMapper.draw(doorRightPatches, "RIGHT");

        List<Patch> doorLeftPatches = new ArrayList<>();
        // Clinic Door (Left/Front)
        doorLeftPatches.add(office.getPatch(47,126));
        // Executive Room Door
        doorLeftPatches.add(office.getPatch(58,126));
        DoorMapper.draw(doorLeftPatches, "LEFT");

        List<Patch> doorUpPatches = new ArrayList<>();
        // Main Entrance Door
        doorUpPatches.add(office.getPatch(29,122));
        // Women's Bathroom Door
        doorUpPatches.add(office.getPatch(34,127));
        // Staff Room Door (?)
        doorUpPatches.add(office.getPatch(56,87));
        // Large Meeting Room Doors
        doorUpPatches.add(office.getPatch(56,95));
        doorUpPatches.add(office.getPatch(56,112));
        // Research Area Room Door
        doorUpPatches.add(office.getPatch(52,20));
        // Faculty Room Door
        doorUpPatches.add(office.getPatch(52,79));
        // Data Collection Room Door
        doorUpPatches.add(office.getPatch(51,15));
        // Bet Human Experience & Data Collection Room Door
        doorUpPatches.add(office.getPatch(51,10));
        // Clinic Door (Right/Back)
        doorUpPatches.add(office.getPatch(56,136));
        // Open Area 2 Door
        doorUpPatches.add(office.getPatch(37,35));
        // Open Area 4 Door (RightMost)
        doorUpPatches.add(office.getPatch(37,68));
        DoorMapper.draw(doorUpPatches, "UP");

        List<Patch> doorDownPatches = new ArrayList<>();
        // Learning Area 1 Door (Leftmost)
        doorDownPatches.add(office.getPatch(29,29));
        // Learning Area 2 Door
        doorDownPatches.add(office.getPatch(29,43));
        // Learning Area 3 Door
        doorDownPatches.add(office.getPatch(29,57));
        // Learning Area 4 Door (RightMost)
        doorDownPatches.add(office.getPatch(29,71));
        // Control Center Door (Front/Down)
        doorDownPatches.add(office.getPatch(29,81));
        // Control Center Door (Back/Up)
        doorDownPatches.add(office.getPatch(19,76));
        // Database Center Door
        doorDownPatches.add(office.getPatch(29,95));
        // Men's Bathroom Door
        doorDownPatches.add(office.getPatch(11,127));
        // Open Area 1 Door (LeftMost)
        doorDownPatches.add(office.getPatch(45,24));
        // Open Area 3 Door
        doorDownPatches.add(office.getPatch(45,57));

        DoorMapper.draw(doorDownPatches, "DOWN");

        List<Patch> meetingDeskHorizontalPatches = new ArrayList<>();
        meetingDeskHorizontalPatches.add(office.getPatch(61,100));
        meetingDeskHorizontalPatches.add(office.getPatch(63,100));
        MeetingDeskMapper.draw(meetingDeskHorizontalPatches, "HORIZONTAL", 5);

        List<Patch> meetingDeskVerticalPatches = new ArrayList<>();
        meetingDeskVerticalPatches.add(office.getPatch(17,6));
        MeetingDeskMapper.draw(meetingDeskVerticalPatches, "VERTICAL", 5);

//        List<Patch> officeDeskPatches = new ArrayList<>();
//        officeDeskPatches.add(office.getPatch(52,44));
//        officeDeskPatches.add(office.getPatch(50,63));
//        OfficeDeskMapper.draw(officeDeskPatches);

        // Office Elevator (Spawn and Despawn point)
        List<Patch> officeGateEntranceExitPatches = new ArrayList<>();
        officeGateEntranceExitPatches.add(office.getPatch(13,137));
        officeGateEntranceExitPatches.add(office.getPatch(21,137));
        officeGateEntranceExitPatches.add(office.getPatch(29,137));
        OfficeGateMapper.draw(officeGateEntranceExitPatches, OfficeGate.OfficeGateMode.ENTRANCE_AND_EXIT);

//        List<Patch> plantPatches = new ArrayList<>();
//        plantPatches.add(office.getPatch(0,20));
//        plantPatches.add(office.getPatch(44,37));
//        plantPatches.add(office.getPatch(44,42));
//        plantPatches.add(office.getPatch(0,79));
//        plantPatches.add(office.getPatch(44,68));
//        plantPatches.add(office.getPatch(20,79));
//        plantPatches.add(office.getPatch(21,79));
//        plantPatches.add(office.getPatch(38,79));
//        plantPatches.add(office.getPatch(39,79));
//        PlantMapper.draw(plantPatches);

//        List<Patch> printerPatches = new ArrayList<>();
//        printerPatches.add(office.getPatch(44,39));
//        PrinterMapper.draw(printerPatches);

        List<Patch> receptionTablePatches = new ArrayList<>();
        // Reception Table
        receptionTablePatches.add(office.getPatch(39,118));
        ReceptionTableMapper.draw(receptionTablePatches);

//        List<Patch> tableUpPatches = new ArrayList<>();
//        tableUpPatches.add(office.getPatch(28,5));
//        tableUpPatches.add(office.getPatch(28,9));
//        tableUpPatches.add(office.getPatch(28,13));
//        tableUpPatches.add(office.getPatch(31,5));
//        tableUpPatches.add(office.getPatch(31,9));
//        tableUpPatches.add(office.getPatch(31,13));
//        tableUpPatches.add(office.getPatch(51,9));
//        tableUpPatches.add(office.getPatch(52,9));
//        tableUpPatches.add(office.getPatch(53,9));
//        tableUpPatches.add(office.getPatch(54,9));
//        tableUpPatches.add(office.getPatch(51,11));
//        tableUpPatches.add(office.getPatch(52,11));
//        tableUpPatches.add(office.getPatch(53,11));
//        tableUpPatches.add(office.getPatch(54,11));
//        TableMapper.draw(tableUpPatches, "UP");

//        List<Patch> tableRightPatches = new ArrayList<>();
//        tableRightPatches.add(office.getPatch(36,2));
//        tableRightPatches.add(office.getPatch(36,6));
//        tableRightPatches.add(office.getPatch(36,10));
//        tableRightPatches.add(office.getPatch(36,14));
//        tableRightPatches.add(office.getPatch(40,2));
//        tableRightPatches.add(office.getPatch(40,6));
//        tableRightPatches.add(office.getPatch(40,10));
//        tableRightPatches.add(office.getPatch(40,14));
//        TableMapper.draw(tableRightPatches, "RIGHT");

        // TRASH
        List<Patch> trashPatches = new ArrayList<>();
        trashPatches.add(office.getPatch(36,16));
        trashPatches.add(office.getPatch(27,0));
        trashPatches.add(office.getPatch(29,0));
        trashPatches.add(office.getPatch(31,0));
        trashPatches.add(office.getPatch(18,69));
        trashPatches.add(office.getPatch(20,69));
        trashPatches.add(office.getPatch(22,69));
        trashPatches.add(office.getPatch(26,95));
        trashPatches.add(office.getPatch(33,47));
        trashPatches.add(office.getPatch(33,49));
        trashPatches.add(office.getPatch(33,51));
        TrashMapper.draw(trashPatches);

        // EAT TABLE
        List<Patch> eatTablePatches = new ArrayList<>();
        for (int i = 6; i < 24; i++) {
            if (i == 7 || i == 11 || i == 15 || i == 19 || i == 23) {
                for (int j = 97; j < 117; j++) {
                    if (j == 97 || j == 103 || j == 109 || j == 115) {
                        eatTablePatches.add(office.getPatch(i, j));
                    }
                }
            }
        }
        EatTableMapper.draw(eatTablePatches);

        List<Patch> studyTablePatches = new ArrayList<>();
        studyTablePatches.add(office.getPatch(37, 101));
        studyTablePatches.add(office.getPatch(41, 101));
        studyTablePatches.add(office.getPatch(45, 101));
        studyTablePatches.add(office.getPatch(49, 101));
        studyTablePatches.add(office.getPatch(53, 101));
        studyTablePatches.add(office.getPatch(37, 105));
        studyTablePatches.add(office.getPatch(41, 105));
        studyTablePatches.add(office.getPatch(45, 105));
        studyTablePatches.add(office.getPatch(49, 105));
        studyTablePatches.add(office.getPatch(53, 105));
        studyTablePatches.add(office.getPatch(37, 111));
        studyTablePatches.add(office.getPatch(41, 111));
        studyTablePatches.add(office.getPatch(45, 111));
        studyTablePatches.add(office.getPatch(49, 111));
        studyTablePatches.add(office.getPatch(53, 111));
        studyTablePatches.add(office.getPatch(37, 115));
        studyTablePatches.add(office.getPatch(41, 115));
        studyTablePatches.add(office.getPatch(45, 115));
        studyTablePatches.add(office.getPatch(49, 115));
        studyTablePatches.add(office.getPatch(53, 115));
        StudyTableMapper.draw(studyTablePatches, "UP");

        List<Patch> toiletPatches = new ArrayList<>();
        for (int j = 133; j < 140; j += 2) {
            // Men's Bathroom Toilet
            toiletPatches.add(office.getPatch(1,j));
            // Women's Bathroom Toilet
            toiletPatches.add(office.getPatch(44,j));
        }
        ToiletMapper.draw(toiletPatches);

        List<Patch> sinkPatches = new ArrayList<>();
        for (int j = 133; j < 140; j += 2) {
            // Men's Bathroom Sink
            sinkPatches.add(office.getPatch(10,j));

            // Women's Bathroom Sink
            sinkPatches.add(office.getPatch(35,j));
        }

        // Pantry Sink
        sinkPatches.add(office.getPatch(70,84));
        SinkMapper.draw(sinkPatches);

        List<Patch> fridgePatches = new ArrayList<>();
        fridgePatches.add(office.getPatch(70,93));
        FridgeMapper.draw(fridgePatches);

        List<Patch> waterDispenserPatches = new ArrayList<>();
        waterDispenserPatches.add(office.getPatch(70,91));
        WaterDispenserMapper.draw(waterDispenserPatches);

//        List<Patch> securityPatches = new ArrayList<>();
//        securityPatches.add(office.getPatch(56,26));
//        SecurityMapper.draw(securityPatches);
    }

    private void drawInterface() {
        drawOfficeViewBackground(Main.officeSimulator.getOffice());
        drawOfficeViewForeground(Main.officeSimulator.getOffice(), false);
    }

    public void drawOfficeViewBackground(Office office) {
        OfficeGraphicsController.requestDrawOfficeView(stackPane, office, OfficeGraphicsController.tileSize, true, false);
    }

    public void drawOfficeViewForeground(Office office, boolean speedAware) {
        OfficeGraphicsController.requestDrawOfficeView(stackPane, office, OfficeGraphicsController.tileSize, false, speedAware);
        requestUpdateInterfaceSimulationElements();
    }

    public void exportHeatMap() {
        try {
            OfficeSimulator.exportHeatMap();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void requestUpdateInterfaceSimulationElements() {
        Platform.runLater(this::updateSimulationTime);
        Platform.runLater(this::updateStatistics);
    }

    public void updateSimulationTime() {
        LocalTime currentTime = Main.officeSimulator.getSimulationTime().getTime();
        long elapsedTime = Main.officeSimulator.getSimulationTime().getStartTime().until(currentTime, ChronoUnit.SECONDS) / 5;
        String timeString;
        timeString = String.format("%02d", currentTime.getHour()) + ":" + String.format("%02d", currentTime.getMinute()) + ":" + String.format("%02d", currentTime.getSecond());
        elapsedTimeText.setText("Current time: " + timeString + " (" + elapsedTime + " ticks)");
    }
    public void updateStatistics() {
        currentManagerCount.setText(String.valueOf(OfficeSimulator.currentManagerCount));
        currentBusinessCount.setText(String.valueOf(OfficeSimulator.currentBusinessCount));
        currentResearchCount.setText(String.valueOf(OfficeSimulator.currentResearchCount));
        currentTechnicalCount.setText(String.valueOf(OfficeSimulator.currentTechnicalCount));
        currentSecretaryCount.setText(String.valueOf(OfficeSimulator.currentSecretaryCount));
        currentClientCount.setText(String.valueOf(OfficeSimulator.currentClientCount));
        currentDriverCount.setText(String.valueOf(OfficeSimulator.currentDriverCount));
        currentVisitorCount.setText(String.valueOf(OfficeSimulator.currentVisitorCount));
        currentNonverbalCount.setText(String.valueOf(OfficeSimulator.currentNonverbalCount));
        currentCooperativeCount.setText(String.valueOf(OfficeSimulator.currentCooperativeCount));
        currentExchangeCount.setText(String.valueOf(OfficeSimulator.currentExchangeCount));
        averageNonverbalDuration.setText(String.format("%.02f", OfficeSimulator.averageNonverbalDuration));
        averageCooperativeDuration.setText(String.format("%.02f", OfficeSimulator.averageCooperativeDuration));
        averageExchangeDuration.setText(String.format("%.02f", OfficeSimulator.averageExchangeDuration));
        currentTeam1Count.setText(String.valueOf(OfficeSimulator.currentTeam1Count));
        currentTeam2Count.setText(String.valueOf(OfficeSimulator.currentTeam2Count));
        currentTeam3Count.setText(String.valueOf(OfficeSimulator.currentTeam3Count));
        currentTeam4Count.setText(String.valueOf(OfficeSimulator.currentTeam4Count));
        currentBossManagerCount.setText(String.valueOf(OfficeSimulator.currentBossManagerCount));
        currentBossBusinessCount.setText(String.valueOf(OfficeSimulator.currentBossBusinessCount));
        currentBossResearcherCount.setText(String.valueOf(OfficeSimulator.currentBossResearcherCount));
        currentBossTechnicalCount.setText(String.valueOf(OfficeSimulator.currentBossTechnicalCount));
        currentBossJanitorCount.setText(String.valueOf(OfficeSimulator.currentBossJanitorCount));
        currentBossClientCount.setText(String.valueOf(OfficeSimulator.currentBossClientCount));
        currentBossDriverCount.setText(String.valueOf(OfficeSimulator.currentBossDriverCount));
        currentBossVisitorCount.setText(String.valueOf(OfficeSimulator.currentBossVisitorCount));
        currentBossGuardCount.setText(String.valueOf(OfficeSimulator.currentBossGuardCount));
        currentBossReceptionistCount.setText(String.valueOf(OfficeSimulator.currentBossReceptionistCount));
        currentBossSecretaryCount.setText(String.valueOf(OfficeSimulator.currentBossSecretaryCount));
        currentManagerManagerCount.setText(String.valueOf(OfficeSimulator.currentManagerManagerCount));
        currentManagerBusinessCount.setText(String.valueOf(OfficeSimulator.currentManagerBusinessCount));
        currentManagerResearcherCount.setText(String.valueOf(OfficeSimulator.currentManagerResearcherCount));
        currentManagerTechnicalCount.setText(String.valueOf(OfficeSimulator.currentManagerTechnicalCount));
        currentManagerJanitorCount.setText(String.valueOf(OfficeSimulator.currentManagerJanitorCount));
        currentManagerClientCount.setText(String.valueOf(OfficeSimulator.currentManagerClientCount));
        currentManagerDriverCount.setText(String.valueOf(OfficeSimulator.currentManagerDriverCount));
        currentManagerVisitorCount.setText(String.valueOf(OfficeSimulator.currentManagerVisitorCount));
        currentManagerGuardCount.setText(String.valueOf(OfficeSimulator.currentManagerGuardCount));
        currentManagerReceptionistCount.setText(String.valueOf(OfficeSimulator.currentManagerReceptionistCount));
        currentManagerSecretaryCount.setText(String.valueOf(OfficeSimulator.currentManagerSecretaryCount));
        currentBusinessBusinessCount.setText(String.valueOf(OfficeSimulator.currentBusinessBusinessCount));
        currentBusinessResearcherCount.setText(String.valueOf(OfficeSimulator.currentBusinessResearcherCount));
        currentBusinessTechnicalCount.setText(String.valueOf(OfficeSimulator.currentBusinessTechnicalCount));
        currentBusinessJanitorCount.setText(String.valueOf(OfficeSimulator.currentBusinessJanitorCount));
        currentBusinessClientCount.setText(String.valueOf(OfficeSimulator.currentBusinessClientCount));
        currentBusinessDriverCount.setText(String.valueOf(OfficeSimulator.currentBusinessDriverCount));
        currentBusinessVisitorCount.setText(String.valueOf(OfficeSimulator.currentBusinessVisitorCount));
        currentBusinessGuardCount.setText(String.valueOf(OfficeSimulator.currentBusinessGuardCount));
        currentBusinessReceptionistCount.setText(String.valueOf(OfficeSimulator.currentBusinessReceptionistCount));
        currentBusinessSecretaryCount.setText(String.valueOf(OfficeSimulator.currentBusinessSecretaryCount));
        currentResearcherResearcherCount.setText(String.valueOf(OfficeSimulator.currentResearcherResearcherCount));
        currentResearcherTechnicalCount.setText(String.valueOf(OfficeSimulator.currentResearcherTechnicalCount));
        currentResearcherJanitorCount.setText(String.valueOf(OfficeSimulator.currentResearcherJanitorCount));
        currentResearcherClientCount.setText(String.valueOf(OfficeSimulator.currentResearcherClientCount));
        currentResearcherDriverCount.setText(String.valueOf(OfficeSimulator.currentResearcherDriverCount));
        currentResearcherVisitorCount.setText(String.valueOf(OfficeSimulator.currentResearcherVisitorCount));
        currentResearcherGuardCount.setText(String.valueOf(OfficeSimulator.currentResearcherGuardCount));
        currentResearcherReceptionistCount.setText(String.valueOf(OfficeSimulator.currentResearcherReceptionistCount));
        currentResearcherSecretaryCount.setText(String.valueOf(OfficeSimulator.currentResearcherSecretaryCount));
        currentTechnicalTechnicalCount.setText(String.valueOf(OfficeSimulator.currentTechnicalTechnicalCount));
        currentTechnicalJanitorCount.setText(String.valueOf(OfficeSimulator.currentTechnicalJanitorCount));
        currentTechnicalClientCount.setText(String.valueOf(OfficeSimulator.currentTechnicalClientCount));
        currentTechnicalDriverCount.setText(String.valueOf(OfficeSimulator.currentTechnicalDriverCount));
        currentTechnicalVisitorCount.setText(String.valueOf(OfficeSimulator.currentTechnicalVisitorCount));
        currentTechnicalGuardCount.setText(String.valueOf(OfficeSimulator.currentTechnicalGuardCount));
        currentTechnicalReceptionistCount.setText(String.valueOf(OfficeSimulator.currentTechnicalReceptionistCount));
        currentTechnicalSecretaryCount.setText(String.valueOf(OfficeSimulator.currentTechnicalSecretaryCount));
        currentJanitorJanitorCount.setText(String.valueOf(OfficeSimulator.currentJanitorJanitorCount));
        currentJanitorClientCount.setText(String.valueOf(OfficeSimulator.currentJanitorClientCount));
        currentJanitorDriverCount.setText(String.valueOf(OfficeSimulator.currentJanitorDriverCount));
        currentJanitorVisitorCount.setText(String.valueOf(OfficeSimulator.currentJanitorVisitorCount));
        currentJanitorSecretaryCount.setText(String.valueOf(OfficeSimulator.currentJanitorSecretaryCount));
        currentClientClientCount.setText(String.valueOf(OfficeSimulator.currentClientClientCount));
        currentClientDriverCount.setText(String.valueOf(OfficeSimulator.currentClientDriverCount));
        currentClientVisitorCount.setText(String.valueOf(OfficeSimulator.currentClientVisitorCount));
        currentClientGuardCount.setText(String.valueOf(OfficeSimulator.currentClientGuardCount));
        currentClientReceptionistCount.setText(String.valueOf(OfficeSimulator.currentClientReceptionistCount));
        currentClientSecretaryCount.setText(String.valueOf(OfficeSimulator.currentClientSecretaryCount));
        currentDriverDriverCount.setText(String.valueOf(OfficeSimulator.currentDriverDriverCount));
        currentDriverVisitorCount.setText(String.valueOf(OfficeSimulator.currentDriverVisitorCount));
        currentDriverGuardCount.setText(String.valueOf(OfficeSimulator.currentDriverGuardCount));
        currentDriverReceptionistCount.setText(String.valueOf(OfficeSimulator.currentDriverReceptionistCount));
        currentDriverSecretaryCount.setText(String.valueOf(OfficeSimulator.currentDriverSecretaryCount));
        currentVisitorVisitorCount.setText(String.valueOf(OfficeSimulator.currentVisitorVisitorCount));
        currentVisitorGuardCount.setText(String.valueOf(OfficeSimulator.currentVisitorGuardCount));
        currentVisitorReceptionistCount.setText(String.valueOf(OfficeSimulator.currentVisitorReceptionistCount));
        currentVisitorSecretaryCount.setText(String.valueOf(OfficeSimulator.currentVisitorSecretaryCount));
        currentGuardSecretaryCount.setText(String.valueOf(OfficeSimulator.currentGuardSecretaryCount));
        currentReceptionistSecretaryCount.setText(String.valueOf(OfficeSimulator.currentReceptionistSecretaryCount));
        currentSecretarySecretaryCount.setText(String.valueOf(OfficeSimulator.currentSecretarySecretaryCount));
    }

    public void setElements() {
        stackPane.setScaleX(CANVAS_SCALE);
        stackPane.setScaleY(CANVAS_SCALE);

        double rowsScaled = Main.officeSimulator.getOffice().getRows() * OfficeGraphicsController.tileSize;
        double columnsScaled = Main.officeSimulator.getOffice().getColumns() * OfficeGraphicsController.tileSize;

        stackPane.setPrefWidth(columnsScaled);
        stackPane.setPrefHeight(rowsScaled);

        backgroundCanvas.setWidth(columnsScaled);
        backgroundCanvas.setHeight(rowsScaled);

        foregroundCanvas.setWidth(columnsScaled);
        foregroundCanvas.setHeight(rowsScaled);

        markingsCanvas.setWidth(columnsScaled);
        markingsCanvas.setHeight(rowsScaled);
    }

    @FXML
    public void playAction() {
        if (!Main.officeSimulator.isRunning()) {
            Main.officeSimulator.setRunning(true);
            Main.officeSimulator.getPlaySemaphore().release();
            playButton.setText("Pause");
            exportToCSVButton.setDisable(true);
            exportHeatMapButton.setDisable(true);
        }
        else {
            Main.officeSimulator.setRunning(false);
            playButton.setText("Play");
            exportToCSVButton.setDisable(false);
            exportHeatMapButton.setDisable(false);
        }
    }

    @Override
    protected void closeAction() {
    }

    public void disableEdits() {
        nonverbalMean.setDisable(true);
        nonverbalStdDev.setDisable(true);
        cooperativeMean.setDisable(true);
        cooperativeStdDev.setDisable(true);
        exchangeMean.setDisable(true);
        exchangeStdDev.setDisable(true);
        fieldOfView.setDisable(true);
        maxClients.setDisable(true);
        maxCurrentClients.setDisable(true);
        maxDrivers.setDisable(true);
        maxCurrentDrivers.setDisable(true);
        maxVisitors.setDisable(true);
        maxCurrentVisitors.setDisable(true);
        resetToDefaultButton.setDisable(true);
        configureIOSButton.setDisable(true);
        editInteractionButton.setDisable(true);
    }

    public void resetToDefault() {
        nonverbalMean.setText(Integer.toString(OfficeAgentMovement.defaultNonverbalMean));
        nonverbalStdDev.setText(Integer.toString(OfficeAgentMovement.defaultNonverbalStdDev));
        cooperativeMean.setText(Integer.toString(OfficeAgentMovement.defaultCooperativeMean));
        cooperativeStdDev.setText(Integer.toString(OfficeAgentMovement.defaultCooperativeStdDev));
        exchangeMean.setText(Integer.toString(OfficeAgentMovement.defaultExchangeMean));
        exchangeStdDev.setText(Integer.toString(OfficeAgentMovement.defaultExchangeStdDev));
        fieldOfView.setText(Integer.toString(OfficeAgentMovement.defaultFieldOfView));
        maxClients.setText(Integer.toString(OfficeSimulator.defaultMaxClients));
        maxDrivers.setText(Integer.toString(OfficeSimulator.defaultMaxDrivers));
        maxVisitors.setText(Integer.toString(OfficeSimulator.defaultMaxVisitors));
        maxCurrentClients.setText(Integer.toString(OfficeSimulator.defaultMaxCurrentClients));
        maxCurrentDrivers.setText(Integer.toString(OfficeSimulator.defaultMaxCurrentDrivers));
        maxCurrentVisitors.setText(Integer.toString(OfficeSimulator.defaultMaxCurrentVisitors));
    }

    public void openIOSLevels() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/socialsim/view/OfficeConfigureIOS.fxml"));
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
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/socialsim/view/OfficeEditInteractions.fxml"));
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

    public void exportToCSV(){
        try {
            OfficeSimulator.exportToCSV();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    public void configureParameters(Office office) {
        office.setNonverbalMean(Integer.parseInt(nonverbalMean.getText()));
        office.setNonverbalStdDev(Integer.parseInt(nonverbalStdDev.getText()));
        office.setCooperativeMean(Integer.parseInt(cooperativeMean.getText()));
        office.setCooperativeStdDev(Integer.parseInt(cooperativeStdDev.getText()));
        office.setExchangeMean(Integer.parseInt(exchangeMean.getText()));
        office.setExchangeStdDev(Integer.parseInt(exchangeStdDev.getText()));
        office.setFieldOfView(Integer.parseInt(fieldOfView.getText()));
        office.setMAX_CLIENTS(Integer.parseInt(maxClients.getText()));
        office.setMAX_CURRENT_CLIENTS(Integer.parseInt(maxCurrentClients.getText()));
        office.setMAX_DRIVERS(Integer.parseInt(maxDrivers.getText()));
        office.setMAX_CURRENT_DRIVERS(Integer.parseInt(maxCurrentDrivers.getText()));
        office.setMAX_VISITORS(Integer.parseInt(maxVisitors.getText()));
        office.setMAX_CURRENT_VISITORS(Integer.parseInt(maxCurrentVisitors.getText()));

        currentManagerCount.setText(String.valueOf(OfficeSimulator.currentManagerCount));
        currentBusinessCount.setText(String.valueOf(OfficeSimulator.currentBusinessCount));
        currentResearchCount.setText(String.valueOf(OfficeSimulator.currentResearchCount));
        currentTechnicalCount.setText(String.valueOf(OfficeSimulator.currentTechnicalCount));
        currentSecretaryCount.setText(String.valueOf(OfficeSimulator.currentSecretaryCount));
        currentClientCount.setText(String.valueOf(OfficeSimulator.currentClientCount));
        currentDriverCount.setText(String.valueOf(OfficeSimulator.currentDriverCount));
        currentVisitorCount.setText(String.valueOf(OfficeSimulator.currentVisitorCount));
        currentNonverbalCount.setText(String.valueOf(OfficeSimulator.currentNonverbalCount));
        currentCooperativeCount.setText(String.valueOf(OfficeSimulator.currentCooperativeCount));
        currentExchangeCount.setText(String.valueOf(OfficeSimulator.currentExchangeCount));
        averageNonverbalDuration.setText(String.valueOf(OfficeSimulator.averageNonverbalDuration));
        averageCooperativeDuration.setText(String.valueOf(OfficeSimulator.averageCooperativeDuration));
        averageExchangeDuration.setText(String.valueOf(OfficeSimulator.averageExchangeDuration));
        currentTeam1Count.setText(String.valueOf(OfficeSimulator.currentTeam1Count));
        currentTeam2Count.setText(String.valueOf(OfficeSimulator.currentTeam2Count));
        currentTeam3Count.setText(String.valueOf(OfficeSimulator.currentTeam3Count));
        currentTeam4Count.setText(String.valueOf(OfficeSimulator.currentTeam4Count));
        currentBossManagerCount.setText(String.valueOf(OfficeSimulator.currentBossManagerCount));
        currentBossBusinessCount.setText(String.valueOf(OfficeSimulator.currentBossBusinessCount));
        currentBossResearcherCount.setText(String.valueOf(OfficeSimulator.currentBossResearcherCount));
        currentBossTechnicalCount.setText(String.valueOf(OfficeSimulator.currentBossTechnicalCount));
        currentBossJanitorCount.setText(String.valueOf(OfficeSimulator.currentBossJanitorCount));
        currentBossClientCount.setText(String.valueOf(OfficeSimulator.currentBossClientCount));
        currentBossDriverCount.setText(String.valueOf(OfficeSimulator.currentBossDriverCount));
        currentBossVisitorCount.setText(String.valueOf(OfficeSimulator.currentBossVisitorCount));
        currentBossGuardCount.setText(String.valueOf(OfficeSimulator.currentBossGuardCount));
        currentBossReceptionistCount.setText(String.valueOf(OfficeSimulator.currentBossReceptionistCount));
        currentBossSecretaryCount.setText(String.valueOf(OfficeSimulator.currentBossSecretaryCount));
        currentManagerManagerCount.setText(String.valueOf(OfficeSimulator.currentManagerManagerCount));
        currentManagerBusinessCount.setText(String.valueOf(OfficeSimulator.currentManagerBusinessCount));
        currentManagerResearcherCount.setText(String.valueOf(OfficeSimulator.currentManagerResearcherCount));
        currentManagerTechnicalCount.setText(String.valueOf(OfficeSimulator.currentManagerTechnicalCount));
        currentManagerJanitorCount.setText(String.valueOf(OfficeSimulator.currentManagerJanitorCount));
        currentManagerClientCount.setText(String.valueOf(OfficeSimulator.currentManagerClientCount));
        currentManagerDriverCount.setText(String.valueOf(OfficeSimulator.currentManagerDriverCount));
        currentManagerVisitorCount.setText(String.valueOf(OfficeSimulator.currentManagerVisitorCount));
        currentManagerGuardCount.setText(String.valueOf(OfficeSimulator.currentManagerGuardCount));
        currentManagerReceptionistCount.setText(String.valueOf(OfficeSimulator.currentManagerReceptionistCount));
        currentManagerSecretaryCount.setText(String.valueOf(OfficeSimulator.currentManagerSecretaryCount));
        currentBusinessBusinessCount.setText(String.valueOf(OfficeSimulator.currentBusinessBusinessCount));
        currentBusinessResearcherCount.setText(String.valueOf(OfficeSimulator.currentBusinessResearcherCount));
        currentBusinessTechnicalCount.setText(String.valueOf(OfficeSimulator.currentBusinessTechnicalCount));
        currentBusinessJanitorCount.setText(String.valueOf(OfficeSimulator.currentBusinessJanitorCount));
        currentBusinessClientCount.setText(String.valueOf(OfficeSimulator.currentBusinessClientCount));
        currentBusinessDriverCount.setText(String.valueOf(OfficeSimulator.currentBusinessDriverCount));
        currentBusinessVisitorCount.setText(String.valueOf(OfficeSimulator.currentBusinessVisitorCount));
        currentBusinessGuardCount.setText(String.valueOf(OfficeSimulator.currentBusinessGuardCount));
        currentBusinessReceptionistCount.setText(String.valueOf(OfficeSimulator.currentBusinessReceptionistCount));
        currentBusinessSecretaryCount.setText(String.valueOf(OfficeSimulator.currentBusinessSecretaryCount));
        currentResearcherResearcherCount.setText(String.valueOf(OfficeSimulator.currentResearcherResearcherCount));
        currentResearcherTechnicalCount.setText(String.valueOf(OfficeSimulator.currentResearcherTechnicalCount));
        currentResearcherJanitorCount.setText(String.valueOf(OfficeSimulator.currentResearcherJanitorCount));
        currentResearcherClientCount.setText(String.valueOf(OfficeSimulator.currentResearcherClientCount));
        currentResearcherDriverCount.setText(String.valueOf(OfficeSimulator.currentResearcherDriverCount));
        currentResearcherVisitorCount.setText(String.valueOf(OfficeSimulator.currentResearcherVisitorCount));
        currentResearcherGuardCount.setText(String.valueOf(OfficeSimulator.currentResearcherGuardCount));
        currentResearcherReceptionistCount.setText(String.valueOf(OfficeSimulator.currentResearcherReceptionistCount));
        currentResearcherSecretaryCount.setText(String.valueOf(OfficeSimulator.currentResearcherSecretaryCount));
        currentTechnicalTechnicalCount.setText(String.valueOf(OfficeSimulator.currentTechnicalTechnicalCount));
        currentTechnicalJanitorCount.setText(String.valueOf(OfficeSimulator.currentTechnicalJanitorCount));
        currentTechnicalClientCount.setText(String.valueOf(OfficeSimulator.currentTechnicalClientCount));
        currentTechnicalDriverCount.setText(String.valueOf(OfficeSimulator.currentTechnicalDriverCount));
        currentTechnicalVisitorCount.setText(String.valueOf(OfficeSimulator.currentTechnicalVisitorCount));
        currentTechnicalGuardCount.setText(String.valueOf(OfficeSimulator.currentTechnicalGuardCount));
        currentTechnicalReceptionistCount.setText(String.valueOf(OfficeSimulator.currentTechnicalReceptionistCount));
        currentTechnicalSecretaryCount.setText(String.valueOf(OfficeSimulator.currentTechnicalSecretaryCount));
        currentJanitorJanitorCount.setText(String.valueOf(OfficeSimulator.currentJanitorJanitorCount));
        currentJanitorClientCount.setText(String.valueOf(OfficeSimulator.currentJanitorClientCount));
        currentJanitorDriverCount.setText(String.valueOf(OfficeSimulator.currentJanitorDriverCount));
        currentJanitorVisitorCount.setText(String.valueOf(OfficeSimulator.currentJanitorVisitorCount));
        currentJanitorSecretaryCount.setText(String.valueOf(OfficeSimulator.currentJanitorSecretaryCount));
        currentClientClientCount.setText(String.valueOf(OfficeSimulator.currentClientClientCount));
        currentClientDriverCount.setText(String.valueOf(OfficeSimulator.currentClientDriverCount));
        currentClientVisitorCount.setText(String.valueOf(OfficeSimulator.currentClientVisitorCount));
        currentClientGuardCount.setText(String.valueOf(OfficeSimulator.currentClientGuardCount));
        currentClientReceptionistCount.setText(String.valueOf(OfficeSimulator.currentClientReceptionistCount));
        currentClientSecretaryCount.setText(String.valueOf(OfficeSimulator.currentClientSecretaryCount));
        currentDriverDriverCount.setText(String.valueOf(OfficeSimulator.currentDriverDriverCount));
        currentDriverVisitorCount.setText(String.valueOf(OfficeSimulator.currentDriverVisitorCount));
        currentDriverGuardCount.setText(String.valueOf(OfficeSimulator.currentDriverGuardCount));
        currentDriverReceptionistCount.setText(String.valueOf(OfficeSimulator.currentDriverReceptionistCount));
        currentDriverSecretaryCount.setText(String.valueOf(OfficeSimulator.currentDriverSecretaryCount));
        currentVisitorVisitorCount.setText(String.valueOf(OfficeSimulator.currentVisitorVisitorCount));
        currentVisitorGuardCount.setText(String.valueOf(OfficeSimulator.currentVisitorGuardCount));
        currentVisitorReceptionistCount.setText(String.valueOf(OfficeSimulator.currentVisitorReceptionistCount));
        currentVisitorSecretaryCount.setText(String.valueOf(OfficeSimulator.currentVisitorSecretaryCount));
        currentGuardSecretaryCount.setText(String.valueOf(OfficeSimulator.currentGuardSecretaryCount));
        currentReceptionistSecretaryCount.setText(String.valueOf(OfficeSimulator.currentReceptionistSecretaryCount));
        currentSecretarySecretaryCount.setText(String.valueOf(OfficeSimulator.currentSecretarySecretaryCount));
    }

    public boolean validateParameters() {
        boolean validParameters = Integer.parseInt(nonverbalMean.getText()) >= 0 && Integer.parseInt(nonverbalMean.getText()) >= 0
                && Integer.parseInt(cooperativeMean.getText()) >= 0 && Integer.parseInt(cooperativeStdDev.getText()) >= 0
                && Integer.parseInt(exchangeMean.getText()) >= 0 && Integer.parseInt(exchangeStdDev.getText()) >= 0
                && Integer.parseInt(fieldOfView.getText()) >= 0 && Integer.parseInt(fieldOfView.getText()) <= 360
                && Integer.parseInt(maxClients.getText()) >= 0 && Integer.parseInt(maxDrivers.getText()) >= 0
                && Integer.parseInt(maxVisitors.getText()) >= 0;
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

}