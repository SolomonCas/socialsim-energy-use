package com.socialsim.controller.graphics.amenity.mapper;

import com.socialsim.controller.Main;
import com.socialsim.controller.graphics.amenity.AmenityMapper;
import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchobject.Amenity;
import com.socialsim.model.core.environment.patchobject.passable.goal.Switch;

import java.util.ArrayList;
import java.util.List;

public class SwitchMapper extends AmenityMapper {

    public static void draw(List<Patch> patches, String type, String facing) {
        for (Patch patch : patches) {
            List<Amenity.AmenityBlock> amenityBlocks = new ArrayList<>();
            int origPatchRow = patch.getMatrixPosition().getRow();
            int origPatchCol = patch.getMatrixPosition().getColumn();

            // FIRST PATCH
            Amenity.AmenityBlock.AmenityBlockFactory amenityBlockFactory = Switch.SwitchBlock.switchBlockFactory;
            Amenity.AmenityBlock amenityBlock = amenityBlockFactory.create(patch, true, true);
            amenityBlocks.add(amenityBlock);
            patch.setAmenityBlock(amenityBlock);

            if (facing.equals("SOUTH")) {
                Patch nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow + 2, origPatchCol);
                Amenity.AmenityBlock nextAmenityBlock = amenityBlockFactory.create(nextPatch, true, false);
                amenityBlocks.add(nextAmenityBlock);
                nextPatch.setAmenityBlock(nextAmenityBlock);
            }

            List<Switch> switches = Main.simulator.getEnvironment().getSwitches();
            Switch switchToAdd;
            switchToAdd = Switch.SwitchFactory.create(amenityBlocks, true, type, facing);
            switches.add(switchToAdd);

            amenityBlocks.forEach(ab -> ab.getPatch().getEnvironment().getAmenityPatchSet().add(ab.getPatch()));
        }
    }

}