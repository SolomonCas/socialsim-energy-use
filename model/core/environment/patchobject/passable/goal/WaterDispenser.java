package com.socialsim.model.core.environment.patchobject.passable.goal;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.controller.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.controller.graphics.amenity.graphic.WaterDispenserGraphic;
import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchobject.Amenity;

import java.util.List;

public class WaterDispenser extends Goal {

    /***** VARIABLES *****/
    public static final WaterDispenser.WaterDispenserFactory waterDispenserFactory;
    private final WaterDispenserGraphic waterDispenserGraphic;

    static {
        waterDispenserFactory = new WaterDispenser.WaterDispenserFactory();
    }

    /***** CONSTRUCTOR *****/
    protected WaterDispenser(List<AmenityBlock> amenityBlocks, boolean enabled) {
        super(amenityBlocks, enabled);

        this.waterDispenserGraphic = new WaterDispenserGraphic(this);
    }

    /***** OVERRIDE *****/
    @Override
    public String toString() {
        return "WaterDispenser" + ((this.enabled) ? "" : " (disabled)");
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
    public static class WaterDispenserBlock extends Amenity.AmenityBlock {
        public static WaterDispenser.WaterDispenserBlock.WaterDispenserBlockFactory waterDispenserBlockFactory;

        static {
            waterDispenserBlockFactory = new WaterDispenser.WaterDispenserBlock.WaterDispenserBlockFactory();
        }

        private WaterDispenserBlock(Patch patch, boolean attractor, boolean hasGraphic) {
            super(patch, attractor, hasGraphic);
        }

        public static class WaterDispenserBlockFactory extends Amenity.AmenityBlock.AmenityBlockFactory {
            @Override
            public WaterDispenser.WaterDispenserBlock create(Patch patch, boolean attractor, boolean hasGraphic) {
                return new WaterDispenser.WaterDispenserBlock(patch, attractor, hasGraphic);
            }
        }
    }

    public static class WaterDispenserFactory extends GoalFactory {
        public static WaterDispenser create(List<AmenityBlock> amenityBlocks, boolean enabled) {
            return new WaterDispenser(amenityBlocks, enabled);
        }
    }
}