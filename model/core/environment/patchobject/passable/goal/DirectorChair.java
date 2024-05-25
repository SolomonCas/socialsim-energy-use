package com.socialsim.model.core.environment.patchobject.passable.goal;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.controller.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.controller.graphics.amenity.graphic.DirectorChairGraphic;
import com.socialsim.model.core.environment.Patch;

import java.util.List;

public class DirectorChair extends Goal {
    /***** VARIABLES *****/
    public static final DirectorChairFactory directorChairFactory;
    private final DirectorChairGraphic directorChairGraphic;

    static {
        directorChairFactory = new DirectorChairFactory();
    }

    /***** CONSTRUCTOR *****/
    protected DirectorChair(List<AmenityBlock> amenityBlocks, boolean enabled) {
        super(amenityBlocks, enabled);

        this.directorChairGraphic = new DirectorChairGraphic(this);
    }

    /***** OVERRIDE *****/
    @Override
    public String toString() {
        return "DirectorChair" + ((this.enabled) ? "" : " (disabled)");
    }

    @Override
    public AmenityGraphic getGraphicObject() {
        return this.directorChairGraphic;
    }

    @Override
    public AmenityGraphicLocation getGraphicLocation() {
        return this.directorChairGraphic.getGraphicLocation();
    }


    /***** INNER STATIC CLASS *****/
    public static class DirectorChairBlock extends AmenityBlock {
        public static DirectorChairBlockFactory directorChairBlockFactory;

        static {
            directorChairBlockFactory = new DirectorChairBlockFactory();
        }

        private DirectorChairBlock(Patch patch, boolean attractor, boolean hasGraphic) {
            super(patch, attractor, hasGraphic);
        }

        public static class DirectorChairBlockFactory extends AmenityBlockFactory {
            @Override
            public DirectorChairBlock create(Patch patch, boolean attractor, boolean hasGraphic) {
                return new DirectorChairBlock(patch, attractor, hasGraphic);
            }
        }
    }

    public static class DirectorChairFactory extends GoalFactory {
        public static DirectorChair create(List<AmenityBlock> amenityBlocks, boolean enabled) {
            return new DirectorChair(amenityBlocks, enabled);
        }
    }
}