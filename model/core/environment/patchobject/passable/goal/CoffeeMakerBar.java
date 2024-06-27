package com.socialsim.model.core.environment.patchobject.passable.goal;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.controller.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.controller.graphics.amenity.graphic.CoffeeMakerBarGraphic;
import com.socialsim.model.core.environment.Patch;

import java.util.List;

public class CoffeeMakerBar extends Goal {

    /***** VARIABLES *****/
    public static final CoffeeMakerBarFactory coffeeMakerBarFactory;
    private final CoffeeMakerBarGraphic coffeeMakerBarGraphic;

    static {
        coffeeMakerBarFactory = new CoffeeMakerBarFactory();
    }

    /***** CONSTRUCTOR *****/
    protected CoffeeMakerBar(List<AmenityBlock> amenityBlocks, boolean enabled) {
        super(amenityBlocks, enabled);

        this.coffeeMakerBarGraphic = new CoffeeMakerBarGraphic(this);
    }

    /***** OVERRIDE *****/
    @Override
    public String toString() {
        return "CoffeeMakerBar" + ((this.enabled) ? "" : " (disabled)");
    }

    @Override
    public AmenityGraphic getGraphicObject() {
        return this.coffeeMakerBarGraphic;
    }

    @Override
    public AmenityGraphicLocation getGraphicLocation() {
        return this.coffeeMakerBarGraphic.getGraphicLocation();
    }


    /***** INNER STATIC CLASS *****/
    public static class CoffeeMakerBarBlock extends AmenityBlock {
        public static CoffeeMakerBarBlockFactory coffeeMakerBarBlockFactory;

        static {
            coffeeMakerBarBlockFactory = new CoffeeMakerBarBlockFactory();
        }

        private CoffeeMakerBarBlock(Patch patch, boolean attractor, boolean hasGraphic) {
            super(patch, attractor, hasGraphic);
        }

        public static class CoffeeMakerBarBlockFactory extends AmenityBlockFactory {
            @Override
            public CoffeeMakerBarBlock create(Patch patch, boolean attractor, boolean hasGraphic) {
                return new CoffeeMakerBarBlock(patch, attractor, hasGraphic);
            }
        }
    }

    public static class CoffeeMakerBarFactory extends GoalFactory {
        public static CoffeeMakerBar create(List<AmenityBlock> amenityBlocks, boolean enabled) {
            return new CoffeeMakerBar(amenityBlocks, enabled);
        }
    }

}