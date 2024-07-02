package com.socialsim.controller.graphics.amenity.graphic;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.model.core.environment.patchobject.passable.goal.Switch;

public class SwitchGraphic extends AmenityGraphic {

    /***** VARIABLES *****/
    private static final int ROW_SPAN = 2;
    private static final int COLUMN_SPAN = 1;

    private static final int NORMAL_ROW_OFFSET = 0;
    private static final int NORMAL_COLUMN_OFFSET = 0;



    /***** CONSTRUCTOR *****/

    public SwitchGraphic(Switch aSwitch, String type, String facing) {
        super(aSwitch, ROW_SPAN, COLUMN_SPAN, NORMAL_ROW_OFFSET, NORMAL_COLUMN_OFFSET);

        switch (type) {
            case "LIGHT":
                switch (facing) {
                    case "SOUTH" -> this.graphicIndex = 0;
                    case "NORTH" -> this.graphicIndex = 1;
                    case "EAST" -> this.graphicIndex = 2;
                    case "WEST" -> this.graphicIndex = 3;
                }
                break;
            case "AC":
                switch (facing) {
                    case "SOUTH" -> this.graphicIndex = 4;
                    case "NORTH" -> this.graphicIndex = 5;
                    case "EAST" -> this.graphicIndex = 6;
                    case "WEST" -> this.graphicIndex = 7;
                }
                break;
        }
    }

}