package com.socialsim.controller.graphics.amenity.graphic;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.model.core.environment.patchobject.passable.goal.LearningTable;

public class LearningTableGraphic extends AmenityGraphic {

    /***** VARIABLES *****/
    private static final int ROW_SPAN_VERTICAL = 5;
    private static final int COLUMN_SPAN_VERTICAL = 2;

    private static final int ROW_SPAN_HORIZONTAL = 2;
    private static final int COLUMN_SPAN_HORIZONTAL = 5;

    private static final int NORMAL_ROW_OFFSET = 0;
    private static final int NORMAL_COLUMN_OFFSET = 0;

    /***** CONSTRUCTOR *****/
    public LearningTableGraphic(LearningTable learningTable, String orientation) {
        super(
                learningTable,
                orientation.equals("HORIZONTAL") ? ROW_SPAN_HORIZONTAL : ROW_SPAN_VERTICAL,
                orientation.equals("HORIZONTAL") ? COLUMN_SPAN_HORIZONTAL : COLUMN_SPAN_VERTICAL,
                NORMAL_ROW_OFFSET,
                NORMAL_COLUMN_OFFSET
        );

        switch (orientation) {
            case "HORIZONTAL" -> this.graphicIndex = 0;
            case "VERTICAL" -> this.graphicIndex = 1;
        }
    }
}