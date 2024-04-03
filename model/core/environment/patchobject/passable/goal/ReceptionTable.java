package com.socialsim.model.core.environment.patchobject.passable.goal;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.controller.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.controller.graphics.amenity.graphic.ReceptionTableGraphic;
import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchobject.Amenity;

import java.util.List;

public class ReceptionTable extends Goal {

    /***** VARIABLES *****/
    public static final long serialVersionUID = -5458621245735102190L;
    public static final ReceptionTable.ReceptionTableFactory receptionTableFactory;
    private final ReceptionTableGraphic receptionTableGraphic;

    static {
        receptionTableFactory = new ReceptionTable.ReceptionTableFactory();
    }

    /***** CONSTRUCTOR *****/
    protected ReceptionTable(List<AmenityBlock> amenityBlocks, boolean enabled) {
        super(amenityBlocks, enabled);

        this.receptionTableGraphic = new ReceptionTableGraphic(this);
    }

    /***** OVERRIDE *****/
    @Override
    public String toString() {
        return "ReceptionTable" + ((this.enabled) ? "" : " (disabled)");
    }

    @Override
    public AmenityGraphic getGraphicObject() {
        return this.receptionTableGraphic;
    }

    @Override
    public AmenityGraphicLocation getGraphicLocation() {
        return this.receptionTableGraphic.getGraphicLocation();
    }


    /***** INNER STATIC CLASS *****/
    public static class ReceptionTableBlock extends Amenity.AmenityBlock {
        public static ReceptionTable.ReceptionTableBlock.ReceptionTableBlockFactory receptionTableBlockFactory;

        static {
            receptionTableBlockFactory = new ReceptionTable.ReceptionTableBlock.ReceptionTableBlockFactory();
        }

        private ReceptionTableBlock(Patch patch, boolean attractor, boolean hasGraphic) {
            super(patch, attractor, hasGraphic);
        }

        public static class ReceptionTableBlockFactory extends Amenity.AmenityBlock.AmenityBlockFactory {
            @Override
            public ReceptionTable.ReceptionTableBlock create(Patch patch, boolean attractor, boolean hasGraphic) {
                return new ReceptionTable.ReceptionTableBlock(patch, attractor, hasGraphic);
            }
        }
    }

    public static class ReceptionTableFactory extends GoalFactory {
        public static ReceptionTable create(List<AmenityBlock> amenityBlocks, boolean enabled) {
            return new ReceptionTable(amenityBlocks, enabled);
        }
    }
}