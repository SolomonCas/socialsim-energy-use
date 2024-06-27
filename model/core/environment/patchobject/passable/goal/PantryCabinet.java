package com.socialsim.model.core.environment.patchobject.passable.goal;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.controller.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.controller.graphics.amenity.graphic.PantryCabinetGraphic;
import com.socialsim.model.core.environment.Patch;

import java.util.List;

public class PantryCabinet extends Goal {

    /***** VARIABLES *****/
    public static final PantryCabinetFactory pantryCabinetFactory;
    private final PantryCabinetGraphic pantryCabinetGraphic;

    static {
        pantryCabinetFactory = new PantryCabinetFactory();
    }

    /***** CONSTRUCTOR *****/
    protected PantryCabinet(List<AmenityBlock> amenityBlocks, boolean enabled) {
        super(amenityBlocks, enabled);

        this.pantryCabinetGraphic = new PantryCabinetGraphic(this);
    }

    /***** OVERRIDE *****/
    @Override
    public String toString() {
        return "PantryCabinet" + ((this.enabled) ? "" : " (disabled)");
    }

    @Override
    public AmenityGraphic getGraphicObject() {
        return this.pantryCabinetGraphic;
    }

    @Override
    public AmenityGraphicLocation getGraphicLocation() {
        return this.pantryCabinetGraphic.getGraphicLocation();
    }


    /***** INNER STATIC CLASS *****/
    public static class PantryCabinetBlock extends AmenityBlock {
        public static PantryCabinetBlockFactory pantryCabinetBlockFactory;

        static {
            pantryCabinetBlockFactory = new PantryCabinetBlockFactory();
        }

        private PantryCabinetBlock(Patch patch, boolean attractor, boolean hasGraphic) {
            super(patch, attractor, hasGraphic);
        }

        public static class PantryCabinetBlockFactory extends AmenityBlockFactory {
            @Override
            public PantryCabinetBlock create(Patch patch, boolean attractor, boolean hasGraphic) {
                return new PantryCabinetBlock(patch, attractor, hasGraphic);
            }
        }
    }

    public static class PantryCabinetFactory extends GoalFactory {
        public static PantryCabinet create(List<AmenityBlock> amenityBlocks, boolean enabled) {
            return new PantryCabinet(amenityBlocks, enabled);
        }
    }

}