package com.socialsim.controller.graphics.amenity.mapper;


import com.socialsim.controller.Main;
import com.socialsim.controller.graphics.amenity.AmenityMapper;
import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchobject.Amenity;
import com.socialsim.model.core.environment.patchobject.passable.goal.Cubicle;
import com.socialsim.model.core.environment.patchobject.passable.goal.MESATable;
import com.socialsim.model.core.environment.patchobject.passable.goal.PantryTable;

import java.util.ArrayList;
import java.util.List;

public class CubicleMapper extends AmenityMapper {

    /***** METHOD *****/

    public static void draw(List<Patch> patches, String type, String facing, String tableOn, boolean withAppliance, int numberOfMonitor) {
        for (Patch patch : patches) {
            List<Amenity.AmenityBlock> amenityBlocks = new ArrayList<>();
            int origPatchRow = patch.getMatrixPosition().getRow();
            int origPatchCol = patch.getMatrixPosition().getColumn();
            int index = 0;
            String belongTo = "";

            List<Patch> chairWestPatches = new ArrayList<>();
            List<Patch> chairEastPatches = new ArrayList<>();
            List<Patch> chairNorthPatches = new ArrayList<>();
            List<Patch> chairSouthPatches = new ArrayList<>();

            List<Patch> monitorWestPatches = new ArrayList<>();
            List<Patch> monitorEastPatches = new ArrayList<>();
            List<Patch> monitorNorthPatches = new ArrayList<>();
            List<Patch> monitorSouthPatches = new ArrayList<>();

            // CUBICLE'S FIRST PATCH (UPPER LEFT CORNER)
            Amenity.AmenityBlock.AmenityBlockFactory amenityBlockFactory = Cubicle.CubicleBlock.cubicleBlockFactory;
            Amenity.AmenityBlock amenityBlock = amenityBlockFactory.create(patch, false, true);
            amenityBlocks.add(amenityBlock);
            patch.setAmenityBlock(amenityBlock);

            Amenity.AmenityBlock nextAmenityBlock;

            switch (type) {
                case ("MESA") -> {
                    for (int i = 1; i <= 4; i++) {
                        if (tableOn.equals("NORTH_AND_WEST") || tableOn.equals("SOUTH_AND_WEST")) {
                            Patch nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow + i, origPatchCol);
                            nextAmenityBlock = amenityBlockFactory.create(nextPatch, false, false);
                            amenityBlocks.add(nextAmenityBlock);
                            nextPatch.setAmenityBlock(nextAmenityBlock);
                        }
                        else if (tableOn.equals("SOUTH_AND_EAST") || tableOn.equals("NORTH_AND_EAST")) {
                            Patch nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow + i, origPatchCol + 3);
                            nextAmenityBlock = amenityBlockFactory.create(nextPatch, false, false);
                            amenityBlocks.add(nextAmenityBlock);
                            nextPatch.setAmenityBlock(nextAmenityBlock);
                        }

                    }
                    for (int j = 1; j <= 3; j++) {
                        if (tableOn.equals("SOUTH_AND_WEST") || tableOn.equals("SOUTH_AND_EAST")) {
                            Patch nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow + 4, origPatchCol + j);
                            nextAmenityBlock = amenityBlockFactory.create(nextPatch, false, false);
                            amenityBlocks.add(nextAmenityBlock);
                            nextPatch.setAmenityBlock(nextAmenityBlock);
                        }
                        else if (tableOn.equals("NORTH_AND_WEST") || tableOn.equals("NORTH_AND_EAST")) {
                            Patch nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow, origPatchCol + j);
                            nextAmenityBlock = amenityBlockFactory.create(nextPatch, false, false);
                            amenityBlocks.add(nextAmenityBlock);
                            nextPatch.setAmenityBlock(nextAmenityBlock);
                        }
                    }

