package com.socialsim.controller.graphics.amenity.mapper;

import com.socialsim.controller.Main;
import com.socialsim.controller.graphics.amenity.AmenityMapper;
import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchfield.ReceptionQueue;
import com.socialsim.model.core.environment.patchobject.Amenity;
import com.socialsim.model.core.environment.patchobject.passable.goal.ReceptionChair;
import com.socialsim.model.core.environment.patchobject.passable.goal.ReceptionTable;

import java.util.ArrayList;
import java.util.List;

public class ReceptionTableMapper extends AmenityMapper {

    public static void draw(List<Patch> patches) {
        for (Patch patch : patches) {
            List<Amenity.AmenityBlock> amenityBlocks = new ArrayList<>();
            int origPatchRow = patch.getMatrixPosition().getRow();
            int origPatchCol = patch.getMatrixPosition().getColumn();

            Amenity.AmenityBlock.AmenityBlockFactory amenityBlockFactory = ReceptionTable.ReceptionTableBlock.receptionTableBlockFactory;
            Amenity.AmenityBlock amenityBlock = amenityBlockFactory.create(patch, true, true);
            amenityBlocks.add(amenityBlock);
            patch.setAmenityBlock(amenityBlock);

            Patch patch2 = Main.simulator.getEnvironment().getPatch(origPatchRow, origPatchCol + 1);
            Amenity.AmenityBlock amenityBlock2 = amenityBlockFactory.create(patch2, true, false);
            amenityBlocks.add(amenityBlock2);
            patch2.setAmenityBlock(amenityBlock2);

            Patch patch3 = Main.simulator.getEnvironment().getPatch(origPatchRow, origPatchCol + 2);
            Amenity.AmenityBlock amenityBlock3 = amenityBlockFactory.create(patch3, true, true);
            amenityBlocks.add(amenityBlock3);
            patch3.setAmenityBlock(amenityBlock3);

            Patch patch4 = Main.simulator.getEnvironment().getPatch(origPatchRow, origPatchCol + 3);
            Amenity.AmenityBlock amenityBlock4 = amenityBlockFactory.create(patch4, true, false);
            amenityBlocks.add(amenityBlock4);
            patch4.setAmenityBlock(amenityBlock4);

            ReceptionTable receptionTableToAdd = ReceptionTable.ReceptionTableFactory.create(amenityBlocks, true, 10);
            Main.simulator.getEnvironment().getReceptionTables().add(receptionTableToAdd);
            amenityBlocks.forEach(ab -> ab.getPatch().getEnvironment().getAmenityPatchSet().add(ab.getPatch()));

            // Setting the chairs
            int index = Main.simulator.getEnvironment().getReceptionTables().indexOf(receptionTableToAdd);
            List<Patch> receptionChairPatches = new ArrayList<>();
            receptionChairPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol + 3));
            ReceptionChairMapper.draw(receptionChairPatches, index);


            // This lines of code is responsible for adding a queue/line for agents to wait their turn.
            // Developer Note: My plan is to make a queue like above the reception table.
            List<Patch> receptionQueuePatches = new ArrayList<>();
            receptionQueuePatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow - 1, origPatchCol + 4));
            Main.simulator.getEnvironment().getReceptionQueues().add(ReceptionQueue.receptionQueueFactory.create(receptionQueuePatches, receptionTableToAdd, 1));
        }
    }

}