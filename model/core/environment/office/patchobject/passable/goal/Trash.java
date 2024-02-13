package com.socialsim.model.core.environment.office.patchobject.passable.goal;

import com.socialsim.controller.generic.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.controller.university.graphics.amenity.UniversityAmenityGraphic;
import com.socialsim.controller.university.graphics.amenity.graphic.TrashGraphic;
import com.socialsim.model.core.environment.generic.Patch;
import com.socialsim.model.core.environment.generic.patchobject.passable.goal.Goal;

import java.util.List;

public class Trash extends Goal {

    public static final TrashFactory trashFactory;
    private final TrashGraphic trashGraphic;

    static {
        trashFactory = new TrashFactory();
    }

    protected Trash(List<AmenityBlock> amenityBlocks, boolean enabled) {
        super(amenityBlocks, enabled);

        this.trashGraphic = new TrashGraphic(this);
    }


    @Override
    public String toString() {
        return "Trash" + ((this.enabled) ? "" : " (disabled)");
    }

    @Override
    public UniversityAmenityGraphic getGraphicObject() {
        return this.trashGraphic;
    }

    @Override
    public AmenityGraphicLocation getGraphicLocation() {
        return this.trashGraphic.getGraphicLocation();
    }

    public static class TrashBlock extends AmenityBlock {
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