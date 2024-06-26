package com.socialsim.controller.graphics.amenity.mapper;

import com.socialsim.controller.Main;
import com.socialsim.controller.graphics.amenity.AmenityMapper;
import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchobject.Amenity;
import com.socialsim.model.core.environment.patchobject.passable.goal.LearningChair;

import java.util.ArrayList;
import java.util.List;

public class LearningChairMapper extends AmenityMapper {

    public static void draw(List<Patch> patches, int index, String facing, String type) {
        for (Patch patch : patches) {
            List<Amenity.AmenityBlock> amenityBlocks = new ArrayList<>();
            Amenity.AmenityBlock.AmenityBlockFactory amenityBlockFactory = LearningChair.LearningChairBlock.learningChairBlockFactory;
            Amenity.AmenityBlock amenityBlock = amenityBlockFactory.create(patch, true, true);
            amenityBlocks.add(amenityBlock);
            patch.setAmenityBlock(amenityBlock);

            LearningChair chairToAdd = LearningChair.LearningChairFactory.create(amenityBlocks, true, facing, type);
            Main.simulator.getEnvironment().getLearningTables().get(index).getLearningChairs().add(chairToAdd);
            amenityBlocks.forEach(ab -> ab.getPatch().getEnvironment().getAmenityPatchSet().add(ab.getPatch()));
        }
    }

}