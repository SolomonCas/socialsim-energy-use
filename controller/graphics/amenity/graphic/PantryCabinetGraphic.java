package com.socialsim.controller.graphics.amenity.graphic;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.model.core.environment.patchobject.passable.goal.PantryCabinet;

public class PantryCabinetGraphic extends AmenityGraphic {

    /***** VARIABLES *****/
    private static final int ROW_SPAN = 1;
    private static final int COLUMN_SPAN = 1;

    private static final int NORMAL_ROW_OFFSET = 0;
    private static final int NORMAL_COLUMN_OFFSET = 0;



    /***** CONSTRUCTOR *****/

    public PantryCabinetGraphic(PantryCabinet pantryCabinet) {
        super(pantryCabinet, ROW_SPAN, COLUMN_SPAN, NORMAL_ROW_OFFSET, NORMAL_COLUMN_OFFSET);

        this.graphicIndex = 0;
    }

}