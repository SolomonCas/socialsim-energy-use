package com.socialsim.controller.graphics.amenity.mapper;

import com.socialsim.controller.Main;
import com.socialsim.controller.graphics.amenity.AmenityMapper;
import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchfield.ReceptionQueue;
import com.socialsim.model.core.environment.patchobject.Amenity;
import com.socialsim.model.core.environment.patchobject.passable.goal.ReceptionTable;

import java.util.ArrayList;
import java.util.List;

public class ReceptionTableMapper extends AmenityMapper {

    public static void draw(List<Patch> patches, String dimensions) {
        for (Patch patch : patches) {
            List<Amenity.AmenityBlock> amenityBlocks = new ArrayList<>();
            int origPatchRow = patch.getMatrixPosition().getRow();
            int origPatchCol = patch.getMatrixPosition().getColumn();

            // TABLE'S FIRST PATCH (UPPER LEFT CORNER)
            Amenity.AmenityBlock.AmenityBlockFactory amenityBlockFactory = ReceptionTable.ReceptionTableBlock.receptionTableBlockFactory;
            Amenity.AmenityBlock amenityBlock = amenityBlockFactory.create(patch, true, true);
            amenityBlocks.add(amenityBlock);
            patch.setAmenityBlock(amenityBlock);

            List<ReceptionTable> receptionTables = Main.simulator.getEnvironment().getReceptionTables();
            ReceptionTable receptionTableToAdd;
            int index = 0;

            List<Patch> receptionChairNorthPatches = new ArrayList<>();
            List<Patch> receptionQueuePatches = new ArrayList<>();


            if (dimensions.equals("1x1")) {

                receptionTableToAdd = ReceptionTable.ReceptionTableFactory.create(amenityBlocks, true, "1x1", 10);
                receptionTables.add(receptionTableToAdd);
                index = receptionTables.indexOf(receptionTableToAdd);

                // NORTH CHAIR(S)
                receptionChairNorthPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol));

                // QUEUE POSITION
                receptionQueuePatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow - 1, origPatchCol));
                Main.simulator.getEnvironment().getReceptionQueues().add(ReceptionQueue.receptionQueueFactory.create(receptionQueuePatches, receptionTableToAdd, "receptionQueue"));

            }
            else if (dimensions.equals("5x1")) {

                // THE REST OF THE TABLE'S PATCHES
                for (int i = 1; i <= 4; i++) {
                    Patch nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow + i, origPatchCol);
                    Amenity.AmenityBlock nextAmenityBlock = amenityBlockFactory.create(nextPatch, false, false);
                    amenityBlocks.add(nextAmenityBlock);
                    nextPatch.setAmenityBlock(nextAmenityBlock);
                }

                receptionTableToAdd = ReceptionTable.ReceptionTableFactory.create(amenityBlocks, true, "5x1", 10);
                receptionTables.add(receptionTableToAdd);
                index = receptionTables.indexOf(receptionTableToAdd);

                // NORTH CHAIR(S)
                receptionChairNorthPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 3, origPatchCol + 1));
                receptionChairNorthPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol + 1));

                // QUEUE POSITION
                receptionQueuePatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 3, origPatchCol -1));
                Main.simulator.getEnvironment().getReceptionQueues().add(ReceptionQueue.receptionQueueFactory.create(receptionQueuePatches, receptionTableToAdd, "receptionQueue"));
            }
            else if (dimensions.equals("4x1")) {

                // THE REST OF THE TABLE'S PATCHES
                for (int i = 1; i <= 3; i++) {
                    Patch nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow + i, origPatchCol);
                    Amenity.AmenityBlock nextAmenityBlock = amenityBlockFactory.create(nextPatch, false, false);
                    amenityBlocks.add(nextAmenityBlock);
                    nextPatch.setAmenityBlock(nextAmenityBlock);
                }

                receptionTableToAdd = ReceptionTable.ReceptionTableFactory.create(amenityBlocks, true, "4x1", 10);
                receptionTables.add(receptionTableToAdd);
                index = receptionTables.indexOf(receptionTableToAdd);

                // NORTH CHAIR(S)
                receptionChairNorthPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 2, origPatchCol + 1));
                receptionChairNorthPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol + 1));

                // QUEUE POSITION
                receptionQueuePatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 2, origPatchCol - 1));
                Main.simulator.getEnvironment().getReceptionQueues().add(ReceptionQueue.receptionQueueFactory.create(receptionQueuePatches, receptionTableToAdd, "receptionQueue"));
            }
            else if (dimensions.equals("2x1")) {

                // THE REST OF THE TABLE'S PATCHES
                Patch nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol);
                Amenity.AmenityBlock nextAmenityBlock = amenityBlockFactory.create(nextPatch, false, false);
                amenityBlocks.add(nextAmenityBlock);
                nextPatch.setAmenityBlock(nextAmenityBlock);

                receptionTableToAdd = ReceptionTable.ReceptionTableFactory.create(amenityBlocks, true, "2x1", 10);
                receptionTables.add(receptionTableToAdd);
                index = receptionTables.indexOf(receptionTableToAdd);

                // NORTH CHAIR(S)
                receptionChairNorthPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol + 1));
                receptionChairNorthPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow, origPatchCol + 1));

                // QUEUE POSITION
                receptionQueuePatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow - 1, origPatchCol + 1));
                Main.simulator.getEnvironment().getReceptionQueues().add(ReceptionQueue.receptionQueueFactory.create(receptionQueuePatches, receptionTableToAdd, "receptionQueue"));
            }
            else if (dimensions.equals("1x8")) {

                // THE REST OF THE TABLE'S PATCHES
                for (int j = 1; j <= 7; j++) {
                    Patch nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow, origPatchCol + j);
                    if (j == 2) {
                        Amenity.AmenityBlock nextAmenityBlock = amenityBlockFactory.create(nextPatch, true, false);
                        amenityBlocks.add(nextAmenityBlock);
                        nextPatch.setAmenityBlock(nextAmenityBlock);
                    }
                    else {
                        Amenity.AmenityBlock nextAmenityBlock = amenityBlockFactory.create(nextPatch, false, false);
                        amenityBlocks.add(nextAmenityBlock);
                        nextPatch.setAmenityBlock(nextAmenityBlock);
                    }
                }

                receptionTableToAdd = ReceptionTable.ReceptionTableFactory.create(amenityBlocks, true, "1x8", 10);
                receptionTables.add(receptionTableToAdd);
                index = receptionTables.indexOf(receptionTableToAdd);

                // NORTH CHAIR(S)
                receptionChairNorthPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol + 5));
                receptionChairNorthPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol + 2));

                // QUEUE POSITION
                receptionQueuePatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow - 1, origPatchCol + 5));
                Main.simulator.getEnvironment().getReceptionQueues().add(ReceptionQueue.receptionQueueFactory.create(receptionQueuePatches, receptionTableToAdd, "receptionQueue"));
            }
            else if (dimensions.equals("1x6")) {

                // THE REST OF THE TABLE'S PATCHES
                for (int j = 1; j <= 6; j++) {
                    Patch nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow, origPatchCol + j);
                    Amenity.AmenityBlock nextAmenityBlock = amenityBlockFactory.create(nextPatch, false, false);
                    amenityBlocks.add(nextAmenityBlock);
                    nextPatch.setAmenityBlock(nextAmenityBlock);
                }

                receptionTableToAdd = ReceptionTable.ReceptionTableFactory.create(amenityBlocks, true, "1x6", 10);
                receptionTables.add(receptionTableToAdd);
                index = receptionTables.indexOf(receptionTableToAdd);

                // NORTH CHAIR(S)
                receptionChairNorthPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol + 4));
                receptionChairNorthPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol + 1));

                // QUEUE POSITION
                receptionQueuePatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow - 1, origPatchCol + 4));
                Main.simulator.getEnvironment().getReceptionQueues().add(ReceptionQueue.receptionQueueFactory.create(receptionQueuePatches, receptionTableToAdd, "receptionQueue"));
            }
            else if (dimensions.equals("1x3")) {

                // THE REST OF THE TABLE'S PATCHES
                for (int j = 1; j <= 2; j++) {
                    Patch nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow, origPatchCol + j);
                    Amenity.AmenityBlock nextAmenityBlock = amenityBlockFactory.create(nextPatch, false, false);
                    amenityBlocks.add(nextAmenityBlock);
                    nextPatch.setAmenityBlock(nextAmenityBlock);
                }

                receptionTableToAdd = ReceptionTable.ReceptionTableFactory.create(amenityBlocks, true, "1x3", 10);
                receptionTables.add(receptionTableToAdd);
                index = receptionTables.indexOf(receptionTableToAdd);

                // NORTH CHAIR(S)
                receptionChairNorthPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol + 2));
                receptionChairNorthPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol));

                // QUEUE POSITION
                receptionQueuePatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow - 1, origPatchCol + 2));
                Main.simulator.getEnvironment().getReceptionQueues().add(ReceptionQueue.receptionQueueFactory.create(receptionQueuePatches, receptionTableToAdd, "receptionQueue"));
            }
            else if (dimensions.equals("1x2")) {

                // THE REST OF THE TABLE'S PATCHES
                Patch nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow, origPatchCol + 1);
                Amenity.AmenityBlock nextAmenityBlock = amenityBlockFactory.create(nextPatch, false, false);
                amenityBlocks.add(nextAmenityBlock);
                nextPatch.setAmenityBlock(nextAmenityBlock);

                receptionTableToAdd = ReceptionTable.ReceptionTableFactory.create(amenityBlocks, true, "1x2", 10);
                receptionTables.add(receptionTableToAdd);
                index = receptionTables.indexOf(receptionTableToAdd);

                // NORTH CHAIR(S)
                receptionChairNorthPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol + 1));
                receptionChairNorthPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol));

                // QUEUE POSITION
                receptionQueuePatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow - 1, origPatchCol + 1));
                Main.simulator.getEnvironment().getReceptionQueues().add(ReceptionQueue.receptionQueueFactory.create(receptionQueuePatches, receptionTableToAdd, "receptionQueue"));
            }

            ChairMapper.draw(receptionChairNorthPatches, index, "NORTH", "OFFICE", "ReceptionTable");
            amenityBlocks.forEach(ab -> ab.getPatch().getEnvironment().getAmenityPatchSet().add(ab.getPatch()));
        }
    }

}