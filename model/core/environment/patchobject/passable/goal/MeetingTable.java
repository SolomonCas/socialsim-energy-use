package com.socialsim.model.core.environment.patchobject.passable.goal;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.controller.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.controller.graphics.amenity.graphic.DirectorTableGraphic;
import com.socialsim.controller.graphics.amenity.graphic.MeetingTableGraphic;
import com.socialsim.model.core.environment.Patch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MeetingTable extends Goal {


    /***** VARIABLES *****/
    public static final MeetingTableFactory meetingTableFactory;
    private final MeetingTableGraphic meetingTableGraphic;
    private final List<MeetingChair> meetingChairs;
    static {
        meetingTableFactory = new MeetingTable.MeetingTableFactory();
    }


    /***** CONSTRUCTOR *****/
    protected MeetingTable(List<AmenityBlock> amenityBlocks, boolean enabled, String orientation, String size) {
        super(amenityBlocks, enabled);
        this.meetingTableGraphic = new MeetingTableGraphic(this, orientation, size);
        this.meetingChairs = Collections.synchronizedList(new ArrayList<>());
    }


    /***** GETTERS *****/
    public List<MeetingChair> getMeetingChairs() {
        return meetingChairs;
    }



    /***** OVERRIDE *****/
    @Override
    public String toString() {
        return "Meeting Table" + ((this.enabled) ? "" : " (disabled)");
    }

    @Override
    public AmenityGraphic getGraphicObject() {
        return this.meetingTableGraphic;
    }

    @Override
    public AmenityGraphicLocation getGraphicLocation() {
        return this.meetingTableGraphic.getGraphicLocation();
    }




    /***** INNER STATIC CLASSES *****/
    public static class MeetingTableBlock extends AmenityBlock {
        public static MeetingTable.MeetingTableBlock.MeetingTableBlockFactory meetingTableBlockFactory;

        static {
            meetingTableBlockFactory = new MeetingTable.MeetingTableBlock.MeetingTableBlockFactory();
        }

        private MeetingTableBlock(Patch patch, boolean attractor, boolean hasGraphic) {
            super(patch, attractor, hasGraphic);
        }

        public static class MeetingTableBlockFactory extends AmenityBlockFactory {
            @Override
            public MeetingTable.MeetingTableBlock create(Patch patch, boolean attractor, boolean hasGraphic) {
                return new MeetingTable.MeetingTableBlock(patch, attractor, hasGraphic);
            }
        }
    }

    public static class MeetingTableFactory extends GoalFactory {
        public static MeetingTable create(List<AmenityBlock> amenityBlocks, boolean enabled, String orientation, String size) {
            return new MeetingTable(amenityBlocks, enabled, orientation, size);
        }
    }
}
