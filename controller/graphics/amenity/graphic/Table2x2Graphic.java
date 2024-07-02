package com.socialsim.controller.graphics.amenity.graphic;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.model.core.environment.patchobject.passable.goal.Table2x2;

public class Table2x2Graphic extends AmenityGraphic {

    /***** VARIABLES *****/
    private static final int ROW_SPAN = 2;
    private static final int COLUMN_SPAN = 2;

    private static final int NORMAL_ROW_OFFSET = 0;
    private static final int NORMAL_COLUMN_OFFSET = 0;

    /***** CONSTRUCTOR *****/
    public Table2x2Graphic(Table2x2 table) {
        super(
                table,
                ROW_SPAN,
                COLUMN_SPAN,
                NORMAL_ROW_OFFSET,
                NORMAL_COLUMN_OFFSET
        );

        this.graphicIndex = 0;
    }
}