package com.socialsim.model.core.environment.patchobject.passable.goal;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.controller.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.controller.graphics.amenity.graphic.ReceptionChairGraphic;
import com.socialsim.model.core.environment.Patch;

import java.util.List;

public class ReceptionChair extends Goal {
    /***** VARIABLES *****/
    public static final ReceptionChairFactory receptionChairFactory;
    private final ReceptionChairGraphic receptionChairGraphic;

    static {
        receptionChairFactory = new ReceptionChairFactory();
    }

    /***** CONSTRUCTOR *****/
    protected ReceptionChair(List<AmenityBlock> amenityBlocks, boolean enabled) {
        super(amenityBlocks, enabled);

        this.receptionChairGraphic = new ReceptionChairGraphic(this);
    }

    /***** OVERRIDE *****/
    @Override
    public String toString() {
        return "ReceptionChair" + ((this.enabled) ? "" : " (disabled)");
    }

    @Override
    public AmenityGraphic getGraphicObject() {
        return this.receptionChairGraphic;
    }

    @Override
    public AmenityGraphicLocation getGraphicLocation() {
        return this.receptionChairGraphic.getGraphicLocation();
    }


    /***** INNER STATIC CLASS *****/
    public static class ReceptionChairBlock extends AmenityBlock {
        public static ReceptionChairBlockFactory receptionChairBlockFactory;

        static {
            receptionChairBlockFactory = new ReceptionChairBlockFactory();
        }

        private ReceptionChairBlock(Patch patch, boolean attractor, boolean hasGraphic) {
            super(patch, attractor, hasGraphic);
        }

        public static class ReceptionChairBlockFactory extends AmenityBlockFactory {
            @Override
            public ReceptionChairBlock create(Patch patch, boolean attractor, boolean hasGraphic) {
                return new ReceptionChairBlock(patch, attractor, hasGraphic);
            }
        }
    }

    public static class ReceptionChairFactory extends GoalFactory {
        public static ReceptionChair create(List<AmenityBlock> amenityBlocks, boolean enabled) {
            return new ReceptionChair(amenityBlocks, enabled);
        }
    }
}