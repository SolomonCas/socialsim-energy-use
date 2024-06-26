package com.socialsim.controller.graphics.amenity.mapper;


import com.socialsim.controller.Main;
import com.socialsim.controller.graphics.amenity.AmenityMapper;
import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchobject.Amenity;
import com.socialsim.model.core.environment.patchobject.passable.goal.Cubicle;

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
                    for (int i = 0; i <= 4; i++) {
                        for (int j = 0; j <= 3; j++) {
                            if (i == 0) {
                                j++;
                            }

                            Patch nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow + i, origPatchCol + j);

                            if (((tableOn.equals("NORTH_AND_WEST") || tableOn.equals("SOUTH_AND_EAST")) && ((i == 1 && j == 2) || (i == 3 && j == 1))) ||
                                    ((tableOn.equals("NORTH_AND_EAST") || tableOn.equals("SOUTH_AND_WEST")) && ((i == 1 && j == 1) || (i == 3 && j == 2)))) {
                                nextAmenityBlock = amenityBlockFactory.create(nextPatch, true, false);
                            } else {
                                nextAmenityBlock = amenityBlockFactory.create(nextPatch, false, false);
                            }

                            amenityBlocks.add(nextAmenityBlock);
                            nextPatch.setAmenityBlock(nextAmenityBlock);
                        }
                    }
                }
                case "TYPE_A" -> {
                    for (int i = 0; i <= 7; i++) {
                        for (int j = 0; j <= 4; j++) {
                            if (i == 0) {
                                j++;
                            }

                            Patch nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow + i, origPatchCol + j);

                            if ((i == 1 && (j == 1 || j == 3)) || (i == 6 && (j == 1 || j == 3))) {
                                nextAmenityBlock = amenityBlockFactory.create(nextPatch, true, false);
                            } else {
                                nextAmenityBlock = amenityBlockFactory.create(nextPatch, false, false);
                            }

                            amenityBlocks.add(nextAmenityBlock);
                            nextPatch.setAmenityBlock(nextAmenityBlock);
                        }
                    }
                }
                case "TYPE_B" -> {
                    for (int i = 0; i <= 3; i++) {
                        for (int j = 0; j <= 2; j++) {
                            if (i == 0) {
                                j++;
                            }

                            Patch nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow + i, origPatchCol + j);

                            if ((i == 2 && j == 1) ||
                                    (facing.equals("WEST") && (i == 1 && j == 0)) ||
                                    (facing.equals("EAST") && (i == 1 && j == 2))) {
                                nextAmenityBlock = amenityBlockFactory.create(nextPatch, true, false);
                            } else {
                                nextAmenityBlock = amenityBlockFactory.create(nextPatch, false, false);
                            }

                            amenityBlocks.add(nextAmenityBlock);
                            nextPatch.setAmenityBlock(nextAmenityBlock);
                        }
                    }
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
                    } else if (facing.equals("WEST") || facing.equals("EAST")) {
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

            Cubicle cubicleToAdd = Cubicle.CubicleFactory.create(amenityBlocks, true, type, facing, tableOn, withAppliance);
            Main.simulator.getEnvironment().getCubicles().add(cubicleToAdd);
            amenityBlocks.forEach(ab -> ab.getPatch().getEnvironment().getAmenityPatchSet().add(ab.getPatch()));
        }
    }

}