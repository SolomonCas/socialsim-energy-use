package com.socialsim.model.core.environment.patchobject.passable.goal;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.controller.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.controller.graphics.amenity.graphic.ToiletGraphic;
import com.socialsim.model.core.environment.Patch;

import java.util.List;

public class Toilet extends Goal {

    /***** VARIABLES *****/
    public static final ToiletFactory toiletFactory;
    private final ToiletGraphic toiletGraphic;

    static {
        toiletFactory = new ToiletFactory();
    }

    /***** CONSTRUCTOR *****/
    protected Toilet(List<AmenityBlock> amenityBlocks, boolean enabled, String facing) {
        super(amenityBlocks, enabled);

        this.toiletGraphic = new ToiletGraphic(this, facing);
    }

    /***** OVERRIDE *****/
    @Override
    public String toString() {
        return "Toilet" + ((this.enabled) ? "" : " (disabled)");
    }

    @Override
    public AmenityGraphic getGraphicObject() {
        return this.toiletGraphic;
    }

    @Override
    public AmenityGraphicLocation getGraphicLocation() {
        return this.toiletGraphic.getGraphicLocation();
    }


    /***** INNER STATIC CLASS *****/
    public static class ToiletBlock extends AmenityBlock {
        public static ToiletBlockFactory toiletBlockFactory;

        static {
            toiletBlockFactory = new ToiletBlockFactory();
        }

        private ToiletBlock(Patch patch, boolean attractor, boolean hasGraphic) {
            super(patch, attractor, hasGraphic);
        }

        public static class ToiletBlockFactory extends AmenityBlockFactory {
            @Override
            public ToiletBlock create(Patch patch, boolean attractor, boolean hasGraphic) {
                return new ToiletBlock(patch, attractor, hasGraphic);
            }
        }
    }

    public static class ToiletFactory extends GoalFactory {
        public static Toilet create(List<AmenityBlock> amenityBlocks, boolean enabled, String facing) {
            return new Toilet(amenityBlocks, enabled, facing);
        }
    }
}