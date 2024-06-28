package com.socialsim.model.core.environment.patchobject.passable.goal;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.controller.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.controller.graphics.amenity.graphic.CubicleGraphic;
import com.socialsim.model.core.environment.Patch;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Cubicle extends Goal {

    /***** VARIABLES *****/
    public static final CubicleFactory cubicleFactory;
    private final CubicleGraphic cubicleGraphic;
    static {
        cubicleFactory = new CubicleFactory();
    }
    private final boolean withAppliance;
    private boolean isOn;
    private List<Chair> chairs;

    /***** CONSTRUCTOR *****/
    protected Cubicle(List<AmenityBlock> amenityBlocks, boolean enabled, String type, String facing, String tableOn, boolean withAppliance) {
        super(amenityBlocks, enabled);
        this.cubicleGraphic = new CubicleGraphic(this, type, facing, tableOn, withAppliance);
        this.withAppliance = withAppliance;
        this.chairs = Collections.synchronizedList(new ArrayList<>());
        this.isOn = false;
    }

    /***** GETTERS *****/
    public List<Chair> getCubicleChairs() {
        return chairs;
    }

    public boolean withAppliance() {
        return withAppliance;
    }

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
        return "Cubicle" + ((this.enabled) ? "" : " (disabled)");
    }
    @Override
    public AmenityGraphic getGraphicObject() {
        return this.cubicleGraphic;
    }

    @Override
    public AmenityGraphicLocation getGraphicLocation() {
        return this.cubicleGraphic.getGraphicLocation();
    }

    /***** INNER STATIC CLASS *****/
    public static class CubicleBlock extends AmenityBlock {
        public static CubicleBlockFactory cubicleBlockFactory;

        static {
            cubicleBlockFactory = new CubicleBlockFactory();
        }

        private CubicleBlock(Patch patch, boolean attractor, boolean hasGraphic) {
            super(patch, attractor, hasGraphic);
        }

        public static class CubicleBlockFactory extends AmenityBlockFactory {
            @Override
            public CubicleBlock create(Patch patch, boolean attractor, boolean hasGraphic) {
                return new CubicleBlock(patch, attractor, hasGraphic);
            }
        }
    }

    public static class CubicleFactory extends GoalFactory {
        public static Cubicle create(List<AmenityBlock> amenityBlocks, boolean enabled, String type, String facing, String tableOn, boolean withAppliance) {
            return new Cubicle(amenityBlocks, enabled, type, facing, tableOn, withAppliance);
        }
    }
}