package com.socialsim.controller.graphics.amenity.mapper;

import com.socialsim.controller.Main;
import com.socialsim.controller.graphics.amenity.AmenityMapper;
import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchobject.Amenity;
import com.socialsim.model.core.environment.patchobject.passable.elevator.Elevator;

import java.util.ArrayList;
import java.util.List;

public class ElevatorMapper extends AmenityMapper {

    public static void draw(List<Patch> patches, Elevator.ElevatorMode elevatorMode, String orientation) {
        for (Patch patch : patches) {
            List<Amenity.AmenityBlock> amenityBlocks = new ArrayList<>();
            int origPatchRow = patch.getMatrixPosition().getRow();
            int origPatchCol = patch.getMatrixPosition().getColumn();

            // TABLE'S FIRST PATCH (UPPER LEFT CORNER)
            Amenity.AmenityBlock.AmenityBlockFactory amenityBlockFactory = Elevator.ElevatorBlock.elevatorBlockFactory;
            Amenity.AmenityBlock amenityBlock = amenityBlockFactory.create(patch, false, true);
            amenityBlocks.add(amenityBlock);
            patch.setAmenityBlock(amenityBlock);

            List<Elevator> elevators = Main.simulator.getEnvironment().getElevators();
            Elevator elevatorToAdd;

            if (orientation.equals("VERTICAL")) {
                // THE REST OF THE TABLE'S PATCHES
                for (int i = 1; i <= 4; i++) {
                    Patch nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow + i, origPatchCol);
                    Amenity.AmenityBlock nextAmenityBlock = amenityBlockFactory.create(nextPatch, false, false);
                    amenityBlocks.add(nextAmenityBlock);
                    nextPatch.setAmenityBlock(nextAmenityBlock);
                }
            }

            elevatorToAdd = Elevator.ElevatorFactory.create(amenityBlocks, true, 1000, elevatorMode, orientation);
            elevators.add(elevatorToAdd);
            amenityBlocks.forEach(ab -> ab.getPatch().getEnvironment().getAmenityPatchSet().add(ab.getPatch()));
        }
    }

}