package com.socialsim.model.core.environment.patchobject.passable.goal;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.controller.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.controller.graphics.amenity.graphic.OfficeToiletGraphic;
import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchobject.Amenity;

import java.util.List;

public class OfficeToilet extends Goal {

    /***** VARIABLES *****/
    public static final OfficeToilet.OfficeToiletFactory officeToiletFactory;
    private final OfficeToiletGraphic officeToiletGraphic;

    private boolean isClean = false;

    static {
        officeToiletFactory = new OfficeToilet.OfficeToiletFactory();
    }

    /***** CONSTRUCTOR *****/
    protected OfficeToilet(List<AmenityBlock> amenityBlocks, boolean enabled) {
        super(amenityBlocks, enabled);

        this.officeToiletGraphic = new OfficeToiletGraphic(this);
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
        return "OfficeToilet" + ((this.enabled) ? "" : " (disabled)");
    }

    @Override
    public AmenityGraphic getGraphicObject() {
        return this.officeToiletGraphic;
    }

    @Override
    public AmenityGraphicLocation getGraphicLocation() {
        return this.officeToiletGraphic.getGraphicLocation();
    }


    /***** INNER STATIC CLASS *****/
    public static class OfficeToiletBlock extends Amenity.AmenityBlock {
        public static OfficeToilet.OfficeToiletBlock.OfficeToiletBlockFactory officeToiletBlockFactory;

        static {
            officeToiletBlockFactory = new OfficeToilet.OfficeToiletBlock.OfficeToiletBlockFactory();
        }

        private OfficeToiletBlock(Patch patch, boolean attractor, boolean hasGraphic) {
            super(patch, attractor, hasGraphic);
        }

        public static class OfficeToiletBlockFactory extends Amenity.AmenityBlock.AmenityBlockFactory {
            @Override
            public OfficeToilet.OfficeToiletBlock create(Patch patch, boolean attractor, boolean hasGraphic) {
                return new OfficeToilet.OfficeToiletBlock(patch, attractor, hasGraphic);
            }
        }
    }

    public static class OfficeToiletFactory extends GoalFactory {
        public static OfficeToilet create(List<AmenityBlock> amenityBlocks, boolean enabled) {
            return new OfficeToilet(amenityBlocks, enabled);
        }
    }
}