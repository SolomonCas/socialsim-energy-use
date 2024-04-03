package com.socialsim.model.core.environment.patchobject.passable.goal;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.controller.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.controller.graphics.amenity.graphic.TrashGraphic;
import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchobject.Amenity;

import java.util.List;

public class Trash extends Goal {

    /***** VARIABLES *****/
    public static final TrashFactory trashFactory;
    private final TrashGraphic trashGraphic;

    static {
        trashFactory = new TrashFactory();
    }

    /***** CONSTRUCTOR *****/
    protected Trash(List<AmenityBlock> amenityBlocks, boolean enabled) {
        super(amenityBlocks, enabled);

        this.trashGraphic = new TrashGraphic(this);
    }

    /***** OVERRIDE *****/
    @Override
    public String toString() {
        return "Trash" + ((this.enabled) ? "" : " (disabled)");
    }

    @Override
    public AmenityGraphic getGraphicObject() {
        return this.trashGraphic;
    }

    @Override
    public AmenityGraphicLocation getGraphicLocation() {
        return this.trashGraphic.getGraphicLocation();
    }


    /***** INNER STATIC CLASS *****/
    public static class TrashBlock extends Amenity.AmenityBlock {
        public static TrashBlockFactory trashBlockFactory;

        static {
            trashBlockFactory = new TrashBlockFactory();
        }

        private TrashBlock(Patch patch, boolean attractor, boolean hasGraphic) {
            super(patch, attractor, hasGraphic);
        }

        public static class TrashBlockFactory extends AmenityBlockFactory {
            @Override
            public TrashBlock create(Patch patch, boolean attractor, boolean hasGraphic) {
                return new TrashBlock(patch, attractor, hasGraphic);
            }
        }
    }

    public static class TrashFactory extends GoalFactory {
        public static Trash create(List<AmenityBlock> amenityBlocks, boolean enabled) {
            return new Trash(amenityBlocks, enabled);
        }
    }

}