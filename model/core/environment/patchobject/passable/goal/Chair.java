package com.socialsim.model.core.environment.patchobject.passable.goal;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.controller.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.controller.graphics.amenity.graphic.ChairGraphic;
import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchobject.Amenity;

import java.util.List;

public class Chair extends Goal {

    /***** VARIABLES *****/
    public static final ChairFactory chairFactory;
    private final ChairGraphic chairGraphic;

    static {
        chairFactory = new ChairFactory();
    }

    /***** CONSTRUCTOR *****/
    protected Chair(List<AmenityBlock> amenityBlocks, boolean enabled, String facing, String type) {
        super(amenityBlocks, enabled);

        this.chairGraphic = new ChairGraphic(this, facing, type);
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
    public static class ChairBlock extends AmenityBlock {
        public static ChairBlockFactory chairBlockFactory;

        static {
            chairBlockFactory = new ChairBlockFactory();
        }

        private ChairBlock(Patch patch, boolean attractor, boolean hasGraphic) {
            super(patch, attractor, hasGraphic);
        }

        public static class ChairBlockFactory extends AmenityBlockFactory {
            @Override
            public ChairBlock create(Patch patch, boolean attractor, boolean hasGraphic) {
                return new ChairBlock(patch, attractor, hasGraphic);
            }
        }
    }

    public static class ChairFactory extends GoalFactory {
        public static Chair create(List<AmenityBlock> amenityBlocks, boolean enabled, String facing, String type) {
            return new Chair(amenityBlocks, enabled, facing, type);
        }
    }
}