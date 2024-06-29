package com.socialsim.controller.graphics.amenity.mapper;

import com.socialsim.controller.Main;
import com.socialsim.controller.graphics.amenity.AmenityMapper;
import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchobject.Amenity;
import com.socialsim.model.core.environment.patchobject.passable.goal.HumanExpTable;
import com.socialsim.model.core.environment.patchobject.passable.goal.Light;

import java.util.ArrayList;
import java.util.List;

public class LightMapper extends AmenityMapper {


    public static void draw(List<Patch> patches, String type, String orientation) {
        for (Patch patch : patches) {
            List<Amenity.AmenityBlock> amenityBlocks = new ArrayList<>();
            int origPatchRow = patch.getMatrixPosition().getRow();
            int origPatchCol = patch.getMatrixPosition().getColumn();

            // FIRST PATCH
            Amenity.AmenityBlock.AmenityBlockFactory amenityBlockFactory = Light.LightBlock.lightBlockFactory;
            Amenity.AmenityBlock amenityBlock = amenityBlockFactory.create(patch, true, true);
            amenityBlocks.add(amenityBlock);
            patch.setAmenityBlock(amenityBlock);

            List<Light> lights = Main.simulator.getEnvironment().getLights();
            Light lightToAdd;
            Patch nextPatch;
            Amenity.AmenityBlock nextAmenityBlock;


                if (type.equals("LINEAR_PENDANT_LIGHT") || type.equals("RECESSED_LINEAR_LIGHT")) {
                    switch (orientation) {
                        case "HORIZONTAL":
                            nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow, origPatchCol + 1);
                            nextAmenityBlock = amenityBlockFactory.create(nextPatch, true, false);
                            amenityBlocks.add(nextAmenityBlock);
                            nextPatch.setAmenityBlock(nextAmenityBlock);
                            break;
                        case "VERTICAL":
                            nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol);
                            nextAmenityBlock = amenityBlockFactory.create(nextPatch, true, false);
                            amenityBlocks.add(nextAmenityBlock);
                            nextPatch.setAmenityBlock(nextAmenityBlock);
                            break;
                        default:
                            throw new IllegalArgumentException("Unknown orientation: " + orientation);
                    }
                }

                 else if (type.equals("TRACK_LIGHT")) {
                    switch (orientation) {
                        case "HORIZONTAL":
                            // THE REST OF THE PATCHES
                            for (int j = 1; j <= 3; j++) {
                                nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow, origPatchCol + j);
                                nextAmenityBlock = amenityBlockFactory.create(nextPatch, true, false);
                                amenityBlocks.add(nextAmenityBlock);
                                nextPatch.setAmenityBlock(nextAmenityBlock);
                            }
                            break;
                        case "VERTICAL":
                            // THE REST OF THE PATCHES
                            for (int i = 1; i <= 3; i++) {
                                nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow + i, origPatchCol);
                                nextAmenityBlock = amenityBlockFactory.create(nextPatch, true, false);
                                amenityBlocks.add(nextAmenityBlock);
                                nextPatch.setAmenityBlock(nextAmenityBlock);
                            }
                            break;
                        default:
                            throw new IllegalArgumentException("Unknown orientation: " + orientation);
                    }
                }

            lightToAdd = Light.LightFactory.create(amenityBlocks, true, type, orientation);
            lights.add(lightToAdd);
            amenityBlocks.forEach(ab -> ab.getPatch().getEnvironment().getAmenityPatchSet().add(ab.getPatch()));
        }
    }
}