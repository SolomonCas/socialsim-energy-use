package com.socialsim.model.core.environment.patchobject.passable.goal;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.controller.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.controller.graphics.amenity.graphic.SinkGraphic;
import com.socialsim.model.core.environment.Patch;

import java.util.List;

public class Sink extends Goal {

    /***** VARIABLES *****/
    public static final SinkFactory sinkFactory;
    private final SinkGraphic sinkGraphic;

    static {
        sinkFactory = new SinkFactory();
    }

    /***** CONSTRUCTOR *****/
    protected Sink(List<AmenityBlock> amenityBlocks, boolean enabled, String facing) {
        super(amenityBlocks, enabled);

        this.sinkGraphic = new SinkGraphic(this, facing);
    }

    /***** OVERRIDE *****/
    @Override
    public String toString() {
        return "Sink" + ((this.enabled) ? "" : " (disabled)");
    }

    @Override
    public AmenityGraphic getGraphicObject() {
        return this.sinkGraphic;
    }

    @Override
    public AmenityGraphicLocation getGraphicLocation() {
        return this.sinkGraphic.getGraphicLocation();
    }


    /***** INNER STATIC CLASS *****/
    public static class SinkBlock extends AmenityBlock {
        public static SinkBlockFactory sinkBlockFactory;

        static {
            sinkBlockFactory = new SinkBlockFactory();
        }

        private SinkBlock(Patch patch, boolean attractor, boolean hasGraphic) {
            super(patch, attractor, hasGraphic);
        }

        public static class SinkBlockFactory extends AmenityBlockFactory {
            @Override
            public SinkBlock create(Patch patch, boolean attractor, boolean hasGraphic) {
                return new SinkBlock(patch, attractor, hasGraphic);
            }
        }
    }

    public static class SinkFactory extends GoalFactory {
        public static Sink create(List<AmenityBlock> amenityBlocks, boolean enabled, String facing) {
            return new Sink(amenityBlocks, enabled, facing);
        }
    }
}