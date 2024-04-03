package com.socialsim.model.core.environment.patchobject.passable.goal;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.controller.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.controller.graphics.amenity.graphic.CabinetGraphic;
import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchobject.Amenity;

import java.util.List;

public class Cabinet extends Goal {
    /***** VARIABLES *****/
    public static final Cabinet.CabinetFactory cabinetFactory;
    private final CabinetGraphic cabinetGraphic;

    static {
        cabinetFactory = new Cabinet.CabinetFactory();
    }

    /***** CONSTRUCTOR *****/
    protected Cabinet(List<AmenityBlock> amenityBlocks, boolean enabled, String facing) {
        super(amenityBlocks, enabled);

        this.cabinetGraphic = new CabinetGraphic(this, facing);
    }

    /***** OVERRRIDE *****/
    @Override
    public String toString() {
        return "Cabinet" + ((this.enabled) ? "" : " (disabled)");
    }

    @Override
    public AmenityGraphic getGraphicObject() {
        return this.cabinetGraphic;
    }

    @Override
    public AmenityGraphicLocation getGraphicLocation() {
        return this.cabinetGraphic.getGraphicLocation();
    }


    /***** INNER STATIC CLASSES *****/
    public static class CabinetBlock extends Amenity.AmenityBlock {
        public static Cabinet.CabinetBlock.CabinetBlockFactory cabinetBlockFactory;

        static {
            cabinetBlockFactory = new Cabinet.CabinetBlock.CabinetBlockFactory();
        }

        private CabinetBlock(Patch patch, boolean attractor, boolean hasGraphic) {
            super(patch, attractor, hasGraphic);
        }

        public static class CabinetBlockFactory extends Amenity.AmenityBlock.AmenityBlockFactory {
            @Override
            public Cabinet.CabinetBlock create(Patch patch, boolean attractor, boolean hasGraphic) {
                return new Cabinet.CabinetBlock(patch, attractor, hasGraphic);
            }
        }
    }

    public static class CabinetFactory extends GoalFactory {
        public static Cabinet create(List<AmenityBlock> amenityBlocks, boolean enabled, String facing) {
            return new Cabinet(amenityBlocks, enabled, facing);
        }
    }
}