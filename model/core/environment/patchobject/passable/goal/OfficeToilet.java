package com.socialsim.model.core.environment.patchobject.passable.goal;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.controller.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.controller.graphics.amenity.graphic.ToiletGraphic;
import com.socialsim.model.core.environment.Patch;

import java.util.List;

public class OfficeToilet extends Goal {

    /***** VARIABLES *****/
    public static final OfficeToiletFactory officeToiletFactory;
    private final ToiletGraphic officeToiletGraphic;

    private boolean isClean = false;

    static {
        officeToiletFactory = new OfficeToiletFactory();
    }

    /***** CONSTRUCTOR *****/
    protected OfficeToilet(List<AmenityBlock> amenityBlocks, boolean enabled, String facing) {
        super(amenityBlocks, enabled);

        this.officeToiletGraphic = new ToiletGraphic(this, facing);
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
    public static class OfficeToiletBlock extends AmenityBlock {
        public static OfficeToiletBlockFactory officeToiletBlockFactory;

        static {
            officeToiletBlockFactory = new OfficeToiletBlockFactory();
        }

        private OfficeToiletBlock(Patch patch, boolean attractor, boolean hasGraphic) {
            super(patch, attractor, hasGraphic);
        }

        public static class OfficeToiletBlockFactory extends AmenityBlockFactory {
            @Override
            public OfficeToiletBlock create(Patch patch, boolean attractor, boolean hasGraphic) {
                return new OfficeToiletBlock(patch, attractor, hasGraphic);
            }
        }
    }

    public static class OfficeToiletFactory extends GoalFactory {
        public static OfficeToilet create(List<AmenityBlock> amenityBlocks, boolean enabled, String facing) {
            return new OfficeToilet(amenityBlocks, enabled, facing);
        }
    }
}