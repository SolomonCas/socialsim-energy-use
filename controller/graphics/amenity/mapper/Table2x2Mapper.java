package com.socialsim.controller.graphics.amenity.mapper;

import com.socialsim.controller.Main;
import com.socialsim.controller.graphics.amenity.AmenityMapper;
import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchobject.Amenity;
import com.socialsim.model.core.environment.patchobject.passable.goal.Table2x2;

import java.util.ArrayList;
import java.util.List;

public class Table2x2Mapper extends AmenityMapper {


    public static void draw(List<Patch> patches) {
        for (Patch patch : patches) {
            List<Amenity.AmenityBlock> amenityBlocks = new ArrayList<>();
            int origPatchRow = patch.getMatrixPosition().getRow();
            int origPatchCol = patch.getMatrixPosition().getColumn();
            Table2x2 table2x2ToAdd;
            List<Table2x2> table2x2s = Main.simulator.getEnvironment().getTable2x2s();


            // FIRST PATCH
            Amenity.AmenityBlock.AmenityBlockFactory amenityBlockFactory = Table2x2.Table2x2Block.table2x2BlockFactory;
            Amenity.AmenityBlock amenityBlock = amenityBlockFactory.create(patch, true, true);
            amenityBlocks.add(amenityBlock);
            patch.setAmenityBlock(amenityBlock);

            // THE REST OF THE TABLE'S PATCHES
            for (int i = 1; i <= 1; i++){
                for (int j = 1; j <= 1; j++) {
                    Patch nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow + i, origPatchCol + j);
                    Amenity.AmenityBlock nextAmenityBlock = amenityBlockFactory.create(nextPatch, false, false);
                    amenityBlocks.add(nextAmenityBlock);
                    nextPatch.setAmenityBlock(nextAmenityBlock);
                }
            }

            table2x2ToAdd = Table2x2.Table2x2Factory.create(amenityBlocks, true);
            table2x2s.add(table2x2ToAdd);
            int index = table2x2s.indexOf(table2x2ToAdd);



            // Set Monitor
            List<Patch> directorMonitorEastPatches = new ArrayList<>();
            directorMonitorEastPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow, origPatchCol + 1));
            MonitorMapper.draw(directorMonitorEastPatches, index, "EAST", "Table2x2");



            amenityBlocks.forEach(ab -> ab.getPatch().getEnvironment().getAmenityPatchSet().add(ab.getPatch()));
        }
    }
}