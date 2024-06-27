package com.socialsim.controller.graphics.amenity.graphic;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.model.core.environment.patchobject.passable.goal.DirectorTable;

import java.util.Objects;

public class DirectorTableGraphic extends AmenityGraphic {

    /***** VARIABLES *****/

    private static final int ROW_SPAN_VERTICAL = 3;
    private static final int COLUMN_SPAN_VERTICAL = 1;

    private static final int ROW_SPAN_HORIZONTAL = 1;
    private static final int COLUMN_SPAN_HORIZONTAL = 3;
    private static final int NORMAL_ROW_OFFSET = 0;
    private static final int NORMAL_COLUMN_OFFSET = 0;




    /***** CONSTRUCTOR *****/

    public DirectorTableGraphic(DirectorTable directorTable, String orientation) {
        super(
                directorTable,
                orientation.equals("HORIZONTAL") ? ROW_SPAN_HORIZONTAL : ROW_SPAN_VERTICAL,
                orientation.equals("HORIZONTAL") ? COLUMN_SPAN_HORIZONTAL : COLUMN_SPAN_VERTICAL,
                NORMAL_ROW_OFFSET,
                NORMAL_COLUMN_OFFSET
        );

        switch (orientation) {
            case "HORIZONTAL" -> this.graphicIndex = 0;
            case "VERTICAL" -> this.graphicIndex = 1; // no sprite yet
        }

        // no sprites for no appliance yet, add as needed
        // and modify this as needed
    }

}