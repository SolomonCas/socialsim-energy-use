package com.socialsim.model.core.environment.office.patchobject.passable.goal;

import com.socialsim.controller.generic.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.controller.office.graphics.amenity.OfficeAmenityGraphic;
import com.socialsim.controller.office.graphics.amenity.graphic.CollabChairGraphic;
import com.socialsim.model.core.environment.generic.Patch;
import com.socialsim.model.core.environment.generic.patchobject.Amenity;
import com.socialsim.model.core.environment.generic.patchobject.passable.goal.Goal;

import java.util.List;

public class CollabChair extends Goal {

    public static final CollabChair.CollabChairFactory collabChairFactory;
    private final CollabChairGraphic collabChairGraphic;

    static {
        collabChairFactory = new CollabChair.CollabChairFactory();
    }

    protected CollabChair(List<AmenityBlock> amenityBlocks, boolean enabled) {
        super(amenityBlocks, enabled);

        this.collabChairGraphic = new CollabChairGraphic(this);
    }


    @Override
    public String toString() {
        return "CollbChair" + ((this.enabled) ? "" : " (disabled)");
    }

    @Override
    public OfficeAmenityGraphic getGraphicObject() {
        return this.collabChairGraphic;
    }

    @Override
    public AmenityGraphicLocation getGraphicLocation() {
        return this.collabChairGraphic.getGraphicLocation();
    }

    public static class CollabChairBlock extends Amenity.AmenityBlock {
        public static CollabChair.CollabChairBlock.CollabChairBlockFactory collabChairBlockFactory;

        static {
            collabChairBlockFactory = new CollabChair.CollabChairBlock.CollabChairBlockFactory();
        }

        private CollabChairBlock(Patch patch, boolean attractor, boolean hasGraphic) {
            super(patch, attractor, hasGraphic);
        }

        public static class CollabChairBlockFactory extends Amenity.AmenityBlock.AmenityBlockFactory {
            @Override
            public CollabChair.CollabChairBlock create(Patch patch, boolean attractor, boolean hasGraphic) {
                return new CollabChair.CollabChairBlock(patch, attractor, hasGraphic);
            }
        }
    }

    public static class CollabChairFactory extends GoalFactory {
        public static CollabChair create(List<AmenityBlock> amenityBlocks, boolean enabled) {
            return new CollabChair(amenityBlocks, enabled);
        }
    }

}