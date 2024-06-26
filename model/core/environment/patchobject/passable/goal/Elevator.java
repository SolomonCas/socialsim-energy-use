package com.socialsim.model.core.environment.patchobject.passable.goal;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.controller.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.controller.graphics.amenity.graphic.ElevatorGraphic;
import com.socialsim.model.core.environment.Patch;

import java.util.List;

public class Elevator extends Goal {

    /***** VARIABLES *****/
    public static final ElevatorFactory elevatorFactory;
    private final ElevatorGraphic elevatorGraphic;


    static {
        elevatorFactory = new ElevatorFactory();
    }

    /***** CONSTRUCTOR *****/
    protected Elevator(List<AmenityBlock> amenityBlocks, boolean enabled, String orientation) {
        super(amenityBlocks, enabled);
        this.elevatorGraphic = new ElevatorGraphic(this, orientation);
    }


    /***** OVERRIDE *****/
    @Override
    public String toString() {
        return "Elevator" + ((this.enabled) ? "" : " (disabled)");
    }

    @Override
    public AmenityGraphic getGraphicObject() {
        return this.elevatorGraphic;
    }

    @Override
    public AmenityGraphicLocation getGraphicLocation() {
        return this.elevatorGraphic.getGraphicLocation();
    }


    /***** INNER STATIC CLASS *****/
    public static class ElevatorBlock extends AmenityBlock {
        public static ElevatorBlockFactory elevatorBlockFactory;

        static {
            elevatorBlockFactory = new ElevatorBlockFactory();
        }

        private ElevatorBlock(Patch patch, boolean attractor, boolean hasGraphic) {
            super(patch, attractor, hasGraphic);
        }

        public static class ElevatorBlockFactory extends AmenityBlockFactory {
            @Override
            public ElevatorBlock create(Patch patch, boolean attractor, boolean hasGraphic) {
                return new ElevatorBlock(patch, attractor, hasGraphic);
            }
        }
    }

    public static class ElevatorFactory extends GoalFactory {
        public static Elevator create(List<AmenityBlock> amenityBlocks, boolean enabled, String orientation) {
            return new Elevator(amenityBlocks, enabled, orientation);
        }
    }
}