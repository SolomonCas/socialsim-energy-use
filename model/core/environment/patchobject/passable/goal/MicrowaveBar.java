package com.socialsim.model.core.environment.patchobject.passable.goal;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.controller.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.controller.graphics.amenity.graphic.MicrowaveBarGraphic;
import com.socialsim.model.core.environment.Patch;

import java.util.List;

public class MicrowaveBar extends Goal {

    /***** VARIABLES *****/
    public static final MicrowaveBarFactory microwaveBarFactory;
    private final MicrowaveBarGraphic microwaveBarGraphic;

    static {
        microwaveBarFactory = new MicrowaveBarFactory();
    }

    /***** CONSTRUCTOR *****/
    protected MicrowaveBar(List<AmenityBlock> amenityBlocks, boolean enabled) {
        super(amenityBlocks, enabled);

        this.microwaveBarGraphic = new MicrowaveBarGraphic(this);
    }

    /***** OVERRIDE *****/
    @Override
    public String toString() {
        return "Microwave Bar" + ((this.enabled) ? "" : " (disabled)");
    }

    @Override
    public AmenityGraphic getGraphicObject() {
        return this.microwaveBarGraphic;
    }

    @Override
    public AmenityGraphicLocation getGraphicLocation() {
        return this.microwaveBarGraphic.getGraphicLocation();
    }


    /***** INNER STATIC CLASS *****/
    public static class MicrowaveBarBlock extends AmenityBlock {
        public static MicrowaveBarBlockFactory microwaveBarBlockFactory;

        static {
            microwaveBarBlockFactory = new MicrowaveBarBlockFactory();
        }

        private MicrowaveBarBlock(Patch patch, boolean attractor, boolean hasGraphic) {
            super(patch, attractor, hasGraphic);
        }

        public static class MicrowaveBarBlockFactory extends AmenityBlockFactory {
            @Override
            public MicrowaveBarBlock create(Patch patch, boolean attractor, boolean hasGraphic) {
                return new MicrowaveBarBlock(patch, attractor, hasGraphic);
            }
        }
    }

    public static class MicrowaveBarFactory extends GoalFactory {
        public static MicrowaveBar create(List<AmenityBlock> amenityBlocks, boolean enabled) {
            return new MicrowaveBar(amenityBlocks, enabled);
        }
    }

}