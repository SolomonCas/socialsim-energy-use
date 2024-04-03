package com.socialsim.controller.graphics.amenity.mapper;

import com.socialsim.controller.Main;
import com.socialsim.controller.graphics.amenity.AmenityMapper;
import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchobject.Amenity;
import com.socialsim.model.core.environment.patchobject.passable.goal.MeetingDesk;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MeetingDeskMapper extends AmenityMapper {

    public static void draw(List<Patch> patches, String facing, int length) {
        for (Patch patch : patches) {
            List<Amenity.AmenityBlock> amenityBlocks = new ArrayList<>();
            int origPatchRow = patch.getMatrixPosition().getRow();
            int origPatchCol = patch.getMatrixPosition().getColumn();

            Amenity.AmenityBlock.AmenityBlockFactory amenityBlockFactory = MeetingDesk.MeetingDeskBlock.meetingDeskBlockFactory;
            Amenity.AmenityBlock amenityBlock = amenityBlockFactory.create(patch, true, true);
            amenityBlocks.add(amenityBlock);
            patch.setAmenityBlock(amenityBlock);

            if (Objects.equals(facing, "HORIZONTAL")) {
                for (int i = 1; i < length * 2; i++) {
                    Patch patchBack = Main.simulator.getEnvironment().getPatch(origPatchRow, origPatchCol + i);
                    Amenity.AmenityBlock amenityBlockBack = null;
                    if (i % 2 == 0) {
                        amenityBlockBack = amenityBlockFactory.create(patchBack, true, true);
                    }
                    else {
                        amenityBlockBack = amenityBlockFactory.create(patchBack, true, false);
                    }
                    amenityBlocks.add(amenityBlockBack);
                    patchBack.setAmenityBlock(amenityBlockBack);
                }

                for (int i = 0; i < length * 2; i++) {
                    Patch patchFront = Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol + i);
                    Amenity.AmenityBlock amenityBlockFront = amenityBlockFactory.create(patchFront, true, false);
                    amenityBlocks.add(amenityBlockFront);
                    patchFront.setAmenityBlock(amenityBlockFront);
                }
            }
            else {
                for (int i = 1; i < length * 2; i++) {
                    Patch patchBack = Main.simulator.getEnvironment().getPatch(origPatchRow + i, origPatchCol);
                    Amenity.AmenityBlock amenityBlockBack = null;
                    if (i % 2 == 0) {
                        amenityBlockBack = amenityBlockFactory.create(patchBack, true, true);
                    }
                    else {
                        amenityBlockBack = amenityBlockFactory.create(patchBack, true, false);
                    }
                    amenityBlocks.add(amenityBlockBack);
                    patchBack.setAmenityBlock(amenityBlockBack);
                }

                for (int i = 0; i < length * 2; i++) {
                    Patch patchFront = Main.simulator.getEnvironment().getPatch(origPatchRow + i, origPatchCol + 1);
                    Amenity.AmenityBlock amenityBlockFront = amenityBlockFactory.create(patchFront, true, false);
                    amenityBlocks.add(amenityBlockFront);
                    patchFront.setAmenityBlock(amenityBlockFront);
                }
            }

            MeetingDesk meetingDeskToAdd = MeetingDesk.MeetingDeskFactory.create(amenityBlocks, true, facing);
            Main.simulator.getEnvironment().getMeetingDesks().add(meetingDeskToAdd);
            amenityBlocks.forEach(ab -> ab.getPatch().getEnvironment().getAmenityPatchSet().add(ab.getPatch()));
        }
    }

}