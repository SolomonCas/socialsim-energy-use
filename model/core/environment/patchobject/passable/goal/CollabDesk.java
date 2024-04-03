package com.socialsim.model.core.environment.patchobject.passable.goal;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.controller.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.controller.graphics.amenity.graphic.CollabDeskGraphic;
import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchobject.Amenity;

import java.util.List;

public class CollabDesk extends Goal {

    /***** VARIABLES *****/
    public static final CollabDesk.CollabDeskFactory collabDeskFactory;
    private final CollabDeskGraphic collabDeskGraphic;

    static {
        collabDeskFactory = new CollabDesk.CollabDeskFactory();
    }

    /***** CONSTRUCTOR *****/
    protected CollabDesk(List<AmenityBlock> amenityBlocks, boolean enabled, String facing) {
        super(amenityBlocks, enabled);

        this.collabDeskGraphic = new CollabDeskGraphic(this, facing);
    }

    /***** OVERRIDE *****/
    @Override
    public String toString() {
        return "CollabDesk" + ((this.enabled) ? "" : " (disabled)");
    }

    @Override
    public AmenityGraphic getGraphicObject() {
        return this.collabDeskGraphic;
    }

    @Override
    public AmenityGraphicLocation getGraphicLocation() {
        return this.collabDeskGraphic.getGraphicLocation();
    }


    /***** INNER STATIC CLASS *****/
    public static class CollabDeskBlock extends Amenity.AmenityBlock {
        public static CollabDesk.CollabDeskBlock.CollabDeskBlockFactory collabDeskBlockFactory;

        static {
            collabDeskBlockFactory = new CollabDesk.CollabDeskBlock.CollabDeskBlockFactory();
        }

        private CollabDeskBlock(Patch patch, boolean attractor, boolean hasGraphic) {
            super(patch, attractor, hasGraphic);
        }

        public static class CollabDeskBlockFactory extends Amenity.AmenityBlock.AmenityBlockFactory {
            @Override
            public CollabDesk.CollabDeskBlock create(Patch patch, boolean attractor, boolean hasGraphic) {
                return new CollabDesk.CollabDeskBlock(patch, attractor, hasGraphic);
            }
        }
    }

    public static class CollabDeskFactory extends GoalFactory {
        public static CollabDesk create(List<AmenityBlock> amenityBlocks, boolean enabled, String facing) {
            return new CollabDesk(amenityBlocks, enabled, facing);
        }
    }
}