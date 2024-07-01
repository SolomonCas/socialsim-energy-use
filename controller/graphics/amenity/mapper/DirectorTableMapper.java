package com.socialsim.controller.graphics.amenity.mapper;

import com.socialsim.controller.Main;
import com.socialsim.controller.graphics.amenity.AmenityMapper;
import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchobject.Amenity;
import com.socialsim.model.core.environment.patchobject.passable.goal.DirectorTable;
import com.socialsim.model.core.environment.patchobject.passable.goal.Monitor;
import com.socialsim.model.core.environment.patchobject.passable.goal.PantryTable;

import java.util.ArrayList;
import java.util.List;

public class DirectorTableMapper extends AmenityMapper {


    public static void draw(List<Patch> patches, String orientation, boolean withAppliance) {
        for (Patch patch : patches) {
            List<Amenity.AmenityBlock> amenityBlocks = new ArrayList<>();
            int origPatchRow = patch.getMatrixPosition().getRow();
            int origPatchCol = patch.getMatrixPosition().getColumn();

            Amenity.AmenityBlock.AmenityBlockFactory amenityBlockFactory = DirectorTable.DirectorTableBlock.directorTableBlockFactory;
            Amenity.AmenityBlock amenityBlock = amenityBlockFactory.create(patch, false, true);
            amenityBlocks.add(amenityBlock);
            patch.setAmenityBlock(amenityBlock);

            DirectorTable directorTableToAdd;
            List<DirectorTable> directorTables = Main.simulator.getEnvironment().getDirectorTables();
            int index = 0;


            if (orientation.equals("HORIZONTAL")) {

                // THE REST OF THE TABLE'S PATCHES
                for (int j = 1; j <= 2; j++) {
                    Patch nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow, origPatchCol + j);
                    Amenity.AmenityBlock nextAmenityBlock = amenityBlockFactory.create(nextPatch, false, false);
                    amenityBlocks.add(nextAmenityBlock);
                    nextPatch.setAmenityBlock(nextAmenityBlock);
                }

                directorTableToAdd = DirectorTable.DirectorTableFactory.create(amenityBlocks, true, orientation);
                directorTables.add(directorTableToAdd);
                index = directorTables.indexOf(directorTableToAdd);

                // NORTH CHAIR(S)
                List<Patch> directorChairNorthPatches = new ArrayList<>();
                directorChairNorthPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol + 1));
                ChairMapper.draw(directorChairNorthPatches, index, "NORTH", "OFFICE", "DirectorTable");

                // Set Monitor
                List<Patch> directorMonitorSouthPatches = new ArrayList<>();
                directorMonitorSouthPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow, origPatchCol + 1));
                MonitorMapper.draw(directorMonitorSouthPatches, index, "SOUTH", "DirectorTable");

            }



            amenityBlocks.forEach(ab -> ab.getPatch().getEnvironment().getAmenityPatchSet().add(ab.getPatch()));
        }
    }
}