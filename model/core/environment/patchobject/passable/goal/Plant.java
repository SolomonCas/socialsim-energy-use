package com.socialsim.model.core.environment.patchobject.passable.goal;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.controller.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.controller.graphics.amenity.graphic.PlantGraphic;
import com.socialsim.model.core.environment.Patch;

import java.util.List;

public class Plant extends Goal {

    /***** VARIABLES *****/
    public static final PlantFactory plantFactory;
    private final PlantGraphic plantGraphic;
    private boolean isWatered = false;

    static {
        plantFactory = new PlantFactory();
    }

    /***** CONSTRUCTOR *****/
    protected Plant(List<AmenityBlock> amenityBlocks, boolean enabled) {
        super(amenityBlocks, enabled);

        this.plantGraphic = new PlantGraphic(this);
    }


    /***** GETTER *****/
    public boolean isWatered() {
        return isWatered;
    }

    /***** SETTER *****/
    public void setWatered(boolean watered) {
        isWatered = watered;
    }

    /***** OVERRIDE *****/
    @Override
    public String toString() {
        return "Plant" + ((this.enabled) ? "" : " (disabled)");
    }

    @Override
    public AmenityGraphic getGraphicObject() {
        return this.plantGraphic;
    }

    @Override
    public AmenityGraphicLocation getGraphicLocation() {
        return this.plantGraphic.getGraphicLocation();
    }


    /***** INNER STATIC CLASS *****/
    public static class PlantBlock extends AmenityBlock {
        public static PlantBlockFactory plantBlockFactory;

        static {
            plantBlockFactory = new PlantBlockFactory();
        }

        private PlantBlock(Patch patch, boolean attractor, boolean hasGraphic) {
            super(patch, attractor, hasGraphic);
        }

        public static class PlantBlockFactory extends AmenityBlockFactory {
            @Override
            public PlantBlock create(Patch patch, boolean attractor, boolean hasGraphic) {
                return new PlantBlock(patch, attractor, hasGraphic);
            }
        }
    }

    public static class PlantFactory extends GoalFactory {
        public static Plant create(List<AmenityBlock> amenityBlocks, boolean enabled) {
            return new Plant(amenityBlocks, enabled);
        }
    }
}