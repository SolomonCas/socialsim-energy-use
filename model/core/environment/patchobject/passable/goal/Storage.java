package com.socialsim.model.core.environment.patchobject.passable.goal;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.controller.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.controller.graphics.amenity.graphic.CabinetDrawerGraphic;
import com.socialsim.model.core.environment.Patch;

import java.util.List;

public class Storage extends Goal {
    /***** VARIABLES *****/
    public static final StorageFactory storageFactory;
    private final CabinetDrawerGraphic cabinetDrawerGraphic;

    static {
        storageFactory = new StorageFactory();
    }

    /***** CONSTRUCTOR *****/
    protected Storage(List<AmenityBlock> amenityBlocks, boolean enabled, String type, String facing) {
        super(amenityBlocks, enabled);

        this.cabinetDrawerGraphic = new CabinetDrawerGraphic(this, type, facing);
    }

    /***** OVERRRIDE *****/
    @Override
    public String toString() {
        return "Storage" + ((this.enabled) ? "" : " (disabled)");
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
    public static class StorageBlock extends AmenityBlock {
        public static StorageBlockFactory storageBlockFactory;

        static {
            storageBlockFactory = new StorageBlockFactory();
        }

        private StorageBlock(Patch patch, boolean attractor, boolean hasGraphic) {
            super(patch, attractor, hasGraphic);
        }

        public static class StorageBlockFactory extends AmenityBlockFactory {
            @Override
            public StorageBlock create(Patch patch, boolean attractor, boolean hasGraphic) {
                return new StorageBlock(patch, attractor, hasGraphic);
            }
        }
    }

    public static class StorageFactory extends GoalFactory {
        public static Storage create(List<AmenityBlock> amenityBlocks, boolean enabled, String type, String facing) {
            return new Storage(amenityBlocks, enabled, type, facing);
        }
    }
}