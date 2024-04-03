package com.socialsim.model.core.environment.patchobject.passable.goal;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.controller.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.controller.graphics.amenity.graphic.ChairGraphic;
import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchobject.Amenity;

import java.util.List;

public class Chair extends Goal {

    /***** VARIABLES *****/
    public static final Chair.ChairFactory chairFactory;
    private final ChairGraphic chairGraphic;

    static {
        chairFactory = new Chair.ChairFactory();
    }

    /***** CONSTRUCTOR *****/
    protected Chair(List<AmenityBlock> amenityBlocks, boolean enabled) {
        super(amenityBlocks, enabled);

        this.chairGraphic = new ChairGraphic(this);
    }

    /***** OVERRIDE *****/
    @Override
    public String toString() {
        return "Chair" + ((this.enabled) ? "" : " (disabled)");
    }

    @Override
    public AmenityGraphic getGraphicObject() {
        return this.chairGraphic;
    }

    @Override
    public AmenityGraphicLocation getGraphicLocation() {
        return this.chairGraphic.getGraphicLocation();
    }


    /***** INNER STATIC CLASSES *****/
    public static class ChairBlock extends Amenity.AmenityBlock {
        public static Chair.ChairBlock.ChairBlockFactory chairBlockFactory;

        static {
            chairBlockFactory = new Chair.ChairBlock.ChairBlockFactory();
        }

        private ChairBlock(Patch patch, boolean attractor, boolean hasGraphic) {
            super(patch, attractor, hasGraphic);
        }

        public static class ChairBlockFactory extends Amenity.AmenityBlock.AmenityBlockFactory {
            @Override
            public Chair.ChairBlock create(Patch patch, boolean attractor, boolean hasGraphic) {
                return new Chair.ChairBlock(patch, attractor, hasGraphic);
            }
        }
    }

    public static class ChairFactory extends GoalFactory {
        public static Chair create(List<AmenityBlock> amenityBlocks, boolean enabled) {
            return new Chair(amenityBlocks, enabled);
        }
    }
}