                    // Set chairs as attractors
                    switch (tableOn) {
                        case "NORTH_AND_EAST" -> {
                            chairNorthPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol + 1));
                            chairEastPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 3, origPatchCol + 2));
                        }
                        case "SOUTH_AND_WEST" -> {
                            chairWestPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol + 1));
                            chairSouthPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 3, origPatchCol + 2));
                        }
                        case "NORTH_AND_WEST" -> {
                            chairNorthPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol + 2));
                            chairWestPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 3, origPatchCol + 1));
                        }
                        case "SOUTH_AND_EAST" -> {
                            chairEastPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol + 2));
                            chairSouthPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 3, origPatchCol + 1));
                        }
                    }
                }
                case "TYPE_A" -> {
                    Patch nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow, origPatchCol + 4);

                    nextAmenityBlock = amenityBlockFactory.create(nextPatch, false, false);

                    amenityBlocks.add(nextAmenityBlock);
                    nextPatch.setAmenityBlock(nextAmenityBlock);
                    for (int i = 1; i <= 7; i++) {
                        nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow + i, origPatchCol);

                        nextAmenityBlock = amenityBlockFactory.create(nextPatch, false, false);

                        amenityBlocks.add(nextAmenityBlock);
                        nextPatch.setAmenityBlock(nextAmenityBlock);

                        nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow + i, origPatchCol + 4);

                        nextAmenityBlock = amenityBlockFactory.create(nextPatch, false, false);

                        amenityBlocks.add(nextAmenityBlock);
                        nextPatch.setAmenityBlock(nextAmenityBlock);
                    }

                    for (int j = 1; j <= 3; j++) {
                        nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow + 3, origPatchCol + j);

                        nextAmenityBlock = amenityBlockFactory.create(nextPatch, false, false);

                        amenityBlocks.add(nextAmenityBlock);
                        nextPatch.setAmenityBlock(nextAmenityBlock);

                        nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow + 4, origPatchCol + j);

                        nextAmenityBlock = amenityBlockFactory.create(nextPatch, false, false);

                        amenityBlocks.add(nextAmenityBlock);
                        nextPatch.setAmenityBlock(nextAmenityBlock);
                    }

                    // Set chairs
                    chairWestPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol + 1));
                    chairWestPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 6, origPatchCol + 1));
                    chairEastPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol + 3));
                    chairEastPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 6, origPatchCol + 3));

                    // Set Monitors
                    monitorWestPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol + 4));
                }
                case "TYPE_B" -> {
                    if (facing.equals("WEST")) {
                        Patch nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow + 3, origPatchCol);

                        nextAmenityBlock = amenityBlockFactory.create(nextPatch, false, false);

                        amenityBlocks.add(nextAmenityBlock);
                        nextPatch.setAmenityBlock(nextAmenityBlock);
                        for (int i = 1; i <= 2; i++) {
                            nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow + i, origPatchCol + 2);

                            nextAmenityBlock = amenityBlockFactory.create(nextPatch, false, false);

                            amenityBlocks.add(nextAmenityBlock);
                            nextPatch.setAmenityBlock(nextAmenityBlock);
                        }

                        for (int j = 1; j <= 2; j++) {
                            nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow, origPatchCol + j);

                            nextAmenityBlock = amenityBlockFactory.create(nextPatch, false, false);

                            amenityBlocks.add(nextAmenityBlock);
                            nextPatch.setAmenityBlock(nextAmenityBlock);

                            nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow + 3, origPatchCol + j);

                            nextAmenityBlock = amenityBlockFactory.create(nextPatch, false, false);

                            amenityBlocks.add(nextAmenityBlock);
                            nextPatch.setAmenityBlock(nextAmenityBlock);
                        }
                        // Set chairs
                        chairNorthPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol + 1));
                        chairSouthPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 2, origPatchCol + 1));

                        // Set Monitors
                        monitorSouthPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow, origPatchCol + 1));
                        monitorNorthPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 3, origPatchCol + 1));
                    }
                    else if (facing.equals("EAST")) {
                        Patch nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow + 3, origPatchCol);

                        nextAmenityBlock = amenityBlockFactory.create(nextPatch, false, false);

                        amenityBlocks.add(nextAmenityBlock);
                        nextPatch.setAmenityBlock(nextAmenityBlock);
                        for (int i = 1; i <= 2; i++) {
                            nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow + i, origPatchCol);

                            nextAmenityBlock = amenityBlockFactory.create(nextPatch, false, false);

                            amenityBlocks.add(nextAmenityBlock);
                            nextPatch.setAmenityBlock(nextAmenityBlock);
                        }

                        for (int j = 1; j <= 2; j++) {
                            nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow, origPatchCol + j);

                            nextAmenityBlock = amenityBlockFactory.create(nextPatch, false, false);

                            amenityBlocks.add(nextAmenityBlock);
                            nextPatch.setAmenityBlock(nextAmenityBlock);

                            nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow + 3, origPatchCol + j);

                            nextAmenityBlock = amenityBlockFactory.create(nextPatch, false, false);

                            amenityBlocks.add(nextAmenityBlock);
                            nextPatch.setAmenityBlock(nextAmenityBlock);
                        }


                        // Set chairs
                        chairNorthPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol + 1));
                        chairSouthPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 2, origPatchCol + 1));

                        // Set Monitors
                        monitorSouthPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow, origPatchCol + 1));
                        monitorNorthPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 3, origPatchCol + 1));
                    }
                }
                case "TYPE_C" -> {
                    if (facing.equals("NORTH") || facing.equals("SOUTH")) {
                        Patch nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol);
                        nextAmenityBlock = amenityBlockFactory.create(nextPatch, false, false);
                        amenityBlocks.add(nextAmenityBlock);
                        nextPatch.setAmenityBlock(nextAmenityBlock);
                        for (int i = 0; i <= 1; i++) {
                            nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow + i, origPatchCol + 2);
                            nextAmenityBlock = amenityBlockFactory.create(nextPatch, false, false);
                            amenityBlocks.add(nextAmenityBlock);
                            nextPatch.setAmenityBlock(nextAmenityBlock);
                        }

                        if (facing.equals("NORTH")) {
                            nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol + 1);
                            nextAmenityBlock = amenityBlockFactory.create(nextPatch, false, false);
                            amenityBlocks.add(nextAmenityBlock);
                            nextPatch.setAmenityBlock(nextAmenityBlock);

                            // Set Chair
                            chairSouthPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow, origPatchCol + 1));

                            // Set Monitors
                            monitorNorthPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol + 1));
                        }
                        else if (facing.equals("SOUTH")) {
                            nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow, origPatchCol + 1);
                            nextAmenityBlock = amenityBlockFactory.create(nextPatch, false, false);
                            amenityBlocks.add(nextAmenityBlock);
                            nextPatch.setAmenityBlock(nextAmenityBlock);

                            // Set Chair
                            chairNorthPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol + 1));

                            // Set Monitors
                            monitorSouthPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow, origPatchCol + 1));
                        }
                    }
                    else if (facing.equals("WEST") || facing.equals("EAST")) {
                        Patch nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow, origPatchCol + 1);
                        nextAmenityBlock = amenityBlockFactory.create(nextPatch, false, false);
                        amenityBlocks.add(nextAmenityBlock);
                        nextPatch.setAmenityBlock(nextAmenityBlock);
                        for (int j = 0; j <= 1; j++) {
                            nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow + 2, origPatchCol + j);
                            nextAmenityBlock = amenityBlockFactory.create(nextPatch, false, false);
                            amenityBlocks.add(nextAmenityBlock);
                            nextPatch.setAmenityBlock(nextAmenityBlock);
                        }

                        if (facing.equals("WEST")) {
                            nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol + 1);
                            nextAmenityBlock = amenityBlockFactory.create(nextPatch, false, false);
                            amenityBlocks.add(nextAmenityBlock);
                            nextPatch.setAmenityBlock(nextAmenityBlock);

                            // Set Chair
                            chairEastPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol));

                            // Set Monitors
                            monitorWestPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol + 1));
                        }
                        else if (facing.equals("EAST")) {
                            nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol);
                            nextAmenityBlock = amenityBlockFactory.create(nextPatch, false, false);
                            amenityBlocks.add(nextAmenityBlock);
                            nextPatch.setAmenityBlock(nextAmenityBlock);

                            // Set Chair
                            chairWestPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol + 1));

                            // Set Monitors
                            monitorEastPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol));
                        }
                    }
                }
            }
            if (type.equals("MESA")) {
                MESATable mesaTableToAdd = MESATable.MESATableFactory.create(amenityBlocks, true, type, facing, tableOn);
                Main.simulator.getEnvironment().getMesaTables().add(mesaTableToAdd);
                index =  Main.simulator.getEnvironment().getMesaTables().indexOf(mesaTableToAdd);
                belongTo = "MESATable";
            }
            else {
                Cubicle cubicleToAdd = Cubicle.CubicleFactory.create(amenityBlocks, true, type, facing, tableOn);
                Main.simulator.getEnvironment().getCubicles().add(cubicleToAdd);
                index =  Main.simulator.getEnvironment().getCubicles().indexOf(cubicleToAdd);
                belongTo = "Cubicle";

            };
            if (!chairWestPatches.isEmpty())
                ChairMapper.draw(chairWestPatches, index, "WEST", "OFFICE", belongTo);
            if (!chairSouthPatches.isEmpty())
                ChairMapper.draw(chairSouthPatches, index, "SOUTH", "OFFICE", belongTo);
            if (!chairEastPatches.isEmpty())
                ChairMapper.draw(chairEastPatches, index, "EAST", "OFFICE", belongTo);
            if (!chairNorthPatches.isEmpty())
                ChairMapper.draw(chairNorthPatches, index, "NORTH", "OFFICE", belongTo);

            if (!monitorWestPatches.isEmpty())
                MonitorMapper.draw(monitorWestPatches, index, "WEST", belongTo);
            if (!monitorSouthPatches.isEmpty())
                MonitorMapper.draw(monitorSouthPatches, index, "SOUTH", belongTo);
            if (!monitorEastPatches.isEmpty())
                MonitorMapper.draw(monitorEastPatches, index, "EAST", belongTo);
            if (!monitorNorthPatches.isEmpty())
                MonitorMapper.draw(monitorNorthPatches, index, "NORTH", belongTo);

            amenityBlocks.forEach(ab -> ab.getPatch().getEnvironment().getAmenityPatchSet().add(ab.getPatch()));
        }
    }

}