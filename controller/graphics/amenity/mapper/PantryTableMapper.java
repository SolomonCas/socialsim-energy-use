package com.socialsim.controller.graphics.amenity.mapper;

import com.socialsim.controller.Main;
import com.socialsim.controller.graphics.amenity.AmenityMapper;
import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchobject.Amenity;
import com.socialsim.model.core.environment.patchobject.passable.goal.PantryTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PantryTableMapper extends AmenityMapper {

    public static void draw(List<Patch> patches, String facing) {
        for (Patch patch : patches) {
            List<Amenity.AmenityBlock> amenityBlocks = new ArrayList<>();
            int origPatchRow = patch.getMatrixPosition().getRow();
            int origPatchCol = patch.getMatrixPosition().getColumn();

            List<Patch> pantryChairPatches = new ArrayList<>();

            Amenity.AmenityBlock.AmenityBlockFactory amenityBlockFactory = PantryTable.PantryTableBlock.pantryTableBlockFactory;
            Amenity.AmenityBlock amenityBlock = amenityBlockFactory.create(patch, true, true);
            amenityBlocks.add(amenityBlock);
            patch.setAmenityBlock(amenityBlock);

            if (Objects.equals(facing, "UP") || Objects.equals(facing, "DOWN")) {
                Patch patch2 = Main.simulator.getEnvironment().getPatch(origPatchRow, origPatchCol + 1);
                Amenity.AmenityBlock amenityBlock2 = amenityBlockFactory.create(patch2, true, false);
                amenityBlocks.add(amenityBlock2);
                patch2.setAmenityBlock(amenityBlock2);
                pantryChairPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow - 1, origPatchCol));
                pantryChairPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow, origPatchCol - 1));
                pantryChairPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol + 1));
                pantryChairPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow, origPatchCol + 2));
            }
            else {
                Patch patch2 = Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol);
                Amenity.AmenityBlock amenityBlock2 = amenityBlockFactory.create(patch2, true, false);
                amenityBlocks.add(amenityBlock2);
                patch2.setAmenityBlock(amenityBlock2);
                pantryChairPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow, origPatchCol - 1));
                pantryChairPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow - 1, origPatchCol));
                pantryChairPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol + 1));
                pantryChairPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 2, origPatchCol));
            }


            PantryTable tableToAdd = PantryTable.PantryTableFactory.create(amenityBlocks, true, facing);
            Main.simulator.getEnvironment().getPantryTables().add(tableToAdd);
            int index = Main.simulator.getEnvironment().getPantryTables().indexOf(tableToAdd);
            PantryChairMapper.draw(pantryChairPatches, index);

            amenityBlocks.forEach(ab -> ab.getPatch().getEnvironment().getAmenityPatchSet().add(ab.getPatch()));
        }
    }

}