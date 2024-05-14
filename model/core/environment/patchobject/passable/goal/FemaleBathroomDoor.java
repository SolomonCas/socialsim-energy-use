package com.socialsim.model.core.environment.patchobject.passable.goal;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.controller.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.controller.graphics.amenity.graphic.FemaleBathroomDoorGraphic;
import com.socialsim.model.core.environment.Patch;

import java.util.List;

public class FemaleBathroomDoor extends Goal {

    /***** VARIABLES *****/
    public static final FemaleBathroomDoor.FemaleBathroomDoorFactory femaleBathroomDoorFactory;
    private final FemaleBathroomDoorGraphic femaleBathroomDoorGraphic;

    static {
        femaleBathroomDoorFactory = new FemaleBathroomDoor.FemaleBathroomDoorFactory();
    }

    /***** CONSTRUCTOR *****/
    protected FemaleBathroomDoor(List<AmenityBlock> amenityBlocks, boolean enabled, String facing) {
        super(amenityBlocks, enabled);

        this.femaleBathroomDoorGraphic = new FemaleBathroomDoorGraphic(this, facing);
    }

    /***** OVERRIDE *****/
    @Override
    public String toString() {
        return "FemaleBathroomDoor" + ((this.enabled) ? "" : " (disabled)");
    }

    @Override
    public AmenityGraphic getGraphicObject() {
        return this.femaleBathroomDoorGraphic;
    }

    @Override
    public AmenityGraphicLocation getGraphicLocation() {
        return this.femaleBathroomDoorGraphic.getGraphicLocation();
    }


    /***** INNER STATIC CLASS *****/
    public static class FemaleBathroomDoorBlock extends AmenityBlock {
        public static FemaleBathroomDoor.FemaleBathroomDoorBlock.FemaleBathroomDoorBlockFactory femaleBathroomDoorBlockFactory;

        static {
            femaleBathroomDoorBlockFactory = new FemaleBathroomDoor.FemaleBathroomDoorBlock.FemaleBathroomDoorBlockFactory();
        }

        private FemaleBathroomDoorBlock(Patch patch, boolean attractor, boolean hasGraphic) {
            super(patch, attractor, hasGraphic);
        }

        public static class FemaleBathroomDoorBlockFactory extends AmenityBlockFactory {
            @Override
            public FemaleBathroomDoor.FemaleBathroomDoorBlock create(Patch patch, boolean attractor, boolean hasGraphic) {
                return new FemaleBathroomDoor.FemaleBathroomDoorBlock(patch, attractor, hasGraphic);
            }
        }
    }

    public static class FemaleBathroomDoorFactory extends GoalFactory {
        public static FemaleBathroomDoor create(List<AmenityBlock> amenityBlocks, boolean enabled, String facing) {
            return new FemaleBathroomDoor(amenityBlocks, enabled, facing);
        }
    }
}