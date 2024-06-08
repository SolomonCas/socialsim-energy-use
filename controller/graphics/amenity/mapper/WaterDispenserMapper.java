package com.socialsim.controller.graphics.amenity.mapper;

import com.socialsim.controller.Main;
import com.socialsim.controller.graphics.amenity.AmenityMapper;
import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchfield.WaterDispenserQueue;
import com.socialsim.model.core.environment.patchobject.Amenity;
import com.socialsim.model.core.environment.patchobject.passable.goal.WaterDispenser;

import java.util.ArrayList;
import java.util.List;

public class WaterDispenserMapper extends AmenityMapper {

    public static void draw(List<Patch> patches) {
        for (Patch patch : patches) {
            List<Amenity.AmenityBlock> amenityBlocks = new ArrayList<>();
            int origPatchRow = patch.getMatrixPosition().getRow();
            int origPatchCol = patch.getMatrixPosition().getColumn();

            Amenity.AmenityBlock.AmenityBlockFactory amenityBlockFactory = WaterDispenser.WaterDispenserBlock.waterDispenserBlockFactory;
            Amenity.AmenityBlock amenityBlock = amenityBlockFactory.create(patch, false, true);
            amenityBlocks.add(amenityBlock);
            patch.setAmenityBlock(amenityBlock);

            Patch lowerPatch = Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol);
            Amenity.AmenityBlock amenityBlock2 = amenityBlockFactory.create(lowerPatch, true, false);
            amenityBlocks.add(amenityBlock2);
            lowerPatch.setAmenityBlock(amenityBlock2);

            WaterDispenser waterDispenserToAdd = WaterDispenser.WaterDispenserFactory.create(amenityBlocks, true);
            Main.simulator.getEnvironment().getWaterDispensers().add(waterDispenserToAdd);
            amenityBlocks.forEach(ab -> ab.getPatch().getEnvironment().getAmenityPatchSet().add(ab.getPatch()));

            List<Patch> waterDispenserQueuePatches = new ArrayList<>();
            waterDispenserQueuePatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 2, origPatchCol));
            Main.simulator.getEnvironment().getWaterDispenserQueues().add(WaterDispenserQueue.waterDispenserQueueFactory.create(waterDispenserQueuePatches, waterDispenserToAdd, 1));
        }
    }

}