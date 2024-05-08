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

    public static void draw(List<Patch> patches, String facing, int length, boolean isLarge) {
        for (Patch patch : patches) {
            List<Amenity.AmenityBlock> amenityBlocks = new ArrayList<>();
            int origPatchRow = patch.getMatrixPosition().getRow();
            int origPatchCol = patch.getMatrixPosition().getColumn();

            List<Patch> meetingChairPatches = new ArrayList<>();

            Amenity.AmenityBlock.AmenityBlockFactory amenityBlockFactory = MeetingDesk.MeetingDeskBlock.meetingDeskBlockFactory;
            Amenity.AmenityBlock amenityBlock = amenityBlockFactory.create(patch, true, true);
            amenityBlocks.add(amenityBlock);
            patch.setAmenityBlock(amenityBlock);

            if (isLarge && Objects.equals(facing, "HORIZONTAL")) {
                Patch patch1 = Main.simulator.getEnvironment().getPatch(origPatchRow + 2, origPatchCol);
                Amenity.AmenityBlock amenityBlock1 = amenityBlockFactory.create(patch1, true, true);
                amenityBlocks.add(amenityBlock1);
                patch.setAmenityBlock(amenityBlock1);
            }
            else if (isLarge && Objects.equals(facing, "VERTICAL")) {
                Patch patch1 = Main.simulator.getEnvironment().getPatch(origPatchRow, origPatchCol + 2);
                Amenity.AmenityBlock amenityBlock1 = amenityBlockFactory.create(patch1, true, true);
                amenityBlocks.add(amenityBlock1);
                patch.setAmenityBlock(amenityBlock1);
            }

            if (Objects.equals(facing, "HORIZONTAL")) {
                for (int i = 1; i < length * 2; i++) {
                    Patch patchBack = Main.simulator.getEnvironment().getPatch(origPatchRow, origPatchCol + i);
                    Amenity.AmenityBlock amenityBlockBack = null;
                    if (i % 2 == 0) {
                        amenityBlockBack = amenityBlockFactory.create(patchBack, true, true);
                    }
                    else {
                        amenityBlockBack = amenityBlockFactory.create(patchBack, true, false);
                        meetingChairPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow - 1, origPatchCol + i));
                    }
                    amenityBlocks.add(amenityBlockBack);
                    patchBack.setAmenityBlock(amenityBlockBack);
                }

                for (int i = 0; i < length * 2; i++) {
                    Patch patchFront = Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol + i);
                    Amenity.AmenityBlock amenityBlockFront = amenityBlockFactory.create(patchFront, true, false);
                    if (!isLarge && i % 2 != 0) {
                        meetingChairPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 2, origPatchCol + i));
                    }
                    amenityBlocks.add(amenityBlockFront);
                    patchFront.setAmenityBlock(amenityBlockFront);
                }

                if(isLarge) {
                    for (int i = 1; i < length * 2; i++) {
                        Patch patchBack = Main.simulator.getEnvironment().getPatch(origPatchRow + 2, origPatchCol + i);
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
                        Patch patchFront = Main.simulator.getEnvironment().getPatch(origPatchRow + 3, origPatchCol + i);
                        Amenity.AmenityBlock amenityBlockFront = amenityBlockFactory.create(patchFront, true, false);
                        if (i % 2 != 0) {
                            meetingChairPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 4, origPatchCol + i));
                        }
                        amenityBlocks.add(amenityBlockFront);
                        patchFront.setAmenityBlock(amenityBlockFront);
                    }
                }

                meetingChairPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol - 1));
                meetingChairPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol + length * 2));

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
                        meetingChairPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + i, origPatchCol - 1));
                    }
                    amenityBlocks.add(amenityBlockBack);
                    patchBack.setAmenityBlock(amenityBlockBack);
                }

                for (int i = 0; i < length * 2; i++) {
                    Patch patchFront = Main.simulator.getEnvironment().getPatch(origPatchRow + i, origPatchCol + 1);
                    Amenity.AmenityBlock amenityBlockFront = amenityBlockFactory.create(patchFront, true, false);
                    if (!isLarge && i % 2 != 0) {
                        meetingChairPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + i, origPatchCol + 2));
                    }
                    amenityBlocks.add(amenityBlockFront);
                    patchFront.setAmenityBlock(amenityBlockFront);
                }

                if(isLarge) {
                    for (int i = 1; i < length * 2; i++) {
                        Patch patchBack = Main.simulator.getEnvironment().getPatch(origPatchRow + i, origPatchCol + 2);
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
                        Patch patchFront = Main.simulator.getEnvironment().getPatch(origPatchRow + i, origPatchCol + 3);
                        Amenity.AmenityBlock amenityBlockFront = amenityBlockFactory.create(patchFront, true, false);
                        if (i % 2 != 0) {
                            meetingChairPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + i, origPatchCol + 4));
                        }
                        amenityBlocks.add(amenityBlockFront);
                        patchFront.setAmenityBlock(amenityBlockFront);
                    }

                }

                meetingChairPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow - 1, origPatchCol));
                meetingChairPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + length * 2, origPatchCol));
            }

            MeetingDesk meetingDeskToAdd = MeetingDesk.MeetingDeskFactory.create(amenityBlocks, true, facing);
            Main.simulator.getEnvironment().getMeetingDesks().add(meetingDeskToAdd);
            int index = Main.simulator.getEnvironment().getMeetingDesks().indexOf(meetingDeskToAdd);
            MeetingChairMapper.draw(meetingChairPatches, index);
            amenityBlocks.forEach(ab -> ab.getPatch().getEnvironment().getAmenityPatchSet().add(ab.getPatch()));
        }
    }

}