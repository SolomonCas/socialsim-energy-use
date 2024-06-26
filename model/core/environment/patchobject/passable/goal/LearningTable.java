package com.socialsim.model.core.environment.patchobject.passable.goal;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.controller.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.controller.graphics.amenity.graphic.LearningTableGraphic;
import com.socialsim.model.core.environment.Patch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LearningTable extends Goal {


    /***** VARIABLES *****/
    public static final LearningTableFactory learningTableFactory;
    private final LearningTableGraphic learningTableGraphic;

    private final List<LearningChair> learningChairs;
    
    static {
        learningTableFactory = new LearningTable.LearningTableFactory();
    }



    /***** CONSTRUCTOR *****/
    protected LearningTable(List<AmenityBlock> amenityBlocks, boolean enabled, String orientation) {
        super(amenityBlocks, enabled);
        this.learningTableGraphic = new LearningTableGraphic(this, orientation);
        this.learningChairs = Collections.synchronizedList(new ArrayList<>());
    }



    /***** GETTERS *****/
    public List<LearningChair> getLearningChairs() {
        return learningChairs;
    }


    /***** OVERRIDE *****/
    @Override
    public String toString() {
        return "Learning Table" + ((this.enabled) ? "" : " (disabled)");
    }

    @Override
    public AmenityGraphic getGraphicObject() {
        return this.learningTableGraphic;
    }

    @Override
    public AmenityGraphicLocation getGraphicLocation() {
        return this.learningTableGraphic.getGraphicLocation();
    }




    /***** INNER STATIC CLASSES *****/
    public static class LearningTableBlock extends AmenityBlock {
        public static LearningTable.LearningTableBlock.LearningTableBlockFactory learningTableBlockFactory;

        static {
            learningTableBlockFactory = new LearningTable.LearningTableBlock.LearningTableBlockFactory();
        }

        private LearningTableBlock(Patch patch, boolean attractor, boolean hasGraphic) {
            super(patch, attractor, hasGraphic);
        }

        public static class LearningTableBlockFactory extends AmenityBlockFactory {
            @Override
            public LearningTable.LearningTableBlock create(Patch patch, boolean attractor, boolean hasGraphic) {
                return new LearningTable.LearningTableBlock(patch, attractor, hasGraphic);
            }
        }
    }

    public static class LearningTableFactory extends GoalFactory {
        public static LearningTable create(List<AmenityBlock> amenityBlocks, boolean enabled, String orientation) {
            return new LearningTable(amenityBlocks, enabled, orientation);
        }
    }
}
