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
        return switch (type) {
            case "SINGLE_PENDANT_LIGHT", "SINGLE_RECESSED_LIGHT" -> SPAN_1;
            case "LINEAR_PENDANT_LIGHT", "RECESSED_LINEAR_LIGHT" -> switch (orientation) {
                case "HORIZONTAL" -> SPAN_1;
                case "VERTICAL" -> SPAN_2;
                default -> throw new IllegalArgumentException("Unknown orientation: " + orientation);
            };
            case "TRACK_LIGHT" -> switch (orientation) {
                case "HORIZONTAL" -> SPAN_1;
                case "VERTICAL" -> SPAN_4;
                default -> throw new IllegalArgumentException("Unknown orientation: " + orientation);
            };
            default -> throw new IllegalArgumentException("Unknown type: " + type);
        };
    }

    private static int getColumnSpan(String type, String orientation) {
        return switch (type) {
            case "SINGLE_PENDANT_LIGHT", "SINGLE_RECESSED_LIGHT" -> SPAN_1;
            case "LINEAR_PENDANT_LIGHT", "RECESSED_LINEAR_LIGHT" -> switch (orientation) {
                case "HORIZONTAL" -> SPAN_2;
                case "VERTICAL" -> SPAN_1;
                default -> throw new IllegalArgumentException("Unknown orientation: " + orientation);
            };
            case "TRACK_LIGHT" -> switch (orientation) {
                case "HORIZONTAL" -> SPAN_4;
                case "VERTICAL" -> SPAN_1;
                default -> throw new IllegalArgumentException("Unknown orientation: " + orientation);
            };
            default -> throw new IllegalArgumentException("Unknown type: " + type);
        };
    }

    private static int getGraphicIndex(String type, String orientation) {
        return switch (type) {
            case "SINGLE_PENDANT_LIGHT" -> 0;
            case "SINGLE_RECESSED_LIGHT" -> 1;
            case "LINEAR_PENDANT_LIGHT" -> switch (orientation) {
                case "HORIZONTAL" -> 2;
                case "VERTICAL" -> 3;
                default -> throw new IllegalArgumentException("Unknown orientation: " + orientation);
            };
            case "RECESSED_LINEAR_LIGHT" -> switch (orientation) {
                case "HORIZONTAL" -> 4;
                case "VERTICAL" -> 5;
                default -> throw new IllegalArgumentException("Unknown orientation: " + orientation);
            };
            case "TRACK_LIGHT" -> switch (orientation) {
                case "HORIZONTAL" -> 6;
                case "VERTICAL" -> 7;
                default -> throw new IllegalArgumentException("Unknown orientation: " + orientation);
            };
            default -> throw new IllegalArgumentException("Unknown type: " + type);
        };
    }
}