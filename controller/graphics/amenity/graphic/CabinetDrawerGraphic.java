package com.socialsim.controller.graphics.amenity.graphic;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.model.core.environment.patchobject.Amenity;

public class CabinetDrawerGraphic extends AmenityGraphic {

    /***** VARIABLES *****/
    private static final int SPAN_1 = 1;
    private static final int SPAN_2 = 2;
    private static final int SPAN_3 = 3;

    private static final int NORMAL_ROW_OFFSET = 0;
    private static final int NORMAL_COLUMN_OFFSET = 0;



    /***** CONSTRUCTOR *****/
    public CabinetDrawerGraphic(Amenity amenity, String type, String facing) {
        super(
                amenity,
                getRowSpan(type, facing),
                getColumnSpan(type, facing),
                NORMAL_ROW_OFFSET,
                NORMAL_COLUMN_OFFSET);

        this.graphicIndex = getGraphicIndex(type, facing);
    }

    private static int getRowSpan(String type, String facing) {
        return switch (type) {
            case "CABINET", "DOUBLE_DRAWERS" -> SPAN_3;
            case "CABINET_1x2" -> SPAN_1;
            case "DRAWERS" -> SPAN_2;
            default -> throw new IllegalStateException("Unknown value: " + type);
        };
    }

    private static int getColumnSpan(String type, String facing) {
        return switch (type) {
            case "DRAWERS", "DOUBLE_DRAWERS" -> SPAN_1;
            case "CABINET", "CABINET_1x2" -> SPAN_2;
            default -> throw new IllegalStateException("Unknown value: " + type);
        };
    }

    private static int getGraphicIndex(String type, String facing) {
        switch (type) {
            case "CABINET", "CABINET_1x2" -> {
                return switch (facing) {
                    case "SOUTH" -> 0;
                    case "NORTH" -> 1;
                    default -> throw new IllegalStateException("Unknown value: " + facing);
                };
            }
            case "DRAWERS" -> {
                return switch (facing) {
                    case "SOUTH" -> 2;
                    case "NORTH" -> 3;
                    case "EAST" -> 4;
                    case "WEST" -> 5;
                    default -> throw new IllegalStateException("Unknown value: " + facing);
                };
            }
            case "DOUBLE_DRAWERS" -> {
                return switch (facing) {
                    case "EAST" -> 6;
                    case "WEST" -> 7;
                    default -> throw new IllegalStateException("Unknown value: " + facing);
                };
            }
            default -> throw new IllegalStateException("Unknown value: " + type);
        }
    }


}