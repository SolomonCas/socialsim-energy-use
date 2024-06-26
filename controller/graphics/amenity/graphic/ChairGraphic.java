package com.socialsim.controller.graphics.amenity.graphic;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.model.core.environment.patchobject.Amenity;

public class ChairGraphic extends AmenityGraphic {

    /***** VARIABLES *****/
    private static final int ROW_SPAN = 1;
    private static final int COLUMN_SPAN = 1;

    private static final int NORMAL_ROW_OFFSET = 0;
    private static final int NORMAL_COLUMN_OFFSET = 0;

    /***** CONSTRUCTOR *****/
    public ChairGraphic(Amenity chair, String facing, String type) {
        super(chair, ROW_SPAN, COLUMN_SPAN, NORMAL_ROW_OFFSET, NORMAL_COLUMN_OFFSET);

        this.graphicIndex = getGraphicIndex(facing, type);
    }

    private static int getGraphicIndex(String facing, String type) {
        switch (type) {
            case "OFFICE":
                switch (facing) {
                    case "SOUTH": return 0;
                    case "NORTH": return 1;
                    case "EAST": return 2;
                    case "WEST": return 3;
                }
                break;
            case "PANTRY_TYPE_A":
                switch (facing) {
                    case "SOUTH": return 4;
                    case "NORTH": return 5;
                    case "EAST": return 6;
                    case "WEST": return 7;
                }
                break;
            case "PANTRY_TYPE_B":
                switch (facing) {
                    case "SOUTH": return 8;
                    case "NORTH": return 9;
                    case "EAST": return 10;
                    case "WEST": return 11;
                }
                break;
            default: throw new IllegalArgumentException("Unknown type: " + type);
        }
        throw new IllegalArgumentException("Unknown facing: " + facing);
    }
}
