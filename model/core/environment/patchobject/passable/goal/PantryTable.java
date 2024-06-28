package com.socialsim.model.core.environment.patchobject.passable.goal;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.controller.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.controller.graphics.amenity.graphic.PantryTableGraphic;
import com.socialsim.model.core.environment.Patch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PantryTable extends Goal {

    /***** VARIABLES *****/
    public static final PantryTableFactory pantryTableFactory;
    private final PantryTableGraphic pantryTableGraphic;

    private final List<Chair> pantryChairs;

    static {
        pantryTableFactory = new PantryTableFactory();
    }

    /***** CONSTRUCTOR *****/
    protected PantryTable(List<AmenityBlock> amenityBlocks, boolean enabled, String type) {
        super(amenityBlocks, enabled);
        this.pantryTableGraphic = new PantryTableGraphic(this, type);
        this.pantryChairs = Collections.synchronizedList(new ArrayList<>());
    }


    /***** GETTER *****/
    public List<Chair> getPantryChairs() {
        return pantryChairs;
    }


    @Override
    public String toString() {
        return "Pantry Table" + ((this.enabled) ? "" : " (disabled)");
    }

    @Override
    public AmenityGraphic getGraphicObject() {
        return this.pantryTableGraphic;
    }

    @Override
    public AmenityGraphicLocation getGraphicLocation() {
        return this.pantryTableGraphic.getGraphicLocation();
    }


    /***** INNER STATIC CLASS *****/
    public static class PantryTableBlock extends AmenityBlock {
        public static PantryTableBlockFactory pantryTableBlockFactory;

        static {
            pantryTableBlockFactory = new PantryTableBlockFactory();
        }

        private PantryTableBlock(Patch patch, boolean attractor, boolean hasGraphic) {
            super(patch, attractor, hasGraphic);
        }

        public static class PantryTableBlockFactory extends AmenityBlockFactory {
            @Override
            public PantryTableBlock create(Patch patch, boolean attractor, boolean hasGraphic) {
                return new PantryTableBlock(patch, attractor, hasGraphic);
            }
        }
    }

    public static class PantryTableFactory extends GoalFactory {
        public static PantryTable create(List<AmenityBlock> amenityBlocks, boolean enabled, String type) {
            return new PantryTable(amenityBlocks, enabled, type);
        }
    }
}