package com.socialsim.model.core.environment.patchobject.passable.goal;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.controller.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.controller.graphics.amenity.graphic.CollabChairGraphic;
import com.socialsim.model.core.environment.Patch;

import java.util.List;

public class CollabChair extends Goal {
    /***** VARIABLES *****/
    public static final CollabChairFactory collabChairFactory;
    private final CollabChairGraphic collabChairGraphic;

    static {
        collabChairFactory = new CollabChairFactory();
    }

    /***** CONSTRUCTOR *****/
    protected CollabChair(List<AmenityBlock> amenityBlocks, boolean enabled) {
        super(amenityBlocks, enabled);

        this.collabChairGraphic = new CollabChairGraphic(this);
    }

    /***** OVERRIDE *****/
    @Override
    public String toString() {
        return "CollbChair" + ((this.enabled) ? "" : " (disabled)");
    }

    @Override
    public AmenityGraphic getGraphicObject() {
        return this.collabChairGraphic;
    }

    @Override
    public AmenityGraphicLocation getGraphicLocation() {
        return this.collabChairGraphic.getGraphicLocation();
    }


    /***** INNER STATIC CLASS *****/
    public static class CollabChairBlock extends AmenityBlock {
        public static CollabChairBlockFactory collabChairBlockFactory;

        static {
            collabChairBlockFactory = new CollabChairBlockFactory();
        }

        private CollabChairBlock(Patch patch, boolean attractor, boolean hasGraphic) {
            super(patch, attractor, hasGraphic);
        }

        public static class CollabChairBlockFactory extends AmenityBlockFactory {
            @Override
            public CollabChairBlock create(Patch patch, boolean attractor, boolean hasGraphic) {
                return new CollabChairBlock(patch, attractor, hasGraphic);
            }
        }
    }

    public static class CollabChairFactory extends GoalFactory {
        public static CollabChair create(List<AmenityBlock> amenityBlocks, boolean enabled) {
            return new CollabChair(amenityBlocks, enabled);
        }
    }
}