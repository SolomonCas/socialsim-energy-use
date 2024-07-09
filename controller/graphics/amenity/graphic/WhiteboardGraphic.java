package com.socialsim.controller.graphics.amenity.graphic;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.model.core.environment.patchobject.passable.goal.Whiteboard;

public class WhiteboardGraphic extends AmenityGraphic {

    /***** VARIABLES *****/

    private static final int SPAN_1 = 1;
    private static final int SPAN_2 = 2;
    private static final int SPAN_4 = 4;
    private static final int SPAN_5 = 5;
    private static final int SPAN_11 = 11;

    private static final int NORMAL_ROW_OFFSET = 0;
    private static final int NORMAL_COLUMN_OFFSET = 0;




    /***** CONSTRUCTOR *****/

    public WhiteboardGraphic(Whiteboard whiteboard, String facing, String length) {
        super(
                whiteboard,
                getRowSpan(facing, length),
                getColumnSpan(facing, length),
                NORMAL_ROW_OFFSET,
                NORMAL_COLUMN_OFFSET
        );
        this.graphicIndex = getGraphicIndex(facing, length);
    }

    private static int getRowSpan(String facing, String length) {
        if (facing.equals("NORTH")) {
            return SPAN_1;
        }
        else if (facing.equals("SOUTH")) {
            return SPAN_2;
        }
        else if (facing.equals("WEST")) {
            return SPAN_4;
        }
        else if (facing.equals("EAST")) {
            if (length.equals("11")) {
                return SPAN_11;
            } else {
                return SPAN_4;
            }
        } else {
            throw new IllegalArgumentException("[UNKNOWN] Facing: " + facing);
        }
    }

    private static int getColumnSpan(String facing, String length) {
        if (facing.equals("NORTH")) {
            if (length.equals("5")) {
                return SPAN_5;
            } else {
                return SPAN_2;
            }
        }
        else if (facing.equals("SOUTH")) {
            return SPAN_5;
        }
        else if (facing.equals("WEST") || facing.equals("EAST")) {
            return SPAN_1;
        }
        else {
            throw new IllegalArgumentException("[UNKNOWN] Facing: " + facing);
        }
    }

    private static int getGraphicIndex(String facing, String length) {
        if ("NORTH".equals(facing)) {
            if ("2".equals(length)) {
                return 0;
            } else if ("5".equals(length)) {
                return 1;
            } else {
                throw new IllegalArgumentException("[UNKNOWN] Length: " + length);
            }
        } else if ("SOUTH".equals(facing)) {
            return 2;
        } else if ("WEST".equals(facing)) {
            return 3;
        } else if ("EAST".equals(facing)) {
            if ("4".equals(length)) {
                return 4;
            } else if ("11".equals(length)) {
                return 5;
            } else {
                throw new IllegalArgumentException("[UNKNOWN] Length: " + length);
            }
        } else {
            throw new IllegalArgumentException("[UNKNOWN] Facing: " + facing + " Length: " + length);
        }
    }

}