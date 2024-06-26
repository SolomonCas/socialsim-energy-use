package com.socialsim.model.core.environment.patchobject.passable.goal;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.controller.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.controller.graphics.amenity.graphic.StorageCabinetGraphic;
import com.socialsim.model.core.environment.Patch;

import java.util.List;

public class StorageCabinet extends Goal {
    /***** VARIABLES *****/
    public static final StorageCabinetFactory storageCabinetFactory;
    private final StorageCabinetGraphic storageCabinetGraphic;

    static {
        storageCabinetFactory = new StorageCabinetFactory();
    }

    /***** CONSTRUCTOR *****/
    protected StorageCabinet(List<AmenityBlock> amenityBlocks, boolean enabled, String facing) {
        super(amenityBlocks, enabled);

        this.storageCabinetGraphic = new StorageCabinetGraphic(this, facing);
    }

    /***** OVERRRIDE *****/
    @Override
    public String toString() {
        return "StorageCabinet" + ((this.enabled) ? "" : " (disabled)");
    }

    @Override
    public AmenityGraphic getGraphicObject() {
        return this.storageCabinetGraphic;
    }

    @Override
    public AmenityGraphicLocation getGraphicLocation() {
        return this.storageCabinetGraphic.getGraphicLocation();
    }


    /***** INNER STATIC CLASSES *****/
    public static class StorageCabinetBlock extends AmenityBlock {
        public static StorageCabinetBlockFactory storageCabinetBlockFactory;

        static {
            storageCabinetBlockFactory = new StorageCabinetBlockFactory();
        }

        private StorageCabinetBlock(Patch patch, boolean attractor, boolean hasGraphic) {
            super(patch, attractor, hasGraphic);
        }

        public static class StorageCabinetBlockFactory extends AmenityBlockFactory {
            @Override
            public StorageCabinetBlock create(Patch patch, boolean attractor, boolean hasGraphic) {
                return new StorageCabinetBlock(patch, attractor, hasGraphic);
            }
        }
    }

    public static class StorageCabinetFactory extends GoalFactory {
        public static StorageCabinet create(List<AmenityBlock> amenityBlocks, boolean enabled, String facing) {
            return new StorageCabinet(amenityBlocks, enabled, facing);
        }
    }
}