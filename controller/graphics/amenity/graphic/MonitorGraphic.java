package com.socialsim.controller.graphics.amenity.graphic;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.model.core.environment.patchobject.Amenity;
import com.socialsim.model.core.environment.patchobject.passable.goal.Monitor;

public class MonitorGraphic extends AmenityGraphic {

    /***** VARIABLES *****/

    private static final int ROW_SPAN = 1; // HI I CHANGED THIS FROM 2 TO 1 IDK WHY IT WAS SET TO 2
    private static final int COLUMN_SPAN = 1;

    private static final int NORMAL_ROW_OFFSET = 0;
    private static final int NORMAL_COLUMN_OFFSET = 0;



    /***** CONSTRUCTOR *****/

    public MonitorGraphic(Amenity monitor, String facing) {
        super(monitor, ROW_SPAN, COLUMN_SPAN, NORMAL_ROW_OFFSET, NORMAL_COLUMN_OFFSET);

        switch (facing) {
            case "SOUTH" -> this.graphicIndex = 0;
            case "NORTH" -> this.graphicIndex = 1;
            case "NORTH_BEHIND_CUBICLE" -> this.graphicIndex = 2;
            case "EAST" -> this.graphicIndex = 3;
            case "WEST" -> this.graphicIndex = 4;
        }
    }

}