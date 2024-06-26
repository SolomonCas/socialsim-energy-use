package com.socialsim.model.core.environment.patchobject.passable.goal;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.controller.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.controller.graphics.amenity.graphic.TrashCanGraphic;
import com.socialsim.model.core.environment.Patch;

import java.util.List;

public class TrashCan extends Goal {

    /***** VARIABLES *****/
    public static final TrashCanFactory trashCanFactory;
    private final TrashCanGraphic trashCanGraphic;

    static {
        trashCanFactory = new TrashCanFactory();
    }

    /***** CONSTRUCTOR *****/
    protected TrashCan(List<AmenityBlock> amenityBlocks, boolean enabled) {
        super(amenityBlocks, enabled);

        this.trashCanGraphic = new TrashCanGraphic(this);
    }

    /***** OVERRIDE *****/
    @Override
    public String toString() {
        return "TrashCan" + ((this.enabled) ? "" : " (disabled)");
    }

    @Override
    public AmenityGraphic getGraphicObject() {
        return this.trashCanGraphic;
    }

    @Override
    public AmenityGraphicLocation getGraphicLocation() {
        return this.trashCanGraphic.getGraphicLocation();
    }


    /***** INNER STATIC CLASS *****/
    public static class TrashCanBlock extends AmenityBlock {
        public static TrashCanBlockFactory trashCanBlockFactory;

        static {
            trashCanBlockFactory = new TrashCanBlockFactory();
        }

        private TrashCanBlock(Patch patch, boolean attractor, boolean hasGraphic) {
            super(patch, attractor, hasGraphic);
        }

        public static class TrashCanBlockFactory extends AmenityBlockFactory {
            @Override
            public TrashCanBlock create(Patch patch, boolean attractor, boolean hasGraphic) {
                return new TrashCanBlock(patch, attractor, hasGraphic);
            }
        }
    }

    public static class TrashCanFactory extends GoalFactory {
        public static TrashCan create(List<AmenityBlock> amenityBlocks, boolean enabled) {
            return new TrashCan(amenityBlocks, enabled);
        }
    }

}