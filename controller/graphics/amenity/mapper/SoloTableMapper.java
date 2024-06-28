package com.socialsim.controller.graphics.amenity.mapper;

import com.socialsim.controller.Main;
import com.socialsim.controller.graphics.amenity.AmenityMapper;
import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchobject.Amenity;
import com.socialsim.model.core.environment.patchobject.passable.goal.SoloTable;

import java.util.ArrayList;
import java.util.List;

public class SoloTableMapper extends AmenityMapper {

    public static void draw(List<Patch> patches, String dimensions, String position) {
        for (Patch patch : patches) {
            List<Amenity.AmenityBlock> amenityBlocks = new ArrayList<>();
            int origPatchRow = patch.getMatrixPosition().getRow();
            int origPatchCol = patch.getMatrixPosition().getColumn();

            // TABLE'S FIRST PATCH (UPPER LEFT CORNER)
            Amenity.AmenityBlock.AmenityBlockFactory amenityBlockFactory = SoloTable.SoloTableBlock.soloTableBlockFactory;
            Amenity.AmenityBlock amenityBlock = amenityBlockFactory.create(patch, false, true);
            amenityBlocks.add(amenityBlock);
            patch.setAmenityBlock(amenityBlock);

            List<SoloTable> soloTables = Main.simulator.getEnvironment().getSoloTables();
            SoloTable soloTableToAdd;
            int index = 0;

            List<Patch> soloChairNorthPatches = new ArrayList<>();

            if (dimensions.equals("1x8")) {

                // THE REST OF THE TABLE'S PATCHES
                for (int j = 1; j <= 7; j++) {
                    Patch nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow, origPatchCol + j);
                    Amenity.AmenityBlock nextAmenityBlock = amenityBlockFactory.create(nextPatch, false, false);
                    amenityBlocks.add(nextAmenityBlock);
                    nextPatch.setAmenityBlock(nextAmenityBlock);
                }

                if (position.equals("TOP")) {
                    soloTableToAdd = SoloTable.SoloTableFactory.create(amenityBlocks, true, "1x8", "TOP");
                    soloTables.add(soloTableToAdd);
                    index = soloTables.indexOf(soloTableToAdd);

                    // CHAIR
                    soloChairNorthPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol + 5));
                    ChairMapper.draw(soloChairNorthPatches, index, "NORTH", "OFFICE", "SoloTable");
                }
                else if (position.equals("BOTTOM")) {
                    soloTableToAdd = SoloTable.SoloTableFactory.create(amenityBlocks, true, "1x8", "BOTTOM");
                    soloTables.add(soloTableToAdd);
                    index = soloTables.indexOf(soloTableToAdd);

                    // CHAIR
                    soloChairNorthPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow - 1, origPatchCol + 2));
                    ChairMapper.draw(soloChairNorthPatches, index, "SOUTH", "OFFICE", "SoloTable");
                }
            }
            
            amenityBlocks.forEach(ab -> ab.getPatch().getEnvironment().getAmenityPatchSet().add(ab.getPatch()));
        }
    }

}