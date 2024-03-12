package com.socialsim.controller.graphics;

import com.socialsim.controller.controls.Controller;
import com.socialsim.model.core.environment.Environment;
import com.socialsim.model.core.environment.patchobject.Amenity;
import javafx.scene.layout.StackPane;

import java.util.List;

public class GraphicsController extends Controller {
    public static List<Amenity.AmenityBlock> firstPortalAmenityBlocks;
    public static double tileSize;
    public static boolean willPeek;
    private static long millisecondsLastCanvasRefresh;

    static {
        GraphicsController.firstPortalAmenityBlocks = null;
        GraphicsController.willPeek = false;
        millisecondsLastCanvasRefresh = 0;
    }

    public static void requestDrawEnvironmentView(StackPane canvases, Environment environment, double tileSize, boolean background, boolean speedAware) {

    }
}