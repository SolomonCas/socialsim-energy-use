package com.socialsim.model.core.environment.patchobject.passable.goal;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.controller.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.controller.graphics.amenity.graphic.RefrigeratorGraphic;
import com.socialsim.model.core.environment.Patch;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class Refrigerator  extends QueueableGoal implements Serializable {

    /***** VARIABLES *****/
    @Serial
    private static final long serialVersionUID = -5458621245735102190L;
    public static final RefrigeratorFactory refrigeratorFactory;
    private final RefrigeratorGraphic refrigeratorGraphic;

    static {
        refrigeratorFactory = new RefrigeratorFactory();
    }

    private double coolnessLevel = 50;
    private boolean activeCycle = false;
    private int duration = 0;
    /***** CONSTRUCTOR *****/
    protected Refrigerator(List<AmenityBlock> amenityBlocks, boolean enabled, int waitingTime) {
        super(amenityBlocks, enabled, waitingTime);

        this.refrigeratorGraphic = new RefrigeratorGraphic(this);
    }

    /***** OVERRIDE *****/
    @Override
    public String toString() {
        return "Refrigerator" + ((this.enabled) ? "" : " (disabled)");
    }

    @Override
    public AmenityGraphic getGraphicObject() {
        return this.refrigeratorGraphic;
    }

    @Override
    public AmenityGraphicLocation getGraphicLocation() {
        return this.refrigeratorGraphic.getGraphicLocation();
    }

    public boolean isActiveCycle() {
        return activeCycle;
    }

    public void setActiveCycle(boolean activeCycle) {
        this.activeCycle = activeCycle;
    }

    public double getCoolnessLevel() {
        return coolnessLevel;
    }

    public void setCoolnessLevel(double coolnessLevel) {
        this.coolnessLevel = coolnessLevel;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }


    /***** INNER STATIC CLASS *****/
    public static class RefrigeratorBlock extends AmenityBlock {
        public static RefrigeratorBlockFactory refrigeratorBlockFactory;

        static {
            refrigeratorBlockFactory = new RefrigeratorBlockFactory();
        }

        private RefrigeratorBlock(Patch patch, boolean attractor, boolean hasGraphic) {
            super(patch, attractor, hasGraphic);
        }

        public static class RefrigeratorBlockFactory extends AmenityBlockFactory {
            @Override
            public RefrigeratorBlock create(Patch patch, boolean attractor, boolean hasGraphic) {
                return new RefrigeratorBlock(patch, attractor, hasGraphic);
            }
        }
    }

    public static class RefrigeratorFactory extends Goal.GoalFactory {
        public static Refrigerator create(List<AmenityBlock> amenityBlocks, boolean enabled, int waitingTime) {
            return new Refrigerator(amenityBlocks, enabled, waitingTime);
        }
    }
}