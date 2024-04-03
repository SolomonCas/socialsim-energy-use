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
    public static final GateFactory gateFactory;
    private final GateGraphic gateGraphic;

    static {
        gateFactory = new GateFactory();
    }



    // CONSTRUCTORS
    public Gate(List<AmenityBlock> amenityBlocks, boolean enabled, double chancePerTick, GateMode officeGateMode) {
        super(amenityBlocks, enabled);

        if (this.getAmenityBlocks() != null) {
            this.spawners = new ArrayList<>();

            for (AmenityBlock amenityBlock : this.getAmenityBlocks()) {
                if (amenityBlock instanceof GateBlock) {
                    GateBlock gateBlock = (GateBlock) amenityBlock;


                    if (gateBlock.isSpawner()) {
                        this.spawners.add(gateBlock);
                    }
                }
            }
        }
        else {
            this.spawners = null;
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
        public static GateBlock.GateBlockFactory gateBlockFactory;

        static {
            gateBlockFactory = new GateBlock.GateBlockFactory();
        }


        public GateBlock(Patch patch, boolean attractor, boolean spawner, boolean hasGraphic) {
            super(patch, attractor, hasGraphic);
            this.spawner = spawner;
        }

        public boolean isSpawner() {
            return spawner;
        }


        public static class GateBlockFactory extends AmenityBlockFactory {

            public GateBlock create(Patch patch, boolean attractor, boolean hasGraphic) {
                return new GateBlock(patch, attractor, true, hasGraphic);
            }


            public GateBlock create(Patch patch, boolean attractor, boolean spawner, boolean hasGraphic) {
                return null;
            }
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



    public static class GateFactory {
        public static Gate create(List<AmenityBlock> amenityBlocks, boolean enabled, double chancePerTick, GateMode stationGateMode) {
            return new Gate(amenityBlocks, enabled, chancePerTick, stationGateMode);
        }
    }




}