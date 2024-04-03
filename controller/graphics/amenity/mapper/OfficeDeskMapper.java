package com.socialsim.controller.graphics.amenity.mapper;

import com.socialsim.controller.Main;
import com.socialsim.controller.graphics.amenity.AmenityMapper;
import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchobject.Amenity;
import com.socialsim.model.core.environment.patchobject.passable.goal.OfficeDesk;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OfficeDeskMapper extends AmenityMapper {

    public static void draw(List<Patch> patches, String facing, int length) {
        for (Patch patch : patches) {
            List<Amenity.AmenityBlock> amenityBlocks = new ArrayList<>();
            int origPatchRow = patch.getMatrixPosition().getRow();
            int origPatchCol = patch.getMatrixPosition().getColumn();

            Amenity.AmenityBlock.AmenityBlockFactory amenityBlockFactory = OfficeDesk.OfficeDeskBlock.officeDeskBlockFactory;
            Amenity.AmenityBlock amenityBlock = amenityBlockFactory.create(patch, true, true);
            amenityBlocks.add(amenityBlock);
            patch.setAmenityBlock(amenityBlock);

            if (Objects.equals(facing, "HORIZONTAL")) {
                for (int i = 1; i < length * 2; i++) {
                    Patch patchBack = Main.simulator.getEnvironment().getPatch(origPatchRow, origPatchCol + i);
                    Amenity.AmenityBlock amenityBlockBack =
                            amenityBlockFactory.create(patchBack, true, true);
                    amenityBlocks.add(amenityBlockBack);
                    patchBack.setAmenityBlock(amenityBlockBack);
                }
            }
            else {
                for (int i = 1; i < length * 2; i++) {
                    Patch patchBack = Main.simulator.getEnvironment().getPatch(origPatchRow + i, origPatchCol);
                    Amenity.AmenityBlock amenityBlockBack =
                            amenityBlockFactory.create(patchBack, true, true);
                    amenityBlocks.add(amenityBlockBack);
                    patchBack.setAmenityBlock(amenityBlockBack);
                }
            }

            OfficeDesk officeDeskToAdd = OfficeDesk.OfficeDeskFactory.create(amenityBlocks, true);
            Main.simulator.getEnvironment().getOfficeDesks().add(officeDeskToAdd);
            amenityBlocks.forEach(ab -> ab.getPatch().getEnvironment().getAmenityPatchSet().add(ab.getPatch()));
        }
    }

}