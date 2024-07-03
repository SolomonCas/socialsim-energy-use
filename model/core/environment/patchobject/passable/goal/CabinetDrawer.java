package com.socialsim.model.core.environment.patchobject.passable.goal;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.controller.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.controller.graphics.amenity.graphic.CabinetDrawerGraphic;
import com.socialsim.model.core.environment.Patch;

import java.util.List;

public class CabinetDrawer extends Goal {
    /***** VARIABLES *****/
    public static final CabinetDrawerFactory cabinetDrawerFactory;
    private final CabinetDrawerGraphic cabinetDrawerGraphic;

    static {
        cabinetDrawerFactory = new CabinetDrawerFactory();
    }

    /***** CONSTRUCTOR *****/
    protected CabinetDrawer(List<AmenityBlock> amenityBlocks, boolean enabled, String type, String facing) {
        super(amenityBlocks, enabled);

        this.cabinetDrawerGraphic = new CabinetDrawerGraphic(this, type, facing);
    }

    /***** OVERRRIDE *****/
    @Override
    public String toString() {
        return "CabinetDrawer" + ((this.enabled) ? "" : " (disabled)");
    }

    @Override
    public AmenityGraphic getGraphicObject() {
        return this.cabinetDrawerGraphic;
    }

    @Override
    public AmenityGraphicLocation getGraphicLocation() {
        return this.cabinetDrawerGraphic.getGraphicLocation();
    }


    /***** INNER STATIC CLASSES *****/
    public static class CabinetDrawerBlock extends AmenityBlock {
        public static CabinetDrawerBlockFactory cabinetDrawerBlockFactory;

        static {
            cabinetDrawerBlockFactory = new CabinetDrawerBlockFactory();
        }

        private CabinetDrawerBlock(Patch patch, boolean attractor, boolean hasGraphic) {
            super(patch, attractor, hasGraphic);
        }

        public static class CabinetDrawerBlockFactory extends AmenityBlockFactory {
            @Override
            public CabinetDrawerBlock create(Patch patch, boolean attractor, boolean hasGraphic) {
                return new CabinetDrawerBlock(patch, attractor, hasGraphic);
            }
        }
    }

    public static class CabinetDrawerFactory extends GoalFactory {
        public static CabinetDrawer create(List<AmenityBlock> amenityBlocks, boolean enabled, String type, String facing) {
            return new CabinetDrawer(amenityBlocks, enabled, type, facing);
        }
    }
}