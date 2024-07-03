package com.socialsim.controller.graphics.amenity.graphic;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.model.core.environment.patchobject.passable.goal.Server;

public class ServerGraphic extends AmenityGraphic {


    /***** VARIABLES *****/

    private static final int SPAN_1 = 1;
    private static final int SPAN_2 = 2;
    private static final int SPAN_3 = 3;

    private static final int NORMAL_ROW_OFFSET = 0;
    private static final int NORMAL_COLUMN_OFFSET = 0;



    /***** CONSTRUCTOR *****/

    public ServerGraphic(Server server, String type) {
        super(
                server,
                getRowSpan(type),
                SPAN_1,
                NORMAL_ROW_OFFSET,
                NORMAL_COLUMN_OFFSET);

        switch (type) {
            case "TYPE_A" -> this.graphicIndex = 0;
            case "TYPE_B" -> this.graphicIndex = 1;
        }
    }

    private static int getRowSpan(String type) {
        return switch (type) {
            case "TYPE_A" -> SPAN_2;
            case "TYPE_B" -> SPAN_3;
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
    }

}