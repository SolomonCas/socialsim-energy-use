package com.socialsim.model.core.environment.patchobject.passable.goal;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.controller.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.controller.graphics.amenity.graphic.ChairGraphic;
import com.socialsim.model.core.environment.Patch;

import java.util.List;

public class MeetingChair extends Goal {
    /***** VARIABLES *****/
    public static final MeetingChairFactory meetingChairFactory;
    private final ChairGraphic officeChairGraphic;

    static {
        meetingChairFactory = new MeetingChairFactory();
    }

    /***** CONSTRUCTOR *****/
    protected MeetingChair(List<AmenityBlock> amenityBlocks, boolean enabled, String facing, String type) {
        super(amenityBlocks, enabled);

        this.officeChairGraphic = new ChairGraphic(this, facing, type);
    }

    /***** OVERRIDE *****/
    @Override
    public String toString() {
        return "MeetingChair" + ((this.enabled) ? "" : " (disabled)");
    }

    @Override
    public AmenityGraphic getGraphicObject() {
        return this.officeChairGraphic;
    }

    @Override
    public AmenityGraphicLocation getGraphicLocation() {
        return this.officeChairGraphic.getGraphicLocation();
    }


    /***** INNER STATIC CLASS *****/
    public static class MeetingChairBlock extends AmenityBlock {
        public static MeetingChairBlockFactory meetingChairBlockFactory;

        static {
            meetingChairBlockFactory = new MeetingChairBlockFactory();
        }

        private MeetingChairBlock(Patch patch, boolean attractor, boolean hasGraphic) {
            super(patch, attractor, hasGraphic);
        }

        public static class MeetingChairBlockFactory extends AmenityBlockFactory {
            @Override
            public MeetingChairBlock create(Patch patch, boolean attractor, boolean hasGraphic) {
                return new MeetingChairBlock(patch, attractor, hasGraphic);
            }
        }
    }

    public static class MeetingChairFactory extends GoalFactory {
        public static MeetingChair create(List<AmenityBlock> amenityBlocks, boolean enabled, String facing, String type) {
            return new MeetingChair(amenityBlocks, enabled, facing, type);
        }
    }
}