package com.socialsim.model.core.environment.patchobject.passable.goal;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.controller.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.controller.graphics.amenity.graphic.BarGraphic;
import com.socialsim.model.core.environment.Patch;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataCollTable extends Goal {

    /***** VARIABLES *****/
    @Serial
    private static final long serialVersionUID = -5458621245735102190L;
    public static final DataCollTableFactory dataCollTableFactory;
    private final BarGraphic barGraphic;

    private final List<Chair> dataCollChairs;

    static {
        dataCollTableFactory = new DataCollTableFactory();
    }

    /***** CONSTRUCTOR *****/
    protected DataCollTable(List<AmenityBlock> amenityBlocks, boolean enabled, String dimensions) {
        super(amenityBlocks, enabled);
        this.barGraphic = new BarGraphic(this, dimensions);
        this.dataCollChairs = Collections.synchronizedList(new ArrayList<>());
    }

    /***** GETTER *****/
    public List<Chair> getDataCollChairs() {
        return dataCollChairs;
    }

    /***** OVERRIDE *****/
    @Override
    public String toString() {
        return "DataColl Table" + ((this.enabled) ? "" : " (disabled)");
    }

    @Override
    public AmenityGraphic getGraphicObject() {
        return this.barGraphic;
    }

    @Override
    public AmenityGraphicLocation getGraphicLocation() {
        return this.barGraphic.getGraphicLocation();
    }


    /***** INNER STATIC CLASS *****/
    public static class DataCollTableBlock extends AmenityBlock {
        public static DataCollTableBlockFactory dataCollTableBlockFactory;

        static {
            dataCollTableBlockFactory = new DataCollTableBlockFactory();
        }

        private DataCollTableBlock(Patch patch, boolean attractor, boolean hasGraphic) {
            super(patch, attractor, hasGraphic);
        }

        public static class DataCollTableBlockFactory extends AmenityBlockFactory {
            @Override
            public DataCollTableBlock create(Patch patch, boolean attractor, boolean hasGraphic) {
                return new DataCollTableBlock(patch, attractor, hasGraphic);
            }
        }
    }

    public static class DataCollTableFactory extends GoalFactory {
        public static DataCollTable create(List<AmenityBlock> amenityBlocks, boolean enabled, String dimensions) {
            return new DataCollTable(amenityBlocks, enabled, dimensions);
        }
    }
}