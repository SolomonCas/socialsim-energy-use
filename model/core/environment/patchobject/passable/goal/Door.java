package com.socialsim.model.core.environment.patchobject.passable.goal;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.controller.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.controller.graphics.amenity.graphic.DoorGraphic;
import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchobject.Amenity;

import java.util.List;

public class Door extends Goal {

    /***** VARIABLES *****/
    public static final Door.DoorFactory doorFactory;
    private final DoorGraphic doorGraphic;

    static {
        doorFactory = new Door.DoorFactory();
    }

    /***** CONSTRUCTOR *****/
    protected Door(List<AmenityBlock> amenityBlocks, boolean enabled, String facing) {
        super(amenityBlocks, enabled);

        this.doorGraphic = new DoorGraphic(this, facing);
    }

    /***** OVERRIDE *****/
    @Override
    public String toString() {
        return "Door" + ((this.enabled) ? "" : " (disabled)");
    }

    @Override
    public AmenityGraphic getGraphicObject() {
        return this.doorGraphic;
    }

    @Override
    public AmenityGraphicLocation getGraphicLocation() {
        return this.doorGraphic.getGraphicLocation();
    }


    /***** INNER STATIC CLASS *****/
    public static class DoorBlock extends Amenity.AmenityBlock {
        public static Door.DoorBlock.DoorBlockFactory doorBlockFactory;

        static {
            doorBlockFactory = new Door.DoorBlock.DoorBlockFactory();
        }

        private DoorBlock(Patch patch, boolean attractor, boolean hasGraphic) {
            super(patch, attractor, hasGraphic);
        }

        public static class DoorBlockFactory extends Amenity.AmenityBlock.AmenityBlockFactory {
            @Override
            public Door.DoorBlock create(Patch patch, boolean attractor, boolean hasGraphic) {
                return new Door.DoorBlock(patch, attractor, hasGraphic);
            }
        }
    }

    public static class DoorFactory extends GoalFactory {
        public static Door create(List<AmenityBlock> amenityBlocks, boolean enabled, String facing) {
            return new Door(amenityBlocks, enabled, facing);
        }
    }
}