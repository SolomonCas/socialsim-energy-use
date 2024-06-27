package com.socialsim.model.core.environment.patchobject.passable.goal;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.controller.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.controller.graphics.amenity.graphic.ChairGraphic;
import com.socialsim.model.core.environment.Patch;

import java.util.List;

public class SoloChair extends Goal {
    /***** VARIABLES *****/
    public static final SoloChairFactory soloChairFactory;
    private final ChairGraphic officeChairGraphic;

    static {
        soloChairFactory = new SoloChairFactory();
    }

    /***** CONSTRUCTOR *****/
    protected SoloChair(List<AmenityBlock> amenityBlocks, boolean enabled, String facing, String type) {
        super(amenityBlocks, enabled);

        this.officeChairGraphic = new ChairGraphic(this, facing, type);
    }

    /***** OVERRIDE *****/
    @Override
    public String toString() {
        return "Solo Chair" + ((this.enabled) ? "" : " (disabled)");
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
    public static class SoloChairBlock extends AmenityBlock {
        public static SoloChairBlockFactory soloChairBlockFactory;

        static {
            soloChairBlockFactory = new SoloChairBlockFactory();
        }

        private SoloChairBlock(Patch patch, boolean attractor, boolean hasGraphic) {
            super(patch, attractor, hasGraphic);
        }

        public static class SoloChairBlockFactory extends AmenityBlockFactory {
            @Override
            public SoloChairBlock create(Patch patch, boolean attractor, boolean hasGraphic) {
                return new SoloChairBlock(patch, attractor, hasGraphic);
            }
        }
    }

    public static class SoloChairFactory extends GoalFactory {
        public static SoloChair create(List<AmenityBlock> amenityBlocks, boolean enabled, String facing, String type) {
            return new SoloChair(amenityBlocks, enabled, facing, type);
        }
    }
}