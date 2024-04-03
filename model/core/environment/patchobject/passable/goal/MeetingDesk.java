package com.socialsim.model.core.environment.patchobject.passable.goal;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.controller.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.controller.graphics.amenity.graphic.MeetingDeskGraphic;
import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchobject.Amenity;

import java.util.List;

public class MeetingDesk extends Goal {

    /***** VARIABLES *****/
    public static final MeetingDesk.MeetingDeskFactory meetingDeskFactory;
    private final MeetingDeskGraphic meetingDeskGraphic;

    static {
        meetingDeskFactory = new MeetingDesk.MeetingDeskFactory();
    }

    /***** CONSTRUCTOR *****/
    protected MeetingDesk(List<AmenityBlock> amenityBlocks, boolean enabled, String facing) {
        super(amenityBlocks, enabled);

        this.meetingDeskGraphic = new MeetingDeskGraphic(this, facing);
    }

    /***** OVERRIDE *****/
    @Override
    public String toString() {
        return "MeetingDesk" + ((this.enabled) ? "" : " (disabled)");
    }

    @Override
    public AmenityGraphic getGraphicObject() {
        return this.meetingDeskGraphic;
    }

    @Override
    public AmenityGraphicLocation getGraphicLocation() {
        return this.meetingDeskGraphic.getGraphicLocation();
    }


    /***** INNER STATIC CLASS *****/
    public static class MeetingDeskBlock extends Amenity.AmenityBlock {
        public static MeetingDesk.MeetingDeskBlock.MeetingDeskBlockFactory meetingDeskBlockFactory;

        static {
            meetingDeskBlockFactory = new MeetingDesk.MeetingDeskBlock.MeetingDeskBlockFactory();
        }

        private MeetingDeskBlock(Patch patch, boolean attractor, boolean hasGraphic) {
            super(patch, attractor, hasGraphic);
        }

        public static class MeetingDeskBlockFactory extends Amenity.AmenityBlock.AmenityBlockFactory {
            @Override
            public MeetingDesk.MeetingDeskBlock create(Patch patch, boolean attractor, boolean hasGraphic) {
                return new MeetingDesk.MeetingDeskBlock(patch, attractor, hasGraphic);
            }
        }
    }

    public static class MeetingDeskFactory extends GoalFactory {
        public static MeetingDesk create(List<AmenityBlock> amenityBlocks, boolean enabled, String facing) {
            return new MeetingDesk(amenityBlocks, enabled, facing);
        }
    }
}