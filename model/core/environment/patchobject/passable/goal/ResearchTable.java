package com.socialsim.model.core.environment.patchobject.passable.goal;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.controller.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.controller.graphics.amenity.graphic.ResearchTableGraphic;
import com.socialsim.model.core.environment.Patch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ResearchTable extends Goal {


    /***** VARIABLES *****/
    public static final ResearchTableFactory researchTableFactory;
    private final ResearchTableGraphic researchTableGraphic;
    private final List<Chair> researchChairs;
    static {
        researchTableFactory = new ResearchTable.ResearchTableFactory();
    }
    private final boolean withAppliance;
    private boolean isOn;




    /***** CONSTRUCTOR *****/
    protected ResearchTable(List<AmenityBlock> amenityBlocks, boolean enabled, String facing, boolean withAppliance) {
        super(amenityBlocks, enabled);
        this.researchTableGraphic = new ResearchTableGraphic(this, facing, withAppliance);
        this.researchChairs = Collections.synchronizedList(new ArrayList<>());
        this.withAppliance = withAppliance;
        this.isOn = false;
    }




    /***** GETTERS *****/
    public List<Chair> getResearchChairs() {
        return researchChairs;
    }
    public boolean withAppliance() {
        return withAppliance;
    }

    public boolean isOn() {
        return isOn;
    }

    /***** SETTER *****/
    public void setOn(boolean on) {
        isOn = on;
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
        public static ResearchTable create(List<AmenityBlock> amenityBlocks, boolean enabled, String facing, boolean withAppliance) {
            return new ResearchTable(amenityBlocks, enabled, facing, withAppliance);
        }
    }
}
