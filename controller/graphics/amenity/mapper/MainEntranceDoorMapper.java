package com.socialsim.controller.graphics.amenity.mapper;

import com.socialsim.controller.Main;
import com.socialsim.controller.graphics.amenity.AmenityMapper;
import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchobject.Amenity;
import com.socialsim.model.core.environment.patchobject.passable.goal.MainEntranceDoor;

import java.util.ArrayList;
import java.util.List;

public class MainEntranceDoorMapper extends AmenityMapper {

    public static void draw(List<Patch> patches, String facing) {
        for (Patch patch : patches) {
            List<Amenity.AmenityBlock> amenityBlocks = new ArrayList<>();
            int origPatchRow = patch.getMatrixPosition().getRow();
            int origPatchCol = patch.getMatrixPosition().getColumn();

            Amenity.AmenityBlock.AmenityBlockFactory amenityBlockFactory = MainEntranceDoor.MainEntranceDoorBlock.mainEntranceDoorBlockFactory;
            Amenity.AmenityBlock amenityBlock = amenityBlockFactory.create(patch, true, true);
            amenityBlocks.add(amenityBlock);
            patch.setAmenityBlock(amenityBlock);
            patch.setPatchField(null);

            if(facing.equals("UP") || facing.equals("DOWN")) {
                Patch rightPatch = Main.simulator.getEnvironment().getPatch(origPatchRow, origPatchCol + 1);
                Amenity.AmenityBlock amenityBlock2 = amenityBlockFactory.create(rightPatch, true, true);
                amenityBlocks.add(amenityBlock2);
                rightPatch.setAmenityBlock(amenityBlock2);
                rightPatch.setPatchField(null);

                Patch rightPatch3 = Main.simulator.getEnvironment().getPatch(origPatchRow, origPatchCol + 2);
                Amenity.AmenityBlock amenityBlock3 = amenityBlockFactory.create(rightPatch3, true, true);
                amenityBlocks.add(amenityBlock3);
                rightPatch3.setAmenityBlock(amenityBlock3);
                rightPatch3.setPatchField(null);

                Patch rightPatch4 = Main.simulator.getEnvironment().getPatch(origPatchRow, origPatchCol + 3);
                Amenity.AmenityBlock amenityBlock4 = amenityBlockFactory.create(rightPatch4, true, false);
                amenityBlocks.add(amenityBlock4);
                rightPatch4.setAmenityBlock(amenityBlock4);
                rightPatch4.setPatchField(null);
            }
            else {
                Patch lowerPatch = Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol);
                Amenity.AmenityBlock amenityBlock2 = amenityBlockFactory.create(lowerPatch, true, true);
                amenityBlocks.add(amenityBlock2);
                lowerPatch.setAmenityBlock(amenityBlock2);
                lowerPatch.setPatchField(null);

                Patch lowerPatch3 = Main.simulator.getEnvironment().getPatch(origPatchRow + 2, origPatchCol);
                Amenity.AmenityBlock amenityBlock3 = amenityBlockFactory.create(lowerPatch3, true, true);
                amenityBlocks.add(amenityBlock3);
                lowerPatch3.setAmenityBlock(amenityBlock3);
                lowerPatch3.setPatchField(null);

                Patch lowerPatch4 = Main.simulator.getEnvironment().getPatch(origPatchRow + 3, origPatchCol);
                Amenity.AmenityBlock amenityBlock4 = amenityBlockFactory.create(lowerPatch4, true, false);
                amenityBlocks.add(amenityBlock4);
                lowerPatch4.setAmenityBlock(amenityBlock4);
                lowerPatch4.setPatchField(null);
            }

            MainEntranceDoor doorToAdd = MainEntranceDoor.MainEntranceDoorFactory.create(amenityBlocks, true, facing);
            Main.simulator.getEnvironment().getMainEntranceDoors().add(doorToAdd);
            amenityBlocks.forEach(ab -> ab.getPatch().getEnvironment().getAmenityPatchSet().add(ab.getPatch()));
        }
    }

}