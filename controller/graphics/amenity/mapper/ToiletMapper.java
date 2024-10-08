package com.socialsim.controller.graphics.amenity.mapper;

import com.socialsim.controller.Main;
import com.socialsim.controller.graphics.amenity.AmenityMapper;
import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchobject.Amenity;
import com.socialsim.model.core.environment.patchobject.passable.goal.OfficeToilet;
import com.socialsim.model.core.environment.patchobject.passable.goal.Toilet;

import java.util.ArrayList;
import java.util.List;

public class ToiletMapper extends AmenityMapper {

    public static void draw(List<Patch> patches, String facing, String type) {
        for (Patch patch : patches) {
            List<Amenity.AmenityBlock> amenityBlocks = new ArrayList<>();
            int origPatchRow = patch.getMatrixPosition().getRow();
            int origPatchCol = patch.getMatrixPosition().getColumn();

            // FIRST PATCH
            Amenity.AmenityBlock.AmenityBlockFactory amenityBlockFactory = Toilet.ToiletBlock.toiletBlockFactory;
            Amenity.AmenityBlock amenityBlock = amenityBlockFactory.create(patch, false, true);
            amenityBlocks.add(amenityBlock);
            patch.setAmenityBlock(amenityBlock);

            // SECOND PATCH
            Patch nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol);
            Amenity.AmenityBlock nextAmenityBlock = amenityBlockFactory.create(nextPatch, true, false);
            amenityBlocks.add(nextAmenityBlock);
            nextPatch.setAmenityBlock(nextAmenityBlock);



            switch (type) {
                case "TOILET" -> {
                    Toilet toiletToAdd = Toilet.ToiletFactory.create(amenityBlocks, true, facing);
                    Main.simulator.getEnvironment().getToilets().add(toiletToAdd);
                }
                case "OFFICE_TOILET" -> {
                    OfficeToilet officeToiletToAdd = OfficeToilet.OfficeToiletFactory.create(amenityBlocks, true, facing);
                    Main.simulator.getEnvironment().getOfficeToilets().add(officeToiletToAdd);
                }
            }

            amenityBlocks.forEach(ab -> ab.getPatch().getEnvironment().getAmenityPatchSet().add(ab.getPatch()));
        }
    }

}