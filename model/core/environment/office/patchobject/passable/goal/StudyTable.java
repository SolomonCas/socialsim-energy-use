package com.socialsim.model.core.environment.office.patchobject.passable.goal;

import com.socialsim.controller.generic.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.controller.office.graphics.amenity.OfficeAmenityGraphic;
import com.socialsim.controller.office.graphics.amenity.graphic.StudyTableGraphic;
import com.socialsim.model.core.environment.generic.Patch;
import com.socialsim.model.core.environment.generic.patchobject.passable.goal.Goal;

import java.util.List;

public class StudyTable extends Goal {

    public static final StudyTableFactory studyTableFactory;
    private final StudyTableGraphic studyTableGraphic;

    static {
        studyTableFactory = new StudyTableFactory();
    }

    protected StudyTable(List<AmenityBlock> amenityBlocks, boolean enabled, String facing) {
        super(amenityBlocks, enabled);

        this.studyTableGraphic = new StudyTableGraphic(this, facing);
    }

    @Override
    public String toString() {
        return "StudyTable" + ((this.enabled) ? "" : " (disabled)");
    }

    @Override
    public OfficeAmenityGraphic getGraphicObject() {
        return this.studyTableGraphic;
    }

    @Override
    public AmenityGraphicLocation getGraphicLocation() {
        return this.studyTableGraphic.getGraphicLocation();
    }

    public static class StudyTableBlock extends AmenityBlock {
        public static StudyTableBlockFactory studyTableBlockFactory;

        static {
            studyTableBlockFactory = new StudyTableBlockFactory();
        }

        private StudyTableBlock(Patch patch, boolean attractor, boolean hasGraphic) {
            super(patch, attractor, hasGraphic);
        }

        public static class StudyTableBlockFactory extends AmenityBlockFactory {
            @Override
            public StudyTableBlock create(Patch patch, boolean attractor, boolean hasGraphic) {
                return new StudyTableBlock(patch, attractor, hasGraphic);
            }
        }
    }

    public static class StudyTableFactory extends GoalFactory {
        public static StudyTable create(List<AmenityBlock> amenityBlocks, boolean enabled, String facing) {
            return new StudyTable(amenityBlocks, enabled, facing);
        }
    }

}