package com.socialsim.controller.graphics.amenity.mapper;

import com.socialsim.controller.Main;
import com.socialsim.controller.graphics.amenity.AmenityMapper;
import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchobject.Amenity;
import com.socialsim.model.core.environment.patchobject.passable.goal.Monitor;

import java.util.ArrayList;
import java.util.List;

public class MonitorMapper extends AmenityMapper {

    public static void draw(List<Patch> patches, int index, String facing, String belongsTo) {
        for (Patch patch : patches) {
            List<Amenity.AmenityBlock> amenityBlocks = new ArrayList<>();
            Amenity.AmenityBlock.AmenityBlockFactory amenityBlockFactory = Monitor.MonitorBlock.monitorBlockFactory;
            Amenity.AmenityBlock amenityBlock = amenityBlockFactory.create(patch, true, true);
            amenityBlocks.add(amenityBlock);
            patch.setAmenityBlock(amenityBlock);

            Monitor monitorToAdd = Monitor.MonitorFactory.create(amenityBlocks, true, facing);
            switch (belongsTo) {
                case "DirectorTable" -> {
                    Main.simulator.getEnvironment().getDirectorTables().get(index).getMonitors().add(monitorToAdd);
                }
                case "ResearchTable" -> {
                    Main.simulator.getEnvironment().getResearchTables().get(index).getMonitors().add(monitorToAdd);
                }
                case "Cubicle" -> {
                    Main.simulator.getEnvironment().getCubicles().get(index).getMonitors().add(monitorToAdd);
                }
            }
            amenityBlocks.forEach(ab -> ab.getPatch().getEnvironment().getAmenityPatchSet().add(ab.getPatch()));
        }
    }

}