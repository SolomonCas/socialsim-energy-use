package com.socialsim.controller.graphics.amenity.graphic;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.model.core.environment.patchobject.Amenity;
import com.socialsim.model.core.environment.patchobject.passable.goal.Cubicle;

public class CubicleGraphic extends AmenityGraphic {

    /***** VARIABLES *****/
    private static final int ROW_SPAN_MESA = 5;;
    private static final int ROW_SPAN_TYPE_A = 8;
    private static final int ROW_SPAN_TYPE_B = 4;
    private static final int ROW_SPAN_VERTICAL_TYPE_C = 3;
    private static final int ROW_SPAN_HORIZONTAL_TYPE_C = 2;

    private static final int COLUMN_SPAN_MESA = 4;
    private static final int COLUMN_SPAN_TYPE_A = 5;
    private static final int COLUMN_SPAN_TYPE_B = 3;
    private static final int COLUMN_SPAN_VERTICAL_TYPE_C = 2;
    private static final int COLUMN_SPAN_HORIZONTAL_TYPE_C = 3;


    private static final int NORMAL_ROW_OFFSET = 0;
    private static final int NORMAL_COLUMN_OFFSET = 0;

    /***** CONSTRUCTOR *****/
    public CubicleGraphic(Amenity cubicle, String type, String facing, String tableOn) {
        super(
            cubicle,
            getRowSpan(type, facing),
            getColumnSpan(type, facing),
            NORMAL_ROW_OFFSET,
            NORMAL_COLUMN_OFFSET);

        this.graphicIndex = getGraphicIndex(type, facing, tableOn);
    }

    private static int getRowSpan(String type, String facing) {
        switch (type) {
            case "MESA":
                return ROW_SPAN_MESA;
            case "TYPE_A":
                return ROW_SPAN_TYPE_A;
            case "TYPE_B":
                return ROW_SPAN_TYPE_B;
            case "TYPE_C":
                switch (facing) {
                    case "NORTH", "SOUTH":
                        return ROW_SPAN_HORIZONTAL_TYPE_C;
                    case "WEST", "EAST":
                        return ROW_SPAN_VERTICAL_TYPE_C;
                }
            default:
                throw new IllegalArgumentException("Unknown type: " + type);
        }
    }

    private static int getColumnSpan(String type, String facing) {
        switch (type) {
            case "MESA":
                return COLUMN_SPAN_MESA;
            case "TYPE_A":
                return COLUMN_SPAN_TYPE_A;
            case "TYPE_B":
                return COLUMN_SPAN_TYPE_B;
            case "TYPE_C":
                switch (facing) {
                    case "NORTH", "SOUTH":
                        return COLUMN_SPAN_HORIZONTAL_TYPE_C;
                    case "WEST", "EAST":
                        return COLUMN_SPAN_VERTICAL_TYPE_C;
                }
            default:
                throw new IllegalArgumentException("Unknown type: " + type);
        }
    }

    private static int getGraphicIndex(String type, String facing, String tableOn) {
        switch (type) {
            case "MESA":
                switch (tableOn) {
                    case "NORTH_AND_WEST":
                        return 0;
                    case "NORTH_AND_EAST":
                        return 1;
                    case "SOUTH_AND_WEST":
                        return 2;
                    case "SOUTH_AND_EAST":
                        return 3;
                }
            case "TYPE_A":
                return 4;
            case "TYPE_B":
                switch (facing) {
                    case "WEST":
                        return 5;
                    case "EAST":
                        return 6;
                }
            case "TYPE_C":
                switch (facing) {
                    case "NORTH":
                        return 7;
                    case "SOUTH":
                        return 8;
                    case "WEST":
                        return 9;
                    case "EAST":
                        return 10;
                    default:
                        throw new IllegalArgumentException("Unknown facing: " + facing);
                }
            default:
                throw new IllegalArgumentException("Unknown type: " + type);
        }
    }
}