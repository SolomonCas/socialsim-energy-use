package com.socialsim.model.core.environment.patchobject.passable.elevator;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.controller.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.controller.graphics.amenity.graphic.ElevatorGraphic;
import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchobject.Drawable;
import com.socialsim.model.core.environment.patchobject.passable.NonObstacle;

import java.util.ArrayList;
import java.util.List;

public class Elevator extends NonObstacle implements Drawable {

    // VARIABLES
    private final List<ElevatorBlock> spawners;
    private double chancePerTick;
    private ElevatorMode elevatorMode;
    public static final ElevatorFactory elevatorFactory;
    private final ElevatorGraphic elevatorGraphic;

    static {
        elevatorFactory = new ElevatorFactory();
    }



    // CONSTRUCTORS
    public Elevator(List<AmenityBlock> amenityBlocks, boolean enabled, double chancePerTick, ElevatorMode officeElevatorMode, String orientation) {
        super(amenityBlocks, enabled);

        if (this.getAmenityBlocks() != null) {
            this.spawners = new ArrayList<>();

            for (AmenityBlock amenityBlock : this.getAmenityBlocks()) {
                if (amenityBlock instanceof ElevatorBlock) {
                    ElevatorBlock elevatorBlock = (ElevatorBlock) amenityBlock;


                    if (elevatorBlock.isSpawner()) {
                        this.spawners.add(elevatorBlock);
                    }
                }
            }
        }
        else {
            this.spawners = null;
        }

        this.chancePerTick = chancePerTick;
        this.elevatorMode = elevatorMode;
        this.elevatorGraphic = new ElevatorGraphic(this, orientation);
    }




    // METHODS





    // GETTERS
    public List<ElevatorBlock> getSpawners() {
        return spawners;
    }
    public double getChancePerTick() {
        return chancePerTick;
    }
    public ElevatorMode getElevatorMode() {
        return elevatorMode;
    }


    // SETTERS
    public void setChancePerTick(double chancePerTick) {
        this.chancePerTick = chancePerTick;
    }

    public void setElevatorMode(ElevatorMode elevatorMode) {
        this.elevatorMode = elevatorMode;
    }






    // OVERRIDE
    @Override
    public String toString() {
        return "Entrance/exit" + ((enabled) ? "" : " (disabled)");
    }

    @Override
    public AmenityGraphic getGraphicObject() {
        return elevatorGraphic;
    }

    @Override
    public AmenityGraphicLocation getGraphicLocation() {
        return elevatorGraphic.getGraphicLocation();
    }





    // CLASSES

    public static class ElevatorBlock extends AmenityBlock {
        private final boolean spawner;
        public static ElevatorBlock.ElevatorBlockFactory elevatorBlockFactory;

        static {
            elevatorBlockFactory = new ElevatorBlock.ElevatorBlockFactory();
        }


        public ElevatorBlock(Patch patch, boolean attractor, boolean spawner, boolean hasGraphic) {
            super(patch, attractor, hasGraphic);
            this.spawner = spawner;
        }

        public boolean isSpawner() {
            return spawner;
        }


        public static class ElevatorBlockFactory extends AmenityBlockFactory {

            public ElevatorBlock create(Patch patch, boolean attractor, boolean hasGraphic) {
                return new ElevatorBlock(patch, attractor, true, hasGraphic);
            }


            public ElevatorBlock create(Patch patch, boolean attractor, boolean spawner, boolean hasGraphic) {
                return null;
            }
        }
    }





    public enum ElevatorMode {
        ENTRANCE("Entrance"), EXIT("Exit"), ENTRANCE_AND_EXIT("Entrance and Exit");

        private final String name;

        ElevatorMode(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }



    public static class ElevatorFactory {
        public static Elevator create(List<AmenityBlock> amenityBlocks, boolean enabled, double chancePerTick, ElevatorMode stationElevatorMode, String orientation) {
            return new Elevator(amenityBlocks, enabled, chancePerTick, stationElevatorMode, orientation);
        }
    }




}