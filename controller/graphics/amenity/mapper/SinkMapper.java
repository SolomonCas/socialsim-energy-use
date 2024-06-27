package com.socialsim.controller.graphics.amenity.mapper;

import com.socialsim.controller.Main;
import com.socialsim.controller.graphics.amenity.AmenityMapper;
import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchobject.Amenity;
import com.socialsim.model.core.environment.patchobject.passable.goal.Sink;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SinkMapper extends AmenityMapper {

    public static void draw(List<Patch> patches, String facing) {
        for (Patch patch : patches) {
            List<Amenity.AmenityBlock> amenityBlocks = new ArrayList<>();
            int origPatchRow = patch.getMatrixPosition().getRow();
            int origPatchCol = patch.getMatrixPosition().getColumn();

            // FIRST PATCH
            Amenity.AmenityBlock.AmenityBlockFactory amenityBlockFactory = Sink.SinkBlock.sinkBlockFactory;
            Amenity.AmenityBlock amenityBlock = amenityBlockFactory.create(patch, false, true);
            amenityBlocks.add(amenityBlock);
            patch.setAmenityBlock(amenityBlock);

            List<Sink> Sinks = Main.simulator.getEnvironment().getSinks();
            Sink sinkToAdd = Sink.SinkFactory.create(amenityBlocks, true, facing);
            Sinks.add(sinkToAdd);

            amenityBlocks.forEach(ab -> ab.getPatch().getEnvironment().getAmenityPatchSet().add(ab.getPatch()));
        }
    }

}