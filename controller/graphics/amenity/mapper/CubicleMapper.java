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

    public static void draw(List<Patch> patches, String type, String facing, String tableOn, boolean withAppliance) {
        for (Patch patch : patches) {
            List<Amenity.AmenityBlock> amenityBlocks = new ArrayList<>();
            int origPatchRow = patch.getMatrixPosition().getRow();
            int origPatchCol = patch.getMatrixPosition().getColumn();

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
                    if (tableOn.equals("NORTH_AND_EAST") || tableOn.equals("SOUTH_AND_WEST")) {
                        Patch seat1 = Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol + 1);
                        nextAmenityBlock = amenityBlockFactory.create(seat1, true, false);
                        amenityBlocks.add(nextAmenityBlock);
                        seat1.setAmenityBlock(nextAmenityBlock);

                        Patch seat2 = Main.simulator.getEnvironment().getPatch(origPatchRow + 3, origPatchCol + 2);
                        nextAmenityBlock = amenityBlockFactory.create(seat1, true, false);
                        amenityBlocks.add(nextAmenityBlock);
                        seat2.setAmenityBlock(nextAmenityBlock);
                    }
                    else if (tableOn.equals("NORTH_AND_WEST") || tableOn.equals("SOUTH_AND_EAST")) {
                        Patch seat1 = Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol + 2);
                        nextAmenityBlock = amenityBlockFactory.create(seat1, true, false);
                        amenityBlocks.add(nextAmenityBlock);
                        seat1.setAmenityBlock(nextAmenityBlock);

                        Patch seat2 = Main.simulator.getEnvironment().getPatch(origPatchRow + 3, origPatchCol + 1);
                        nextAmenityBlock = amenityBlockFactory.create(seat1, true, false);
                        amenityBlocks.add(nextAmenityBlock);
                        seat2.setAmenityBlock(nextAmenityBlock);
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

                    // Set chairs as attractors
                    nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol + 1);

                    nextAmenityBlock = amenityBlockFactory.create(nextPatch, true, false);

                    amenityBlocks.add(nextAmenityBlock);
                    nextPatch.setAmenityBlock(nextAmenityBlock);

                    nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol + 3);

                    nextAmenityBlock = amenityBlockFactory.create(nextPatch, true, false);

                    amenityBlocks.add(nextAmenityBlock);
                    nextPatch.setAmenityBlock(nextAmenityBlock);

                    nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow + 6, origPatchCol + 1);

                    nextAmenityBlock = amenityBlockFactory.create(nextPatch, true, false);

                    amenityBlocks.add(nextAmenityBlock);
                    nextPatch.setAmenityBlock(nextAmenityBlock);

                    nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow + 6, origPatchCol + 3);

                    nextAmenityBlock = amenityBlockFactory.create(nextPatch, true, false);

                    amenityBlocks.add(nextAmenityBlock);
                    nextPatch.setAmenityBlock(nextAmenityBlock);

                }
                case "TYPE_B" -> {
                    Patch nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow + 3, origPatchCol);

                    nextAmenityBlock = amenityBlockFactory.create(nextPatch, false, false);

                    amenityBlocks.add(nextAmenityBlock);
                    nextPatch.setAmenityBlock(nextAmenityBlock);
                    for (int i = 1; i <= 2; i++) {
                        nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow + i, origPatchCol + 2);

                        nextAmenityBlock = amenityBlockFactory.create(nextPatch, false, false);

                        amenityBlocks.add(nextAmenityBlock);
                        nextPatch.setAmenityBlock(nextAmenityBlock);

                        nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow + i, origPatchCol + 3);

                        nextAmenityBlock = amenityBlockFactory.create(nextPatch, false, false);

                        amenityBlocks.add(nextAmenityBlock);
                        nextPatch.setAmenityBlock(nextAmenityBlock);
                    }

                    for (int j = 1; j <= 5; j++) {
                        nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow, origPatchCol + j);

                        nextAmenityBlock = amenityBlockFactory.create(nextPatch, false, false);

                        amenityBlocks.add(nextAmenityBlock);
                        nextPatch.setAmenityBlock(nextAmenityBlock);

                        nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow + 3, origPatchCol + j);

                        nextAmenityBlock = amenityBlockFactory.create(nextPatch, false, false);

                        amenityBlocks.add(nextAmenityBlock);
                        nextPatch.setAmenityBlock(nextAmenityBlock);
                    }

                    // Set chairs as attractors
                    nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol);

                    nextAmenityBlock = amenityBlockFactory.create(nextPatch, true, false);

                    amenityBlocks.add(nextAmenityBlock);
                    nextPatch.setAmenityBlock(nextAmenityBlock);

                    nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow + 2, origPatchCol + 1);

                    nextAmenityBlock = amenityBlockFactory.create(nextPatch, true, false);

                    amenityBlocks.add(nextAmenityBlock);
                    nextPatch.setAmenityBlock(nextAmenityBlock);

                    nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow + 2, origPatchCol + 4);

                    nextAmenityBlock = amenityBlockFactory.create(nextPatch, true, false);

                    amenityBlocks.add(nextAmenityBlock);
                    nextPatch.setAmenityBlock(nextAmenityBlock);

                    nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol + 5);

                    nextAmenityBlock = amenityBlockFactory.create(nextPatch, true, false);

                    amenityBlocks.add(nextAmenityBlock);
                    nextPatch.setAmenityBlock(nextAmenityBlock);
                }
                case "TYPE_C" -> {
                    if (facing.equals("NORTH") || facing.equals("SOUTH")) {
                        for (int i = 0; i <= 1; i++) {
                            for (int j = 0; j <= 2; j++) {
                                if (i == 0) {
                                    j++;
                                }

                                Patch nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow + i, origPatchCol + j);

                                if ((facing.equals("NORTH") && (i == 0 && j == 1)) ||
                                        (facing.equals("SOUTH") && (i == 1 && j == 1))) {
                                    nextAmenityBlock = amenityBlockFactory.create(nextPatch, true, false);
                                } else {
                                    nextAmenityBlock = amenityBlockFactory.create(nextPatch, false, false);
                                }


                                amenityBlocks.add(nextAmenityBlock);
                                nextPatch.setAmenityBlock(nextAmenityBlock);
                            }
                        }
                    }
                    else if (facing.equals("WEST") || facing.equals("EAST")) {
                        for (int i = 0; i <= 2; i++) {
                            for (int j = 0; j <= 1; j++) {
                                if (i == 0) {
                                    j++;
                                }

                                Patch nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow + i, origPatchCol + j);

                                if ((facing.equals("WEST") && (i == 1 && j == 1)) ||
                                        (facing.equals("EAST") && (i == 1 && j == 0))) {
                                    nextAmenityBlock = amenityBlockFactory.create(nextPatch, true, false);
                                } else {
                                    nextAmenityBlock = amenityBlockFactory.create(nextPatch, false, false);
                                }


                                amenityBlocks.add(nextAmenityBlock);
                                nextPatch.setAmenityBlock(nextAmenityBlock);
                            }
                        }
                    }
                }
            }
            if (type.equals("MESA")) {
                MESATable mesaTableToAdd = MESATable.MESATableFactory.create(amenityBlocks, true, type, facing, tableOn, withAppliance);
                Main.simulator.getEnvironment().getMesaTables().add(mesaTableToAdd);
            }
            else {
                Cubicle cubicleToAdd = Cubicle.CubicleFactory.create(amenityBlocks, true, type, facing, tableOn, withAppliance);
                Main.simulator.getEnvironment().getCubicles().add(cubicleToAdd);
            };
            amenityBlocks.forEach(ab -> ab.getPatch().getEnvironment().getAmenityPatchSet().add(ab.getPatch()));
        }
    }

}