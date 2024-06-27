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

        /*** CUBICLE ***/
        final List<AmenityGraphicLocation> CubicleGraphic = new ArrayList<>();

        // MESA
        CubicleGraphic.add(new AmenityGraphicLocation(1, 0));  // table on N&W
        CubicleGraphic.add(new AmenityGraphicLocation(1, 4));  // table on N&E
        CubicleGraphic.add(new AmenityGraphicLocation(1, 8));  // table on S&W
        CubicleGraphic.add(new AmenityGraphicLocation(1, 12)); // table on S&E

        // TYPE A
        CubicleGraphic.add(new AmenityGraphicLocation(8, 0));

        // TYPE B
        CubicleGraphic.add(new AmenityGraphicLocation(8, 6)); // FACING WEST
        CubicleGraphic.add(new AmenityGraphicLocation(8, 9)); // FACING EAST

        // TYPE C WITH APPLIANCES
        CubicleGraphic.add(new AmenityGraphicLocation(10, 13)); // FACING NORTH
        CubicleGraphic.add(new AmenityGraphicLocation(10, 16)); // FACING SOUTH
        CubicleGraphic.add(new AmenityGraphicLocation(10, 21)); // FACING WEST
        CubicleGraphic.add(new AmenityGraphicLocation(10, 19)); // FACING EAST

        // TYPE C NO APPLIANCES
        CubicleGraphic.add(new AmenityGraphicLocation(12, 13)); // FACING NORTH
        CubicleGraphic.add(new AmenityGraphicLocation(12, 16)); // FACING SOUTH
        CubicleGraphic.add(new AmenityGraphicLocation(13, 21)); // FACING WEST
        CubicleGraphic.add(new AmenityGraphicLocation(13, 19)); // FACING EAST

        AMENITY_GRAPHICS.put(Cubicle.class, CubicleGraphic);


        /*** Bar ***/
        final List<AmenityGraphicLocation> BarGraphic = new ArrayList<>();

        // 1x1 Bar
        BarGraphic.add(new AmenityGraphicLocation(3, 30)); // 1x1

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


        /*** Research Table ***/
        final List<AmenityGraphicLocation> researchTableGraphic = new ArrayList<>();
        // No Appliance
        researchTableGraphic.add(new AmenityGraphicLocation(1, 17)); // FACING WEST
        researchTableGraphic.add(new AmenityGraphicLocation(1, 18)); // FACING EAST
        researchTableGraphic.add(new AmenityGraphicLocation(6, 17)); // FACING SOUTH
        // With Appliance
        researchTableGraphic.add(new AmenityGraphicLocation(1, 19)); // FACING WEST
        researchTableGraphic.add(new AmenityGraphicLocation(1, 20)); // FACING EAST
        AMENITY_GRAPHICS.put(ResearchTable.class, researchTableGraphic);

        /*** Meeting Table ***/
        final List<AmenityGraphicLocation> meetingTableGraphic = new ArrayList<>();
        meetingTableGraphic.add(new AmenityGraphicLocation(12, 24)); // VERTICAL LARGE
        meetingTableGraphic.add(new AmenityGraphicLocation(14, 28)); // VERTICAL SMALL
        meetingTableGraphic.add(new AmenityGraphicLocation(17, 12)); // HORIZONTAL LARGE
        meetingTableGraphic.add(new AmenityGraphicLocation(21, 12)); // HORIZONTAL SMALL
        AMENITY_GRAPHICS.put(MeetingTable.class, meetingTableGraphic);

        /*** Learning Table ***/
        final List<AmenityGraphicLocation> learningTableGraphic = new ArrayList<>();
        learningTableGraphic.add(new AmenityGraphicLocation(27, 7)); // HORIZONTAL
        learningTableGraphic.add(new AmenityGraphicLocation(24, 13)); // VERTICAL
        AMENITY_GRAPHICS.put(LearningTable.class, learningTableGraphic);

        /*** Pantry Table ***/
        final List<AmenityGraphicLocation> pantryTableGraphic = new ArrayList<>();
        pantryTableGraphic.add(new AmenityGraphicLocation(5, 25)); // TYPE A
        pantryTableGraphic.add(new AmenityGraphicLocation(5, 26)); // TYPE B
        AMENITY_GRAPHICS.put(PantryTable.class, pantryTableGraphic);

        /*** Director Table ***/
        final List<AmenityGraphicLocation> directorTableGraphic = new ArrayList<>();
        directorTableGraphic.add(new AmenityGraphicLocation(14, 6));
        AMENITY_GRAPHICS.put(DirectorTable.class, directorTableGraphic);

        /*** White Board ***/
        final List<AmenityGraphicLocation> whiteboardGraphic = new ArrayList<>();
        whiteboardGraphic.add(new AmenityGraphicLocation(21, 9)); // NORTH
        whiteboardGraphic.add(new AmenityGraphicLocation(18, 6)); // SOUTH
        whiteboardGraphic.add(new AmenityGraphicLocation(21, 8)); // WEST
        whiteboardGraphic.add(new AmenityGraphicLocation(21, 7)); // EAST 4
        whiteboardGraphic.add(new AmenityGraphicLocation(21, 6)); // EAST 11
        AMENITY_GRAPHICS.put(Whiteboard.class, whiteboardGraphic);

        /*** Chair ***/
        final List<AmenityGraphicLocation> chairGraphic = new ArrayList<>();

        // Office Chair
        chairGraphic.add(new AmenityGraphicLocation(24, 17)); // SOUTH
        chairGraphic.add(new AmenityGraphicLocation(24, 18)); // NORTH
        chairGraphic.add(new AmenityGraphicLocation(24, 19)); // EAST
        chairGraphic.add(new AmenityGraphicLocation(24, 20)); // WEST

        // Pantry Chair Type A
        chairGraphic.add(new AmenityGraphicLocation(27, 17)); // SOUTH
        chairGraphic.add(new AmenityGraphicLocation(27, 18)); // NORTH
        chairGraphic.add(new AmenityGraphicLocation(27, 19)); // EAST
        chairGraphic.add(new AmenityGraphicLocation(27, 20)); // WEST

        // Pantry Chair Type B
        chairGraphic.add(new AmenityGraphicLocation(30, 17)); // SOUTH
        chairGraphic.add(new AmenityGraphicLocation(30, 18)); // NORTH
        chairGraphic.add(new AmenityGraphicLocation(30, 19)); // EAST
        chairGraphic.add(new AmenityGraphicLocation(30, 20)); // WEST

        AMENITY_GRAPHICS.put(DirectorChair.class, chairGraphic);
        AMENITY_GRAPHICS.put(LearningChair.class, chairGraphic);
        AMENITY_GRAPHICS.put(MeetingChair.class, chairGraphic);
        AMENITY_GRAPHICS.put(ResearchChair.class, chairGraphic);
        AMENITY_GRAPHICS.put(ReceptionChair.class, chairGraphic);
        AMENITY_GRAPHICS.put(SoloChair.class, chairGraphic);
        AMENITY_GRAPHICS.put(HumanExpChair.class, chairGraphic);
        AMENITY_GRAPHICS.put(DataCollChair.class, chairGraphic);
        AMENITY_GRAPHICS.put(PantryChair.class, chairGraphic);
        AMENITY_GRAPHICS.put(Chair.class, chairGraphic);


        /*** Elevator ***/
        final List<AmenityGraphicLocation> elevatorGraphic = new ArrayList<>();
        elevatorGraphic.add(new AmenityGraphicLocation(14, 30));
        AMENITY_GRAPHICS.put(Elevator.class, elevatorGraphic);

        /*** Couch ***/
        final List<AmenityGraphicLocation> couchGraphic = new ArrayList<>();
        couchGraphic.add(new AmenityGraphicLocation(18, 3));
        AMENITY_GRAPHICS.put(Couch.class, couchGraphic);

        /*** Refrigerator ***/
        final List<AmenityGraphicLocation> RefrigeratorGraphic = new ArrayList<>();
        RefrigeratorGraphic.add(new AmenityGraphicLocation(24, 0));
        AMENITY_GRAPHICS.put(Refrigerator.class, RefrigeratorGraphic);

        /*** Water Dispenser ***/
        final List<AmenityGraphicLocation> WaterDispenserGraphic = new ArrayList<>();
        WaterDispenserGraphic.add(new AmenityGraphicLocation(24, 1));
        AMENITY_GRAPHICS.put(WaterDispenser.class, WaterDispenserGraphic);

        /*** Plant ***/
        final List<AmenityGraphicLocation> PlantGraphic = new ArrayList<>();
        PlantGraphic.add(new AmenityGraphicLocation(28, 1));
        AMENITY_GRAPHICS.put(Plant.class, PlantGraphic);

        /*** Trash Can ***/
        final List<AmenityGraphicLocation> TrashCanGraphic = new ArrayList<>();
        TrashCanGraphic.add(new AmenityGraphicLocation(21, 1));
        AMENITY_GRAPHICS.put(TrashCan.class, TrashCanGraphic);

        /*** Pantry Cabinet ***/
        final List<AmenityGraphicLocation> PantryCabinetGraphic = new ArrayList<>();
        PantryCabinetGraphic.add(new AmenityGraphicLocation(1, 30));
        AMENITY_GRAPHICS.put(PantryCabinet.class, PantryCabinetGraphic);

        /*** Sink ***/
        final List<AmenityGraphicLocation> SinkGraphic = new ArrayList<>();
        SinkGraphic.add(new AmenityGraphicLocation(31, 10)); // SOUTH
        SinkGraphic.add(new AmenityGraphicLocation(31, 11)); // NORTH
        AMENITY_GRAPHICS.put(Sink.class, SinkGraphic);
        AMENITY_GRAPHICS.put(OfficeSink.class, SinkGraphic);

        /*** Toilet ***/
        final List<AmenityGraphicLocation> ToiletGraphic = new ArrayList<>();
        ToiletGraphic.add(new AmenityGraphicLocation(31, 7)); // SOUTH
        ToiletGraphic.add(new AmenityGraphicLocation(31, 8)); // NORTH
        AMENITY_GRAPHICS.put(Toilet.class, ToiletGraphic);
        AMENITY_GRAPHICS.put(OfficeToilet.class, ToiletGraphic);

        /*** Coffee Maker Bar ***/
        final List<AmenityGraphicLocation> CoffeeMakerBarGraphic = new ArrayList<>();
        CoffeeMakerBarGraphic.add(new AmenityGraphicLocation(1, 26));
        AMENITY_GRAPHICS.put(CoffeeMakerBar.class, CoffeeMakerBarGraphic);

        /*** Kettle Bar ***/
        final List<AmenityGraphicLocation> KettleBarGraphic = new ArrayList<>();
        KettleBarGraphic.add(new AmenityGraphicLocation(1, 27));
        AMENITY_GRAPHICS.put(KettleBar.class, KettleBarGraphic);

        /*** Microwave Bar ***/
        final List<AmenityGraphicLocation> MicrowaveBarGraphic = new ArrayList<>();
        MicrowaveBarGraphic.add(new AmenityGraphicLocation(1, 28));
        AMENITY_GRAPHICS.put(MicrowaveBar.class, MicrowaveBarGraphic);




