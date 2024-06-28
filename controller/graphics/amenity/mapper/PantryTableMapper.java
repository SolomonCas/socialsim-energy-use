package com.socialsim.controller.graphics.amenity.mapper;

import com.socialsim.controller.Main;
import com.socialsim.controller.graphics.amenity.AmenityMapper;
import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchobject.Amenity;
import com.socialsim.model.core.environment.patchobject.passable.goal.PantryTable;

import java.util.ArrayList;
import java.util.List;

public class PantryTableMapper extends AmenityMapper {


    public static void draw(List<Patch> patches, String type) {
        for (Patch patch : patches) {
            List<Amenity.AmenityBlock> amenityBlocks = new ArrayList<>();
            int origPatchRow = patch.getMatrixPosition().getRow();
            int origPatchCol = patch.getMatrixPosition().getColumn();

            // TABLE'S FIRST PATCH (UPPER LEFT CORNER)
            Amenity.AmenityBlock.AmenityBlockFactory amenityBlockFactory = PantryTable.PantryTableBlock.pantryTableBlockFactory;
            Amenity.AmenityBlock amenityBlock = amenityBlockFactory.create(patch, false, true);
            amenityBlocks.add(amenityBlock);
            patch.setAmenityBlock(amenityBlock);

            PantryTable tableToAdd;
            List<PantryTable> pantryTables = Main.simulator.getEnvironment().getPantryTables();

            if (type.equals("TYPE_A")) {
                tableToAdd = PantryTable.PantryTableFactory.create(amenityBlocks, true, "TYPE_A");
                pantryTables.add(tableToAdd);
                int index = pantryTables.indexOf(tableToAdd);

                // SOUTH CHAIR(S)
                List<Patch> pantryChairTypeASouthPatches = new ArrayList<>();
                pantryChairTypeASouthPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow - 1, origPatchCol));
                ChairMapper.draw(pantryChairTypeASouthPatches, index, "SOUTH", "PANTRY_TYPE_A", "PantryTable");
                // NORTH CHAIR(S)
                List<Patch> pantryChairTypeANorthPatches = new ArrayList<>();
                pantryChairTypeANorthPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol));
                ChairMapper.draw(pantryChairTypeANorthPatches, index, "NORTH", "PANTRY_TYPE_A", "PantryTable");
                // EAST CHAIR(S)
                List<Patch> pantryChairTypeAEastPatches = new ArrayList<>();
                pantryChairTypeAEastPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow, origPatchCol - 1));
                ChairMapper.draw(pantryChairTypeAEastPatches, index, "EAST", "PANTRY_TYPE_A", "PantryTable");
                // WEST CHAIR(S)
                List<Patch> pantryChairTypeAWestPatches = new ArrayList<>();
                pantryChairTypeAWestPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow, origPatchCol + 1));
                ChairMapper.draw(pantryChairTypeAWestPatches, index, "WEST", "PANTRY_TYPE_A", "PantryTable");
            }
            else if (type.equals("TYPE_B")) {

                // THE REST OF THE TABLE'S PATCHES
                for (int j = 1; j <= 2; j++) {
                    Patch nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow, origPatchCol + j);
                    Amenity.AmenityBlock nextAmenityBlock = amenityBlockFactory.create(nextPatch, false, false);
                    amenityBlocks.add(nextAmenityBlock);
                    nextPatch.setAmenityBlock(nextAmenityBlock);
                }

                tableToAdd = PantryTable.PantryTableFactory.create(amenityBlocks, true, "TYPE_B");
                pantryTables.add(tableToAdd);
                int index = pantryTables.indexOf(tableToAdd);

                // SOUTH CHAIR(S)
                List<Patch> pantryChairTypeBSouthPatches = new ArrayList<>();
                pantryChairTypeBSouthPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow - 1, origPatchCol));
                pantryChairTypeBSouthPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow - 1, origPatchCol + 2));
                ChairMapper.draw(pantryChairTypeBSouthPatches, index, "SOUTH", "PANTRY_TYPE_B", "PantryTable");
                // NORTH CHAIR(S)
                List<Patch> pantryChairTypeBNorthPatches = new ArrayList<>();
                pantryChairTypeBNorthPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol));
                pantryChairTypeBNorthPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol + 2));

                ChairMapper.draw(pantryChairTypeBNorthPatches, index, "NORTH", "PANTRY_TYPE_B", "PantryTable");
            }

            amenityBlocks.forEach(ab -> ab.getPatch().getEnvironment().getAmenityPatchSet().add(ab.getPatch()));
        }
    }


}