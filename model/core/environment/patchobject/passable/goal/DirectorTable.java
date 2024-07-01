package com.socialsim.model.core.environment.patchobject.passable.goal;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.controller.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.controller.graphics.amenity.graphic.DirectorTableGraphic;
import com.socialsim.model.core.environment.Patch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DirectorTable extends Goal {

    /***** VARIABLES *****/
    public static final DirectorTableFactory directorTableFactory;
    private final DirectorTableGraphic directorTableGraphic;
    private final List<Chair> directorChairs;
    private final List<Monitor> monitors;

    static {
        directorTableFactory = new DirectorTableFactory();
    }

    /***** CONSTRUCTOR *****/
    protected DirectorTable(List<AmenityBlock> amenityBlocks, boolean enabled, String orientation) {
        super(amenityBlocks, enabled);
        this.directorTableGraphic = new DirectorTableGraphic(this, orientation);
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
        return this.directorTableGraphic;
    }

    @Override
    public AmenityGraphicLocation getGraphicLocation() {
        return this.directorTableGraphic.getGraphicLocation();
    }




    /***** INNER STATIC CLASS *****/
    public static class DirectorTableBlock extends AmenityBlock {
        public static DirectorTableBlockFactory directorTableBlockFactory;

        static {
            directorTableBlockFactory = new DirectorTableBlockFactory();
        }

        private DirectorTableBlock(Patch patch, boolean attractor, boolean hasGraphic) {
            super(patch, attractor, hasGraphic);
        }

        public static class DirectorTableBlockFactory extends AmenityBlockFactory {
            @Override
            public DirectorTableBlock create(Patch patch, boolean attractor, boolean hasGraphic) {
                return new DirectorTableBlock(patch, attractor, hasGraphic);
            }
        }
    }

    public static class DirectorTableFactory extends GoalFactory {
        public static DirectorTable create(List<AmenityBlock> amenityBlocks, boolean enabled, String orientation) {
            return new DirectorTable(amenityBlocks, enabled, orientation);
        }
    }
}