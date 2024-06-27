package com.socialsim.controller.graphics.amenity.graphic;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.model.core.environment.patchobject.passable.goal.MeetingTable;

public class MeetingTableGraphic extends AmenityGraphic {

    /***** VARIABLES *****/
    private static final int ROW_SPAN_VERTICAL_SMALL = 7;
    private static final int COLUMN_SPAN_VERTICAL_SMALL = 1;
    private static final int ROW_SPAN_VERTICAL_LARGE = 9;
    private static final int COLUMN_SPAN_VERTICAL_LARGE = 3;

    private static final int ROW_SPAN_HORIZONTAL_SMALL = 1;
    private static final int COLUMN_SPAN_HORIZONTAL_SMALL = 7;
    private static final int ROW_SPAN_HORIZONTAL_LARGE = 3;
    private static final int COLUMN_SPAN_HORIZONTAL_LARGE = 9;

    private static final int NORMAL_ROW_OFFSET = 0;
    private static final int NORMAL_COLUMN_OFFSET = 0;

    /***** CONSTRUCTOR *****/
    public MeetingTableGraphic(MeetingTable meetingTable, String orientation, String size) {
            super(
                    meetingTable,
                    getRowSpan(orientation, size),
                    getColumnSpan(orientation, size),
                    NORMAL_ROW_OFFSET,
                    NORMAL_COLUMN_OFFSET
            );

            this.graphicIndex = getGraphicIndex(orientation, size);
        }

        private static int getRowSpan(String orientation, String size) {
            if (orientation.equals("VERTICAL")) {
                if (size.equals("LARGE")) {
                    return ROW_SPAN_VERTICAL_LARGE;
                } else if (size.equals("SMALL")) {
                    return ROW_SPAN_VERTICAL_SMALL;
                } else {
                    throw new IllegalArgumentException("Unknown size: " + size);
                }
            }
            else if (orientation.equals("HORIZONTAL")) {
                if (size.equals("LARGE")) {
                    return ROW_SPAN_HORIZONTAL_LARGE;
                } else if (size.equals("SMALL")) {
                    return ROW_SPAN_HORIZONTAL_SMALL;
                } else {
                    throw new IllegalArgumentException("Unknown size: " + size);
                }
            } else {
                throw new IllegalArgumentException("Unknown orientation: " + orientation);
            }
        }

        private static int getColumnSpan(String orientation, String size) {
            if (orientation.equals("VERTICAL")) {
                if (size.equals("LARGE")) {
                    return COLUMN_SPAN_VERTICAL_LARGE;
                } else if (size.equals("SMALL")) {
                    return COLUMN_SPAN_VERTICAL_SMALL;
                } else {
                    throw new IllegalArgumentException("Unknown size: " + size);
                }
            } else if (orientation.equals("HORIZONTAL")) {
                if (size.equals("LARGE")) {
                    return COLUMN_SPAN_HORIZONTAL_LARGE;
                } else if (size.equals("SMALL")) {
                    return COLUMN_SPAN_HORIZONTAL_SMALL;
                } else {
                    throw new IllegalArgumentException("Unknown size: " + size);
                }
            } else {
                throw new IllegalArgumentException("Unknown orientation: " + orientation);
            }
        }

        private static int getGraphicIndex(String orientation, String size) {
            if (orientation.equals("VERTICAL")) {
                if (size.equals("LARGE")) {
                    return 0;
                } else if (size.equals("SMALL")) {
                    return 1;
                } else {
                    throw new IllegalArgumentException("Unknown size: " + size);
                }
            } else if (orientation.equals("HORIZONTAL")) {
                if (size.equals("LARGE")) {
                    return 2;
                } else if (size.equals("SMALL")) {
                    return 3; // no sprite yet for this
                } else {
                    throw new IllegalArgumentException("Unknown size: " + size);
                }
            } else {
                throw new IllegalArgumentException("Unknown orientation: " + orientation);
            }
        }

}