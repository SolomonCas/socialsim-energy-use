package com.socialsim.model.core.environment.patchobject.passable.goal;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.controller.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.controller.graphics.amenity.graphic.MaleBathroomDoorGraphic;
import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchobject.Amenity;

import java.util.List;

public class MaleBathroomDoor extends Goal {

    /***** VARIABLES *****/
    public static final MaleBathroomDoor.MaleBathroomDoorFactory maleBathroomDoorFactory;
    private final MaleBathroomDoorGraphic maleBathroomDoorGraphic;

    static {
        maleBathroomDoorFactory = new MaleBathroomDoor.MaleBathroomDoorFactory();
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
    public static class MaleBathroomDoorBlock extends Amenity.AmenityBlock {
        public static MaleBathroomDoor.MaleBathroomDoorBlock.MaleBathroomDoorBlockFactory maleBathroomDoorBlockFactory;

        static {
            maleBathroomDoorBlockFactory = new MaleBathroomDoor.MaleBathroomDoorBlock.MaleBathroomDoorBlockFactory();
        }

        private MaleBathroomDoorBlock(Patch patch, boolean attractor, boolean hasGraphic) {
            super(patch, attractor, hasGraphic);
        }

        public static class MaleBathroomDoorBlockFactory extends Amenity.AmenityBlock.AmenityBlockFactory {
            @Override
            public MaleBathroomDoor.MaleBathroomDoorBlock create(Patch patch, boolean attractor, boolean hasGraphic) {
                return new MaleBathroomDoor.MaleBathroomDoorBlock(patch, attractor, hasGraphic);
            }
        }
    }

    public static class MaleBathroomDoorFactory extends GoalFactory {
        public static MaleBathroomDoor create(List<AmenityBlock> amenityBlocks, boolean enabled, String facing) {
            return new MaleBathroomDoor(amenityBlocks, enabled, facing);
        }
    }
}