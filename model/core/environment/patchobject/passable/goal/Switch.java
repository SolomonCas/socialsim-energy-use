package com.socialsim.model.core.environment.patchobject.passable.goal;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.controller.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.controller.graphics.amenity.graphic.SwitchGraphic;
import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchfield.PatchField;

import java.util.List;

public class Switch extends Goal {

    /***** VARIABLES *****/
    public static final SwitchFactory switchFactory;
    private final SwitchGraphic switchGraphic;

    static {
        switchFactory = new SwitchFactory();
    }

    private final String type;
    private boolean isTurnedOn;

    /***** CONSTRUCTOR *****/
    protected Switch(List<AmenityBlock> amenityBlocks, boolean enabled, String type, String facing) {
        super(amenityBlocks, enabled);
        this.type = type;
//        this.isTurnedOn = isTurnedOn;

        this.switchGraphic = new SwitchGraphic(this, type, facing);
    }

//    protected Switch(List<AmenityBlock> amenityBlocks, boolean enabled, List<PatchField> area, String type, String facing, List<Patch> scope, boolean isTurnedOn) {
//        super(amenityBlocks, enabled);
//        this.type = type;
//        this.isTurnedOn = isTurnedOn;
//
//        this.switchGraphic = new SwitchGraphic(this, area, type, facing, scope, isTurnedOn);
//    }

    /***** GETTER *****/
    public String getType() {
        return type;
    }
    public boolean isTurnedOn() {
        return isTurnedOn;
    }

    /* Switch to On */
    public void setTurnedOn(boolean isTurnedOn) {
        this.isTurnedOn = isTurnedOn;
    }


    /***** OVERRIDE *****/
    @Override
    public String toString() {
        return "Switch" + ((this.enabled) ? "" : " (disabled)");
    }

    @Override
    public AmenityGraphic getGraphicObject() {
        return this.switchGraphic;
    }

    @Override
    public AmenityGraphicLocation getGraphicLocation() {
        return this.switchGraphic.getGraphicLocation();
    }


    /***** INNER STATIC CLASS *****/
    public static class SwitchBlock extends AmenityBlock {
        public static SwitchBlockFactory switchBlockFactory;

        static {
            switchBlockFactory = new SwitchBlockFactory();
        }

        private SwitchBlock(Patch patch, boolean attractor, boolean hasGraphic) {
            super(patch, attractor, hasGraphic);
        }

        public static class SwitchBlockFactory extends AmenityBlockFactory {
            @Override
            public SwitchBlock create(Patch patch, boolean attractor, boolean hasGraphic) {
                return new SwitchBlock(patch, attractor, hasGraphic);
            }
        }
    }

    public static class SwitchFactory extends GoalFactory {
        public static Switch create(List<AmenityBlock> amenityBlocks, boolean enabled, String type, String facing) {
            return new Switch(amenityBlocks, enabled, type, facing);
        }
    }

}