package com.socialsim.controller.graphics.amenity.graphic;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.model.core.environment.patchobject.passable.goal.WindowBlinds;

public class WindowBlindsGraphic extends AmenityGraphic {

    /***** VARIABLES *****/

    private static final int SPAN_1 = 1;
    private static final int SPAN_2 = 2;
    private static final int SPAN_3 = 3;

    private static final int NORMAL_ROW_OFFSET = 0;
    private static final int NORMAL_COLUMN_OFFSET = 0;

    /***** CONSTRUCTOR *****/
    public WindowBlindsGraphic(WindowBlinds windowBlinds, String state) {
        super(
                windowBlinds,
                getRowSpan(state),
                SPAN_1, // Column span is always 1
                NORMAL_ROW_OFFSET,
                NORMAL_COLUMN_OFFSET
        );
        this.graphicIndex = getGraphicIndex(state);

    }

    private static int getRowSpan(String state) {
        return switch (state) {
            case    "OPENED_SOUTH_FROM_INSIDE",
                    "OPENED_SOUTH_FROM_OUTSIDE",
                    "CLOSED_SOUTH_FROM_INSIDE",
                    "CLOSED_SOUTH_FROM_OUTSIDE"
                    -> SPAN_3;
            case    "OPENED_NORTH",
                    "OPENED_EAST",
                    "OPENED_WEST",
                    "CLOSED_NORTH",
                    "CLOSED_EAST",
                    "CLOSED_WEST"
                    -> SPAN_1;
            case "GLASS" -> SPAN_2;
            default -> throw new IllegalStateException("Unknown value: " + state);
        };
    }

    private static int getGraphicIndex(String state) {
        return switch (state) {
            case "OPENED_SOUTH_FROM_INSIDE" -> 0;
            case "OPENED_SOUTH_FROM_OUTSIDE" -> 1;
            case "OPENED_NORTH" -> 2;
            case "OPENED_EAST" -> 3;
            case "OPENED_WEST" -> 4;
            case "CLOSED_SOUTH_FROM_INSIDE" -> 5;
            case "CLOSED_SOUTH_FROM_OUTSIDE" -> 6;
            case "CLOSED_NORTH" -> 7;
            case "CLOSED_EAST" -> 8;
            case "CLOSED_WEST" -> 9;
            case "GLASS" -> 10;
            default -> throw new IllegalArgumentException("Unknown state: " + state);
        };
    }


}