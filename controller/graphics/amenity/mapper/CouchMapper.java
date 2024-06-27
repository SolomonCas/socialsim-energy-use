package com.socialsim.controller.graphics.amenity.mapper;

import com.socialsim.controller.Main;
import com.socialsim.controller.graphics.amenity.AmenityMapper;
import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchobject.Amenity;
import com.socialsim.model.core.environment.patchobject.passable.goal.Couch;
import com.socialsim.model.core.environment.patchobject.passable.goal.Couch;

import java.util.ArrayList;
import java.util.List;

public class CouchMapper extends AmenityMapper {

    public static void draw(List<Patch> patches, String facing) {
        for (Patch patch : patches) {
            List<Amenity.AmenityBlock> amenityBlocks = new ArrayList<>();
            int origPatchRow = patch.getMatrixPosition().getRow();
            int origPatchCol = patch.getMatrixPosition().getColumn();

            // TABLE'S FIRST PATCH (UPPER LEFT CORNER)
            Amenity.AmenityBlock.AmenityBlockFactory amenityBlockFactory = Couch.CouchBlock.couchBlockFactory;
            Amenity.AmenityBlock amenityBlock = amenityBlockFactory.create(patch, false, true);
            amenityBlocks.add(amenityBlock);
            patch.setAmenityBlock(amenityBlock);

            List<Couch> couches = Main.simulator.getEnvironment().getCouches();
            Couch couchToAdd;

            if (facing.equals("WEST")) {
                // THE REST OF THE TABLE'S PATCHES
                for (int i = 0; i <= 9; i++) {
                    for (int j = 0; j <= 1; j++) {
                        if (i == 0) {
                            j++;
                        }
                        Patch nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow + i, origPatchCol + j);
                        Amenity.AmenityBlock nextAmenityBlock = amenityBlockFactory.create(nextPatch, false, false);
                        amenityBlocks.add(nextAmenityBlock);
                        nextPatch.setAmenityBlock(nextAmenityBlock);
                    }
                }
            }

            couchToAdd = Couch.CouchFactory.create(amenityBlocks, true, "WEST");
            couches.add(couchToAdd);
            amenityBlocks.forEach(ab -> ab.getPatch().getEnvironment().getAmenityPatchSet().add(ab.getPatch()));
        }
    }

}