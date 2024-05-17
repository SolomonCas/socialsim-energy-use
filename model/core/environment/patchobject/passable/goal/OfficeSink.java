package com.socialsim.model.core.environment.patchobject.passable.goal;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.controller.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.controller.graphics.amenity.graphic.OfficeSinkGraphic;
import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchobject.Amenity;

import java.util.List;

public class OfficeSink extends Goal {

    /***** VARIABLES *****/
    public static final OfficeSink.OfficeSinkFactory officeSinkFactory;
    private final OfficeSinkGraphic officeSinkGraphic;

    static {
        officeSinkFactory = new OfficeSink.OfficeSinkFactory();
    }

    /***** CONSTRUCTOR *****/
    protected OfficeSink(List<AmenityBlock> amenityBlocks, boolean enabled) {
        super(amenityBlocks, enabled);

        this.officeSinkGraphic = new OfficeSinkGraphic(this);
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
    public static class OfficeSinkBlock extends Amenity.AmenityBlock {
        public static OfficeSink.OfficeSinkBlock.OfficeSinkBlockFactory officeSinkBlockFactory;

        static {
            officeSinkBlockFactory = new OfficeSink.OfficeSinkBlock.OfficeSinkBlockFactory();
        }

        private OfficeSinkBlock(Patch patch, boolean attractor, boolean hasGraphic) {
            super(patch, attractor, hasGraphic);
        }

        public static class OfficeSinkBlockFactory extends Amenity.AmenityBlock.AmenityBlockFactory {
            @Override
            public OfficeSink.OfficeSinkBlock create(Patch patch, boolean attractor, boolean hasGraphic) {
                return new OfficeSink.OfficeSinkBlock(patch, attractor, hasGraphic);
            }
        }
    }

    public static class OfficeSinkFactory extends GoalFactory {
        public static OfficeSink create(List<AmenityBlock> amenityBlocks, boolean enabled) {
            return new OfficeSink(amenityBlocks, enabled);
        }
    }
}