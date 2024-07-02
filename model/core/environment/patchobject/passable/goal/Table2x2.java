package com.socialsim.model.core.environment.patchobject.passable.goal;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.controller.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.controller.graphics.amenity.graphic.Table2x2Graphic;
import com.socialsim.model.core.environment.Patch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Table2x2 extends Goal {

    /***** VARIABLES *****/
    public static final Table2x2Factory table2x2Factory;
    private final Table2x2Graphic table2x2Graphic;
    private final List<Chair> directorChairs;
    private final List<Monitor> monitors;

    static {
        table2x2Factory = new Table2x2Factory();
    }

    /***** CONSTRUCTOR *****/
    protected Table2x2(List<AmenityBlock> amenityBlocks, boolean enabled) {
        super(amenityBlocks, enabled);
        this.table2x2Graphic = new Table2x2Graphic(this);
        this.directorChairs = Collections.synchronizedList(new ArrayList<>());
        this.monitors = Collections.synchronizedList(new ArrayList<>());

    }

    /***** GETTERS *****/
    public List<Chair> getDirectorChairs() {
        return directorChairs;
    }

    public List<Monitor> getMonitors() {
        return monitors;
    }

    /***** OVERRIDE *****/
    @Override
    public String toString() {
        return "Director Table" + ((this.enabled) ? "" : " (disabled)");
    }

    @Override
    public AmenityGraphic getGraphicObject() {
        return this.table2x2Graphic;
    }

    @Override
    public AmenityGraphicLocation getGraphicLocation() {
        return this.table2x2Graphic.getGraphicLocation();
    }




    /***** INNER STATIC CLASS *****/
    public static class Table2x2Block extends AmenityBlock {
        public static Table2x2BlockFactory table2x2BlockFactory;

        static {
            table2x2BlockFactory = new Table2x2BlockFactory();
        }

        private Table2x2Block(Patch patch, boolean attractor, boolean hasGraphic) {
            super(patch, attractor, hasGraphic);
        }

        public static class Table2x2BlockFactory extends AmenityBlockFactory {
            @Override
            public Table2x2Block create(Patch patch, boolean attractor, boolean hasGraphic) {
                return new Table2x2Block(patch, attractor, hasGraphic);
            }
        }
    }

    public static class Table2x2Factory extends GoalFactory {
        public static Table2x2 create(List<AmenityBlock> amenityBlocks, boolean enabled) {
            return new Table2x2(amenityBlocks, enabled);
        }
    }
}