package com.socialsim.controller.graphics.amenity.mapper;


import com.socialsim.controller.Main;
import com.socialsim.controller.graphics.amenity.AmenityMapper;
import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchobject.Amenity;
import com.socialsim.model.core.environment.patchobject.passable.goal.LearningTable;
import com.socialsim.model.core.environment.patchobject.passable.goal.PantryTable;

import java.util.ArrayList;
import java.util.List;

public class LearningTableMapper extends AmenityMapper {

    /***** METHOD *****/
    public static void draw(List<Patch> patches, String orientation) {
        for (Patch patch : patches) {
            List<Amenity.AmenityBlock> amenityBlocks = new ArrayList<>();
            int origPatchRow = patch.getMatrixPosition().getRow();
            int origPatchCol = patch.getMatrixPosition().getColumn();

            // TABLE'S FIRST PATCH (UPPER LEFT CORNER)
            Amenity.AmenityBlock.AmenityBlockFactory amenityBlockFactory = LearningTable.LearningTableBlock.learningTableBlockFactory;
            Amenity.AmenityBlock amenityBlock = amenityBlockFactory.create(patch, true, true);
            amenityBlocks.add(amenityBlock);
            patch.setAmenityBlock(amenityBlock);

            LearningTable tableToAdd;
            List<LearningTable> learningTables = Main.simulator.getEnvironment().getLearningTables();
            int index = 0;

            if (orientation.equals("HORIZONTAL")) {
                for (int i = 0; i <= 1; i++) {
                    for (int j = 0; j <= 4; j++) {
                        if (i == 0) {
                            j++;
                        }
                        Patch nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow + i, origPatchCol + j);
                        Amenity.AmenityBlock nextAmenityBlock = amenityBlockFactory.create(nextPatch, false, false);
                        amenityBlocks.add(nextAmenityBlock);
                        nextPatch.setAmenityBlock(nextAmenityBlock);
                    }
                }

                tableToAdd = LearningTable.LearningTableFactory.create(amenityBlocks, true, orientation);
                learningTables.add(tableToAdd);
                index = learningTables.indexOf(tableToAdd);

                // SOUTH CHAIR(S)
                List<Patch> southHorizontalLearningChair = new ArrayList<>();
                southHorizontalLearningChair.add(Main.simulator.getEnvironment().getPatch(origPatchRow - 1, origPatchCol));
                southHorizontalLearningChair.add(Main.simulator.getEnvironment().getPatch(origPatchRow - 1, origPatchCol + 2));
                southHorizontalLearningChair.add(Main.simulator.getEnvironment().getPatch(origPatchRow - 1, origPatchCol + 4));
                ChairMapper.draw(southHorizontalLearningChair, index, "SOUTH", "OFFICE", "LearningTable");
                // NORTH CHAIR(S)
                List<Patch> northHorizontalLearningChair = new ArrayList<>();
                northHorizontalLearningChair.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 2, origPatchCol));
                northHorizontalLearningChair.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 2, origPatchCol + 2));
                northHorizontalLearningChair.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 2, origPatchCol + 4));
                ChairMapper.draw(northHorizontalLearningChair, index, "NORTH", "OFFICE", "LearningTable");
            }

            amenityBlocks.forEach(ab -> ab.getPatch().getEnvironment().getAmenityPatchSet().add(ab.getPatch()));

        }
    }
}
