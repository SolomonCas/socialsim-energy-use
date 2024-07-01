package com.socialsim.model.core.environment.patchobject.passable.goal;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.controller.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.controller.graphics.amenity.graphic.CubicleGraphic;
import com.socialsim.model.core.environment.Patch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MESATable extends Goal {

    /***** VARIABLES *****/
    public static final MESATableFactory mesaTableFactory;
    private final CubicleGraphic mesaTableGraphic;
    private final List<Chair> mesaChairs;
    static {
        mesaTableFactory = new MESATableFactory();
    }

    /***** CONSTRUCTOR *****/
    protected MESATable(List<AmenityBlock> amenityBlocks, boolean enabled, String type, String facing, String tableOn) {
        super(amenityBlocks, enabled);
        this.mesaTableGraphic = new CubicleGraphic(this, type, facing, tableOn);
        this.mesaChairs = Collections.synchronizedList(new ArrayList<>());
    }

    /***** GETTER *****/
    public List<Chair> getMesaChairs() {
        return mesaChairs;
    }

    /***** OVERRIDE *****/
    @Override
    public String toString() {
        return "MESATable" + ((this.enabled) ? "" : " (disabled)");
    }
    @Override
    public AmenityGraphic getGraphicObject() {
        return this.mesaTableGraphic;
    }

    @Override
    public AmenityGraphicLocation getGraphicLocation() {
        return this.mesaTableGraphic.getGraphicLocation();
    }

    /***** INNER STATIC CLASS *****/
    public static class MESATableBlock extends AmenityBlock {
        public static MESATableBlockFactory mesaTableBlockFactory;

        static {
            mesaTableBlockFactory = new MESATableBlockFactory();
        }

        private MESATableBlock(Patch patch, boolean attractor, boolean hasGraphic) {
            super(patch, attractor, hasGraphic);
        }

        public static class MESATableBlockFactory extends AmenityBlockFactory {
            @Override
            public MESATableBlock create(Patch patch, boolean attractor, boolean hasGraphic) {
                return new MESATableBlock(patch, attractor, hasGraphic);
            }
        }
    }

    public static class MESATableFactory extends GoalFactory {
        public static MESATable create(List<AmenityBlock> amenityBlocks, boolean enabled, String type, String facing, String tableOn) {
            return new MESATable(amenityBlocks, enabled, type, facing, tableOn);
        }
    }
}