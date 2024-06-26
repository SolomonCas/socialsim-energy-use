package com.socialsim.model.core.environment.patchobject.passable.goal;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.controller.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.controller.graphics.amenity.graphic.ChairGraphic;
import com.socialsim.model.core.environment.Patch;

import java.util.List;

public class DirectorChair extends Goal {
    /***** VARIABLES *****/
    public static final DirectorChairFactory directorChairFactory;
    private final ChairGraphic officeChairGraphic;

    static {
        directorChairFactory = new DirectorChairFactory();
    }

    /***** CONSTRUCTOR *****/
    protected DirectorChair(List<AmenityBlock> amenityBlocks, boolean enabled, String facing, String type) {
        super(amenityBlocks, enabled);

        this.officeChairGraphic = new ChairGraphic(this, facing, type);
    }

    /***** OVERRIDE *****/
    @Override
    public String toString() {
        return "DirectorChair" + ((this.enabled) ? "" : " (disabled)");
    }

    @Override
    public AmenityGraphic getGraphicObject() {
        return this.officeChairGraphic;
    }

    @Override
    public AmenityGraphicLocation getGraphicLocation() {
        return this.officeChairGraphic.getGraphicLocation();
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
        public static DirectorChair create(List<AmenityBlock> amenityBlocks, boolean enabled, String facing, String type) {
            return new DirectorChair(amenityBlocks, enabled, facing, type);
        }
    }
}