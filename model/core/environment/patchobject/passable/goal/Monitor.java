package com.socialsim.model.core.environment.patchobject.passable.goal;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.controller.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.controller.graphics.amenity.graphic.MonitorGraphic;
import com.socialsim.model.core.environment.Patch;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Monitor extends Goal {

    /***** VARIABLES *****/
    public static final MonitorFactory monitorFactory;
    private final MonitorGraphic monitorGraphic;
    static {
        monitorFactory = new MonitorFactory();
    }
    private boolean isOn;

    /***** CONSTRUCTOR *****/
    protected Monitor(List<AmenityBlock> amenityBlocks, boolean enabled, String facing) {
        super(amenityBlocks, enabled);
        this.monitorGraphic = new MonitorGraphic(this, facing);
        this.isOn = false;
    }

    /***** GETTERS *****/

    public boolean isOn() {
        return isOn;
    }

    /***** SETTER *****/
    public void setOn(boolean on) {
        isOn = on;
    }


    /***** OVERRIDE *****/
    @Override
    public String toString() {
        return "Monitor" + ((this.enabled) ? "" : " (disabled)");
    }
    @Override
    public AmenityGraphic getGraphicObject() {
        return this.monitorGraphic;
    }

    @Override
    public AmenityGraphicLocation getGraphicLocation() {
        return this.monitorGraphic.getGraphicLocation();
    }

    /***** INNER STATIC CLASS *****/
    public static class MonitorBlock extends AmenityBlock {
        public static MonitorBlockFactory monitorBlockFactory;

        static {
            monitorBlockFactory = new MonitorBlockFactory();
        }

        private MonitorBlock(Patch patch, boolean attractor, boolean hasGraphic) {
            super(patch, attractor, hasGraphic);
        }

        public static class MonitorBlockFactory extends AmenityBlockFactory {
            @Override
            public MonitorBlock create(Patch patch, boolean attractor, boolean hasGraphic) {
                return new MonitorBlock(patch, attractor, hasGraphic);
            }
        }
    }

    public static class MonitorFactory extends GoalFactory {
        public static Monitor create(List<AmenityBlock> amenityBlocks, boolean enabled, String facing) {
            return new Monitor(amenityBlocks, enabled, facing);
        }
    }
}