package com.socialsim.controller.graphics.amenity.graphic;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.model.core.environment.patchobject.passable.goal.Aircon;

public class AirconGraphic extends AmenityGraphic {

    /***** VARIABLES *****/
    private static final int ROW_SPAN = 2;
    private static final int COLUMN_SPAN = 2;

    private static final int NORMAL_ROW_OFFSET = 0;
    private static final int NORMAL_COLUMN_OFFSET = 0;



    /***** CONSTRUCTOR *****/

    public AirconGraphic(Aircon aircon, boolean isTurnedOn) {
        super(aircon, ROW_SPAN, COLUMN_SPAN, NORMAL_ROW_OFFSET, NORMAL_COLUMN_OFFSET);

        if (!isTurnedOn) {
            this.graphicIndex = 0;
        } else {
            this.graphicIndex = 1;
        }


    }

}