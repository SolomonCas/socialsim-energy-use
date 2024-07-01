package com.socialsim.controller.graphics.amenity.mapper;


import com.socialsim.controller.Main;
import com.socialsim.controller.graphics.amenity.AmenityMapper;
import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchobject.Amenity;
import com.socialsim.model.core.environment.patchobject.passable.goal.ResearchTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ResearchTableMapper extends AmenityMapper {

    /***** METHOD *****/
    public static void draw(List<Patch> patches, String facing, boolean withAppliance) {
        for (Patch patch : patches) {
            List<Amenity.AmenityBlock> amenityBlocks = new ArrayList<>();
            int origPatchRow = patch.getMatrixPosition().getRow();
            int origPatchCol = patch.getMatrixPosition().getColumn();

            // TABLE'S FIRST PATCH (UPPER LEFT CORNER)
            Amenity.AmenityBlock.AmenityBlockFactory amenityBlockFactory = ResearchTable.ResearchTableBlock.researchTableBlockFactory;
            Amenity.AmenityBlock amenityBlock = amenityBlockFactory.create(patch, true, true);
            amenityBlocks.add(amenityBlock);
            patch.setAmenityBlock(amenityBlock);

            ResearchTable tableToAdd;
            List<ResearchTable> researchTables = Main.simulator.getEnvironment().getResearchTables();
            int index = 0;

            // THE REST OF THE TABLE'S PATCHES
            for (int i = 1; i <= 3; i++) {
                Patch nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow + i, origPatchCol);
                Amenity.AmenityBlock nextAmenityBlock = amenityBlockFactory.create(nextPatch, false, false);
                amenityBlocks.add(nextAmenityBlock);
                nextPatch.setAmenityBlock(nextAmenityBlock);
            }

            if (withAppliance) {
                tableToAdd = ResearchTable.ResearchTableFactory.create(amenityBlocks, true, facing, true);
            } else {
                tableToAdd = ResearchTable.ResearchTableFactory.create(amenityBlocks, true, facing, false);
            }
            researchTables.add(tableToAdd);
            index = researchTables.indexOf(tableToAdd);

            if (facing.equals("WEST")) {

                // EAST CHAIR(S)
                List<Patch> eastChairPatches = new ArrayList<>();
                eastChairPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol - 1));
                eastChairPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 2, origPatchCol - 1));
                ChairMapper.draw(eastChairPatches, index, "EAST", "OFFICE", "ResearchTable");

                // Set Monitor
                List<Patch> researchMonitorWestPatches = new ArrayList<>();
                researchMonitorWestPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 2, origPatchCol));
                MonitorMapper.draw(researchMonitorWestPatches, index, "WEST", "ResearchTable");

            }
            else if (Objects.equals(facing, "EAST")) {
                // WEST CHAIR(S)
                List<Patch> eastChairPatches = new ArrayList<>();
                eastChairPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol + 1));
                eastChairPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 2, origPatchCol + 1));
                ChairMapper.draw(eastChairPatches, index, "WEST", "OFFICE", "ResearchTable");

                // Set Monitor
                List<Patch> researchMonitorEastPatches = new ArrayList<>();
                researchMonitorEastPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 2, origPatchCol));
                MonitorMapper.draw(researchMonitorEastPatches, index, "EAST", "ResearchTable");
            }

            amenityBlocks.forEach(ab -> ab.getPatch().getEnvironment().getAmenityPatchSet().add(ab.getPatch()));
        }
    }

}