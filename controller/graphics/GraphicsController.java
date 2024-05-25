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
import com.socialsim.model.core.environment.patchobject.passable.gate.Gate;
import com.socialsim.model.core.environment.patchobject.passable.goal.*;
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


    // VARIABLES
    private static final Image AMENITY_SPRITES = new Image(AmenityGraphic.AMENITY_SPRITE_SHEET_URL);
    private static final Image AMENITY_SPRITES2 = new Image(AmenityGraphic.AMENITY_SPRITE_SHEET_URL2);
    private static final Image AMENITY_SPRITES3 = new Image(AmenityGraphic.AMENITY_SPRITE_SHEET_URL3);
    private static final Image AGENT_SPRITES1 = new Image(AgentGraphic.AGENTS_URL_1);
    private static final Image AGENT_SPRITES2 = new Image(AgentGraphic.AGENTS_URL_2);
    private static final Image AGENT_SPRITES3 = new Image(AgentGraphic.AGENTS_URL_3);
    private static final Image AGENT_SPRITES4 = new Image(AgentGraphic.AGENTS_URL_4);

    public static List<Amenity.AmenityBlock> firstPortalAmenityBlocks;
    public static double tileSize;
    public static boolean willPeek;
    private static long millisecondsLastCanvasRefresh;

    static {
        GraphicsController.firstPortalAmenityBlocks = null;
        GraphicsController.willPeek = false;
        millisecondsLastCanvasRefresh = 0;
    }

    // CONSTRUCTOR



    // METHODS

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

        Platform.runLater(() -> {
            drawView(canvases, environment, tileSize, background);
        });
    }

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
            Pair<PatchField, String> patchNumPair = currentPatch.getPatchField();
            Color patchColor;



            // PATCH AMENITY BLOCK
            if (patchAmenityBlock == null) {
                patchColor = Color.rgb(244, 244, 244);
                backgroundGraphicsContext.setFill(patchColor);
                backgroundGraphicsContext.fillRect(column * tileSize, row * tileSize, tileSize, tileSize);
            }
            else {

                // VARIABLES
                Amenity patchAmenity = currentPatch.getAmenityBlock().getParent();

                if (patchAmenityBlock.hasGraphic()) {

                    // VARIABLES
                    Drawable drawablePatchAmenity = (Drawable) patchAmenity;
                    if (patchAmenity instanceof NonObstacle) {
                        if (!((NonObstacle) patchAmenity).isEnabled()) {
                            drawGraphicTransparently = true;
                        }
                    }

                    if (drawGraphicTransparently) {
                        foregroundGraphicsContext.setGlobalAlpha(0.2);
                    }

                    AmenityGraphicLocation amenityGraphicLocation = drawablePatchAmenity.getGraphicLocation();

                    // IF STATEMENTS
                    if (patchAmenity.getClass() == Gate.class) {
                        foregroundGraphicsContext.drawImage(
                                AMENITY_SPRITES3,
                                amenityGraphicLocation.getSourceX(), amenityGraphicLocation.getSourceY(),
                                amenityGraphicLocation.getSourceWidth(), amenityGraphicLocation.getSourceHeight(),
                                column * tileSize + ((AmenityGraphic) drawablePatchAmenity. getGraphicObject()).getAmenityGraphicOffset().getColumnOffset() * tileSize,
                                row * tileSize + ((AmenityGraphic) drawablePatchAmenity.getGraphicObject()).getAmenityGraphicOffset().getRowOffset() * tileSize,
                                tileSize * ((AmenityGraphic) drawablePatchAmenity.getGraphicObject()).getAmenityGraphicScale().getColumnSpan(),
                                tileSize * ((AmenityGraphic) drawablePatchAmenity.getGraphicObject()).getAmenityGraphicScale().getRowSpan());
                    }
                    else if (   patchAmenity.getClass() == Toilet.class || patchAmenity.getClass() == Sink.class ||
                            patchAmenity.getClass() == Trash.class || patchAmenity.getClass() == OfficeToilet.class ||
                            patchAmenity.getClass() == OfficeSink.class) {
                        foregroundGraphicsContext.drawImage(
                                AMENITY_SPRITES2,
                                amenityGraphicLocation.getSourceX(), amenityGraphicLocation.getSourceY(),
                                amenityGraphicLocation.getSourceWidth(), amenityGraphicLocation.getSourceHeight(),
                                column * tileSize + ((AmenityGraphic) drawablePatchAmenity. getGraphicObject()).getAmenityGraphicOffset().getColumnOffset() * tileSize,
                                row * tileSize + ((AmenityGraphic) drawablePatchAmenity.getGraphicObject()).getAmenityGraphicOffset().getRowOffset() * tileSize,
                                tileSize * ((AmenityGraphic) drawablePatchAmenity.getGraphicObject()).getAmenityGraphicScale().getColumnSpan(),
                                tileSize * ((AmenityGraphic) drawablePatchAmenity.getGraphicObject()).getAmenityGraphicScale().getRowSpan());
                    }
                    else {
                        foregroundGraphicsContext.drawImage(
                                AMENITY_SPRITES,
                                amenityGraphicLocation.getSourceX(), amenityGraphicLocation.getSourceY(),
                                amenityGraphicLocation.getSourceWidth(), amenityGraphicLocation.getSourceHeight(),
                                column * tileSize + ((AmenityGraphic) drawablePatchAmenity. getGraphicObject()).getAmenityGraphicOffset().getColumnOffset() * tileSize,
                                row * tileSize + ((AmenityGraphic) drawablePatchAmenity.getGraphicObject()).getAmenityGraphicOffset().getRowOffset() * tileSize,
                                tileSize * ((AmenityGraphic) drawablePatchAmenity.getGraphicObject()).getAmenityGraphicScale().getColumnSpan(),
                                tileSize * ((AmenityGraphic) drawablePatchAmenity.getGraphicObject()).getAmenityGraphicScale().getRowSpan());
                    }




                    if (drawGraphicTransparently) {
                        foregroundGraphicsContext.setGlobalAlpha(1.0);
                    }

                }
            }




            // PATCH COLORS
            if (patchNumPair != null) {
                PatchField patchPatchField = patchNumPair.getKey();


                /*** WALLS ***/
                if (patchPatchField.getClass() == Wall.class) {


                    // OFFICE OUTLINE

                    // Top of Wall
                    if (patchNumPair.getValue().toString().equals("outlineWallTop")) {
                        patchColor = Color.rgb(0, 0, 0);

                        // Wall
                    } else if (patchNumPair.getValue().toString().equals("outlineWall")) {
                        patchColor = Color.rgb(32, 32, 32);

                        // INSIDE OFFICE

                        // Top of Wall
                    } else if (patchNumPair.getValue().toString().equals("WallTopIn")) {
                        patchColor = Color.rgb(11, 13, 62);

                        // Wall
                    } else if (patchNumPair.getValue().toString().equals("wallIn")) {
                        patchColor = Color.rgb(50, 50, 67);

                        // Wall (signifies entry point)
                    } else if (patchNumPair.getValue().toString().equals("doorWallIn")) {
                        patchColor = Color.rgb(119, 129, 144);


                        // OUTSIDE OFFICE

                        // Top of Wall
                    } else if (patchNumPair.getValue().toString().equals("WallTopOut")) {
                        patchColor = Color.rgb(16, 19, 81);

                        // Wall
                    } else if (patchNumPair.getValue().toString().equals("wallOut")) {
                        patchColor = Color.rgb(35, 66, 125);

                        // Wall (signifies entry point)
                    } else if (patchNumPair.getValue().toString().equals("doorWallOut")) {
                        patchColor = Color.rgb(89, 134, 187);


                        // Outside of Building (NOT ACTUALLY A WALL)
                    } else {
                        patchColor = Color.rgb(83, 99, 130);
                    }

                    backgroundGraphicsContext.setFill(patchColor);
                    backgroundGraphicsContext.setStroke(null);
                    backgroundGraphicsContext.fillRect(column * tileSize, row * tileSize, tileSize, tileSize);
                }


                /*** BATHROOM ***/
                else if (patchPatchField.getClass() == Bathroom.class) {

                    // Male Bathroom
                    if (patchNumPair.getValue().toString().equals("male")) {
                        patchColor = Color.rgb(189, 234, 254);

                        // Female Bathroom
                    } else if (patchNumPair.getValue().toString().equals("female")) {
                        patchColor = Color.rgb(233, 127, 146);


                        // Director's Bathroom
                    } else {
                        patchColor = Color.rgb(136, 158, 152);
                    }


                    backgroundGraphicsContext.setFill(patchColor);
                    backgroundGraphicsContext.fillRect(column * tileSize, row * tileSize, tileSize, tileSize);
                }


                /*** RECEPTION ***/
                else if (patchPatchField.getClass() == Reception.class) {
                    patchColor = Color.rgb(253, 238, 208);
                    backgroundGraphicsContext.setFill(patchColor);
                    backgroundGraphicsContext.fillRect(column * tileSize, row * tileSize, tileSize, tileSize);
                }


                /*** ELEVATOR LOBBY ***/

                else if (patchPatchField.getClass() == Floor.class) {
                    patchColor = Color.rgb(244, 244, 244);
                    backgroundGraphicsContext.setFill(patchColor);
                    backgroundGraphicsContext.fillRect(column * tileSize, row * tileSize, tileSize, tileSize);

                }

                /*** STAFF AREA ***/
                else if (patchPatchField.getClass() == StaffArea.class) {
                    patchColor = Color.rgb(255, 231, 203);
                    backgroundGraphicsContext.setFill(patchColor);
                    backgroundGraphicsContext.fillRect(column * tileSize, row * tileSize, tileSize, tileSize);
                }

                /*** SOLO ROOMS ***/
                else if (patchPatchField.getClass() == SoloRoom.class) {

                    // Solo Room 1
                    if (patchNumPair.getValue().toString().equals("SR1")) {
                        patchColor = Color.rgb(231, 175, 209);

                        // Solo Room 2
                    } else if (patchNumPair.getValue().toString().equals("SR2")) {
                        patchColor = Color.rgb(219, 192, 246);

                        // Solo Room 3
                    } else if (patchNumPair.getValue().toString().equals("SR3")) {
                        patchColor = Color.rgb(163, 173, 208);

                        // Solo Room 4
                    } else {
                        patchColor = Color.rgb(182, 172, 243);
                    }

                    backgroundGraphicsContext.setFill(patchColor);
                    backgroundGraphicsContext.fillRect(column * tileSize, row * tileSize, tileSize, tileSize);
                }


                /*** DATA CENTER ***/
                else if (patchPatchField.getClass() == DataCenter.class) {
                    patchColor = Color.rgb(235, 223, 214);
                    backgroundGraphicsContext.setFill(patchColor);
                    backgroundGraphicsContext.fillRect(column * tileSize, row * tileSize, tileSize, tileSize);
                }

                /*** CONTROL CENTER ***/
                else if (patchPatchField.getClass() == ControlCenter.class) {
                    patchColor = Color.rgb(215, 195, 181);
                    backgroundGraphicsContext.setFill(patchColor);
                    backgroundGraphicsContext.fillRect(column * tileSize, row * tileSize, tileSize, tileSize);
                }

                /*** LEARNING SPACES ***/
                else if (patchPatchField.getClass() == LearningSpace.class) {

                    // Learning Space 1
                    if (patchNumPair.getValue().toString().equals("LS1")) {
                        patchColor = Color.rgb(206, 223, 214);

                        // Learning Space 2
                    } else if (patchNumPair.getValue().toString().equals("LS2")) {
                        patchColor = Color.rgb(221, 210, 224);

                        // Learning Space 3
                    } else if (patchNumPair.getValue().toString().equals("LS3")) {
                        patchColor = Color.rgb(237, 213, 213);

                        // Learning Space 4
                    } else if (patchNumPair.getValue().toString().equals("LS4")) {
                        patchColor = Color.rgb(194, 208, 221);

                        // Glass in between
                    } else {
                        patchColor = Color.rgb(159, 174, 195);
                    }

                    backgroundGraphicsContext.setFill(patchColor);
                    backgroundGraphicsContext.fillRect(column * tileSize, row * tileSize, tileSize, tileSize);
                }

                /*** MEETING ROOM ***/
                else if (patchPatchField.getClass() == MeetingRoom.class) {
                    patchColor = Color.rgb(233, 212, 220);
                    backgroundGraphicsContext.setFill(patchColor);
                    backgroundGraphicsContext.fillRect(column * tileSize, row * tileSize, tileSize, tileSize);
                }

                /*** CONFERENCE ROOM ***/
                else if (patchPatchField.getClass() == ConferenceRoom.class) {
                    patchColor = Color.rgb(235, 230, 251);
                    backgroundGraphicsContext.setFill(patchColor);
                    backgroundGraphicsContext.fillRect(column * tileSize, row * tileSize, tileSize, tileSize);
                }

                /*** STORAGE ROOM ***/
                else if (patchPatchField.getClass() == StorageRoom.class) {
                    patchColor = Color.rgb(178, 159, 141);
                    backgroundGraphicsContext.setFill(patchColor);
                    backgroundGraphicsContext.fillRect(column * tileSize, row * tileSize, tileSize, tileSize);
                }


                /*** FACULTY ROOM ***/
                else if (patchPatchField.getClass() == FacultyRoom.class) {
                    patchColor = Color.rgb(218, 224, 238);
                    backgroundGraphicsContext.setFill(patchColor);
                    backgroundGraphicsContext.fillRect(column * tileSize, row * tileSize, tileSize, tileSize);
                }

                /*** RESEARCH CENTER ***/
                else if (patchPatchField.getClass() == ResearchCenter.class) {
                    patchColor = Color.rgb(182, 206, 199);
                    backgroundGraphicsContext.setFill(patchColor);
                    backgroundGraphicsContext.fillRect(column * tileSize, row * tileSize, tileSize, tileSize);
                }

                /*** DATA COLLECTION ***/
                else if (patchPatchField.getClass() == DataCollectionRoom.class) {
                    patchColor = Color.rgb(220, 203, 218);
                    backgroundGraphicsContext.setFill(patchColor);
                    backgroundGraphicsContext.fillRect(column * tileSize, row * tileSize, tileSize, tileSize);
                }

                /*** HUMAN EXPERIENCE ***/
                else if (patchPatchField.getClass() == HumanExpRoom.class) {
                    patchColor = Color.rgb(216, 222, 255);
                    backgroundGraphicsContext.setFill(patchColor);
                    backgroundGraphicsContext.fillRect(column * tileSize, row * tileSize, tileSize, tileSize);
                }

                /*** CLINIC ***/
                else if (patchPatchField.getClass() == Clinic.class) {
                    patchColor = Color.rgb(157, 160, 163);
                    backgroundGraphicsContext.setFill(patchColor);
                    backgroundGraphicsContext.fillRect(column * tileSize, row * tileSize, tileSize, tileSize);
                }


                /*** DIRECTOR'S ROOM ***/
                else if (patchPatchField.getClass() == DirectorRoom.class) {
                    patchColor = Color.rgb(136, 168, 172);
                    backgroundGraphicsContext.setFill(patchColor);
                    backgroundGraphicsContext.fillRect(column * tileSize, row * tileSize, tileSize, tileSize);
                }

                /*** PANTRY ***/
                else if (patchPatchField.getClass() == Pantry.class) {
                    patchColor = Color.rgb(240, 234, 223);
                    backgroundGraphicsContext.setFill(patchColor);
                    backgroundGraphicsContext.fillRect(column * tileSize, row * tileSize, tileSize, tileSize);
                }

                // INSERT AGENT CODE
                if (!background) {
                    if (!patch.getAgents().isEmpty()) {
                        for (Agent agent : patch.getAgents()) {
                            Agent officeAgent = agent;
                            AgentGraphicLocation agentGraphicLocation = officeAgent.getAgentGraphic().getGraphicLocation();

                            Image CURRENT_URL = null;
                            if (agent.getType() == Agent.Type.GUARD || agent.getType() == Agent.Type.MAINTENANCE) {
                                CURRENT_URL = AGENT_SPRITES1;
                            }
                            else if (agent.getType() == Agent.Type.DIRECTOR || agent.getType() == Agent.Type.STUDENT) {
                                CURRENT_URL = AGENT_SPRITES2;
                            }
                            else if (agent.getType() == Agent.Type.FACULTY) {
                                CURRENT_URL = AGENT_SPRITES3;
                            }

                            foregroundGraphicsContext.drawImage(
                                    CURRENT_URL,
                                    agentGraphicLocation.getSourceX(), agentGraphicLocation.getSourceY(),
                                    agentGraphicLocation.getSourceWidth(), agentGraphicLocation.getSourceHeight(),
                                    getScaledAgentCoordinates(officeAgent).getX() * tileSize,
                                    getScaledAgentCoordinates(officeAgent).getY() * tileSize,
                                    tileSize * 0.7, tileSize * 0.7);
                        }
                    }
                }

            }
        }
    }









    // GETTERS
    public static Coordinates getScaledAgentCoordinates(Agent agent) {
        Coordinates agentPosition = agent.getAgentMovement().getPosition();

        return GraphicsController.getScaledCoordinates(agentPosition);
    }

    public static Coordinates getScaledCoordinates(Coordinates coordinates) {
        return new Coordinates(coordinates.getX() / Patch.PATCH_SIZE_IN_SQUARE_METERS, coordinates.getY() / Patch.PATCH_SIZE_IN_SQUARE_METERS);
    }
}