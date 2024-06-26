package com.socialsim.model.core.environment.patchobject.passable.goal;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.controller.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.controller.graphics.amenity.graphic.ChairGraphic;
import com.socialsim.model.core.environment.Patch;

import java.util.List;

public class HumanExpChair extends Goal {
    /***** VARIABLES *****/
    public static final HumanExpChairFactory humanExpChairFactory;
    private final ChairGraphic officeChairGraphic;

    static {
        humanExpChairFactory = new HumanExpChairFactory();
    }

    /***** CONSTRUCTOR *****/
    protected HumanExpChair(List<AmenityBlock> amenityBlocks, boolean enabled, String facing, String type) {
        super(amenityBlocks, enabled);

        this.officeChairGraphic = new ChairGraphic(this, facing, type);
    }

    /***** OVERRIDE *****/
    @Override
    public String toString() {
        return "HumanExp Chair" + ((this.enabled) ? "" : " (disabled)");
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
    public static class HumanExpChairBlock extends AmenityBlock {
        public static HumanExpChairBlockFactory humanExpChairBlockFactory;

        static {
            humanExpChairBlockFactory = new HumanExpChairBlockFactory();
        }

        private HumanExpChairBlock(Patch patch, boolean attractor, boolean hasGraphic) {
            super(patch, attractor, hasGraphic);
        }

        public static class HumanExpChairBlockFactory extends AmenityBlockFactory {
            @Override
            public HumanExpChairBlock create(Patch patch, boolean attractor, boolean hasGraphic) {
                return new HumanExpChairBlock(patch, attractor, hasGraphic);
            }
        }
    }

    public static class HumanExpChairFactory extends GoalFactory {
        public static HumanExpChair create(List<AmenityBlock> amenityBlocks, boolean enabled, String facing, String type) {
            return new HumanExpChair(amenityBlocks, enabled, facing, type);
        }
    }
}