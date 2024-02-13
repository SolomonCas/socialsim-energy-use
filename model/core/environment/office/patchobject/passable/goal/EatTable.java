package com.socialsim.model.core.environment.office.patchobject.passable.goal;

import com.socialsim.controller.generic.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.controller.office.graphics.amenity.OfficeAmenityGraphic;
import com.socialsim.controller.office.graphics.amenity.graphic.EatTableGraphic;
import com.socialsim.model.core.environment.generic.Patch;
import com.socialsim.model.core.environment.generic.patchobject.Amenity;
import com.socialsim.model.core.environment.generic.patchobject.passable.goal.Goal;

import java.util.List;

public class EatTable extends Goal {

    public static final EatTableFactory eatTableFactory;
    private final EatTableGraphic eatTableGraphic;

    static {
        eatTableFactory = new EatTableFactory();
    }

    protected EatTable(List<AmenityBlock> amenityBlocks, boolean enabled) {
        super(amenityBlocks, enabled);

        this.eatTableGraphic = new EatTableGraphic(this);
    }


    @Override
    public String toString() {
        return "EatTable" + ((this.enabled) ? "" : " (disabled)");
    }

    @Override
    public OfficeAmenityGraphic getGraphicObject() {
        return this.eatTableGraphic;
    }

    @Override
    public AmenityGraphicLocation getGraphicLocation() {
        return this.eatTableGraphic.getGraphicLocation();
    }

    public static class EatTableBlock extends AmenityBlock {
        public static EatTableBlockFactory eatTableBlockFactory;

        static {
            eatTableBlockFactory = new EatTableBlockFactory();
        }

        private EatTableBlock(Patch patch, boolean attractor, boolean hasGraphic) {
            super(patch, attractor, hasGraphic);
        }

        public static class EatTableBlockFactory extends AmenityBlockFactory {
            @Override
            public EatTableBlock create(Patch patch, boolean attractor, boolean hasGraphic) {
                return new EatTableBlock(patch, attractor, hasGraphic);
            }
        }
    }

    public static class EatTableFactory extends GoalFactory {
        public static EatTable create(List<AmenityBlock> amenityBlocks, boolean enabled) {
            return new EatTable(amenityBlocks, enabled);
        }
    }

}