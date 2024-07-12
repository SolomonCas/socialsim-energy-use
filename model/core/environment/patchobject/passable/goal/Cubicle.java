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
    private final List<Monitor> monitors;
    private final List<Chair> chairs;
    private final String type;

    /***** CONSTRUCTOR *****/
    protected Cubicle(List<AmenityBlock> amenityBlocks, boolean enabled, String type, String facing, String tableOn) {
        super(amenityBlocks, enabled);
        this.type = type;
        this.cubicleGraphic = new CubicleGraphic(this, type, facing, tableOn);
        this.monitors = Collections.synchronizedList(new ArrayList<>());
        this.chairs = Collections.synchronizedList(new ArrayList<>());
    }

    /***** GETTERS *****/
    public List<Chair> getCubicleChairs() {
        return chairs;
    }
    public List<Monitor> getMonitors() {
        return monitors;
    }

    public String getType() {
        return type;
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
        public static Cubicle create(List<AmenityBlock> amenityBlocks, boolean enabled, String type, String facing, String tableOn) {
            return new Cubicle(amenityBlocks, enabled, type, facing, tableOn);
        }
    }
}