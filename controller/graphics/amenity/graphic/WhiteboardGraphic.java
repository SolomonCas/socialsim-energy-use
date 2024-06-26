package com.socialsim.controller.graphics.amenity.graphic;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.model.core.environment.patchobject.passable.goal.Whiteboard;

import java.util.Objects;

public class WhiteboardGraphic extends AmenityGraphic {

    /***** VARIABLES *****/

    private static final int ROW_SPAN_VERTICAL_11 = 11;
    private static final int ROW_SPAN_VERTICAL_4 = 4;
    private static final int COLUMN_SPAN_VERTICAL = 1;

    private static final int ROW_SPAN_HORIZONTAL = 1;
    private static final int ROW_SPAN_HORIZONTAL_2 = 2;
    private static final int COLUMN_SPAN_HORIZONTAL_5 = 5;
    private static final int COLUMN_SPAN_HORIZONTAL_2 = 2;

    private static final int NORMAL_ROW_OFFSET = 0;
    private static final int NORMAL_COLUMN_OFFSET = 0;




    /***** CONSTRUCTOR *****/

    public WhiteboardGraphic(Whiteboard whiteboard, String facing, String length) {
        super(
                whiteboard,
                getRowSpan(facing, length),
                getColumnSpan(facing),
                NORMAL_ROW_OFFSET,
                NORMAL_COLUMN_OFFSET
        );
        this.graphicIndex = getGraphicIndex(facing, length);
    }

    private static int getRowSpan(String facing, String length) {
        if (facing.equals("NORTH")) {
            return ROW_SPAN_HORIZONTAL;
        }
        else if (facing.equals("SOUTH")) {
            return ROW_SPAN_HORIZONTAL_2;
        }
        else if (facing.equals("WEST")) {
            return ROW_SPAN_VERTICAL_4;
        }
        else if (facing.equals("EAST")) {
            if (length.equals("11")) {
                return ROW_SPAN_VERTICAL_11;
            } else {
                return ROW_SPAN_VERTICAL_4;
            }
        } else {
            throw new IllegalArgumentException("[UNKNOWN] Facing: " + facing);
        }
    }

    private static int getColumnSpan(String facing) {
        if (facing.equals("NORTH")) {
            return COLUMN_SPAN_HORIZONTAL_2;
        }
        else if (facing.equals("SOUTH")) {
            return COLUMN_SPAN_HORIZONTAL_5;
        }
        else if (facing.equals("WEST") || facing.equals("EAST")) {
            return COLUMN_SPAN_VERTICAL;
        }
        else {
            throw new IllegalArgumentException("[UNKNOWN] Facing: " + facing);
        }
    }

    private static int getGraphicIndex(String facing, String length) {
        if ("NORTH".equals(facing)) {
            return 0;
        } else if ("SOUTH".equals(facing)) {
            return 1;
        } else if ("WEST".equals(facing)) {
            return 2;
        } else if ("EAST".equals(facing)) {
            if ("4".equals(length)) {
                return 3;
            } else if ("11".equals(length)) {
                return 4;
            } else {
                throw new IllegalArgumentException("[UNKNOWN] Length: " + length);
            }
        } else {
            throw new IllegalArgumentException("[UNKNOWN] Facing: " + facing + " Length: " + length);
        }
    }

}