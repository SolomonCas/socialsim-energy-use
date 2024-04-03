package com.socialsim.model.core.environment.patchobject.passable.goal;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.controller.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.controller.graphics.amenity.graphic.CouchGraphic;
import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchobject.Amenity;

import java.util.List;

public class Couch extends Goal {
    /***** VARIABLES *****/
    public static final Couch.CouchFactory couchFactory;
    private final CouchGraphic couchGraphic;

    static {
        couchFactory = new Couch.CouchFactory();
    }


    /***** CONSTRUCTOR *****/
    protected Couch(List<AmenityBlock> amenityBlocks, boolean enabled, String facing) {
        super(amenityBlocks, enabled);

        this.couchGraphic = new CouchGraphic(this, facing);
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
    public static class CouchBlock extends Amenity.AmenityBlock {
        public static Couch.CouchBlock.CouchBlockFactory couchBlockFactory;

        static {
            couchBlockFactory = new Couch.CouchBlock.CouchBlockFactory();
        }

        private CouchBlock(Patch patch, boolean attractor, boolean hasGraphic) {
            super(patch, attractor, hasGraphic);
        }

        public static class CouchBlockFactory extends Amenity.AmenityBlock.AmenityBlockFactory {
            @Override
            public Couch.CouchBlock create(Patch patch, boolean attractor, boolean hasGraphic) {
                return new Couch.CouchBlock(patch, attractor, hasGraphic);
            }
        }
    }

    public static class CouchFactory extends GoalFactory {
        public static Couch create(List<AmenityBlock> amenityBlocks, boolean enabled, String facing) {
            return new Couch(amenityBlocks, enabled, facing);
        }
    }
}