package com.socialsim.model.core.environment.patchobject.passable.goal;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.controller.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.controller.graphics.amenity.graphic.WaterDispenserGraphic;
import com.socialsim.model.core.environment.Patch;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class WaterDispenser extends QueueableGoal implements Serializable {

    /***** VARIABLES *****/
    @Serial
    private static final long serialVersionUID = -5458621245735102190L;
    public static final WaterDispenserFactory waterDispenserFactory;
    private final WaterDispenserGraphic waterDispenserGraphic;

    static {
        waterDispenserFactory = new WaterDispenserFactory();
    }

    /***** CONSTRUCTOR *****/
    protected WaterDispenser(List<AmenityBlock> amenityBlocks, boolean enabled, int waitingTime) {
        super(amenityBlocks, enabled, waitingTime);

        this.waterDispenserGraphic = new WaterDispenserGraphic(this);
    }

    /***** OVERRIDE *****/
    @Override
    public String toString() {
        return "Water Dispenser" + ((this.enabled) ? "" : " (disabled)");
    }

    @Override
    public AmenityGraphic getGraphicObject() {
        return this.waterDispenserGraphic;
    }

    @Override
    public AmenityGraphicLocation getGraphicLocation() {
        return this.waterDispenserGraphic.getGraphicLocation();
    }


    /***** INNER STATIC CLASS *****/
    public static class WaterDispenserBlock extends AmenityBlock {
        public static WaterDispenserBlockFactory waterDispenserBlockFactory;

        static {
            waterDispenserBlockFactory = new WaterDispenserBlockFactory();
        }

        private WaterDispenserBlock(Patch patch, boolean attractor, boolean hasGraphic) {
            super(patch, attractor, hasGraphic);
        }

        public static class WaterDispenserBlockFactory extends AmenityBlockFactory {
            @Override
            public WaterDispenserBlock create(Patch patch, boolean attractor, boolean hasGraphic) {
                return new WaterDispenserBlock(patch, attractor, hasGraphic);
            }
        }
    }

    public static class WaterDispenserFactory extends Goal.GoalFactory {
        public static WaterDispenser create(List<AmenityBlock> amenityBlocks, boolean enabled, int waitingTime) {
            return new WaterDispenser(amenityBlocks, enabled, waitingTime);
        }
    }
}