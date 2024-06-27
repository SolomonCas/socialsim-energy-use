package com.socialsim.model.core.environment.patchobject.passable.goal;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.controller.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.controller.graphics.amenity.graphic.BarGraphic;
import com.socialsim.model.core.environment.Patch;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HumanExpTable extends Goal {

    /***** VARIABLES *****/
    @Serial
    private static final long serialVersionUID = -5458621245735102190L;
    public static final HumanExpTableFactory humanExpTableFactory;
    private final BarGraphic barGraphic;

    private final List<HumanExpChair> humanExpChairs;

    static {
        humanExpTableFactory = new HumanExpTableFactory();
    }

    /***** CONSTRUCTOR *****/
    protected HumanExpTable(List<AmenityBlock> amenityBlocks, boolean enabled, String dimensions) {
        super(amenityBlocks, enabled);
        this.barGraphic = new BarGraphic(this, dimensions);
        this.humanExpChairs = Collections.synchronizedList(new ArrayList<>());
    }

    /***** GETTER *****/
    public List<HumanExpChair> getHumanExpChairs() {
        return humanExpChairs;
    }

    /***** OVERRIDE *****/
    @Override
    public String toString() {
        return "HumanExp Table" + ((this.enabled) ? "" : " (disabled)");
    }

    @Override
    public AmenityGraphic getGraphicObject() {
        return this.barGraphic;
    }

    @Override
    public AmenityGraphicLocation getGraphicLocation() {
        return this.barGraphic.getGraphicLocation();
    }


    /***** INNER STATIC CLASS *****/
    public static class HumanExpTableBlock extends AmenityBlock {
        public static HumanExpTableBlockFactory humanExpTableBlockFactory;

        static {
            humanExpTableBlockFactory = new HumanExpTableBlockFactory();
        }

        private HumanExpTableBlock(Patch patch, boolean attractor, boolean hasGraphic) {
            super(patch, attractor, hasGraphic);
        }

        public static class HumanExpTableBlockFactory extends AmenityBlockFactory {
            @Override
            public HumanExpTableBlock create(Patch patch, boolean attractor, boolean hasGraphic) {
                return new HumanExpTableBlock(patch, attractor, hasGraphic);
            }
        }
    }

    public static class HumanExpTableFactory extends GoalFactory {
        public static HumanExpTable create(List<AmenityBlock> amenityBlocks, boolean enabled, String dimensions) {
            return new HumanExpTable(amenityBlocks, enabled, dimensions);
        }
    }
}