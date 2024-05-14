package com.socialsim.model.core.environment.patchobject.passable.goal;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.controller.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.controller.graphics.amenity.graphic.MainEntranceDoorGraphic;
import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchobject.Amenity;

import java.util.List;

public class MainEntranceDoor extends Goal {

    /***** VARIABLES *****/
    public static final MainEntranceDoor.MainEntranceDoorFactory mainEntranceDoorFactory;
    private final MainEntranceDoorGraphic mainEntranceDoorGraphic;

    static {
        mainEntranceDoorFactory = new MainEntranceDoor.MainEntranceDoorFactory();
    }

    /***** CONSTRUCTOR *****/
    protected MainEntranceDoor(List<AmenityBlock> amenityBlocks, boolean enabled, String facing) {
        super(amenityBlocks, enabled);

        this.mainEntranceDoorGraphic = new MainEntranceDoorGraphic(this, facing);
    }

    /***** OVERRIDE *****/
    @Override
    public String toString() {
        return "MainEntranceDoor" + ((this.enabled) ? "" : " (disabled)");
    }

    @Override
    public AmenityGraphic getGraphicObject() {
        return this.mainEntranceDoorGraphic;
    }

    @Override
    public AmenityGraphicLocation getGraphicLocation() {
        return this.mainEntranceDoorGraphic.getGraphicLocation();
    }


    /***** INNER STATIC CLASS *****/
    public static class MainEntranceDoorBlock extends Amenity.AmenityBlock {
        public static MainEntranceDoor.MainEntranceDoorBlock.MainEntranceDoorBlockFactory mainEntranceDoorBlockFactory;

        static {
            mainEntranceDoorBlockFactory = new MainEntranceDoor.MainEntranceDoorBlock.MainEntranceDoorBlockFactory();
        }

        private MainEntranceDoorBlock(Patch patch, boolean attractor, boolean hasGraphic) {
            super(patch, attractor, hasGraphic);
        }

        public static class MainEntranceDoorBlockFactory extends Amenity.AmenityBlock.AmenityBlockFactory {
            @Override
            public MainEntranceDoor.MainEntranceDoorBlock create(Patch patch, boolean attractor, boolean hasGraphic) {
                return new MainEntranceDoor.MainEntranceDoorBlock(patch, attractor, hasGraphic);
            }
        }
    }

    public static class MainEntranceDoorFactory extends GoalFactory {
        public static MainEntranceDoor create(List<AmenityBlock> amenityBlocks, boolean enabled, String facing) {
            return new MainEntranceDoor(amenityBlocks, enabled, facing);
        }
    }
}