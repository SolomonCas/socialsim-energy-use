package com.socialsim.controller.graphics.amenity.mapper;

import com.socialsim.controller.Main;
import com.socialsim.controller.graphics.amenity.AmenityMapper;
import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchobject.Amenity;
import com.socialsim.model.core.environment.patchobject.passable.goal.HumanExpTable;

import java.util.ArrayList;
import java.util.List;

public class HumanExpTableMapper extends AmenityMapper {

    public static void draw(List<Patch> patches, String dimensions) {
        for (Patch patch : patches) {
            List<Amenity.AmenityBlock> amenityBlocks = new ArrayList<>();
            int origPatchRow = patch.getMatrixPosition().getRow();
            int origPatchCol = patch.getMatrixPosition().getColumn();

            // TABLE'S FIRST PATCH (UPPER LEFT CORNER)
            Amenity.AmenityBlock.AmenityBlockFactory amenityBlockFactory = HumanExpTable.HumanExpTableBlock.humanExpTableBlockFactory;
            Amenity.AmenityBlock amenityBlock = amenityBlockFactory.create(patch, false, true);
            amenityBlocks.add(amenityBlock);
            patch.setAmenityBlock(amenityBlock);

            List<HumanExpTable> humanExpTables = Main.simulator.getEnvironment().getHumanExpTables();
            HumanExpTable humanExpTableToAdd;
            int index = 0;

            List<Patch> humanExpChairWestPatches = new ArrayList<>();
            List<Patch> humanExpChairEastPatches = new ArrayList<>();

            if (dimensions.equals("5x1")) {

                // THE REST OF THE TABLE'S PATCHES
                for (int i = 1; i <= 4; i++) {
                    Patch nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow + i, origPatchCol);
                    Amenity.AmenityBlock nextAmenityBlock = amenityBlockFactory.create(nextPatch, false, false);
                    amenityBlocks.add(nextAmenityBlock);
                    nextPatch.setAmenityBlock(nextAmenityBlock);
                }

                humanExpTableToAdd = HumanExpTable.HumanExpTableFactory.create(amenityBlocks, true, "5x1");
                humanExpTables.add(humanExpTableToAdd);
                index = humanExpTables.indexOf(humanExpTableToAdd);

                // WEST CHAIR
                humanExpChairWestPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 2, origPatchCol + 2));
                HumanExpChairMapper.draw(humanExpChairWestPatches, index, "WEST", "OFFICE");

                // EAST CHAIR
                humanExpChairEastPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 2, origPatchCol - 2));
                HumanExpChairMapper.draw(humanExpChairEastPatches, index, "EAST", "OFFICE");
            }
            
            amenityBlocks.forEach(ab -> ab.getPatch().getEnvironment().getAmenityPatchSet().add(ab.getPatch()));
        }
    }

}