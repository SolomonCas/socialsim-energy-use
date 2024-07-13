package com.socialsim.controller.graphics;

import com.socialsim.controller.controls.Controller;
import com.socialsim.controller.graphics.agent.AgentGraphic;
import com.socialsim.controller.graphics.agent.AgentGraphicLocation;
import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.controller.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.model.core.agent.Agent;
import com.socialsim.model.core.environment.Environment;
import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchfield.*;
import com.socialsim.model.core.environment.patchobject.Amenity;
import com.socialsim.model.core.environment.patchobject.Drawable;
import com.socialsim.model.core.environment.patchobject.passable.NonObstacle;
import com.socialsim.model.core.environment.position.Coordinates;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class GraphicsController extends Controller {


    /* VARIABLES */
    private static final Image AMENITY_SPRITES = new Image(AmenityGraphic.AMENITIES_SPRITE_SHEET_URL);
    private static final Image AGENT_SPRITES = new Image(AgentGraphic.AGENTS_SPRITE_SHEET_URL);

    public static List<Amenity.AmenityBlock> firstPortalAmenityBlocks;
    public static double tileSize;
    public static boolean willPeek;
    private static long millisecondsLastCanvasRefresh;

    static {
        GraphicsController.firstPortalAmenityBlocks = null;
        GraphicsController.willPeek = false;
        millisecondsLastCanvasRefresh = 0;
    }

    /* METHODS */

    private static void clearCanvases(Environment environment, boolean background, GraphicsContext backgroundGraphicsContext, GraphicsContext foregroundGraphicsContext, double tileSize, double canvasWidth, double canvasHeight) {
        if (!background) {
            foregroundGraphicsContext.clearRect(0, 0, environment.getColumns() * tileSize, environment.getRows() * tileSize);
        }
        else {
            foregroundGraphicsContext.clearRect(0, 0, environment.getColumns() * tileSize, environment.getRows() * tileSize);
            backgroundGraphicsContext.setFill(Color.rgb(244, 244, 244));
            backgroundGraphicsContext.fillRect(0, 0, canvasWidth, canvasHeight);
        }
    }

    public static void requestDrawView(StackPane canvases, Environment environment, double tileSize, boolean background, boolean speedAware) {
        if (speedAware) {
            final int millisecondsIntervalBetweenCalls = 2000;
            long currentTimeMilliseconds = System.currentTimeMillis();

            if (currentTimeMilliseconds - millisecondsLastCanvasRefresh < millisecondsIntervalBetweenCalls) {
                return;
            }
            else {
                millisecondsLastCanvasRefresh = System.currentTimeMillis();
            }
        }

        Platform.runLater(() -> drawView(canvases, environment, tileSize, background));
    }

    private static void drawView(StackPane canvases, Environment environment, double tileSize, boolean background) {
        final Canvas backgroundCanvas = (Canvas) canvases.getChildren().get(0);
        final Canvas foregroundCanvas = (Canvas) canvases.getChildren().get(1);

        final GraphicsContext backgroundGraphicsContext = backgroundCanvas.getGraphicsContext2D();
        final GraphicsContext foregroundGraphicsContext = foregroundCanvas.getGraphicsContext2D();

        final double canvasWidth = backgroundCanvas.getWidth();
        final double canvasHeight = backgroundCanvas.getHeight();

        clearCanvases(environment, background, backgroundGraphicsContext, foregroundGraphicsContext, tileSize, canvasWidth, canvasHeight);
        drawObjects(environment, background, backgroundGraphicsContext, foregroundGraphicsContext, tileSize);
    }

    private static void drawObjects(Environment environment, boolean background, GraphicsContext backgroundGraphicsContext, GraphicsContext foregroundGraphicsContext, double tileSize) {

        List<Patch> patches;

        if (background) {
            patches = Arrays.stream(environment.getPatches()).flatMap(Arrays::stream).collect(Collectors.toList());
        }
        else {
            SortedSet<Patch> amenityAgentSet = new TreeSet<>();
            amenityAgentSet.addAll(new ArrayList<>(environment.getAmenityPatchSet()));
            amenityAgentSet.addAll(new ArrayList<>(environment.getAgentPatchSet()));
            patches = new ArrayList<>(amenityAgentSet);
        }


        for (Patch patch : patches) {
            if (patch == null) {
                continue;
            }

            int row = patch.getMatrixPosition().getRow();
            int column = patch.getMatrixPosition().getColumn();
            Patch currentPatch = environment.getPatch(row, column);

            boolean drawGraphicTransparently;
            drawGraphicTransparently = false;

            Amenity.AmenityBlock patchAmenityBlock = currentPatch.getAmenityBlock();
            Pair<PatchField, String> patchStrPair = currentPatch.getPatchField();
            Color patchColor;



            // If patch is empty/null, set to white
            if (patchAmenityBlock == null) {
                patchColor = Color.rgb(255,255,255);
                backgroundGraphicsContext.setFill(patchColor);
                backgroundGraphicsContext.fillRect(column * tileSize, row * tileSize, tileSize, tileSize);
            }
            else {

                // Otherwise, draw
                Amenity patchAmenity = currentPatch.getAmenityBlock().getParent();

                if (patchAmenityBlock.hasGraphic()) {
                    Drawable drawablePatchAmenity = (Drawable) patchAmenity;
                    if (patchAmenity instanceof NonObstacle) {
                        if (!((NonObstacle) patchAmenity).isEnabled()) {
                            drawGraphicTransparently = true;
                        }
                    }

                    if (drawGraphicTransparently) {
                        foregroundGraphicsContext.setGlobalAlpha(0.2);
                    }

                    // System.out.println("PRINT drawablePatchAmenity: " + drawablePatchAmenity);
                    AmenityGraphicLocation amenityGraphicLocation = drawablePatchAmenity.getGraphicLocation();

                    foregroundGraphicsContext.drawImage(
                            AMENITY_SPRITES,
                            amenityGraphicLocation.getSourceX(), amenityGraphicLocation.getSourceY(),
                            amenityGraphicLocation.getSourceWidth(), amenityGraphicLocation.getSourceHeight(),
                            column * tileSize + ((AmenityGraphic) drawablePatchAmenity. getGraphicObject()).getAmenityGraphicOffset().getColumnOffset() * tileSize,
                            row * tileSize + ((AmenityGraphic) drawablePatchAmenity.getGraphicObject()).getAmenityGraphicOffset().getRowOffset() * tileSize,
                            tileSize * ((AmenityGraphic) drawablePatchAmenity.getGraphicObject()).getAmenityGraphicScale().getColumnSpan(),
                            tileSize * ((AmenityGraphic) drawablePatchAmenity.getGraphicObject()).getAmenityGraphicScale().getRowSpan()
                    );

                    if (drawGraphicTransparently) {
                        foregroundGraphicsContext.setGlobalAlpha(1.0);
                    }

                }
            }




            // PATCH COLORS
            if (patchStrPair != null) {
                PatchField patchPatchField = patchStrPair.getKey();

                /* Floor */
                if (patchPatchField.getClass() == Floor.class) {

                    // Floor
                    if (patchStrPair.getValue().isEmpty()) {
                        patchColor = Color.rgb(132, 132, 132);
                    }

                    // Dim
                    else if (patchStrPair.getValue().equals("dimFloor")){
                        patchColor = Color.rgb(110,110,110);
                    }

                    else {
                        throw new IllegalArgumentException("Unexpected patchStrPair: " + patchStrPair);
                    }

                }

                /* Elevator Lobby */
                else if (patchPatchField.getClass() == ElevatorLobby.class) {

                    // Elevator Lobby
                    if (patchStrPair.getValue().isEmpty()) {
                        patchColor = Color.rgb(104, 104, 104);
                    }

                    else {
                        throw new IllegalArgumentException("Unexpected patchStrPair: " + patchStrPair);
                    }

                }

                /* Divider: Walls (and its top view), Office Exterior  */
                else if (patchPatchField.getClass() == Divider.class) {

                    // For divider patches that stay consistent (final) throughout all layouts
                    // This includes: (1) Outline of office (2) pillars inside office
                    if (patchStrPair.getValue().equals("permanentWallTop")) {
                        patchColor = Color.rgb(0,0,0);
                    } else if (patchStrPair.getValue().equals("permanentWall")) {
                        patchColor = Color.rgb(32,32,32);

                    // For divider patches inside office
                    } else if (patchStrPair.getValue().equals("wallTop")) {
                        patchColor = Color.rgb(38,0,39);
                    } else if (patchStrPair.getValue().equals("wall")) {
                        patchColor = Color.rgb(54,35,54);
                    } else if (patchStrPair.getValue().equals("doorPatch")) {
                        patchColor = Color.rgb(60,60,60);

                    // For divider patches outside office
                    } else if (patchStrPair.getValue().equals("officeNextDoor")) {
                        patchColor = Color.rgb(9,9,21);
                    } else if (patchStrPair.getValue().equals("parkingLot")) {
                        patchColor = Color.rgb(168,162,139);

                    // else, throw error
                    } else {
                        throw new IllegalArgumentException("Unexpected patchStrPair: " + patchStrPair);
                    }
                }

                /* Bathroom */
                else if (patchPatchField.getClass() == Bathroom.class) {

                    // Male bathroom outside office
                    if (patchStrPair.getValue().equals("maleBathroom")) {
                        patchColor = Color.rgb(189, 234, 254);

                    // Female bathroom outside office
                    } else if (patchStrPair.getValue().equals("femaleBathroom")) {
                        patchColor = Color.rgb(230, 169, 180);

                    // Director bathroom
                    } else if (patchStrPair.getValue().equals("directorBathroom")) {
                        patchColor = Color.rgb(196, 201, 203);

                    // Dim
                    } else if (patchStrPair.getValue().equals("dimDirectorBathroom")){
                            patchColor = Color.rgb(119, 114, 119);

                    // else, throw error
                    } else {
                        throw new IllegalArgumentException("Unexpected patchStrPair: " + patchStrPair);
                    }
                }

                /* Reception */
                else if (patchPatchField.getClass() == Reception.class) {

                    // Reception
                    if (patchStrPair.getValue().isEmpty()) {
                        patchColor = Color.rgb(189, 182, 205);
                    }

                    // Dim
                    else if (patchStrPair.getValue().equals("dimReception")){
                        patchColor = Color.rgb(108, 103, 118);
                    }

                    else {
                        throw new IllegalArgumentException("Unexpected patchStrPair: " + patchStrPair);
                    }
                }

                /* Solo Room */
                else if (patchPatchField.getClass() == SoloRoom.class) {

                    // Solo Room 1
                    if (patchStrPair.getValue().equals("SR1")) {
                        patchColor = Color.rgb(220, 212, 233);

                    // DIM Solo Room 1
                    } else if (patchStrPair.getValue().equals("dimSR1")) {
                        patchColor = Color.rgb(127, 122, 136);

                    // Solo Room 2
                    } else if (patchStrPair.getValue().equals("SR2")) {
                        patchColor = Color.rgb(133, 161, 163);

                    // DIM Solo Room 2
                    } else if (patchStrPair.getValue().equals("dimSR2")) {
                        patchColor = Color.rgb(98, 115, 117);

                    // Solo Room 3
                    } else if (patchStrPair.getValue().equals("SR3")) {
                        patchColor = Color.rgb(190, 196, 217);

                    // DIM Solo Room 3
                    } else if (patchStrPair.getValue().equals("dimSR3")) {
                        patchColor = Color.rgb(109, 112, 126);

                    // Solo Room 4
                    } else if (patchStrPair.getValue().equals("SR4")) {
                        patchColor = Color.rgb(223, 196, 217);

                    // DIM Solo Room 4
                    } else if (patchStrPair.getValue().equals("dimSR4")) {
                        patchColor = Color.rgb(129, 112, 126);

                    } else {
                        throw new IllegalArgumentException("Unexpected patchStrPair: " + patchStrPair);
                    }
                }

                /* Data Center */
                else if (patchPatchField.getClass() == DataCenter.class) {

                    // Data Center
                    if (patchStrPair.getValue().equals("dataCenter")) {
                        patchColor = Color.rgb(234, 210, 213);
                    }

                    // Dim Data Center
                    else if (patchStrPair.getValue().equals("dimDataCenter")) {
                        patchColor = Color.rgb(137, 121, 123);
                    }

                    // CCTV Area
                    else if (patchStrPair.getValue().equals("CCTV")) {
                        patchColor = Color.rgb(229, 215, 236);
                    }

                    // Dim CCTV Area
                    else if (patchStrPair.getValue().equals("dimCCTV")) {
                        patchColor = Color.rgb(139, 124, 137);
                    }

                    else {
                        throw new IllegalArgumentException("Unexpected patchStrPair: " + patchStrPair);
                    }
                }

                /* Control Center */
                else if (patchPatchField.getClass() == ControlCenter.class) {

                    // Control Center
                    if (patchStrPair.getValue().isEmpty()) {
                        patchColor = Color.rgb(157, 162, 164);
                    }

                    // Dim
                    else if (patchStrPair.getValue().equals("dimControlCenter")){
                        patchColor = Color.rgb(88, 91, 92);
                    }

                    else {
                        throw new IllegalArgumentException("Unexpected patchStrPair: " + patchStrPair);
                    }
                }

                /* Learning Space */
                else if (patchPatchField.getClass() == LearningSpace.class) {

                    // Learning Space 1
                    if (patchStrPair.getValue().equals("LS1")) {
                        patchColor = Color.rgb(214, 224, 232);

                    // DIM Learning Space 1
                    } else if (patchStrPair.getValue().equals("dimLS1")) {
                        patchColor = Color.rgb(120, 130, 135);

                    // Learning Space 2
                    } else if (patchStrPair.getValue().equals("LS2")) {
                        patchColor = Color.rgb(234, 219, 239);

                    // DIM Learning Space 2
                    } else if (patchStrPair.getValue().equals("dimLS2")) {
                        patchColor = Color.rgb(136, 127, 140);

                    // Learning Space 3
                    } else if (patchStrPair.getValue().equals("LS3")) {
                        patchColor = Color.rgb(229, 218, 229);

                    // DIM Learning Space 3
                    } else if (patchStrPair.getValue().equals("dimLS3")) {
                        patchColor = Color.rgb(133, 126, 133);

                    // Learning Space 4
                    } else if (patchStrPair.getValue().equals("LS4")) {
                        patchColor = Color.rgb(208, 214, 220);

                    // DIM Learning Space 4
                    } else if (patchStrPair.getValue().equals("dimLS4")) {
                        patchColor = Color.rgb(120, 123, 128);

                    } else {
                        throw new IllegalArgumentException("Unexpected patchStrPair: " + patchStrPair);
                    }
                }

                /* Breaker Room */
                else if (patchPatchField.getClass() == BreakerRoom.class) {

                    // Control Center
                    if (patchStrPair.getValue().isEmpty()) {
                        patchColor = Color.rgb(111, 156, 166);
                    }

                    // Dim
                    else if (patchStrPair.getValue().equals("dimBreakerRoom")){
                        patchColor = Color.rgb(99, 127, 134);
                    }

                    else {
                        throw new IllegalArgumentException("Unexpected patchStrPair: " + patchStrPair);
                    }
                }

                /* Meeting Room */
                else if (patchPatchField.getClass() == MeetingRoom.class) {

                    // Meeting Room
                    if (patchStrPair.getValue().isEmpty()) {
                        patchColor = Color.rgb(233, 212, 220);
                    }

                    // Dim
                    else if (patchStrPair.getValue().equals("dimMeetingRoom")){
                        patchColor = Color.rgb(136, 122, 128);
                    }

                    else {
                        throw new IllegalArgumentException("Unexpected patchStrPair: " + patchStrPair);
                    }
                }

                /* Conference Room */
                else if (patchPatchField.getClass() == ConferenceRoom.class) {

                    // Conference Room
                    if (patchStrPair.getValue().isEmpty()) {
                        patchColor = Color.rgb(235, 230, 251);
                    }

                    // Dim
                    else if (patchStrPair.getValue().equals("dimConferenceRoom")){
                        patchColor = Color.rgb(137, 134, 147);
                    }

                    else {
                        throw new IllegalArgumentException("Unexpected patchStrPair: " + patchStrPair);
                    }
                }

                /* Storage Room */
                else if (patchPatchField.getClass() == StorageRoom.class) {

                    // Storage Room
                    if (patchStrPair.getValue().isEmpty()) {
                        patchColor = Color.rgb(219, 203, 221);
                    }

                    // Dim
                    else if (patchStrPair.getValue().equals("dimStorageRoom")){
                        patchColor = Color.rgb(127, 116, 128);
                    }

                    else {
                        throw new IllegalArgumentException("Unexpected patchStrPair: " + patchStrPair);
                    }
                }

                /* Clinic */
                else if (patchPatchField.getClass() == Clinic.class) {

                    // Clinic
                    if (patchStrPair.getValue().isEmpty()) {
                        patchColor = Color.rgb(179, 199, 220);
                    }

                    // Dim
                    else if (patchStrPair.getValue().equals("dimClinic")){
                        patchColor = Color.rgb(102, 114, 128);
                    }

                    else {
                        throw new IllegalArgumentException("Unexpected patchStrPair: " + patchStrPair);
                    }
                }

                /* Faculty Room */
                else if (patchPatchField.getClass() == FacultyRoom.class) {

                    // Faculty Room
                    if (patchStrPair.getValue().isEmpty()) {
                        patchColor = Color.rgb(224, 238, 233);
                    }

                    // Dim
                    else if (patchStrPair.getValue().equals("dimFacultyRoom")){
                        patchColor = Color.rgb(130, 139, 136);
                    }

                    else {
                        throw new IllegalArgumentException("Unexpected patchStrPair: " + patchStrPair);
                    }
                }

                /* Research Center */
                else if (patchPatchField.getClass() == ResearchCenter.class) {

                    // Research Center
                    if (patchStrPair.getValue().isEmpty()) {
                        patchColor = Color.rgb(217, 198, 213);
                    }

                    // Dim
                    else if (patchStrPair.getValue().equals("dimResearchCenter")){
                        patchColor = Color.rgb(126, 113, 113);
                    }

                    else {
                        throw new IllegalArgumentException("Unexpected patchStrPair: " + patchStrPair);
                    }
                }

                /* MESA */
                else if (patchPatchField.getClass() == MESA.class) {

                    // MESA
                    if (patchStrPair.getValue().isEmpty()) {
                        patchColor = Color.rgb(209, 191, 213);
                    }

                    // Dim
                    else if (patchStrPair.getValue().equals("dimMESA")){
                        patchColor = Color.rgb(121, 109, 123);
                    }

                    else {
                        throw new IllegalArgumentException("Unexpected patchStrPair: " + patchStrPair);
                    }
                }

                /* Data Collection Room */
                else if (patchPatchField.getClass() == DataCollectionRoom.class) {

                    // Data Collection
                    if (patchStrPair.getValue().isEmpty()) {
                        patchColor = Color.rgb(232, 231, 244);
                    }

                    // Dim
                    else if (patchStrPair.getValue().equals("dimDataCollRoom")){
                        patchColor = Color.rgb(135, 134, 143);
                    }

                    else {
                        throw new IllegalArgumentException("Unexpected patchStrPair: " + patchStrPair);
                    }
                }

                /* Human Experience Room */
                else if (patchPatchField.getClass() == HumanExpRoom.class) {

                    // Human Experience Room
                    if (patchStrPair.getValue().isEmpty()) {
                        patchColor = Color.rgb(241, 223, 255);
                    }

                    // Dim
                    else if (patchStrPair.getValue().equals("dimHumExpRoom")){
                        patchColor = Color.rgb(101, 89, 110);
                    }

                    else {
                        throw new IllegalArgumentException("Unexpected patchStrPair: " + patchStrPair);
                    }
                }

                /* Director Room */
                else if (patchPatchField.getClass() == DirectorRoom.class) {

                    // Director Room
                    if (patchStrPair.getValue().isEmpty()) {
                        patchColor = Color.rgb(206, 198, 207);
                    }

                    // Dim
                    else if (patchStrPair.getValue().equals("dimDirectorRoom")){
                        patchColor = Color.rgb(119, 113, 119);
                    }

                    else {
                        throw new IllegalArgumentException("Unexpected patchStrPair: " + patchStrPair);
                    }
                }

                /* Pantry */
                else if (patchPatchField.getClass() == Pantry.class) {

                    // Pantry
                    if (patchStrPair.getValue().isEmpty()) {
                        patchColor = Color.rgb(220, 216, 208);
                    }

                    // Dim
                    else if (patchStrPair.getValue().equals("dimPantry")){
                        patchColor = Color.rgb(127, 125, 120);
                    }

                    else {
                        throw new IllegalArgumentException("Unexpected patchStrPair: " + patchStrPair);
                    }
                }

                // else, throw error
                else {
                    throw new IllegalArgumentException("Unknown PatchField class, check GraphicsController.");
                }

                backgroundGraphicsContext.setFill(patchColor);
                backgroundGraphicsContext.fillRect(column * tileSize, row * tileSize, tileSize, tileSize);
                backgroundGraphicsContext.setStroke(patchColor);
                backgroundGraphicsContext.setLineWidth(1);
                backgroundGraphicsContext.strokeRect(column * tileSize, row * tileSize, tileSize, tileSize);

                // INSERT AGENT CODE
                if (!background) {
                    if (!patch.getAgents().isEmpty()) {
                        for (Agent agent : patch.getAgents()) {
                            Agent officeAgent = agent;
                            AgentGraphicLocation agentGraphicLocation = officeAgent.getAgentGraphic().getGraphicLocation();

                            Image CURRENT_URL = AGENT_SPRITES;

                            foregroundGraphicsContext.drawImage(
                                    CURRENT_URL,
                                    agentGraphicLocation.getSourceX(), agentGraphicLocation.getSourceY(),
                                    agentGraphicLocation.getSourceWidth(), agentGraphicLocation.getSourceHeight(),
                                    column * tileSize,
                                    row * tileSize,
                                    tileSize, tileSize);
                        }
                    }
                }

            }
        }
    }



    /* GETTERS */
    public static Coordinates getScaledAgentCoordinates(Agent agent) {
        Coordinates agentPosition = agent.getAgentMovement().getPosition();

        return GraphicsController.getScaledCoordinates(agentPosition);
    }

    public static Coordinates getScaledCoordinates(Coordinates coordinates) {
        return new Coordinates(coordinates.getX() / Patch.PATCH_SIZE_IN_SQUARE_METERS, coordinates.getY() / Patch.PATCH_SIZE_IN_SQUARE_METERS);
    }
}