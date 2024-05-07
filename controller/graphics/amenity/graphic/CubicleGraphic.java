package com.socialsim.controller.graphics.amenity.graphic;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.model.core.environment.patchobject.passable.goal.Cubicle;

public class CubicleGraphic extends AmenityGraphic {

    /***** VARIABLES *****/
    private static final int ROW_SPAN = 2;
    private static final int COLUMN_SPAN = 2;

    private static final int NORMAL_ROW_OFFSET = 0;
    private static final int NORMAL_COLUMN_OFFSET = 0;

    /***** CONSTRUCTOR *****/
    public CubicleGraphic(Cubicle cubicle, String facing, boolean withAppliance) {
        super(cubicle, ROW_SPAN, COLUMN_SPAN, NORMAL_ROW_OFFSET, NORMAL_COLUMN_OFFSET);

        if (withAppliance) {
            switch (facing) {
                case "UP" -> this.graphicIndex = 0;
                case "DOWN" -> this.graphicIndex = 1;
                case "LEFT" -> this.graphicIndex = 2;
                case "RIGHT" -> this.graphicIndex = 3;
            }
        }
        else {
            switch (facing) {
                case "UP" -> this.graphicIndex = 4;
                case "DOWN" -> this.graphicIndex = 5;
                case "LEFT" -> this.graphicIndex = 6;
                case "RIGHT" -> this.graphicIndex = 7;
            }
        }
    }

}