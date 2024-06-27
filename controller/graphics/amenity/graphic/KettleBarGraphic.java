package com.socialsim.controller.graphics.amenity.graphic;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.model.core.environment.patchobject.passable.goal.CoffeeMakerBar;
import com.socialsim.model.core.environment.patchobject.passable.goal.KettleBar;

public class KettleBarGraphic extends AmenityGraphic {

    /***** VARIABLES *****/
    private static final int ROW_SPAN = 2;
    private static final int COLUMN_SPAN = 1;

    private static final int NORMAL_ROW_OFFSET = 0;
    private static final int NORMAL_COLUMN_OFFSET = 0;



    /***** CONSTRUCTOR *****/

    public KettleBarGraphic(KettleBar kettleBar) {
        super(kettleBar, ROW_SPAN, COLUMN_SPAN, NORMAL_ROW_OFFSET, NORMAL_COLUMN_OFFSET);

        this.graphicIndex = 0;
    }

}