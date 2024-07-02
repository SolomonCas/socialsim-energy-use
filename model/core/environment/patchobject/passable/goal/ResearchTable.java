package com.socialsim.model.core.environment.patchobject.passable.goal;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.controller.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.controller.graphics.amenity.graphic.ResearchTableGraphic;
import com.socialsim.model.core.environment.Patch;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ResearchTable extends Goal {


    /***** VARIABLES *****/
    public static final ResearchTableFactory researchTableFactory;
    private final ResearchTableGraphic researchTableGraphic;
    private final List<Chair> researchChairs;
    private final List<Monitor> monitors;
    static {
        researchTableFactory = new ResearchTable.ResearchTableFactory();
    }




    /***** CONSTRUCTOR *****/
    protected ResearchTable(List<AmenityBlock> amenityBlocks, boolean enabled, String facing) {
        super(amenityBlocks, enabled);
        this.researchTableGraphic = new ResearchTableGraphic(this, facing);
        this.researchChairs = Collections.synchronizedList(new ArrayList<>());
        this.monitors = Collections.synchronizedList(new ArrayList<>());
    }




    /***** GETTERS *****/
    public List<Chair> getResearchChairs() {
        return researchChairs;
    }

    public List<Monitor> getMonitors() {
        return monitors;
    }

    /***** OVERRIDE *****/
    @Override
    public String toString() {
        return "Research Table" + ((this.enabled) ? "" : " (disabled)");
    }

    @Override
    public AmenityGraphic getGraphicObject() {
        return this.researchTableGraphic;
    }

    @Override
    public AmenityGraphicLocation getGraphicLocation() {
        return this.researchTableGraphic.getGraphicLocation();
    }




    /***** INNER STATIC CLASSES *****/
    public static class ResearchTableBlock extends AmenityBlock {
        public static ResearchTable.ResearchTableBlock.ResearchTableBlockFactory researchTableBlockFactory;

        static {
            researchTableBlockFactory = new ResearchTable.ResearchTableBlock.ResearchTableBlockFactory();
        }

        private ResearchTableBlock(Patch patch, boolean attractor, boolean hasGraphic) {
            super(patch, attractor, hasGraphic);
        }

        public static class ResearchTableBlockFactory extends AmenityBlockFactory {
            @Override
            public ResearchTable.ResearchTableBlock create(Patch patch, boolean attractor, boolean hasGraphic) {
                return new ResearchTable.ResearchTableBlock(patch, attractor, hasGraphic);
            }
        }
    }

    public static class ResearchTableFactory extends GoalFactory {
        public static ResearchTable create(List<AmenityBlock> amenityBlocks, boolean enabled, String facing) {
            return new ResearchTable(amenityBlocks, enabled, facing);
        }
    }
}
