package com.socialsim.controller.graphics.amenity.graphic;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.model.core.environment.patchobject.passable.goal.OfficeDesk;

public class OfficeDeskGraphic extends AmenityGraphic {

    /***** VARIABLES *****/

    private static final int ROW_SPAN = 1;
    private static final int COLUMN_SPAN = 1;

    private static final int NORMAL_ROW_OFFSET = 0;
    private static final int NORMAL_COLUMN_OFFSET = 0;




    /***** CONSTRUCTOR *****/

    public OfficeDeskGraphic(OfficeDesk officeDesk) {
        super(officeDesk, ROW_SPAN, COLUMN_SPAN, NORMAL_ROW_OFFSET, NORMAL_COLUMN_OFFSET);
    }

}