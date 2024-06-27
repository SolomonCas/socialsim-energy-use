package com.socialsim.model.core.environment.patchobject.passable.goal;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.controller.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.controller.graphics.amenity.graphic.ChairGraphic;
import com.socialsim.model.core.environment.Patch;

import java.util.List;

public class LearningChair extends Goal {
    /***** VARIABLES *****/
    public static final LearningChairFactory learningChairFactory;
    private final ChairGraphic officeChairGraphic;

    static {
        learningChairFactory = new LearningChairFactory();
    }

    /***** CONSTRUCTOR *****/
    protected LearningChair(List<AmenityBlock> amenityBlocks, boolean enabled, String facing, String type) {
        super(amenityBlocks, enabled);

        this.officeChairGraphic = new ChairGraphic(this, facing, type);
    }

    /***** OVERRIDE *****/
    @Override
    public String toString() {
        return "LearningChair" + ((this.enabled) ? "" : " (disabled)");
    }

    @Override
    public AmenityGraphic getGraphicObject() {
        return this.officeChairGraphic;
    }

    @Override
    public AmenityGraphicLocation getGraphicLocation() {
        return this.officeChairGraphic.getGraphicLocation();
    }


    /***** INNER STATIC CLASS *****/
    public static class LearningChairBlock extends AmenityBlock {
        public static LearningChairBlockFactory learningChairBlockFactory;

        static {
            learningChairBlockFactory = new LearningChairBlockFactory();
        }

        private LearningChairBlock(Patch patch, boolean attractor, boolean hasGraphic) {
            super(patch, attractor, hasGraphic);
        }

        public static class LearningChairBlockFactory extends AmenityBlockFactory {
            @Override
            public LearningChairBlock create(Patch patch, boolean attractor, boolean hasGraphic) {
                return new LearningChairBlock(patch, attractor, hasGraphic);
            }
        }
    }

    public static class LearningChairFactory extends GoalFactory {
        public static LearningChair create(List<AmenityBlock> amenityBlocks, boolean enabled, String facing, String type) {
            return new LearningChair(amenityBlocks, enabled, facing, type);
        }
    }
}