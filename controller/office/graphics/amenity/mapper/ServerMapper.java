package com.socialsim.controller.office.graphics.amenity.mapper;

import com.socialsim.controller.Main;
import com.socialsim.controller.generic.graphics.amenity.AmenityMapper;
import com.socialsim.model.core.environment.generic.Patch;
import com.socialsim.model.core.environment.generic.patchobject.Amenity;
import com.socialsim.model.core.environment.office.patchobject.passable.goal.Server;

import java.util.ArrayList;
import java.util.List;

public class ServerMapper extends AmenityMapper {

    public static void draw(List<Patch> patches) {
        for (Patch patch : patches) {
            List<Amenity.AmenityBlock> amenityBlocks = new ArrayList<>();
            int origPatchRow = patch.getMatrixPosition().getRow();
            int origPatchCol = patch.getMatrixPosition().getColumn();

            Amenity.AmenityBlock.AmenityBlockFactory amenityBlockFactory = Server.ServerBlock.serverBlockFactory;
            Amenity.AmenityBlock amenityBlock = amenityBlockFactory.create(patch, false, true);
            amenityBlocks.add(amenityBlock);
            patch.setAmenityBlock(amenityBlock);

            Patch patch1 = Main.officeSimulator.getOffice().getPatch(origPatchRow + 1, origPatchCol);
            Amenity.AmenityBlock amenityBlock2 = amenityBlockFactory.create(patch1, true, false);
            amenityBlocks.add(amenityBlock2);
            patch1.setAmenityBlock(amenityBlock2);

            Patch patch2 = Main.officeSimulator.getOffice().getPatch(origPatchRow, origPatchCol + 1);
            Amenity.AmenityBlock amenityBlock3 = amenityBlockFactory.create(patch2, false, false);
            amenityBlocks.add(amenityBlock3);
            patch2.setAmenityBlock(amenityBlock3);

            Patch patch3 = Main.officeSimulator.getOffice().getPatch(origPatchRow + 1, origPatchCol + 1);
            Amenity.AmenityBlock amenityBlock4 = amenityBlockFactory.create(patch3, true, false);
            amenityBlocks.add(amenityBlock4);
            patch3.setAmenityBlock(amenityBlock4);

            Server serverToAdd = Server.ServerFactory.create(amenityBlocks, true);
            Main.officeSimulator.getOffice().getServers().add(serverToAdd);
            amenityBlocks.forEach(ab -> ab.getPatch().getEnvironment().getAmenityPatchSet().add(ab.getPatch()));
        }
    }

}