package com.socialsim.controller.graphics.amenity.graphic;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.model.core.environment.patchobject.Amenity;

public class BarGraphic extends AmenityGraphic {

    /***** VARIABLES *****/

    private static final int ROW_SPAN = 1;
    private static final int COLUMN_SPAN = 1;

    private static final int ROW_SPAN_VERTICAL_5 = 5;
    private static final int ROW_SPAN_VERTICAL_4 = 4;
    private static final int ROW_SPAN_VERTICAL_2 = 2;

    private static final int COLUMN_SPAN_HORIZONTAL_8 = 8;
    private static final int COLUMN_SPAN_HORIZONTAL_6 = 6;
    private static final int COLUMN_SPAN_HORIZONTAL_4 = 4;
    private static final int COLUMN_SPAN_HORIZONTAL_3 = 3;
    private static final int COLUMN_SPAN_HORIZONTAL_2 = 2;

    private static final int NORMAL_ROW_OFFSET = 0;
    private static final int NORMAL_COLUMN_OFFSET = 0;

    /***** CONSTRUCTOR *****/
    public BarGraphic(Amenity bar, String dimensions) {
        super(
                bar,
                dimensions.equals("1x1") ? ROW_SPAN : getRowSpan(dimensions),
                dimensions.equals("1x1") ? COLUMN_SPAN : getColumnSpan(dimensions),
                NORMAL_ROW_OFFSET,
                NORMAL_COLUMN_OFFSET
        );
        this.graphicIndex = getGraphicIndex(dimensions);
    }

    private static int getRowSpan(String dimensions) {
        return switch (dimensions) {
            case "5x1" -> ROW_SPAN_VERTICAL_5;
            case "4x1" -> ROW_SPAN_VERTICAL_4;
            case "2x1" -> ROW_SPAN_VERTICAL_2;
            default -> ROW_SPAN; // Includes "1x1" and horizontal spans.
        };
    }

    private static int getColumnSpan(String dimensions) {
        return switch (dimensions) {
            case "1x8" -> COLUMN_SPAN_HORIZONTAL_8;
            case "1x6" -> COLUMN_SPAN_HORIZONTAL_6;
            case "1x4" -> COLUMN_SPAN_HORIZONTAL_4;
            case "1x3" -> COLUMN_SPAN_HORIZONTAL_3;
            case "1x2" -> COLUMN_SPAN_HORIZONTAL_2;
            default -> COLUMN_SPAN; // Includes "1x1" and vertical spans.
        };
    }

    private static int getGraphicIndex(String dimensions) {
        return switch (dimensions) {
            case "1x1" -> 0;
            case "5x1" -> 1;
            case "4x1" -> 2;
            case "2x1" -> 3;
            case "1x8" -> 4;
            case "1x6" -> 5;
            case "1x3" -> 6;
            case "1x2" -> 7;
            default -> throw new IllegalArgumentException("Unknown dimensions: " + dimensions);
        };
    }

}