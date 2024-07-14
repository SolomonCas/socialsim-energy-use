package com.socialsim.controller.graphics.amenity.mapper;

import com.socialsim.controller.Main;
import com.socialsim.controller.graphics.amenity.AmenityMapper;
import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchobject.Amenity;
import com.socialsim.model.core.environment.patchobject.passable.goal.OfficeSink;
import com.socialsim.model.core.environment.patchobject.passable.goal.Sink;
import com.socialsim.model.core.environment.patchobject.passable.goal.Switch;
import com.socialsim.model.core.environment.patchobject.passable.goal.Sink;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SinkMapper extends AmenityMapper {

    public static void draw(List<Patch> patches, String facing, String type) {
        for (Patch patch : patches) {
            List<Amenity.AmenityBlock> amenityBlocks = new ArrayList<>();
            // FIRST PATCH
            Amenity.AmenityBlock.AmenityBlockFactory amenityBlockFactory = Sink.SinkBlock.sinkBlockFactory;
            Amenity.AmenityBlock amenityBlock = amenityBlockFactory.create(patch, true, true);
            amenityBlocks.add(amenityBlock);
            patch.setAmenityBlock(amenityBlock);

            switch (type) {
                case "SINK" -> {
                    Sink sinkToAdd = Sink.SinkFactory.create(amenityBlocks, true, facing);
                    Main.simulator.getEnvironment().getSinks().add(sinkToAdd);
                }
                case "OFFICE_SINK" -> {
                    OfficeSink officeSinkToAdd = OfficeSink.OfficeSinkFactory.create(amenityBlocks, true, facing);
                    Main.simulator.getEnvironment().getOfficeSinks().add(officeSinkToAdd);
                }
            }

            amenityBlocks.forEach(ab -> ab.getPatch().getEnvironment().getAmenityPatchSet().add(ab.getPatch()));
        }
    }

}