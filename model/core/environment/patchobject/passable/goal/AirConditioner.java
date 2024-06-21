package com.socialsim.model.core.environment.patchobject.passable.goal;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.controller.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.controller.graphics.amenity.graphic.AirConditionerGraphic;
import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchobject.Amenity;

import java.util.List;

public class AirConditioner extends Goal {

    /***** VARIABLES *****/
    public static final AirConditioner.AirConditionerFactory airConditionerFactory;
    private final AirConditionerGraphic airConditionerGraphic;

    static {
        airConditionerFactory = new AirConditioner.AirConditionerFactory();
    }

    /***** CONSTRUCTOR *****/
    protected AirConditioner(List<AmenityBlock> amenityBlocks, boolean enabled) {
        super(amenityBlocks, enabled);

        this.airConditionerGraphic = new AirConditionerGraphic(this);
    }

    /***** OVERRIDE *****/
    @Override
    public String toString() {
        return "AirConditioner" + ((this.enabled) ? "" : " (disabled)");
    }

    @Override
    public AmenityGraphic getGraphicObject() {
        return this.airConditionerGraphic;
    }

    @Override
    public AmenityGraphicLocation getGraphicLocation() {
        return this.airConditionerGraphic.getGraphicLocation();
    }


    /***** INNER STATIC CLASSES *****/
    public static class AirConditionerBlock extends Amenity.AmenityBlock {
        public static AirConditioner.AirConditionerBlock.AirConditionerBlockFactory airConditionerBlockFactory;

        static {
            airConditionerBlockFactory = new AirConditioner.AirConditionerBlock.AirConditionerBlockFactory();
        }

        private AirConditionerBlock(Patch patch, boolean attractor, boolean hasGraphic) {
            super(patch, attractor, hasGraphic);
        }

        public static class AirConditionerBlockFactory extends Amenity.AmenityBlock.AmenityBlockFactory {
            @Override
            public AirConditioner.AirConditionerBlock create(Patch patch, boolean attractor, boolean hasGraphic) {
                return new AirConditioner.AirConditionerBlock(patch, attractor, hasGraphic);
            }
        }
    }

    public static class AirConditionerFactory extends GoalFactory {
        public static AirConditioner create(List<AmenityBlock> amenityBlocks, boolean enabled) {
            return new AirConditioner(amenityBlocks, enabled);
        }
    }
}