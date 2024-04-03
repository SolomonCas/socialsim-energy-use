package com.socialsim.controller.graphics.amenity.mapper;

import com.socialsim.controller.Main;
import com.socialsim.controller.graphics.amenity.AmenityMapper;
import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchobject.Amenity;
import com.socialsim.model.core.environment.patchobject.passable.gate.Gate;

import java.util.ArrayList;
import java.util.List;

public class OfficeGateMapper extends AmenityMapper {

    public static void draw(List<Patch> patches, Gate.GateMode ugMode) {
        for (Patch patch : patches) {
            List<Amenity.AmenityBlock> amenityBlocks = new ArrayList<>();
            int origPatchRow = patch.getMatrixPosition().getRow();
            int origPatchCol = patch.getMatrixPosition().getColumn();

            Amenity.AmenityBlock.AmenityBlockFactory amenityBlockFactory = Gate.GateBlock.gateBlockFactory;
            Amenity.AmenityBlock amenityBlock = amenityBlockFactory.create(patch, true, true);
            amenityBlocks.add(amenityBlock);
            patch.setAmenityBlock(amenityBlock);

            Patch patch2 = Main.simulator.getEnvironment().getPatch(origPatchRow  + 1, origPatchCol);
            Amenity.AmenityBlock amenityBlock2 = amenityBlockFactory.create(patch2, true, false);
            amenityBlocks.add(amenityBlock2);
            patch2.setAmenityBlock(amenityBlock2);

            Patch patch3 = Main.simulator.getEnvironment().getPatch(origPatchRow + 2, origPatchCol);
            Amenity.AmenityBlock amenityBlock3 = amenityBlockFactory.create(patch3, true, true);
            amenityBlocks.add(amenityBlock3);
            patch3.setAmenityBlock(amenityBlock3);

//            Patch patch4 = Main.simulator.getEnvironment().getPatch(origPatchRow  + 3, origPatchCol);
//            Amenity.AmenityBlock amenityBlock4 = amenityBlockFactory.create(patch4, true, false);
//            amenityBlocks.add(amenityBlock4);
//            patch4.setAmenityBlock(amenityBlock4);

            Gate officeGateToAdd =Gate.GateFactory.create(amenityBlocks, true, 10, ugMode);
            Main.simulator.getEnvironment().getGates().add(officeGateToAdd);
            amenityBlocks.forEach(ab -> ab.getPatch().getEnvironment().getAmenityPatchSet().add(ab.getPatch()));
        }
    }

}