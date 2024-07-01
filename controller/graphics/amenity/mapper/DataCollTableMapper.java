package com.socialsim.controller.graphics.amenity.mapper;

import com.socialsim.controller.Main;
import com.socialsim.controller.graphics.amenity.AmenityMapper;
import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchobject.Amenity;
import com.socialsim.model.core.environment.patchobject.passable.goal.DataCollTable;

import java.util.ArrayList;
import java.util.List;

public class DataCollTableMapper extends AmenityMapper {

    public static void draw(List<Patch> patches, String dimensions) {
        for (Patch patch : patches) {
            List<Amenity.AmenityBlock> amenityBlocks = new ArrayList<>();
            int origPatchRow = patch.getMatrixPosition().getRow();
            int origPatchCol = patch.getMatrixPosition().getColumn();

            // TABLE'S FIRST PATCH (UPPER LEFT CORNER)
            Amenity.AmenityBlock.AmenityBlockFactory amenityBlockFactory = DataCollTable.DataCollTableBlock.dataCollTableBlockFactory;
            Amenity.AmenityBlock amenityBlock = amenityBlockFactory.create(patch, true, true);
            amenityBlocks.add(amenityBlock);
            patch.setAmenityBlock(amenityBlock);

            List<DataCollTable> dataCollTables = Main.simulator.getEnvironment().getDataCollTables();
            DataCollTable dataCollTableToAdd;
            int index = 0;

            List<Patch> dataCollChairNorthPatches = new ArrayList<>();

            if (dimensions.equals("1x6")) {

                // THE REST OF THE TABLE'S PATCHES
                for (int j = 1; j <= 5; j++) {
                    Patch nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow, origPatchCol + j);
                    Amenity.AmenityBlock nextAmenityBlock = amenityBlockFactory.create(nextPatch, false, false);
                    amenityBlocks.add(nextAmenityBlock);
                    nextPatch.setAmenityBlock(nextAmenityBlock);
                }

                dataCollTableToAdd = DataCollTable.DataCollTableFactory.create(amenityBlocks, true, "1x6");
                dataCollTables.add(dataCollTableToAdd);
                index = dataCollTables.indexOf(dataCollTableToAdd);

                // NORTH CHAIR
                for (int j = 1; j <= 4; j++) {
                    dataCollChairNorthPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol + j));
                }
                ChairMapper.draw(dataCollChairNorthPatches, index, "NORTH", "OFFICE", "DataCollTable");

            }

            amenityBlocks.forEach(ab -> ab.getPatch().getEnvironment().getAmenityPatchSet().add(ab.getPatch()));
        }
    }
}