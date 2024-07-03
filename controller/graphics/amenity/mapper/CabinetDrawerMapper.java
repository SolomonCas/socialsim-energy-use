package com.socialsim.controller.graphics.amenity.mapper;

import com.socialsim.controller.Main;
import com.socialsim.controller.graphics.amenity.AmenityMapper;
import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchobject.Amenity;
import com.socialsim.model.core.environment.patchobject.passable.goal.CabinetDrawer;

import java.util.ArrayList;
import java.util.List;

public class CabinetDrawerMapper extends AmenityMapper {

    public static void draw(List<Patch> patches, String type, String facing) {
        for (Patch patch : patches) {
            List<Amenity.AmenityBlock> amenityBlocks = new ArrayList<>();
            int origPatchRow = patch.getMatrixPosition().getRow();
            int origPatchCol = patch.getMatrixPosition().getColumn();

            CabinetDrawer cabinetDrawerToAdd;
            List<CabinetDrawer> cabinetDrawer = Main.simulator.getEnvironment().getCabinetDrawers();

            Amenity.AmenityBlock.AmenityBlockFactory amenityBlockFactory = CabinetDrawer.CabinetDrawerBlock.cabinetDrawerBlockFactory;
            Amenity.AmenityBlock amenityBlock;
            
            switch (type) {
                case "CABINET" -> {
                    for (int i = 0; i <= 1; i++) {
                        for (int j = 0; j <= 3; j++) {
                            Patch nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow + i, origPatchCol + j);

                            if (i == 0 && j == 0) {
                                amenityBlock = amenityBlockFactory.create(nextPatch, false, true);
                            }
                            else if (facing.equals("SOUTH") && i == 1) {
                                amenityBlock = amenityBlockFactory.create(nextPatch, true, false);
                            } else {
                                amenityBlock = amenityBlockFactory.create(nextPatch, false, false);
                            }
                            
                            
                            amenityBlocks.add(amenityBlock);
                            nextPatch.setAmenityBlock(amenityBlock);
                        }
                    }
                }
                case "CABINET_1x2" -> {
                    Patch patch1 = Main.simulator.getEnvironment().getPatch(origPatchRow, origPatchCol);
                    amenityBlock = amenityBlockFactory.create(patch1, true, true);
                    amenityBlocks.add(amenityBlock);
                    patch1.setAmenityBlock(amenityBlock);
                    
                    Patch nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow, origPatchCol + 1);
                    amenityBlock = amenityBlockFactory.create(nextPatch, true, false);
                    amenityBlocks.add(amenityBlock);
                    nextPatch.setAmenityBlock(amenityBlock);
                }
                case "DRAWERS" -> {
                    Patch patch1 = Main.simulator.getEnvironment().getPatch(origPatchRow, origPatchCol);
                    amenityBlock = amenityBlockFactory.create(patch1, false, true);
                    amenityBlocks.add(amenityBlock);
                    patch1.setAmenityBlock(amenityBlock);

                    Patch nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol);
                    amenityBlock = amenityBlockFactory.create(nextPatch, true, false);
                    amenityBlocks.add(amenityBlock);
                    nextPatch.setAmenityBlock(amenityBlock);
                }
                case "DOUBLE_DRAWERS" -> {
                    Patch patch1 = Main.simulator.getEnvironment().getPatch(origPatchRow, origPatchCol);
                    amenityBlock = amenityBlockFactory.create(patch1, false, true);
                    amenityBlocks.add(amenityBlock);
                    patch1.setAmenityBlock(amenityBlock);

                    Patch patch2 = Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol);
                    amenityBlock = amenityBlockFactory.create(patch2, true, false);
                    amenityBlocks.add(amenityBlock);
                    patch2.setAmenityBlock(amenityBlock);

                    Patch patch3 = Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol);
                    amenityBlock = amenityBlockFactory.create(patch3, true, false);
                    amenityBlocks.add(amenityBlock);
                    patch3.setAmenityBlock(amenityBlock);
                }
            }


            cabinetDrawerToAdd = CabinetDrawer.CabinetDrawerFactory.create(amenityBlocks, true, type, facing);
            cabinetDrawer.add(cabinetDrawerToAdd);
            
            amenityBlocks.forEach(ab -> ab.getPatch().getEnvironment().getAmenityPatchSet().add(ab.getPatch()));
        }
    }

}