package com.socialsim.controller.graphics.amenity.mapper;

import com.socialsim.controller.Main;
import com.socialsim.controller.graphics.amenity.AmenityMapper;
import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchobject.Amenity;
import com.socialsim.model.core.environment.patchobject.passable.goal.Whiteboard;

import java.util.ArrayList;
import java.util.List;

public class WhiteboardMapper extends AmenityMapper {

    public static void draw(List<Patch> patches, String facing) {
        for (Patch patch : patches) {
            List<Amenity.AmenityBlock> amenityBlocks = new ArrayList<>();
            int origPatchRow = patch.getMatrixPosition().getRow();
            int origPatchCol = patch.getMatrixPosition().getColumn();

            Amenity.AmenityBlock.AmenityBlockFactory amenityBlockFactory = Whiteboard.WhiteboardBlock.whiteboardBlockFactory;
            Amenity.AmenityBlock amenityBlock = amenityBlockFactory.create(patch, false, true);
            amenityBlocks.add(amenityBlock);
            patch.setAmenityBlock(amenityBlock);

            if (facing.equals("UP") || facing.equals("DOWN")) {
                Patch rightPatch = Main.simulator.getEnvironment().getPatch(origPatchRow, origPatchCol + 1);
                Amenity.AmenityBlock amenityBlock2 = amenityBlockFactory.create(rightPatch, true, false);
                amenityBlocks.add(amenityBlock2);
                rightPatch.setAmenityBlock(amenityBlock2);
            }
            else {
                Patch lowerPatch = Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol);
                Amenity.AmenityBlock amenityBlock2 = amenityBlockFactory.create(lowerPatch, true, false);
                amenityBlocks.add(amenityBlock2);
                lowerPatch.setAmenityBlock(amenityBlock2);
            }


            Whiteboard whiteboardToAdd = Whiteboard.WhiteboardFactory.create(amenityBlocks, true, facing);
            Main.simulator.getEnvironment().getWhiteboards().add(whiteboardToAdd);
            amenityBlocks.forEach(ab -> ab.getPatch().getEnvironment().getAmenityPatchSet().add(ab.getPatch()));
        }
    }

}