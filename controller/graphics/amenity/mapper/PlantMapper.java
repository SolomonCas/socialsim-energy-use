package com.socialsim.controller.graphics.amenity.mapper;

import com.socialsim.controller.Main;
import com.socialsim.controller.graphics.amenity.AmenityMapper;
import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchobject.Amenity;
import com.socialsim.model.core.environment.patchobject.passable.goal.Plant;
import com.socialsim.model.core.environment.patchobject.passable.goal.Plant;

import java.util.ArrayList;
import java.util.List;

public class PlantMapper extends AmenityMapper {

    public static void draw(List<Patch> patches) {
        for (Patch patch : patches) {
            List<Amenity.AmenityBlock> amenityBlocks = new ArrayList<>();
            int origPatchRow = patch.getMatrixPosition().getRow();
            int origPatchCol = patch.getMatrixPosition().getColumn();

            // FIRST PATCH
            Amenity.AmenityBlock.AmenityBlockFactory amenityBlockFactory = Plant.PlantBlock.plantBlockFactory;
            Amenity.AmenityBlock amenityBlock = amenityBlockFactory.create(patch, true, true);
            amenityBlocks.add(amenityBlock);
            patch.setAmenityBlock(amenityBlock);

            List<Plant> plants = Main.simulator.getEnvironment().getPlants();
            Plant plantToAdd;
            plantToAdd = Plant.PlantFactory.create(amenityBlocks, true);
            plants.add(plantToAdd);

            amenityBlocks.forEach(ab -> ab.getPatch().getEnvironment().getAmenityPatchSet().add(ab.getPatch()));
        }
    }

}