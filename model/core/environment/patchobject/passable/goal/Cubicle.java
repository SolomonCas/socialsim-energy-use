package com.socialsim.model.core.environment.patchobject.passable.goal;

import com.socialsim.controller.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.controller.graphics.amenity.graphic.CubicleGraphic;
import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchobject.Amenity;

import java.util.List;

public class Cubicle extends Goal {

    /***** VARIABLES *****/
    public static final Cubicle.CubicleFactory cubicleFactory;
    private final CubicleGraphic cubicleGraphic;
    static {
        cubicleFactory = new Cubicle.CubicleFactory();
    }
    private final boolean withAppliance;

    /***** CONSTRUCTOR *****/
    protected Cubicle(List<AmenityBlock> amenityBlocks, boolean enabled, String facing, boolean withAppliance) {
        super(amenityBlocks, enabled);
        this.withAppliance = withAppliance;
        this.cubicleGraphic = new CubicleGraphic(this, facing, withAppliance);
    }

    /***** GETTERS *****/
    public boolean withAppliance() {
        return withAppliance;
    }


    /***** OVERRIDE *****/
    @Override
    public String toString() {
        return "Cubicle" + ((this.enabled) ? "" : " (disabled)");
    }

    @Override
    public AmenityGraphic getGraphicObject() {
        return this.cubicleGraphic;
    }

    @Override
    public AmenityGraphicLocation getGraphicLocation() {
        return this.cubicleGraphic.getGraphicLocation();
    }

    /***** INNER STATIC CLASS *****/
    public static class CubicleBlock extends Amenity.AmenityBlock {
        public static Cubicle.CubicleBlock.CubicleBlockFactory cubicleBlockFactory;

        static {
            cubicleBlockFactory = new Cubicle.CubicleBlock.CubicleBlockFactory();
        }

        private CubicleBlock(Patch patch, boolean attractor, boolean hasGraphic) {
            super(patch, attractor, hasGraphic);
        }

        public static class CubicleBlockFactory extends Amenity.AmenityBlock.AmenityBlockFactory {
            @Override
            public Cubicle.CubicleBlock create(Patch patch, boolean attractor, boolean hasGraphic) {
                return new Cubicle.CubicleBlock(patch, attractor, hasGraphic);
            }
        }
    }

    public static class CubicleFactory extends GoalFactory {
        public static Cubicle create(List<AmenityBlock> amenityBlocks, boolean enabled, String facing, boolean withAppliance) {
            return new Cubicle(amenityBlocks, enabled, facing, withAppliance);
        }
    }
}