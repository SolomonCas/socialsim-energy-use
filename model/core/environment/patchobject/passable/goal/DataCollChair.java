package com.socialsim.model.core.environment.patchobject.passable.goal;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.controller.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.controller.graphics.amenity.graphic.ChairGraphic;
import com.socialsim.model.core.environment.Patch;

import java.util.List;

public class DataCollChair extends Goal {
    /***** VARIABLES *****/
    public static final DataCollChairFactory dataCollChairFactory;
    private final ChairGraphic officeChairGraphic;

    static {
        dataCollChairFactory = new DataCollChairFactory();
    }

    /***** CONSTRUCTOR *****/
    protected DataCollChair(List<AmenityBlock> amenityBlocks, boolean enabled, String facing, String type) {
        super(amenityBlocks, enabled);

        this.officeChairGraphic = new ChairGraphic(this, facing, type);
    }

    /***** OVERRIDE *****/
    @Override
    public String toString() {
        return "DataColl Chair" + ((this.enabled) ? "" : " (disabled)");
    }

    @Override
    public AmenityGraphic getGraphicObject() {
        return this.officeChairGraphic;
    }

    @Override
    public AmenityGraphicLocation getGraphicLocation() {
        return this.officeChairGraphic.getGraphicLocation();
    }


    /***** INNER STATIC CLASS *****/
    public static class DataCollChairBlock extends AmenityBlock {
        public static DataCollChairBlockFactory dataCollChairBlockFactory;

        static {
            dataCollChairBlockFactory = new DataCollChairBlockFactory();
        }

        private DataCollChairBlock(Patch patch, boolean attractor, boolean hasGraphic) {
            super(patch, attractor, hasGraphic);
        }

        public static class DataCollChairBlockFactory extends AmenityBlockFactory {
            @Override
            public DataCollChairBlock create(Patch patch, boolean attractor, boolean hasGraphic) {
                return new DataCollChairBlock(patch, attractor, hasGraphic);
            }
        }
    }

    public static class DataCollChairFactory extends GoalFactory {
        public static DataCollChair create(List<AmenityBlock> amenityBlocks, boolean enabled, String facing, String type) {
            return new DataCollChair(amenityBlocks, enabled, facing, type);
        }
    }
}