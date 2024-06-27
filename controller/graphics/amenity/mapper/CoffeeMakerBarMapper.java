package com.socialsim.controller.graphics.amenity.mapper;

import com.socialsim.controller.Main;
import com.socialsim.controller.graphics.amenity.AmenityMapper;
import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchobject.Amenity;
import com.socialsim.model.core.environment.patchobject.passable.goal.CoffeeMakerBar;

import java.util.ArrayList;
import java.util.List;

public class CoffeeMakerBarMapper extends AmenityMapper {

    public static void draw(List<Patch> patches) {
        for (Patch patch : patches) {
            List<Amenity.AmenityBlock> amenityBlocks = new ArrayList<>();
            int origPatchRow = patch.getMatrixPosition().getRow();
            int origPatchCol = patch.getMatrixPosition().getColumn();

            // FIRST PATCH
            Amenity.AmenityBlock.AmenityBlockFactory amenityBlockFactory = CoffeeMakerBar.CoffeeMakerBarBlock.coffeeMakerBarBlockFactory;
            Amenity.AmenityBlock amenityBlock = amenityBlockFactory.create(patch, false, true);
            amenityBlocks.add(amenityBlock);
            patch.setAmenityBlock(amenityBlock);

            // SECOND PATCH
            Patch nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol);
            Amenity.AmenityBlock nextAmenityBlock = amenityBlockFactory.create(nextPatch, true, false);
            amenityBlocks.add(nextAmenityBlock);
            nextPatch.setAmenityBlock(nextAmenityBlock);

            List<CoffeeMakerBar> coffeeMakerBars = Main.simulator.getEnvironment().getCoffeeMakerBars();
            CoffeeMakerBar coffeeMakerBarToAdd;
            coffeeMakerBarToAdd = CoffeeMakerBar.CoffeeMakerBarFactory.create(amenityBlocks, true);
            coffeeMakerBars.add(coffeeMakerBarToAdd);

            amenityBlocks.forEach(ab -> ab.getPatch().getEnvironment().getAmenityPatchSet().add(ab.getPatch()));
        }
    }

}