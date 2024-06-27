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

public class ReceptionTable extends QueueableGoal implements Serializable {

    /***** VARIABLES *****/
    @Serial
    private static final long serialVersionUID = -5458621245735102190L;
    public static final ReceptionTableFactory receptionTableFactory;
    private final BarGraphic barGraphic;

    private final List<ReceptionChair> receptionChairs;

    static {
        receptionTableFactory = new ReceptionTableFactory();
    }

    /***** CONSTRUCTOR *****/
    protected ReceptionTable(List<AmenityBlock> amenityBlocks, boolean enabled, String dimensions, int waitingTime) {
        super(amenityBlocks, enabled, waitingTime);
        this.barGraphic = new BarGraphic(this, dimensions);
        this.receptionChairs = Collections.synchronizedList(new ArrayList<>());
    }

    /***** GETTER *****/
    public List<ReceptionChair> getReceptionChairs() {
        return receptionChairs;
    }

    /***** OVERRIDE *****/
    @Override
    public String toString() {
        return "ReceptionTable" + ((this.enabled) ? "" : " (disabled)");
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
    public static class ReceptionTableBlock extends AmenityBlock {
        public static ReceptionTableBlockFactory receptionTableBlockFactory;

        static {
            receptionTableBlockFactory = new ReceptionTableBlockFactory();
        }

        private ReceptionTableBlock(Patch patch, boolean attractor, boolean hasGraphic) {
            super(patch, attractor, hasGraphic);
        }

        public static class ReceptionTableBlockFactory extends AmenityBlockFactory {
            @Override
            public ReceptionTableBlock create(Patch patch, boolean attractor, boolean hasGraphic) {
                return new ReceptionTableBlock(patch, attractor, hasGraphic);
            }
        }
    }

    public static class ReceptionTableFactory extends Goal.GoalFactory {
        public static ReceptionTable create(List<AmenityBlock> amenityBlocks, boolean enabled, String dimensions, int waitingTime) {
            return new ReceptionTable(amenityBlocks, enabled, dimensions, waitingTime);
        }
    }
}