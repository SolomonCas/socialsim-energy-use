package com.socialsim.controller.graphics.amenity.mapper;

import com.socialsim.controller.Main;
import com.socialsim.controller.graphics.amenity.AmenityMapper;
import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchobject.Amenity;
import com.socialsim.model.core.environment.patchobject.passable.goal.Aircon;

import java.util.ArrayList;
import java.util.List;

public class AirconMapper extends AmenityMapper {

    public static void draw(List<Patch> patches, boolean isOn) {
        for (Patch patch : patches) {
            List<Amenity.AmenityBlock> amenityBlocks = new ArrayList<>();
            int origPatchRow = patch.getMatrixPosition().getRow();
            int origPatchCol = patch.getMatrixPosition().getColumn();

            // FIRST PATCH
            Amenity.AmenityBlock.AmenityBlockFactory amenityBlockFactory = Aircon.AirconBlock.airconBlockFactory;
            Amenity.AmenityBlock amenityBlock = amenityBlockFactory.create(patch, true, true);
            amenityBlocks.add(amenityBlock);
            patch.setAmenityBlock(amenityBlock);

            for (int i = 0; i <= 1; i++) {
                for (int j = 0; j <= 1; j++) {
                    if (i == 0) {
                        j++;
                    }
                    Patch nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow + i, origPatchCol + j);
                    Amenity.AmenityBlock nextAmenityBlock = amenityBlockFactory.create(nextPatch, true, false);
                    amenityBlocks.add(nextAmenityBlock);
                    nextPatch.setAmenityBlock(nextAmenityBlock);
                }
            }

            List<Aircon> aircons = Main.simulator.getEnvironment().getAircons();
            Aircon airconToAdd;
            airconToAdd = Aircon.AirconFactory.create(amenityBlocks, true, isOn);
            aircons.add(airconToAdd);

            amenityBlocks.forEach(ab -> ab.getPatch().getEnvironment().getAmenityPatchSet().add(ab.getPatch()));
        }
    }

}