package com.socialsim.controller.graphics.amenity.mapper;

import com.socialsim.controller.Main;
import com.socialsim.controller.graphics.amenity.AmenityMapper;
import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchobject.Amenity;
import com.socialsim.model.core.environment.patchobject.passable.goal.Chair;

import java.util.ArrayList;
import java.util.List;

public class ChairMapper extends AmenityMapper {

    public static void draw(List<Patch> patches, int index, String facing, String type, String belongsTo) {
        for (Patch patch : patches) {
            List<Amenity.AmenityBlock> amenityBlocks = new ArrayList<>();
            Amenity.AmenityBlock.AmenityBlockFactory amenityBlockFactory = Chair.ChairBlock.chairBlockFactory;
            Amenity.AmenityBlock amenityBlock = amenityBlockFactory.create(patch, true, true);
            amenityBlocks.add(amenityBlock);
            patch.setAmenityBlock(amenityBlock);

            Chair chairToAdd = Chair.ChairFactory.create(amenityBlocks, true, facing, type);
            switch (belongsTo) {
                case "DirectorTable" -> {
                    Main.simulator.getEnvironment().getDirectorTables().get(index).getDirectorChairs().add(chairToAdd);
                }
                case "HumanExpTable" -> {
                    Main.simulator.getEnvironment().getHumanExpTables().get(index).getHumanExpChairs().add(chairToAdd);
                }
                case "LearningTable" -> {
                    Main.simulator.getEnvironment().getLearningTables().get(index).getLearningChairs().add(chairToAdd);
                }
                case "MeetingTable" -> {
                    Main.simulator.getEnvironment().getMeetingTables().get(index).getMeetingChairs().add(chairToAdd);
                }
                case "PantryTable" -> {
                    Main.simulator.getEnvironment().getPantryTables().get(index).getPantryChairs().add(chairToAdd);
                }
                case "ReceptionTable" -> {
                    Main.simulator.getEnvironment().getReceptionTables().get(index).getReceptionChairs().add(chairToAdd);
                }
                case "ResearchTable" -> {
                    Main.simulator.getEnvironment().getResearchTables().get(index).getResearchChairs().add(chairToAdd);
                }
                case "SoloTable" -> {
                    Main.simulator.getEnvironment().getSoloTables().get(index).getSoloChairs().add(chairToAdd);
                }
                case "DataCollTable" -> {
                    Main.simulator.getEnvironment().getDataCollTables().get(index).getDataCollChairs().add(chairToAdd);
                }
                case "MESATable" -> {
                    Main.simulator.getEnvironment().getMesaTables().get(index).getMesaChairs().add(chairToAdd);
                }
                case "Cubicle" -> {
                    Main.simulator.getEnvironment().getCubicles().get(index).getCubicleChairs().add(chairToAdd);
                }
                default -> {
                    Main.simulator.getEnvironment().getChairs().add(chairToAdd);
                }
            }
            amenityBlocks.forEach(ab -> ab.getPatch().getEnvironment().getAmenityPatchSet().add(ab.getPatch()));
        }
    }

}