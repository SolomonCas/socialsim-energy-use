package com.socialsim.controller.graphics.amenity.mapper;

import com.socialsim.controller.Main;
import com.socialsim.controller.graphics.amenity.AmenityMapper;
import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchfield.FridgeQueue;
import com.socialsim.model.core.environment.patchobject.Amenity;
import com.socialsim.model.core.environment.patchobject.passable.goal.Fridge;

import java.util.ArrayList;
import java.util.List;

public class FridgeMapper extends AmenityMapper {

    public static void draw(List<Patch> patches) {
        for (Patch patch : patches) {
            List<Amenity.AmenityBlock> amenityBlocks = new ArrayList<>();
            int origPatchRow = patch.getMatrixPosition().getRow();
            int origPatchCol = patch.getMatrixPosition().getColumn();

            Amenity.AmenityBlock.AmenityBlockFactory amenityBlockFactory = Fridge.FridgeBlock.fridgeBlockFactory;
            Amenity.AmenityBlock amenityBlock = amenityBlockFactory.create(patch, false, true);
            amenityBlocks.add(amenityBlock);
            patch.setAmenityBlock(amenityBlock);

            Patch lowerPatch = Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol);
            Amenity.AmenityBlock amenityBlock2 = amenityBlockFactory.create(lowerPatch, true, false);
            amenityBlocks.add(amenityBlock2);
            lowerPatch.setAmenityBlock(amenityBlock2);

            Fridge fridgeToAdd = Fridge.FridgeFactory.create(amenityBlocks, true);
            Main.simulator.getEnvironment().getFridges().add(fridgeToAdd);
            amenityBlocks.forEach(ab -> ab.getPatch().getEnvironment().getAmenityPatchSet().add(ab.getPatch()));

            List<Patch> fridgeQueuePatches = new ArrayList<>();
            fridgeQueuePatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 2, origPatchCol));
            Main.simulator.getEnvironment().getFridgeQueues().add(FridgeQueue.fridgeQueueFactory.create(fridgeQueuePatches, fridgeToAdd, 1));
        }
    }

}