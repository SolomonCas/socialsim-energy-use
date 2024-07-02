package com.socialsim.model.core.environment.patchobject.passable.goal;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.controller.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.controller.graphics.amenity.graphic.CouchGraphic;
import com.socialsim.model.core.environment.Patch;

import java.util.List;

public class Couch extends Goal {
    /***** VARIABLES *****/
    public static final CouchFactory couchFactory;
    private final CouchGraphic couchGraphic;

    static {
        couchFactory = new CouchFactory();
    }

    private final String facing;


    /***** CONSTRUCTOR *****/
    protected Couch(List<AmenityBlock> amenityBlocks, boolean enabled, String facing) {
        super(amenityBlocks, enabled);
        this.facing = facing;
        this.couchGraphic = new CouchGraphic(this, facing);
    }

    /***** GETTER *****/
    public String getFacing() {
        return facing;
    }

    /***** OVERRIDE *****/
    @Override
    public String toString() {
        return "Couch" + ((this.enabled) ? "" : " (disabled)");
    }

    @Override
    public AmenityGraphic getGraphicObject() {
        return this.couchGraphic;
    }

    @Override
    public AmenityGraphicLocation getGraphicLocation() {
        return this.couchGraphic.getGraphicLocation();
    }


    /***** INNER STATIC CLASS *****/
    public static class CouchBlock extends AmenityBlock {
        public static CouchBlockFactory couchBlockFactory;

        static {
            couchBlockFactory = new CouchBlockFactory();
        }

        private CouchBlock(Patch patch, boolean attractor, boolean hasGraphic) {
            super(patch, attractor, hasGraphic);
        }

        public static class CouchBlockFactory extends AmenityBlockFactory {
            @Override
            public CouchBlock create(Patch patch, boolean attractor, boolean hasGraphic) {
                return new CouchBlock(patch, attractor, hasGraphic);
            }
        }
    }

    public static class CouchFactory extends GoalFactory {
        public static Couch create(List<AmenityBlock> amenityBlocks, boolean enabled, String facing) {
            return new Couch(amenityBlocks, enabled, facing);
        }
    }
}