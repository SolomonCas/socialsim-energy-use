package com.socialsim.model.core.environment.patchobject.passable.goal;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.controller.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.controller.graphics.amenity.graphic.DoorGraphic;
import com.socialsim.model.core.environment.Patch;

import java.util.List;

public class Door extends Goal {

    /***** VARIABLES *****/
    public static final DoorFactory doorFactory;
    private final DoorGraphic doorGraphic;

    static {
        doorFactory = new DoorFactory();
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
    public static class DoorBlock extends AmenityBlock {
        public static DoorBlockFactory doorBlockFactory;

        static {
            doorBlockFactory = new DoorBlockFactory();
        }

        private DoorBlock(Patch patch, boolean attractor, boolean hasGraphic) {
            super(patch, attractor, hasGraphic);
        }

        public static class DoorBlockFactory extends AmenityBlockFactory {
            @Override
            public DoorBlock create(Patch patch, boolean attractor, boolean hasGraphic) {
                return new DoorBlock(patch, attractor, hasGraphic);
            }
        }
    }

    public static class DoorFactory extends GoalFactory {
        public static Door create(List<AmenityBlock> amenityBlocks, boolean enabled, String facing) {
            return new Door(amenityBlocks, enabled, facing);
        }
    }
}