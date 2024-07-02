package com.socialsim.controller.graphics.amenity.graphic;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.model.core.environment.patchobject.passable.goal.ResearchTable;

public class ResearchTableGraphic extends AmenityGraphic {

    /***** VARIABLES *****/
    private static final int ROW_SPAN_VERTICAL = 4;
    private static final int COLUMN_SPAN_VERTICAL = 1;

    private static final int ROW_SPAN_HORIZONTAL = 1;
    private static final int COLUMN_SPAN_HORIZONTAL = 4;

    private static final int NORMAL_ROW_OFFSET = 0;
    private static final int NORMAL_COLUMN_OFFSET = 0;

    /***** CONSTRUCTOR *****/
    public ResearchTableGraphic(ResearchTable researchTable, String facing) {
        super(
                researchTable,
                facing.equals("NORTH") || facing.equals("SOUTH") ? ROW_SPAN_HORIZONTAL : ROW_SPAN_VERTICAL,
                facing.equals("NORTH") || facing.equals("SOUTH") ? COLUMN_SPAN_HORIZONTAL : COLUMN_SPAN_VERTICAL,
                NORMAL_ROW_OFFSET,
                NORMAL_COLUMN_OFFSET
        );

        switch (facing) {
            case "WEST" -> this.graphicIndex = 0;
            case "EAST" -> this.graphicIndex = 1;
            case "SOUTH" -> this.graphicIndex = 2;
        }
    }

}