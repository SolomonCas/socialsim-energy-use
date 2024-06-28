package com.socialsim.model.core.environment.patchobject.passable.goal;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.controller.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.controller.graphics.amenity.graphic.AirconGraphic;
import com.socialsim.model.core.environment.Patch;

import java.util.List;

public class Aircon extends Goal {

    /***** VARIABLES *****/
    public static final AirconFactory airconFactory;
    private final AirconGraphic airconGraphic;

    static {
        airconFactory = new AirconFactory();
    }

    /***** CONSTRUCTOR *****/
    protected Aircon(List<AmenityBlock> amenityBlocks, boolean enabled) {
        super(amenityBlocks, enabled);

        this.airconGraphic = new AirconGraphic(this);
    }

    /***** OVERRIDE *****/
    @Override
    public String toString() {
        return "Aircon" + ((this.enabled) ? "" : " (disabled)");
    }

    @Override
    public AmenityGraphic getGraphicObject() {
        return this.airconGraphic;
    }

    @Override
    public AmenityGraphicLocation getGraphicLocation() {
        return this.airconGraphic.getGraphicLocation();
    }


    /***** INNER STATIC CLASS *****/
    public static class AirconBlock extends AmenityBlock {
        public static AirconBlockFactory airconBlockFactory;

        static {
            airconBlockFactory = new AirconBlockFactory();
        }

        private AirconBlock(Patch patch, boolean attractor, boolean hasGraphic) {
            super(patch, attractor, hasGraphic);
        }

        public static class AirconBlockFactory extends AmenityBlockFactory {
            @Override
            public AirconBlock create(Patch patch, boolean attractor, boolean hasGraphic) {
                return new AirconBlock(patch, attractor, hasGraphic);
            }
        }
    }

    public static class AirconFactory extends GoalFactory {
        public static Aircon create(List<AmenityBlock> amenityBlocks, boolean enabled) {
            return new Aircon(amenityBlocks, enabled);
        }
    }

}