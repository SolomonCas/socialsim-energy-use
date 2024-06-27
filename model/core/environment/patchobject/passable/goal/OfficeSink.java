package com.socialsim.model.core.environment.patchobject.passable.goal;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.controller.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.controller.graphics.amenity.graphic.SinkGraphic;
import com.socialsim.model.core.environment.Patch;

import java.util.List;

public class OfficeSink extends Goal {

    /***** VARIABLES *****/
    public static final OfficeSinkFactory officeSinkFactory;
    private final SinkGraphic officeSinkGraphic;
    private boolean isClean = false;

    static {
        officeSinkFactory = new OfficeSinkFactory();
    }

    /***** CONSTRUCTOR *****/
    protected OfficeSink(List<AmenityBlock> amenityBlocks, boolean enabled, String facing) {
        super(amenityBlocks, enabled);

        this.officeSinkGraphic = new SinkGraphic(this, facing);
    }

    /***** GETTER *****/
    public boolean isClean() {
        return isClean;
    }

    /***** SETTER *****/
    public void setClean(boolean clean) {
        isClean = clean;
    }

    /***** OVERRIDE *****/
    @Override
    public String toString() {
        return "OfficeSink" + ((this.enabled) ? "" : " (disabled)");
    }

    @Override
    public AmenityGraphic getGraphicObject() {
        return this.officeSinkGraphic;
    }

    @Override
    public AmenityGraphicLocation getGraphicLocation() {
        return this.officeSinkGraphic.getGraphicLocation();
    }


    /***** INNER STATIC CLASS *****/
    public static class OfficeSinkBlock extends AmenityBlock {
        public static OfficeSinkBlockFactory officeSinkBlockFactory;

        static {
            officeSinkBlockFactory = new OfficeSinkBlockFactory();
        }

        private OfficeSinkBlock(Patch patch, boolean attractor, boolean hasGraphic) {
            super(patch, attractor, hasGraphic);
        }

        public static class OfficeSinkBlockFactory extends AmenityBlockFactory {
            @Override
            public OfficeSinkBlock create(Patch patch, boolean attractor, boolean hasGraphic) {
                return new OfficeSinkBlock(patch, attractor, hasGraphic);
            }
        }
    }

    public static class OfficeSinkFactory extends GoalFactory {
        public static OfficeSink create(List<AmenityBlock> amenityBlocks, boolean enabled, String facing) {
            return new OfficeSink(amenityBlocks, enabled, facing);
        }
    }
}