package com.socialsim.model.core.environment.patchobject.passable.goal;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.controller.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.controller.graphics.amenity.graphic.LightGraphic;
import com.socialsim.model.core.environment.Patch;

import java.util.List;

public class Light extends Goal {

    /***** VARIABLES *****/
    public static final LightFactory lightFactory;
    private final LightGraphic lightGraphic;

    static {
        lightFactory = new LightFactory();
    }

    /***** CONSTRUCTOR *****/
    protected Light(List<AmenityBlock> amenityBlocks, boolean enabled, String type, String orientation) {
        super(amenityBlocks, enabled);

        this.lightGraphic = new LightGraphic(this, type, orientation);
    }

    /***** OVERRIDE *****/
    @Override
    public String toString() {
        return "Light" + ((this.enabled) ? "" : " (disabled)");
    }

    @Override
    public AmenityGraphic getGraphicObject() {
        return this.lightGraphic;
    }

    @Override
    public AmenityGraphicLocation getGraphicLocation() {
        return this.lightGraphic.getGraphicLocation();
    }


    /***** INNER STATIC CLASS *****/
    public static class LightBlock extends AmenityBlock {
        public static LightBlockFactory lightBlockFactory;

        static {
            lightBlockFactory = new LightBlockFactory();
        }

        private LightBlock(Patch patch, boolean attractor, boolean hasGraphic) {
            super(patch, attractor, hasGraphic);
        }

        public static class LightBlockFactory extends AmenityBlockFactory {
            @Override
            public LightBlock create(Patch patch, boolean attractor, boolean hasGraphic) {
                return new LightBlock(patch, attractor, hasGraphic);
            }
        }
    }

    public static class LightFactory extends GoalFactory {
        public static Light create(List<AmenityBlock> amenityBlocks, boolean enabled, String type, String orientation) {
            return new Light(amenityBlocks, enabled, type, orientation);
        }
    }

}