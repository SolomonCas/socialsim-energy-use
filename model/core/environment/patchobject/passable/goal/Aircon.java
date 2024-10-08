package com.socialsim.model.core.environment.patchobject.passable.goal;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.controller.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.controller.graphics.amenity.graphic.AirconGraphic;
import com.socialsim.model.core.environment.Patch;

import java.util.List;

public class Aircon extends Goal {

    /***** VARIABLES *****/
    public static final AirconFactory airconFactory;
    private final AirconGraphic airconGraphic;

    /* Indicates whether aircon will go active cycle or not
          and what temperature the agent will feel */
    private int roomTemp;
    //aircon's set temp and threshold
    private int airconTemp;
    private int coolingTimeInTicks;
    private boolean isInActiveCycle;
    private boolean isOn;
    private int coolingRange;
    private boolean isCoolingStarted;
    static {
        airconFactory = new AirconFactory();
    }

    /***** CONSTRUCTOR *****/
    protected Aircon(List<AmenityBlock> amenityBlocks, boolean enabled, boolean isOn) {
        super(amenityBlocks, enabled);
        this.isOn = isOn;
        this.isInActiveCycle = false;

        this.roomTemp = 28;
        this.airconTemp = 28;
        this.coolingTimeInTicks = 0;
        this.coolingRange = 25;
        this.isCoolingStarted = false;

        this.airconGraphic = new AirconGraphic(this, isOn);
    }

    /***** GETTER *****/
    public boolean isTurnedOn() {
        return isOn;
    }

    public AirconGraphic getAirconGraphic() {
        return airconGraphic;
    }

    public boolean isInActiveCycle() {
        return isInActiveCycle;
    }

    public int getRoomTemp() {
        return roomTemp;
    }

    public int getAirconTemp() {
        return airconTemp;
    }

    public int getCoolingTimeInTicks() {
        return coolingTimeInTicks;
    }

    public int getCoolingRange() {
        return coolingRange;
    }

    public boolean isCoolingStarted() {
        return isCoolingStarted;
    }

    /***** SETTER *****/
    public void setOn(boolean on) {
        isOn = on;
        if (!isOn) {
            airconTemp = 28;
        }
    }

    public void setInActiveCycle(boolean inActiveCycle) {
        isInActiveCycle = inActiveCycle;
    }

    public void setRoomTemp(int roomTemp) {
        this.roomTemp = roomTemp;
    }

    public void setAirconTemp(int airconTemp) {
        this.airconTemp = airconTemp;
    }

    public void setCoolingTimeInTicks(int coolingTimeInTicks) {
        this.coolingTimeInTicks = coolingTimeInTicks;
    }

    public void setCoolingRange(int coolingRange) {
        this.coolingRange = coolingRange;
    }

    public void setCoolingStarted(boolean coolingStarted) {
        isCoolingStarted = coolingStarted;
    }
    /***** OVERRIDE *****/
    @Override
    public String toString() {
        return "Aircon" + ((this.enabled) ? "" : " (disabled)");
    }

    @Override
    public AmenityGraphic getGraphicObject() {
        return this.airconGraphic;
    }

    @Override
    public AmenityGraphicLocation getGraphicLocation() {
        return this.airconGraphic.getGraphicLocation();
    }



    /***** INNER STATIC CLASS *****/
    public static class AirconBlock extends AmenityBlock {
        public static AirconBlockFactory airconBlockFactory;

        static {
            airconBlockFactory = new AirconBlockFactory();
        }

        private AirconBlock(Patch patch, boolean attractor, boolean hasGraphic) {
            super(patch, attractor, hasGraphic);
        }

        public static class AirconBlockFactory extends AmenityBlockFactory {
            @Override
            public AirconBlock create(Patch patch, boolean attractor, boolean hasGraphic) {
                return new AirconBlock(patch, attractor, hasGraphic);
            }
        }
    }

    public static class AirconFactory extends GoalFactory {
        public static Aircon create(List<AmenityBlock> amenityBlocks, boolean enabled, boolean isTurnedOn) {
            return new Aircon(amenityBlocks, enabled, isTurnedOn);
        }
    }

}