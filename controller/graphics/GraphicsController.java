package com.socialsim.controller.graphics;

import com.socialsim.controller.controls.Controller;
import com.socialsim.controller.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.model.core.agent.Agent;
import com.socialsim.model.core.environment.Environment;
import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchfield.*;
import com.socialsim.model.core.environment.patchobject.Amenity;
import com.socialsim.model.core.environment.patchobject.Drawable;
import com.socialsim.model.core.environment.patchobject.passable.NonObstacle;
import com.socialsim.model.core.environment.patchobject.passable.gate.Gate;
import com.socialsim.model.core.environment.position.Coordinates;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class GraphicsController extends Controller {


    // VARIABLES

        // INSERT SPRITE SHEETS

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
//            amenityAgentSet.addAll(new ArrayList<>(environment.getAmenityPatchSet()));
//            amenityAgentSet.addAll(new ArrayList<>(environment.getAgentPatchSet()));
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
            Pair<PatchField, Integer> patchNumPair = currentPatch.getPatchField();
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
                    AmenityGraphicLocation amenityGraphicLocation = drawablePatchAmenity.getGraphicLocation();


                    // IF STATEMENTS
                    if (patchAmenity instanceof NonObstacle) {
                        if (!((NonObstacle) patchAmenity).isEnabled()) {
                            drawGraphicTransparently = true;
                        }
                    }

                    if (drawGraphicTransparently) {
                        foregroundGraphicsContext.setGlobalAlpha(0.2);
                    }

                    if (patchAmenity.getClass() == Gate.class) {
                        // INSERT CODE
                    }

                    // ADD OTHER IF STATEMENTS FOR AMENITIES (3 MORE)


                    if (drawGraphicTransparently) {
                        foregroundGraphicsContext.setGlobalAlpha(1.0);
                    }

                }
            }




            // PATCH NUM PAIR
            if (patchNumPair != null) {
                PatchField patchPatchField = patchNumPair.getKey();


                // WALLS
                if (patchPatchField.getClass() == Wall.class) {


                    patchColor = Color.rgb(104, 101, 101);
                    backgroundGraphicsContext.setFill(patchColor);
                    backgroundGraphicsContext.fillRect(column * tileSize, row * tileSize, tileSize, tileSize);


                // BATHROOM
                } else if (patchPatchField.getClass() == Bathroom.class) {

                    // FEMALE
                    if (patchNumPair.getValue() == 1) {
                        patchColor = Color.rgb(233, 127, 146);


                    // MALE
                    } else if (patchNumPair.getValue() == 2) {
                        patchColor = Color.rgb(140, 211, 242);

                    // UNISEX
                    } else {
                        patchColor = Color.rgb(169, 204, 164);
                    }



                    backgroundGraphicsContext.setFill(patchColor);
                    backgroundGraphicsContext.fillRect(column * tileSize, row * tileSize, tileSize, tileSize);



                } // ELEVATOR LOBBY
                else if (patchPatchField.getClass() == ElevLobby.class) {
                    patchColor = Color.rgb(255, 238, 204);
                    backgroundGraphicsContext.setFill(patchColor);
                    backgroundGraphicsContext.fillRect(column * tileSize, row * tileSize, tileSize, tileSize);

                } // RECEPTION
                else if (patchPatchField.getClass() == Reception.class) {
                    patchColor = Color.rgb(255, 238, 204);
                    backgroundGraphicsContext.setFill(patchColor);
                    backgroundGraphicsContext.fillRect(column * tileSize, row * tileSize, tileSize, tileSize);

                } // MEETING ROOM
                else if (patchPatchField.getClass() == MeetingRoom.class) {
                    patchColor = Color.rgb(222, 211, 252);
                    backgroundGraphicsContext.setFill(patchColor);
                    backgroundGraphicsContext.fillRect(column * tileSize, row * tileSize, tileSize, tileSize);

                } // FACULTY ROOM
                else if (patchPatchField.getClass() == FacultyRoom.class) {
                    patchColor = Color.rgb(222, 211, 252);
                    backgroundGraphicsContext.setFill(patchColor);
                    backgroundGraphicsContext.fillRect(column * tileSize, row * tileSize, tileSize, tileSize);
                } // CONTROL CENTER
                else if (patchPatchField.getClass() == ControlCenter.class) {
                    patchColor = Color.rgb(240, 189, 207);
                    backgroundGraphicsContext.setFill(patchColor);
                    backgroundGraphicsContext.fillRect(column * tileSize, row * tileSize, tileSize, tileSize);
                } // DATA CENTER
                else if (patchPatchField.getClass() == DataCenter.class) {
                    patchColor = Color.rgb(250, 189, 193);
                    backgroundGraphicsContext.setFill(patchColor);
                    backgroundGraphicsContext.fillRect(column * tileSize, row * tileSize, tileSize, tileSize);
                } // DEAN ROOM
                else if (patchPatchField.getClass() == DeanRoom.class) {
                    patchColor = Color.rgb(129, 204, 235);
                    backgroundGraphicsContext.setFill(patchColor);
                    backgroundGraphicsContext.fillRect(column * tileSize, row * tileSize, tileSize, tileSize);
                } // DATA COLLECTION
                else if (patchPatchField.getClass() == DataCollectionRoom.class) {
                    patchColor = Color.rgb(155, 235, 197);
                    backgroundGraphicsContext.setFill(patchColor);
                    backgroundGraphicsContext.fillRect(column * tileSize, row * tileSize, tileSize, tileSize);
                } // HUMAN EXPERIENCE
                else if (patchPatchField.getClass() == HumanExpRoom.class) {
                    patchColor = Color.rgb(185, 219, 207);
                    backgroundGraphicsContext.setFill(patchColor);
                    backgroundGraphicsContext.fillRect(column * tileSize, row * tileSize, tileSize, tileSize);
                } // RESEARCH CENTER
                else if (patchPatchField.getClass() == ResearchCenter.class) {
                    patchColor = Color.rgb(169, 204, 164);
                    backgroundGraphicsContext.setFill(patchColor);
                    backgroundGraphicsContext.fillRect(column * tileSize, row * tileSize, tileSize, tileSize);
                } // LEARNING SPACE
                else if (patchPatchField.getClass() == LearningSpace.class) {
                    patchColor = Color.rgb(169, 204, 164);
                    backgroundGraphicsContext.setFill(patchColor);
                    backgroundGraphicsContext.fillRect(column * tileSize, row * tileSize, tileSize, tileSize);
                } // STAFF AREA
                else if (patchPatchField.getClass() == StaffArea.class) {
                    patchColor = Color.rgb(254, 195, 162);
                    backgroundGraphicsContext.setFill(patchColor);
                    backgroundGraphicsContext.fillRect(column * tileSize, row * tileSize, tileSize, tileSize);
                } // PANTRY
                else if (patchPatchField.getClass() == Pantry.class) {
                    patchColor = Color.rgb(239, 213, 189);
                    backgroundGraphicsContext.setFill(patchColor);
                    backgroundGraphicsContext.fillRect(column * tileSize, row * tileSize, tileSize, tileSize);
                } // STORAGE ROOM
                else if (patchPatchField.getClass() == StorageRoom.class) {
                    patchColor = Color.rgb(178, 159, 141);
                    backgroundGraphicsContext.setFill(patchColor);
                    backgroundGraphicsContext.fillRect(column * tileSize, row * tileSize, tileSize, tileSize);
                } // SOLO ROOM
                else if (patchPatchField.getClass() == SoloRoom.class) {
                    patchColor = Color.rgb(169, 204, 164);
                    backgroundGraphicsContext.setFill(patchColor);
                    backgroundGraphicsContext.fillRect(column * tileSize, row * tileSize, tileSize, tileSize);
                }


                    // INSERT AGENT CODE


            }

        }

    }









    // GETTERS
    public static Coordinates getScaledAgentCoordinates(Agent agent) {
        // insert code
        return null;
    }

    public static Coordinates getScaledCoordinates(Coordinates coordinates) {
        return new Coordinates(coordinates.getX() / Patch.PATCH_SIZE_IN_SQUARE_METERS, coordinates.getY() / Patch.PATCH_SIZE_IN_SQUARE_METERS);
    }
}