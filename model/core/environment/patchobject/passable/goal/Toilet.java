package com.socialsim.model.core.environment.patchobject.passable.goal;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.controller.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.controller.graphics.amenity.graphic.ToiletGraphic;
import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchobject.Amenity;

import java.util.List;

public class Toilet extends Goal {

    /***** VARIABLES *****/
    public static final Toilet.ToiletFactory toiletFactory;
    private final ToiletGraphic toiletGraphic;

    static {
        toiletFactory = new Toilet.ToiletFactory();
    }

    /***** CONSTRUCTOR *****/
    protected Toilet(List<AmenityBlock> amenityBlocks, boolean enabled) {
        super(amenityBlocks, enabled);

        this.toiletGraphic = new ToiletGraphic(this);
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
    public static class ToiletBlock extends Amenity.AmenityBlock {
        public static Toilet.ToiletBlock.ToiletBlockFactory toiletBlockFactory;

        static {
            toiletBlockFactory = new Toilet.ToiletBlock.ToiletBlockFactory();
        }

        private ToiletBlock(Patch patch, boolean attractor, boolean hasGraphic) {
            super(patch, attractor, hasGraphic);
        }

        public static class ToiletBlockFactory extends Amenity.AmenityBlock.AmenityBlockFactory {
            @Override
            public Toilet.ToiletBlock create(Patch patch, boolean attractor, boolean hasGraphic) {
                return new Toilet.ToiletBlock(patch, attractor, hasGraphic);
            }
        }
    }

    public static class ToiletFactory extends GoalFactory {
        public static Toilet create(List<AmenityBlock> amenityBlocks, boolean enabled) {
            return new Toilet(amenityBlocks, enabled);
        }
    }
}