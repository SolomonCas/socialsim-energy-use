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

        int width = 83;
        int length = 150;
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
            for (int j = 20; j < 92; j++) {
                wallPatches.add(office.getPatch(i, j));
            }
        }

        // Large meeting room and Staff room (?) Wall
        for (int i = 56; i < 70; i++) {
            for (int j = 92; j < 125; j++) {
                    wallPatches.add(office.getPatch(i, j));
            }
        }

        // Learning Area 1, 2, 3, 4 Wall
        for (int i = 12; i < 30; i++) {
            for (int j = 19; j < 83; j++) {
                wallPatches.add(office.getPatch(i, j));
            }
        }

        // Control Center Wall
        for (int i = 12; i < 30; i++) {
            for (int j = 83; j < 96; j++) {
                wallPatches.add(office.getPatch(i, j));
            }
        }

        // Database Center Wall
        for (int i = 21; i < 30; i++) {
            for (int j = 96; j < 113; j++) {
                wallPatches.add(office.getPatch(i, j));
            }
        }

        // Open Area 1 & 2 Wall
        for (int i = 38; i < 45; i++) {
            for (int j = 24; j < 39; j++) {
                wallPatches.add(office.getPatch(i, j));
            }
        }

        // Open Area 3 & 4 Wall
        for (int i = 38; i < 45; i++) {
            for (int j = 65; j < 80; j++) {
                wallPatches.add(office.getPatch(i, j));
            }
        }

        // Management and Executive Secretary Area Wall
        for (int i = 37; i < 48; i++) {
            for (int j = 95; j < 110; j++) {
                    wallPatches.add(office.getPatch(i, j));
            }
        }

        // Reception Wall
        for (int i = 37; i < 46; i++) {
            for (int j = 120; j < 134; j++) {
                wallPatches.add(office.getPatch(i, j));
            }
        }

        // Clinic & Executive Dean Office Wall
        for (int i = 46; i < 77; i++) {
            for (int j = 134; j < 149; j++) {
                wallPatches.add(office.getPatch(i, j));
            }
        }

        Main.officeSimulator.getOffice().getWalls().add(Wall.wallFactory.create(wallPatches, 1));

        List<Patch> outerWallPatches = new ArrayList<>();

        // Outer Walls
        for (int i = 0; i < 84; i++) {
            for (int j = 0; j < 150; j++) {
                if (i < 5) {
                    outerWallPatches.add(office.getPatch(i, j));
                }
                else if ((i >= 5 && i < 12) && (j <= 6 || j >= 19)) {
                    outerWallPatches.add(office.getPatch(i, j));
                }
                else if (i == 12 && (j <= 6 || (j >= 96 && j < 135) || j >= 145)) {
                    outerWallPatches.add(office.getPatch(i, j));
                }
                else if ((i >= 13 && i < 17) && (j == 0 || (j >= 96 && j < 135) || j >= 145)) {
                    outerWallPatches.add(office.getPatch(i, j));
                }
                else if (i == 17 && (j == 0 || (j >= 96 && j < 126) || j >= 145)) {
                    outerWallPatches.add(office.getPatch(i, j));
                }
                else if ((i >= 18 && i < 21) && (j == 0 || (j >= 96 && j < 113) || (j >= 118 && j < 126) || j >= 145)) {
                    outerWallPatches.add(office.getPatch(i, j));
                }
                else if ((i >= 21 && i < 29) && (j == 0 || (j >= 118 && j < 126) || j >= 145)) {
                    outerWallPatches.add(office.getPatch(i, j));
                }
                else if (i == 29 && (j == 0 || (j >= 118 && j < 135) || j >= 145)) {
                    outerWallPatches.add(office.getPatch(i, j));
                }
                else if ((i >= 30 && i < 34) && (j == 0 || j == 134 || j >= 145)) {
                    outerWallPatches.add(office.getPatch(i, j));
                }
                else if ((i >= 34 && i < 46) && (j == 0 || j >= 134)) {
                    outerWallPatches.add(office.getPatch(i, j));
                }
                else if ((i >= 46 && i < 64) && (j == 0 || j == 149)) {
                    outerWallPatches.add(office.getPatch(i, j));
                }
                else if (i == 64 && (j < 20 || j == 149)) {
                    outerWallPatches.add(office.getPatch(i, j));
                }
                else if (i == 65 && (j == 19 || j == 149)) {
                    outerWallPatches.add(office.getPatch(i, j));
                }
                else if (i == 66 && ((j >= 19 && j <= 91) || j == 149)) {
                    outerWallPatches.add(office.getPatch(i, j));
                }
                else if ((i >= 67 && i < 69) && (j == 91 || j == 149)) {
                    outerWallPatches.add(office.getPatch(i, j));
                }
                else if (i == 69 && ((j >= 88 && j <= 91) || j == 149)) {
                    outerWallPatches.add(office.getPatch(i, j));
                }
                else if ((i >= 70 && i < 77) && (j == 88 || j == 149)) {
                    outerWallPatches.add(office.getPatch(i, j));
                }
                else if (i == 77 && (j == 88 || j >= 134)) {
                    outerWallPatches.add(office.getPatch(i, j));
                }
                else if ((i >= 78 && i < 82) && (j == 88|| j == 134)) {
                    outerWallPatches.add(office.getPatch(i, j));
                }
                else if (i == 82 && (j >= 88 && j <= 134)) {
                    outerWallPatches.add(office.getPatch(i, j));
                }
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
            for (int j = 20; j < 75; j++) {
                if (i < 58 || i > 59) {
                    researchAreaPatches.add(office.getPatch(i, j));
                }
                else if ((i == 58 || i == 59) && (j < 22 || (j > 25 && j < 42) || j > 43)) {
                    researchAreaPatches.add(office.getPatch(i, j));
                }

            }
        }
        Main.officeSimulator.getOffice().getStudyAreas().add(StudyArea.studyAreaFactory.create(researchAreaPatches, 5));

        // Faculty Room
        List<Patch> officeRoomPatches = new ArrayList<>();
        for (int i = 53; i < 66; i++) {
            for (int j = 76; j < 91; j++) {
                officeRoomPatches.add(office.getPatch(i, j));
            }
        }
        Main.officeSimulator.getOffice().getOfficeRooms().add(OfficeRoom.officeRoomFactory.create(officeRoomPatches, 1));

        // Staff Room(?)
        List<Patch> staffRoomAreaPatches = new ArrayList<>();
        for (int i = 57; i < 69; i++) {
            for (int j = 92; j < 102; j++) {
                staffRoomAreaPatches.add(office.getPatch(i, j));
            }
        }
        Main.officeSimulator.getOffice().getStaffOffices().add(StaffOffice.staffOfficeFactory.create(staffRoomAreaPatches, 4));

        // Large Meeting Room
        List<Patch> meetingRoom2Patches = new ArrayList<>();
        for (int i = 57; i < 69; i++) {
            for (int j = 103; j < 124; j++) {
                meetingRoom2Patches.add(office.getPatch(i, j));
            }
        }
        Main.officeSimulator.getOffice().getMeetingRooms().add(MeetingRoom.meetingRoomFactory.create(meetingRoom2Patches, 2));

        // Pantry Area
        List<Patch> pantryPatches = new ArrayList<>();
        for (int i = 70; i < 82; i++) {
            for (int j = 89; j < 134; j++) {
                pantryPatches.add(office.getPatch(i, j));
            }
        }
        Main.officeSimulator.getOffice().getCafeterias().add(Cafeteria.cafeteriaFactory.create(pantryPatches, 1));

        // Learning Area 1 (Leftmost)
        List<Patch> learningAreaPatches1 = new ArrayList<>();
        for (int i = 12; i < 29; i++) {
            for (int j = 20; j < 35; j++) {
                learningAreaPatches1.add(office.getPatch(i, j));
            }
        }
        Main.officeSimulator.getOffice().getStudyAreas().add(StudyArea.studyAreaFactory.create(learningAreaPatches1, 1));

        // Learning Area 2
        List<Patch> learningAreaPatches2 = new ArrayList<>();
        for (int i = 12; i < 29; i++) {
            for (int j = 36; j < 51; j++) {
                learningAreaPatches2.add(office.getPatch(i, j));
            }
        }
        Main.officeSimulator.getOffice().getStudyAreas().add(StudyArea.studyAreaFactory.create(learningAreaPatches2, 2));

        // Learning Area 3
        List<Patch> learningAreaPatches3 = new ArrayList<>();
        for (int i = 12; i < 29; i++) {
            for (int j = 52; j < 67; j++) {
                learningAreaPatches3.add(office.getPatch(i, j));
            }
        }
        Main.officeSimulator.getOffice().getStudyAreas().add(StudyArea.studyAreaFactory.create(learningAreaPatches3, 3));

        // Learning Area 4 (Rightmost)
        List<Patch> learningAreaPatches4 = new ArrayList<>();
        for (int i = 12; i < 29; i++) {
            for (int j = 68; j < 83; j++) {
                learningAreaPatches4.add(office.getPatch(i, j));
            }
        }
        Main.officeSimulator.getOffice().getStudyAreas().add(StudyArea.studyAreaFactory.create(learningAreaPatches4, 4));

        // Control Center (Up/Back)
        List<Patch> controlCenterAreaPatches1 = new ArrayList<>();
        for (int i = 12; i < 17; i++) {
            for (int j = 84; j < 96; j++) {
                controlCenterAreaPatches1.add(office.getPatch(i, j));
            }
        }
        Main.officeSimulator.getOffice().getStaffOffices().add(StaffOffice.staffOfficeFactory.create(controlCenterAreaPatches1, 1));

        // Control Center (Down/Front)
        List<Patch> controlCenterAreaPatches2 = new ArrayList<>();
        for (int i = 18; i < 29; i++) {
            for (int j = 84; j < 96; j++) {
                controlCenterAreaPatches2.add(office.getPatch(i, j));
            }
        }
        Main.officeSimulator.getOffice().getStaffOffices().add(StaffOffice.staffOfficeFactory.create(controlCenterAreaPatches2, 2));

        // Database Control Center
        List<Patch> databaseControlAreaPatches = new ArrayList<>();
        for (int i = 21; i < 29; i++) {
            for (int j = 97; j < 112; j++) {
                databaseControlAreaPatches.add(office.getPatch(i, j));
            }
        }
        Main.officeSimulator.getOffice().getStaffOffices().add(StaffOffice.staffOfficeFactory.create(databaseControlAreaPatches, 3));

        // Open Area Study Area 1 (Leftmost)
        List<Patch> openAreaPatches1 = new ArrayList<>();
        for (int i = 39; i < 44; i++) {
            for (int j = 25; j < 31; j++) {
                    openAreaPatches1.add(office.getPatch(i, j));
            }
        }
        Main.officeSimulator.getOffice().getStudyAreas().add(StudyArea.studyAreaFactory.create(openAreaPatches1, 6));

        // Open Area Study Area 2
        List<Patch> openAreaPatches2 = new ArrayList<>();
        for (int i = 39; i < 44; i++) {
            for (int j = 32; j < 38; j++) {
                openAreaPatches2.add(office.getPatch(i, j));
            }
        }
        Main.officeSimulator.getOffice().getStudyAreas().add(StudyArea.studyAreaFactory.create(openAreaPatches2, 7));

        // Open Area Study Area 3
        List<Patch> openAreaPatches3 = new ArrayList<>();
        for (int i = 39; i < 44; i++) {
            for (int j = 66; j < 72; j++) {
                openAreaPatches3.add(office.getPatch(i, j));
            }
        }
        Main.officeSimulator.getOffice().getStudyAreas().add(StudyArea.studyAreaFactory.create(openAreaPatches3, 8));

        // Open Area Study Area 4 (Rightmost)
        List<Patch> openAreaPatches4 = new ArrayList<>();
        for (int i = 39; i < 44; i++) {
            for (int j = 73; j < 79; j++) {
                openAreaPatches4.add(office.getPatch(i, j));
            }
        }
        Main.officeSimulator.getOffice().getStudyAreas().add(StudyArea.studyAreaFactory.create(openAreaPatches4, 9));

        // Management and Executive Secretary Area 1
        List<Patch> managementAndExecSecretaryAreaPatches1 = new ArrayList<>();
        for (int i = 37; i < 42; i++) {
            for (int j = 96; j < 109; j++) {
                if (j != 102) {
                    managementAndExecSecretaryAreaPatches1.add(office.getPatch(i, j));
                }

            }
        }
        Main.officeSimulator.getOffice().getBreakrooms().add(Breakroom.breakroomFactory.create(managementAndExecSecretaryAreaPatches1, 1));

        // Management and Executive Secretary Area 2
        List<Patch> managementAndExecSecretaryAreaPatches2 = new ArrayList<>();
        for (int i = 43; i < 48; i++) {
            for (int j = 96; j < 109; j++) {
                if (j != 102) {
                    managementAndExecSecretaryAreaPatches2.add(office.getPatch(i, j));
                }
            }
        }
        Main.officeSimulator.getOffice().getBreakrooms().add(Breakroom.breakroomFactory.create(managementAndExecSecretaryAreaPatches2, 2));

        // Men's Bathroom
        List<Patch> mBathroomPatches = new ArrayList<>();
        for (int i = 1; i < 11; i++) {
            for (int j = 135; j < 149; j++) {
                mBathroomPatches.add(office.getPatch(i, j));
            }
        }
        Main.officeSimulator.getOffice().getBathrooms().add(Bathroom.bathroomFactory.create(mBathroomPatches, 2));

        // Women's Bathroom
        List<Patch> fBathroomPatches = new ArrayList<>();
        for (int i = 35; i < 45; i++) {
            for (int j = 135; j < 149; j++) {
                fBathroomPatches.add(office.getPatch(i, j));
            }
        }
        Main.officeSimulator.getOffice().getBathrooms().add(Bathroom.bathroomFactory.create(fBathroomPatches, 1));

        // Reception Area
        List<Patch> receptionPatches = new ArrayList<>();
        for (int i = 30; i < 45; i++) {
            for (int j = 121; j < 134; j++) {
                receptionPatches.add(office.getPatch(i, j));
            }
        }
        Main.officeSimulator.getOffice().getReceptions().add(Reception.receptionFactory.create(receptionPatches, 1));

        // Clinic (Front/Left)
        List<Patch> breakroomPatches1 = new ArrayList<>();
        for (int i = 46; i < 56; i++) {
            for (int j = 135; j < 142; j++) {
                breakroomPatches1.add(office.getPatch(i, j));
            }
        }
        Main.officeSimulator.getOffice().getBreakrooms().add(Breakroom.breakroomFactory.create(breakroomPatches1, 3));

        // Clinic (Back/Right)
        List<Patch> breakroomPatches2 = new ArrayList<>();
        for (int i = 46; i < 56; i++) {
            for (int j = 143; j < 149; j++) {
                breakroomPatches2.add(office.getPatch(i, j));
            }
        }
        Main.officeSimulator.getOffice().getBreakrooms().add(Breakroom.breakroomFactory.create(breakroomPatches2, 4));

        // Executive Dean's Office
        List<Patch> execDeanOfficeAreaPatches = new ArrayList<>();
        for (int i = 57; i < 77; i++) {
            for (int j = 135; j < 149; j++) {
                execDeanOfficeAreaPatches.add(office.getPatch(i, j));
            }
        }
        Main.officeSimulator.getOffice().getOfficeRooms().add(OfficeRoom.officeRoomFactory.create(execDeanOfficeAreaPatches, 2));

        List<Patch> cabinetDownPatches = new ArrayList<>();
        // Pantry
        cabinetDownPatches.add(office.getPatch(70,89));
        cabinetDownPatches.add(office.getPatch(70,94));
        cabinetDownPatches.add(office.getPatch(70,96));

        cabinetDownPatches.add(office.getPatch(23,113));

        cabinetDownPatches.add(office.getPatch(42,110));
        cabinetDownPatches.add(office.getPatch(42,118));
        CabinetMapper.draw(cabinetDownPatches, "DOWN");

        List<Patch> cabinetUpPatches = new ArrayList<>();
        cabinetUpPatches.add(office.getPatch(64,20));
        cabinetUpPatches.add(office.getPatch(64,22));
        cabinetUpPatches.add(office.getPatch(64,24));
        cabinetUpPatches.add(office.getPatch(64,26));

        cabinetUpPatches.add(office.getPatch(54,92));

        cabinetUpPatches.add(office.getPatch(40,110));
        cabinetUpPatches.add(office.getPatch(40,118));
        CabinetMapper.draw(cabinetUpPatches, "UP");

        List<Patch> chairPatches = new ArrayList<>();

        // Data Collection Chair
        chairPatches.add(office.getPatch(53,1));
        chairPatches.add(office.getPatch(53,3));
        chairPatches.add(office.getPatch(53,5));

        // Reception Chair
        chairPatches.add(office.getPatch(40,128));
        // Large Meeting Chair
        for (int j = 109; j < 118; j+=2) {
            chairPatches.add(office.getPatch(60,j));
            chairPatches.add(office.getPatch(65,j));
        }
        chairPatches.add(office.getPatch(62,107));
        chairPatches.add(office.getPatch(62,118));
        // Small Meeting Chair
        for (int i = 18; i < 24; i+=2) {
            chairPatches.add(office.getPatch(i,5));
            chairPatches.add(office.getPatch(i,8));
        }
        chairPatches.add(office.getPatch(16,6));
        chairPatches.add(office.getPatch(25,6));
        // Learning Area 1-4 Chairs
        for (int i = 17; i < 23; i += 5) {
            for (int j = 22; j < 80; j += 16) {
                chairPatches.add(office.getPatch(i,j));
                chairPatches.add(office.getPatch(i,j + 2));
                chairPatches.add(office.getPatch(i + 2,j));
                chairPatches.add(office.getPatch(i + 2,j + 2));

                chairPatches.add(office.getPatch(i,j + 8));
                chairPatches.add(office.getPatch(i,j + 10));
                chairPatches.add(office.getPatch(i + 2,j + 8));
                chairPatches.add(office.getPatch(i + 2,j + 10));
            }
        }
        // Pantry Chairs
        chairPatches.add(office.getPatch(74,129));
        chairPatches.add(office.getPatch(73,130));
        chairPatches.add(office.getPatch(75,131));
        chairPatches.add(office.getPatch(76,130));

        chairPatches.add(office.getPatch(77,94));
        chairPatches.add(office.getPatch(76,95));
        chairPatches.add(office.getPatch(78,96));
        chairPatches.add(office.getPatch(77,97));

        chairPatches.add(office.getPatch(77,104));
        chairPatches.add(office.getPatch(76,105));
        chairPatches.add(office.getPatch(78,106));
        chairPatches.add(office.getPatch(77,107));

        chairPatches.add(office.getPatch(73,118));
        chairPatches.add(office.getPatch(72,119));
        chairPatches.add(office.getPatch(74,120));
        chairPatches.add(office.getPatch(73,121));

        chairPatches.add(office.getPatch(78,118));
        chairPatches.add(office.getPatch(77,119));
        chairPatches.add(office.getPatch(79,120));
        chairPatches.add(office.getPatch(78,121));

        // Management and Executive Secretary chair
        chairPatches.add(office.getPatch(38,100));
        chairPatches.add(office.getPatch(41,100));
        chairPatches.add(office.getPatch(38,104));
        chairPatches.add(office.getPatch(41,104));

        chairPatches.add(office.getPatch(43,100));
        chairPatches.add(office.getPatch(46,100));
        chairPatches.add(office.getPatch(43,104));
        chairPatches.add(office.getPatch(46,104));

        // Executive Director Meeting Chairs
        for (int i = 63; i < 69; i+=2) {
            chairPatches.add(office.getPatch(i,143));
            chairPatches.add(office.getPatch(i,146));
        }
        chairPatches.add(office.getPatch(61,144));
        chairPatches.add(office.getPatch(70,144));

        chairPatches.add(office.getPatch(73,137));

        // Research Area Chairs
        chairPatches.add(office.getPatch(57,28));
        chairPatches.add(office.getPatch(57,31));
        chairPatches.add(office.getPatch(61,28));
        chairPatches.add(office.getPatch(61,31));

        chairPatches.add(office.getPatch(57,35));
        chairPatches.add(office.getPatch(57,38));
        chairPatches.add(office.getPatch(61,35));
        chairPatches.add(office.getPatch(61,38));

        chairPatches.add(office.getPatch(57,47));
        chairPatches.add(office.getPatch(57,50));
        chairPatches.add(office.getPatch(61,47));
        chairPatches.add(office.getPatch(61,50));

        chairPatches.add(office.getPatch(57,54));
        chairPatches.add(office.getPatch(57,57));
        chairPatches.add(office.getPatch(60,57));

        chairPatches.add(office.getPatch(59,64));
        chairPatches.add(office.getPatch(62,61));
        chairPatches.add(office.getPatch(62,64));

        chairPatches.add(office.getPatch(57,69));
        chairPatches.add(office.getPatch(57,72));
        chairPatches.add(office.getPatch(61,69));
        chairPatches.add(office.getPatch(61,72));
        ChairMapper.draw(chairPatches);

        List<Patch> verticalCollabDeskPatches = new ArrayList<>();
        // Research Area Collab Desk
        verticalCollabDeskPatches.add(office.getPatch(57,29));
        verticalCollabDeskPatches.add(office.getPatch(57,36));
        verticalCollabDeskPatches.add(office.getPatch(57,48));

        verticalCollabDeskPatches.add(office.getPatch(57,70));
        CollabDeskMapper.draw(verticalCollabDeskPatches, "VERTICAL", 3);

        List<Patch> verticalCollabDeskPatches2 = new ArrayList<>();
        // Research Area Collab 2 Desk
        verticalCollabDeskPatches2.add(office.getPatch(57,55));
        verticalCollabDeskPatches2.add(office.getPatch(59,62));
        CollabDeskMapper.draw(verticalCollabDeskPatches2, "VERTICAL", 2);

        List<Patch> horizontalCollabDeskPatches = new ArrayList<>();
        horizontalCollabDeskPatches.add(office.getPatch(52,1));
        CollabDeskMapper.draw(horizontalCollabDeskPatches, "HORIZONTAL", 3);

        List<Patch> couchRightPatches = new ArrayList<>();
        couchRightPatches.add(office.getPatch(39,93));
        CouchMapper.draw(couchRightPatches, "RIGHT");

        List<Patch> cubicleUpPatches = new ArrayList<>();
        // Faculty Cubicle
        cubicleUpPatches.add(office.getPatch(60,89));
        cubicleUpPatches.add(office.getPatch(60,85));
        cubicleUpPatches.add(office.getPatch(60,83));
        cubicleUpPatches.add(office.getPatch(60,79));
        cubicleUpPatches.add(office.getPatch(60,77));

        // Control Center Cubicle
        cubicleUpPatches.add(office.getPatch(23,86));

        // Open Area 2 & 4 Cubicle
        cubicleUpPatches.add(office.getPatch(39,34));
        cubicleUpPatches.add(office.getPatch(39,75));
        CubicleMapper.draw(cubicleUpPatches, "UP");

        List<Patch> cubicleDownPatches = new ArrayList<>();
        // Faculty Cubicle
        cubicleDownPatches.add(office.getPatch(58,89));
        cubicleDownPatches.add(office.getPatch(58,85));
        cubicleDownPatches.add(office.getPatch(58,83));
        cubicleDownPatches.add(office.getPatch(58,79));
        cubicleDownPatches.add(office.getPatch(58,77));

        // Control Center Cubicle
        cubicleDownPatches.add(office.getPatch(21,86));

        // Open Area 1 & 3 Cubicle
        cubicleDownPatches.add(office.getPatch(42,27));
        cubicleDownPatches.add(office.getPatch(42,68));
        CubicleMapper.draw(cubicleDownPatches, "DOWN");

        List<Patch> cubicleLeftPatches = new ArrayList<>();
        // Control Center Cubicle
        cubicleLeftPatches.add(office.getPatch(19,93));
        cubicleLeftPatches.add(office.getPatch(21,93));
        cubicleLeftPatches.add(office.getPatch(23,93));
        cubicleLeftPatches.add(office.getPatch(25,93));

        // Research Area Cubicle
        cubicleLeftPatches.add(office.getPatch(61,55));
        cubicleLeftPatches.add(office.getPatch(57,62));
        CubicleMapper.draw(cubicleLeftPatches, "LEFT");

        List<Patch> rightWhiteboardPatches = new ArrayList<>();
        // Small Meeting Whiteboard
        rightWhiteboardPatches.add(office.getPatch(18,1));
        rightWhiteboardPatches.add(office.getPatch(20,1));
        rightWhiteboardPatches.add(office.getPatch(22,1));
        // Research Area Whiteboard (Vertical)
        rightWhiteboardPatches.add(office.getPatch(58,22));
        rightWhiteboardPatches.add(office.getPatch(58,25));

        rightWhiteboardPatches.add(office.getPatch(58,41));
        rightWhiteboardPatches.add(office.getPatch(58,44));
        WhiteboardMapper.draw(rightWhiteboardPatches, "RIGHT");

        List<Patch> topWhiteboardPatches = new ArrayList<>();
        // Research Area Whiteboard (Horizontal)
        topWhiteboardPatches.add(office.getPatch(57,23));
        topWhiteboardPatches.add(office.getPatch(57,42));
        // Learning Area 1 Whiteboard
        topWhiteboardPatches.add(office.getPatch(12,20));
        topWhiteboardPatches.add(office.getPatch(12,22));
        topWhiteboardPatches.add(office.getPatch(12,31));
        topWhiteboardPatches.add(office.getPatch(12,33));
        // Learning Area 2 Whiteboard
        topWhiteboardPatches.add(office.getPatch(12,36));
        topWhiteboardPatches.add(office.getPatch(12,38));
        topWhiteboardPatches.add(office.getPatch(12,47));
        topWhiteboardPatches.add(office.getPatch(12,49));
        // Learning Area 3 Whiteboard
        topWhiteboardPatches.add(office.getPatch(12,52));
        topWhiteboardPatches.add(office.getPatch(12,54));
        topWhiteboardPatches.add(office.getPatch(12,63));
        topWhiteboardPatches.add(office.getPatch(12,65));
        // Learning Area 4 Whiteboard
        topWhiteboardPatches.add(office.getPatch(12,68));
        topWhiteboardPatches.add(office.getPatch(12,70));
        topWhiteboardPatches.add(office.getPatch(12,79));
        topWhiteboardPatches.add(office.getPatch(12,81));
        WhiteboardMapper.draw(topWhiteboardPatches, "UP");

        List<Patch> doorRightPatches = new ArrayList<>();
        // Small Meeting Room Door
        doorRightPatches.add(office.getPatch(13,12));
        doorRightPatches.add(office.getPatch(24,12));
        // Human Experience Room Door
        doorRightPatches.add(office.getPatch(42,14));
        // Bet. Research and Faculty Room Door
        doorRightPatches.add(office.getPatch(53,75));
        DoorMapper.draw(doorRightPatches, "RIGHT");

        List<Patch> doorLeftPatches = new ArrayList<>();
        // Clinic Door (Left/Front)
        doorLeftPatches.add(office.getPatch(47,134));
        // Executive Room Door
        doorLeftPatches.add(office.getPatch(58,134));
        DoorMapper.draw(doorLeftPatches, "LEFT");

        List<Patch> doorUpPatches = new ArrayList<>();
        // Main Entrance Door
        doorUpPatches.add(office.getPatch(29,130));
        // Women's Bathroom Door
        doorUpPatches.add(office.getPatch(34,135));
        // Staff Room Door (?)
        doorUpPatches.add(office.getPatch(56,98));
        // Large Meeting Room Doors
        doorUpPatches.add(office.getPatch(56,103));
        doorUpPatches.add(office.getPatch(56,120));
        // Research Area Room Door
        doorUpPatches.add(office.getPatch(52,20));
        // Faculty Room Door
        doorUpPatches.add(office.getPatch(52,87));
        // Data Collection Room Door
        doorUpPatches.add(office.getPatch(51,15));
        // Bet Human Experience & Data Collection Room Door
        doorUpPatches.add(office.getPatch(51,10));
        // Clinic Door (Right/Back)
        doorUpPatches.add(office.getPatch(56,144));
        // Open Area 1 Door
        doorUpPatches.add(office.getPatch(38,25));
        // Open Area 3 Door (RightMost)
        doorUpPatches.add(office.getPatch(38,66));
        DoorMapper.draw(doorUpPatches, "UP");

        List<Patch> doorDownPatches = new ArrayList<>();
        // Learning Area 1 Door (Leftmost)
        doorDownPatches.add(office.getPatch(29,31));
        // Learning Area 2 Door
        doorDownPatches.add(office.getPatch(29,47));
        // Learning Area 3 Door
        doorDownPatches.add(office.getPatch(29,63));
        // Learning Area 4 Door (RightMost)
        doorDownPatches.add(office.getPatch(29,79));
        // Control Center Door (Front/Down)
        doorDownPatches.add(office.getPatch(29,84));
        // Control Center Door (Back/Up)
        doorDownPatches.add(office.getPatch(17,92));
        // Database Center Door
        doorDownPatches.add(office.getPatch(29,108));
        // Men's Bathroom Door
        doorDownPatches.add(office.getPatch(11,135));
        // Open Area 2 Door (LeftMost)
        doorDownPatches.add(office.getPatch(44,34));
        // Open Area 4 Door
        doorDownPatches.add(office.getPatch(44,75));
        DoorMapper.draw(doorDownPatches, "DOWN");

        // Large Meeting Room Desk
        List<Patch> meetingDeskHorizontalPatches = new ArrayList<>();
        meetingDeskHorizontalPatches.add(office.getPatch(61,108));
        meetingDeskHorizontalPatches.add(office.getPatch(63,108));
        MeetingDeskMapper.draw(meetingDeskHorizontalPatches, "HORIZONTAL", 5);

        // Small Meeting Room Desk
        List<Patch> meetingDeskVerticalPatches = new ArrayList<>();
        meetingDeskVerticalPatches.add(office.getPatch(17,6));

        // Executive Meeting Table
        meetingDeskVerticalPatches.add(office.getPatch(62,144));
        MeetingDeskMapper.draw(meetingDeskVerticalPatches, "VERTICAL", 4);

        // Executive Dean's Office Desk
        List<Patch> horizontalOfficeDeskPatches = new ArrayList<>();
        horizontalOfficeDeskPatches.add(office.getPatch(72,136));
        OfficeDeskMapper.draw(horizontalOfficeDeskPatches, "HORIZONTAL", 2);

        // Research Area Office Desk
        List<Patch> verticalOfficeDeskPatches = new ArrayList<>();
        verticalOfficeDeskPatches.add(office.getPatch(57,30));
        verticalOfficeDeskPatches.add(office.getPatch(57,37));
        verticalOfficeDeskPatches.add(office.getPatch(57,49));
        verticalOfficeDeskPatches.add(office.getPatch(57,71));
        OfficeDeskMapper.draw(verticalOfficeDeskPatches, "VERTICAL", 3);

        List<Patch> verticalOfficeDeskPatches2 = new ArrayList<>();
        verticalOfficeDeskPatches2.add(office.getPatch(57,56));
        verticalOfficeDeskPatches2.add(office.getPatch(59,63));
        OfficeDeskMapper.draw(verticalOfficeDeskPatches2, "VERTICAL", 2);

        // Office Elevator (Spawn and Despawn point)
        List<Patch> officeGateEntranceExitPatches = new ArrayList<>();
        officeGateEntranceExitPatches.add(office.getPatch(13,145));
        officeGateEntranceExitPatches.add(office.getPatch(21,145));
        officeGateEntranceExitPatches.add(office.getPatch(29,145));
        OfficeGateMapper.draw(officeGateEntranceExitPatches, OfficeGate.OfficeGateMode.ENTRANCE_AND_EXIT);

        List<Patch> plantPatches = new ArrayList<>();
        // Reception Plants
        plantPatches.add(office.getPatch(36,133));
        plantPatches.add(office.getPatch(37,133));

        plantPatches.add(office.getPatch(29,1));
        plantPatches.add(office.getPatch(40,1));
        // Pantry Plants
        plantPatches.add(office.getPatch(81,103));
        plantPatches.add(office.getPatch(81,105));
        plantPatches.add(office.getPatch(81,107));
        plantPatches.add(office.getPatch(81,109));
        plantPatches.add(office.getPatch(81,111));
        plantPatches.add(office.getPatch(81,113));

        // Executive Dean's Plants
        plantPatches.add(office.getPatch(48,148));
        plantPatches.add(office.getPatch(50,148));
        plantPatches.add(office.getPatch(52,148));
        plantPatches.add(office.getPatch(54,148));
        PlantMapper.draw(plantPatches);

        List<Patch> printerPatches = new ArrayList<>();
        printerPatches.add(office.getPatch(28,97));
        PrinterMapper.draw(printerPatches);

        List<Patch> serverPatches = new ArrayList<>();
        serverPatches.add(office.getPatch(22,98));
        serverPatches.add(office.getPatch(25,103));
        ServerMapper.draw(serverPatches);

        List<Patch> receptionTablePatches = new ArrayList<>();
        // Reception Table
        receptionTablePatches.add(office.getPatch(39,126));
        ReceptionTableMapper.draw(receptionTablePatches);

        List<Patch> tableUpPatches = new ArrayList<>();
        // Pantry Table
        tableUpPatches.add(office.getPatch(74,130));

        // Management and Executive Secretary Table
        tableUpPatches.add(office.getPatch(38,101));
        tableUpPatches.add(office.getPatch(40,101));
        tableUpPatches.add(office.getPatch(38,103));
        tableUpPatches.add(office.getPatch(40,103));

        tableUpPatches.add(office.getPatch(45,101));
        tableUpPatches.add(office.getPatch(43,101));
        tableUpPatches.add(office.getPatch(45,103));
        tableUpPatches.add(office.getPatch(43,103));
        TableMapper.draw(tableUpPatches, "RIGHT");

        List<Patch> tableRightPatches = new ArrayList<>();
        // Pantry Tables
        tableRightPatches.add(office.getPatch(77,95));
        tableRightPatches.add(office.getPatch(77,105));
        tableRightPatches.add(office.getPatch(73,119));
        tableRightPatches.add(office.getPatch(78,119));

        // Management and Executive Secretary Table
        tableRightPatches.add(office.getPatch(37,100));
        tableRightPatches.add(office.getPatch(37,103));
        tableRightPatches.add(office.getPatch(47,100));
        tableRightPatches.add(office.getPatch(47,103));
        TableMapper.draw(tableRightPatches, "UP");

        // TRASH
        List<Patch> trashPatches = new ArrayList<>();
        trashPatches.add(office.getPatch(74,89));
        TrashMapper.draw(trashPatches);

        // Study Table
        List<Patch> studyTablePatches = new ArrayList<>();
        // Learning Area 1-4 Study Tables
        for (int i = 18; i < 24; i+=5) {
            for (int j = 22; j < 80; j += 16) {
                studyTablePatches.add(office.getPatch(i, j));
                studyTablePatches.add(office.getPatch(i, j + 8));
            }

        }
        StudyTableMapper.draw(studyTablePatches, "UP");

        // Toilet
        List<Patch> toiletPatches = new ArrayList<>();
        for (int j = 141; j < 148; j += 2) {
            // Men's Bathroom Toilet
            toiletPatches.add(office.getPatch(1,j));
            // Women's Bathroom Toilet
            toiletPatches.add(office.getPatch(44,j));
        }

        // Executive Dean's Toilet
        toiletPatches.add(office.getPatch(46,147));
        ToiletMapper.draw(toiletPatches);

        // Sink
        List<Patch> sinkPatches = new ArrayList<>();
        for (int j = 141; j < 148; j += 2) {
            // Men's Bathroom Sink
            sinkPatches.add(office.getPatch(10,j));

            // Women's Bathroom Sink
            sinkPatches.add(office.getPatch(35,j));
        }

        // Executive Dean's Sink
        sinkPatches.add(office.getPatch(46,144));

        // Pantry Sink
        sinkPatches.add(office.getPatch(70,92));
        SinkMapper.draw(sinkPatches);

        List<Patch> fridgePatches = new ArrayList<>();
        fridgePatches.add(office.getPatch(70,101));
        FridgeMapper.draw(fridgePatches);

        List<Patch> waterDispenserPatches = new ArrayList<>();
        waterDispenserPatches.add(office.getPatch(70,99));
        WaterDispenserMapper.draw(waterDispenserPatches);

        List<Patch> securityPatches = new ArrayList<>();
        securityPatches.add(office.getPatch(32,131));
        SecurityMapper.draw(securityPatches);
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