package com.socialsim.controller.graphics.amenity.graphic;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.model.core.environment.patchobject.passable.goal.Light;

public class LightGraphic extends AmenityGraphic {

    /***** VARIABLES *****/

    private static final int SPAN_1 = 1;
    private static final int SPAN_2 = 2;
    private static final int SPAN_4 = 4;


    private static final int NORMAL_ROW_OFFSET = 0;
    private static final int NORMAL_COLUMN_OFFSET = 0;

    /***** CONSTRUCTOR *****/
    public LightGraphic(Light light, String type, String orientation) {
        super(
            light,
            getRowSpan(type, orientation),
            getColumnSpan(type, orientation),
            NORMAL_ROW_OFFSET,
            NORMAL_COLUMN_OFFSET);

        this.graphicIndex = getGraphicIndex(type, orientation);
    }

    private static int getRowSpan(String type, String orientation) {
        switch (type) {
            case "SINGLE_PENDANT_LIGHT": return SPAN_1;
            case "LINEAR_PENDANT_LIGHT", "RECESSED_LINEAR_LIGHT":
                switch (orientation) {
                    case "HORIZONTAL": return SPAN_1;
                    case "VERTICAL":return SPAN_2;
                    default:
                        throw new IllegalArgumentException("Unknown orientation: " + orientation);
                }
            case "TRACK_LIGHT":
                switch (orientation) {
                    case "HORIZONTAL": return SPAN_1;
                    case "VERTICAL":return SPAN_4;
                    default:
                        throw new IllegalArgumentException("Unknown orientation: " + orientation);
                }
            default:
                throw new IllegalArgumentException("Unknown type: " + type);
        }
    }

    private static int getColumnSpan(String type, String orientation) {
        switch (type) {
            case "SINGLE_PENDANT_LIGHT": return SPAN_1;
            case "LINEAR_PENDANT_LIGHT", "RECESSED_LINEAR_LIGHT":
                switch (orientation) {
                    case "HORIZONTAL": return SPAN_2;
                    case "VERTICAL":return SPAN_1;
                    default:
                        throw new IllegalArgumentException("Unknown orientation: " + orientation);
                }
            case "TRACK_LIGHT":
                switch (orientation) {
                    case "HORIZONTAL": return SPAN_4;
                    case "VERTICAL":return SPAN_1;
                    default:
                        throw new IllegalArgumentException("Unknown orientation: " + orientation);
                }
            default:
                throw new IllegalArgumentException("Unknown type: " + type);
        }
    }

    private static int getGraphicIndex(String type, String orientation) {
        switch (type) {
            case "SINGLE_PENDANT_LIGHT": return 0;
            case "LINEAR_PENDANT_LIGHT":
                switch (orientation) {
                    case "HORIZONTAL": return 1;
                    case "VERTICAL":return 2;
                    default:
                        throw new IllegalArgumentException("Unknown orientation: " + orientation);
                }
            case "RECESSED_LINEAR_LIGHT":
                switch (orientation) {
                    case "HORIZONTAL": return 3;
                    case "VERTICAL":return 4;
                    default:
                        throw new IllegalArgumentException("Unknown orientation: " + orientation);
                }
            case "TRACK_LIGHT":
                switch (orientation) {
                    case "HORIZONTAL": return 5;
                    case "VERTICAL":return 6;
                    default:
                        throw new IllegalArgumentException("Unknown orientation: " + orientation);
                }
            default:
                throw new IllegalArgumentException("Unknown type: " + type);
        }
    }
}