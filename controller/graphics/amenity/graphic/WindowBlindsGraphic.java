package com.socialsim.controller.graphics.amenity.graphic;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.model.core.environment.patchobject.passable.goal.WindowBlinds;

public class WindowBlindsGraphic extends AmenityGraphic {

    /***** VARIABLES *****/

    private static final int SPAN_1 = 1;
    private static final int SPAN_2 = 2;
    private static final int SPAN_3 = 3;
    private static final int SPAN_5 = 5;

    private static final int NORMAL_ROW_OFFSET = 0;
    private static final int NORMAL_COLUMN_OFFSET = 0;
    private final WindowBlinds windowBlinds;
    private String state;

    /***** CONSTRUCTOR *****/
    public WindowBlindsGraphic(WindowBlinds windowBlinds, String state) {
        super(
                windowBlinds,
                getRowSpan(state),
                SPAN_1, // Column span is always 1
                NORMAL_ROW_OFFSET,
                NORMAL_COLUMN_OFFSET
        );
        this.windowBlinds = windowBlinds;
        this.state = state;
        this.graphicIndex = getGraphicIndex(state);
    }

    /***** METHOD *****/
    public void change() {
        if(windowBlinds.isOpened()) {
            switch (this.state) {
                case "CLOSED_SOUTH_FROM_INSIDE" -> {
                    this.state = "OPENED_SOUTH_FROM_INSIDE";
                    this.graphicIndex = getGraphicIndex(this.state);
                }
                case "CLOSED_SOUTH_FROM_OUTSIDE" -> {
                    this.state = "OPENED_SOUTH_FROM_OUTSIDE";
                    this.graphicIndex = getGraphicIndex(this.state);
                }
                case "CLOSED_NORTH_AND_SOUTH" -> {
                    this.state = "OPENED_NORTH_AND_SOUTH";
                    this.graphicIndex = getGraphicIndex(this.state);
                }
                case "CLOSED_NORTH" -> {
                    this.state = "OPENED_NORTH";
                    this.graphicIndex = getGraphicIndex(this.state);
                }
                case "CLOSED_EAST" -> {
                    this.state = "OPENED_EAST";
                    this.graphicIndex = getGraphicIndex(this.state);
                }
                case "CLOSED_WEST" -> {
                    this.state = "OPENED_WEST";
                    this.graphicIndex = getGraphicIndex(this.state);
                }
                case "GLASS" -> this.graphicIndex = getGraphicIndex(this.state);
                default -> throw new IllegalArgumentException("Unknown state: " + state);
            }
        }
        else {
            switch (this.state) {
                case "OPENED_SOUTH_FROM_INSIDE" -> {
                    this.state = "CLOSED_SOUTH_FROM_INSIDE";
                    this.graphicIndex = getGraphicIndex(this.state);
                }
                case "OPENED_SOUTH_FROM_OUTSIDE" -> {
                    this.state = "CLOSED_SOUTH_FROM_OUTSIDE";
                    this.graphicIndex = getGraphicIndex(this.state);
                }
                case "OPENED_NORTH_AND_SOUTH" -> {
                    this.state = "CLOSED_NORTH_AND_SOUTH";
                    this.graphicIndex = getGraphicIndex(this.state);
                }
                case "OPENED_NORTH" -> {
                    this.state = "CLOSED_NORTH";
                    this.graphicIndex = getGraphicIndex(this.state);
                }
                case "OPENED_EAST" -> {
                    this.state = "CLOSED_EAST";
                    this.graphicIndex = getGraphicIndex(this.state);
                }
                case "OPENED_WEST" -> {
                    this.state = "CLOSED_WEST";
                    this.graphicIndex = getGraphicIndex(this.state);
                }
                case "GLASS" -> this.graphicIndex = getGraphicIndex(this.state);
                default -> throw new IllegalArgumentException("Unknown state: " + state);
            }
        }
    }
    private static int getRowSpan(String state) {
        return switch (state) {
            case    "OPENED_NORTH_AND_SOUTH",
                    "CLOSED_NORTH_AND_SOUTH"
                    -> SPAN_5;
            case    "OPENED_SOUTH_FROM_INSIDE",
                    "OPENED_SOUTH_FROM_OUTSIDE",
                    "CLOSED_SOUTH_FROM_INSIDE",
                    "CLOSED_SOUTH_FROM_OUTSIDE"
                    -> SPAN_3;
            case    "OPENED_NORTH",
                    "OPENED_EAST",
                    "OPENED_WEST",
                    "CLOSED_NORTH",
                    "CLOSED_EAST",
                    "CLOSED_WEST"
                    -> SPAN_1;
            case "GLASS" -> SPAN_2;
            default -> throw new IllegalStateException("Unknown value: " + state);
        };
    }

    private static int getGraphicIndex(String state) {
        return switch (state) {
            case "OPENED_SOUTH_FROM_INSIDE" -> 0;
            case "OPENED_SOUTH_FROM_OUTSIDE" -> 1;
            case "OPENED_NORTH_AND_SOUTH" -> 2;
            case "OPENED_NORTH" -> 3;
            case "OPENED_EAST" -> 4;
            case "OPENED_WEST" -> 5;
            case "CLOSED_SOUTH_FROM_INSIDE" -> 6;
            case "CLOSED_SOUTH_FROM_OUTSIDE" -> 7;
            case "CLOSED_NORTH_AND_SOUTH" -> 8;
            case "CLOSED_NORTH" -> 9;
            case "CLOSED_EAST" -> 10;
            case "CLOSED_WEST" -> 11;
            case "GLASS" -> 12;
            default -> throw new IllegalArgumentException("Unknown state: " + state);
        };
    }


}