package com.socialsim.model.core.environment.patchobject.passable.goal;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.controller.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.controller.graphics.amenity.graphic.PantryChairGraphic;
import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchobject.Amenity;

import java.util.List;

public class PantryChair extends Goal {

    /***** VARIABLES *****/
    public static final PantryChair.PantryChairFactory pantryChairFactory;
    private final PantryChairGraphic pantryChairGraphic;

    static {
        pantryChairFactory = new PantryChair.PantryChairFactory();
    }

    /***** CONSTRUCTOR *****/
    protected PantryChair(List<AmenityBlock> amenityBlocks, boolean enabled) {
        super(amenityBlocks, enabled);

        this.pantryChairGraphic = new PantryChairGraphic(this);
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
    public static class PantryChairBlock extends Amenity.AmenityBlock {
        public static PantryChair.PantryChairBlock.PantryChairBlockFactory pantryChairBlockFactory;

        static {
            pantryChairBlockFactory = new PantryChair.PantryChairBlock.PantryChairBlockFactory();
        }

        private PantryChairBlock(Patch patch, boolean attractor, boolean hasGraphic) {
            super(patch, attractor, hasGraphic);
        }

        public static class PantryChairBlockFactory extends Amenity.AmenityBlock.AmenityBlockFactory {
            @Override
            public PantryChair.PantryChairBlock create(Patch patch, boolean attractor, boolean hasGraphic) {
                return new PantryChair.PantryChairBlock(patch, attractor, hasGraphic);
            }
        }
    }

    public static class PantryChairFactory extends GoalFactory {
        public static PantryChair create(List<AmenityBlock> amenityBlocks, boolean enabled) {
            return new PantryChair(amenityBlocks, enabled);
        }
    }
}