package com.socialsim.controller.graphics.amenity.mapper;

import com.socialsim.controller.Main;
import com.socialsim.controller.graphics.amenity.AmenityMapper;
import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchfield.RefrigeratorQueue;
import com.socialsim.model.core.environment.patchobject.Amenity;
import com.socialsim.model.core.environment.patchobject.passable.goal.Refrigerator;

import java.util.ArrayList;
import java.util.List;

public class RefrigeratorMapper extends AmenityMapper {

    public static void draw(List<Patch> patches) {
        for (Patch patch : patches) {
            List<Amenity.AmenityBlock> amenityBlocks = new ArrayList<>();
            int origPatchRow = patch.getMatrixPosition().getRow();
            int origPatchCol = patch.getMatrixPosition().getColumn();

            // FIRST PATCH
            Amenity.AmenityBlock.AmenityBlockFactory amenityBlockFactory = Refrigerator.RefrigeratorBlock.refrigeratorBlockFactory;
            Amenity.AmenityBlock amenityBlock = amenityBlockFactory.create(patch, false, true);
            amenityBlocks.add(amenityBlock);
            patch.setAmenityBlock(amenityBlock);

            // SECOND PATCH
            Patch nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol);
            Amenity.AmenityBlock nextAmenityBlock = amenityBlockFactory.create(nextPatch, false, false);
            amenityBlocks.add(nextAmenityBlock);
            nextPatch.setAmenityBlock(nextAmenityBlock);

            List<Refrigerator> refrigerators = Main.simulator.getEnvironment().getRefrigerators();
            Refrigerator refrigeratorToAdd;
            refrigeratorToAdd = Refrigerator.RefrigeratorFactory.create(amenityBlocks, true, 10);
            refrigerators.add(refrigeratorToAdd);
            
            // QUEUE POSITION
            List<Patch> refrigeratorQueuePatches = new ArrayList<>();
            refrigeratorQueuePatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 2, origPatchCol));
            Main.simulator.getEnvironment().getRefrigeratorQueues().add(RefrigeratorQueue.refrigeratorQueueFactory.create(refrigeratorQueuePatches, refrigeratorToAdd, "refrigeratorQueue"));
            
            amenityBlocks.forEach(ab -> ab.getPatch().getEnvironment().getAmenityPatchSet().add(ab.getPatch()));
        }
    }

}