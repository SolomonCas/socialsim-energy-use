package com.socialsim.controller.graphics.amenity.mapper;

import com.socialsim.controller.Main;
import com.socialsim.controller.graphics.amenity.AmenityMapper;
import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchobject.Amenity;
import com.socialsim.model.core.environment.patchobject.passable.goal.WindowBlinds;

import java.util.ArrayList;
import java.util.List;

public class WindowBlindsMapper extends AmenityMapper {


    public static void draw(List<Patch> patches, String state) {
        for (Patch patch : patches) {
            List<Amenity.AmenityBlock> amenityBlocks = new ArrayList<>();
            int origPatchRow = patch.getMatrixPosition().getRow();
            int origPatchCol = patch.getMatrixPosition().getColumn();
            WindowBlinds windowBlindsToAdd;
            List<WindowBlinds> windowBlinds = Main.simulator.getEnvironment().getWindowBlinds();
            int index = 0;

            // FIRST PATCH
            Amenity.AmenityBlock.AmenityBlockFactory amenityBlockFactory = WindowBlinds.WindowBlindsBlock.windowBlindsBlockFactory;
            Amenity.AmenityBlock amenityBlock = amenityBlockFactory.create(patch, true, true);
            amenityBlocks.add(amenityBlock);
            patch.setAmenityBlock(amenityBlock);


            if (state.equals("GLASS")) {
                // THE REST OF THE PATCHES
                Patch nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol);
                Amenity.AmenityBlock nextAmenityBlock = amenityBlockFactory.create(nextPatch, false, false);
                amenityBlocks.add(nextAmenityBlock);
                nextPatch.setAmenityBlock(nextAmenityBlock);
            }
            else if (   state.equals("OPENED_SOUTH_FROM_INSIDE") ||
                        state.equals("OPENED_SOUTH_FROM_OUTSIDE") ||
                        state.equals("CLOSED_SOUTH_FROM_INSIDE") ||
                        state.equals("CLOSED_SOUTH_FROM_OUTSIDE")) {

                for (int i = 1; i <= 2; i++){
                    Patch nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow + i, origPatchCol);
                    Amenity.AmenityBlock nextAmenityBlock = amenityBlockFactory.create(nextPatch, false, false);
                    amenityBlocks.add(nextAmenityBlock);
                    nextPatch.setAmenityBlock(nextAmenityBlock);
                }
            }

            windowBlindsToAdd = WindowBlinds.WindowBlindsFactory.create(amenityBlocks, true, state);
            windowBlinds.add(windowBlindsToAdd);
//                index = windowBlinds.indexOf(windowBlindsToAdd);

            amenityBlocks.forEach(ab -> ab.getPatch().getEnvironment().getAmenityPatchSet().add(ab.getPatch()));
        }
    }
}