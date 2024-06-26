package com.socialsim.controller.graphics.amenity.graphic;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.model.core.environment.patchobject.Amenity;
import com.socialsim.model.core.environment.patchobject.passable.goal.PantryTable;

public class PantryTableGraphic extends AmenityGraphic {

    /***** VARIABLES *****/
    private static final int ROW_SPAN = 1;
    private static final int COLUMN_SPAN_TYPE_A = 1;
    private static final int COLUMN_SPAN_TYPE_B = 3;

    private static final int NORMAL_ROW_OFFSET = 0;
    private static final int NORMAL_COLUMN_OFFSET = 0;



    /***** CONSTRUCTOR *****/

    public PantryTableGraphic(PantryTable table, String type) {
        super(table,
                ROW_SPAN,
                type.equals("TYPE_A") ? COLUMN_SPAN_TYPE_A : COLUMN_SPAN_TYPE_B,
                NORMAL_ROW_OFFSET, NORMAL_COLUMN_OFFSET);

        switch (type) {
            case "TYPE_A" -> this.graphicIndex = 0;
            case "TYPE_B" -> this.graphicIndex = 1;
        }
    }

}