package com.socialsim.model.core.environment.patchobject.passable.gate;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.controller.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.controller.graphics.amenity.graphic.GateGraphic;
import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchobject.Drawable;
import com.socialsim.model.core.environment.patchobject.passable.NonObstacle;

import java.util.ArrayList;
import java.util.List;

public class Gate extends NonObstacle implements Drawable {

    // VARIABLES
    private final List<GateBlock> spawners;
    private double chancePerTick;
    private GateMode gateMode;
    private final GateGraphic gateGraphic;



    // CONSTRUCTORS
    public Gate(List<AmenityBlock> amenityBlocks, boolean enabled, double chancePerTick, GateMode officeGateMode) {
        super(amenityBlocks, enabled);

        this.spawners = new ArrayList<>();
        if (this.getAmenityBlocks() != null) {
            for (AmenityBlock amenityBlock : this.getAmenityBlocks()) {
                if (amenityBlock instanceof GateBlock) {
                    GateBlock gateBlock = (GateBlock) amenityBlock;
                    if (gateBlock.isSpawner()) {
                        this.spawners.add(gateBlock);
                    }
                }
            }
        }

        this.chancePerTick = chancePerTick;
        this.gateMode = gateMode;
        this.gateGraphic = new GateGraphic(this);
    }




    // METHODS





    // GETTERS
    public List<GateBlock> getSpawners() {
        return spawners;
    }
    public double getChancePerTick() {
        return chancePerTick;
    }
    public GateMode getGateMode() {
        return gateMode;
    }


    // SETTERS
    public void setChancePerTick(double chancePerTick) {
        this.chancePerTick = chancePerTick;
    }

    public void setGateMode(GateMode gateMode) {
        this.gateMode = gateMode;
    }






    // OVERRIDE
    @Override
    public String toString() {
        return "Entrance/exit" + ((enabled) ? "" : " (disabled)");
    }

    @Override
    public AmenityGraphic getGraphicObject() {
        return gateGraphic;
    }

    @Override
    public AmenityGraphicLocation getGraphicLocation() {
        return gateGraphic.getGraphicLocation();
    }





    // CLASSES

    public static class GateBlock extends AmenityBlock {
        private final boolean spawner;

        public GateBlock(Patch patch, boolean attractor, boolean spawner, boolean hasGraphic) {
            super(patch, attractor, hasGraphic);
            this.spawner = spawner;
        }

        public boolean isSpawner() {
            return spawner;
        }
    }



    public enum GateMode {
        ENTRANCE("Entrance"), EXIT("Exit"), ENTRANCE_AND_EXIT("Entrance and Exit");

        private final String name;

        GateMode(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }



    public class GateFactory {
        public static Gate create(List<AmenityBlock> amenityBlocks, boolean enabled, double chancePerTick, GateMode stationGateMode) {
            return new Gate(amenityBlocks, enabled, chancePerTick, stationGateMode);
        }
    }




}