package com.socialsim.model.core.environment.patchobject.passable.goal;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.controller.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.controller.graphics.amenity.graphic.PantryTableGraphic;
import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchobject.Amenity;
import com.socialsim.model.core.environment.patchobject.passable.goal.Goal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PantryTable extends Goal {

    /***** VARIABLES *****/
    public static final PantryTable.PantryTableFactory pantryTableFactory;
    private final PantryTableGraphic pantryTableGraphic;

    private final List<PantryChair> pantryChairs;

    static {
        pantryTableFactory = new PantryTable.PantryTableFactory();
    }

    /***** CONSTRUCTOR *****/
    protected PantryTable(List<AmenityBlock> amenityBlocks, boolean enabled, String facing) {
        super(amenityBlocks, enabled);

        this.pantryChairs = Collections.synchronizedList(new ArrayList<>());

        this.pantryTableGraphic = new PantryTableGraphic(this, facing);
    }


    /***** GETTER *****/
    public List<PantryChair> getPantryChairs() {
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
    public static class PantryTableBlock extends Amenity.AmenityBlock {
        public static PantryTable.PantryTableBlock.PantryTableBlockFactory pantryTableBlockFactory;

        static {
            pantryTableBlockFactory = new PantryTable.PantryTableBlock.PantryTableBlockFactory();
        }

        private PantryTableBlock(Patch patch, boolean attractor, boolean hasGraphic) {
            super(patch, attractor, hasGraphic);
        }

        public static class PantryTableBlockFactory extends Amenity.AmenityBlock.AmenityBlockFactory {
            @Override
            public PantryTable.PantryTableBlock create(Patch patch, boolean attractor, boolean hasGraphic) {
                return new PantryTable.PantryTableBlock(patch, attractor, hasGraphic);
            }
        }
    }

    public static class PantryTableFactory extends GoalFactory {
        public static PantryTable create(List<AmenityBlock> amenityBlocks, boolean enabled, String facing) {
            return new PantryTable(amenityBlocks, enabled, facing);
        }
    }
}