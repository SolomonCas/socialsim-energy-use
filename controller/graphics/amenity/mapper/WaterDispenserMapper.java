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

            // FIRST PATCH
            Amenity.AmenityBlock.AmenityBlockFactory amenityBlockFactory = WaterDispenser.WaterDispenserBlock.waterDispenserBlockFactory;
            Amenity.AmenityBlock amenityBlock = amenityBlockFactory.create(patch, false, true);
            amenityBlocks.add(amenityBlock);
            patch.setAmenityBlock(amenityBlock);

            // SECOND PATCH
            Patch nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol);
            Amenity.AmenityBlock nextAmenityBlock = amenityBlockFactory.create(nextPatch, false, false);
            amenityBlocks.add(nextAmenityBlock);
            nextPatch.setAmenityBlock(nextAmenityBlock);

            List<WaterDispenser> waterDispensers = Main.simulator.getEnvironment().getWaterDispensers();
            WaterDispenser waterDispenserToAdd;
            waterDispenserToAdd = WaterDispenser.WaterDispenserFactory.create(amenityBlocks, true, 10);
            waterDispensers.add(waterDispenserToAdd);
            
            // QUEUE POSITION
            List<Patch> waterDispenserQueuePatches = new ArrayList<>();
            waterDispenserQueuePatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 2, origPatchCol));
            Main.simulator.getEnvironment().getWaterDispenserQueues().add(WaterDispenserQueue.waterDispenserQueueFactory.create(waterDispenserQueuePatches, waterDispenserToAdd, "waterDispenserQueue"));
            
            amenityBlocks.forEach(ab -> ab.getPatch().getEnvironment().getAmenityPatchSet().add(ab.getPatch()));
        }
    }

}