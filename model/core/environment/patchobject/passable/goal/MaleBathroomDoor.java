package com.socialsim.model.core.environment.patchobject.passable.goal;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.controller.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.controller.graphics.amenity.graphic.MaleBathroomDoorGraphic;
import com.socialsim.model.core.environment.Patch;

import java.util.List;

public class MaleBathroomDoor extends Goal {

    /***** VARIABLES *****/
    public static final MaleBathroomDoorFactory maleBathroomDoorFactory;
    private final MaleBathroomDoorGraphic maleBathroomDoorGraphic;

    static {
        maleBathroomDoorFactory = new MaleBathroomDoorFactory();
    }

    /***** CONSTRUCTOR *****/
    protected MaleBathroomDoor(List<AmenityBlock> amenityBlocks, boolean enabled, String facing) {
        super(amenityBlocks, enabled);

        this.maleBathroomDoorGraphic = new MaleBathroomDoorGraphic(this, facing);
    }

    /***** OVERRIDE *****/
    @Override
    public String toString() {
        return "MaleBathroomDoor" + ((this.enabled) ? "" : " (disabled)");
    }

    @Override
    public AmenityGraphic getGraphicObject() {
        return this.maleBathroomDoorGraphic;
    }

    @Override
    public AmenityGraphicLocation getGraphicLocation() {
        return this.maleBathroomDoorGraphic.getGraphicLocation();
    }


    /***** INNER STATIC CLASS *****/
    public static class MaleBathroomDoorBlock extends AmenityBlock {
        public static MaleBathroomDoorBlockFactory maleBathroomDoorBlockFactory;

        static {
            maleBathroomDoorBlockFactory = new MaleBathroomDoorBlockFactory();
        }

        private MaleBathroomDoorBlock(Patch patch, boolean attractor, boolean hasGraphic) {
            super(patch, attractor, hasGraphic);
        }

        public static class MaleBathroomDoorBlockFactory extends AmenityBlockFactory {
            @Override
            public MaleBathroomDoorBlock create(Patch patch, boolean attractor, boolean hasGraphic) {
                return new MaleBathroomDoorBlock(patch, attractor, hasGraphic);
            }
        }
    }

    public static class MaleBathroomDoorFactory extends GoalFactory {
        public static MaleBathroomDoor create(List<AmenityBlock> amenityBlocks, boolean enabled, String facing) {
            return new MaleBathroomDoor(amenityBlocks, enabled, facing);
        }
    }
}