package com.socialsim.model.core.environment.patchobject.passable.goal;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.controller.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.controller.graphics.amenity.graphic.ChairGraphic;
import com.socialsim.model.core.environment.Patch;

import java.util.List;

public class PantryChair extends Goal {

    /***** VARIABLES *****/
    public static final PantryChairFactory pantryChairFactory;
    private final ChairGraphic pantryChairGraphic;

    static {
        pantryChairFactory = new PantryChairFactory();
    }

    /***** CONSTRUCTOR *****/
    protected PantryChair(List<AmenityBlock> amenityBlocks, boolean enabled, String facing, String type) {
        super(amenityBlocks, enabled);
        this.pantryChairGraphic = new ChairGraphic(this, facing, type);
    }

    /***** OVERRIDE *****/
    @Override
    public String toString() {
        return "Pantry Chair" + ((this.enabled) ? "" : " (disabled)");
    }

    @Override
    public AmenityGraphic getGraphicObject() {
        return this.pantryChairGraphic;
    }

    @Override
    public AmenityGraphicLocation getGraphicLocation() {
        return this.pantryChairGraphic.getGraphicLocation();
    }


    /***** INNER STATIC CLASSES *****/
    public static class PantryChairBlock extends AmenityBlock {
        public static PantryChairBlockFactory pantryChairBlockFactory;

        static {
            pantryChairBlockFactory = new PantryChairBlockFactory();
        }

        private PantryChairBlock(Patch patch, boolean attractor, boolean hasGraphic) {
            super(patch, attractor, hasGraphic);
        }

        public static class PantryChairBlockFactory extends AmenityBlockFactory {
            @Override
            public PantryChairBlock create(Patch patch, boolean attractor, boolean hasGraphic) {
                return new PantryChairBlock(patch, attractor, hasGraphic);
            }
        }
    }

    public static class PantryChairFactory extends GoalFactory {
        public static PantryChair create(List<AmenityBlock> amenityBlocks, boolean enabled, String facing, String type) {
            return new PantryChair(amenityBlocks, enabled, facing, type);
        }
    }
}