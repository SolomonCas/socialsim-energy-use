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


            if (facing.equals("WEST")) {
                // THE REST OF THE TABLE'S PATCHES
                for (int i = 1; i <= 3; i++) {
                    Patch nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow + i, origPatchCol);
                    Amenity.AmenityBlock nextAmenityBlock = amenityBlockFactory.create(nextPatch, false, false);
                    amenityBlocks.add(nextAmenityBlock);
                    nextPatch.setAmenityBlock(nextAmenityBlock);
                }

                tableToAdd = ResearchTable.ResearchTableFactory.create(amenityBlocks, true, facing);
                researchTables.add(tableToAdd);
                index = researchTables.indexOf(tableToAdd);

                // EAST CHAIR(S)
                List<Patch> eastChairPatches = new ArrayList<>();
                eastChairPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol - 1));
                eastChairPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 2, origPatchCol - 1));
                ChairMapper.draw(eastChairPatches, index, "EAST", "OFFICE", "ResearchTable");

                // Set Monitor
                if (withAppliance) {
                    List<Patch> researchMonitorPatches = new ArrayList<>();
                    researchMonitorPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 2, origPatchCol));
                    MonitorMapper.draw(researchMonitorPatches, index, "WEST", "ResearchTable");
                }

            }
            else if (Objects.equals(facing, "EAST")) {
                // THE REST OF THE TABLE'S PATCHES
                for (int i = 1; i <= 3; i++) {
                    Patch nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow + i, origPatchCol);
                    Amenity.AmenityBlock nextAmenityBlock = amenityBlockFactory.create(nextPatch, false, false);
                    amenityBlocks.add(nextAmenityBlock);
                    nextPatch.setAmenityBlock(nextAmenityBlock);
                }

                tableToAdd = ResearchTable.ResearchTableFactory.create(amenityBlocks, true, facing);
                researchTables.add(tableToAdd);
                index = researchTables.indexOf(tableToAdd);

                // WEST CHAIR(S)
                List<Patch> westChairPatches = new ArrayList<>();
                westChairPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol + 1));
                westChairPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 2, origPatchCol + 1));
                ChairMapper.draw(westChairPatches, index, "WEST", "OFFICE", "ResearchTable");

                // Set Monitor
                if (withAppliance) {
                    List<Patch> researchMonitorPatches = new ArrayList<>();
                    researchMonitorPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 2, origPatchCol));
                    MonitorMapper.draw(researchMonitorPatches, index, "EAST", "ResearchTable");
                }
            }
            else if (Objects.equals(facing, "SOUTH")) {
                // THE REST OF THE TABLE'S PATCHES
                for (int j = 1; j <= 3; j++) {
                    Patch nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow, origPatchCol + j);
                    Amenity.AmenityBlock nextAmenityBlock = amenityBlockFactory.create(nextPatch, false, false);
                    amenityBlocks.add(nextAmenityBlock);
                    nextPatch.setAmenityBlock(nextAmenityBlock);
                }

                tableToAdd = ResearchTable.ResearchTableFactory.create(amenityBlocks, true, facing);
                researchTables.add(tableToAdd);
                index = researchTables.indexOf(tableToAdd);

                // NORTH CHAIR(S)
                List<Patch> northChairPatches = new ArrayList<>();
                northChairPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol + 1));
                northChairPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol + 2));
                ChairMapper.draw(northChairPatches, index, "NORTH", "OFFICE", "ResearchTable");

                // Set Monitor
                if (withAppliance) {
                    List<Patch> researchMonitorPatches = new ArrayList<>();
                    researchMonitorPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow, origPatchCol + 1));
                    MonitorMapper.draw(researchMonitorPatches, index, "SOUTH", "ResearchTable");
                }
            }

            amenityBlocks.forEach(ab -> ab.getPatch().getEnvironment().getAmenityPatchSet().add(ab.getPatch()));
        }
    }

}