package com.socialsim.controller.graphics.amenity;

import com.socialsim.controller.graphics.Graphic;
import com.socialsim.model.core.environment.patchobject.Amenity;
import com.socialsim.model.core.environment.patchobject.passable.gate.Gate;
import com.socialsim.model.core.environment.patchobject.passable.goal.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AmenityGraphic extends Graphic {

    /***** VARIABLES *****/

    // Sprite Sheets
    public static final String AMENITY_SPRITE_SHEET_URL = "com/socialsim/view/image/Office/amenity_spritesheet.png";
    public static final String AMENITY_SPRITE_SHEET_URL2 = "com/socialsim/view/image/University/amenity_spritesheet.png";
    public static final String AMENITY_SPRITE_SHEET_URL3 = "com/socialsim/view/image/Mall/amenity_spritesheet.png";

    public static final HashMap<Class<?>, List<AmenityGraphicLocation>> AMENITY_GRAPHICS = new HashMap<>();

    protected final Amenity amenity;
    protected final List<AmenityGraphicLocation> graphics;
    protected int graphicIndex;
    private final AmenityGraphic.AmenityGraphicScale amenityGraphicScale;
    private final AmenityGraphic.AmenityGraphicOffset amenityGraphicOffset;

    static {
        final List<AmenityGraphicLocation> chairGraphic = new ArrayList<>();
        chairGraphic.add(new AmenityGraphicLocation(0, 0));
        AMENITY_GRAPHICS.put(Chair.class, chairGraphic);

        AMENITY_GRAPHICS.put(AirConditioner.class, chairGraphic);

        final List<AmenityGraphicLocation> collabChairGraphic = new ArrayList<>();
        collabChairGraphic.add(new AmenityGraphicLocation(0, 0));
        AMENITY_GRAPHICS.put(CollabChair.class, collabChairGraphic);

        final List<AmenityGraphicLocation> meetingChairGraphic = new ArrayList<>();
        meetingChairGraphic.add(new AmenityGraphicLocation(0, 0));
        AMENITY_GRAPHICS.put(MeetingChair.class, meetingChairGraphic);

        final List<AmenityGraphicLocation> receptionChairGraphic = new ArrayList<>();
        receptionChairGraphic.add(new AmenityGraphicLocation(0, 0));
        AMENITY_GRAPHICS.put(ReceptionChair.class, receptionChairGraphic);

        final List<AmenityGraphicLocation> pantryChairGraphic = new ArrayList<>();
        pantryChairGraphic.add(new AmenityGraphicLocation(0, 0));
        AMENITY_GRAPHICS.put(PantryChair.class, pantryChairGraphic);

        final List<AmenityGraphicLocation> directorChairGraphic = new ArrayList<>();
        directorChairGraphic.add(new AmenityGraphicLocation(0, 0));
        AMENITY_GRAPHICS.put(DirectorChair.class, directorChairGraphic);

        final List<AmenityGraphicLocation> cabinetGraphic = new ArrayList<>();
        cabinetGraphic.add(new AmenityGraphicLocation(10, 0));
        cabinetGraphic.add(new AmenityGraphicLocation(8, 2));
        AMENITY_GRAPHICS.put(Cabinet.class, cabinetGraphic);

        final List<AmenityGraphicLocation> storageCabinetGraphic = new ArrayList<>();
        storageCabinetGraphic.add(new AmenityGraphicLocation(10, 0));
        storageCabinetGraphic.add(new AmenityGraphicLocation(8, 2));
        AMENITY_GRAPHICS.put(StorageCabinet.class, storageCabinetGraphic);

        final List<AmenityGraphicLocation> collabDeskGraphic = new ArrayList<>();
        collabDeskGraphic.add(new AmenityGraphicLocation(6, 2));
        AMENITY_GRAPHICS.put(CollabDesk.class, collabDeskGraphic);

        final List<AmenityGraphicLocation> couchGraphic = new ArrayList<>();
        couchGraphic.add(new AmenityGraphicLocation(4, 0));
        couchGraphic.add(new AmenityGraphicLocation(4, 2));
        AMENITY_GRAPHICS.put(Couch.class, couchGraphic);

        final List<AmenityGraphicLocation> cubicleGraphic = new ArrayList<>();
        // With Appliance
        cubicleGraphic.add(new AmenityGraphicLocation(2, 0)); // Upward
        cubicleGraphic.add(new AmenityGraphicLocation(2, 2)); // Downward
        cubicleGraphic.add(new AmenityGraphicLocation(14, 2)); // Left
        cubicleGraphic.add(new AmenityGraphicLocation(14, 0)); // Right
        // Without Appliance
        cubicleGraphic.add(new AmenityGraphicLocation(20, 2)); // Upward
        cubicleGraphic.add(new AmenityGraphicLocation(20, 0)); // Downward
        cubicleGraphic.add(new AmenityGraphicLocation(18, 0)); // Left
        cubicleGraphic.add(new AmenityGraphicLocation(18, 2)); // Right

        AMENITY_GRAPHICS.put(Cubicle.class, cubicleGraphic);

        final List<AmenityGraphicLocation> doorGraphic = new ArrayList<>();
        doorGraphic.add(new AmenityGraphicLocation(9, 0));
        doorGraphic.add(new AmenityGraphicLocation(0, 3));
        AMENITY_GRAPHICS.put(Door.class, doorGraphic);

        final List<AmenityGraphicLocation> maleBathroomDoorGraphic = new ArrayList<>();
        maleBathroomDoorGraphic.add(new AmenityGraphicLocation(9, 0));
        maleBathroomDoorGraphic.add(new AmenityGraphicLocation(0, 3));
        AMENITY_GRAPHICS.put(MaleBathroomDoor.class, maleBathroomDoorGraphic);

        final List<AmenityGraphicLocation> femaleBathroomDoorGraphic = new ArrayList<>();
        femaleBathroomDoorGraphic.add(new AmenityGraphicLocation(9, 0));
        femaleBathroomDoorGraphic.add(new AmenityGraphicLocation(0, 3));
        AMENITY_GRAPHICS.put(FemaleBathroomDoor.class, femaleBathroomDoorGraphic);

        final List<AmenityGraphicLocation> mainEntranceDoorGraphic = new ArrayList<>();
        mainEntranceDoorGraphic.add(new AmenityGraphicLocation(9, 0));
        mainEntranceDoorGraphic.add(new AmenityGraphicLocation(0, 3));
        AMENITY_GRAPHICS.put(MainEntranceDoor.class, mainEntranceDoorGraphic);

        final List<AmenityGraphicLocation> meetingDeskGraphic = new ArrayList<>();
        meetingDeskGraphic.add(new AmenityGraphicLocation(6, 2));
        AMENITY_GRAPHICS.put(MeetingDesk.class, meetingDeskGraphic);

        final List<AmenityGraphicLocation> officeGateGraphic = new ArrayList<>();
        officeGateGraphic.add(new AmenityGraphicLocation(0, 1));
        AMENITY_GRAPHICS.put(Gate.class, officeGateGraphic);

        final List<AmenityGraphicLocation> officeDeskGraphic = new ArrayList<>();
        officeDeskGraphic.add(new AmenityGraphicLocation(12, 0));
        AMENITY_GRAPHICS.put(OfficeDesk.class, officeDeskGraphic);

        final List<AmenityGraphicLocation> directorTableGraphic = new ArrayList<>();
        directorTableGraphic.add(new AmenityGraphicLocation(12, 0)); // Horizontal
        directorTableGraphic.add(new AmenityGraphicLocation(12, 0)); // Vertical
        AMENITY_GRAPHICS.put(DirectorTable.class, directorTableGraphic);

        final List<AmenityGraphicLocation> plantGraphic = new ArrayList<>();
        plantGraphic.add(new AmenityGraphicLocation(0, 1));
        AMENITY_GRAPHICS.put(Plant.class, plantGraphic);

        final List<AmenityGraphicLocation> receptionTable = new ArrayList<>();
        receptionTable.add(new AmenityGraphicLocation(7, 0));
        AMENITY_GRAPHICS.put(ReceptionTable.class, receptionTable);

        final List<AmenityGraphicLocation> tableGraphic = new ArrayList<>();
        tableGraphic.add(new AmenityGraphicLocation(6, 0));
        tableGraphic.add(new AmenityGraphicLocation(10, 3));
        AMENITY_GRAPHICS.put(Table.class, tableGraphic);

        final List<AmenityGraphicLocation> pantryTableGraphic = new ArrayList<>();
        pantryTableGraphic.add(new AmenityGraphicLocation(6, 0)); // Horizontal
        pantryTableGraphic.add(new AmenityGraphicLocation(10, 3)); // Vertical
        AMENITY_GRAPHICS.put(PantryTable.class, pantryTableGraphic);

        final List<AmenityGraphicLocation> sinkGraphic = new ArrayList<>();
        sinkGraphic.add(new AmenityGraphicLocation(16, 1));
        AMENITY_GRAPHICS.put(Sink.class, sinkGraphic);

        final List<AmenityGraphicLocation> officeSinkGraphic = new ArrayList<>();
        officeSinkGraphic.add(new AmenityGraphicLocation(16, 1));
        AMENITY_GRAPHICS.put(OfficeSink.class, officeSinkGraphic);

        final List<AmenityGraphicLocation> toiletGraphic = new ArrayList<>();
        toiletGraphic.add(new AmenityGraphicLocation(16, 0));
        AMENITY_GRAPHICS.put(Toilet.class, toiletGraphic);

        final List<AmenityGraphicLocation> officeToiletGraphic = new ArrayList<>();
        officeToiletGraphic.add(new AmenityGraphicLocation(16, 0));
        AMENITY_GRAPHICS.put(OfficeToilet.class, officeToiletGraphic);

        final List<AmenityGraphicLocation> whiteboardGraphic = new ArrayList<>();
        whiteboardGraphic.add(new AmenityGraphicLocation(10, 2));
        whiteboardGraphic.add(new AmenityGraphicLocation(16, 0));
        AMENITY_GRAPHICS.put(Whiteboard.class, whiteboardGraphic);

        final List<AmenityGraphicLocation> waterDispenserGraphic = new ArrayList<>();
        waterDispenserGraphic.add(new AmenityGraphicLocation(12, 2));
        AMENITY_GRAPHICS.put(WaterDispenser.class, waterDispenserGraphic);

        final List<AmenityGraphicLocation> fridgeGraphic = new ArrayList<>();
        fridgeGraphic.add(new AmenityGraphicLocation(12, 3));
        AMENITY_GRAPHICS.put(Fridge.class, fridgeGraphic);

        final List<AmenityGraphicLocation> trashGraphic = new ArrayList<>();
        trashGraphic.add(new AmenityGraphicLocation(1, 1));
        AMENITY_GRAPHICS.put(Trash.class, trashGraphic);

        final List<AmenityGraphicLocation> serverGraphic = new ArrayList<>();
        serverGraphic.add(new AmenityGraphicLocation(16, 2));
        AMENITY_GRAPHICS.put(Server.class, serverGraphic);


    }


    /***** CONSTRUCTOR *****/
    public AmenityGraphic(Amenity amenity, int rowSpan, int columnSpan, int rowOffset, int columnOffset) {
        this.amenity = amenity;

        this.amenityGraphicScale = new AmenityGraphic.AmenityGraphicScale(rowSpan, columnSpan);
        this.amenityGraphicOffset = new AmenityGraphic.AmenityGraphicOffset(rowOffset, columnOffset);

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
    public AmenityGraphic.AmenityGraphicScale getAmenityGraphicScale() {
        return amenityGraphicScale;
    }

    public AmenityGraphic.AmenityGraphicOffset getAmenityGraphicOffset() {
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