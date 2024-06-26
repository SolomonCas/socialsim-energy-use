package com.socialsim.model.core.environment.patchobject.passable.goal;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.controller.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.controller.graphics.amenity.graphic.KettleBarGraphic;
import com.socialsim.model.core.environment.Patch;

import java.util.List;

public class KettleBar extends Goal {

    /***** VARIABLES *****/
    public static final KettleBarFactory kettleBarFactory;
    private final KettleBarGraphic kettleBarGraphic;

    static {
        kettleBarFactory = new KettleBarFactory();
    }

    /***** CONSTRUCTOR *****/
    protected KettleBar(List<AmenityBlock> amenityBlocks, boolean enabled) {
        super(amenityBlocks, enabled);

        this.kettleBarGraphic = new KettleBarGraphic(this);
    }

    /***** OVERRIDE *****/
    @Override
    public String toString() {
        return "KettleBar" + ((this.enabled) ? "" : " (disabled)");
    }

    @Override
    public AmenityGraphic getGraphicObject() {
        return this.kettleBarGraphic;
    }

    @Override
    public AmenityGraphicLocation getGraphicLocation() {
        return this.kettleBarGraphic.getGraphicLocation();
    }


    /***** INNER STATIC CLASS *****/
    public static class KettleBarBlock extends AmenityBlock {
        public static KettleBarBlockFactory kettleBarBlockFactory;

        static {
            kettleBarBlockFactory = new KettleBarBlockFactory();
        }

        private KettleBarBlock(Patch patch, boolean attractor, boolean hasGraphic) {
            super(patch, attractor, hasGraphic);
        }

        public static class KettleBarBlockFactory extends AmenityBlockFactory {
            @Override
            public KettleBarBlock create(Patch patch, boolean attractor, boolean hasGraphic) {
                return new KettleBarBlock(patch, attractor, hasGraphic);
            }
        }
    }

    public static class KettleBarFactory extends GoalFactory {
        public static KettleBar create(List<AmenityBlock> amenityBlocks, boolean enabled) {
            return new KettleBar(amenityBlocks, enabled);
        }
    }

}