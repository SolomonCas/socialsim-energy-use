package com.socialsim.model.core.environment.patchobject.passable.goal;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.controller.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.controller.graphics.amenity.graphic.ChairGraphic;
import com.socialsim.model.core.environment.Patch;

import java.util.List;

public class ResearchChair extends Goal {
    /***** VARIABLES *****/
    public static final ResearchChairFactory researchChairFactory;
    private final ChairGraphic officeChairGraphic;

    static {
        researchChairFactory = new ResearchChairFactory();
    }

    /***** CONSTRUCTOR *****/
    protected ResearchChair(List<AmenityBlock> amenityBlocks, boolean enabled, String facing, String type) {
        super(amenityBlocks, enabled);

        this.officeChairGraphic = new ChairGraphic(this, facing, type);
    }

    /***** OVERRIDE *****/
    @Override
    public String toString() {
        return "ResearchChair" + ((this.enabled) ? "" : " (disabled)");
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
    public static class ResearchChairBlock extends AmenityBlock {
        public static ResearchChairBlockFactory researchChairBlockFactory;

        static {
            researchChairBlockFactory = new ResearchChairBlockFactory();
        }

        private ResearchChairBlock(Patch patch, boolean attractor, boolean hasGraphic) {
            super(patch, attractor, hasGraphic);
        }

        public static class ResearchChairBlockFactory extends AmenityBlockFactory {
            @Override
            public ResearchChairBlock create(Patch patch, boolean attractor, boolean hasGraphic) {
                return new ResearchChairBlock(patch, attractor, hasGraphic);
            }
        }
    }

    public static class ResearchChairFactory extends GoalFactory {
        public static ResearchChair create(List<AmenityBlock> amenityBlocks, boolean enabled, String facing, String type) {
            return new ResearchChair(amenityBlocks, enabled, facing, type);
        }
    }
}