package com.socialsim.controller.graphics.amenity.mapper;

import com.socialsim.controller.Main;
import com.socialsim.controller.graphics.amenity.AmenityMapper;
import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchobject.Amenity;
import com.socialsim.model.core.environment.patchobject.passable.goal.CollabDesk;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CollabDeskMapper extends AmenityMapper {

    public static void draw(List<Patch> patches, String facing, int length) {
        for (Patch patch : patches) {
            List<Amenity.AmenityBlock> amenityBlocks = new ArrayList<>();
            int origPatchRow = patch.getMatrixPosition().getRow();
            int origPatchCol = patch.getMatrixPosition().getColumn();

            Amenity.AmenityBlock.AmenityBlockFactory amenityBlockFactory = CollabDesk.CollabDeskBlock.collabDeskBlockFactory;
            Amenity.AmenityBlock amenityBlock = amenityBlockFactory.create(patch, true, true);
            amenityBlocks.add(amenityBlock);
            patch.setAmenityBlock(amenityBlock);

            if (Objects.equals(facing, "HORIZONTAL")) {
                for (int i = 1; i < length; i++) {
                    Patch patchBack = Main.simulator.getEnvironment().getPatch(origPatchRow, origPatchCol + i);
                    Amenity.AmenityBlock amenityBlockBack = amenityBlockFactory.create(patchBack, true, true);
                    amenityBlocks.add(amenityBlockBack);
                    patchBack.setAmenityBlock(amenityBlockBack);
                }
            }
            else {
                for (int i = 1; i < length; i++) {
                    Patch patchBack = Main.simulator.getEnvironment().getPatch(origPatchRow + i, origPatchCol);
                    Amenity.AmenityBlock amenityBlockBack = amenityBlockFactory.create(patchBack, true, true);
                    amenityBlocks.add(amenityBlockBack);
                    patchBack.setAmenityBlock(amenityBlockBack);
                }

//                for (int i = 0; i < 6; i++) {
//                    Patch patchFront = Main.officeSimulator.getOffice().getPatch(origPatchRow + i, origPatchCol + 1);
//                    Amenity.AmenityBlock amenityBlockFront = amenityBlockFactory.create(patchFront, true, false);
//                    amenityBlocks.add(amenityBlockFront);
//                    patchFront.setAmenityBlock(amenityBlockFront);
//                }
            }


            CollabDesk collabDeskToAdd = CollabDesk.CollabDeskFactory.create(amenityBlocks, true, facing);
            Main.simulator.getEnvironment().getCollabDesks().add(collabDeskToAdd);
            amenityBlocks.forEach(ab -> ab.getPatch().getEnvironment().getAmenityPatchSet().add(ab.getPatch()));
        }
    }

}