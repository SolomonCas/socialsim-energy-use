package com.socialsim.model.core.environment.patchobject.passable.goal;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.controller.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.controller.graphics.amenity.graphic.BarGraphic;
import com.socialsim.model.core.environment.Patch;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SoloTable extends Goal {

    /***** VARIABLES *****/
    @Serial
    private static final long serialVersionUID = -5458621245735102190L;
    public static final SoloTableFactory soloTableFactory;
    private final BarGraphic barGraphic;

    private final List<Chair> soloChairs;

    static {
        soloTableFactory = new SoloTableFactory();
    }

    /***** CONSTRUCTOR *****/
    protected SoloTable(List<AmenityBlock> amenityBlocks, boolean enabled, String dimensions, String position) {
        super(amenityBlocks, enabled);
        this.barGraphic = new BarGraphic(this, dimensions);
        this.soloChairs = Collections.synchronizedList(new ArrayList<>());
    }

    /***** GETTER *****/
    public List<Chair> getSoloChairs() {
        return soloChairs;
    }

    /***** OVERRIDE *****/
    @Override
    public String toString() {
        return "Solo Table" + ((this.enabled) ? "" : " (disabled)");
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
    public static class SoloTableBlock extends AmenityBlock {
        public static SoloTableBlockFactory soloTableBlockFactory;

        static {
            soloTableBlockFactory = new SoloTableBlockFactory();
        }

        private SoloTableBlock(Patch patch, boolean attractor, boolean hasGraphic) {
            super(patch, attractor, hasGraphic);
        }

        public static class SoloTableBlockFactory extends AmenityBlockFactory {
            @Override
            public SoloTableBlock create(Patch patch, boolean attractor, boolean hasGraphic) {
                return new SoloTableBlock(patch, attractor, hasGraphic);
            }
        }
    }

    public static class SoloTableFactory extends Goal.GoalFactory {
        public static SoloTable create(List<AmenityBlock> amenityBlocks, boolean enabled, String dimensions, String position) {
            return new SoloTable(amenityBlocks, enabled, dimensions, position);
        }
    }
}