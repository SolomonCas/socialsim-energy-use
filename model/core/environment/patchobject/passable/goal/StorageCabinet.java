package com.socialsim.model.core.environment.patchobject.passable.goal;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.controller.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.controller.graphics.amenity.graphic.StorageCabinetGraphic;
import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchobject.Amenity;

import java.util.List;

public class StorageCabinet extends Goal {
    /***** VARIABLES *****/
    public static final StorageCabinet.StorageCabinetFactory storageCabinetFactory;
    private final StorageCabinetGraphic storageCabinetGraphic;

    static {
        storageCabinetFactory = new StorageCabinet.StorageCabinetFactory();
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
    public static class StorageCabinetBlock extends Amenity.AmenityBlock {
        public static StorageCabinet.StorageCabinetBlock.StorageCabinetBlockFactory storageCabinetBlockFactory;

        static {
            storageCabinetBlockFactory = new StorageCabinet.StorageCabinetBlock.StorageCabinetBlockFactory();
        }

        private StorageCabinetBlock(Patch patch, boolean attractor, boolean hasGraphic) {
            super(patch, attractor, hasGraphic);
        }

        public static class StorageCabinetBlockFactory extends Amenity.AmenityBlock.AmenityBlockFactory {
            @Override
            public StorageCabinet.StorageCabinetBlock create(Patch patch, boolean attractor, boolean hasGraphic) {
                return new StorageCabinet.StorageCabinetBlock(patch, attractor, hasGraphic);
            }
        }
    }

    public static class StorageCabinetFactory extends GoalFactory {
        public static StorageCabinet create(List<AmenityBlock> amenityBlocks, boolean enabled, String facing) {
            return new StorageCabinet(amenityBlocks, enabled, facing);
        }
    }
}