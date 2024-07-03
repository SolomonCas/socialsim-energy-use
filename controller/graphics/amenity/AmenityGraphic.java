package com.socialsim.controller.graphics.amenity;

import com.socialsim.controller.graphics.Graphic;
import com.socialsim.model.core.environment.patchobject.Amenity;
import com.socialsim.model.core.environment.patchobject.passable.elevator.Elevator;
import com.socialsim.model.core.environment.patchobject.passable.goal.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AmenityGraphic extends Graphic {

    /***** VARIABLES *****/

    // Sprite Sheets
    public static final String AMENITIES_SPRITE_SHEET_URL = "com/socialsim/view/spritesheets/Amenities Sprite Sheet.png";
    public static final HashMap<Class<?>, List<AmenityGraphicLocation>> AMENITY_GRAPHICS = new HashMap<>();

    protected final Amenity amenity;
    protected final List<AmenityGraphicLocation> graphics;
    protected int graphicIndex;
    private final AmenityGraphicScale amenityGraphicScale;
    private final AmenityGraphicOffset amenityGraphicOffset;

    static {

        /* ELEVATOR */
        final List<AmenityGraphicLocation> ElevatorGraphic = new ArrayList<>();
        ElevatorGraphic.add(new AmenityGraphicLocation(14, 30));
        AMENITY_GRAPHICS.put(Elevator.class, ElevatorGraphic);


        /* *** *** *** *** *** *** *** *** *** *** */


        /* BAR */

        final List<AmenityGraphicLocation> BarGraphic = new ArrayList<>();

        // 1x1 Bar
        BarGraphic.add(new AmenityGraphicLocation(4, 30)); // 1x1

        // Vertical Bars
        BarGraphic.add(new AmenityGraphicLocation(1, 22)); // 5x1
        BarGraphic.add(new AmenityGraphicLocation(1, 23)); // 4x1
        BarGraphic.add(new AmenityGraphicLocation(1, 24)); // 2x1

        // Horizontal Bars
        BarGraphic.add(new AmenityGraphicLocation(8, 23)); // 1x8
        BarGraphic.add(new AmenityGraphicLocation(9, 25)); // 1x6
        BarGraphic.add(new AmenityGraphicLocation(10, 28)); // 1x3
        BarGraphic.add(new AmenityGraphicLocation(11, 29)); // 1x2

        AMENITY_GRAPHICS.put(ReceptionTable.class, BarGraphic);
        AMENITY_GRAPHICS.put(SoloTable.class, BarGraphic);
        AMENITY_GRAPHICS.put(HumanExpTable.class, BarGraphic);
        AMENITY_GRAPHICS.put(DataCollTable.class, BarGraphic);


        // TODO: What if we just merge all tables in one graphic & mapper?
        /* DIRECTOR TABLE */

        final List<AmenityGraphicLocation> directorTableGraphic = new ArrayList<>();
        directorTableGraphic.add(new AmenityGraphicLocation(28, 14)); // HORIZONTAL
        directorTableGraphic.add(new AmenityGraphicLocation(27, 12)); // VERTICAL
        AMENITY_GRAPHICS.put(DirectorTable.class, directorTableGraphic);


        /* LEARNING TABLE */
        final List<AmenityGraphicLocation> LearningTableGraphic = new ArrayList<>();
        LearningTableGraphic.add(new AmenityGraphicLocation(23, 8)); // HORIZONTAL
        LearningTableGraphic.add(new AmenityGraphicLocation(21, 14)); // VERTICAL
        AMENITY_GRAPHICS.put(LearningTable.class, LearningTableGraphic);

        /* MEETING TABLE */

        final List<AmenityGraphicLocation> MeetingTableGraphic = new ArrayList<>();
        MeetingTableGraphic.add(new AmenityGraphicLocation(12, 24)); // VERTICAL LARGE
        MeetingTableGraphic.add(new AmenityGraphicLocation(14, 28)); // VERTICAL SMALL
        MeetingTableGraphic.add(new AmenityGraphicLocation(14, 13)); // HORIZONTAL LARGE
        MeetingTableGraphic.add(new AmenityGraphicLocation(18, 13)); // HORIZONTAL SMALL
        AMENITY_GRAPHICS.put(MeetingTable.class, MeetingTableGraphic);


        /* RESEARCH TABLE */

        final List<AmenityGraphicLocation> ResearchTableGraphic = new ArrayList<>();
        ResearchTableGraphic.add(new AmenityGraphicLocation(1, 18)); // FACING WEST
        ResearchTableGraphic.add(new AmenityGraphicLocation(1, 19)); // FACING EAST
        ResearchTableGraphic.add(new AmenityGraphicLocation(6, 17)); // FACING SOUTH
        AMENITY_GRAPHICS.put(ResearchTable.class, ResearchTableGraphic);


        /* PANTRY TABLE */

        final List<AmenityGraphicLocation> PantryTableGraphic = new ArrayList<>();
        PantryTableGraphic.add(new AmenityGraphicLocation(5, 25)); // TYPE A
        PantryTableGraphic.add(new AmenityGraphicLocation(5, 26)); // TYPE B
        AMENITY_GRAPHICS.put(PantryTable.class, PantryTableGraphic);


        /* 2x2 TABLE */

        final List<AmenityGraphicLocation> Table2x2Graphic = new ArrayList<>();
        Table2x2Graphic.add(new AmenityGraphicLocation(21, 17)); // 1 variation
        AMENITY_GRAPHICS.put(Table2x2.class, Table2x2Graphic);


        /* CHAIR */

        final List<AmenityGraphicLocation> ChairGraphic = new ArrayList<>();

        // Office Chair
        ChairGraphic.add(new AmenityGraphicLocation(24, 19)); // SOUTH
        ChairGraphic.add(new AmenityGraphicLocation(24, 20)); // NORTH
        ChairGraphic.add(new AmenityGraphicLocation(24, 21)); // EAST
        ChairGraphic.add(new AmenityGraphicLocation(24, 22)); // WEST

        // Pantry Chair Type A
        ChairGraphic.add(new AmenityGraphicLocation(27, 19)); // SOUTH
        ChairGraphic.add(new AmenityGraphicLocation(27, 20)); // NORTH
        ChairGraphic.add(new AmenityGraphicLocation(27, 21)); // EAST
        ChairGraphic.add(new AmenityGraphicLocation(27, 22)); // WEST

        // Pantry Chair Type B
        ChairGraphic.add(new AmenityGraphicLocation(30, 19)); // SOUTH
        ChairGraphic.add(new AmenityGraphicLocation(30, 20)); // NORTH
        ChairGraphic.add(new AmenityGraphicLocation(30, 21)); // EAST
        ChairGraphic.add(new AmenityGraphicLocation(30, 22)); // WEST

        AMENITY_GRAPHICS.put(Chair.class, ChairGraphic);


        /* COUCH */

        final List<AmenityGraphicLocation> CouchGraphic = new ArrayList<>();
        CouchGraphic.add(new AmenityGraphicLocation(18, 3));
        AMENITY_GRAPHICS.put(Couch.class, CouchGraphic);


        /* CUBICLE */

        final List<AmenityGraphicLocation> CubicleGraphic = new ArrayList<>();

        // MESA
        CubicleGraphic.add(new AmenityGraphicLocation(1, 0));  // TABLE ON NORTH & WEST
        CubicleGraphic.add(new AmenityGraphicLocation(1, 4));  // TABLE ON NORTH & EAST
        CubicleGraphic.add(new AmenityGraphicLocation(1, 8));  // TABLE ON SOUTH & WEST
        CubicleGraphic.add(new AmenityGraphicLocation(1, 12)); // TABLE ON SOUTH & EAST

        // TYPE A
        CubicleGraphic.add(new AmenityGraphicLocation(8, 0)); //  BACK-TO-BACK SPECIAL

        // TYPE B
        CubicleGraphic.add(new AmenityGraphicLocation(8, 6)); // FACING WEST
        CubicleGraphic.add(new AmenityGraphicLocation(8, 9)); // FACING EAST

        // TYPE C
        CubicleGraphic.add(new AmenityGraphicLocation(10, 13)); // FACING NORTH
        CubicleGraphic.add(new AmenityGraphicLocation(10, 16)); // FACING SOUTH
        CubicleGraphic.add(new AmenityGraphicLocation(10, 21)); // FACING WEST
        CubicleGraphic.add(new AmenityGraphicLocation(10, 19)); // FACING EAST

        AMENITY_GRAPHICS.put(Cubicle.class, CubicleGraphic);
        AMENITY_GRAPHICS.put(MESATable.class, CubicleGraphic);

        /* PLANT */

        final List<AmenityGraphicLocation> PlantGraphic = new ArrayList<>();
        PlantGraphic.add(new AmenityGraphicLocation(28, 1));
        AMENITY_GRAPHICS.put(Plant.class, PlantGraphic);


        /* STORAGE */
        final List<AmenityGraphicLocation> StorageGraphic = new ArrayList<>();

        // Regular Cabinets
        StorageGraphic.add(new AmenityGraphicLocation(27, 27)); // SOUTH
        StorageGraphic.add(new AmenityGraphicLocation(27, 29)); // NORTH

        // Drawers
        StorageGraphic.add(new AmenityGraphicLocation(24, 27)); // FACING SOUTH
        StorageGraphic.add(new AmenityGraphicLocation(24, 28)); // FACING NORTH
        StorageGraphic.add(new AmenityGraphicLocation(24, 29)); // FACING EAST
        StorageGraphic.add(new AmenityGraphicLocation(24, 30)); // FACING WEST

        // Double Drawers
        StorageGraphic.add(new AmenityGraphicLocation(23, 24)); // FACING EAST
        StorageGraphic.add(new AmenityGraphicLocation(23, 25)); // FACING WEST

        AMENITY_GRAPHICS.put(StorageCabinet.class, StorageGraphic);


        /* SINK */

        final List<AmenityGraphicLocation> SinkGraphic = new ArrayList<>();
        SinkGraphic.add(new AmenityGraphicLocation(34, 3)); // SOUTH
        SinkGraphic.add(new AmenityGraphicLocation(34, 4)); // NORTH
        AMENITY_GRAPHICS.put(Sink.class, SinkGraphic);
        AMENITY_GRAPHICS.put(OfficeSink.class, SinkGraphic);


        /* TOILET */

        final List<AmenityGraphicLocation> ToiletGraphic = new ArrayList<>();
        ToiletGraphic.add(new AmenityGraphicLocation(34, 0)); // SOUTH
        ToiletGraphic.add(new AmenityGraphicLocation(34, 1)); // NORTH
        AMENITY_GRAPHICS.put(Toilet.class, ToiletGraphic);
        AMENITY_GRAPHICS.put(OfficeToilet.class, ToiletGraphic);


        /* TRASH CAN */

        final List<AmenityGraphicLocation> TrashCanGraphic = new ArrayList<>();
        TrashCanGraphic.add(new AmenityGraphicLocation(21, 1));
        AMENITY_GRAPHICS.put(TrashCan.class, TrashCanGraphic);


        /* WHITEBOARD */

        final List<AmenityGraphicLocation> WhiteboardGraphic = new ArrayList<>();
        WhiteboardGraphic.add(new AmenityGraphicLocation(17, 9)); // NORTH
        WhiteboardGraphic.add(new AmenityGraphicLocation(14, 6)); // SOUTH
        WhiteboardGraphic.add(new AmenityGraphicLocation(17, 8)); // WEST
        WhiteboardGraphic.add(new AmenityGraphicLocation(17, 7)); // EAST 4
        WhiteboardGraphic.add(new AmenityGraphicLocation(17, 6)); // EAST 11
        AMENITY_GRAPHICS.put(Whiteboard.class, WhiteboardGraphic);


        /* WINDOW + BLINDS */

        final List<AmenityGraphicLocation> WindowBlindsGraphic = new ArrayList<>();

        // Opened Blinds
        WindowBlindsGraphic.add(new AmenityGraphicLocation(32, 6)); // SOUTH (FROM THE INSIDE)
        WindowBlindsGraphic.add(new AmenityGraphicLocation(32, 7)); // SOUTH (FROM THE OUTSIDE)
        WindowBlindsGraphic.add(new AmenityGraphicLocation(32, 8)); // NORTH
        WindowBlindsGraphic.add(new AmenityGraphicLocation(32, 9)); // EAST
        WindowBlindsGraphic.add(new AmenityGraphicLocation(32, 10)); // WEST

        // Closed Blinds
        WindowBlindsGraphic.add(new AmenityGraphicLocation(32, 11)); // SOUTH (FROM THE INSIDE)
        WindowBlindsGraphic.add(new AmenityGraphicLocation(32, 12)); // SOUTH (FROM THE OUTSIDE)
        WindowBlindsGraphic.add(new AmenityGraphicLocation(32, 13)); // NORTH
        WindowBlindsGraphic.add(new AmenityGraphicLocation(32, 14)); // EAST
        WindowBlindsGraphic.add(new AmenityGraphicLocation(32, 15)); // WEST

        // Glass
        WindowBlindsGraphic.add(new AmenityGraphicLocation(35, 9)); // SOUTH

        AMENITY_GRAPHICS.put(WindowBlinds.class, WindowBlindsGraphic);



        /* *** *** *** *** *** FOR CONSUMING ENERGY *** *** *** *** *** */


        /* COFFEE MAKER BAR */

        final List<AmenityGraphicLocation> CoffeeMakerBarGraphic = new ArrayList<>();
        CoffeeMakerBarGraphic.add(new AmenityGraphicLocation(1, 26));
        AMENITY_GRAPHICS.put(CoffeeMakerBar.class, CoffeeMakerBarGraphic);


        /* MICROWAVE BAR */
        final List<AmenityGraphicLocation> MicrowaveBarGraphic = new ArrayList<>();
        MicrowaveBarGraphic.add(new AmenityGraphicLocation(1, 28));
        AMENITY_GRAPHICS.put(MicrowaveBar.class, MicrowaveBarGraphic);


        /* KETTLE BAR */

        final List<AmenityGraphicLocation> KettleBarGraphic = new ArrayList<>();
        KettleBarGraphic.add(new AmenityGraphicLocation(1, 27));
        AMENITY_GRAPHICS.put(KettleBar.class, KettleBarGraphic);


        /* WATER DISPENSER */

        final List<AmenityGraphicLocation> WaterDispenserGraphic = new ArrayList<>();
        WaterDispenserGraphic.add(new AmenityGraphicLocation(24, 1));
        AMENITY_GRAPHICS.put(WaterDispenser.class, WaterDispenserGraphic);


        /* REFRIGERATOR */

        final List<AmenityGraphicLocation> RefrigeratorGraphic = new ArrayList<>();
        RefrigeratorGraphic.add(new AmenityGraphicLocation(24, 0));
        AMENITY_GRAPHICS.put(Refrigerator.class, RefrigeratorGraphic);


        /* MONITOR */

        final List<AmenityGraphicLocation> MonitorGraphic = new ArrayList<>();
        MonitorGraphic.add(new AmenityGraphicLocation(28, 7));  // SOUTH
        MonitorGraphic.add(new AmenityGraphicLocation(28, 8));  // NORTH
        MonitorGraphic.add(new AmenityGraphicLocation(30, 8));  // NORTH (BEHIND CUBICLE WALL)
        MonitorGraphic.add(new AmenityGraphicLocation(28, 9));  // EAST
        MonitorGraphic.add(new AmenityGraphicLocation(28, 10)); // WEST
        AMENITY_GRAPHICS.put(Monitor.class, MonitorGraphic);


        /* SWITCH */

        final List<AmenityGraphicLocation> SwitchGraphic = new ArrayList<>();

        // For Light
        SwitchGraphic.add(new AmenityGraphicLocation(33, 17)); // SOUTH
        SwitchGraphic.add(new AmenityGraphicLocation(33, 18)); // NORTH
        SwitchGraphic.add(new AmenityGraphicLocation(33, 19)); // EAST
        SwitchGraphic.add(new AmenityGraphicLocation(33, 20)); // WEST

        // For AC
        SwitchGraphic.add(new AmenityGraphicLocation(36, 17)); // SOUTH
        SwitchGraphic.add(new AmenityGraphicLocation(36, 18)); // NORTH
        SwitchGraphic.add(new AmenityGraphicLocation(36, 19)); // EAST
        SwitchGraphic.add(new AmenityGraphicLocation(36, 20)); // WEST

        AMENITY_GRAPHICS.put(Switch.class, SwitchGraphic);


        /* AIR CON */

        final List<AmenityGraphicLocation> AirconGraphic = new ArrayList<>();
        AirconGraphic.add(new AmenityGraphicLocation(35, 14));
        AMENITY_GRAPHICS.put(Aircon.class, AirconGraphic);


        /* LIGHT */

        final List<AmenityGraphicLocation> LightGraphic = new ArrayList<>();

        // Single Pendant Light
        LightGraphic.add(new AmenityGraphicLocation(36, 24));

        // Linear Pendant Light
        LightGraphic.add(new AmenityGraphicLocation(33, 23)); // HORIZONTAL
        LightGraphic.add(new AmenityGraphicLocation(33, 22)); // VERTICAL

        // Recessed Linear Light
        LightGraphic.add(new AmenityGraphicLocation(35, 29)); // HORIZONTAL
        LightGraphic.add(new AmenityGraphicLocation(35, 28)); // VERTICAL

        // Track Light
        LightGraphic.add(new AmenityGraphicLocation(32, 27)); // HORIZONTAL
        LightGraphic.add(new AmenityGraphicLocation(32, 26)); // VERTICAL

        AMENITY_GRAPHICS.put(Light.class, LightGraphic);



        /* *** *** *** *** *** ONLY FOR DECORATION *** *** *** *** *** */


        /* PANTRY CABINET */

        final List<AmenityGraphicLocation> PantryCabinetGraphic = new ArrayList<>();
        PantryCabinetGraphic.add(new AmenityGraphicLocation(1, 30));
        AMENITY_GRAPHICS.put(PantryCabinet.class, PantryCabinetGraphic);


        /* SERVER */

        final List<AmenityGraphicLocation> ServerGraphic = new ArrayList<>();
        ServerGraphic.add(new AmenityGraphicLocation(27, 24)); // TYPE A
        ServerGraphic.add(new AmenityGraphicLocation(27, 25)); // TYPE B
        AMENITY_GRAPHICS.put(Server.class, ServerGraphic);


        /* BOX */

        final List<AmenityGraphicLocation> BoxGraphic = new ArrayList<>();
        BoxGraphic.add(new AmenityGraphicLocation(28, 0)); // 1x1
        BoxGraphic.add(new AmenityGraphicLocation(30, 0)); // 2x1
        BoxGraphic.add(new AmenityGraphicLocation(30, 3)); // 2x2
//        AMENITY_GRAPHICS.put(Box.class, BoxGraphic);

    }


    /***** CONSTRUCTOR *****/
    public AmenityGraphic(Amenity amenity, int rowSpan, int columnSpan, int rowOffset, int columnOffset) {
        this.amenity = amenity;

        this.amenityGraphicScale = new AmenityGraphicScale(rowSpan, columnSpan);
        this.amenityGraphicOffset = new AmenityGraphicOffset(rowOffset, columnOffset);

        this.graphics = new ArrayList<>();

        for (AmenityGraphicLocation amenityGraphicLocation : AMENITY_GRAPHICS.get(amenity.getClass())) {
            AmenityGraphicLocation newAmenityGraphicLocation = new AmenityGraphicLocation(amenityGraphicLocation.getGraphicRow(), amenityGraphicLocation.getGraphicColumn());

            newAmenityGraphicLocation.setGraphicWidth(columnSpan);
            newAmenityGraphicLocation.setGraphicHeight(rowSpan);
            this.graphics.add(newAmenityGraphicLocation);
        }

        this.graphicIndex = 0;
    }




    /***** GETTERS *****/
    public AmenityGraphicScale getAmenityGraphicScale() {
        return amenityGraphicScale;
    }

    public AmenityGraphicOffset getAmenityGraphicOffset() {
        return amenityGraphicOffset;
    }

    public Amenity getAmenity() {
        return amenity;
    }

    public AmenityGraphicLocation getGraphicLocation() {
        return this.graphics.get(this.graphicIndex);
    }



    /***** INNER STATIC CLASSES *****/
    public static class AmenityGraphicScale {
        private int rowSpan;
        private int columnSpan;

        public AmenityGraphicScale(int rowSpan, int columnSpan) {
            this.rowSpan = rowSpan;
            this.columnSpan = columnSpan;
        }

        public int getRowSpan() {
            return rowSpan;
        }

        public void setRowSpan(int rowSpan) {
            this.rowSpan = rowSpan;
        }

        public int getColumnSpan() {
            return columnSpan;
        }

        public void setColumnSpan(int columnSpan) {
            this.columnSpan = columnSpan;
        }
    }

    public static class AmenityGraphicOffset {
        private int rowOffset;
        private int columnOffset;

        public AmenityGraphicOffset(int rowOffset, int columnOffset) {
            this.rowOffset = rowOffset;
            this.columnOffset = columnOffset;
        }

        public int getRowOffset() {
            return rowOffset;
        }

        public void setRowOffset(int rowOffset) {
            this.rowOffset = rowOffset;
        }

        public int getColumnOffset() {
            return columnOffset;
        }

        public void setColumnOffset(int columnOffset) {
            this.columnOffset = columnOffset;
        }
    }

}