//        final List<AmenityGraphicLocation> cabinetGraphic = new ArrayList<>();
//        cabinetGraphic.add(new AmenityGraphicLocation(10, 0));
//        cabinetGraphic.add(new AmenityGraphicLocation(8, 2));
//        AMENITY_GRAPHICS.put(Cabinet.class, cabinetGraphic);
//
//        final List<AmenityGraphicLocation> storageCabinetGraphic = new ArrayList<>();
//        storageCabinetGraphic.add(new AmenityGraphicLocation(10, 0));
//        storageCabinetGraphic.add(new AmenityGraphicLocation(8, 2));
//        AMENITY_GRAPHICS.put(StorageCabinet.class, storageCabinetGraphic);
//
//
//        final List<AmenityGraphicLocation> doorGraphic = new ArrayList<>();
//        doorGraphic.add(new AmenityGraphicLocation(9, 0));
//        doorGraphic.add(new AmenityGraphicLocation(0, 3));
//        AMENITY_GRAPHICS.put(Door.class, doorGraphic);
//
//        final List<AmenityGraphicLocation> maleBathroomDoorGraphic = new ArrayList<>();
//        maleBathroomDoorGraphic.add(new AmenityGraphicLocation(9, 0));
//        maleBathroomDoorGraphic.add(new AmenityGraphicLocation(0, 3));
//        AMENITY_GRAPHICS.put(MaleBathroomDoor.class, maleBathroomDoorGraphic);
//
//        final List<AmenityGraphicLocation> femaleBathroomDoorGraphic = new ArrayList<>();
//        femaleBathroomDoorGraphic.add(new AmenityGraphicLocation(9, 0));
//        femaleBathroomDoorGraphic.add(new AmenityGraphicLocation(0, 3));
//        AMENITY_GRAPHICS.put(FemaleBathroomDoor.class, femaleBathroomDoorGraphic);
//
//        final List<AmenityGraphicLocation> mainEntranceDoorGraphic = new ArrayList<>();
//        mainEntranceDoorGraphic.add(new AmenityGraphicLocation(9, 0));
//        mainEntranceDoorGraphic.add(new AmenityGraphicLocation(0, 3));
//        AMENITY_GRAPHICS.put(MainEntranceDoor.class, mainEntranceDoorGraphic);
//
//        final List<AmenityGraphicLocation> officeGateGraphic = new ArrayList<>();
//        officeGateGraphic.add(new AmenityGraphicLocation(0, 1));
//        AMENITY_GRAPHICS.put(Gate.class, officeGateGraphic);
//
//
//
//
//        final List<AmenityGraphicLocation> serverGraphic = new ArrayList<>();
//        serverGraphic.add(new AmenityGraphicLocation(16, 2));
//        AMENITY_GRAPHICS.put(Server.class, serverGraphic);


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