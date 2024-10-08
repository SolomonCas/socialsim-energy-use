package com.socialsim.controller.graphics.amenity.mapper;

import com.socialsim.controller.Main;
import com.socialsim.controller.graphics.amenity.AmenityMapper;
import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchobject.Amenity;
import com.socialsim.model.core.environment.patchobject.passable.goal.Whiteboard;
import com.socialsim.model.core.environment.patchobject.passable.goal.Whiteboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WhiteboardMapper extends AmenityMapper {

    // TODO: UPDATE THIS MAPPER
    public static void draw(List<Patch> patches, String facing, String length) {
        for (Patch patch : patches) {
            List<Amenity.AmenityBlock> amenityBlocks = new ArrayList<>();
            int origPatchRow = patch.getMatrixPosition().getRow();
            int origPatchCol = patch.getMatrixPosition().getColumn();

            if (facing.equals("NORTH")) {
                Amenity.AmenityBlock.AmenityBlockFactory amenityBlockFactory = Whiteboard.WhiteboardBlock.whiteboardBlockFactory;
                Amenity.AmenityBlock amenityBlock = amenityBlockFactory.create(patch, false, true);
                amenityBlocks.add(amenityBlock);
                patch.setAmenityBlock(amenityBlock);

                Patch patch2 = Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol);
                Amenity.AmenityBlock amenityBlock2 = amenityBlockFactory.create(patch2, false, false);
                amenityBlocks.add(amenityBlock2);
                patch2.setAmenityBlock(amenityBlock2);

                Patch patch3 = Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol);
                Amenity.AmenityBlock amenityBlock3 = amenityBlockFactory.create(patch3, true, false);
                amenityBlocks.add(amenityBlock3);
                patch3.setAmenityBlock(amenityBlock3);

                Patch patch4 = Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol);
                Amenity.AmenityBlock amenityBlock4 = amenityBlockFactory.create(patch4, false, false);
                amenityBlocks.add(amenityBlock4);
                patch4.setAmenityBlock(amenityBlock4);

            }
            else if (facing.equals("SOUTH")) {
                Amenity.AmenityBlock.AmenityBlockFactory amenityBlockFactory = Whiteboard.WhiteboardBlock.whiteboardBlockFactory;
                Amenity.AmenityBlock amenityBlock = amenityBlockFactory.create(patch, false, true);
                amenityBlocks.add(amenityBlock);
                patch.setAmenityBlock(amenityBlock);

                Patch patch2 = Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol);
                Amenity.AmenityBlock amenityBlock2 = amenityBlockFactory.create(patch2, false, false);
                amenityBlocks.add(amenityBlock2);
                patch2.setAmenityBlock(amenityBlock2);

                Patch patch3 = Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol);
                Amenity.AmenityBlock amenityBlock3 = amenityBlockFactory.create(patch3, false, false);
                amenityBlocks.add(amenityBlock3);
                patch3.setAmenityBlock(amenityBlock3);

                Patch patch4 = Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol);
                Amenity.AmenityBlock amenityBlock4 = amenityBlockFactory.create(patch4, true, false);
                amenityBlocks.add(amenityBlock4);
                patch4.setAmenityBlock(amenityBlock4);

            }
            else if (facing.equals("WEST")) {
                Amenity.AmenityBlock.AmenityBlockFactory amenityBlockFactory = Whiteboard.WhiteboardBlock.whiteboardBlockFactory;
                Amenity.AmenityBlock amenityBlock = amenityBlockFactory.create(patch, false, true);
                amenityBlocks.add(amenityBlock);
                patch.setAmenityBlock(amenityBlock);

                Patch patch2 = Main.simulator.getEnvironment().getPatch(origPatchRow, origPatchCol + 1);
                Amenity.AmenityBlock amenityBlock2 = amenityBlockFactory.create(patch2, false, false);
                amenityBlocks.add(amenityBlock2);
                patch2.setAmenityBlock(amenityBlock2);

                Patch patch3 = Main.simulator.getEnvironment().getPatch(origPatchRow, origPatchCol + 1);
                Amenity.AmenityBlock amenityBlock3 = amenityBlockFactory.create(patch3, true, false);
                amenityBlocks.add(amenityBlock3);
                patch3.setAmenityBlock(amenityBlock3);

                Patch patch4 = Main.simulator.getEnvironment().getPatch(origPatchRow, origPatchCol + 1);
                Amenity.AmenityBlock amenityBlock4 = amenityBlockFactory.create(patch4, false, false);
                amenityBlocks.add(amenityBlock4);
                patch4.setAmenityBlock(amenityBlock4);
            }
            else if (facing.equals("EAST")){
                Amenity.AmenityBlock.AmenityBlockFactory amenityBlockFactory = Whiteboard.WhiteboardBlock.whiteboardBlockFactory;
                Amenity.AmenityBlock amenityBlock = amenityBlockFactory.create(patch, true, true);
                amenityBlocks.add(amenityBlock);
                patch.setAmenityBlock(amenityBlock);

                Patch patch2 = Main.simulator.getEnvironment().getPatch(origPatchRow, origPatchCol + 1);
                Amenity.AmenityBlock amenityBlock2 = amenityBlockFactory.create(patch2, true, false);
                amenityBlocks.add(amenityBlock2);
                patch2.setAmenityBlock(amenityBlock2);

                Patch patch3 = Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol);
                Amenity.AmenityBlock amenityBlock3 = amenityBlockFactory.create(patch3, false, false);
                amenityBlocks.add(amenityBlock3);
                patch3.setAmenityBlock(amenityBlock3);

                Patch patch4 = Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol + 1);
                Amenity.AmenityBlock amenityBlock4 = amenityBlockFactory.create(patch4, false, false);
                amenityBlocks.add(amenityBlock4);
                patch4.setAmenityBlock(amenityBlock4);
            }


            Whiteboard whiteboardToAdd = Whiteboard.WhiteboardFactory.create(amenityBlocks, true, facing, length);
            Main.simulator.getEnvironment().getWhiteboards().add(whiteboardToAdd);
            amenityBlocks.forEach(ab -> ab.getPatch().getEnvironment().getAmenityPatchSet().add(ab.getPatch()));
        }
    }

}