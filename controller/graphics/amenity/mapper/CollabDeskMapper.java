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

            List<Patch> collabChairPatches = new ArrayList<>();

            Amenity.AmenityBlock.AmenityBlockFactory amenityBlockFactory = CollabDesk.CollabDeskBlock.collabDeskBlockFactory;
            Amenity.AmenityBlock amenityBlock = amenityBlockFactory.create(patch, true, true);
            amenityBlocks.add(amenityBlock);
            patch.setAmenityBlock(amenityBlock);

            collabChairPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow - 1, origPatchCol));
            collabChairPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol));

            if (Objects.equals(facing, "HORIZONTAL")) {
                for (int i = 1; i < length; i++) {
                    Patch patchBack = Main.simulator.getEnvironment().getPatch(origPatchRow, origPatchCol + i);
                    Amenity.AmenityBlock amenityBlockBack = amenityBlockFactory.create(patchBack, true, true);
                    amenityBlocks.add(amenityBlockBack);
                    patchBack.setAmenityBlock(amenityBlockBack);

                    collabChairPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow - 1, origPatchCol + i));
                    collabChairPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol + i));
                }
            }
            else {
                for (int i = 1; i < length; i++) {
                    Patch patchBack = Main.simulator.getEnvironment().getPatch(origPatchRow + i, origPatchCol);
                    Amenity.AmenityBlock amenityBlockBack = amenityBlockFactory.create(patchBack, true, true);
                    amenityBlocks.add(amenityBlockBack);
                    patchBack.setAmenityBlock(amenityBlockBack);

                    collabChairPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow - 1, origPatchCol + i));
                    collabChairPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol + i));
                }

            }


            CollabDesk collabDeskToAdd = CollabDesk.CollabDeskFactory.create(amenityBlocks, true, facing);
            Main.simulator.getEnvironment().getCollabDesks().add(collabDeskToAdd);
            int index = Main.simulator.getEnvironment().getCollabDesks().indexOf(collabDeskToAdd);
            CollabChairMapper.draw(collabChairPatches, index);

            amenityBlocks.forEach(ab -> ab.getPatch().getEnvironment().getAmenityPatchSet().add(ab.getPatch()));
        }
    }

}