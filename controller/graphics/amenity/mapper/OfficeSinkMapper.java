package com.socialsim.controller.graphics.amenity.mapper;

import com.socialsim.controller.Main;
import com.socialsim.controller.graphics.amenity.AmenityMapper;
import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchobject.Amenity;
import com.socialsim.model.core.environment.patchobject.passable.goal.OfficeSink;

import java.util.ArrayList;
import java.util.List;

public class OfficeSinkMapper extends AmenityMapper {

    public static void draw(List<Patch> patches, String facing) {
        for (Patch patch : patches) {
            List<Amenity.AmenityBlock> amenityBlocks = new ArrayList<>();
            Amenity.AmenityBlock.AmenityBlockFactory amenityBlockFactory = OfficeSink.OfficeSinkBlock.officeSinkBlockFactory;
            Amenity.AmenityBlock amenityBlock = amenityBlockFactory.create(patch, true, true);
            amenityBlocks.add(amenityBlock);
            patch.setAmenityBlock(amenityBlock);

            OfficeSink officeSinkToAdd = OfficeSink.OfficeSinkFactory.create(amenityBlocks, true, facing);
            Main.simulator.getEnvironment().getOfficeSinks().add(officeSinkToAdd);
            amenityBlocks.forEach(ab -> ab.getPatch().getEnvironment().getAmenityPatchSet().add(ab.getPatch()));
        }
    }

}