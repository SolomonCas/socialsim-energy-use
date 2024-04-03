package com.socialsim.model.core.environment.patchobject.passable.goal;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.controller.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.controller.graphics.amenity.graphic.FridgeGraphic;
import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchobject.Amenity;

import java.util.List;

public class Fridge extends Goal {

    /***** VARIABLES *****/
    public static final Fridge.FridgeFactory fridgeFactory;
    private final FridgeGraphic fridgeGraphic;

    static {
        fridgeFactory = new Fridge.FridgeFactory();
    }

    /***** CONSTRUCTOR *****/
    protected Fridge(List<AmenityBlock> amenityBlocks, boolean enabled) {
        super(amenityBlocks, enabled);

        this.fridgeGraphic = new FridgeGraphic(this);
    }

    /***** OVERRIDE *****/
    @Override
    public String toString() {
        return "Fridge" + ((this.enabled) ? "" : " (disabled)");
    }

    @Override
    public AmenityGraphic getGraphicObject() {
        return this.fridgeGraphic;
    }

    @Override
    public AmenityGraphicLocation getGraphicLocation() {
        return this.fridgeGraphic.getGraphicLocation();
    }


    /***** INNER STATIC CLASS *****/
    public static class FridgeBlock extends Amenity.AmenityBlock {
        public static Fridge.FridgeBlock.FridgeBlockFactory fridgeBlockFactory;

        static {
            fridgeBlockFactory = new Fridge.FridgeBlock.FridgeBlockFactory();
        }

        private FridgeBlock(Patch patch, boolean attractor, boolean hasGraphic) {
            super(patch, attractor, hasGraphic);
        }

        public static class FridgeBlockFactory extends Amenity.AmenityBlock.AmenityBlockFactory {
            @Override
            public Fridge.FridgeBlock create(Patch patch, boolean attractor, boolean hasGraphic) {
                return new Fridge.FridgeBlock(patch, attractor, hasGraphic);
            }
        }
    }

    public static class FridgeFactory extends GoalFactory {
        public static Fridge create(List<AmenityBlock> amenityBlocks, boolean enabled) {
            return new Fridge(amenityBlocks, enabled);
        }
    }
}