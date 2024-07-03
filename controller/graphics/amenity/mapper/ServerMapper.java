package com.socialsim.controller.graphics.amenity.mapper;

import com.socialsim.controller.Main;
import com.socialsim.controller.graphics.amenity.AmenityMapper;
import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchobject.Amenity;
import com.socialsim.model.core.environment.patchobject.passable.goal.Server;
import com.socialsim.model.core.environment.patchobject.passable.goal.Server;

import java.util.ArrayList;
import java.util.List;

public class ServerMapper extends AmenityMapper {

    public static void draw(List<Patch> patches, String type) {
        for (Patch patch : patches) {
            List<Amenity.AmenityBlock> amenityBlocks = new ArrayList<>();
            int origPatchRow = patch.getMatrixPosition().getRow();
            int origPatchCol = patch.getMatrixPosition().getColumn();

            // FIRST PATCH
            Amenity.AmenityBlock.AmenityBlockFactory amenityBlockFactory = Server.ServerBlock.serverBlockFactory;
            Amenity.AmenityBlock amenityBlock = amenityBlockFactory.create(patch, false, true);
            amenityBlocks.add(amenityBlock);
            patch.setAmenityBlock(amenityBlock);


            if (type.equals("TYPE_A")) {
                Patch nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol);
                Amenity.AmenityBlock nextAmenityBlock = amenityBlockFactory.create(nextPatch, true, false);
                amenityBlocks.add(nextAmenityBlock);
                nextPatch.setAmenityBlock(nextAmenityBlock);
            }
            else if (type.equals("TYPE_B")) {
                Patch patch1 = Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol);
                Amenity.AmenityBlock nextAmenityBlock1 = amenityBlockFactory.create(patch1, false, false);
                amenityBlocks.add(nextAmenityBlock1);
                patch1.setAmenityBlock(nextAmenityBlock1);

                Patch nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol);
                Amenity.AmenityBlock nextAmenityBlock2 = amenityBlockFactory.create(nextPatch, true, false);
                amenityBlocks.add(nextAmenityBlock2);
                nextPatch.setAmenityBlock(nextAmenityBlock2);
            }

            List<Server> servers = Main.simulator.getEnvironment().getServers();
            Server serverToAdd;
            serverToAdd = Server.ServerFactory.create(amenityBlocks, true, type);
            servers.add(serverToAdd);

            amenityBlocks.forEach(ab -> ab.getPatch().getEnvironment().getAmenityPatchSet().add(ab.getPatch()));
        }
    }

}